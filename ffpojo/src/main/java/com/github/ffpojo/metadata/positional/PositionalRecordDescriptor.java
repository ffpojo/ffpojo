package com.github.ffpojo.metadata.positional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.ffpojo.exception.InvalidMetadataException;
import com.github.ffpojo.metadata.RecordDescriptor;

public class PositionalRecordDescriptor extends RecordDescriptor {

	public PositionalRecordDescriptor() {
		super();
		this.setFieldDescriptors(new ArrayList<PositionalFieldDescriptor>());
	}
	
	public PositionalRecordDescriptor(Class<?> recordClazz, List<PositionalFieldDescriptor> fieldDescriptors) {
		super(recordClazz, fieldDescriptors);
	}
	
	@Override
	public void assertValid() throws InvalidMetadataException {
		List<PositionalFieldDescriptor> positionalFieldDescriptors = this.getFieldDescriptors();
		if (positionalFieldDescriptors != null && !positionalFieldDescriptors.isEmpty()) {
			for (int i = 0; i < positionalFieldDescriptors.size(); i++) {
				PositionalFieldDescriptor actualFieldDescriptor = positionalFieldDescriptors.get(i);
				
				boolean isFirstFieldDescriptor = i==0;
				PositionalFieldDescriptor previousFieldDescriptor = null;
				if (!isFirstFieldDescriptor) {
					previousFieldDescriptor = positionalFieldDescriptors.get(i-1);
				}
				
				if (actualFieldDescriptor.getInitialPosition() <= 0 || actualFieldDescriptor.getFinalPosition() <= 0) {
					throw new InvalidMetadataException("Initial position and final position must be positive integers (greater than zero): " + actualFieldDescriptor.getGetter());
				}
				if (actualFieldDescriptor.getFinalPosition() < actualFieldDescriptor.getInitialPosition()) {
					throw new InvalidMetadataException("Invalid interval of positions: " + actualFieldDescriptor.getGetter());
				}
				/* Disabled: block blank spaces
				if (isFirstFieldDescriptor && actualFieldDescriptor.getInitialPosition() != 1) {
					throw new InvalidMetadataException("Initial position of the first field must be 1 (one): " + actualFieldDescriptor.getGetter());
				}
				*/
				/* Disabled: block blank spaces
				if (!isFirstFieldDescriptor && actualFieldDescriptor.getInitialPosition() != previousFieldDescriptor.getFinalPosition() + 1) {
					throw new InvalidMetadataException("Position interval overlaps the previous field or cause blank spaces: " + actualFieldDescriptor.getGetter());
				}
				*/
				// Block overlap but permit blank spaces
				if (!isFirstFieldDescriptor && actualFieldDescriptor.getInitialPosition() <= previousFieldDescriptor.getFinalPosition()) {
					throw new InvalidMetadataException("Position interval overlaps the previous field: " + actualFieldDescriptor.getGetter());
				}
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PositionalFieldDescriptor> getFieldDescriptors() {
		return (List<PositionalFieldDescriptor>)super.getFieldDescriptors();
	}
	
	@Override
	public void sortFieldDescriptors() {
		List<PositionalFieldDescriptor> fieldDescriptors = getFieldDescriptors();
		if (fieldDescriptors != null && !fieldDescriptors.isEmpty()) {
			Collections.sort(fieldDescriptors);
		}
	}
	
}
