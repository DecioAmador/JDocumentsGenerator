package com.github.decioamador.jdocsgen.test;

public class Model3 {

	private String guid;
	private Integer number;
	
	public Model3(String guid, Integer number) {
		this.guid = guid;
		this.number = number;
	}

	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
