//============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2013 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
//============================================================================
package org.talend.core.service;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.core.IService;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Property;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC sunchaoqun  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public interface IESBMicroService extends IService {

    IProcessor createJavaProcessor(IProcess process, Property property, boolean filenameFromLabel, boolean isRoute);

    IRunnableWithProgress createRunnableWithProgress(Map exportChoiceMap,
            List<? extends IRepositoryNode> nodes, String version, String destinationPath, String context);
}
