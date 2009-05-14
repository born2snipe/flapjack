/**
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. 
 */
package flapjack.util;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;


public abstract class ValueConverterTestCase extends TestCase {
    private static final String EQUALS = "expected {0}, actual {1}";

    protected void assertEquals(byte[] expected, byte[] actual) {
        assertTrue("Byte array contents do not match", Arrays.equals(expected, actual));
    }

    protected void assertEquals(Class[] expected, Class[] actual) {
        assertTrue(MessageFormat.format(EQUALS, new Object[]{Arrays.toString(expected), Arrays.toString(actual)}), Arrays.equals(expected, actual));
    }

    protected byte[] binary(String value) {
        return value.getBytes();
    }

    protected byte[] binary(final int i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeInt(i);
            }
        });
    }

    protected byte[] binary(final long i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeLong(i);
            }
        });
    }

    protected byte[] binary(final float i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeFloat(i);
            }
        });
    }

    protected byte[] binary(final double i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeDouble(i);
            }
        });
    }

    protected byte[] binary(final boolean i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeBoolean(i);
            }
        });
    }

    protected byte[] binary(final short i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeShort(i);
            }
        });
    }

    protected byte[] binary(final char i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeChar(i);
            }
        });
    }

    protected byte[] text(final int i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.write(String.valueOf(i).getBytes());
            }
        });
    }

    protected byte[] text(final long i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.write(String.valueOf(i).getBytes());
            }
        });
    }

    protected byte[] text(final float i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.write(String.valueOf(i).getBytes());
            }
        });
    }

    protected byte[] text(final double i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.write(String.valueOf(i).getBytes());
            }
        });
    }

    protected byte[] text(final boolean i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.write(String.valueOf(i).getBytes());
            }
        });
    }

    protected byte[] text(final short i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.write(String.valueOf(i).getBytes());
            }
        });
    }

    protected byte[] text(final char i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.write(String.valueOf(i).getBytes());
            }
        });
    }

    private byte[] write(WriteData writeData) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(baos);
        try {
            writeData.write(output);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    private static interface WriteData {
        void write(DataOutputStream output) throws IOException;
    }
}
