package com.github.ffpojo.metadata.extra;

import com.github.ffpojo.decorator.*;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.metadata.DefaultFieldDecorator;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.delimited.annotation.extra.*;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.extra.*;
import com.google.common.truth.Truth;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by William on 30/10/15.
 */

public class FFPojoAnnotationFieldManagerTest {

    final FFPojoAnnotationFieldManager annotationFieldManager =  new FFPojoAnnotationFieldManager();
    IntegerPositionalField integerPositionalField;
    BooleanDelimitedField booleanDelimitedField;
    PositionalField positionalField;


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        integerPositionalField = Mock.class.getDeclaredMethod("getTestIntegerPositionalField").getDeclaredAnnotation(IntegerPositionalField.class);
        positionalField = Mock.class.getDeclaredMethod("getTestPositionalField").getDeclaredAnnotation(PositionalField.class);
        booleanDelimitedField = Mock.class.getDeclaredMethod("getTestDelimitedField").getDeclaredAnnotation(BooleanDelimitedField.class);
    }

    @Test
    public void shouldToReturnDateDecoratorWhenDateDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(DateDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalDateDecorator.class);
    }

    @Test
    public void shouldToReturnDateDecoratorWhenDatePositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(DatePositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalDateDecorator.class);
    }

    @Test
    public void shouldToReturnLongDecoratorWhenLongDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(LongDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalLongDecorator.class);
    }

    @Test
    public void shouldToReturnLongDecoratorWhenLongPositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(LongPositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalLongDecorator.class);
    }

    @Test
    public void shouldToReturnBigIntegerDecoratorWhenBigIntegerDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(BigIntegerDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalBigIntegerDecorator.class);
    }

    @Test
    public void shouldToReturnBigIntegerDecoratorWhenBigIntegerPositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(BigIntegerPositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalBigIntegerDecorator.class);
    }

    @Test
    public void shouldToReturnIntegerDecoratorWhenIntegerDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(IntegerDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalIntegerDecorator.class);
    }

    @Test
    public void shouldToReturnIntegerDecoratorWhenIntegerPositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(IntegerPositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalIntegerDecorator.class);
    }

    @Test
    public void shouldToReturnBigDecimalDecoratorWhenBigDecimalDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(BigDecimalDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalBigDecimalDecorator.class);
    }

    @Test
    public void shouldToReturnBigDecimalDecoratorWhenBigDecimalPositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(BigDecimalPositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalBigDecimalDecorator.class);
    }


    @Test
    public void shouldToReturnDoubleDecoratorWhenDoubleDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(DoubleDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalDoubleDecorator.class);
    }

    @Test
    public void shouldToReturnDoubleDecoratorWhenDoublePositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(DoublePositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalDoubleDecorator.class);
    }

    @Test
    public void shouldToReturnFloatDecoratorWhenFloatDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(FloatDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalFloatDecorator.class);
    }

    @Test
    public void shouldToReturnFloatDecoratorWhenFloatPositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(FloatPositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalFloatDecorator.class);
    }

    @Test
    public void shouldToReturnListDecoratorWhenListDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(ListDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalListDecorator.class);
    }

    @Test
    public void shouldToReturnListDecoratorWhenListPositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(ListPositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalListDecorator.class);
    }

    @Test
    public void shouldToReturnSetDecoratorWhenSetDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(SetDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalSetDecorator.class);
    }

    @Test
    public void shouldToReturnSetDecoratorWhenSetPositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(SetPositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalSetDecorator.class);
    }

    @Test
    public void shouldToReturnBooleanDecoratorWhenBooleanDelimitedField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(BooleanDelimitedField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalBooleanDecorator.class);
    }

    @Test
    public void shouldToReturnBooleanDecoratorWhenBooleanPositionalField(){
        Class<?> decorator =  annotationFieldManager.getClassDecorator(BooleanPositionalField.class);
        Truth.assert_().that(decorator).isEqualTo(InternalBooleanDecorator.class);
    }

    @Test
    public void shouldToReturnTrueIfDelimitedFieldAnnotation(){
        final boolean delimitedField = annotationFieldManager.isDelimitedField(BigDecimalDelimitedField.class);
        Truth.assert_().that(delimitedField).isTrue();
    }

    @Test
    public void shouldToReturnFalseIfIsNotDelimitedFieldAnnotation(){
        final boolean delimitedField = annotationFieldManager.isDelimitedField(BigDecimalPositionalField.class);
        Truth.assert_().that(delimitedField).isFalse();
    }

    @Test
    public void shouldToReturnTrueIfPositionalFieldAnnotation(){
        final boolean positionalField = annotationFieldManager.isPositionalField(BigDecimalPositionalField.class);
        Truth.assert_().that(positionalField).isTrue();
    }

    @Test
    public void shouldToReturnFalseIfIsNotPositionalFieldFieldAnnotation(){
        final boolean positionalField = annotationFieldManager.isPositionalField(BigDecimalDelimitedField.class);
        Truth.assert_().that(positionalField).isFalse();
    }

    @Test
    public void testGetDecoratorInstance() throws Exception {
        final FieldDecorator<?> newInstanceDecorator = annotationFieldManager.createNewInstanceDecorator(positionalField);
        Truth.assert_().that(newInstanceDecorator).isInstanceOf(MockDecorator.class);
    }

    @Test
    public void testGetDecoratorInstanceInternalIntegerDecorator() throws Exception {
        final FieldDecorator<?> newInstanceDecorator = annotationFieldManager.createNewInstanceDecorator(integerPositionalField);
        Truth.assert_().that(newInstanceDecorator).isInstanceOf(InternalIntegerDecorator.class);
    }

    @Test
    public void testGetDecoratorInstanceInternalBooleanDecorator() throws Exception {
        final FieldDecorator<?> newInstanceDecorator = annotationFieldManager.createNewInstanceDecorator(booleanDelimitedField);
        Truth.assert_().that(newInstanceDecorator).isInstanceOf(InternalBooleanDecorator.class);
    }

    @Test
    public void testIsFieldAnnotedWithFFPojoAnnotation() throws Exception {
        final boolean isAnnotatedFieldIntMock = annotationFieldManager.isFieldAnnotedWithFFPojoAnnotation(Mock.class.getField("intMock"));
        Truth.assert_().that(isAnnotatedFieldIntMock).isTrue();


    }

    @Test
    public void shouldToThrowFFPojoExceptionWhenNotFFPojoAnnotationFieldClass(){
        expectedException.expectMessage(String.format("The class %s not seem a DelimitedField or PositionalField annotation.", PositionalRecord.class));
        expectedException.expect(FFPojoException.class);
        annotationFieldManager.getClassDecorator(PositionalRecord.class);
    }
}
class Mock{
    @PositionalField(initialPosition = 0, finalPosition = 1)
    public int intMock;

    @IntegerPositionalField(initialPosition = 0, finalPosition = 1)
    public int getTestIntegerPositionalField() {
        return 0;
    }

    @BooleanDelimitedField(positionIndex = 0)
    public boolean getTestDelimitedField() {
        return true;
    }

    @PositionalField(initialPosition = 0, finalPosition = 1, decorator = MockDecorator.class)
    public boolean getTestPositionalField(){
        return false;
    }
}
class MockDecorator extends DefaultFieldDecorator{

}