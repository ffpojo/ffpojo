package com.github.ffpojo.container;

import java.io.IOException;
import java.io.InputStream;

import com.github.ffpojo.exception.MetadataContainerException;
import com.github.ffpojo.exception.MetadataReaderException;
import com.github.ffpojo.metadata.RecordDescriptor;
import com.github.ffpojo.reader.XmlMetadataReader;


class XmlMetadataContainer extends BaseMetadataContainer implements MetadataContainer {

	public XmlMetadataContainer(InputStream xmlMetadataInputStream) throws MetadataContainerException {
		XmlMetadataReader xmlMetadataReader = new XmlMetadataReader(xmlMetadataInputStream);
		try {
			this.recordDescriptorByClazzMap = xmlMetadataReader.readMetadata();
			xmlMetadataInputStream.close();
		} catch (MetadataReaderException e) {
			throw new MetadataContainerException("Error while reading ffpojo-ofm xml metadata", e);
		} catch (IOException e) {
			throw new MetadataContainerException("Error while reading ffpojo-ofm xml metadata", e);
		}
	}
	
	public RecordDescriptor getRecordDescriptor(Class<?> recordClazz) {
		return recordDescriptorByClazzMap.get(recordClazz);
	}

}
