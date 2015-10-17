package com.github.ffpojo.samples.dsl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.github.ffpojo.FFPojoFlatFileReaderBuilder;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.samples.pojo.Customer;

public class InputStreamFlatFileReaderBuilderMultiThreadDefaultProcessorExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "SimpleInputStreamFlatFileReaderExample.txt";
	
	public static void main(String[] args) {
		InputStreamFlatFileReaderBuilderMultiThreadDefaultProcessorExample example = new InputStreamFlatFileReaderBuilderMultiThreadDefaultProcessorExample();
		try {
			System.out.println("Making POJO from file system TXT FILE...");
			List<Customer> customers =  example.readCustomers();
			for (Customer customer : customers) {
				System.out.println(String.format("%s - %s - %s", customer.getId(), customer.getName(), customer.getEmail()));
			}
			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("all")
	public List<Customer> readCustomers() throws IOException, FFPojoException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(INPUT_TXT_RESOURCE_CLASSPATH);
		return new FFPojoFlatFileReaderBuilder()
			.withInputStream(inputStream)
			.withRecordClass(Customer.class)
			.withThreads(3)
			.randomRead();
	}
	
}
