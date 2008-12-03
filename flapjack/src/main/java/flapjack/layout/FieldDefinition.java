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

// TODO - need to come up with a format type (Text or Binary)
public interface FieldDefinition {
    /**
     * The location of the field in the record
     * <b>Zero based</b>
     *
     * @return
     */
    int getPosition();

    /**
     * The length of the field in the record
     *
     * @return
     */
    int getLength();

    /**
     * Documentary value
     *
     * @return
     */
    String getName();
}
