package com.github.ffpojo.samples.write;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.file.writer.FileSystemFlatFileWriter;
import com.github.ffpojo.file.writer.FlatFileWriter;
import com.github.ffpojo.mock.CustomerMockFactory;
import com.github.ffpojo.samples.pojo.Customer;

/**
 * @author Gilberto Holms
 * @author William Miranda
 *
 */
public class SimpleFileSystemFlatFileWriterExample {

	//change here (make sure you have permission to write in the specified path):
	private static final String OUTPUT_TXT_OS_PATH = System.getProperty("java.io.tmpdir") + "SimpleFileSystemFlatFileWriterExample.txt";
	
	public static void main(String[] args) {
		try {
			System.out.println("Making TXT from POJO...");
			writeCustomersToText();
			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeCustomersToText() throws IOException, FFPojoException {
		File file = new File(OUTPUT_TXT_OS_PATH);
		file.createNewFile();
		System.out.println(file.getPath());
		FlatFileWriter ffWriter = new FileSystemFlatFileWriter(file, true);
		List<Customer> customers = CustomerMockFactory.createCustomersMockList();
		ffWriter.writeRecordList(customers);
		ffWriter.close();	
	}
	
}
