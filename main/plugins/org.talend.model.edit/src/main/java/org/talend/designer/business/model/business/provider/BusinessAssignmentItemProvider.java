/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.designer.business.model.business.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.talend.designer.business.model.business.BusinessAssignment;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.BusinessProcess;
import org.talend.designer.business.model.business.Context;
import org.talend.designer.business.model.business.Copybook;
import org.talend.designer.business.model.business.DatabaseMetadata;
import org.talend.designer.business.model.business.Documentation;
import org.talend.designer.business.model.business.FileDelimitedMetadata;
import org.talend.designer.business.model.business.FileExcelMetadata;
import org.talend.designer.business.model.business.FileLdifMetadata;
import org.talend.designer.business.model.business.FilePositionalMetadata;
import org.talend.designer.business.model.business.FileRegexpMetadata;
import org.talend.designer.business.model.business.FileXmlMetadata;
import org.talend.designer.business.model.business.GenericSchemaMetadata;
import org.talend.designer.business.model.business.Joblet;
import org.talend.designer.business.model.business.Ldap;
import org.talend.designer.business.model.business.Process;
import org.talend.designer.business.model.business.Query;
import org.talend.designer.business.model.business.Routine;
import org.talend.designer.business.model.business.SAPFunction;
import org.talend.designer.business.model.business.SQLPattern;
import org.talend.designer.business.model.business.Salesforce;
import org.talend.designer.business.model.business.SapFunctionMetadata;
import org.talend.designer.business.model.business.Service;
import org.talend.designer.business.model.business.TableMetadata;
import org.talend.designer.business.model.business.TalendItem;
import org.talend.designer.business.model.business.Wsdl;
import org.talend.designer.business.model.business.util.BusinessSwitch;

/**
 * This is the item provider adapter for a {@link org.talend.designer.business.model.business.BusinessAssignment}
 * object. <!-- begin-user-doc --> <!-- end-user-doc -->
 */
public class BusinessAssignmentItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider,
        IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource,
        ITableItemLabelProvider {

    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public BusinessAssignmentItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public List getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

            addTalendItemPropertyDescriptor(object);
            addCommentPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Talend Item feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected void addTalendItemPropertyDescriptor(Object object) {
        itemPropertyDescriptors
                .add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
                        getResourceLocator(),
                        getString("_UI_BusinessAssignment_talendItem_feature"), //$NON-NLS-1$
                        getString(
                                "_UI_PropertyDescriptor_description", "_UI_BusinessAssignment_talendItem_feature", "_UI_BusinessAssignment_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        BusinessPackage.Literals.BUSINESS_ASSIGNMENT__TALEND_ITEM, true, false, true, null, null, null));
    }

    /**
     * This adds a property descriptor for the Comment feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected void addCommentPropertyDescriptor(Object object) {
        itemPropertyDescriptors
                .add(createItemPropertyDescriptor(
                        ((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
                        getResourceLocator(),
                        getString("_UI_BusinessAssignment_comment_feature"), //$NON-NLS-1$
                        getString(
                                "_UI_PropertyDescriptor_description", "_UI_BusinessAssignment_comment_feature", "_UI_BusinessAssignment_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        BusinessPackage.Literals.BUSINESS_ASSIGNMENT__COMMENT, true, false, false,
                        ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
     * This returns BusinessAssignment.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
     */
    public Object getImage(Object object) {
        BusinessAssignment businessAssignment = (BusinessAssignment) object;
        TalendItem talendItem = businessAssignment.getTalendItem();

        if (talendItem != null) {
            IItemLabelProvider itemLabelProvider = (IItemLabelProvider) adapterFactory
                    .adapt(talendItem, IItemLabelProvider.class);
            return itemLabelProvider.getImage(talendItem);
        }
        return null;
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     */
    public String getText(Object object) {
        BusinessAssignment businessAssignment = (BusinessAssignment) object;
        TalendItem talendItem = businessAssignment.getTalendItem();

        if (talendItem != null) {
            IItemLabelProvider itemLabelProvider = (IItemLabelProvider) adapterFactory
                    .adapt(talendItem, IItemLabelProvider.class);
            return itemLabelProvider.getText(talendItem);
        }
        return null;
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached children and by creating
     * a viewer notification, which it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     */
    public void notifyChanged(Notification notification) {
        updateChildren(notification);

        switch (notification.getFeatureID(BusinessAssignment.class)) {
            case BusinessPackage.BUSINESS_ASSIGNMENT__COMMENT:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
     * that can be created under this object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
    }

    /**
     * Return the resource locator for this item provider's resources.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ResourceLocator getResourceLocator() {
        return BusinessEditPlugin.INSTANCE;
    }

    public Object getColumnImage(Object object, int columnIndex) {
        if (columnIndex == 0)
            return getImage(object);
        return null;
    }

    public String getColumnText(Object object, int columnIndex) {
        BusinessAssignment businessAssignment = (BusinessAssignment) object;

        switch (columnIndex) {
        case 0:
            Object type = new BusinessSwitch() {

                @Override
                public Object caseBusinessProcess(BusinessProcess object) {
                    return getString("_UI_BusinessProcess_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseDatabaseMetadata(DatabaseMetadata object) {
                    return getString("_UI_DatabaseMetadata_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseDocumentation(Documentation object) {
                    return getString("_UI_Documentation_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseFileDelimitedMetadata(FileDelimitedMetadata object) {
                    return getString("_UI_FileDelimitedMetadata_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseFilePositionalMetadata(FilePositionalMetadata object) {
                    return getString("_UI_FilePositionalMetadata_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseProcess(Process object) {
                    return getString("_UI_Process_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseRoutine(Routine object) {
                    return getString("_UI_Routine_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseTableMetadata(TableMetadata object) {
                    return getString("_UI_TableMetadata_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseFileRegexpMetadata(FileRegexpMetadata object) {
                    return getString("_UI_FileRegexpMetadata_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseFileXmlMetadata(FileXmlMetadata object) {
                    return getString("_UI_FileXmlMetadata_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseFileLdifMetadata(FileLdifMetadata object) {
                    return getString("_UI_FileLdifMetadata_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseContext(Context object) {
                    return getString("_UI_Context_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseGenericSchemaMetadata(GenericSchemaMetadata object) {
                    return getString("_UI_GenericSchemaMetadata_type"); //$NON-NLS-1$
                }

                @Override
                public Object caseJoblet(Joblet object) {
                    // TODO Auto-generated method stub
                    return getString("_UI_Joblet_type");//$NON-NLS-1$
                }

                @Override
                public Object caseQuery(Query object) {
                    // TODO Auto-generated method stub
                    return getString("_UI_Query_type");//$NON-NLS-1$
                }

                @Override
                public Object caseFileExcelMetadata(FileExcelMetadata object) {
                    return getString("_UI_FileExcelMetadata_type");//$NON-NLS-1$
                }

                @Override
                public Object caseSapFunctionMetadata(SapFunctionMetadata object) {
                    return getString("_UI_SapFunctionMetadata_type");//$NON-NLS-1$
                }

                @Override
                public Object caseSAPFunction(SAPFunction object) {

                    return getString("_UI_SAPFunction_type");
                }

                @Override
                public Object caseCopybook(Copybook object) {
                    return getString("_UI_Copybook_type");//$NON-NLS-1$
                }

                @Override
                public Object caseLdap(Ldap object) {
                    return getString("_UI_Ldap_type");//$NON-NLS-1$
                }

                @Override
                public Object caseSalesforce(Salesforce object) {
                    return getString("_UI_Salesforce_type");//$NON-NLS-1$
                }

                @Override
                public Object caseSQLPattern(SQLPattern object) {
                    return getString("_UI_SQLPattern_type");//$NON-NLS-1$
                }

                @Override
                public Object caseWsdl(Wsdl object) {
                    return getString("_UI_Wsdl_type");//$NON-NLS-1$
                }

                public Object caseService(Service object) {
                    return getString("_UI_Service_type");//$NON-NLS-1$
                };

            }.doSwitch(businessAssignment.getTalendItem());

            return (String) type;
        case 1:
            return getText(object);
        case 2:
            return businessAssignment.getComment();
        }
        return null;
    }

}
