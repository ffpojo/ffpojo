package com.github.ffpojo.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.ffpojo.exception.InvalidMetadataException;
import com.github.ffpojo.metadata.DefaultFieldDecorator;
import com.github.ffpojo.metadata.positional.PaddingAlign;
import com.github.ffpojo.metadata.positional.PositionalFieldDescriptor;
import com.github.ffpojo.metadata.positional.PositionalRecordDescriptor;

public class PositionalRecordDescriptorTest {
	
	@Test
	public void deve_validar_quando_classe_do_registro_for_nula() throws InvalidMetadataException {
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor();
		descriptor.setRecordClazz(null);
		descriptor.setFieldDescriptors(new ArrayList<PositionalFieldDescriptor>());
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_lista_de_campos_for_nula() throws InvalidMetadataException {
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor();
		descriptor.setRecordClazz(TestPojo.class);
		descriptor.setFieldDescriptors(null);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_lista_de_campos_for_vazia() throws InvalidMetadataException {
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor();
		descriptor.setRecordClazz(TestPojo.class);
		descriptor.setFieldDescriptors(new ArrayList<PositionalFieldDescriptor>());
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_um_campo_possui_posicao_inicial_menor_que_zero() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
		fd.setGetter(TestPojo.class.getMethods()[0]);
		fd.setPaddingAlign(PaddingAlign.RIGHT);
		fd.setPaddingCharacter(' ');
		fd.setTrimOnRead(true);
		fd.setDecorator(new DefaultFieldDecorator());
		fd.setInitialPosition(-1);
		fd.setFinalPosition(10);
		fieldDescriptors.add(fd);
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_um_campo_possui_posicao_inicial_igual_a_zero() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
		fd.setGetter(TestPojo.class.getMethods()[0]);
		fd.setPaddingAlign(PaddingAlign.RIGHT);
		fd.setPaddingCharacter(' ');
		fd.setTrimOnRead(true);
		fd.setDecorator(new DefaultFieldDecorator());
		fd.setInitialPosition(0);
		fd.setFinalPosition(10);
		fieldDescriptors.add(fd);
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_um_campo_possui_posicao_final_menor_que_posicao_inicial() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
		fd.setGetter(TestPojo.class.getMethods()[0]);
		fd.setPaddingAlign(PaddingAlign.RIGHT);
		fd.setPaddingCharacter(' ');
		fd.setTrimOnRead(true);
		fd.setDecorator(new DefaultFieldDecorator());
		fd.setInitialPosition(10);
		fd.setFinalPosition(5);
		fieldDescriptors.add(fd);
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_um_campo_possui_posicao_final_igual_a_posicao_inicial() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
		fd.setGetter(TestPojo.class.getMethods()[0]);
		fd.setPaddingAlign(PaddingAlign.RIGHT);
		fd.setPaddingCharacter(' ');
		fd.setTrimOnRead(true);
		fd.setDecorator(new DefaultFieldDecorator());
		fd.setInitialPosition(5);
		fd.setFinalPosition(5);
		fieldDescriptors.add(fd);
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_ordenar_os_campos_levando_em_conta_posicao_inicial_e_final() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		PositionalFieldDescriptor fd1 = new PositionalFieldDescriptor();
		fd1.setGetter(TestPojo.class.getMethods()[0]);
		fd1.setPaddingAlign(PaddingAlign.RIGHT);
		fd1.setPaddingCharacter(' ');
		fd1.setTrimOnRead(true);
		fd1.setDecorator(new DefaultFieldDecorator());
		fd1.setInitialPosition(7);
		fd1.setFinalPosition(10);
		fieldDescriptors.add(fd1);

		PositionalFieldDescriptor fd2 = new PositionalFieldDescriptor();
		fd2.setGetter(TestPojo.class.getMethods()[1]);
		fd2.setPaddingAlign(PaddingAlign.RIGHT);
		fd2.setPaddingCharacter(' ');
		fd2.setTrimOnRead(true);
		fd2.setDecorator(new DefaultFieldDecorator());
		fd2.setInitialPosition(3);
		fd2.setFinalPosition(6);
		fieldDescriptors.add(fd2);
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		
		PositionalFieldDescriptor[] expected = {fd2, fd1};
		PositionalFieldDescriptor[] actual = descriptor.getFieldDescriptors().toArray(new PositionalFieldDescriptor[descriptor.getFieldDescriptors().size()]);
		Assert.assertArrayEquals(expected, actual);
	}
	
	@Test
	public void deve_validar_quando_os_campos_possuem_posicoes_complementares_iniciando_na_primeira_posicao() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(1);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(11);
			fd.setFinalPosition(20);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_os_campos_possuem_posicoes_complementares_iniciando_em_qualquer_posicao() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(5);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(11);
			fd.setFinalPosition(20);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_os_campos_possuem_posicoes_nao_complementares_sem_sobreposicao_iniciando_na_primeira_posicao() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(1);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(20);
			fd.setFinalPosition(25);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test
	public void deve_validar_quando_os_campos_possuem_posicoes_nao_complementares_sem_sobreposicao_iniciando_em_qualquer_posicao() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(5);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(20);
			fd.setFinalPosition(25);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_os_campos_possuem_sobreposicao_de_intervalos_totalmente_contida() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(5);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(7);
			fd.setFinalPosition(9);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_os_campos_possuem_sobreposicao_de_intervalos_contida_no_inicio() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(5);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(7);
			fd.setFinalPosition(15);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_os_campos_possuem_sobreposicao_de_intervalos_contida_no_final() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(5);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(1);
			fd.setFinalPosition(6);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_os_campos_possuem_sobreposicao_de_intervalos_coincidindo_campo_inicial() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(5);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(1);
			fd.setFinalPosition(5);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_os_campos_possuem_sobreposicao_de_intervalos_coincidindo_campo_final() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(5);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(10);
			fd.setFinalPosition(15);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
		descriptor.sortFieldDescriptors();
		descriptor.assertValid();
	}
	
	@Test(expected = InvalidMetadataException.class)
	public void nao_deve_validar_quando_dois_campos_ou_mais_possuem_intervalos_identicos() throws InvalidMetadataException {	
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[0]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(5);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		{
			PositionalFieldDescriptor fd = new PositionalFieldDescriptor();
			fd.setGetter(TestPojo.class.getMethods()[1]);
			fd.setPaddingAlign(PaddingAlign.RIGHT);
			fd.setPaddingCharacter(' ');
			fd.setTrimOnRead(true);
			fd.setDecorator(new DefaultFieldDecorator());
			fd.setInitialPosition(5);
			fd.setFinalPosition(10);
			fieldDescriptors.add(fd);
		}
		
		PositionalRecordDescriptor descriptor = new PositionalRecordDescriptor(TestPojo.class, fieldDescriptors);
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
