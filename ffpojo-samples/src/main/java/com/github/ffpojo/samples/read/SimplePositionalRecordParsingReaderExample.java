package com.github.ffpojo.samples.read;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.samples.pojo.Customer;

/**
 * 
 * @author Gilberto Holms - gibaholms@hotmail.com 
 * @author William Miranda -  blackstile@hotmail.com
 *
 */
public class SimplePositionalRecordParsingReaderExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "SimplePositionalRecordParsingExampleInput.txt";	
	
	public static void main(String[] args) {
		SimplePositionalRecordParsingReaderExample example = new SimplePositionalRecordParsingReaderExample();
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
		InputStreamReader inputStreamReader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT_TXT_RESOURCE_CLASSPATH));
		BufferedReader textFileReader = new BufferedReader(inputStreamReader);
		String line;
		while ( (line = textFileReader.readLine()) != null) {
			Customer cust = ffpojo.createFromText(Customer.class, line);
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}
		textFileReader.close();
	}
	
	
}
