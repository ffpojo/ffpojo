package com.github.ffpojo.decorator;

import com.github.ffpojo.metadata.DefaultFieldDecorator;

public class LongDecorator extends DefaultFieldDecorator {
	@Override
	public Object fromString(String str) {
		return Long.valueOf(str);
	}
}
