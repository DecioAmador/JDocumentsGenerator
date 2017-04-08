package com.github.decioamador.jdocsgen.test.model;

public class Model3 {

	private String uuid;
	private Integer number;
	
	public Model3(String uuid, Integer number) {
		this.uuid = uuid;
		this.number = number;
	}

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String guid) {
		this.uuid = guid;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
