package com.github.ffpojo.file.processor.record.event;

public class DefaultRecordEvent implements RecordEvent {

	private Object record;
	private String recordText;
	private long recordIndex;
	
	public DefaultRecordEvent(Object record, String recordText, long recordIndex) {
		this.record = record;
		this.recordText = recordText;
		this.recordIndex = recordIndex;
	}

	public Object getRecord() {
		return record;
	}
	
	public String getRecordText() {
		return recordText;
	}
	
	public long getRecordIndex() {
		return recordIndex;
	}

}
