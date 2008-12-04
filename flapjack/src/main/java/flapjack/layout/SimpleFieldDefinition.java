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
package flapjack.layout;

import flapjack.util.DataType;

/**
 * Basic implementation of a FieldDefinition
 */
public class SimpleFieldDefinition implements FieldDefinition {
    private int position;
    private int length;
    private String name;
    private DataType dataType;

    public SimpleFieldDefinition() {
    }

    public SimpleFieldDefinition(String name, int position, int length, DataType dataType) {
        setPosition(position);
        setLength(length);
        setName(name);
        setDataType(dataType);
    }

    public int getPosition() {
        return position;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public int getLength() {
        return length;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DataType getFormat() {
        return dataType;
    }

    public String toString() {
        return getName() + "@" + getPosition() + ",length=" + getLength();
    }
}
