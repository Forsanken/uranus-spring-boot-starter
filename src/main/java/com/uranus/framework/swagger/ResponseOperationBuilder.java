/**
 * FileName: ResponseOperationBuilder
 * Author:   chy
 * Date:     2018/10/17 15:11
 * Description: swagger2 自定义 OperationBuilder 拓展
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.swagger;

import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.Iterator;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br> 
 * 〈swagger2 自定义 OperationBuilder 拓展〉
 *
 * @author chy 2018/10/17
 * @since 1.0.0
 */
public class ResponseOperationBuilder implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext operationContext) {
        Set<ResponseMessage> def = operationContext.operationBuilder().build().getResponseMessages();
        Iterator iterator = def.iterator();
        while (iterator.hasNext()) {
            ResponseMessage rm  = (ResponseMessage)iterator.next();
            if (rm.getCode() != 200) {
                iterator.remove();
            }
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}