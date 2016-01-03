package com.github.ffpojo.metadata.positional;

import com.github.ffpojo.metadata.FullLineField;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.PositionalFieldRemainder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by William on 02/01/16.
 */
public class PositionalFieldDescriptorTest {

    @Test
    public void testCompareToFullLineField() throws Exception {




    }
}

class Mock {
    @FullLineField
    private String fieldOne;
    @PositionalFieldRemainder
    private String fieldTwo;
    @PositionalField(initialPosition = 1, finalPosition = 2)
    private String fieldThree;
    @PositionalField(initialPosition = 3, finalPosition = 4)
    private String fieldFour;
    @PositionalField(initialPosition = 4, finalPosition = 5)
    private String fieldFive;
}