package com.uranus.framework.web.enums;

public enum ErrorCodeEnum implements ICodeEnum {

	RUNTIME_ERROR(10000, "运行时出现异常", "运行异常"),
	OTHER_ERROR(10001, "其他异常","不明异常"),
    ;

	private int code;

	private String msg;

	private String desc;

	ErrorCodeEnum(int code, String msg, String desc) {
		this.code = code;
		this.msg = msg;
		this.desc = desc;

	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public String getDesc() {
		return desc;
	}
}
