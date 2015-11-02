package com.github.ffpojo.metadata.positional.annotation.extra;

import com.github.ffpojo.metadata.positional.PaddingAlign;
import com.github.ffpojo.metadata.positional.annotation.EnumerationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by William on 02/11/15.
 * @author William Miranda -  blackstile@hotmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface EnumPositionalField {
    int initialPosition();
    int finalPosition();
    EnumerationType enumType() default EnumerationType.STRING;
    Class<? extends  Enum> enumClass();
    PaddingAlign paddingAlign() default PaddingAlign.RIGHT;
    char paddingCharacter() default ' ';
    boolean trimOnRead() default true;

}
