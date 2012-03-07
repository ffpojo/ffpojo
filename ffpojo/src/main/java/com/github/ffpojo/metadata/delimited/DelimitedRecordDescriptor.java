package com.github.ffpojo.metadata.delimited;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.ffpojo.exception.InvalidMetadataException;
import com.github.ffpojo.metadata.RecordDescriptor;

public class DelimitedRecordDescriptor extends RecordDescriptor {

	private String delimiter;
	
	public DelimitedRecordDescriptor() {
		super();
		this.setFieldDescriptors(new ArrayList<DelimitedFieldDescriptor>());
	}
	
	public DelimitedRecordDescriptor(Class<?> recordClazz, List<DelimitedFieldDescriptor> fieldDescriptors, String delimiter) {
		super(recordClazz, fieldDescriptors);
		this.delimiter = delimiter;
	}

	@Override
	public void assertValid() throws InvalidMetadataException {
		if (this.getDelimiter() == null || this.getDelimiter().equals("")) {
			throw new InvalidMetadataException("Delimiter is required and cannot be an empty string");
		}
		List<DelimitedFieldDescriptor> delimitedFieldDescriptors = this.getFieldDescriptors();
		if (delimitedFieldDescriptors != null && !delimitedFieldDescriptors.isEmpty()) {
			for (int i = 0; i < delimitedFieldDescriptors.size(); i++) {
				DelimitedFieldDescriptor actualFieldDescriptor = delimitedFieldDescriptors.get(i);
				
				boolean isFirstFieldDescriptor = i==0;
				DelimitedFieldDescriptor previousFieldDescriptor = null;
				if (!isFirstFieldDescriptor) {
					previousFieldDescriptor = delimitedFieldDescriptors.get(i-1);
				}
				
				if (actualFieldDescriptor.getPositionIndex() <= 0) {
					throw new InvalidMetadataException("Position index must be an positive integer (greater than zero): " + actualFieldDescriptor.getGetter());
				}
				/* Disabled: block missing fields
				if (isFirstFieldDescriptor && actualFieldDescriptor.getPositionIndex() != 1) {
					throw new InvalidMetadataException("Position index of the first field must be 1 (one): " + actualFieldDescriptor.getGetter());
				}
				*/
				/* Disabled: block missing fields
				if (!isFirstFieldDescriptor && actualFieldDescriptor.getPositionIndex() != previousFieldDescriptor.getPositionIndex() + 1) {
					throw new InvalidMetadataException("Position index jumps the previous field causing missing field: " + actualFieldDescriptor.getGetter());
				}
				*/
				// Block overlap but permit missing fields
				if (!isFirstFieldDescriptor && actualFieldDescriptor.getPositionIndex() == previousFieldDescriptor.getPositionIndex()) {
					throw new InvalidMetadataException("Position index overlaps the previous field: " + actualFieldDescriptor.getGetter());
				}
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DelimitedFieldDescriptor> getFieldDescriptors() {
		return (List<DelimitedFieldDescriptor>)super.getFieldDescriptors();
	}
	
	@Override
	public void sortFieldDescriptors() {
		List<DelimitedFieldDescriptor> fieldDescriptors = getFieldDescriptors();
		if (fieldDescriptors != null && !fieldDescriptors.isEmpty()) {
			Collections.sort(fieldDescriptors);
		}
	}
	
	// GETTERS AND SETTERS
	
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}
