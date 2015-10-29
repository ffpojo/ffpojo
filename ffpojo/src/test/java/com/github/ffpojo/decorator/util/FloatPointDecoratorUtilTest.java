package com.github.ffpojo.decorator.util;

import java.math.BigDecimal;

import org.junit.Test;
import com.google.common.truth.*;


public class FloatPointDecoratorUtilTest {
	
	private FloatPointDecoratorUtil floatPointDecoratorUtil =  new FloatPointDecoratorUtil();
	
	/**
	 * NULL VERIFICATION
	 */
	@Test
	public void testConverterNullCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(0);
		Truth.assert_().that(floatPointDecoratorUtil.toString(null)).isEqualTo("");
	}

	/**
	 * BIG DECIMAL VERIFICATION
	 */
	@Test
	public void testConveterStringToBigDecimalDuasCasasDecimais() {
		floatPointDecoratorUtil.setPrecicion(2);
		Truth.assert_().that(floatPointDecoratorUtil.fromString("12021")).isEqualTo(BigDecimal.valueOf(120.21));
	}
	
	@Test
	public void testConveterStringBigDecimalUmaCasasDecimais() {
		floatPointDecoratorUtil.setPrecicion(1);
		Truth.assert_().that(floatPointDecoratorUtil.fromString("12001")).isEqualTo(BigDecimal.valueOf(1200.1));
	}
	
	@Test
	public void testConveterStringBigDecimalZeroCasasDecimais() {
		floatPointDecoratorUtil.setPrecicion(0);
		Truth.assert_().that(floatPointDecoratorUtil.fromString("12001")).isEqualTo(BigDecimal.valueOf(12001));
	}
	
	@Test
	public void testConverterBigDecimalComDuasCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(2);
		Truth.assert_().that(floatPointDecoratorUtil.toString(BigDecimal.valueOf(120.21))).isEqualTo("12021");
	}
	
	@Test
	public void testConverterBigDecimalComCincoCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(5);
		Truth.assert_().that(floatPointDecoratorUtil.toString(BigDecimal.valueOf(120.21345))).isEqualTo("12021345");
	}
	
	@Test
	public void testConverterBigDecimalComUmaCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(1);
		Truth.assert_().that(floatPointDecoratorUtil.toString(BigDecimal.valueOf(120.1))).isEqualTo("1201");
	}
	
	@Test
	public void testConverterBigDecimalComZeroCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(0);
		Truth.assert_().that(floatPointDecoratorUtil.toString(BigDecimal.valueOf(1201))).isEqualTo("1201");
	}

	
	/**
	 * DOUBLE VERIFICATION 
	 **/
	@Test
	public void testConveterStringToDoubleDuasCasasDecimais() {
		floatPointDecoratorUtil.setPrecicion(2);
		Truth.assert_().that(floatPointDecoratorUtil.doubleFromString("12021")).isWithin(0).of(120.21);
	}
	
	@Test
	public void testConveterStringDoubleUmaCasasDecimais() {
		floatPointDecoratorUtil.setPrecicion(1);
		Truth.assert_().that(floatPointDecoratorUtil.doubleFromString("12001")).isWithin(0).of(1200.1);
	}
	
	@Test
	public void testConveterStringDoubleZeroCasasDecimais() {
		floatPointDecoratorUtil.setPrecicion(0);
		Truth.assert_().that(floatPointDecoratorUtil.doubleFromString("12001")).isWithin(0).of(12001d);
	}
	
	
	@Test
	public void testConverterDoubleComDuasCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(2);
		Truth.assert_().that(floatPointDecoratorUtil.toString(120.21d)).isEqualTo("12021");
	}
	
	@Test
	public void testConverterDoubleComCincoCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(5);
		Truth.assert_().that(floatPointDecoratorUtil.toString(120.21345d)).isEqualTo("12021345");
	}
	
	@Test
	public void testConverterDoubleComUmaCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(1);
		Truth.assert_().that(floatPointDecoratorUtil.toString(120.1d)).isEqualTo("1201");
	}
	
	@Test
	public void testConverterDoubleComZeroCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(0);
		Truth.assert_().that(floatPointDecoratorUtil.toString(1201d)).isEqualTo("1201");
	}
	
	
	/**
	 * FLOAT VERIFICATION 
	 **/
	@Test
	public void testConveterStringToFloatDuasCasasDecimais() {
		floatPointDecoratorUtil.setPrecicion(2);
		Truth.assert_().that(floatPointDecoratorUtil.floatFromString("12021")).isEqualTo(Float.valueOf("120.21"));
	}
	
	@Test
	public void testConveterStringFloatUmaCasasDecimais() {
		floatPointDecoratorUtil.setPrecicion(1);
		Truth.assert_().that(floatPointDecoratorUtil.floatFromString("12001")).isEqualTo(Float.valueOf("1200.1"));
	}
	
	@Test
	public void testConveterStringFloatZeroCasasDecimais() {
		floatPointDecoratorUtil.setPrecicion(0);
		Truth.assert_().that(floatPointDecoratorUtil.floatFromString("12001")).isEqualTo(Float.valueOf("12001"));
	}
	
	
	@Test
	public void testConverterFloatComDuasCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(2);
		Truth.assert_().that(floatPointDecoratorUtil.toString(Float.valueOf("120.21"))).isEqualTo("12021");
	}
	
	@Test
	public void testConverterFloatComCincoCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(5);
		Truth.assert_().that(floatPointDecoratorUtil.toString(Float.valueOf("120.21345"))).isEqualTo("12021345");
	}
	
	@Test
	public void testConverterFloatComUmaCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(1);
		Truth.assert_().that(floatPointDecoratorUtil.toString(Float.valueOf("120.1"))).isEqualTo("1201");
	}
	
	@Test
	public void testConverterFloatComZeroCasasDecimais(){
		floatPointDecoratorUtil.setPrecicion(0);
		Truth.assert_().that(floatPointDecoratorUtil.toString(Float.valueOf("1201"))).isEqualTo("1201");
 	}

}
