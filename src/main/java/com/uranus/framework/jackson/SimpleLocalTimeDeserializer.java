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
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 〈一句话功能简述〉<br> 
 * 〈自定义Jackson的反序列化代码〉
 *
 * @author chy
 * @create 2019/3/12
 * @since 1.0.0
 */
public class SimpleLocalTimeDeserializer extends LocalTimeDeserializer {

    public SimpleLocalTimeDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            String string = p.getText().trim();
            if (string.length() == 4) {
                return LocalTime.parse(string, DateTimeFormatter.ofPattern("HHmm"));
            }
            if (string.length() == 8) {
                return LocalTime.parse(string, DateTimeFormatter.ofPattern("HH:mm:ss"));
            }
        }
        return super.deserialize(p, ctxt);
    }
}