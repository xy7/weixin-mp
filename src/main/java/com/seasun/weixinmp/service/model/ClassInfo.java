package com.seasun.weixinmp.service.model;

public class ClassInfo {
	
	private int classId;
	private String className;
	private String desc;
	
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return "ClassInfo [classId=" + classId + ", className=" + className + ", desc=" + desc + "]";
	}

}
