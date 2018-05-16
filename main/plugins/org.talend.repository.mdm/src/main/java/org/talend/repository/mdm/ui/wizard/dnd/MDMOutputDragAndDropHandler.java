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
package org.talend.repository.mdm.ui.wizard.dnd;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.swt.dnd.LocalDataTransfer;
import org.talend.commons.ui.swt.dnd.LocalDraggedData;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Attribute;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Element;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractXmlStepForm;

/**
 * wzhang class global comment. Detailled comment
 */
public class MDMOutputDragAndDropHandler {

    private MDMSchema2TreeLinker linker;

    private DragSource dragSource;

    private DropTarget loopDropTarget;

    public MDMOutputDragAndDropHandler(MDMSchema2TreeLinker linker) {
        this.linker = linker;
        init();
    }

    private void init() {
        createDragSource();
        createDropTarget();
    }

    private AbstractXmlStepForm getMainForm() {
        return linker.getForm();
    }

    private void createDragSource() {
        if (dragSource != null) {
            dragSource.dispose();
        }

        dragSource = new DragSource(linker.getSource(), DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
        dragSource.setTransfer(new Transfer[] { LocalDataTransfer.getInstance() });

        DragSourceListener sourceListener = new TreeDragSourceListener();
        dragSource.addDragListener(sourceListener);
    }

    private void createDropTarget() {

        if (loopDropTarget != null) {
            loopDropTarget.dispose();
        }
        loopDropTarget = new DropTarget(linker.getTarget(), DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
        loopDropTarget.setTransfer(new Transfer[] { LocalDataTransfer.getInstance() });
        DropTargetListener targetListener = new TableDropTargetListener();
        loopDropTarget.addDropListener(targetListener);

    }

    /**
     * 
     * DOC wzhang XmlFileDragAndDropHandler class global comment. Detailled comment
     */

    class TreeDragSourceListener implements TransferDragSourceListener {

        public void dragFinished(DragSourceEvent event) {
            event.getSource();
        }

        public void dragSetData(DragSourceEvent event) {
            event.getSource();
        }

        public void dragStart(DragSourceEvent event) {
            TableItem[] items = linker.getSource().getSelection();
            if (items.length == 0) {
                event.doit = false;
            } else {
                LocalDraggedData draggedData = new LocalDraggedData();
                for (TableItem tableItem : items) {
                    draggedData.add(tableItem.getData());
                }
                draggedData.setTable(getMainForm().getMetadataTable());
                LocalDataTransfer.getInstance().setLocalDraggedData(draggedData);
            }

        }

        public Transfer getTransfer() {
            return LocalDataTransfer.getInstance();
        }
    }

    /**
     * 
     * DOC wzhang XmlFileDragAndDropHandler class global comment. Detailled comment
     */
    class TableDropTargetListener implements TransferDropTargetListener {

        public void dragEnter(DropTargetEvent event) {

        }

        public void dragLeave(DropTargetEvent event) {

        }

        public void dragOperationChanged(DropTargetEvent event) {
        }

        public void dropAccept(DropTargetEvent event) {

        }

        public Transfer getTransfer() {
            return LocalDataTransfer.getInstance();
        }

        public boolean isEnabled(DropTargetEvent event) {
            return true;
        }

        public void dragOver(DropTargetEvent event) {
            Item targetItem = (Item) event.item;
            if (targetItem == null) {
                event.detail = DND.DROP_NONE;
                return;
            }
            FOXTreeNode targetNode = (FOXTreeNode) (targetItem.getData());
            LocalDraggedData draggedData = LocalDataTransfer.getInstance().getDraggedData();
            List<Object> dragdedData = draggedData.getTransferableEntryList();
            if (dragdedData.size() == 1 && isDropRelatedColumn(event)) {
                if (targetNode instanceof Element) {
                    Element element = (Element) targetNode;
                    if (!element.getElementChildren().isEmpty() || element.getParent() == null) {
                        event.detail = DND.DROP_NONE;
                        return;
                    }
                } else {
                    FOXTreeNode parent = targetNode.getParent();
                    if (parent == null) {
                        event.detail = DND.DROP_NONE;
                        return;
                    }
                }
            }
            event.detail = DND.DROP_LINK;
        }

        private boolean isDropRelatedColumn(DropTargetEvent event) {
            DropTarget dropTarget = (DropTarget) event.getSource();
            Display display = event.display;
            Control control = dropTarget.getControl();
            TreeItem item = (TreeItem) event.item;
            Rectangle rec = display.map(control, null, item.getBounds(1));
            if ((event.x >= rec.x) && (event.y >= rec.y) && ((event.x - rec.x) <= rec.width) && ((event.y - rec.y) <= rec.height)) {
                return true;
            }
            return false;
        }

        private void setDefaultFixValue(FOXTreeNode treeNode) {
            String fixValue = treeNode.getDefaultValue();
            if (fixValue == null) {
                return;
            }
            treeNode.setDefaultValue(null);
        }

        public void drop(DropTargetEvent event) {
            DropTarget dropTarget = (DropTarget) event.getSource();
            Item targetItem = (Item) event.item;
            if (targetItem == null) {
                return;
            }
            Control control = dropTarget.getControl();
            LocalDraggedData draggedData = LocalDataTransfer.getInstance().getDraggedData();
            List<Object> dragdedData = draggedData.getTransferableEntryList();
            IMetadataTable table = null;
            if (draggedData.getTable() instanceof MetadataTable) {
                table = ConvertionHelper.convert((MetadataTable) draggedData.getTable());
            }

            FOXTreeNode targetNode = (FOXTreeNode) (targetItem.getData());

            if (dragdedData.size() == 1 && isDropRelatedColumn(event)) {
                if (!targetNode.hasChildren()) {
                    // IMetadataColumn metaColumn = (IMetadataColumn) dragdedData.get(0);
                    IMetadataColumn metaColumn = ConvertionHelper.convertToIMetaDataColumn((MetadataColumn) dragdedData.get(0));
                    targetNode.setDefaultValue(null);
                    targetNode.setColumn(metaColumn);
                    targetNode.setTable(table);
                    targetNode.setDataType(metaColumn.getTalendType());
                    // targetNode.setRow(row);

                    linker.getXMLViewer().refresh(targetNode);
                    linker.getXMLViewer().expandAll();

                    Display display = linker.getSource().getDisplay();
                    Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
                    linker.getSource().getShell().setCursor(cursor);

                    linker.valuedChanged(targetItem);

                    linker.getSource().getShell().setCursor(null);
                }
            } else if (dragdedData.size() > 0) {

                if (targetNode.hasChildren()) {
                    List<FOXTreeNode> children = targetNode.getChildren();
                    for (FOXTreeNode foxTreeNode : children) {
                        if (!(foxTreeNode instanceof Attribute)) {
                            MessageDialog.openConfirm(control.getShell(), Messages.getString("MDMOutputDragAndDropHandler_warn"), //$NON-NLS-1$
                                    Messages.getString("MDMOutputDragAndDropHandler.Has_element", targetNode.getLabel())); //$NON-NLS-1$
                            return;
                        }
                    }
                } else if (targetNode.getParent() == null) {
                    MessageDialog.openConfirm(control.getShell(), Messages.getString("MDMOutputDragAndDropHandler_warn"), //$NON-NLS-1$
                            Messages.getString("MDMOutputDragAndDropHandler.Is_root", targetNode.getLabel())); //$NON-NLS-1$
                    return;
                }
                // IMetadataColumn metaColumn = (IMetadataColumn) dragdedData.get(0);
                IMetadataColumn metaColumn = ConvertionHelper.convertToIMetaDataColumn((MetadataColumn) dragdedData.get(0));

                targetNode.setColumn(metaColumn);
                targetNode.setDataType(metaColumn.getTalendType());
                setDefaultFixValue(targetNode);

                linker.getXMLViewer().refresh();
                linker.getXMLViewer().expandAll();

                Display display = linker.getSource().getDisplay();
                Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
                linker.getSource().getShell().setCursor(cursor);

                linker.valuedChanged(targetItem);

                linker.getSource().getShell().setCursor(null);
            }
            linker.getXMLViewer().refresh();
            linker.getXMLViewer().expandAll();
            linker.updateLinksStyleAndControlsSelection(control, true);
            linker.getForm().updateConnection();
            linker.getForm().updateStatus();
        }
    }

}
