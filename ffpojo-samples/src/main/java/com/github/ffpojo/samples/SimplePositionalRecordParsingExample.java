package com.github.ffpojo.samples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

public class SimplePositionalRecordParsingExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "SimplePositionalRecordParsingExampleInput.txt";
	
	//change here (make sure you have permission to write in the specified path):
	private static final String OUTPUT_TXT_OS_PATH = System.getProperty("java.io.tmpdir") + "SimplePositionalRecordParsingExampleOutput.txt";
	
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
		SimplePositionalRecordParsingExample example = new SimplePositionalRecordParsingExample();
		try {
			System.out.println("Making POJO from TXT...");
			example.readCustomersFromText();
			
			System.out.println("Making TXT from POJO...");
			example.writeCustomersToText();
			
			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}
	
	public void readCustomersFromText() throws IOException, FFPojoException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		BufferedReader textFileReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT_TXT_RESOURCE_CLASSPATH)));
		String line;
		while ( (line = textFileReader.readLine()) != null) {
			Customer cust = ffpojo.createFromText(Customer.class, line);
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}
		textFileReader.close();
	}
	
	public void writeCustomersToText() throws IOException, FFPojoException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		File file = new File(OUTPUT_TXT_OS_PATH);
		file.createNewFile();
		BufferedWriter textFileWriter = new BufferedWriter(new FileWriter(file));
		List<Customer> customers = createCustomersMockList();
		for (int i = 0; i < customers.size(); i++) {
			String line = ffpojo.parseToText(customers.get(i));
			textFileWriter.write(line);
			if (i < customers.size() - 1) {
				textFileWriter.newLine();
			}
		}
		textFileWriter.close();	
	}
	
	private static List<Customer> createCustomersMockList() {
		List<Customer> customers = new ArrayList<Customer>();
		{
			Customer cust = new Customer();
			cust.setId(98456L); 
			cust.setName("Axel Rose"); 
			cust.setEmail("axl@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(65478L); 
			cust.setName("Bono Vox"); 
			cust.setEmail("bono@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(78425L); 
			cust.setName("Bob Marley"); 
			cust.setEmail("marley@thehost.com");
			customers.add(cust);
		}
		return customers;
	}
	
}
