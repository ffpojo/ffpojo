package com.github.ffpojo.test;

import junit.framework.Assert;

import org.junit.Test;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedField;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedRecord;

public class DelimitedRecordParserTest {

	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_quando_os_campos_possuem_indices_complementares_iniciando_no_primeiro_indice() throws FFPojoException {
		TestPojo1 pojo = new TestPojo1();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		pojo.setPhoneNumber("32735726");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba,St Martin Street,32735726";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_quando_os_campos_possuem_indices_complementares_iniciando_em_qualquer_indice() throws FFPojoException {
		TestPojo2 pojo = new TestPojo2();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		pojo.setPhoneNumber("32735726");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = ",,Giba,St Martin Street,32735726";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_quando_os_campos_possuem_indices_nao_complementares_iniciando_no_primeiro_indice() throws FFPojoException {
		TestPojo3 pojo = new TestPojo3();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		pojo.setPhoneNumber("32735726");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba,,,St Martin Street,,32735726";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_quando_os_campos_possuem_indices_nao_complementares_iniciando_em_qualquer_indice() throws FFPojoException {
		TestPojo4 pojo = new TestPojo4();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		pojo.setPhoneNumber("32735726");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = ",,Giba,,,St Martin Street,,32735726";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_considerando_delimitador_simples() throws FFPojoException {
		TestPojo5 pojo = new TestPojo5();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba#St Martin Street";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_considerando_delimitador_composto() throws FFPojoException {
		TestPojo6 pojo = new TestPojo6();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba#@#St Martin Street";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_considerando_delimitador_simples_sendo_regex_metacaractere() throws FFPojoException {
		TestPojo7 pojo = new TestPojo7();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba$St Martin Street";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_considerando_delimitador_composto_sendo_regex_metacaractere() throws FFPojoException {
		TestPojo8 pojo = new TestPojo8();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba{$}St Martin Street";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_considerando_delimitador_como_caractere_de_espaco_simples() throws FFPojoException {
		TestPojo9 pojo = new TestPojo9();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba St Martin Street";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_considerando_delimitador_como_caractere_de_espaco_composto() throws FFPojoException {
		TestPojo10 pojo = new TestPojo10();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba     St Martin Street";
		Assert.assertEquals(expected, actual);
	}
	
	@DelimitedRecord
	public static final class TestPojo1 {
		private String name;
		private String address;
		private String phoneNumber;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 1)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 2)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
		@DelimitedField(positionIndex = 3)
		public String getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	}
	
	@DelimitedRecord
	public static final class TestPojo2 {
		private String name;
		private String address;
		private String phoneNumber;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 3)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 4)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
		@DelimitedField(positionIndex = 5)
		public String getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	}
	
	@DelimitedRecord
	public static final class TestPojo3 {
		private String name;
		private String address;
		private String phoneNumber;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 1)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 4)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
		@DelimitedField(positionIndex = 6)
		public String getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	}
	
	@DelimitedRecord
	public static final class TestPojo4 {
		private String name;
		private String address;
		private String phoneNumber;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 3)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 6)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
		@DelimitedField(positionIndex = 8)
		public String getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	}
	
	@DelimitedRecord(delimiter = "#")
	public static final class TestPojo5 {
		private String name;
		private String address;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 1)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 2)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
	}
	
	@DelimitedRecord(delimiter = "#@#")
	public static final class TestPojo6 {
		private String name;
		private String address;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 1)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 2)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
	}
	
	@DelimitedRecord(delimiter = "$")
	public static final class TestPojo7 {
		private String name;
		private String address;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 1)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 2)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
	}
	
	@DelimitedRecord(delimiter = "{$}")
	public static final class TestPojo8 {
		private String name;
		private String address;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 1)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 2)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
	}
	
	@DelimitedRecord(delimiter = " ")
	public static final class TestPojo9 {
		private String name;
		private String address;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 1)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 2)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
	}
	
	@DelimitedRecord(delimiter = "     ")
	public static final class TestPojo10 {
		private String name;
		private String address;
		// GETTERS AND SETTERS
		@DelimitedField(positionIndex = 1)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@DelimitedField(positionIndex = 2)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
	}
}
