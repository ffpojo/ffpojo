## FFPOJO - Flat File POJO Parser ## [![Build Status](https://travis-ci.org/ffpojo/ffpojo.svg)](https://travis-ci.org/ffpojo/ffpojo)

The **FFPOJO Project** is a **Flat-File Parser**, POJO based, library for Java applications. It's a **Object-Oriented** approach to work to flat-files, because the libray is based on **POJOs** and an **Object-Flat-Mapping (OFM)**, using **Java Annotations**, **XML** or **both**. When used together, the XML mapping **overrides** the annotations.

The FFPOJO library can work to positional and delimited flat-files, and provides many features like:

* Annotations and/or XML mappings, with overridding (like the JPA api)
* Uses a "Metadata Container" pattern, that caches metadata information in static way to improve performance on parsing
* Simple text-to-pojo and pojo-to-text parsing
* Easy custom-type conversion when reading or writing, through the decorator API
* Full object-oriented flat-file reader that supports complex files with Header, Body and Trailer definitions
* File system flat-file reader developed with NIO, that performs 25% faster than regular buffered reader
* Record processor layer, that works in "Push" model and supports single-threaded or multi-threaded file processing
* Full object-oriented flat-file writer
* Lightweight, no dependence to any other framework or API

### Starting Points ###

* [Releases](https://github.com/ffpojo/ffpojo/tags)
* [Getting Started](#getting-started)
* [Samples](#samples)
* [XML Schema](https://github.com/ffpojo/ffpojo/blob/master/readme-ffpojo-ofm.xsd)

---

#### Getting Started Tutorial <a id="getting-started"/> ####
This is a samples based tutorial, that shows the most important features of FFPOJO in a hurry.
To run the sample codes you will need to download the sample text file resources, that are used in the codes.
Download full samples in project named **ffpojo-samples**.

##### Building The Project #####

When comming to GitHub this project was migrated to Maven, then building the project is very simple, default maven style. This project also
have no dependencies, so thats simplier at most. The build process is shown below:

1. Install Apache Maven (version 2+)
1. Append the Maven bin path to your PATH environment variable
1. Open the command line and go to the ffpojo folder that have the "pom.xml" file
1. Enter the command below: `mvn clean install`
1. Now its just pick-up the generated jar file at the "target" folder.

---

##### Metadata Definitions #####
To describe your flat-file records and fields you must use **Java Annotations and/or XML**. If used both, the XML information will override the annotations
on a **class level**. It means that you can't override a single field, but you can override an entire record class.

---

##### Record Class #####
The record class is a Plain-Old-Java-Object that represents a line in a flat-file. One record can be Positional or Delimited (not both). The only rule is
that the record class must be in JavaBeans standard, providing one getter (pojo-to-text parsing) and one setter (text-to-pojo parsing) for each field.

---

##### Record Field #####
The record field is record class attribute that represents a field-token in a flat-file. Positional records has Positional Fields and Delimited Records has
Delimited Fields (we can't mix cats and dogs). By convention, the annotation metadata for fields must appear in the **GETTER METHODS**.

---

##### Object-Flat-Mapping XML Schema #####
If you choose to map your ffpojos using XML, it must be valid according to the [ffpojo-ofm.xsd](https://github.com/ffpojo/ffpojo/blob/master/readme-ffpojo-ofm.xsd) schema.

---

##### Object-Flat-Mapping XML Example #####

```xml
<ffpojo-mappings xmlns="http://www.ffpojo.org/ofm">
	<ffpojo class="com.domain.pkg.Customer">
		<positional>
			<positional-field name="name" initial-position="1" final-position="5"/>
			<positional-field name="address" initial-position="6" final-position="10"/>
		</positional>
	</ffpojo>
	<ffpojo class="com.domain.pkg.Employee">
		<delimited delimiter=";">
			<delimited-field name="id" position-index="1" decorator-class="com.domain.pkg.MyLongDecorator"/>
			<delimited-field name="endereco" position-index="2"/>
		</delimited>
	</ffpojo>
</ffpojo-mappings>
```

---

#### Index of Samples <a id="samples"/> ####

1. [Simple Positional Record Parsing Example](#example-1)
1. [Simple Delimited Record Parsing Example](#example-2)
1. [Positional Record Parsing With Decorator Example](#example-3)
1. [Simple File System Flat File Reader Example](#example-4)
1. [Simple Input Stream Flat File Reader Example](#example-5)
1. [File System Flat File Reader With Header And Trailer Example](#example-6)
1. [Simple File System Flat File Reader With Default Flat File Processor Example](#example-7)
1. [Simple File System Flat File Reader With Thread Pool Flat File Processor Example](#example-8)
1. [Simple File System Flat File Reader With Thread Pool Flat File Processor And Error Handler Example](#example-9)
1. [Simple File System Flat File Writer Example](#example-10)

---

##### 1 - Simple Positional Record Parsing Example <a id="example-1"/> #####

```java
// package and imports omitted

public class SimplePositionalRecordParsingExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "SimplePositionalRecordParsingExampleInput.txt";

	//change here (make sure you have permission to write in the specified path):
	private static final String OUTPUT_TXT_OS_PATH = "C:/Users/gibaholms/Desktop/SimplePositionalRecordParsingExampleOutput.txt";

	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;

		@PositionalField(initialPosition = 1, finalPosition = 5)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}

		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

	public static void main(String[] args) {
		SimplePositionalRecordParsingExample example = new SimplePositionalRecordParsingExample();
		try {
			System.out.println("Making POJO from TXT...");
			example.readCustomersFromText();

			System.out.println("Making TXT from POJO...");
			example.writeCustomersToText();

			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}

	public void readCustomersFromText() throws IOException, FFPojoException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		BufferedReader textFileReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT_TXT_RESOURCE_CLASSPATH)));
		String line;
		while ( (line = textFileReader.readLine()) != null) {
			Customer cust = ffpojo.createFromText(Customer.class, line);
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}
		textFileReader.close();
	}

	public void writeCustomersToText() throws IOException, FFPojoException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		File file = new File(OUTPUT_TXT_OS_PATH);
		file.createNewFile();
		BufferedWriter textFileWriter = new BufferedWriter(new FileWriter(file));
		List<Customer> customers = createCustomersMockList();
		for (int i = 0; i < customers.size(); i++) {
			String line = ffpojo.parseToText(customers.get(i));
			textFileWriter.write(line);
			if (i < customers.size() - 1) {
				textFileWriter.newLine();
			}
		}
		textFileWriter.close();
	}

	private static List<Customer> createCustomersMockList() {
		List<Customer> customers = new ArrayList<Customer>();
		{
			Customer cust = new Customer();
			cust.setId(98456L);
			cust.setName("Axel Rose");
			cust.setEmail("axl@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(65478L);
			cust.setName("Bono Vox");
			cust.setEmail("bono@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(78425L);
			cust.setName("Bob Marley");
			cust.setEmail("marley@thehost.com");
			customers.add(cust);
		}
		return customers;
	}

}
```

---

##### 2 - Simple Delimited Record Parsing Example <a id="example-2"/> #####

```java
// package and imports omitted

public class SimpleDelimitedRecordParsingExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "SimpleDelimitedRecordParsingExampleInput.txt";

	//change here (make sure you have permission to write in the specified path):
	private static final String OUTPUT_TXT_OS_PATH = "C:/Users/gibaholms/Desktop/SimpleDelimitedRecordParsingExampleOutput.txt";

	@DelimitedRecord(delimiter = "|")
	public static class Customer {

		private Long id;
		private String name;
		private String email;

		@DelimitedField(positionIndex = 1)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}

		@DelimitedField(positionIndex = 2)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@DelimitedField(positionIndex = 3)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

	public static void main(String[] args) {
		SimpleDelimitedRecordParsingExample example = new SimpleDelimitedRecordParsingExample();
		try {
			System.out.println("Making POJO from TXT...");
			example.readCustomersFromText();

			System.out.println("Making TXT from POJO...");
			example.writeCustomersToText();

			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}

	public void readCustomersFromText() throws IOException, FFPojoException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		BufferedReader textFileReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT_TXT_RESOURCE_CLASSPATH)));
		String line;
		while ( (line = textFileReader.readLine()) != null) {
			Customer cust = ffpojo.createFromText(Customer.class, line);
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}
		textFileReader.close();
	}

	public void writeCustomersToText() throws IOException, FFPojoException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		File file = new File(OUTPUT_TXT_OS_PATH);
		file.createNewFile();
		BufferedWriter textFileWriter = new BufferedWriter(new FileWriter(file));
		List<Customer> customers = createCustomersMockList();
		for (int i = 0; i < customers.size(); i++) {
			String line = ffpojo.parseToText(customers.get(i));
			textFileWriter.write(line);
			if (i < customers.size() - 1) {
				textFileWriter.newLine();
			}
		}
		textFileWriter.close();
	}

	private static List<Customer> createCustomersMockList() {
		List<Customer> customers = new ArrayList<Customer>();
		{
			Customer cust = new Customer();
			cust.setId(98456L);
			cust.setName("Axel Rose");
			cust.setEmail("axl@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(65478L);
			cust.setName("Bono Vox");
			cust.setEmail("bono@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(78425L);
			cust.setName("Bob Marley");
			cust.setEmail("marley@thehost.com");
			customers.add(cust);
		}
		return customers;
	}

}
```

---

##### 3 - Positional Record Parsing With Decorator Example <a id="example-3"/> #####

```java
// package and imports omitted

public class PositionalRecordParsingWithDecoratorExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "PositionalRecordParsingWithDecoratorExampleInput.txt";

	//change here (make sure you have permission to write in the specified path):
	private static final String OUTPUT_TXT_OS_PATH = "C:/Users/gibaholms/Desktop/PositionalRecordParsingWithDecoratorExampleOutput.txt";

	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;
		private Date birthDate;

		@PositionalField(initialPosition = 1, finalPosition = 5, decorator = LongDecorator.class)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}

		@PositionalField(initialPosition = 56, finalPosition = 65, decorator = DateDecorator.class)
		public Date getBirthDate() {
			return birthDate;
		}
		public void setBirthDate(Date birthDate) {
			this.birthDate = birthDate;
		}
	}

	public static class LongDecorator extends DefaultFieldDecorator {
		@Override
		public Object fromString(String str) {
			return Long.valueOf(str);
		}
	}

	public static class DateDecorator implements FieldDecorator<Date> {
		private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		public Date fromString(String str) throws FieldDecoratorException {
			try {
				return sdf.parse(str);
			} catch (ParseException e) {
				throw new FieldDecoratorException("Error while parsing date field from string: " + str, e);
			}
		}

		public String toString(Date obj) {
			Date date = (Date)obj;
			return sdf.format(date);
		}
	}

	public static void main(String[] args) {
		PositionalRecordParsingWithDecoratorExample example = new PositionalRecordParsingWithDecoratorExample();
		try {
			System.out.println("Making POJO from TXT...");
			example.readCustomersFromText();

			System.out.println("Making TXT from POJO...");
			example.writeCustomersToText();

			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}

	public void readCustomersFromText() throws IOException, FFPojoException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		BufferedReader textFileReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT_TXT_RESOURCE_CLASSPATH)));
		String line;
		while ( (line = textFileReader.readLine()) != null) {
			Customer cust = ffpojo.createFromText(Customer.class, line);
			System.out.printf("[%d][%s][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail(), cust.getBirthDate());
		}
		textFileReader.close();
	}

	public void writeCustomersToText() throws IOException, FFPojoException {
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		File file = new File(OUTPUT_TXT_OS_PATH);
		file.createNewFile();
		BufferedWriter textFileWriter = new BufferedWriter(new FileWriter(file));
		List<Customer> customers = createCustomersMockList();
		for (int i = 0; i < customers.size(); i++) {
			String line = ffpojo.parseToText(customers.get(i));
			textFileWriter.write(line);
			if (i < customers.size() - 1) {
				textFileWriter.newLine();
			}
		}
		textFileWriter.close();
	}

	private static List<Customer> createCustomersMockList() {
		List<Customer> customers = new ArrayList<Customer>();
		{
			Customer cust = new Customer();
			cust.setId(98456L);
			cust.setName("Axel Rose");
			cust.setEmail("axl@thehost.com");
			cust.setBirthDate(new Date());
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(65478L);
			cust.setName("Bono Vox");
			cust.setEmail("bono@thehost.com");
			cust.setBirthDate(new Date());
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(78425L);
			cust.setName("Bob Marley");
			cust.setEmail("marley@thehost.com");
			cust.setBirthDate(new Date());
			customers.add(cust);
		}
		return customers;
	}

}
```

---

##### 4 - Simple File System Flat File Reader Example <a id="example-4"/> #####

```java
// package and imports omitted

public class SimpleFileSystemFlatFileReaderExample {

	//copy the file "SimpleFileSystemFlatFileReaderExample.txt" (make sure you have permission to read in the specified path):
	private static final String INPUT_TXT_OS_PATH = "C:/Users/gholms/Desktop/SimpleFileSystemFlatFileReaderExample.txt";

	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;

		@PositionalField(initialPosition = 1, finalPosition = 5)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}

		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

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
		File inputFile = new File(INPUT_TXT_OS_PATH);
		if (!inputFile.exists()) {
			throw new IllegalStateException("File not found: " + INPUT_TXT_OS_PATH);
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
```

---

##### 5 - Simple Input Stream Flat File Reader Example <a id="example-5"/> #####

```java
// package and imports omitted

public class SimpleInputStreamFlatFileReaderExample {

	private static final String INPUT_TXT_RESOURCE_CLASSPATH = "SimpleInputStreamFlatFileReaderExample.txt";

	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;

		@PositionalField(initialPosition = 1, finalPosition = 5)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}

		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

	public static void main(String[] args) {
		SimpleInputStreamFlatFileReaderExample example = new SimpleInputStreamFlatFileReaderExample();
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
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(INPUT_TXT_RESOURCE_CLASSPATH);
		FlatFileReaderDefinition ffDefinition = new FlatFileReaderDefinition(Customer.class);
		FlatFileReader ffReader = new InputStreamFlatFileReader(inputStream, ffDefinition);
		for (Object record : ffReader) {
			Customer cust = (Customer)record;
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}
		ffReader.close();
	}

}
```

---

##### 6 - File System Flat File Reader With Header And Trailer Example <a id="example-6"/> #####

```java
// package and imports omitted

public class FileSystemFlatFileReaderWithHeaderAndTrailerExample {

	//copy the file "FileSystemFlatFileReaderWithHeaderAndTrailerExample.txt" (make sure you have permission to read in the specified path):
	private static final String INPUT_TXT_OS_PATH = "C:/Users/gholms/Desktop/FileSystemFlatFileReaderWithHeaderAndTrailerExample.txt";

	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;

		@PositionalField(initialPosition = 1, finalPosition = 5)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}

		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

	@PositionalRecord
	public static class Header {

		private Long controlNumber;
		private Date processDate;

		@PositionalField(initialPosition = 1, finalPosition = 10)
		public Long getControlNumber() {
			return controlNumber;
		}
		public void setControlNumber(Long controlNumber) {
			this.controlNumber = controlNumber;
		}
		// must use a String setter or a FieldDecorator
		public void setControlNumber(String controlNumber) {
			this.controlNumber = Long.valueOf(controlNumber);
		}

		@PositionalField(initialPosition = 11, finalPosition = 20, decorator = DateDecorator.class)
		public Date getProcessDate() {
			return processDate;
		}
		public void setProcessDate(Date processDate) {
			this.processDate = processDate;
		}
	}

	@PositionalRecord
	public static class Trailer {

		private Integer recordsCount;

		@PositionalField(initialPosition = 1, finalPosition = 4)
		public Integer getRecordsCount() {
			return recordsCount;
		}
		public void setRecordsCount(Integer recordsCount) {
			this.recordsCount = recordsCount;
		}
		// must use a String setter or a FieldDecorator
		public void setRecordsCount(String recordsCount) {
			this.recordsCount = Integer.valueOf(recordsCount);
		}
	}

	public static class DateDecorator implements FieldDecorator<Date> {
		private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		public Date fromString(String str) throws FieldDecoratorException {
			try {
				return sdf.parse(str);
			} catch (ParseException e) {
				throw new FieldDecoratorException("Error while parsing date field from string: " + str, e);
			}
		}

		public String toString(Date obj) {
			Date date = (Date)obj;
			return sdf.format(date);
		}
	}

	public static void main(String[] args) {
		FileSystemFlatFileReaderWithHeaderAndTrailerExample example = new FileSystemFlatFileReaderWithHeaderAndTrailerExample();
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
		File inputFile = new File(INPUT_TXT_OS_PATH);
		if (!inputFile.exists()) {
			throw new IllegalStateException("File not found: " + INPUT_TXT_OS_PATH);
		}
		FlatFileReaderDefinition ffDefinition = new FlatFileReaderDefinition(Customer.class);
		ffDefinition.setHeader(Header.class);
		ffDefinition.setTrailer(Trailer.class);
		FlatFileReader ffReader = new FileSystemFlatFileReader(inputFile, ffDefinition);
		for (Object record : ffReader) {
			RecordType recordType = ffReader.getRecordType();
			if (recordType == RecordType.HEADER) {
				System.out.print("HEADER FOUND: ");
				Header header = (Header)record;
				System.out.printf("[%d][%s]\n", header.getControlNumber(), header.getProcessDate());
			} else if (recordType == RecordType.BODY) {
				Customer cust = (Customer)record;
				System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
			} else if (recordType == RecordType.TRAILER) {
				System.out.print("TRAILER FOUND: ");
				Trailer trailer = (Trailer)record;
				System.out.printf("[%d]\n", trailer.getRecordsCount());
			}
		}
		ffReader.close();
	}

}
```

---

##### 7 - Simple File System Flat File Reader With Default Flat File Processor Example <a id="example-7"/> #####

```java
// package and imports omitted

public class SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample {

	//copy the file "SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample.txt" (make sure you have permission to read in the specified path):
	private static final String INPUT_TXT_OS_PATH = "C:/Users/gholms/Desktop/SimpleFileSystemFlatFileReaderWithDefaultFlatFileProcessorExample.txt";

	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;

		@PositionalField(initialPosition = 1, finalPosition = 5)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}

		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

	public static class CustomerRecordProcessor implements RecordProcessor {

		public void processBody(RecordEvent event) {
			Customer cust = (Customer)event.getRecord();
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}

		public void processHeader(RecordEvent event) {
			// blank
		}

		public void processTrailer(RecordEvent event) {
			// blank
		}

	}

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
		File inputFile = new File(INPUT_TXT_OS_PATH);
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
```

---

##### 8 - Simple File System Flat File Reader With Thread Pool Flat File Processor Example <a id="example-8"/> #####

```java
// package and imports omitted

public class SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorExample {

	//copy the file "SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorExample.txt" (make sure you have permission to read in the specified path):
	private static final String INPUT_TXT_OS_PATH = "C:/Users/gholms/Desktop/SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorExample.txt";

	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;

		@PositionalField(initialPosition = 1, finalPosition = 5)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}

		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

	// processor class must be thread-safe !
	public static class CustomerRecordProcessor implements RecordProcessor {

		public void processBody(RecordEvent event) {
			Customer cust = (Customer)event.getRecord();
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
		}

		public void processHeader(RecordEvent event) {
			// blank
		}

		public void processTrailer(RecordEvent event) {
			// blank
		}

	}

	public static void main(String[] args) {
		SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorExample example = new SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorExample();
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
		File inputFile = new File(INPUT_TXT_OS_PATH);
		if (!inputFile.exists()) {
			throw new IllegalStateException("File not found: " + INPUT_TXT_OS_PATH);
		}
		FlatFileReaderDefinition ffDefinition = new FlatFileReaderDefinition(Customer.class);
		FlatFileReader ffReader = new FileSystemFlatFileReader(inputFile, ffDefinition);
		FlatFileProcessor ffProcessor = new ThreadPoolFlatFileProcessor(ffReader, 5);
		ffProcessor.processFlatFile(new CustomerRecordProcessor());
		ffReader.close();
	}

}
```

---

##### 9 - Simple File System Flat File Reader With Thread Pool Flat File Processor And Error Handler Example <a id="example-9"/> #####

```java
// package and imports omitted

public class SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample {

	//copy the file "SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample.txt" (make sure you have permission to read in the specified path):
	private static final String INPUT_TXT_OS_PATH = "C:/Users/gholms/Desktop/SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample.txt";

	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;

		@PositionalField(initialPosition = 1, finalPosition = 5)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}

		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

	// processor class must be thread-safe !
	public static class CustomerRecordProcessor extends DefaultRecordProcessor {

		public void processBody(RecordEvent event) throws RecordProcessorException {
			Customer cust = (Customer)event.getRecord();
			System.out.printf("[%d][%s][%s]\n", cust.getId(), cust.getName(), cust.getEmail());
			throw new RecordProcessorException("An error occurred !!!");
		}

	}

	public static class CustomerErrorHandler implements ErrorHandler {

		public void error(RecordProcessorException exception) throws RecordProcessorException {
			System.out.println("ErrorHandler executed !");
		}

	}

	public static void main(String[] args) {
		SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample example = new SimpleFileSystemFlatFileReaderWithThreadPoolFlatFileProcessorAndErrorHandlerExample();
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
		File inputFile = new File(INPUT_TXT_OS_PATH);
		if (!inputFile.exists()) {
			throw new IllegalStateException("File not found: " + INPUT_TXT_OS_PATH);
		}
		FlatFileReaderDefinition ffDefinition = new FlatFileReaderDefinition(Customer.class);
		FlatFileReader ffReader = new FileSystemFlatFileReader(inputFile, ffDefinition);
		FlatFileProcessor ffProcessor = new ThreadPoolFlatFileProcessor(ffReader, 5);
		ffProcessor.setErrorHandler(new CustomerErrorHandler());
		ffProcessor.processFlatFile(new CustomerRecordProcessor());
		ffReader.close();
	}

}
```

---

##### 10 - Simple File System Flat File Writer Example <a id="example-10"/> #####

```java
// package and imports omitted

public class SimpleFileSystemFlatFileWriterExample {

	//change here (make sure you have permission to write in the specified path):
	private static final String OUTPUT_TXT_OS_PATH = "C:/Users/gibaholms/Desktop/SimpleFileSystemFlatFileWriterExample.txt";

	@PositionalRecord
	public static class Customer {

		private Long id;
		private String name;
		private String email;

		@PositionalField(initialPosition = 1, finalPosition = 5)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		// must use a String setter or a FieldDecorator
		public void setId(String id) {
			this.id = Long.valueOf(id);
		}

		@PositionalField(initialPosition = 6, finalPosition = 25)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		@PositionalField(initialPosition = 26, finalPosition = 55)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

	public static void main(String[] args) {
		SimplePositionalRecordParsingExample example = new SimplePositionalRecordParsingExample();
		try {
			System.out.println("Making TXT from POJO...");
			example.writeCustomersToText();

			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}

	public void writeCustomersToText() throws IOException, FFPojoException {
		File file = new File(OUTPUT_TXT_OS_PATH);
		FlatFileWriter ffWriter = new FileSystemFlatFileWriter(file, true);
		List<Customer> customers = createCustomersMockList();
		ffWriter.writeRecordList(customers);
		ffWriter.close();
	}

	private static List<Customer> createCustomersMockList() {
		List<Customer> customers = new ArrayList<Customer>();
		{
			Customer cust = new Customer();
			cust.setId(98456L);
			cust.setName("Axel Rose");
			cust.setEmail("axl@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(65478L);
			cust.setName("Bono Vox");
			cust.setEmail("bono@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(78425L);
			cust.setName("Bob Marley");
			cust.setEmail("marley@thehost.com");
			customers.add(cust);
		}
		return customers;
	}

}
```
