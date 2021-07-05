package com.demo.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ByteBufUtilsTest {


    @Test
    void testWriteDate() {
        ByteBuf dateByteBuf = ByteBufAllocator.DEFAULT.buffer(8);
        LocalDate inputDate = LocalDate.now();
        ByteBufUtils.writeDate(dateByteBuf, inputDate);
        LocalDate outputDate = ByteBufUtils.readDate(dateByteBuf);
        Assertions.assertThat(outputDate).isEqualTo(inputDate);
    }

}