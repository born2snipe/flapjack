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
package flapjack.model;

import flapjack.layout.FieldDefinition;
import flapjack.util.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;


public abstract class AbstractBinaryFieldFactory implements BinaryFieldFactory {
    public byte[] build(Object domain, TypeConverter typeConverter, FieldDefinition fieldDefinition) {
        return build(domain, typeConverter, Arrays.asList(new FieldDefinition[]{fieldDefinition}));
    }

    public byte[] build(Object domain, TypeConverter typeConverter, List fieldDefinitions) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            buid(output, domain, typeConverter, fieldDefinitions);
        } catch (IOException e) {
            // TODO -revisit!?
            throw new RuntimeException(e);
        }
        return output.toByteArray();
    }

    protected abstract void buid(OutputStream output, Object domain, TypeConverter typeConverter, List fieldDefinitions) throws IOException;
}
