package com.github.ffpojo.metadata.positional;

import com.github.ffpojo.metadata.FieldDescriptor;
import com.github.ffpojo.metadata.FullLineField;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.PositionalFieldRemainder;
import com.google.common.truth.Truth;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Created by William on 02/01/16.
 */
public class PositionalFieldDescriptorTest {

    @Test
    public void testCompareToFullLineField() throws Exception {
        PositionalFieldDescriptor positionalFieldDescriptor = PositionalFieldDescriptorBuilder.newInstance()
                .initialPosition(1)
                .finalPosition(2)
                .getter(Mock.class.getDeclaredMethod("getName"))
                .build();



        PositionalFieldDescriptor positionalFieldDescriptor2 = PositionalFieldDescriptorBuilder.newInstance()
                .initialPosition(3)
                .finalPosition(4)
                .getter(Mock.class.getDeclaredMethod("getAge"))
                .build();

        positionalFieldDescriptor.setGetter(Mock.class.getDeclaredMethod("getName"));

        PositionalFieldDescriptor positionalFieldDescriptorFull = PositionalFieldDescriptorBuilder.newInstance()
                .isFullLine()
                .build();

        PositionalFieldDescriptor positionalFieldDescriptorRemainder = PositionalFieldDescriptorBuilder.newInstance()
                .isIgnoreMissingFieldsInTheEnd()
                .build();

        Truth.assert_().that(positionalFieldDescriptor.compareTo(positionalFieldDescriptor2)).isLessThan(0);
        Truth.assert_().that(positionalFieldDescriptor2.compareTo(positionalFieldDescriptor)).isGreaterThan(0);
        Truth.assert_().that(positionalFieldDescriptor2.compareTo(positionalFieldDescriptorFull)).isLessThan(0);
        Truth.assert_().that(positionalFieldDescriptorFull.compareTo(positionalFieldDescriptorRemainder)).isGreaterThan(0);
        Truth.assert_().that(positionalFieldDescriptorRemainder.compareTo(positionalFieldDescriptorFull)).isLessThan(0);

        Set<FieldDescriptor> fieldDescriptorSet =  new TreeSet<FieldDescriptor>();
        fieldDescriptorSet.add(positionalFieldDescriptorRemainder);
        fieldDescriptorSet.add(positionalFieldDescriptorFull);
        fieldDescriptorSet.add(positionalFieldDescriptor);
        fieldDescriptorSet.add(positionalFieldDescriptor2);

        final Iterator<FieldDescriptor> iterator = fieldDescriptorSet.iterator();
        Truth.assert_().that(iterator.next()).isEqualTo(positionalFieldDescriptor);
        Truth.assert_().that(iterator.next()).isEqualTo(positionalFieldDescriptor2);
        Truth.assert_().that(iterator.next()).isEqualTo(positionalFieldDescriptorRemainder);
        Truth.assert_().that(iterator.next()).isEqualTo(positionalFieldDescriptorFull);

    }
}

class PositionalFieldDescriptorBuilder{

    final PositionalFieldDescriptor positionalFieldDescriptor =  new PositionalFieldDescriptor();

    public static PositionalFieldDescriptorBuilder newInstance(){
        return new PositionalFieldDescriptorBuilder();
    }

    public PositionalFieldDescriptorBuilder initialPosition(int initValue){
        this.positionalFieldDescriptor.setInitialPosition(initValue);
        return this;
    }

    public PositionalFieldDescriptorBuilder finalPosition(int finalValue){
        this.positionalFieldDescriptor.setFinalPosition(finalValue);
        return this;
    }

    public PositionalFieldDescriptorBuilder isFullLine(){
        this.positionalFieldDescriptor.setIsFullLineField(true);
        return this;
    }

    public PositionalFieldDescriptorBuilder isIgnoreMissingFieldsInTheEnd(){
        this.positionalFieldDescriptor.setIgnoreMissingFieldsInTheEnd(true);
        return this;
    }

    public PositionalFieldDescriptorBuilder getter(Method getter){
        this.positionalFieldDescriptor.setGetter(getter);
        return this;
    }

    public PositionalFieldDescriptor build(){
        return this.positionalFieldDescriptor;
    }

}

class Mock{
    public String getName(){return "";}
    public String getAge(){return "";}
}