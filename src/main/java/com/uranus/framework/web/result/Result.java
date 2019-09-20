package com.uranus.framework.web.result;

import com.uranus.framework.web.exception.BaseException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.uranus.framework.web.enums.ICodeEnum;

import java.io.Serializable;

@Data
@ApiModel(description = "基础返回对象")
public class Result<T> implements Serializable {

    /**
     * 消息码
     */
    @ApiModelProperty(value = "返回码(0-成功；其他-失败)", dataType = "Integer", name = "code", example = "1")
	private int code;
    /**
     * 返回数据说明
     */
    @ApiModelProperty(value = "返回信息(失败时返回错误提示信息)", dataType = "String", name = "msg", example = "成功")
	private String msg;
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据", name = "data")
	private T data;

	public Result(ICodeEnum code) {
		this.code = code.getCode();
		this.msg = code.getDesc();
	}

	public Result(ICodeEnum code, T data) {
		this(code);
		this.data = data;
	}

    public Result(BaseException ex) {
        this.code = ex.getErrorCode();
        this.msg = ex.getMessage();
    }

    public void setCodeEnum(ICodeEnum code) {
        this.code = code.getCode();
        this.msg = code.getDesc();
    }
}
