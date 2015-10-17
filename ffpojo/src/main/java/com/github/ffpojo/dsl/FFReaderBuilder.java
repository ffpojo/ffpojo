package com.github.ffpojo.dsl;

import java.util.List;

public interface FFReaderBuilder<T> {

	public List<T> read ();
	
	public void read(ReadProcessor processor);
	
	public FFPojoRandomReaderBuilder<T> withThreads(int qtdeThreads);
	
}
