package com.github.ffpojo.samples.write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.mock.CustomerMockFactory;
import com.github.ffpojo.samples.pojo.Customer;

/**
 * Class contains example how to do to write a entity list into txt file.
 *   
 * @author Gilberto Holms - gibaholms@hotmail.com 
 * @author William Miranda -  blackstile@hotmail.com
 *
 */
public class SimplePositionalRecordParsingWriterExample {

	//change here (make sure you have permission to write in the specified path):
	private static final String OUTPUT_TXT_OS_PATH = System.getProperty("java.io.tmpdir") + "SimplePositionalRecordParsingExampleOutput.txt";
	
	public static void main(String[] args) {
		SimplePositionalRecordParsingWriterExample example = new SimplePositionalRecordParsingWriterExample();
		try {
			System.out.println("Making TXT file from POJO...");
			example.writeCustomersToText();
			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeCustomersToText() throws IOException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		File file = new File(OUTPUT_TXT_OS_PATH);
		file.createNewFile();
		BufferedWriter textFileWriter = new BufferedWriter(new FileWriter(file));
		List<Customer> customers = CustomerMockFactory.createCustomersMockList();
		for (int i = 0; i < customers.size(); i++) {
			String line = ffpojo.parseToText(customers.get(i));
			textFileWriter.write(line);
			if (i < customers.size() - 1) {
				textFileWriter.newLine();
			}
		}
		textFileWriter.close();	
		System.out.println(file.getAbsolutePath());
	}
	
	
}
