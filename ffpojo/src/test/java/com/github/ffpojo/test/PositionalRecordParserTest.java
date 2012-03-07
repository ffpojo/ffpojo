package com.github.ffpojo.test;

import junit.framework.Assert;

import org.junit.Test;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.metadata.positional.PaddingAlign;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

public class PositionalRecordParserTest {

	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_quando_os_campos_possuem_posicoes_complementares_iniciando_na_primeira_posicao() throws FFPojoException {
		TestPojo1 pojo = new TestPojo1();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		pojo.setPhoneNumber("32735726");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba      St Martin Street    32735726  ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_quando_os_campos_possuem_posicoes_complementares_iniciando_em_qualquer_posicao() throws FFPojoException {
		TestPojo2 pojo = new TestPojo2();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		pojo.setPhoneNumber("32735726");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "          Giba      St Martin Street    32735726  ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_quando_os_campos_possuem_posicoes_nao_complementares_iniciando_na_primeira_posicao() throws FFPojoException {
		TestPojo3 pojo = new TestPojo3();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		pojo.setPhoneNumber("32735726");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba                St Martin Street    32735726  ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_quando_os_campos_possuem_posicoes_nao_complementares_iniciando_em_qualquer_posicao() throws FFPojoException {
		TestPojo4 pojo = new TestPojo4();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		pojo.setPhoneNumber("32735726");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "          Giba                St Martin Street    32735726  ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_considerando_padding_direita_e_padding_caractere() throws FFPojoException {
		TestPojo5 pojo = new TestPojo5();
		pojo.setName("Giba");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "Giba################";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_pojo_em_string_de_acordo_com_anotacoes_considerando_padding_esquerda_e_padding_caractere() throws FFPojoException {
		TestPojo6 pojo = new TestPojo6();
		pojo.setName("Giba");
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "################Giba";
		Assert.assertEquals(expected, actual);
	}
	
	@PositionalRecord
	public static final class TestPojo1 {
		private String name;
		private String address;
		private String phoneNumber;
		// GETTERS AND SETTERS
		@PositionalField(initialPosition = 1, finalPosition = 10)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@PositionalField(initialPosition = 11, finalPosition = 30)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
		@PositionalField(initialPosition = 31, finalPosition = 40)
		public String getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	}
	
	@PositionalRecord
	public static final class TestPojo2 {
		private String name;
		private String address;
		private String phoneNumber;
		// GETTERS AND SETTERS
		@PositionalField(initialPosition = 11, finalPosition = 20)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@PositionalField(initialPosition = 21, finalPosition = 40)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
		@PositionalField(initialPosition = 41, finalPosition = 50)
		public String getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	}
	
	@PositionalRecord
	public static final class TestPojo3 {
		private String name;
		private String address;
		private String phoneNumber;
		// GETTERS AND SETTERS
		@PositionalField(initialPosition = 1, finalPosition = 10)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@PositionalField(initialPosition = 21, finalPosition = 40)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
		@PositionalField(initialPosition = 41, finalPosition = 50)
		public String getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	}
	
	@PositionalRecord
	public static final class TestPojo4 {
		private String name;
		private String address;
		private String phoneNumber;
		// GETTERS AND SETTERS
		@PositionalField(initialPosition = 11, finalPosition = 20)
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		@PositionalField(initialPosition = 31, finalPosition = 50)
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
		@PositionalField(initialPosition = 51, finalPosition = 60)
		public String getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	}
	
	@PositionalRecord
	public static final class TestPojo5 {
		private String name;
		// GETTERS AND SETTERS
		@PositionalField(initialPosition = 1, finalPosition = 20, paddingAlign = PaddingAlign.RIGHT, paddingCharacter = '#')
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
	}
	
	@PositionalRecord
	public static final class TestPojo6 {
		private String name;
		// GETTERS AND SETTERS
		@PositionalField(initialPosition = 1, finalPosition = 20, paddingAlign = PaddingAlign.LEFT, paddingCharacter = '#')
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
	}
}
