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

import flapjack.util.TypeConverter;


public interface ObjectMapper {

    /**
     * Map the given field to the domain model
     *
     * @param parsedFields - the fields parsed from the record
     * @param domain       - the domain model to map the fields to
     * @throws ObjectMappingException - thrown when a problem occurs while trying to map the fields
     */
    void mapOnTo(Object parsedFields, Object domain) throws ObjectMappingException;

    /**
     * You may not want to map all the fields in the record
     *
     * @param ignoreUnmappedFields - ingore fields in the record you do not care to map to your domain model
     */
    void setIgnoreUnmappedFields(boolean ignoreUnmappedFields);

    /**
     * The ObjectMappingStore that contains all your ObjectMappings
     *
     * @param objectMappingStore - the store to be used during mapping
     */
    void setObjectMappingStore(ObjectMappingStore objectMappingStore);

    /**
     * The TypeConverter to be used for the object mapping
     *
     * @param typeConverter
     */
    void setTypeConverter(TypeConverter typeConverter);
}
