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


public abstract class AbstractTextValueConverter implements ValueConverter {
    public final Object toDomain(byte[] bytes) {
        if (bytes == null)
            return null;
        String text = new String(bytes);
        if (text.length() == 0)
            return null;
        return fromText(text);
    }

    public final byte[] toBytes(Object domain) {
        if (domain == null)
            return null;
        return toText(domain).getBytes();
    }

    protected abstract Object fromText(String text);

    protected String toText(Object domain) {
        return domain.toString();
    }
}
