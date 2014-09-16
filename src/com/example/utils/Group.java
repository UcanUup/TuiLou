package com.example.utils;

import java.util.List;

public class Group {
	private String gname;
	private String email;
	private String remark;
	private String code;
	
	public static boolean isUpdate = false;
	
	public static List<String> groups;
	
	public static List<List<Group>> childs;
	public static List<Group> child1;
	public static List<Group> child2;
	
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
