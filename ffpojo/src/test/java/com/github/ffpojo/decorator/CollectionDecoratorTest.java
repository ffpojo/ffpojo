package com.github.ffpojo.decorator;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.extra.CollectionPositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.DatePositionalFiled;

public class CollectionDecoratorTest {

	@Test
	public void testToStringCollection() {
		Funcionario funcionario =  new Funcionario();
		funcionario.setNome("William");
		funcionario.setSobreNome("Miranda de Jesus");
		funcionario.setProjetos(criarListProjetos());
	}

	private List<Projeto> criarListProjetos() {
		List<Projeto> projetos =  new ArrayList<Projeto>();
		for(int i=0; i <=100; i++) {
			Projeto projeto =  new Projeto();
			projeto.setDescricao("Projeto " + 1);
			projeto.setDataFinal(new Date());
			projetos.add(projeto);
		}
		return projetos;
	}

	@Test
	public void testFromString() {
		fail("Not yet implemented");
	}

}

@PositionalRecord
class Funcionario{
	@PositionalField(initialPosition=1, finalPosition=10)
	private String nome;
	@PositionalField(initialPosition=11, finalPosition=20)
	private String sobreNome;
	@CollectionPositionalField(itemType=Projeto.class, initialPosition=21, finalPosition=500)
	private List<Projeto> projetos;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobreNome() {
		return sobreNome;
	}
	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}
	public List<Projeto> getProjetos() {
		return projetos;
	}
	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}
	
}

@PositionalRecord
class Projeto{
	@PositionalField(initialPosition=1, finalPosition=10)
	private String descricao;
	@DatePositionalFiled(initialPosition=11, finalPosition=18, dateFormat="ddMMyyyy")
	private Date dataInicio;
	@DatePositionalFiled(initialPosition=19, finalPosition=26, dateFormat="ddMMyyyy")
	private Date dataFinal;
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	
}