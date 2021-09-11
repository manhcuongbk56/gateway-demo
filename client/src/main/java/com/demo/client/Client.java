package com.demo.client;

import com.demo.common.constant.ResponseCode;
import com.demo.common.message.cancelorder.CancelStockOrderResponse;
import com.demo.common.message.messagetype.MessageType;
import com.demo.common.message.orderhistory.GetStockOrderHistoryResponse;
import com.demo.common.message.stockorder.OrderStockResponse;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.common.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@SuppressWarnings({"unchecked", "rawtypes"})
/**
 * A simple client with async api
 * every send request method will
 *  + encode the request to byteBuf (every request will have an unique requestId)
 *  + send to socket (channel)
 *  + return a completable future and put this future to the {@link Client#futureResponseMap}
 *  the caller can wait on this completable future
 *  when response from server arrived, detect the right handler by header response type
 *  get the requestId, decode the response, get the completable future of this request from {@link Client#futureResponseMap}
 *  by requestId and complete this future with the encoded response.
 *  After that, caller can receive the result.
 *  For example, please see {@link Client#getStockPrice} and {@link Client#handleGetStockPriceResponse(ByteBuf)}
 */
public class Client {

    private static LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(4);
    private final Channel channel;


    private Map<UUID, CompletableFuture> futureResponseMap;
    private Map<Integer, ResponseHandler> decoders;

    public Client(Channel channel) {
        this.channel = channel;
        channel.pipeline().addLast(lengthFieldPrepender);
        channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        channel.pipeline().addLast(new Reader());
        futureResponseMap = new ConcurrentHashMap<>();
        decoders = new HashMap<>();
        decoders.put(MessageType.Response.GET_PRICE_RESPONSE, this::handleGetStockPriceResponse);
        decoders.put(MessageType.Response.ORDER_STOCK_RESPONSE, this::handleOrderStockResponse);
        decoders.put(MessageType.Response.CANCEL_STOCK_ORDER_RESPONSE, this::handleCancelStockOrderResponse);
        decoders.put(MessageType.Response.GET_STOCK_ORDER_HISTORY_RESPONSE, this::handleGetStockOrderHistoryResponse);
        decoders.put(MessageType.Response.ORDER_STOCK_COMPLETED, this::handleStockOrderCompletedResponse);
    }

    private void send(ByteBuf byteBuf) {
        channel.writeAndFlush(byteBuf);
    }

    public void readResponse(ChannelHandlerContext ctx, Object msg) {
        DatagramPacket datagramPacket = (DatagramPacket) msg;
        ByteBuf responseByteBuf = datagramPacket.content();
        if (responseByteBuf.readableBytes() <= 0){
            return;
        }
        int responseType = responseByteBuf.readInt();
        //based on the response type => get the right handler and handle
        decoders.get(responseType).handle(responseByteBuf);
    }

    private void handleGetStockPriceResponse(ByteBuf responseByteBuf) {
        //read request id, start encode response
        UUID requestId = ByteBufUtils.readUUID(responseByteBuf);
        responseByteBuf.skipBytes(1);
        String responseCode = ByteBufUtils.readString(responseByteBuf, 4);
        StockPriceResponse response;
        if (Objects.equals(responseCode, ResponseCode.FAIL.getCode())) {
            log.info("Request fail: requestId {}, responseCode {}", requestId, responseCode);
            response = StockPriceResponse.fail(requestId);
        } else {
            responseByteBuf.skipBytes(1);
            String name = ByteBufUtils.read20BytesString(responseByteBuf);
            responseByteBuf.skipBytes(1);
            double price = responseByteBuf.readDouble();
            responseByteBuf.skipBytes(2);
            response = StockPriceResponse.success(requestId, name, price);
        }
        //after done encode response => get the completable future of this requestId => complete with parsed response
        futureResponseMap.get(requestId).complete(response);
    }

    private void handleCancelStockOrderResponse(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.skipBytes(1);
        String responseCode = ByteBufUtils.readString(byteBuf, 4);
        CancelStockOrderResponse response = new CancelStockOrderResponse(requestId, responseCode);
        futureResponseMap.get(requestId).complete(response);
    }

    @SuppressWarnings("unchecked")
    private void handleGetStockOrderHistoryResponse(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.skipBytes(1);
        int totalDay = byteBuf.readInt();
        List<GetStockOrderHistoryResponse.DayHistory> days = new ArrayList<>(totalDay);
        for (int i = 0; i < totalDay; i++) {
            days.add(parseDayHistory(byteBuf));
        }
        GetStockOrderHistoryResponse response = new GetStockOrderHistoryResponse(requestId, days);
        futureResponseMap.get(requestId).complete(response);
    }

    private GetStockOrderHistoryResponse.DayHistory parseDayHistory(ByteBuf byteBuf) {
        int size = byteBuf.readInt();
        LocalDate day = ByteBufUtils.readDate(byteBuf);
        List<GetStockOrderHistoryResponse.StockOrderInfo> orders = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            orders.add(parseStockOrderInfo(byteBuf));
        }
        return new GetStockOrderHistoryResponse.DayHistory(day, orders);
    }

    private GetStockOrderHistoryResponse.StockOrderInfo parseStockOrderInfo(ByteBuf byteBuf) {
        String stockName = ByteBufUtils.read20BytesString(byteBuf);
        byteBuf.skipBytes(1);
        String sellOrBuy = ByteBufUtils.read20BytesString(byteBuf);
        byteBuf.skipBytes(1);
        long quantity = byteBuf.readLong();
        byteBuf.skipBytes(1);
        double price = byteBuf.readDouble();
        byteBuf.skipBytes(1);
        boolean isSuccess = byteBuf.readBoolean();
        return new GetStockOrderHistoryResponse.StockOrderInfo(stockName, sellOrBuy, quantity, price, isSuccess);
    }


    private void handleStockOrderCompletedResponse(ByteBuf byteBuf) {
        long orderNo = byteBuf.readLong();
        log.info("Order no: {} completed", orderNo);
    }


    private void handleOrderStockResponse(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.skipBytes(1);
        String responseCode = ByteBufUtils.readString(byteBuf, 4);
        OrderStockResponse response;
        if (Objects.equals(responseCode, ResponseCode.FAIL.getCode())) {
            log.info("Request fail: requestId {}, responseCode {}", requestId, responseCode);
            response = OrderStockResponse.fail(requestId);
        } else {
            byteBuf.skipBytes(1);
            long orderNo = byteBuf.readLong();
            response = OrderStockResponse.success(requestId, orderNo);
        }
        futureResponseMap.get(requestId).complete(response);
    }


    public CompletableFuture<StockPriceResponse> getStockPrice(String stockName) {
        //Start to encode request
        ByteBuf request = ByteBufAllocator.DEFAULT.buffer(46);
        request.writeInt(MessageType.Request.GET_PRICE);
        UUID requestId = ByteBufUtils.writeUUID(request);
        request.writeByte('|');
        for (int i = 0; i < 4; i++) {
            request.writeByte(' ');
        }
        request.writeByte('|');
        ByteBufUtils.write20BytesString(request, stockName);
        //done encode => send to server = write to socket (channel)
        //Create a completable future to return to caller to wait on this
        CompletableFuture<StockPriceResponse> responseFuture = new CompletableFuture<>();
        //Put complete future to map, to get when response arrived, should put before send to guarantee when response arrive, our response map
        // already have the completable future of response.
        futureResponseMap.put(requestId, responseFuture);
        send(request);
        log.info("Done send request to server");
        // return
        return responseFuture;
    }

    public CompletableFuture<OrderStockResponse> orderStock(String stockName, String sellOrBuy, long quantity, double price) {
        ByteBuf request = ByteBufAllocator.DEFAULT.buffer(100);
        request.writeInt(MessageType.Request.STOCK_ORDER);
        UUID requestId = ByteBufUtils.writeUUID(request);
        request.writeByte('|');
        ByteBufUtils.write20BytesString(request, stockName);
        request.writeByte('|');
        ByteBufUtils.write20BytesString(request, sellOrBuy);
        request.writeByte('|');
        request.writeLong(quantity);
        request.writeByte('|');
        request.writeDouble(price);
        CompletableFuture<OrderStockResponse> responseFuture = new CompletableFuture<>();
        futureResponseMap.put(requestId, responseFuture);
        send(request);
        return responseFuture;
    }

    public CompletableFuture<CancelStockOrderResponse> cancelStockOrderRequest(long orderNo) {
        ByteBuf request = ByteBufAllocator.DEFAULT.buffer(45);
        request.writeInt(MessageType.Request.CANCEL_ORDER);
        UUID requestId = ByteBufUtils.writeUUID(request);
        request.writeByte('|');
        request.writeLong(orderNo);
        CompletableFuture<CancelStockOrderResponse> responseFuture = new CompletableFuture<>();
        futureResponseMap.put(requestId, responseFuture);
        send(request);
        return responseFuture;
    }

    public CompletableFuture<GetStockOrderHistoryResponse> getOrderHistory(long clientAccountNo, LocalDate fromDate, LocalDate toDate) {
        ByteBuf request = ByteBufAllocator.DEFAULT.buffer(67);
        request.writeInt(MessageType.Request.GET_HISTORY);
        UUID requestId = ByteBufUtils.writeUUID(request);
        request.writeByte('|');
        request.writeLong(clientAccountNo);
        request.writeByte('|');
        ByteBufUtils.writeDate(request, fromDate);
        request.writeByte('|');
        ByteBufUtils.writeDate(request, toDate);
        CompletableFuture<GetStockOrderHistoryResponse> responseFuture = new CompletableFuture<>();
        futureResponseMap.put(requestId, responseFuture);
        send(request);
        return responseFuture;
    }

    private class Reader extends ChannelInboundHandlerAdapter {

        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            log.info("receive total {} response", count.incrementAndGet());
            Client.this.readResponse(ctx, msg);
        }

    }

}
