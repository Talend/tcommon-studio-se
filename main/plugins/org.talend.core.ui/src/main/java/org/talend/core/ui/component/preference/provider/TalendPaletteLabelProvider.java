// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.component.preference.provider;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.ImageProvider;

public class TalendPaletteLabelProvider implements ILabelProvider {

    @Override
    public Image getImage(Object element) {
        IPaletteItem entry = (IPaletteItem) element;
        ImageDescriptor descriptor = entry.getImageDesc();
        return ImageProvider.getImage(descriptor);
    }

    @Override
    public String getText(Object element) {
        if (element instanceof String) {
            return (String) element;
        }
        return ((IPaletteItem) element).getLabel();
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub
    }

}
