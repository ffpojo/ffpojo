package com.github.ffpojo.util;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static List<MappedByteBuffer> mapFileUsingMappedByteBuffers(FileChannel channel, MapMode mode, int pageSize) throws IOException {
		List<MappedByteBuffer> buffers = new ArrayList<MappedByteBuffer>();
		long channelSize = channel.size();
		long mappingStart = 0;
		int mappingSize = 0;
		for (long i = 0; mappingStart + mappingSize < channelSize; i++) {
			if ((channelSize / pageSize) == i) {
				mappingSize = (int) (channelSize - i * pageSize);
			} else {
				mappingSize = pageSize;
			}
			mappingStart = i * pageSize;
			MappedByteBuffer pagina = channel.map(mode, mappingStart, mappingSize);
			buffers.add(pagina);
		}
		return buffers;
	}

}
