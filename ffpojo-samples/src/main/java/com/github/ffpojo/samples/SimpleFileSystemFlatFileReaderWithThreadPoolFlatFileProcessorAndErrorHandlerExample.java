package com.github.ffpojo.samples;

import java.io.File;
import java.io.IOException;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.exception.RecordProcessorException;
import com.github.ffpojo.file.processor.FlatFileProcessor;
import com.github.ffpojo.file.processor.ThreadPoolFlatFileProcessor;
import com.github.ffpojo.file.processor.record.DefaultRecordProcessor;
import com.github.ffpojo.file.processor.record.event.RecordEvent;
import com.github.ffpojo.file.processor.record.handler.ErrorHandler;
import com.github.ffpojo.file.reader.FileSystemFlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReaderMultiDefinition;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

public class SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample {

	//copy the file "SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample.txt" (make sure you have permission to read in the specified path):
	private static final String INPUT_TXT_OS_PATH = "SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample.txt";
	
	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;
		
		@PositionalField(initialPosition = 1, finalPosition = 5)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}
		
		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}
	
	// processor class must be thread-safe !
	public static class CustomerRecordProcessor extends DefaultRecordProcessor {

		public void processBody(RecordEvent event) throws RecordProcessorException {
			Customer cust = (Customer)event.getRecord();
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
			throw new RecordProcessorException("An error occurred !!!");
		}
		
	}
	
	public static class CustomerErrorHandler implements ErrorHandler {

		public void error(RecordProcessorException exception) throws RecordProcessorException {
			System.out.println("ErrorHandler executed !");
		}
		
	}
	
	public static void main(String[] args) {
		SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample example = new SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample();
		try {
			System.out.println("Making POJO from file system TXT FILE...");
			example.readCustomers();
			
			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}
	
	public void readCustomers() throws IOException, FFPojoException {
		
		ClassLoader classLoader = this.getClass().getClassLoader();
    	File inputFile = new File(classLoader.getResource(INPUT_TXT_OS_PATH).getFile());
    	
		if (!inputFile.exists()) {
			throw new IllegalStateException("File not found: " + INPUT_TXT_OS_PATH);
		}
		FlatFileReaderMultiDefinition ffDefinition = new FlatFileReaderMultiDefinition(Customer.class);
		FlatFileReader ffReader = new FileSystemFlatFileReader(inputFile, ffDefinition);
		FlatFileProcessor ffProcessor = new ThreadPoolFlatFileProcessor(ffReader, 5);
		ffProcessor.setErrorHandler(new CustomerErrorHandler());
		ffProcessor.processFlatFile(new CustomerRecordProcessor());
		ffReader.close();
	}
	
}
