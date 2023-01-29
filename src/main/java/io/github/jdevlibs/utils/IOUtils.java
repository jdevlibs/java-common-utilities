/*  ---------------------------------------------------------------------------
 *  * Copyright 2020-2021 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  ---------------------------------------------------------------------------
 */
package io.github.jdevlibs.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Supot Saelao
 * @version 1.0
 */
public final class IOUtils {

	private static final int EOF = -1;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	private static final String LINE_SEP_WINDOWS = "\r\n";
	private static final String LINE_SEP_UNIX 	 = "\n";
	
	private IOUtils() {}
	
	public static byte[] toBytes(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		
		return output.toByteArray();
	}
	
	public static ByteArrayOutputStream toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		
		return output;
	}
	
	public static InputStream toInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}
	
	public static InputStream toInputStream(String value) throws IOException {
		return toInputStream(value, null);
	}

    public static InputStream toInputStream(String value, String encoding) throws IOException {
        byte[] bytes = Validators.isNotEmpty(encoding) ? value.getBytes(encoding) : value.getBytes();
        return new ByteArrayInputStream(bytes);
    }
    
    public static String toString(InputStream input) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw);
        return sw.toString();
    }
    
    public static String toString(InputStream input, String encoding)
            throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw, encoding);
        return sw.toString();
    }

    public static String toString(Reader input) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw);
        return sw.toString();
    }

    public static List<String> readLines(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input);
        return readLines(reader);
    }

    public static List<String> readLines(InputStream input, String encoding) throws IOException {
        if (encoding == null) {
            return readLines(input);
        } else {
            InputStreamReader reader = new InputStreamReader(input, encoding);
            return readLines(reader);
        }
    }

	public static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		List<String> list = new ArrayList<>();
		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}
		return list;
	}
    
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static void copy(InputStream input, Writer output) throws IOException {
		InputStreamReader in = new InputStreamReader(input);
		copy(in, output);
	}

	public static void copy(InputStream input, Writer output, String encoding) throws IOException {
		if (encoding == null) {
			copy(input, output);
		} else {
			InputStreamReader in = new InputStreamReader(input, encoding);
			copy(in, output);
		}
	}

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

	public static void copy(Reader input, OutputStream output) throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(output);
		copy(input, out);
		out.flush();
	}

    public static void copy(Reader input, OutputStream output, String encoding)
            throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            OutputStreamWriter out = new OutputStreamWriter(output, encoding);
            copy(input, out);
            out.flush();
        }
    }
    
	public static void write(byte[] data, OutputStream output) throws IOException {
		if (Validators.isNotNull(data)) {
			output.write(data);
		}
	}

    public static void write(byte[] data, Writer output) throws IOException {
    	if (Validators.isNotNull(data)) {
            output.write(new String(data));
        }
    }

	public static void write(byte[] data, Writer output, String encoding) throws IOException {
		if (Validators.isNotNull(data)) {
			if (encoding == null) {
				write(data, output);
			} else {
				output.write(new String(data, encoding));
			}
		}
	}

	public static void write(char[] data, Writer output) throws IOException {
		if (Validators.isNotNull(data)) {
			output.write(data);
		}
	}

	public static void write(char[] data, OutputStream output) throws IOException {
		if (Validators.isNotNull(data)) {
			output.write(new String(data).getBytes());
		}
	}

    public static void write(char[] data, OutputStream output, String encoding)
            throws IOException {
    	if (Validators.isNotNull(data)) {
            if (encoding == null) {
                write(data, output);
            } else {
                output.write(new String(data).getBytes(encoding));
            }
        }
    }

    public static void write(String data, Writer output) throws IOException {
    	if (Validators.isNotNull(data)) {
            output.write(data);
        }
    }

	public static void write(String data, OutputStream output) throws IOException {
		if (Validators.isNotNull(data)) {
			output.write(data.getBytes());
		}
	}

	public static void write(String data, OutputStream output, String encoding) throws IOException {
		if (Validators.isNotNull(data)) {
			if (encoding == null) {
				write(data, output);
			} else {
				output.write(data.getBytes(encoding));
			}
		}
    }

	public static void write(StringBuilder data, Writer output) throws IOException {
		if (Validators.isNotNull(data)) {
			output.write(data.toString());
		}
	}

	public static void write(StringBuilder data, OutputStream output) throws IOException {
		if (Validators.isNotNull(data)) {
			output.write(data.toString().getBytes());
		}
	}

	public static void write(StringBuilder data, OutputStream output, String encoding) throws IOException {
		if (Validators.isNotNull(data)) {
			if (encoding == null) {
				write(data, output);
			} else {
				output.write(data.toString().getBytes(encoding));
			}
		}
	}

	public static void writeLines(Collection<String> lines, OutputStream output) throws IOException {
		if (Validators.isEmpty(lines)) {
			return;
		}
		
		String lineEnding = getLineEnding();
		for (String line : lines) {
			if (Validators.isNotNull(line)) {
				output.write(line.getBytes());
			}
			if (lineEnding != null) {
				output.write(lineEnding.getBytes());
			}
		}
    }

	public static void writeLines(Collection<String> lines, OutputStream output, String encoding)
			throws IOException {
    	
        if (Validators.isEmpty(encoding)) {
            writeLines(lines, output);
        } else {
        	if (Validators.isEmpty(lines)) {
        		return;
        	}
        	
        	String lineEnding = getLineEnding();
        	for (String line : lines) {
        		if (Validators.isNotNull(line)) {
                    output.write(line.getBytes(encoding));
                }
				if (lineEnding != null) {
					output.write(lineEnding.getBytes(encoding));
				}
            }
        }
    }

	public static void writeLines(Collection<String> lines, Writer writer) throws IOException {
		if (Validators.isEmpty(lines)) {
			return;
		}
		
		String lineEnding = getLineEnding();
		for (String line : lines) {
			if (Validators.isNotNull(line)) {
                writer.write(line);
            }
            writer.write(lineEnding);
        }
    }

    private static String getLineEnding() {
		if (Systems.isWindowsOS()) {
			return LINE_SEP_WINDOWS;
		} else {
			return LINE_SEP_UNIX;
		}
    }
}
