package com.github.ffpojo.metadata.positional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import com.github.ffpojo.exception.InvalidMetadataException;
import com.github.ffpojo.metadata.RecordDescriptor;

/**
 *  @author  Gilberto Holms - gibaholms@hotmail.com
 */
public class PositionalRecordDescriptor extends RecordDescriptor {

	private boolean ignorePositionNotFound = false;
	
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
		if (null != positionalFieldDescriptors){
			sortAndRemoveRepeated(positionalFieldDescriptors);
		}
		if (positionalFieldDescriptors != null && !positionalFieldDescriptors.isEmpty()) {
			for (int i = 0; i < positionalFieldDescriptors.size(); i++) {
				PositionalFieldDescriptor actualFieldDescriptor = positionalFieldDescriptors.get(i);
				if (actualFieldDescriptor.isRemainPosition()){
					continue;
				}
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
					final String filedName = getFieldName(actualFieldDescriptor);
					final String previousName = getFieldName(previousFieldDescriptor);

					throw new InvalidMetadataException(String.format("Position interval of field %s overlaps the previous field %s ", filedName, previousName));
				}
			}
		}
	}

	private String getFieldName(PositionalFieldDescriptor fieldDescriptor) {
		String previousName = "";
		if (fieldDescriptor.isByProperty()){
            previousName =  fieldDescriptor.getGetter().getName();
        }else {
            previousName =  fieldDescriptor.getField().getName();
        }
		return previousName;
	}

	private void sortAndRemoveRepeated(List<PositionalFieldDescriptor> positionalFieldDescriptors) {
		final TreeSet<PositionalFieldDescriptor> fieldDescriptors = new TreeSet<PositionalFieldDescriptor>(positionalFieldDescriptors);
		positionalFieldDescriptors.clear();
		positionalFieldDescriptors.addAll(fieldDescriptors);
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

	public boolean isIgnorePositionNotFound() {
		return ignorePositionNotFound;
	}

	public void setIgnorePositionNotFound(boolean ignorePositionNotFound) {
		this.ignorePositionNotFound = ignorePositionNotFound;
	}
	
	
	
}
