package com.demo.gateway.encodedecode.encoder;

import com.demo.common.constant.ResponseCode;
import com.demo.common.message.messagetype.MessageType;
import com.demo.common.message.stockorder.OrderStockResponse;
import com.demo.gateway.encodedecode.Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.demo.common.utils.ByteBufUtils.writeUUID;

@Log4j2
public class OrderStockResponseEncoder implements Encoder<OrderStockResponse> {

    @Override
    public ByteBuf encode(OrderStockResponse orderStockResponse) {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer(58);
        out.writeInt(MessageType.Response.ORDER_STOCK_RESPONSE);
        writeUUID(out, orderStockResponse.getRequestId());
        out.writeByte('|');
        out.writeBytes(orderStockResponse.getResponseCode().getBytes(StandardCharsets.UTF_8));
        if (Objects.equals(orderStockResponse.getResponseCode(), ResponseCode.FAIL.getCode())){
            return out;
        }
        out.writeByte('|');
        out.writeLong(orderStockResponse.getOrderNo());
        return out;
    }


}
