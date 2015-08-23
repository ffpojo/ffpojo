package com.github.ffpojo.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.metadata.positional.PaddingAlign;
import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.metadata.positional.annotation.FFPojoAccessorType;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.extra.DatePositionalFiled;
import com.github.ffpojo.metadata.positional.annotation.extra.DoublePositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.IntegerPositionalField;
import com.google.common.truth.Truth;

import junit.framework.Assert;

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
		pojo.setSalario(12.345);
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		String actual = ffpojo.parseToText(pojo);
		
		String expected = "12345     Giba                St Martin Street    32735726          ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deve_transformar_string_em_pojo_de_acordo_com_anotacoes_quando_os_campos_possuem_posicoes_nao_complementares_iniciando_em_qualquer_posicao() throws FFPojoException, ParseException {
		String expected = "12345     Giba                St Martin Street    32735726  08071980";
		TestPojo4 pojo = new TestPojo4();
		pojo.setName("Giba");
		pojo.setAddress("St Martin Street");
		pojo.setPhoneNumber("32735726");
		pojo.setSalario(12.345);
		pojo.setDataDeNascimento(new SimpleDateFormat("dd/MM/yyyy").parse("08/07/1980"));
		
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		TestPojo4 actual = ffpojo.createFromText(TestPojo4.class, expected);
		
		Assert.assertEquals(pojo, actual);
		Truth.assert_().that(actual.getDataDeNascimento()).isEqualTo(pojo.getDataDeNascimento());
	}
	
	@Test
	public void deveTestarIgnorePositionNotFound() throws FFPojoException {
		String expected = "William   35  St Martin Street";
		FFPojoHelper ffpojo = FFPojoHelper.getInstance();
		TestPojo8 actual = ffpojo.createFromText(TestPojo8.class, expected);
		Truth.assert_().that(actual.getName()).isEqualTo("William");
		Truth.assert_().that(actual.getIdade()).isEqualTo(35);
		Truth.assert_().that(actual.getOpcionalDescricao()).isEqualTo("St Martin Street");
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
		private Double salario;
		private Date dataDeNascimento;
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
		@DoublePositionalField(initialPosition=1, finalPosition=10, precision=3)
		public Double getSalario(){ return this.salario; }
		public void setSalario(Double salario){ this.salario =  salario; }
		
		@DatePositionalFiled(initialPosition=61, finalPosition=68, dateFormat="ddMMyyyy")
		public Date getDataDeNascimento() { return dataDeNascimento; }
		public void setDataDeNascimento(Date dataDeNascimento) { this.dataDeNascimento = dataDeNascimento; }
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((address == null) ? 0 : address.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
			result = prime * result + ((salario == null) ? 0 : salario.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestPojo4 other = (TestPojo4) obj;
			if (address == null) {
				if (other.address != null)
					return false;
			} else if (!address.equals(other.address))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (phoneNumber == null) {
				if (other.phoneNumber != null)
					return false;
			} else if (!phoneNumber.equals(other.phoneNumber))
				return false;
			if (salario == null) {
				if (other.salario != null)
					return false;
			} else if (!salario.equals(other.salario))
				return false;
			return true;
		}
		
		
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
	
	@PositionalRecord(ignorePositionNotFound=true)
	@FFPojoAccessorType(accessorType=AccessorType.FIELD)
	public static final class TestPojo8 {
		@PositionalField(initialPosition = 1, finalPosition = 10, paddingAlign = PaddingAlign.LEFT, paddingCharacter = '#')
		private String name;
		@IntegerPositionalField(initialPosition=11, finalPosition=14)
		private int idade;
		@PositionalField(initialPosition=15, finalPosition=3000)
		private String opcionalDescricao;
		
		// GETTERS AND SETTERS
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public int getIdade() {
			return idade;
		}
		public void setIdade(int idade) {
			this.idade = idade;
		}
		public String getOpcionalDescricao() {
			return opcionalDescricao;
		}
		public void setOpcionalDescricao(String opcionalDescricao) {
			this.opcionalDescricao = opcionalDescricao;
		}
		
		
	}
}
