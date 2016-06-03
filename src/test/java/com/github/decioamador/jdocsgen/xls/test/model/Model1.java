package com.github.decioamador.jdocsgen.xls.test.model;

import java.util.Date;

public class Model1 {

	private String guid;
	private Date date;
	private Long id;
	private String name;
	private Model2 model2;
	
	public Model1(String guid, Long id, Date date, String name, Model2 model2){
		this.guid = guid;
		this.id = id;
		this.date = date;
		this.name = name;
		this.model2 = model2;
	}
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Model2 getModel2() {
		return model2;
	}
	public void setModel2(Model2 model2) {
		this.model2 = model2;
	}
}
