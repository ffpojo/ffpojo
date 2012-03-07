package com.github.ffpojo.file.processor;

import java.util.concurrent.ExecutorService;

import com.github.ffpojo.exception.RecordProcessorException;
import com.github.ffpojo.file.processor.record.RecordProcessor;
import com.github.ffpojo.file.processor.record.event.DefaultRecordEvent;
import com.github.ffpojo.file.processor.record.event.RecordEvent;
import com.github.ffpojo.file.reader.FlatFileReader;
import com.github.ffpojo.file.reader.RecordType;


public class CustomExecutorFlatFileProcessor extends BaseFlatFileProcessor implements FlatFileProcessor {

	private ExecutorService executor;
	
	public CustomExecutorFlatFileProcessor(FlatFileReader flatFileReader, ExecutorService executor) {
		super(flatFileReader);
		if (executor == null || executor.isShutdown()) {
			throw new IllegalArgumentException("ExecutorService is null or shutdown");
		}
		this.executor = executor;
	}
	
	public void processFlatFile(final RecordProcessor processor) {
		for(final Object record : flatFileReader) {
			final RecordType recordType = flatFileReader.getRecordType();
			final RecordEvent event = new DefaultRecordEvent(record, flatFileReader.getRecordText(), flatFileReader.getRecordIndex());
			executor.execute(new Runnable() {
				public void run() {
					try {
						if (recordType == RecordType.HEADER) {
							processor.processHeader(event);
						} else if (recordType == RecordType.BODY) {
							processor.processBody(event);
						} else if (recordType == RecordType.TRAILER) {
							processor.processTrailer(event);
						}
					} catch (RecordProcessorException e) {
						try {
							errorHandler.error(e);
						} catch (RecordProcessorException exThrownByErrorHandler) {
							exThrownByErrorHandler.printStackTrace();
						}
					}
				}
			});
		}
		executor.shutdown();
		while(!executor.isTerminated()) {
			Thread.yield();
		}
	}
	
}
