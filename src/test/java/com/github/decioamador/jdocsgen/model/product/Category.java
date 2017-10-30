package com.github.decioamador.jdocsgen.model.product;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Category implements Comparable<Category> {

    private Long id;
    private BasicInfo basicInfo;
    private Set<Product> products;

    public Category(final Long id, final String name, final String description) {
	this.id = Objects.requireNonNull(id);
	this.basicInfo = new BasicInfo(Objects.requireNonNull(name), Objects.requireNonNull(description));
    }

    public boolean addProduct(final Product p) {
	boolean added = false;
	if (products == null) {
	    products = new TreeSet<>();
	}
	added = products.add(Objects.requireNonNull(p));
	return added;
    }

    public boolean removeProduct(final Product p) {
	boolean removed = false;
	if (products != null) {
	    removed = products.remove(p);
	}
	return removed;
    }

    public Long getId() {
	return id;
    }

    public void setId(final Long id) {
	this.id = id;
    }

    public BasicInfo getBasicInfo() {
	return basicInfo;
    }

    public void setBasicInfo(final BasicInfo basicInfo) {
	this.basicInfo = basicInfo;
    }

    public Set<Product> getProducts() {
	return products;
    }

    public void setProducts(final Set<Product> products) {
	this.products = products;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final Category other = (Category) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    @Override
    public int compareTo(final Category o) {
	int result = 1;
	if (o != null) {
	    if (this.basicInfo.getName().equals(o.basicInfo.getName())) {
		result = this.id.compareTo(o.id);
	    } else {
		result = this.basicInfo.getName().compareTo(o.basicInfo.getName());
	    }
	}
	return result;
    }

}
