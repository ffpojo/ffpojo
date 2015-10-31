package com.github.ffpojo.beans;

import java.util.HashSet;
import java.util.Set;

import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.extra.SetPositionalField;

@PositionalRecord
public class Manager extends Employee{

	private String name;
	private String lasName;
	private Set<Employee> employees;

	@SetPositionalField(initialPosition=31, finalPosition=11030, itemType=Employee.class)
	public Set<Employee> getEmployees() {
		if (employees == null) employees =  new HashSet<Employee>();
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	@PositionalField(initialPosition=1, finalPosition=10)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@PositionalField(initialPosition=11, finalPosition=30)
	public String getLasName() {
		return lasName;
	}

	public void setLasName(String lasName) {
		this.lasName = lasName;
	}
	
	
	
	
}
