package com.github.ffpojo.samples.read;

import java.io.File;
import java.io.IOException;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.file.reader.FileSystemFlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReaderDefinition;
import com.github.ffpojo.samples.pojo.Customer;

public class SimpleFileSystemFlatFileReaderExample {

	//copy the file "SimpleFileSystemFlatFileReaderExample.txt" (make sure you have permission to read in the specified path):
	private static final String FILE_NAME = "SimpleFileSystemFlatFileReaderExample.txt";
	
	public static void main(String[] args) {
		SimpleFileSystemFlatFileReaderExample example = new SimpleFileSystemFlatFileReaderExample();
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
		
		
		ClassLoader classLoader = this.getClass().getClassLoader();
    	File inputFile = new File(classLoader.getResource(FILE_NAME).getFile());
    	
		if (!inputFile.exists()) {
			throw new IllegalStateException("File not found: " + FILE_NAME);
		}
		FlatFileReaderDefinition ffDefinition = new FlatFileReaderDefinition(Customer.class);
		FlatFileReader ffReader = new FileSystemFlatFileReader(inputFile, ffDefinition);
		for (Object record : ffReader) {
			Customer cust = (Customer)record;
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}
		ffReader.close();
	}
	
}
