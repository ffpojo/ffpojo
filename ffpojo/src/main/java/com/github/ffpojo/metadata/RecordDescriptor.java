package com.github.ffpojo.metadata;

import java.util.List;

import com.github.ffpojo.exception.InvalidMetadataException;

public abstract class RecordDescriptor {

	private List<? extends FieldDescriptor> fieldDescriptors;
	private Class<?> recordClazz;
	
	public RecordDescriptor() {
		// default constructor
	}
	
	public RecordDescriptor(Class<?> recordClazz, List<? extends FieldDescriptor> fieldDescriptors) {
		this.recordClazz = recordClazz;
		this.fieldDescriptors = fieldDescriptors;
	}
	
	public abstract void assertValid() throws InvalidMetadataException;
	
	public abstract void sortFieldDescriptors();
	
	// GETTERS AND SETTERS
	
	public List<? extends FieldDescriptor> getFieldDescriptors() {
		return fieldDescriptors;
	}
	public void setFieldDescriptors(List<? extends FieldDescriptor> fieldDescriptors) {
		this.fieldDescriptors = fieldDescriptors;
	}
	public Class<?> getRecordClazz() {
		return recordClazz;
	}
	public void setRecordClazz(Class<?> recordClazz) {
		this.recordClazz = recordClazz;
	}
}
