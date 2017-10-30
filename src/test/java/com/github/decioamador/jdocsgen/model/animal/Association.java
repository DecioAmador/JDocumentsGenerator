package com.github.decioamador.jdocsgen.model.animal;

import java.util.List;

public class Association<T> {

    private String name;
    private List<T> members;

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public List<T> getMembers() {
	return members;
    }

    public void setMembers(final List<T> members) {
	this.members = members;
    }

}
