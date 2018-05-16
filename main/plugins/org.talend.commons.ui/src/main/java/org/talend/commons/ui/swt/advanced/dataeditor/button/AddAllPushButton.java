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
package org.talend.commons.ui.swt.advanced.dataeditor.button;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public abstract class AddAllPushButton extends ExtendedPushButton {

	/**
	 * DOC YeXiaowei AddAllPustButton constructor comment.
	 * 
	 * @param parent
	 * @param extendedViewer
	 * @param tooltip
	 * @param image
	 */
	public AddAllPushButton(Composite parent,
			AbstractExtendedControlViewer extendedViewer) {
		super(parent, extendedViewer, Messages
				.getString("AddAllPushButton.AddAllButton.Tip"), ImageProvider //$NON-NLS-1$
				.getImage(EImage.ADD_ALL_ICON));
	}

	protected abstract Command getCommandToExecute();

	protected abstract List<Object> getObjectToAdd();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton
	 * #getEnabledState()
	 */
	@Override
	public boolean getEnabledState() {
		return super.getEnabledState()
				&& !getExtendedControlViewer().isReadOnly();
	}
}
