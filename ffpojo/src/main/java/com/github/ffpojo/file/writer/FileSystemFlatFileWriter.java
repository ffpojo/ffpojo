package com.github.ffpojo.file.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.file.reader.FlatFileReader;

public class FileSystemFlatFileWriter extends BaseFlatFileWriter implements FlatFileWriter {

	private FFPojoHelper ffpojoHelper;
	private BufferedWriter writer;

	public FileSystemFlatFileWriter(File file, boolean createIfNotExists) throws IOException, FFPojoException {
		if (file == null) {
			throw new IllegalArgumentException("File object is null");
		} else {
			if (!file.exists()) {
				if (createIfNotExists) {
					file.createNewFile();
				} else {
					throw new IllegalArgumentException("Specified file does not exists: " + file.getName());
				}
			}
			if (!file.isFile()) {
				throw new IllegalArgumentException("Specified file does not represent a file: " + file.getName());
		    } else if (!file.canWrite()) {
		    	throw new IllegalArgumentException("Specified file cannot be written, please check the SO permissions: " + file.getName());
		    }
		}
		this.ffpojoHelper = FFPojoHelper.getInstance();
		this.writer = new BufferedWriter(new FileWriter(file));
	}
	
	public void writeRecord(Object record) throws IOException, FFPojoException {
		if (record == null) {
			throw new IllegalArgumentException("Record object is null");
		} else {
			String recordText = ffpojoHelper.parseToText(record);
			if (recordsWritten > 0) {
				writer.newLine();
			}
			writer.write(recordText);
			recordsWritten++;
		}
	}
	
	public void writeRecordArray(Object[] recordArray) throws IOException, FFPojoException {
		if (recordArray != null && recordArray.length > 0) {
			for(Object rec : recordArray) {
				this.writeRecord(rec);
			}
		}
	}
	
	public void writeRecordList(Collection<?> recordCollection) throws IOException, FFPojoException {
		if (recordCollection != null && !recordCollection.isEmpty()) {
			for(Object rec : recordCollection) {
				this.writeRecord(rec);
			}
		}
	}
	
	public void writeFromFlatFileReader(FlatFileReader flatFileReader) throws IOException, FFPojoException {
		if (flatFileReader == null || flatFileReader.isClosed()) {
			throw new IllegalArgumentException("FlatFileReader object is null or closed");
		}
		flatFileReader.reset();
		for(Object record : flatFileReader) {
			this.writeRecord(record);
		}
		flatFileReader.close();
	}
	
	public void close() throws IOException {
		if (writer != null) {
			this.writer.close();
			this.writer = null;
		}
		this.closed = true;
		System.gc();
	}

}
