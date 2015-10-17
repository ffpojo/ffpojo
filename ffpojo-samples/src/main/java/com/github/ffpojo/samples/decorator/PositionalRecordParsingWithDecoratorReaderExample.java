package com.github.ffpojo.samples.decorator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.samples.pojo.Customer;

public class PositionalRecordParsingWithDecoratorReaderExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "PositionalRecordParsingWithDecoratorExampleInput.txt";
	
	public static void main(String[] args) {
		PositionalRecordParsingWithDecoratorReaderExample example = new PositionalRecordParsingWithDecoratorReaderExample();
		try {
			System.out.println("Making POJO from TXT...");
			example.readCustomersFromText();
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
			System.out.printf("[%d][%s][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail(), cust.getBirthDate());
		}
		textFileReader.close();
	}	
}
