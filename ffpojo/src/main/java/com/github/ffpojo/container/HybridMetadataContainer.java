package com.github.ffpojo.container;

import java.io.InputStream;

import com.github.ffpojo.exception.MetadataContainerException;
import com.github.ffpojo.metadata.RecordDescriptor;


public class HybridMetadataContainer extends BaseMetadataContainer implements MetadataContainer {

	private static final String XML_OFM_CLASSPATH = "ffpojo-ofm.xml";
	
	private MetadataContainer annotationMetadataContainer;
	private MetadataContainer xmlMetadataContainer;

	public HybridMetadataContainer() throws MetadataContainerException {
		this.annotationMetadataContainer = new AnnotationMetadataContainer();
		InputStream xmlFileInputStream = getClass().getClassLoader().getResourceAsStream(XML_OFM_CLASSPATH);
		if (xmlFileInputStream != null) {
			this.xmlMetadataContainer = new XmlMetadataContainer(xmlFileInputStream);
		}
	}
	
	public RecordDescriptor getRecordDescriptor(Class<?> recordClazz) throws MetadataContainerException {
		RecordDescriptor recordDescriptor = recordDescriptorByClazzMap.get(recordClazz);
		if (recordDescriptor == null) {
			if (xmlMetadataContainer != null) {
				recordDescriptor = xmlMetadataContainer.getRecordDescriptor(recordClazz);
			}
			if (recordDescriptor == null) {
				recordDescriptor = annotationMetadataContainer.getRecordDescriptor(recordClazz);
			}
			if (recordDescriptor == null) {
				throw new MetadataContainerException("Object-Flat-Mapping metadata not found for class " + recordClazz);
			} else {
				recordDescriptorByClazzMap.put(recordClazz, recordDescriptor);
			}
		}
		return recordDescriptor;
	}
	
}
