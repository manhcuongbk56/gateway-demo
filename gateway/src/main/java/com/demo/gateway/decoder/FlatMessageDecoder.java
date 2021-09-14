package com.demo.gateway.decoder;

import com.demo.gateway.exceptions.UnknownDataTypeException;
import com.demo.gateway.schemaloader.Field;
import com.demo.gateway.schemaloader.Schema;
import com.demo.gateway.schemaloader.SchemaProvider;
import com.demo.gateway.utils.ByteBufUtils;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

import static com.demo.gateway.util.FieldConstants.DATA_TYPE;
import static com.demo.gateway.util.FieldConstants.DATA_TYPE_LENGTH;

public class FlatMessageDecoder implements Decoder {



    private SchemaProvider schemaProvider;

    @Inject
    public FlatMessageDecoder(SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    @Override
    public Map<String, Object> decode(ByteBuf data) {
        String dataType = ByteBufUtils.readString(data, DATA_TYPE_LENGTH);
        Schema schema = schemaProvider.getSchema(dataType);
        Map<String, Object> input = new HashMap<>();
        input.put(DATA_TYPE, dataType);
        for (Field field : schema.getFields()) {
            input.put(field.getName(), readField(field, data));
        }
        return input;
    }

    private Object readField(Field field, ByteBuf data) {
        switch (field.getDataType()) {
            case TEXT:
                return ByteBufUtils.readString(data, field.getLength());
            case NUMBER:
                return ByteBufUtils.readNumber(data, field.getLength());
        }
        //should not reach here
        throw new UnknownDataTypeException(field.getDataType());
    }

}
