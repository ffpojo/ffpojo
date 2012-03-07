package com.github.ffpojo.parser;

import com.github.ffpojo.metadata.RecordDescriptor;
import com.github.ffpojo.metadata.delimited.DelimitedRecordDescriptor;
import com.github.ffpojo.metadata.positional.PositionalRecordDescriptor;

public class RecordParserFactory {

	public static RecordParser createRecordParser(RecordDescriptor recordDescriptor) {
		if (recordDescriptor instanceof PositionalRecordDescriptor) {
			return new PositionalRecordParser((PositionalRecordDescriptor)recordDescriptor);
		} else if (recordDescriptor instanceof DelimitedRecordDescriptor) {
			return new DelimitedRecordParser((DelimitedRecordDescriptor)recordDescriptor);
		} else {
			throw new IllegalArgumentException("RecordParser not found for class " + recordDescriptor.getClass());
		}
	}
	
}
