/*
 * Copyright 2009-2011 Levi Hoogenberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.aluminumproject.writers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class AbstractOutputStreamWriterTest {
	private OutputStream outputStream;

	private AbstractOutputStreamWriter writer;
	private AbstractOutputStreamWriter bufferedWriter;
	private AbstractOutputStreamWriter nonClosingWriter;

	@BeforeMethod
	public void createOutputStreamAndWriters() {
		outputStream = new OutputStream();

		writer = new OutputStreamWriter(outputStream, false, true);
		bufferedWriter = new OutputStreamWriter(outputStream, true, true);
		nonClosingWriter = new OutputStreamWriter(outputStream, false, false);
	}

	public void byteArraysShouldBeWrittenAsIs() {
		writer.write(new byte[] {10});

		byte[] bytes = outputStream.toByteArray();
		assert bytes != null;
		assert bytes.length == 1;
		assert bytes[0] == 10;
	}

	public void nonByteArraysShouldBeConvertedToStrings() {
		writer.write(10);

		String string = outputStream.toString();
		assert string != null;
		assert string.equals("10");
	}

	@Test(dependsOnMethods = "nonByteArraysShouldBeConvertedToStrings")
	public void bufferedWriterShouldBufferWrittenObjects() {
		bufferedWriter.write(10);

		byte[] bytes = outputStream.toByteArray();
		assert bytes != null;
		assert bytes.length == 0;
	}

	@Test(dependsOnMethods = "bufferedWriterShouldBufferWrittenObjects")
	public void flushingWriterShouldFlushBuffer() {
		bufferedWriter.write(10);
		bufferedWriter.flush();

		String string = outputStream.toString();
		assert string != null;
		assert string.equals("10");
	}

	public void closingWriterShouldCloseOutputStream() {
		writer.close();

		assert !outputStream.open;
	}

	public void closingNonClosingOutputStreamWriterShouldLeaveOutputStreamOpen() {
		nonClosingWriter.close();

		assert outputStream.open;
	}

	private static class OutputStream extends ByteArrayOutputStream {
		private boolean open;

		public OutputStream() {
			open = true;
		}

		public void close() throws IOException {
			super.close();

			open = false;
		}
	}

	private static class OutputStreamWriter extends AbstractOutputStreamWriter {
		private OutputStream outputStream;

		private boolean close;

		public OutputStreamWriter(OutputStream outputStream, boolean buffer, boolean close) {
			super(buffer);

			this.outputStream = outputStream;

			this.close = close;
		}

		protected OutputStream createOutputStream() {
			return outputStream;
		}

		protected void closeOutputStream() throws WriterException {
			if (close) {
				super.closeOutputStream();
			}
		}
	}
}