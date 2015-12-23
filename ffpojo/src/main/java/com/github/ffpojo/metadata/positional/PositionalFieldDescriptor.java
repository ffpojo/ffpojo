package com.github.ffpojo.metadata.positional;

import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.FieldDescriptor;

/**
 * @author Gilberto Holms - gibaholms@hotmail.com
 * @author William Miranda  - blackstile@hotmail.com
 */
public class PositionalFieldDescriptor extends FieldDescriptor implements Comparable<PositionalFieldDescriptor> {

	private int initialPosition;
	private int finalPosition;
	private PaddingAlign paddingAlign;
	private char paddingCharacter;
	private boolean trimOnRead;
	private FieldDecorator<?> decorator;
	private boolean ignoreMissingFieldsInTheEnd;
	private boolean remainPosition;

	public int compareTo(PositionalFieldDescriptor other) {
		int resultCompareFullLine = comparePositionalFullLine(other);
		if(resultCompareFullLine != 0){
			return resultCompareFullLine;
		}

		int resultCompareFieldRemainder =  comparePositionalFieldRemainder(other);
		if(resultCompareFieldRemainder != 0){
			return resultCompareFieldRemainder;
		}

		if (this.initialPosition - other.initialPosition == 0) {

			if (this.finalPosition - other.finalPosition == 0) {
				return this.getGetter().getName().compareTo(other.getGetter().getName());
			} else {
				return this.finalPosition - other.finalPosition;
			}
		} else {
			return this.initialPosition - other.initialPosition;
		}
	}

	private int comparePositionalFullLine(PositionalFieldDescriptor other){
		if (this.isFullLineField()){
			return 1;
		}
		if(other.isFullLineField()){
			return -1;
		}
		return 0;
	}

	private int comparePositionalFieldRemainder(PositionalFieldDescriptor other){
		if (this.isPositionalFieldRemainder()){
			return 1;
		}
		if(other.isPositionalFieldRemainder()){
			return -1;
		}
		return 0;
	}

	// GETTERS AND SETTERS
	
	public int getInitialPosition() {
		return initialPosition;
	}
	public void setInitialPosition(int initialPosition) {
		this.initialPosition = initialPosition;
	}
	public int getFinalPosition() {
		return finalPosition;
	}
	public void setFinalPosition(int finalPosition) {
		this.finalPosition = finalPosition;
	}
	public FieldDecorator<?> getDecorator() {
		return decorator;
	}
	public void setDecorator(FieldDecorator<?> decorator) {
		this.decorator = decorator;
	}
	public PaddingAlign getPaddingAlign() {
		return paddingAlign;
	}
	public void setPaddingAlign(PaddingAlign paddingAlign) {
		this.paddingAlign = paddingAlign;
	}
	public char getPaddingCharacter() {
		return paddingCharacter;
	}
	public void setPaddingCharacter(char paddingCharacter) {
		this.paddingCharacter = paddingCharacter;
	}
	public boolean isTrimOnRead() {
		return trimOnRead;
	}
	public void setTrimOnRead(boolean trimOnRead) {
		this.trimOnRead = trimOnRead;
	}

	public boolean isIgnoreMissingFieldsInTheEnd() {
		return ignoreMissingFieldsInTheEnd;
	}

	public void setIgnoreMissingFieldsInTheEnd(boolean ignoreMissingFieldsInTheEnd) {
		this.ignoreMissingFieldsInTheEnd = ignoreMissingFieldsInTheEnd;
	}

	public boolean isPositionalFieldRemainder() {
		return remainPosition;
	}

	public void setRemainPosition(boolean remainPosition) {
		this.remainPosition = remainPosition;
	}


}
