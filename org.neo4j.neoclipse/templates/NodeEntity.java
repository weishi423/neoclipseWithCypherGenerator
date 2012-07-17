/*******************************************************************************
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 * nrkkalyan@gmail.com
 *******************************************************************************/


import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.data.neo4j.annotation.*;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.*;


/**
 * 
 * _COMMENTS_
 * 
 */
@NodeEntity
public class _ENTITY_ extends BaseNode {
	private static final long serialVersionUID = 1L;

_@RelatedTo_
_@RelatedToVia_
_@Query_
_FIELDS_

    public _ENTITY_() {
    }

_GETTER_AND_SETTER_
    
    @Override
    public String toString() {
    	String str = _TO_STRING_;
        return str;
    }
      
}

