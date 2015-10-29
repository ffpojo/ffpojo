package com.github.ffpojo.file.reader.extra;

import java.util.HashMap;
import java.util.Map;

public class IdentifierLine {


	public IdentifierLine() {
	}

	private Map<Integer, String> mapIds = new HashMap<Integer, String>();

	public Map<Integer, String> getMapIds() {
		return mapIds;
	}

	public void setMapIds(Map<Integer, String> mapIds) {
		this.mapIds = mapIds;
	}

	public void putId(Integer startPosition, String text){
		mapIds.put(startPosition, text);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		IdentifierLine that = (IdentifierLine) o;

		return mapIds.equals(that.mapIds);

	}

	@Override
	public int hashCode() {
		return mapIds.hashCode();
	}
}
