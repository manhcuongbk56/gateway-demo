package com.demo.gateway.decoder;

import io.netty.buffer.ByteBuf;

import java.util.Map;

public interface Decoder {

    public Map<String, Object> decode(ByteBuf data);

}
