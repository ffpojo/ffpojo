package com.github.ffpojo.reader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.github.ffpojo.exception.MetadataReaderException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.RecordDescriptor;
import com.github.ffpojo.metadata.delimited.DelimitedFieldDescriptor;
import com.github.ffpojo.metadata.delimited.DelimitedRecordDescriptor;
import com.github.ffpojo.metadata.positional.PaddingAlign;
import com.github.ffpojo.metadata.positional.PositionalFieldDescriptor;
import com.github.ffpojo.metadata.positional.PositionalRecordDescriptor;
import com.github.ffpojo.util.ReflectUtil;

public class XmlMetadataReader {
	
	private static final String SCHEMA_CLASSPATH = "ffpojo-ofm.xsd";
	private static final String ATTRIBUTE_JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String ATTRIBUTE_JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private static final class XmlValidationErrorHandler implements ErrorHandler {
		public void error(SAXParseException exception) throws SAXException {
			throw exception;
		}
		public void fatalError(SAXParseException exception) throws SAXException {
			throw exception;
		}
		public void warning(SAXParseException exception) throws SAXException {
			throw exception;
		}
    }
    
	private InputStream xmlMetadataInputStream;

	public XmlMetadataReader(InputStream xmlMetadataInputStream) {
		this.xmlMetadataInputStream = xmlMetadataInputStream;
	}
	
	public Map<Class<?>, RecordDescriptor> readMetadata() throws MetadataReaderException {
		InputStream schemaFileInputStream = getClass().getClassLoader().getResourceAsStream(SCHEMA_CLASSPATH);

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setAttribute(ATTRIBUTE_JAXP_SCHEMA_LANGUAGE, XMLConstants.W3C_XML_SCHEMA_NS_URI);
		docBuilderFactory.setAttribute(ATTRIBUTE_JAXP_SCHEMA_SOURCE, schemaFileInputStream);
		docBuilderFactory.setValidating(true);
		docBuilderFactory.setNamespaceAware(true);
		docBuilderFactory.setIgnoringComments(true);
		docBuilderFactory.setCoalescing(true);		
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		
		Document doc;
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			docBuilder.setErrorHandler(new XmlValidationErrorHandler());
	        doc = docBuilder.parse(xmlMetadataInputStream);
		} catch (ParserConfigurationException e) {
			throw new MetadataReaderException(e);
		} catch (IOException e) {
			throw new MetadataReaderException(e);
		} catch (SAXException e) {
			throw new MetadataReaderException(e);
		} 
		
		Node rootNode = doc.getFirstChild();

		return readFFPojoMappingsNode(rootNode);
	}
	
	private Map<Class<?>, RecordDescriptor> readFFPojoMappingsNode(Node node) throws MetadataReaderException {
		Map<Class<?>, RecordDescriptor> recordDescriptorByClazzMap = new HashMap<Class<?>, RecordDescriptor>();
		if (node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					RecordDescriptor recordDescriptor = readFFPojoNode(childNode);
					if (recordDescriptor != null) {
						recordDescriptor.sortFieldDescriptors();
						recordDescriptor.assertValid();
						recordDescriptorByClazzMap.put(recordDescriptor.getRecordClazz(), recordDescriptor);
					}
				}
			}
		}
		return recordDescriptorByClazzMap;
	}
	
	private RecordDescriptor readFFPojoNode(Node node) throws MetadataReaderException {
		RecordDescriptor recordDescriptor = null;
		Class<?> recordClazz = null;
		if (node.hasAttributes()) {
			NamedNodeMap attributes = node.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attr = attributes.item(i);
				if (attr.getNodeName().equals("class")) {
					String recordClazzName = attr.getNodeValue();
					try {
						recordClazz = Class.forName(recordClazzName);
					} catch (ClassNotFoundException e) {
						throw new MetadataReaderException("Record class not found on classpath: " + recordClazzName, e);
					}
				}
			}
		}
		if (node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);			
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					if (childNode.getNodeName().equals("positional")) {
						PositionalRecordDescriptor positionalRecordDescriptor = new PositionalRecordDescriptor(); 
						positionalRecordDescriptor.setRecordClazz(recordClazz);
						readPositionalNode(childNode, positionalRecordDescriptor);
						recordDescriptor = positionalRecordDescriptor;
					} else if (childNode.getNodeName().equals("delimited")) {
						DelimitedRecordDescriptor delimitedRecordDescriptor = new DelimitedRecordDescriptor(); 
						delimitedRecordDescriptor.setRecordClazz(recordClazz);
						readDelimitedNode(childNode, delimitedRecordDescriptor);
						recordDescriptor = delimitedRecordDescriptor;
					}
				}
			}
		}
		return recordDescriptor;
	}
	
	private void readPositionalNode(Node node, PositionalRecordDescriptor recordDescriptorIn) throws MetadataReaderException {
		if (node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					readPositionalFieldNode(childNode, recordDescriptorIn);
				}
			}
		}
	}
	
	private void readPositionalFieldNode(Node node, PositionalRecordDescriptor recordDescriptorIn) throws MetadataReaderException {
		PositionalFieldDescriptor positionalFieldDescriptor = new PositionalFieldDescriptor();
		if (node.hasAttributes()) {
			NamedNodeMap attributes = node.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attr = attributes.item(i);
				if (attr.getNodeName().equals("name")) {
					String fieldName = attr.getNodeValue();
					Method getter;
					try {
						getter = ReflectUtil.getGetterFromFieldName(fieldName, recordDescriptorIn.getRecordClazz());
					} catch (SecurityException e) {
						throw new MetadataReaderException(e);
					} catch (NoSuchMethodException e) {
						throw new MetadataReaderException("Getter method not found for field name: " + fieldName, e);
					}
					positionalFieldDescriptor.setGetter(getter);
				} else if (attr.getNodeName().equals("decorator-class")) {
					String decoratorClazzName = attr.getNodeValue();
					FieldDecorator<?> decorator;
					try {
						Object decoratorObj = Class.forName(decoratorClazzName).newInstance();
						if (FieldDecorator.class.isInstance(decoratorObj)) {
							decorator = (FieldDecorator<?>)decoratorObj;
						} else {
							throw new MetadataReaderException("Decorator class must be a subtype of " + FieldDecorator.class);
						}
					} catch (ClassNotFoundException e) {
						throw new MetadataReaderException("Decorator class not found on classpath: " + decoratorClazzName, e);
					} catch (Exception e) {
						throw new MetadataReaderException("Error while instantiating decorator class: " + decoratorClazzName, e);
					}
					positionalFieldDescriptor.setDecorator(decorator);
				} else if (attr.getNodeName().equals("initial-position")) {
					int initialPosition = Integer.parseInt(attr.getNodeValue());
					positionalFieldDescriptor.setInitialPosition(initialPosition);
				} else if (attr.getNodeName().equals("final-position")) {
					int finalPosition = Integer.parseInt(attr.getNodeValue());
					positionalFieldDescriptor.setFinalPosition(finalPosition);
				} else if (attr.getNodeName().equals("padding-align")) {
					PaddingAlign paddingAlign = PaddingAlign.valueOf(attr.getNodeValue());
					positionalFieldDescriptor.setPaddingAlign(paddingAlign);
				} else if (attr.getNodeName().equals("padding-character")) {
					String paddingCharacter = attr.getNodeValue();
					positionalFieldDescriptor.setPaddingCharacter(paddingCharacter.charAt(0));
				} else if (attr.getNodeName().equals("trim-on-read")) {
					boolean trimOnRead = Boolean.valueOf(attr.getNodeValue());
					positionalFieldDescriptor.setTrimOnRead(trimOnRead);
				}
			}
		}
		recordDescriptorIn.getFieldDescriptors().add(positionalFieldDescriptor);
	}
	
	private void readDelimitedNode(Node node, DelimitedRecordDescriptor recordDescriptorIn) throws MetadataReaderException {
		if (node.hasAttributes()) {
			NamedNodeMap attributes = node.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attr = attributes.item(i);
				if (attr.getNodeName().equals("delimiter")) {
					String delimiter = attr.getNodeValue();
					recordDescriptorIn.setDelimiter(delimiter);
				}
			}
		}
		if (node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					readDelimitedFieldNode(childNode, recordDescriptorIn);
				}
			}
		}
	}
	
	private void readDelimitedFieldNode(Node node, DelimitedRecordDescriptor recordDescriptorIn) throws MetadataReaderException {
		DelimitedFieldDescriptor delimitedFieldDescriptor = new DelimitedFieldDescriptor();
		if (node.hasAttributes()) {
			NamedNodeMap attributes = node.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attr = attributes.item(i);
				if (attr.getNodeName().equals("name")) {
					String fieldName = attr.getNodeValue();
					Method getter;
					try {
						getter = ReflectUtil.getGetterFromFieldName(fieldName, recordDescriptorIn.getRecordClazz());
					} catch (SecurityException e) {
						throw new MetadataReaderException(e);
					} catch (NoSuchMethodException e) {
						throw new MetadataReaderException("Getter method not found for field name: " + fieldName, e);
					}
					delimitedFieldDescriptor.setGetter(getter);
				} else if (attr.getNodeName().equals("decorator-class")) {
					String decoratorClazzName = attr.getNodeValue();
					FieldDecorator<?> decorator;
					try {
						Object decoratorObj = Class.forName(decoratorClazzName).newInstance();
						if (FieldDecorator.class.isInstance(decoratorObj)) {
							decorator = (FieldDecorator<?>)decoratorObj;
						} else {
							throw new MetadataReaderException("Decorator class must be a subtype of " + FieldDecorator.class);
						}
					} catch (ClassNotFoundException e) {
						throw new MetadataReaderException("Decorator class not found on classpath: " + decoratorClazzName, e);
					} catch (Exception e) {
						throw new MetadataReaderException("Error while instantiating decorator class: " + decoratorClazzName, e);
					}
					delimitedFieldDescriptor.setDecorator(decorator);
				} else if (attr.getNodeName().equals("position-index")) {
					int positionIndex = Integer.parseInt(attr.getNodeValue());
					delimitedFieldDescriptor.setPositionIndex(positionIndex);
				}
			}
		}
		recordDescriptorIn.getFieldDescriptors().add(delimitedFieldDescriptor);
	}

}
