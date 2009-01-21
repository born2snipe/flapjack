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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


public abstract class AbstractBinaryValueConverter implements ValueConverter {
    public final Object convert(byte[] bytes) {
        if (bytes == null)
            throw new IllegalArgumentException("Byte array given was null");
        if (bytes.length < requiredNumberOfBytes())
            throw new IllegalArgumentException("There are not enough bytes expected " + requiredNumberOfBytes() + " got " + bytes.length + " bytes");

        DataInputStream input = null;
        try {
            input = new DataInputStream(new ByteArrayInputStream(bytes));
            return readData(input);
        } catch (IOException err) {
            throw new RuntimeException(err);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {

                }
            }
        }
    }

    protected abstract int requiredNumberOfBytes();

    protected abstract Object readData(DataInputStream input) throws IOException;
}