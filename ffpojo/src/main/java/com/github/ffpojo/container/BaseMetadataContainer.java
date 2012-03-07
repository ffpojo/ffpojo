package com.github.ffpojo.container;

import java.util.HashMap;
import java.util.Map;

import com.github.ffpojo.metadata.RecordDescriptor;


abstract class BaseMetadataContainer implements MetadataContainer {

	protected Map<Class<?>, RecordDescriptor> recordDescriptorByClazzMap;

	protected BaseMetadataContainer() {
		this.recordDescriptorByClazzMap = new HashMap<Class<?>, RecordDescriptor>();
	}

}
