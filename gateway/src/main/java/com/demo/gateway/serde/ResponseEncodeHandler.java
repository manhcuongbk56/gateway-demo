package com.demo.gateway.serde;

import com.demo.common.constant.ResponseCode;
import com.demo.common.message.stockprice.StockPriceResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

@Log4j2
@ChannelHandler.Sharable
public class ResponseEncodeHandler implements GatewayEncoder<StockPriceResponse> {

    private static byte[] ID_PADDING = new byte[24];
    private static byte[] PRICE_PADDING = new byte[2];

    @Override
    public ByteBuf encode(StockPriceResponse stockPriceResponse) {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeLong(stockPriceResponse.getRequestId().getMostSignificantBits());
        out.writeLong(stockPriceResponse.getRequestId().getLeastSignificantBits());
        out.writeBytes(ID_PADDING);
        out.writeByte('|');
        out.writeBytes(stockPriceResponse.getResponseCode().getBytes(StandardCharsets.UTF_8));
        if (Objects.equals(stockPriceResponse.getResponseCode(), ResponseCode.FAIL.getCode())){
            return out;
        }
        out.writeByte('|');
        byte[] nameRealBytes = stockPriceResponse.getStockItemName().getBytes(StandardCharsets.UTF_8);
        byte[] padding = new byte[20 - nameRealBytes.length];
        Arrays.fill(padding, (byte) ' ');
        out.writeBytes(nameRealBytes);
        out.writeBytes(padding);
        out.writeByte('|');
        out.writeDouble(stockPriceResponse.getStockPrice());
        out.writeBytes(PRICE_PADDING);
        return out;
    }


}
