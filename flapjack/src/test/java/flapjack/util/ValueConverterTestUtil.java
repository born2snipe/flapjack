/**
 * Copyright 2008 Dan Dudley
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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class ValueConverterTestUtil {
    public static byte[] bytes(final int i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeInt(i);
            }
        });
    }

    public static byte[] bytes(final long i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeLong(i);
            }
        });
    }

    public static byte[] bytes(final float i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeFloat(i);
            }
        });
    }

    public static byte[] bytes(final double i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeDouble(i);
            }
        });
    }

    public static byte[] bytes(final boolean i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeBoolean(i);
            }
        });
    }

    public static byte[] bytes(final short i) {
        return write(new WriteData() {
            public void write(DataOutputStream output) throws IOException {
                output.writeShort(i);
            }
        });
    }

    private static byte[] write(WriteData writeData) {
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
