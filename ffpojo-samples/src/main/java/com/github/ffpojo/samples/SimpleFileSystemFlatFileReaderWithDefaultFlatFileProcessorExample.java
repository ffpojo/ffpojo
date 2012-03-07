package com.github.ffpojo.samples;

import java.io.File;
import java.io.IOException;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.file.processor.DefaultFlatFileProcessor;
import com.github.ffpojo.file.processor.FlatFileProcessor;
import com.github.ffpojo.file.processor.record.RecordProcessor;
import com.github.ffpojo.file.processor.record.event.RecordEvent;
import com.github.ffpojo.file.reader.FileSystemFlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReaderDefinition;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

public class SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample {

	//copy the file "SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample.txt" (make sure you have permission to read in the specified path):
	private static final String INPUT_TXT_OS_PATH = "C:/Users/gholms/Desktop/SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample.txt";
	
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
	
	public static class CustomerRecordProcessor implements RecordProcessor {

		public void processBody(RecordEvent event) {
			Customer cust = (Customer)event.getRecord();
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}

		public void processHeader(RecordEvent event) {
			// blank
		}

		public void processTrailer(RecordEvent event) {
			// blank
		}
		
	}
	
	public static void main(String[] args) {
		SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample example = new SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample();
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
		File inputFile = new File(INPUT_TXT_OS_PATH);
		if (!inputFile.exists()) {
			throw new IllegalStateException("File not found: " + INPUT_TXT_OS_PATH);
		}
		FlatFileReaderDefinition ffDefinition = new FlatFileReaderDefinition(Customer.class);
		FlatFileReader ffReader = new FileSystemFlatFileReader(inputFile, ffDefinition);
		FlatFileProcessor ffProcessor = new DefaultFlatFileProcessor(ffReader);
		ffProcessor.processFlatFile(new CustomerRecordProcessor());
		ffReader.close();
	}
	
}
