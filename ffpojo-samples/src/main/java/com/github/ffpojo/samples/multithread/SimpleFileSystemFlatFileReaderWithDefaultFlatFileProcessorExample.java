package com.github.ffpojo.samples.multithread;

import java.io.File;
import java.io.IOException;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.file.processor.DefaultFlatFileProcessor;
import com.github.ffpojo.file.processor.FlatFileProcessor;
import com.github.ffpojo.file.reader.FileSystemFlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReader;
import com.github.ffpojo.file.reader.FlatFileReaderDefinition;
import com.github.ffpojo.processor.CustomerRecordProcessor;
import com.github.ffpojo.samples.pojo.Customer;


/**
 * @author Gilbertor Holms
 * @author William Miranda
 *
 */
public class SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample {

	//copy the file "SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample.txt" (make sure you have permission to read in the specified path):
	private static final String INPUT_TXT_OS_PATH = "SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample.txt";
	
	public static void main(String[] args) {
		SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample example = new SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample();
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
    	File inputFile = new File(classLoader.getResource(INPUT_TXT_OS_PATH).getFile());
		if (!inputFile.exists()) {
			throw new IllegalStateException("File not found: " + INPUT_TXT_OS_PATH);
		}
		FlatFileReaderDefinition ffDefinition = new FlatFileReaderDefinition(Customer.class);
		FlatFileReader ffReader = new FileSystemFlatFileReader(inputFile, ffDefinition);
		FlatFileProcessor ffProcessor = new DefaultFlatFileProcessor(ffReader);
		ffProcessor.processFlatFile(new CustomerRecordProcessor());
		ffReader.close();
	}
	
}
