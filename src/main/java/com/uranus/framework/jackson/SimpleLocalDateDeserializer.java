/**
 * FileName: SimpleStringDeserializer
 * Author:   chy
 * Date:     2019/3/12 16:50
 * Description: 自定义Jackson的String反序列化
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 〈一句话功能简述〉<br> 
 * 〈自定义Jackson的反序列化代码〉
 *
 * @author chy 2019/3/12
 * @since 1.0.0
 */
public class SimpleLocalDateDeserializer extends LocalDateDeserializer {

    public SimpleLocalDateDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            String string = p.getText().trim();
            if (string.length() == 6) {
                return LocalDate.parse(string, DateTimeFormatter.ofPattern("yyMMdd"));
            }
            if (string.length() == 8) {
                return LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyyMMdd"));
            }
            if (string.length() == 10) {
                return LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }
        return super.deserialize(p, ctxt);
    }
}