package com.github.decioamador.jdocsgen.model;

public class Model2 {
	
	private String uuid;
	private String label;
	private Model3 model3;
	
	public Model2(String uuid, String label, Model3 model3) {
		this.uuid = uuid;
		this.label = label;
		this.model3 = model3;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
