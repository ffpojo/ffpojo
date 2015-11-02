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
	private boolean ignorePositionNotFound;
	private boolean remainPosition;

	public int compareTo(PositionalFieldDescriptor other) {
		if (this.isRemainPosition()){
			return 1;
		}
		if(other.isRemainPosition()){
			return -1;
		}
		if (this.initialPosition - other.initialPosition == 0) {

			if (this.finalPosition - other.finalPosition == 0) {
				if (this.isByProperty()){
					return this.getGetter().getName().compareTo(other.getGetter().getName());
				}else{
					return this.getField() == null ? -1 : other.getField() == null ?  1 :
							this.getField().getName().compareTo(other.getField().getName());
				}
			} else {
				return this.finalPosition - other.finalPosition;
			}
		} else {
			return this.initialPosition - other.initialPosition;
		}
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

	public boolean isIgnorePositionNotFound() {
		return ignorePositionNotFound;
	}

	public void setIgnorePositionNotFound(boolean ignorePositionNotFound) {
		this.ignorePositionNotFound = ignorePositionNotFound;
	}

	public boolean isRemainPosition() {
		return remainPosition;
	}

	public void setRemainPosition(boolean remainPosition) {
		this.remainPosition = remainPosition;
	}


}
