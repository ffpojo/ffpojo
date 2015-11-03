package com.github.ffpojo.decorator.util;

import com.github.ffpojo.metadata.positional.annotation.extra.DatePositionalField;
import org.junit.Test;

import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.extra.DoublePositionalField;
import com.google.common.truth.Truth;

public class CollectionDecoratorUtilTest {

	@Test
	public void testObjectLineSizeFromProperty() {
		int lineSize =  new CollectionDecoratorUtil(TestAnnotation.class).objectLineSize();
		Truth.assert_().that(lineSize).isEqualTo(40);
	}
	
	@Test
	public void testObjectLineSizeFromField() {
		int lineSize =  new CollectionDecoratorUtil(TestAccessorType.class).objectLineSize();
		Truth.assert_().that(lineSize).isEqualTo(40);
	}
	@Test
	public void testObjectChildLineSizeFromField() {
		int lineSize =  new CollectionDecoratorUtil(TestAccessorTypeChild.class).objectLineSize();
		Truth.assert_().that(lineSize).isEqualTo(40);
	}
	
	@Test
	public void testObjectChildMixLineSizeFromField() {
		int lineSize =  new CollectionDecoratorUtil(TestAccessorTypeChildMixFieldAndProperty.class).objectLineSize();
		Truth.assert_().that(lineSize).isEqualTo(40);
	}


}

@PositionalRecord
class TestAccessorType{
	
	@PositionalField(initialPosition=1, finalPosition=10)
	private String name;
	@PositionalField(initialPosition=11, finalPosition=20)
	private String email;
	@PositionalField(initialPosition=21, finalPosition=30)
	private String lastName;
	@PositionalField(initialPosition=31, finalPosition=40)
	private String address;
	
}

@PositionalRecord
class TestAccessorTypeChild extends TestAccessorType{
	@PositionalField(initialPosition=-1, finalPosition=0)
	private String xpto;
}

@PositionalRecord
class TestAccessorTypeChildMixFieldAndProperty extends TestAnnotation{
	@PositionalField(initialPosition=-1, finalPosition=0)
	private String xpto;
}

@PositionalRecord
class TestAnnotation{
	
	private String nome;
	private String email;
	private String lastName;
	
	@PositionalField(initialPosition=1, finalPosition=10)
	public String getNome() {
		return nome;
	}
	@PositionalField(initialPosition=11, finalPosition=20)
	public String getEmail() {
		return email;
	}
	@DoublePositionalField(initialPosition=21, finalPosition=30)
	public String getLastName() {
		return lastName;
	}
	@DatePositionalField(initialPosition=31, finalPosition=40, dateFormat="")
	public String getAddress() {
		return lastName;
	}
	
}