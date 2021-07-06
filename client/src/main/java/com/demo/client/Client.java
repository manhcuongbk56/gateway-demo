package com.demo.client;

import com.demo.common.constant.ResponseCode;
import com.demo.common.message.cancelorder.CancelStockOrderResponse;
import com.demo.common.message.messagetype.MessageType;
import com.demo.common.message.orderhistory.StockOrderHistoryResponse;
import com.demo.common.message.stockorder.OrderStockResponse;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.common.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
        decoders.put(MessageType.Response.GET_STOCK_ORDER_HISTORY_RESPONSE, this::handleGetStockPriceResponse);
        decoders.put(MessageType.Response.ORDER_STOCK_COMPLETED, this::handleStockOrderCompletedResponse);
    }

    private void send(ByteBuf byteBuf) {
        channel.writeAndFlush(byteBuf);
    }

    public void readResponse(ChannelHandlerContext ctx, Object msg) {
        ByteBuf responseByteBuf = (ByteBuf) msg;
        int responseType = responseByteBuf.readInt();
        decoders.get(responseType).handle(responseByteBuf);
    }

    private void handleGetStockPriceResponse(ByteBuf responseByteBuf) {
        UUID requestId = ByteBufUtils.readUUID(responseByteBuf);
        responseByteBuf.readByte();
        String responseCode = responseByteBuf.readBytes(4).toString(CharsetUtil.UTF_8);
        StockPriceResponse response;
        if (Objects.equals(responseCode, ResponseCode.FAIL.getCode())) {
            log.info("Request fail: requestId {}, responseCode {}", requestId, responseCode);
            response = StockPriceResponse.fail(requestId);
        } else {
            responseByteBuf.readByte();
            String name = responseByteBuf.readBytes(20).toString(CharsetUtil.UTF_8).trim();
            responseByteBuf.readByte();
            double price = responseByteBuf.readDouble();
            responseByteBuf.readBytes(2);
            response = StockPriceResponse.success(requestId, name, price);
        }
        futureResponseMap.get(requestId).complete(response);
    }

    private void handleCancelStockOrderResponse(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.readByte();
        String responseCode = byteBuf.readBytes(4).toString(CharsetUtil.UTF_8);
        CancelStockOrderResponse response = new CancelStockOrderResponse(requestId, responseCode);
        futureResponseMap.get(requestId).complete(response);
    }

    @SuppressWarnings("unchecked")
    private void handleGetStockOrderHistoryResponse(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.readByte();
        int totalDay = byteBuf.readInt();
         List<StockOrderHistoryResponse.DayHistory> days = new ArrayList<>(totalDay);
        for (int i = 0; i< totalDay; i++){
            days.add(parseDayHistory(byteBuf));
        }
        StockOrderHistoryResponse response = new StockOrderHistoryResponse(requestId, days);
        futureResponseMap.get(requestId).complete(response);
    }

    private StockOrderHistoryResponse.DayHistory parseDayHistory(ByteBuf byteBuf){
        int size = byteBuf.readInt();
        LocalDate day = ByteBufUtils.readDate(byteBuf);
        List<StockOrderHistoryResponse.StockOrderInfo> orders = new ArrayList<>(size);
        for (int i = 0; i < size; i++){
            orders.add(parseStockOrderInfo(byteBuf));
        }
        return new StockOrderHistoryResponse.DayHistory(day, orders);
    }

    private StockOrderHistoryResponse.StockOrderInfo parseStockOrderInfo(ByteBuf byteBuf){
        String stockName = ByteBufUtils.read20BytesString(byteBuf);
        byteBuf.readByte();
        String sellOrBuy = ByteBufUtils.read20BytesString(byteBuf);
        byteBuf.readByte();
        long quantity = byteBuf.readLong();
        byteBuf.readByte();
        double price = byteBuf.readDouble();
        byteBuf.readByte();
        boolean isSuccess = byteBuf.readBoolean();
        return new StockOrderHistoryResponse.StockOrderInfo(stockName, sellOrBuy, quantity, price, isSuccess);
    }


    private void handleStockOrderCompletedResponse(ByteBuf byteBuf) {
        long orderNo = byteBuf.readLong();
        log.info("Order no: {} completed", orderNo);
    }


    private void handleOrderStockResponse(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.readByte();
        String responseCode = byteBuf.readBytes(4).toString(CharsetUtil.UTF_8);
        OrderStockResponse response;
        if (Objects.equals(responseCode, ResponseCode.FAIL.getCode())) {
            log.info("Request fail: requestId {}, responseCode {}", requestId, responseCode);
            response = OrderStockResponse.fail(requestId);
        } else {
            byteBuf.readByte();
            long orderNo = byteBuf.readLong();
            response = OrderStockResponse.success(requestId, orderNo);
        }
        futureResponseMap.get(requestId).complete(response);
    }




    public CompletableFuture<StockPriceResponse> getStockPrice(String stockName) {
        ByteBuf request = ByteBufAllocator.DEFAULT.buffer(46);
        request.writeInt(MessageType.Request.GET_PRICE);
        UUID requestId = ByteBufUtils.writeUUID(request);
        request.writeByte('|');
        for (int i = 0; i < 4; i++) {
            request.writeByte(' ');
        }
        request.writeByte('|');
        ByteBufUtils.write20BytesString(request, stockName);
        send(request);
        CompletableFuture<StockPriceResponse> responseFuture = new CompletableFuture<>();
        futureResponseMap.put(requestId, responseFuture);
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
        send(request);
        CompletableFuture<OrderStockResponse> responseFuture = new CompletableFuture<>();
        futureResponseMap.put(requestId, responseFuture);
        return responseFuture;
    }

    public CompletableFuture<CancelStockOrderResponse> cancelStockOrderRequest(long orderNo) {
        ByteBuf request = ByteBufAllocator.DEFAULT.buffer(45);
        request.writeInt(MessageType.Request.STOCK_ORDER);
        UUID requestId = ByteBufUtils.writeUUID(request);
        request.writeByte('|');
        request.writeLong(orderNo);
        send(request);
        CompletableFuture<CancelStockOrderResponse> responseFuture = new CompletableFuture<>();
        futureResponseMap.put(requestId, responseFuture);
        return responseFuture;
    }

    public CompletableFuture<CancelStockOrderResponse> getOrderHistory(long clientAccountNo, LocalDate fromDate, LocalDate toDate) {
        ByteBuf request = ByteBufAllocator.DEFAULT.buffer(67);
        request.writeInt(MessageType.Request.STOCK_ORDER);
        UUID requestId = ByteBufUtils.writeUUID(request);
        request.writeByte('|');
        request.writeLong(clientAccountNo);
        request.writeByte('|');
        ByteBufUtils.writeDate(request, fromDate);
        request.writeByte('|');
        ByteBufUtils.writeDate(request, toDate);
        send(request);
        CompletableFuture<CancelStockOrderResponse> responseFuture = new CompletableFuture<>();
        futureResponseMap.put(requestId, responseFuture);
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
