package com.github.ffpojo.decorator;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

public class CollectionDecoratorTest {

	@Test
	public void testToStringCollection() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromString() {
		fail("Not yet implemented");
	}

}

@PositionalRecord
class Funcionario{
	private String nome;
	private String sobreNome;
	private List<Projeto> projetos;
	
	
}

@PositionalRecord
class Projeto{
	private String descricao;
	private String dataInicio;
	private String dataFinal;
}