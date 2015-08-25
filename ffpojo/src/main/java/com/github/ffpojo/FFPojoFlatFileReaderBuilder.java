package com.github.ffpojo;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.ffpojo.metadata.positional.annotation.FFPojoAccessorType;

public class FFPojoFlatFileReaderBuilder {
	
	private File file;
	private final Set<Class<?>> classes =  new HashSet<Class<?>>();
	
	public AddClassBuilder withFile(File file){
		this.file =  file;
		return new AddClassBuilderImpl(this);
	}
	
	public InputStreamReaderBuilder withInputStream(InputStream inputStream) {
		return null;
	}

	private class AddClassBuilderImpl implements AddClassBuilder{
		private FFPojoFlatFileReaderBuilder builder;
		
		public AddClassBuilderImpl(FFPojoFlatFileReaderBuilder builder) {
			this.builder =  builder;
		}
		public FFReaderBuilder addRecordClass(Class<?> clazz) {
			classes.add(clazz);
			return new FFReaderBuilderImpl(builder);
		}
		public FFReaderBuilder addRecordClasses(List<Class<?>> clazzes) {
			classes.addAll(clazzes);
			return new FFReaderBuilderImpl(builder);
		}
	}
	
	private class FFReaderBuilderImpl implements FFReaderBuilder{
		
		private FFPojoFlatFileReaderBuilder builder;

		public FFReaderBuilderImpl(FFPojoFlatFileReaderBuilder fFPojoFlatFileReaderBuilder){
			this.builder =  fFPojoFlatFileReaderBuilder;
		}
		public List<?> read() {
			return null;
		}
		
	}
	
	public static void main(String[] args) {
		new FFPojoFlatFileReaderBuilder()
			.withFile(new File(""))
			.addRecordClass(FFPojoAccessorType.class)
			.read();
	}

}