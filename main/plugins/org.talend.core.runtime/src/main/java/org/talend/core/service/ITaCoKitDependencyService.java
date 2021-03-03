/*
 * Copyright (C) 2006-2021 Talend Inc. - www.talend.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 */
package org.talend.core.service;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.swt.graphics.Image;
import org.talend.core.IService;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * For documentation, see implementation in org.talend.sdk.component.studio-integration plugin
 */
public interface ITaCoKitDependencyService extends IService {

    boolean hasTaCoKitComponents(final Stream<IComponent> components);

    Set<String> getTaCoKitOnlyDependencies(final Stream<IComponent> components);

    Stream<IComponent> getJobComponents(Item item);

    Path findM2Path();

    Image getTaCoKitImageByRepositoryType(ERepositoryObjectType repObjType);
}
