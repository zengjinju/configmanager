package com.zjj.configmanager.enums;

/**
 * Created by admin on 2017/12/2.
 */
public enum AppServerEnum {

	NB("nb","测试");

	private String code;
	private String name;
	AppServerEnum(String code,String name){
		this.code=code;
		this.name=name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static AppServerEnum getAppServerByName(String code){
		for(AppServerEnum value : values()){
			if(code.equals(value.getCode())){
				return value;
			}
		}
		return null;
	}
}
