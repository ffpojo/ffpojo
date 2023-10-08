package com.github.ffpojo.metadata.delimited;

import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.FieldDescriptor;


public class DelimitedFieldDescriptor extends FieldDescriptor implements Comparable<DelimitedFieldDescriptor> {

	private int positionIndex;

	public int compareTo(DelimitedFieldDescriptor other) {
		int resultCompareFullLine = compareFieldDescriptorFullLine(other);
		if(resultCompareFullLine != 0){
			return resultCompareFullLine;
		}

		if (this.positionIndex - other.positionIndex == 0) {
			return this.getGetter().getName().compareTo(other.getGetter().getName());
		} else {
			return this.positionIndex - other.positionIndex;
		}
	}
	
	// GETTERS AND SETTERS
	
	public int getPositionIndex() {
		return positionIndex;
	}
	public void setPositionIndex(int positionIndex) {
		this.positionIndex = positionIndex;
	}

}
