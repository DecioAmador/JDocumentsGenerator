package com.github.decioamador.jdocsgen.excel.test.model;

public class Model2 {
	
	private String guid;
	private String label;
	private Model3 model3;
	
	public Model2(String guid, String label, Model3 model3) {
		this.guid = guid;
		this.label = label;
		this.model3 = model3;
	}
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Model3 getModel3() {
		return model3;
	}
	public void setModel3(Model3 model3) {
		this.model3 = model3;
	}
}
