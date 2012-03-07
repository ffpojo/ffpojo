package com.github.ffpojo.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.ffpojo.exception.InvalidMetadataException;
import com.github.ffpojo.metadata.DefaultFieldDecorator;
import com.github.ffpojo.metadata.delimited.DelimitedFieldDescriptor;
import com.github.ffpojo.metadata.delimited.DelimitedRecordDescriptor;

public class DelimitedRecordDescriptorTest {

	@Test
	public void deve_validar_quando_classe_do_registro_for_nula() throws InvalidMetadataException {
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor();
		descriptor.setDelimiter(",");
		descriptor.setRecordClazz(null);
		descriptor.setFieldDescriptors(new ArrayList<DelimitedFieldDescriptor>());
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_lista_de_campos_for_nula() throws InvalidMetadataException {
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor();
		descriptor.setDelimiter(",");
		descriptor.setRecordClazz(TestPojo.class);
		descriptor.setFieldDescriptors(null);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_delimitador_for_nulo() throws InvalidMetadataException {
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor();
		descriptor.setDelimiter(null);
		descriptor.setRecordClazz(TestPojo.class);
		descriptor.setFieldDescriptors(new ArrayList<DelimitedFieldDescriptor>());
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_delimitador_for_vazio() throws InvalidMetadataException {
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor();
		descriptor.setDelimiter("");
		descriptor.setRecordClazz(TestPojo.class);
		descriptor.setFieldDescriptors(new ArrayList<DelimitedFieldDescriptor>());
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_delimitador_for_espaco() throws InvalidMetadataException {
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor();
		descriptor.setDelimiter(" ");
		descriptor.setRecordClazz(TestPojo.class);
		descriptor.setFieldDescriptors(new ArrayList<DelimitedFieldDescriptor>());
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_lista_de_campos_for_vazia() throws InvalidMetadataException {
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor();
		descriptor.setRecordClazz(TestPojo.class);
		descriptor.setDelimiter(",");
		descriptor.setFieldDescriptors(new ArrayList<DelimitedFieldDescriptor>());
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_um_campo_possui_indice_menor_que_zero() throws InvalidMetadataException {	
		List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
		fd.setGetter(TestPojo.class.getMethods()[0]);
		fd.setDecorator(new DefaultFieldDecorator());
		fd.setPositionIndex(-1);
		fieldDescriptors.add(fd);
		
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor(TestPojo.class, fieldDescriptors, ",");
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_um_campo_possui_indice_igual_a_zero() throws InvalidMetadataException {	
		List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
		fd.setGetter(TestPojo.class.getMethods()[0]);
		fd.setDecorator(new DefaultFieldDecorator());
		fd.setPositionIndex(0);
		fieldDescriptors.add(fd);
		
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor(TestPojo.class, fieldDescriptors, ",");
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_ordenar_os_campos_levando_em_conta_posicao_inicial_e_final() throws InvalidMetadataException {	
		List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		
		DelimitedFieldDescriptor fd1 = new DelimitedFieldDescriptor();
		fd1.setGetter(TestPojo.class.getMethods()[0]);
		fd1.setDecorator(new DefaultFieldDecorator());
		fd1.setPositionIndex(5);
		fieldDescriptors.add(fd1);

		DelimitedFieldDescriptor fd2 = new DelimitedFieldDescriptor();
		fd2.setGetter(TestPojo.class.getMethods()[0]);
		fd2.setDecorator(new DefaultFieldDecorator());
		fd2.setPositionIndex(2);
		fieldDescriptors.add(fd2);
		
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor(TestPojo.class, fieldDescriptors, ",");
		descriptor.sortFieldDescriptors();

		DelimitedFieldDescriptor[] expected = {fd2, fd1};
		DelimitedFieldDescriptor[] actual = descriptor.getFieldDescriptors().toArray(new DelimitedFieldDescriptor[descriptor.getFieldDescriptors().size()]);
		Assert.assertArrayEquals(expected, actual);
	}
	
	@Test
	public void deve_validar_quando_os_campos_possuem_indices_complementares_iniciando_na_primeira_posicao() throws InvalidMetadataException {	
		List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(1);
			fieldDescriptors.add(fd);
		}
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(2);
			fieldDescriptors.add(fd);
		}
		
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor(TestPojo.class, fieldDescriptors, ",");
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_os_campos_possuem_indices_complementares_iniciando_em_qualquer_posicao() throws InvalidMetadataException {	
		List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(5);
			fieldDescriptors.add(fd);
		}
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(6);
			fieldDescriptors.add(fd);
		}
		
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor(TestPojo.class, fieldDescriptors, ",");
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_os_campos_possuem_indices_nao_complementares_iniciando_na_primeira_posicao() throws InvalidMetadataException {	
		List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(1);
			fieldDescriptors.add(fd);
		}
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(6);
			fieldDescriptors.add(fd);
		}
		
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor(TestPojo.class, fieldDescriptors, ",");
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_os_campos_possuem_indices_nao_complementares_iniciando_em_qualquer_posicao() throws InvalidMetadataException {	
		List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(3);
			fieldDescriptors.add(fd);
		}
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(6);
			fieldDescriptors.add(fd);
		}
		
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor(TestPojo.class, fieldDescriptors, ",");
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_dois_campos_ou_mais_possuem_indices_identicos() throws InvalidMetadataException {	
		List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(3);
			fieldDescriptors.add(fd);
		}
		{
			DelimitedFieldDescriptor fd = new DelimitedFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setPositionIndex(3);
			fieldDescriptors.add(fd);
		}
		
		DelimitedRecordDescriptor descriptor = new DelimitedRecordDescriptor(TestPojo.class, fieldDescriptors, ",");
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@SuppressWarnings("unused")
	private static final class TestPojo {
		private String name;
		private String address;
		private Long phoneNumber;
		// GETTERS AND SETTERS
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public String getAddress() { return address; }
		public void setAddress(String address) { this.address = address; }
		public Long getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(Long phoneNumber) { this.phoneNumber = phoneNumber; }
	}
	
}
