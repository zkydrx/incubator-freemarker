/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.freemarker.core.model.impl;

import org.apache.freemarker.core.Version;
import org.apache.freemarker.core.model.TemplateHashModel;
import org.apache.freemarker.core.model.TemplateModel;
import org.apache.freemarker.core.model.TemplateModelException;

/**
 * A restricted object wrapper that will not expose arbitrary object, just those that directly correspond to the
 * {@link TemplateModel} sub-interfaces ({@code String}, {@code Map} and such). If it had to wrap other kind of objects,
 * it will throw exception. It will also block {@code ?api} calls on the values it wraps.
 */
public class SimpleObjectWrapper extends DefaultObjectWrapper {

    /**
     * @param incompatibleImprovements see in {@link DefaultObjectWrapper#DefaultObjectWrapper(Version)}.
     * 
     * @since 2.3.21
     */
    public SimpleObjectWrapper(Version incompatibleImprovements) {
        super(incompatibleImprovements);
    }

    /**
     * Called if a type other than the simple ones we know about is passed in. 
     * In this implementation, this just throws an exception.
     */
    @Override
    protected TemplateModel handleNonBasicTypes(Object obj) throws TemplateModelException {
        throw new TemplateModelException("SimpleObjectWrapper deliberately won't wrap this type: "
                + obj.getClass().getName());
    }

    @Override
    public TemplateHashModel wrapAsAPI(Object obj) throws TemplateModelException {
        throw new TemplateModelException("SimpleObjectWrapper deliberately doesn't allow ?api.");
    }

}
