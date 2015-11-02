package com.github.ffpojo.metadata.positional.annotation;

/**
 * Created by William on 02/11/15.
 */
public enum EnumerationType {
    STRING, ORDINAL;

    public boolean isString(){
        return EnumerationType.STRING.equals(this);
    }

    public boolean isOrdinal(){
        return EnumerationType.ORDINAL.equals(this);
    }
}
