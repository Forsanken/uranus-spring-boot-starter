/**
 * FileName: Page
 * Author:   chy
 * Date:     2019/8/27 15:10
 * Description: 分页数据
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.web.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈分页数据〉
 *
 * @author chy 2019/8/27
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(description = "基础分页请求对象")
public class Page implements Serializable {

    @ApiModelProperty(value = "当前页数", dataType = "Integer", name = "pageNum", example = "1", required = true)
    @Min(1)
    @NotBlank
    private int pageNum;

    @ApiModelProperty(value = "每页行数", dataType = "Integer", name = "pageSize", example = "20", required = true)
    @Min(1)
    @NotBlank
    private int pageSize;
}