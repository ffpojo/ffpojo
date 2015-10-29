package com.github.ffpojo.samples.pojo;

import java.util.Date;

import com.github.ffpojo.decorator.DateDecorator;
import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.metadata.positional.annotation.FFPojoAccessorType;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

@PositionalRecord(ignorePositionNotFound=true)
@FFPojoAccessorType(accessorType = AccessorType.FIELD)
public class Customer {

	private Long id;
	private String name;
	private String email;
	private Date birthDate;
	
	@PositionalField(initialPosition = 1, finalPosition = 5)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	// must use a String setter or a FieldDecorator
	public void setId(String id) {
		this.id = Long.valueOf(id);
	}
	
	@PositionalField(initialPosition = 6, finalPosition = 25)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@PositionalField(initialPosition = 26, finalPosition = 55)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@PositionalField(initialPosition = 56, finalPosition = 65, decorator = DateDecorator.class)
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
}
