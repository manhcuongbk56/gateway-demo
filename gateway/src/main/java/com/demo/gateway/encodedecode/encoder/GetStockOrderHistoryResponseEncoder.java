package com.demo.gateway.encodedecode.encoder;

import com.demo.common.message.messagetype.MessageType;
import com.demo.common.message.orderhistory.GetStockOrderHistoryResponse;
import com.demo.common.utils.ByteBufUtils;
import com.demo.gateway.encodedecode.Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@ChannelHandler.Sharable
public class GetStockOrderHistoryResponseEncoder implements Encoder<GetStockOrderHistoryResponse> {


    @Override
    public ByteBuf encode(GetStockOrderHistoryResponse stockOrderHistoryResponse) {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeInt(MessageType.Response.GET_STOCK_ORDER_HISTORY_RESPONSE);
        ByteBufUtils.writeUUID(out, stockOrderHistoryResponse.getRequestId());
        out.writeByte('|');
        List<GetStockOrderHistoryResponse.DayHistory> days = stockOrderHistoryResponse.getDays();
        int size = days.size();
        out.writeInt(size);
        for (GetStockOrderHistoryResponse.DayHistory day: days){
            writeDayHistory(out, day);
        }
        return out;
    }


    private void writeDayHistory(ByteBuf byteBuf, GetStockOrderHistoryResponse.DayHistory dayHistory){
        List<GetStockOrderHistoryResponse.StockOrderInfo> orders = dayHistory.getOrders();
        byteBuf.writeInt(orders.size());
        ByteBufUtils.writeDate(byteBuf, dayHistory.getDay());
        dayHistory.getOrders().forEach(order -> {
            writeStockOrderInfo(byteBuf, order);
        });
    }

    private void writeStockOrderInfo(ByteBuf byteBuf, GetStockOrderHistoryResponse.StockOrderInfo info){
        ByteBufUtils.write20BytesString(byteBuf, info.getStock());
        byteBuf.writeByte('|');
        ByteBufUtils.write20BytesString(byteBuf, info.getSellOrBuy());
        byteBuf.writeByte('|');
        byteBuf.writeLong(info.getQuantity());
        byteBuf.writeByte('|');
        byteBuf.writeDouble(info.getPrice());
        byteBuf.writeByte('|');
        byteBuf.writeBoolean(info.isSuccess());
    }

}
