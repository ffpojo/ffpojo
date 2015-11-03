package com.github.ffpojo.samples.delimiter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedField;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedRecord;
import com.github.ffpojo.metadata.delimited.annotation.extra.LongDelimitedField;

/**
 * @author Gilberto Holms
 * @author William Miranda
 *
 */
public class SimpleDelimitedRecordParsingReadExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "SimpleDelimitedRecordParsingExampleInput.txt";
	
	@DelimitedRecord(delimiter = "|")
	public static class Customer {

		@LongDelimitedField(positionIndex = 1)
		private Long id;
		@DelimitedField(positionIndex = 2)
		private String name;
		private String email;

		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		@DelimitedField(positionIndex = 3)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}
	
	public static void main(String[] args) {
		SimpleDelimitedRecordParsingReadExample example = new SimpleDelimitedRecordParsingReadExample();
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
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}
		textFileReader.close();
	}
	
}
