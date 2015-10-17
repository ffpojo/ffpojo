package com.github.ffpojo.file.reader.extra;

public class IdentifierLine {
	
	private int size;
	private int startPosition;
	
	public IdentifierLine(int startPosition, int size) {
		this.size =  size;
		this.startPosition = startPosition;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + size;
		result = prime * result + startPosition;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifierLine other = (IdentifierLine) obj;
		if (size != other.size)
			return false;
		if (startPosition != other.startPosition)
			return false;
		return true;
	}
	
	

}
