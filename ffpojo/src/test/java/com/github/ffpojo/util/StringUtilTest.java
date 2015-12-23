package com.github.ffpojo.util;

import com.google.common.truth.Truth;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by William on 23/12/15.
 */
public class StringUtilTest {

    @Test
    public void testFillToLengthLeftDirection() throws Exception {
        final String initial = "1000";
        final int finalSize = 20;
        final char charToFill = '0';
        final String result = StringUtil.fillToLength(initial, finalSize, charToFill, StringUtil.Direction.LEFT);
        final String expected = "00000000000000001000";
        Truth.assert_().that(result).isEqualTo(expected);
        Truth.assert_().that(result.length()).isEqualTo(finalSize);
    }

    @Test
    public void testFillToLengthRightDirection() throws Exception {
        final String initial = "william";
        final int finalSize = 20;
        final char charToFill = ' ';
        final String result = StringUtil.fillToLength(initial, finalSize, charToFill, StringUtil.Direction.RIGHT);
        final String expected = "william             ";
        Truth.assert_().that(result).isEqualTo(expected);
        Truth.assert_().that(result.length()).isEqualTo(finalSize);
    }


    @Test
    public void testFillToLengthZeroLength() throws Exception {
        final String initial = "william";
        final int finalSize = 0;
        final char charToFill = ' ';
        final String result = StringUtil.fillToLength(initial, finalSize, charToFill, StringUtil.Direction.RIGHT);
        final String expected = initial;
        Truth.assert_().that(result).isEqualTo(expected);
        Truth.assert_().that(result.length()).isEqualTo(expected.length());
    }

    @Test
    public void testFillToLengthTextLengthMoreThanParamLength() throws Exception {
        final String initial = "william";
        final int finalSize = 4;
        final char charToFill = ' ';
        final String result = StringUtil.fillToLength(initial, finalSize, charToFill, StringUtil.Direction.RIGHT);
        final String expected = "will";
        Truth.assert_().that(result).isEqualTo(expected);
        Truth.assert_().that(result.length()).isEqualTo(expected.length());
    }

    @Test
    public void testIsNullOrEmptyTestingNullValue() throws Exception {
        final String nullValue =  null;
        final boolean result = StringUtil.isNullOrEmpty(nullValue);
        Truth.assert_().that(result).isTrue();
    }


    @Test
    public void testIsNullOrEmptyTestingEmptyValue() throws Exception {
        final String emptyValue =  StringUtil.EMPTY;
        final boolean result = StringUtil.isNullOrEmpty(emptyValue);
        Truth.assert_().that(result).isTrue();
    }


    @Test
    public void testIsNullOrEmptyTestingBlankValue() throws Exception {
        final String blankValue =  "                    ";
        final boolean result = StringUtil.isNullOrEmpty(blankValue);
        Truth.assert_().that(result).isTrue();
    }

    @Test
    public void testNotNull() throws Exception {
        final String blankValue =  "william";
        final boolean result = StringUtil.isNotEmpty(blankValue);
        Truth.assert_().that(result).isTrue();
    }


    @Test
    public void testNotNullWithNullValue() throws Exception {
        final String blankValue =  null;
        final boolean result = StringUtil.isNotEmpty(blankValue);
        Truth.assert_().that(result).isFalse();
    }


    @Test
    public void testPastelCaseToCamelCase() throws Exception {
        final String initial = "StringUtilTest";
        final String expected = "stringUtilTest";
        final String result = StringUtil.pastelCaseToCamelCase(initial);
        Truth.assert_().that(result).isEqualTo(result);


    }

    @Test
    public void testCamelCaseToPastelCase() throws Exception {
        final String initial = "stringUtilTest";
        final String expexcted = "StringUtilTest";
        final String result = StringUtil.camelCaseToPastelCase(initial);
        Truth.assert_().that(result).isEqualTo(result);
    }
}