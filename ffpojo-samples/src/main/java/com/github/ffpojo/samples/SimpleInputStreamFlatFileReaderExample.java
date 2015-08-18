package com.github.ffpojo.samples;

import java.io.IOException;
import java.io.InputStream;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.file.reader.FlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReaderDefinition;
import com.github.ffpojo.file.reader.InputStreamFlatFileReader;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

public class SimpleInputStreamFlatFileReaderExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "SimpleInputStreamFlatFileReaderExample.txt";
	
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
	
	public static void main(String[] args) {
		SimpleInputStreamFlatFileReaderExample example = new SimpleInputStreamFlatFileReaderExample();
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
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(INPUT_TXT_RESOURCE_CLASSPATH);
		FlatFileReaderDefinition ffDefinition = new FlatFileReaderDefinition(Customer.class);
		FlatFileReader ffReader = new InputStreamFlatFileReader(inputStream, ffDefinition);
		for (Object record : ffReader) {
			Customer cust = (Customer)record;
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}
		ffReader.close();
	}
	
}
