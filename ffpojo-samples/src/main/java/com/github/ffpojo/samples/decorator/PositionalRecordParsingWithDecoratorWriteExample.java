package com.github.ffpojo.samples.decorator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.samples.pojo.Customer;

public class PositionalRecordParsingWithDecoratorWriteExample {

	//change here (make sure you have permission to write in the specified path):
	//private static final String OUTPUT_TXT_OS_PATH = "C:/Users/gibaholms/Desktop/PositionalRecordParsingWithDecoratorExampleOutput.txt";
	private static final String OUTPUT_TXT_OS_PATH = System.getProperty("java.io.tmpdir") + "PositionalRecordParsingWithDecoratorExampleOutput.txt";
	
	public static void main(String[] args) {
		PositionalRecordParsingWithDecoratorWriteExample example = new PositionalRecordParsingWithDecoratorWriteExample();
		try {
			System.out.println("Making TXT from POJO...");
			example.writeCustomersToText();
			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}

	public void writeCustomersToText() throws IOException, FFPojoException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		File file = new File(OUTPUT_TXT_OS_PATH);
		System.out.println(file.getAbsolutePath());
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
			cust.setBirthDate(new Date());
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(65478L); 
			cust.setName("Bono Vox"); 
			cust.setEmail("bono@thehost.com");
			cust.setBirthDate(new Date());
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(78425L); 
			cust.setName("Bob Marley"); 
			cust.setEmail("marley@thehost.com");
			cust.setBirthDate(new Date());
			customers.add(cust);
		}
		return customers;
	}
	
}
