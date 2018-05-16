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
package org.talend.repository.example.viewer.content.example;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.talend.repository.example.viewer.tester.example.ExampleTester;
import org.talend.repository.model.RepositoryNode;

public class ExampleSubCompProvider implements ITreeContentProvider {

    ExampleTester exampleTester = new ExampleTester();

    @Override
    public Object getParent(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    //
    // WARNING, you can return false here and still return children that will be displayed.
    //
    //
    @Override
    public boolean hasChildren(Object element) {
        boolean isJob = element instanceof RepositoryNode ? exampleTester.test(element, "isJob", null, null) : false; //$NON-NLS-1$
        return element.toString().startsWith("bm") || isJob;// just to limit to 1 level //$NON-NLS-1$
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object[] getElements(Object inputElement) {

        return getChildren(inputElement);

    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (hasChildren(parentElement)) {
            return new Object[] { "comp1", "comp2", "comp3" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }// else no children
        return new Object[0];
    }

}
