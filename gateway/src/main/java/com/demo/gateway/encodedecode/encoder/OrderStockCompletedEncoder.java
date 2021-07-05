package com.demo.gateway.encodedecode.encoder;

import com.demo.common.message.messagetype.MessageType;
import com.demo.common.message.stockorder.OrderStockCompleted;
import com.demo.gateway.encodedecode.Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ChannelHandler.Sharable
public class OrderStockCompletedEncoder implements Encoder<OrderStockCompleted> {

    @Override
    public ByteBuf encode(OrderStockCompleted orderStockCompleted) {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer(12);
        out.writeInt(MessageType.Response.ORDER_STOCK_COMPLETED);
        out.writeLong(orderStockCompleted.getOrderNo());
        return out;
    }


}
