package com.github.ffpojo.samples.read;

import java.io.IOException;
import java.io.InputStream;

import com.github.ffpojo.file.reader.FlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReaderDefinition;
import com.github.ffpojo.file.reader.InputStreamFlatFileReader;
import com.github.ffpojo.samples.pojo.Customer;

/**
 * Example how to convert text from InputStream to Pojo
 * 
 * @author Gilberto Holms - gibaholms@hotmail.com 
 * @author William Miranda -  blackstile@hotmail.com
 *
 */
public class SimpleInputStreamFlatFileReaderExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "SimpleInputStreamFlatFileReaderExample.txt";
	
	public static void main(String[] args) {
		SimpleInputStreamFlatFileReaderExample example = new SimpleInputStreamFlatFileReaderExample();
		try {
			System.out.println("Making POJO from file system TXT FILE...");
			example.readCustomers();
			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readCustomers() throws IOException {
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
