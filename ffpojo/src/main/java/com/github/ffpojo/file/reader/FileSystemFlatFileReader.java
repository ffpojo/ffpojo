package com.github.ffpojo.file.reader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.exception.RecordParserException;
import com.github.ffpojo.util.FileUtil;

public class FileSystemFlatFileReader extends BaseFlatFileReader implements FlatFileReader {

	private static final int DEFAULT_LINE_BYTE_BUFFER_SIZE = 2048;
	private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
	private static final int PAGE_SIZE = Integer.MAX_VALUE;
	private static final boolean IS_RESET_SUPPORTED = true;
	
	private List<MappedByteBuffer> buffers;
	private MappedByteBuffer singleBuffer;
	private boolean isSingleBuffer;
	
	private FileChannel channel;
	private ByteBuffer byteBuffer;
	private CharsetDecoder decoder;
	private long fileSize;
	private long position;
	
	public FileSystemFlatFileReader(File file, FlatFileReaderDefinition flatFileDefinition) throws IOException {
		this(file, flatFileDefinition, DEFAULT_CHARSET, DEFAULT_LINE_BYTE_BUFFER_SIZE);
	}
	
	public FileSystemFlatFileReader(File file, FlatFileReaderDefinition flatFileDefinition, int lineByteBufferSize) throws IOException {
		this(file, flatFileDefinition, DEFAULT_CHARSET, lineByteBufferSize);
	}
	
	public FileSystemFlatFileReader(File file, FlatFileReaderDefinition flatFileDefinition, Charset charset) throws IOException {
		this(file, flatFileDefinition, charset, DEFAULT_LINE_BYTE_BUFFER_SIZE);
	}
	
	@SuppressWarnings("resource")
	public FileSystemFlatFileReader(File file, FlatFileReaderDefinition flatFile, Charset charset, int lineByteBufferSize) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("File object is null");
		} else if (!file.exists()) {
			throw new IllegalArgumentException("Specified file does not exists: " + file.getName());
		} else if (!file.isFile()) {
			throw new IllegalArgumentException("Specified file does not represent a file: " + file.getName());
	    } else if (!file.canRead()) {
	    	throw new IllegalArgumentException("Specified file cannot be read, please check the SO permissions: " + file.getName());
	    }
		this.flatFileDefinition = flatFile;
		this.fileSize = file.length();
		this.decoder = charset.newDecoder();
		
		this.byteBuffer = ByteBuffer.allocateDirect(lineByteBufferSize);
		this.channel = (new RandomAccessFile(file, "r")).getChannel();
		
		this.buffers = FileUtil.mapFileUsingMappedByteBuffers(channel, FileChannel.MapMode.READ_ONLY, PAGE_SIZE);
		if (buffers.size() == 1) {
			this.singleBuffer = buffers.get(0);
			isSingleBuffer = true;
		}
	}
	
	private byte getByteByPosition(long position) {
		if (isSingleBuffer) {
			return singleBuffer.get((int)position);
		} else {
			int bufferIndex = (int)(position / PAGE_SIZE);
			int bufferPosition = (int)(position % PAGE_SIZE);
			return buffers.get(bufferIndex).get(bufferPosition);
		}
	}
		
	public boolean isResetSupported() {
		return IS_RESET_SUPPORTED;
	}
	
	public void reset() {
		this.position = 0;
		this.recordIndex = 0;
		this.recordType = null;
		this.recordText = null;
	}
	
	public void close() throws IOException {
		if (channel != null && channel.isOpen()) {
			this.channel.close();
		}
		if (buffers != null && !buffers.isEmpty()) {
			this.buffers.clear();
		}
		this.singleBuffer = null;
		this.byteBuffer = null;
		this.fileSize = 0;
		this.closed = true;
		System.gc();
	}
	
	public boolean hasNext() {
		if (position < fileSize) {
			return true;
		} else {
			return false;
		}
	}
	
	public Object next() {	
		if (!this.hasNext()) {
			throw new NoSuchElementException("There are no more records to read");
		}

		this.byteBuffer.clear();
		boolean foundLineBreak = false;
		boolean foundEndOfFile = false;
		while(!foundLineBreak && !foundEndOfFile) {
			
			byte actualByte = getByteByPosition(position);
			if ((char)actualByte == '\r' || (char)actualByte == '\n') {
				foundLineBreak = true;
				if (position == fileSize - 1) {
					foundEndOfFile = true;
				} else {
					byte nextByte = getByteByPosition(position + 1);
					if ((char)nextByte == '\n') {
						position++;
					}
				}
			} else {
				byteBuffer.put(actualByte);
			}
			
			if (position == fileSize - 1) {
				foundEndOfFile = true;
			}
			
			position++;
		}
		
		Object record;
		try {
			byteBuffer.flip();
			CharBuffer charBuffer = this.decoder.decode(byteBuffer);
			record = parseRecordFromText(charBuffer.toString());
			this.recordText = charBuffer.toString();
			recordIndex++;
		} catch (IOException e) {
			throw new FFPojoException("Error while decoding the line number " + (recordIndex + 1), e);
		} catch (RecordParserException e) {
			throw new FFPojoException("Error while parsing from text the line number " + (recordIndex + 1), e);
		}
		
		return record;
	}

}
