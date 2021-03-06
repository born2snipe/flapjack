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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class CharBinaryValueConverter extends AbstractBinaryValueConverter implements TypedValueConverter {

    public Class[] types() {
        return new Class[]{char.class, Character.class};
    }

    protected int requiredNumberOfBytes() {
        return 2;
    }

    protected Object readData(DataInputStream input) throws IOException {
        return new Character(input.readChar());
    }

    protected void writeData(DataOutputStream output, Object domain) throws IOException {
        output.writeChar(((Character) domain).charValue());
    }

}
