package com.github.ffpojo.samples;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.file.reader.FileSystemFlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReaderDefinition;
import com.github.ffpojo.file.reader.RecordType;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

public class FileSystemFlatFileReaderWithHeaderAndTrailerExample {

	//copy the file "FileSystemFlatFileReaderWithHeaderAndTrailerExample.txt" (make sure you have permission to read in the specified path):
	private static final String FILE_NAME =  "FileSystemFlatFileReaderWithHeaderAndTrailerExample.txt";
	
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
	
	@PositionalRecord
	public static class Header {

		private Long controlNumber;
		private Date processDate;
		
		@PositionalField(initialPosition = 1, finalPosition = 10)
		public Long getControlNumber() {
			return controlNumber;
		}
		public void setControlNumber(Long controlNumber) {
			this.controlNumber = controlNumber;
		}
		// must use a String setter or a FieldDecorator
		public void setControlNumber(String controlNumber) {
			this.controlNumber = Long.valueOf(controlNumber);
		}
		
		@PositionalField(initialPosition = 11, finalPosition = 20, decorator = DateDecorator.class)
		public Date getProcessDate() {
			return processDate;
		}
		public void setProcessDate(Date processDate) {
			this.processDate = processDate;
		}
	}
	
	@PositionalRecord
	public static class Trailer {

		private Integer recordsCount;
		
		@PositionalField(initialPosition = 1, finalPosition = 4)
		public Integer getRecordsCount() {
			return recordsCount;
		}
		public void setRecordsCount(Integer recordsCount) {
			this.recordsCount = recordsCount;
		}
		// must use a String setter or a FieldDecorator
		public void setRecordsCount(String recordsCount) {
			this.recordsCount = Integer.valueOf(recordsCount);
		}
	}
	
	public static class DateDecorator implements FieldDecorator<Date> {
		private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		public Date fromString(String str) throws FieldDecoratorException {
			try {
				return sdf.parse(str);
			} catch (ParseException e) {
				throw new FieldDecoratorException("Error while parsing date field from string: " + str, e);
			}
		}

		public String toString(Date obj) {
			Date date = (Date)obj;
			return sdf.format(date);
		}
	}
	
	public static void main(String[] args) {
		FileSystemFlatFileReaderWithHeaderAndTrailerExample example = new FileSystemFlatFileReaderWithHeaderAndTrailerExample();
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
    	File inputFile = new File(classLoader.getResource(FILE_NAME).getFile());
    	
		if (!inputFile.exists()) {
			throw new IllegalStateException("File not found: " + inputFile.getAbsolutePath());
		}
		FlatFileReaderDefinition ffDefinition = new FlatFileReaderDefinition(Customer.class);
		ffDefinition.setHeader(Header.class);
		ffDefinition.setTrailer(Trailer.class);
		FlatFileReader ffReader = new FileSystemFlatFileReader(inputFile, ffDefinition);
		for (Object record : ffReader) {
			RecordType recordType = ffReader.getRecordType();
			if (recordType == RecordType.HEADER) {
				System.out.print("HEADER FOUND: ");
				Header header = (Header)record;
				System.out.printf("[%d][%s]\n", header.getControlNumber(), header.getProcessDate());
			} else if (recordType == RecordType.BODY) {
				Customer cust = (Customer)record;
				System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
			} else if (recordType == RecordType.TRAILER) {
				System.out.print("TRAILER FOUND: ");
				Trailer trailer = (Trailer)record;
				System.out.printf("[%d]\n", trailer.getRecordsCount());
			}
		}
		ffReader.close();
	}
	
}
