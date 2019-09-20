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
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈自定义Jackson的反序列化代码〉
 *
 * @author chy
 * @create 2019/3/12
 * @since 1.0.0
 */
public class SimpleStringDeserializer extends StringDeserializer {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            if (null == p.getText()) {
                return null;
            }
            String text = p.getText().trim();
            if (text.length() == 0) {
                return null;
            }
            return text;
        }
        return super.deserialize(p,ctxt);
    }
}