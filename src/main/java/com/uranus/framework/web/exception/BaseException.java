package com.uranus.framework.web.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 异常基类
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "基础异常信息")
public class BaseException extends Exception implements Serializable{

	/**
	 * 错误编码
	 */
    @ApiModelProperty(value = "异常码", dataType = "Integer", name = "errorCode", example = "1")
	private int errorCode;

	/**
	 * 消息是否为属性文件中的Key
	 */
    @ApiModelProperty(value = "消息是否为属性文件中的Key", dataType = "boolean", name = "propertiesKey", example = "true")
	private boolean propertiesKey = true;

	/**
	 * 构造一个基本异常.
	 */
	public BaseException() {
		super();
	}

	/**
	 * 构造一个基本异常.
	 * 
	 * @param message
	 *            信息描述
	 */
	public BaseException(String message) {
		super(message);
	}

	/**
	 * 构造一个基本异常.
	 * 
	 * @param errorCode
	 *            错误编码
	 * @param message
	 *            信息描述
	 */
	public BaseException(int errorCode, String message) {
		this(errorCode, message, true);
	}

	/**
	 * 构造一个基本异常.
	 * 
	 * @param errorCode
	 *            错误编码
	 * @param message
	 *            信息描述
	 */
	public BaseException(int errorCode, String message, Throwable cause) {
		this(errorCode, message, cause, true);
	}

	/**
	 * 构造一个基本异常.
	 * 
	 * @param errorCode
	 *            错误编码
	 * @param message
	 *            信息描述
	 * @param propertiesKey
	 *            消息是否为属性文件中的Key
	 */
	public BaseException(int errorCode, String message, boolean propertiesKey) {
		super(message);
		this.setErrorCode(errorCode);
		this.setPropertiesKey(propertiesKey);
	}

	/**
	 * 构造一个基本异常.
	 * 
	 * @param errorCode
	 *            错误编码
	 * @param message
	 *            信息描述
	 */
	public BaseException(int errorCode, String message, Throwable cause,
			boolean propertiesKey) {
		super(message, cause);
		this.setErrorCode(errorCode);
		this.setPropertiesKey(propertiesKey);
	}

	/**
	 * 构造一个基本异常.
	 * 
	 * @param message
	 *            信息描述
	 * @param cause
	 *            根异常类（可以存入任何异常）
	 */
	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
