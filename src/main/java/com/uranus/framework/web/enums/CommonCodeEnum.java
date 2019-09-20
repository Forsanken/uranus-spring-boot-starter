package com.uranus.framework.web.enums;

public enum CommonCodeEnum implements ICodeEnum {

	SUCCESS(0, "Success", "成功"),
	FAILED(1, "Failed","失败"),
    ;

	private int code;

	private String msg;

	private String desc;

	CommonCodeEnum(int code, String msg, String desc) {
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
