// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.repository;

import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.Viewer;

/**
 * created by wchen on 2013-11-12 Detailled comment
 * 
 */
public interface IRepositoryReviewFilter extends IExecutableExtension {

    public boolean filter(Viewer viewer, Object parentElement, Object element, String filterType);

}
