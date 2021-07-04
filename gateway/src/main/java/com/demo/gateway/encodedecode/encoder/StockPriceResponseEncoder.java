package com.demo.gateway.encodedecode.encoder;

import com.demo.common.constant.ResponseCode;
import com.demo.common.message.messagetype.IntegerMessageType;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.common.utils.ByteBufUtils;
import com.demo.gateway.encodedecode.Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

@Log4j2
@ChannelHandler.Sharable
public class StockPriceResponseEncoder implements Encoder<StockPriceResponse> {

    private static byte[] ID_PADDING = new byte[24];
    private static byte[] PRICE_PADDING = new byte[2];

    @Override
    public ByteBuf encode(StockPriceResponse stockPriceResponse) {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeInt(IntegerMessageType.Response.GET_PRICE_RESPONSE);
        ByteBufUtils.writeUUID(out, stockPriceResponse.getRequestId());
        out.writeByte('|');
        out.writeBytes(stockPriceResponse.getResponseCode().getBytes(StandardCharsets.UTF_8));
        if (Objects.equals(stockPriceResponse.getResponseCode(), ResponseCode.FAIL.getCode())){
            return out;
        }
        out.writeByte('|');
        ByteBufUtils.write20BytesString(out, stockPriceResponse.getStockItemName());
        out.writeByte('|');
        out.writeDouble(stockPriceResponse.getStockPrice());
        out.writeBytes(PRICE_PADDING);
        return out;
    }


}