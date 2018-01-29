// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.preference.collector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.token.TokenCollectorFactory;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

/**
 * ggu class global comment. Detailled comment
 */
public class TalendDataCollectorPreviewPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    public TalendDataCollectorPreviewPreferencePage() {
        super();
        setPreferenceStore(CoreUIPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.getString("TalendDataCollectorPreviewPreferencePage_Description")); //$NON-NLS-1$
        noDefaultAndApplyButton();
    }

    public void init(IWorkbench arg0) {

    }

    @Override
    protected Control createContents(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new GridLayout());
        comp.setLayoutData(new GridData(GridData.FILL_BOTH));

        // // label
        // Label label = new Label(comp, SWT.NONE);
        // label.setText(Messages.getString("TalendDataCollectorPreviewPreferencePage_Details")); //$NON-NLS-1$

        // tree
        TreeViewer treeViewer = new TreeViewer(comp, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        final Tree tree = treeViewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 500;
        gridData.minimumHeight = 500;
        tree.setLayoutData(gridData);

        TreeColumn keyColumn = new TreeColumn(tree, SWT.NONE);
        keyColumn.setText(Messages.getString("TalendDataCollectorPreviewPreferencePage_Key")); //$NON-NLS-1$
        keyColumn.setWidth(200);

        TreeColumn valueColumn = new TreeColumn(tree, SWT.NONE);
        valueColumn.setText(Messages.getString("TalendDataCollectorPreviewPreferencePage_Value")); //$NON-NLS-1$
        valueColumn.setWidth(250);

        treeViewer.setContentProvider(new JsonProvider());
        treeViewer.setLabelProvider(new JsonProvider());

        // set data
        try {
            TokenCollectorFactory factory = TokenCollectorFactory.getFactory();
            factory.priorCollect();
            treeViewer.setInput(factory.collectTokenInfors());
        } catch (Exception e) {
            //
        }

        //
        treeViewer.expandAll();
        keyColumn.pack();

        return comp;
    }

    /**
     * 
     * ggu JsonProvider class global comment. Detailled comment
     */

    class JsonProvider extends LabelProvider implements ITreeContentProvider, ITableLabelProvider {

        private static final String EMPTY = ""; //$NON-NLS-1$

        /**
         * 
         * ggu KeyValue class global comment. Detailled comment
         */
        class KeyValue {

            private String key;

            private Object value;

            public KeyValue(String key, Object value) {
                this.key = key;
                this.value = value;
            }

            public String getKey() {
                return key;
            }

            public Object getValue() {
                return value;
            }

        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

        public Image getColumnImage(Object arg0, int arg1) {
            return null;
        }

        public String getColumnText(Object obj, int index) {

            if (obj instanceof KeyValue) {
                KeyValue kv = (KeyValue) obj;
                if (index == 0) {
                    return kv.getKey();
                } else if (index == 1) {
                    Object value = kv.getValue();
                    if (value instanceof JSONObject || value instanceof JSONArray) {
                        return getColumnText(value, index);
                    }
                    return value.toString();
                }

            } else if (index == 1) { // work for value
                String emptyValue = Messages.getString("TalendDataCollectorPreviewPreferencePage_EmptyValue"); //$NON-NLS-1$
                if (obj instanceof JSONObject) {
                    return ((JSONObject) obj).length() > 0 ? EMPTY : emptyValue;
                } else if (obj instanceof JSONArray) {
                    return ((JSONArray) obj).length() > 0 ? EMPTY : emptyValue;
                } else {
                    return obj.toString();
                }
            }
            return null;
        }

        public Object[] getChildren(Object obj) {
            if (obj instanceof JSONObject) {
                JSONObject json = (JSONObject) obj;
                Iterator<String> keys = json.keys();
                List<KeyValue> list = new ArrayList<KeyValue>();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        Object value = json.get(key);
                        KeyValue kv = new KeyValue(key, value);
                        list.add(kv);
                    } catch (JSONException e) {
                        //
                    }
                }
                Collections.sort(list, new Comparator<KeyValue>() {

                    public int compare(KeyValue kv1, KeyValue kv2) {
                        if (kv1.getValue() instanceof JSONObject || kv1.getValue() instanceof JSONArray) {
                            if (kv2.getValue() instanceof JSONObject || kv2.getValue() instanceof JSONArray) {
                                // compare the key.
                            } else {
                                return 1;
                            }
                        }
                        if (kv2.getValue() instanceof JSONObject || kv2.getValue() instanceof JSONArray) {
                            if (kv1.getValue() instanceof JSONObject || kv1.getValue() instanceof JSONArray) {
                                // compare the key.
                            } else {
                                return -1;
                            }
                        }
                        return kv1.getKey().compareTo(kv2.getKey());
                    }
                });
                return list.toArray();
            } else if (obj instanceof JSONArray) {
                JSONArray array = (JSONArray) obj;
                Object[] objects = new Object[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    try {
                        Object object = array.get(i);
                        objects[i] = object;
                    } catch (JSONException e) {
                        //
                    }
                }
                return objects;
            } else if (obj instanceof KeyValue) {
                KeyValue kv = (KeyValue) obj;
                return getChildren(kv.getValue());
            }
            return null;
        }

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof JSONObject) {
                return getChildren(inputElement);
            }
            return null;
        }

        public Object getParent(Object arg0) {
            return null;
        }

        public boolean hasChildren(Object arg0) {
            Object[] children = getChildren(arg0);
            return children != null && children.length > 0;
        }

    }
}
