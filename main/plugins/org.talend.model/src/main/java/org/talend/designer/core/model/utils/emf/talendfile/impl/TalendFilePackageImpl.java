/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.designer.core.model.utils.emf.talendfile.impl;

import java.util.Map;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;
import org.talend.designer.core.model.utils.emf.talendfile.ColumnType;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.DocumentRoot;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.ItemInforType;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.designer.core.model.utils.emf.talendfile.LogToDatabaseType;
import org.talend.designer.core.model.utils.emf.talendfile.LogToFileType;
import org.talend.designer.core.model.utils.emf.talendfile.LogToStdOutType;
import org.talend.designer.core.model.utils.emf.talendfile.LogsType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeContainerType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.NoteType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RequiredType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.SubjobType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class TalendFilePackageImpl extends EPackageImpl implements TalendFilePackage {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass columnTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass connectionTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass contextParameterTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass contextTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass documentRootEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass elementParameterTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass elementValueTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass jobTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass logsTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass logToDatabaseTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass logToFileTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass logToStdOutTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass nodeTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass noteTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass parametersTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass processTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass requiredTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass subjobTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass itemInforTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractExternalDataEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass routinesParameterTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass nodeContainerTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass screenshotsMapEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass additionalFieldMapEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry
     * EPackage.Registry} by the package package URI value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also
     * performs initialization of the package, or returns the registered package, if one already exists. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage#eNS_URI
     * @see #init()
     * @generated
     */
    private TalendFilePackageImpl() {
        super(eNS_URI, TalendFileFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     *
     * <p>This method is used to initialize {@link TalendFilePackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static TalendFilePackage init() {
        if (isInited) return (TalendFilePackage)EPackage.Registry.INSTANCE.getEPackage(TalendFilePackage.eNS_URI);

        // Obtain or create and register package
        Object registeredTalendFilePackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        TalendFilePackageImpl theTalendFilePackage = registeredTalendFilePackage instanceof TalendFilePackageImpl ? (TalendFilePackageImpl)registeredTalendFilePackage : new TalendFilePackageImpl();

        isInited = true;

        // Initialize simple dependencies
        XMLTypePackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theTalendFilePackage.createPackageContents();

        // Initialize created meta-data
        theTalendFilePackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theTalendFilePackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(TalendFilePackage.eNS_URI, theTalendFilePackage);
        return theTalendFilePackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getColumnType() {
        return columnTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_Comment() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_DefaultValue() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_Key() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_Length() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_Name() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_Nullable() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_OriginalDbColumnName() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_Pattern() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_Precision() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_SourceType() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_Type() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_RelatedEntity() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_RelationshipType() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_OriginalLength() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getColumnType_AdditionalField() {
        return (EReference)columnTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getColumnType_UsefulColumn() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getConnectionType() {
        return connectionTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getConnectionType_ElementParameter() {
        return (EReference)connectionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_ConnectorName() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_Label() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_LineStyle() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_MergeOrder() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_Metaname() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_OffsetLabelX() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_OffsetLabelY() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_OutputId() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_Source() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConnectionType_Target() {
        return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getContextParameterType() {
        return contextParameterTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextParameterType_Comment() {
        return (EAttribute)contextParameterTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextParameterType_Name() {
        return (EAttribute)contextParameterTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextParameterType_Prompt() {
        return (EAttribute)contextParameterTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextParameterType_PromptNeeded() {
        return (EAttribute)contextParameterTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextParameterType_RepositoryContextId() {
        return (EAttribute)contextParameterTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextParameterType_Type() {
        return (EAttribute)contextParameterTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextParameterType_Value() {
        return (EAttribute)contextParameterTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextParameterType_InternalId() {
        return (EAttribute)contextParameterTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getContextType() {
        return contextTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getContextType_ContextParameter() {
        return (EReference)contextTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextType_ConfirmationNeeded() {
        return (EAttribute)contextTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContextType_Name() {
        return (EAttribute)contextTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getDocumentRoot() {
        return documentRootEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Mixed() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XMLNSPrefixMap() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XSISchemaLocation() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Connection() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Context() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ElementParameter() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Node() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Note() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Parameters() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Process() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Required() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Subjob() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getElementParameterType() {
        return elementParameterTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getElementParameterType_ElementValue() {
        return (EReference)elementParameterTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementParameterType_ContextMode() {
        return (EAttribute)elementParameterTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementParameterType_Show() {
        return (EAttribute)elementParameterTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementParameterType_Field() {
        return (EAttribute)elementParameterTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementParameterType_Name() {
        return (EAttribute)elementParameterTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementParameterType_Value() {
        return (EAttribute)elementParameterTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getElementValueType() {
        return elementValueTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementValueType_ElementRef() {
        return (EAttribute)elementValueTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementValueType_Value() {
        return (EAttribute)elementValueTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementValueType_Type() {
        return (EAttribute)elementValueTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementValueType_HexValue() {
        return (EAttribute)elementValueTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getJobType() {
        return jobTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJobType_Context() {
        return (EAttribute)jobTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJobType_Name() {
        return (EAttribute)jobTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getLogsType() {
        return logsTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getLogsType_LogToFile() {
        return (EReference)logsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getLogsType_LogToDatabase() {
        return (EReference)logsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getLogsType_LogToStdOut() {
        return (EReference)logsTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getLogToDatabaseType() {
        return logToDatabaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLogToDatabaseType_Database() {
        return (EAttribute)logToDatabaseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLogToDatabaseType_Level() {
        return (EAttribute)logToDatabaseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLogToDatabaseType_Selected() {
        return (EAttribute)logToDatabaseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getLogToFileType() {
        return logToFileTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLogToFileType_Filename() {
        return (EAttribute)logToFileTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLogToFileType_Level() {
        return (EAttribute)logToFileTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLogToFileType_Selected() {
        return (EAttribute)logToFileTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getLogToStdOutType() {
        return logToStdOutTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLogToStdOutType_Level() {
        return (EAttribute)logToStdOutTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLogToStdOutType_Selected() {
        return (EAttribute)logToStdOutTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getMetadataType() {
        return metadataTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getMetadataType_Column() {
        return (EReference)metadataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataType_Comment() {
        return (EAttribute)metadataTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataType_Connector() {
        return (EAttribute)metadataTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataType_Label() {
        return (EAttribute)metadataTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataType_Name() {
        return (EAttribute)metadataTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataType_Source() {
        return (EAttribute)metadataTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataType_Type() {
        return (EAttribute)metadataTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMetadataType_AdditionalProperties() {
        return (EReference)metadataTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getNodeType() {
        return nodeTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeType_ElementParameter() {
        return (EReference)nodeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeType_Metadata() {
        return (EReference)nodeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_BinaryData() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_StringData() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_ComponentName() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_ComponentVersion() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_OffsetLabelX() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_OffsetLabelY() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_PosX() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_PosY() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_SizeX() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_SizeY() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNodeType_Screenshot() {
        return (EAttribute)nodeTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeType_NodeData() {
        return (EReference)nodeTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeType_NodeContainer() {
        return (EReference)nodeTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getNoteType() {
        return noteTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNoteType_Opaque() {
        return (EAttribute)noteTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNoteType_PosX() {
        return (EAttribute)noteTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNoteType_PosY() {
        return (EAttribute)noteTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNoteType_SizeHeight() {
        return (EAttribute)noteTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNoteType_SizeWidth() {
        return (EAttribute)noteTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNoteType_Text() {
        return (EAttribute)noteTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getNoteType_ElementParameter() {
        return (EReference)noteTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getParametersType() {
        return parametersTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getParametersType_ElementParameter() {
        return (EReference)parametersTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParametersType_RoutinesParameter() {
        return (EReference)parametersTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getProcessType() {
        return processTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_Description() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_Required() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_Context() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_Parameters() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_Node() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_Connection() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_Note() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_Subjob() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_Screenshot() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_Screenshots() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_RoutinesDependencies() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_JobType() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_Framework() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessType_Logs() {
        return (EReference)processTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_Author() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_Comment() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_DefaultContext() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_Name() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_Purpose() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_RepositoryContextId() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_Status() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessType_Version() {
        return (EAttribute)processTypeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getRequiredType() {
        return requiredTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getRequiredType_Job() {
        return (EReference)requiredTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getSubjobType() {
        return subjobTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EReference getSubjobType_ElementParameter() {
        return (EReference)subjobTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getItemInforType() {
        return itemInforTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getItemInforType_IdOrName() {
        return (EAttribute)itemInforTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getItemInforType_System() {
        return (EAttribute)itemInforTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractExternalData() {
        return abstractExternalDataEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getRoutinesParameterType() {
        return routinesParameterTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRoutinesParameterType_Id() {
        return (EAttribute)routinesParameterTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRoutinesParameterType_Name() {
        return (EAttribute)routinesParameterTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNodeContainerType() {
        return nodeContainerTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeContainerType_ElementParameter() {
        return (EReference)nodeContainerTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getScreenshotsMap() {
        return screenshotsMapEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getScreenshotsMap_Key() {
        return (EAttribute)screenshotsMapEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getScreenshotsMap_Value() {
        return (EAttribute)screenshotsMapEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAdditionalFieldMap() {
        return additionalFieldMapEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAdditionalFieldMap_Key() {
        return (EAttribute)additionalFieldMapEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAdditionalFieldMap_Value() {
        return (EAttribute)additionalFieldMapEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TalendFileFactory getTalendFileFactory() {
        return (TalendFileFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        columnTypeEClass = createEClass(COLUMN_TYPE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__COMMENT);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__DEFAULT_VALUE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__KEY);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__LENGTH);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__NAME);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__NULLABLE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__ORIGINAL_DB_COLUMN_NAME);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__PATTERN);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__PRECISION);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__SOURCE_TYPE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__TYPE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__RELATED_ENTITY);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__RELATIONSHIP_TYPE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__ORIGINAL_LENGTH);
        createEReference(columnTypeEClass, COLUMN_TYPE__ADDITIONAL_FIELD);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__USEFUL_COLUMN);

        connectionTypeEClass = createEClass(CONNECTION_TYPE);
        createEReference(connectionTypeEClass, CONNECTION_TYPE__ELEMENT_PARAMETER);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__CONNECTOR_NAME);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__LABEL);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__LINE_STYLE);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__MERGE_ORDER);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__METANAME);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__OFFSET_LABEL_X);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__OFFSET_LABEL_Y);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__OUTPUT_ID);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__SOURCE);
        createEAttribute(connectionTypeEClass, CONNECTION_TYPE__TARGET);

        contextParameterTypeEClass = createEClass(CONTEXT_PARAMETER_TYPE);
        createEAttribute(contextParameterTypeEClass, CONTEXT_PARAMETER_TYPE__COMMENT);
        createEAttribute(contextParameterTypeEClass, CONTEXT_PARAMETER_TYPE__NAME);
        createEAttribute(contextParameterTypeEClass, CONTEXT_PARAMETER_TYPE__PROMPT);
        createEAttribute(contextParameterTypeEClass, CONTEXT_PARAMETER_TYPE__PROMPT_NEEDED);
        createEAttribute(contextParameterTypeEClass, CONTEXT_PARAMETER_TYPE__REPOSITORY_CONTEXT_ID);
        createEAttribute(contextParameterTypeEClass, CONTEXT_PARAMETER_TYPE__TYPE);
        createEAttribute(contextParameterTypeEClass, CONTEXT_PARAMETER_TYPE__VALUE);
        createEAttribute(contextParameterTypeEClass, CONTEXT_PARAMETER_TYPE__INTERNAL_ID);

        contextTypeEClass = createEClass(CONTEXT_TYPE);
        createEReference(contextTypeEClass, CONTEXT_TYPE__CONTEXT_PARAMETER);
        createEAttribute(contextTypeEClass, CONTEXT_TYPE__CONFIRMATION_NEEDED);
        createEAttribute(contextTypeEClass, CONTEXT_TYPE__NAME);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CONNECTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CONTEXT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ELEMENT_PARAMETER);
        createEReference(documentRootEClass, DOCUMENT_ROOT__NODE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__NOTE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PARAMETERS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROCESS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__REQUIRED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SUBJOB);

        elementParameterTypeEClass = createEClass(ELEMENT_PARAMETER_TYPE);
        createEReference(elementParameterTypeEClass, ELEMENT_PARAMETER_TYPE__ELEMENT_VALUE);
        createEAttribute(elementParameterTypeEClass, ELEMENT_PARAMETER_TYPE__FIELD);
        createEAttribute(elementParameterTypeEClass, ELEMENT_PARAMETER_TYPE__NAME);
        createEAttribute(elementParameterTypeEClass, ELEMENT_PARAMETER_TYPE__VALUE);
        createEAttribute(elementParameterTypeEClass, ELEMENT_PARAMETER_TYPE__CONTEXT_MODE);
        createEAttribute(elementParameterTypeEClass, ELEMENT_PARAMETER_TYPE__SHOW);

        elementValueTypeEClass = createEClass(ELEMENT_VALUE_TYPE);
        createEAttribute(elementValueTypeEClass, ELEMENT_VALUE_TYPE__ELEMENT_REF);
        createEAttribute(elementValueTypeEClass, ELEMENT_VALUE_TYPE__VALUE);
        createEAttribute(elementValueTypeEClass, ELEMENT_VALUE_TYPE__TYPE);
        createEAttribute(elementValueTypeEClass, ELEMENT_VALUE_TYPE__HEX_VALUE);

        jobTypeEClass = createEClass(JOB_TYPE);
        createEAttribute(jobTypeEClass, JOB_TYPE__CONTEXT);
        createEAttribute(jobTypeEClass, JOB_TYPE__NAME);

        logsTypeEClass = createEClass(LOGS_TYPE);
        createEReference(logsTypeEClass, LOGS_TYPE__LOG_TO_FILE);
        createEReference(logsTypeEClass, LOGS_TYPE__LOG_TO_DATABASE);
        createEReference(logsTypeEClass, LOGS_TYPE__LOG_TO_STD_OUT);

        logToDatabaseTypeEClass = createEClass(LOG_TO_DATABASE_TYPE);
        createEAttribute(logToDatabaseTypeEClass, LOG_TO_DATABASE_TYPE__DATABASE);
        createEAttribute(logToDatabaseTypeEClass, LOG_TO_DATABASE_TYPE__LEVEL);
        createEAttribute(logToDatabaseTypeEClass, LOG_TO_DATABASE_TYPE__SELECTED);

        logToFileTypeEClass = createEClass(LOG_TO_FILE_TYPE);
        createEAttribute(logToFileTypeEClass, LOG_TO_FILE_TYPE__FILENAME);
        createEAttribute(logToFileTypeEClass, LOG_TO_FILE_TYPE__LEVEL);
        createEAttribute(logToFileTypeEClass, LOG_TO_FILE_TYPE__SELECTED);

        logToStdOutTypeEClass = createEClass(LOG_TO_STD_OUT_TYPE);
        createEAttribute(logToStdOutTypeEClass, LOG_TO_STD_OUT_TYPE__LEVEL);
        createEAttribute(logToStdOutTypeEClass, LOG_TO_STD_OUT_TYPE__SELECTED);

        metadataTypeEClass = createEClass(METADATA_TYPE);
        createEReference(metadataTypeEClass, METADATA_TYPE__COLUMN);
        createEAttribute(metadataTypeEClass, METADATA_TYPE__COMMENT);
        createEAttribute(metadataTypeEClass, METADATA_TYPE__CONNECTOR);
        createEAttribute(metadataTypeEClass, METADATA_TYPE__LABEL);
        createEAttribute(metadataTypeEClass, METADATA_TYPE__NAME);
        createEAttribute(metadataTypeEClass, METADATA_TYPE__SOURCE);
        createEAttribute(metadataTypeEClass, METADATA_TYPE__TYPE);
        createEReference(metadataTypeEClass, METADATA_TYPE__ADDITIONAL_PROPERTIES);

        nodeTypeEClass = createEClass(NODE_TYPE);
        createEReference(nodeTypeEClass, NODE_TYPE__ELEMENT_PARAMETER);
        createEReference(nodeTypeEClass, NODE_TYPE__METADATA);
        createEAttribute(nodeTypeEClass, NODE_TYPE__BINARY_DATA);
        createEAttribute(nodeTypeEClass, NODE_TYPE__STRING_DATA);
        createEAttribute(nodeTypeEClass, NODE_TYPE__COMPONENT_NAME);
        createEAttribute(nodeTypeEClass, NODE_TYPE__COMPONENT_VERSION);
        createEAttribute(nodeTypeEClass, NODE_TYPE__OFFSET_LABEL_X);
        createEAttribute(nodeTypeEClass, NODE_TYPE__OFFSET_LABEL_Y);
        createEAttribute(nodeTypeEClass, NODE_TYPE__POS_X);
        createEAttribute(nodeTypeEClass, NODE_TYPE__POS_Y);
        createEAttribute(nodeTypeEClass, NODE_TYPE__SIZE_X);
        createEAttribute(nodeTypeEClass, NODE_TYPE__SIZE_Y);
        createEAttribute(nodeTypeEClass, NODE_TYPE__SCREENSHOT);
        createEReference(nodeTypeEClass, NODE_TYPE__NODE_DATA);
        createEReference(nodeTypeEClass, NODE_TYPE__NODE_CONTAINER);

        noteTypeEClass = createEClass(NOTE_TYPE);
        createEAttribute(noteTypeEClass, NOTE_TYPE__OPAQUE);
        createEAttribute(noteTypeEClass, NOTE_TYPE__POS_X);
        createEAttribute(noteTypeEClass, NOTE_TYPE__POS_Y);
        createEAttribute(noteTypeEClass, NOTE_TYPE__SIZE_HEIGHT);
        createEAttribute(noteTypeEClass, NOTE_TYPE__SIZE_WIDTH);
        createEAttribute(noteTypeEClass, NOTE_TYPE__TEXT);
        createEReference(noteTypeEClass, NOTE_TYPE__ELEMENT_PARAMETER);

        parametersTypeEClass = createEClass(PARAMETERS_TYPE);
        createEReference(parametersTypeEClass, PARAMETERS_TYPE__ELEMENT_PARAMETER);
        createEReference(parametersTypeEClass, PARAMETERS_TYPE__ROUTINES_PARAMETER);

        processTypeEClass = createEClass(PROCESS_TYPE);
        createEAttribute(processTypeEClass, PROCESS_TYPE__DESCRIPTION);
        createEReference(processTypeEClass, PROCESS_TYPE__REQUIRED);
        createEReference(processTypeEClass, PROCESS_TYPE__CONTEXT);
        createEReference(processTypeEClass, PROCESS_TYPE__PARAMETERS);
        createEReference(processTypeEClass, PROCESS_TYPE__NODE);
        createEReference(processTypeEClass, PROCESS_TYPE__CONNECTION);
        createEReference(processTypeEClass, PROCESS_TYPE__NOTE);
        createEReference(processTypeEClass, PROCESS_TYPE__LOGS);
        createEAttribute(processTypeEClass, PROCESS_TYPE__AUTHOR);
        createEAttribute(processTypeEClass, PROCESS_TYPE__COMMENT);
        createEAttribute(processTypeEClass, PROCESS_TYPE__DEFAULT_CONTEXT);
        createEAttribute(processTypeEClass, PROCESS_TYPE__NAME);
        createEAttribute(processTypeEClass, PROCESS_TYPE__PURPOSE);
        createEAttribute(processTypeEClass, PROCESS_TYPE__REPOSITORY_CONTEXT_ID);
        createEAttribute(processTypeEClass, PROCESS_TYPE__STATUS);
        createEAttribute(processTypeEClass, PROCESS_TYPE__VERSION);
        createEReference(processTypeEClass, PROCESS_TYPE__SUBJOB);
        createEAttribute(processTypeEClass, PROCESS_TYPE__SCREENSHOT);
        createEReference(processTypeEClass, PROCESS_TYPE__SCREENSHOTS);
        createEReference(processTypeEClass, PROCESS_TYPE__ROUTINES_DEPENDENCIES);
        createEAttribute(processTypeEClass, PROCESS_TYPE__JOB_TYPE);
        createEAttribute(processTypeEClass, PROCESS_TYPE__FRAMEWORK);

        requiredTypeEClass = createEClass(REQUIRED_TYPE);
        createEReference(requiredTypeEClass, REQUIRED_TYPE__JOB);

        subjobTypeEClass = createEClass(SUBJOB_TYPE);
        createEReference(subjobTypeEClass, SUBJOB_TYPE__ELEMENT_PARAMETER);

        itemInforTypeEClass = createEClass(ITEM_INFOR_TYPE);
        createEAttribute(itemInforTypeEClass, ITEM_INFOR_TYPE__ID_OR_NAME);
        createEAttribute(itemInforTypeEClass, ITEM_INFOR_TYPE__SYSTEM);

        abstractExternalDataEClass = createEClass(ABSTRACT_EXTERNAL_DATA);

        routinesParameterTypeEClass = createEClass(ROUTINES_PARAMETER_TYPE);
        createEAttribute(routinesParameterTypeEClass, ROUTINES_PARAMETER_TYPE__ID);
        createEAttribute(routinesParameterTypeEClass, ROUTINES_PARAMETER_TYPE__NAME);

        nodeContainerTypeEClass = createEClass(NODE_CONTAINER_TYPE);
        createEReference(nodeContainerTypeEClass, NODE_CONTAINER_TYPE__ELEMENT_PARAMETER);

        screenshotsMapEClass = createEClass(SCREENSHOTS_MAP);
        createEAttribute(screenshotsMapEClass, SCREENSHOTS_MAP__KEY);
        createEAttribute(screenshotsMapEClass, SCREENSHOTS_MAP__VALUE);

        additionalFieldMapEClass = createEClass(ADDITIONAL_FIELD_MAP);
        createEAttribute(additionalFieldMapEClass, ADDITIONAL_FIELD_MAP__KEY);
        createEAttribute(additionalFieldMapEClass, ADDITIONAL_FIELD_MAP__VALUE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

        // Add supertypes to classes

        // Initialize classes and features; add operations and parameters
        initEClass(columnTypeEClass, ColumnType.class, "ColumnType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getColumnType_Comment(), theXMLTypePackage.getString(), "comment", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_DefaultValue(), theXMLTypePackage.getString(), "defaultValue", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_Key(), theXMLTypePackage.getBoolean(), "key", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_Length(), theXMLTypePackage.getInt(), "length", "-1", 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_Nullable(), theXMLTypePackage.getBoolean(), "nullable", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_OriginalDbColumnName(), theXMLTypePackage.getString(), "originalDbColumnName", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_Pattern(), theXMLTypePackage.getString(), "pattern", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_Precision(), theXMLTypePackage.getInt(), "precision", "-1", 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_SourceType(), theXMLTypePackage.getString(), "sourceType", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_Type(), theXMLTypePackage.getString(), "type", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_RelatedEntity(), theXMLTypePackage.getString(), "relatedEntity", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_RelationshipType(), theXMLTypePackage.getString(), "relationshipType", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_OriginalLength(), theXMLTypePackage.getInt(), "originalLength", null, 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getColumnType_AdditionalField(), this.getAdditionalFieldMap(), null, "additionalField", null, 0, -1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColumnType_UsefulColumn(), ecorePackage.getEBoolean(), "usefulColumn", "true", 0, 1, ColumnType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(connectionTypeEClass, ConnectionType.class, "ConnectionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConnectionType_ElementParameter(), this.getElementParameterType(), null, "elementParameter", null, 0, -1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_ConnectorName(), theXMLTypePackage.getString(), "connectorName", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_Label(), theXMLTypePackage.getString(), "label", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_LineStyle(), theXMLTypePackage.getInt(), "lineStyle", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_MergeOrder(), theXMLTypePackage.getInt(), "mergeOrder", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_Metaname(), theXMLTypePackage.getString(), "metaname", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_OffsetLabelX(), theXMLTypePackage.getInt(), "offsetLabelX", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_OffsetLabelY(), theXMLTypePackage.getInt(), "offsetLabelY", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_OutputId(), theXMLTypePackage.getInt(), "outputId", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_Source(), theXMLTypePackage.getString(), "source", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConnectionType_Target(), theXMLTypePackage.getString(), "target", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(contextParameterTypeEClass, ContextParameterType.class, "ContextParameterType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getContextParameterType_Comment(), theXMLTypePackage.getString(), "comment", null, 0, 1, ContextParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContextParameterType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ContextParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContextParameterType_Prompt(), theXMLTypePackage.getString(), "prompt", null, 0, 1, ContextParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContextParameterType_PromptNeeded(), theXMLTypePackage.getBoolean(), "promptNeeded", null, 0, 1, ContextParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContextParameterType_RepositoryContextId(), theXMLTypePackage.getString(), "repositoryContextId", null, 0, 1, ContextParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContextParameterType_Type(), theXMLTypePackage.getString(), "type", null, 0, 1, ContextParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContextParameterType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ContextParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContextParameterType_InternalId(), theXMLTypePackage.getString(), "internalId", null, 0, 1, ContextParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(contextTypeEClass, ContextType.class, "ContextType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getContextType_ContextParameter(), this.getContextParameterType(), null, "contextParameter", null, 0, -1, ContextType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContextType_ConfirmationNeeded(), theXMLTypePackage.getBoolean(), "confirmationNeeded", null, 0, 1, ContextType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContextType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ContextType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Connection(), this.getConnectionType(), null, "connection", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Context(), this.getContextType(), null, "context", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ElementParameter(), this.getElementParameterType(), null, "elementParameter", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Node(), this.getNodeType(), null, "node", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Note(), this.getNoteType(), null, "note", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Parameters(), this.getParametersType(), null, "parameters", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Process(), this.getProcessType(), null, "process", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Required(), this.getRequiredType(), null, "required", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Subjob(), this.getSubjobType(), null, "subjob", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(elementParameterTypeEClass, ElementParameterType.class, "ElementParameterType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getElementParameterType_ElementValue(), this.getElementValueType(), null, "elementValue", null, 0, -1, ElementParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementParameterType_Field(), theXMLTypePackage.getString(), "field", null, 0, 1, ElementParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementParameterType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ElementParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementParameterType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ElementParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementParameterType_ContextMode(), theXMLTypePackage.getBoolean(), "contextMode", null, 0, 1, ElementParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementParameterType_Show(), theXMLTypePackage.getBoolean(), "show", "true", 0, 1, ElementParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(elementValueTypeEClass, ElementValueType.class, "ElementValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getElementValueType_ElementRef(), theXMLTypePackage.getString(), "elementRef", null, 0, 1, ElementValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementValueType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ElementValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementValueType_Type(), theXMLTypePackage.getString(), "type", null, 0, 1, ElementValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementValueType_HexValue(), theXMLTypePackage.getBoolean(), "hexValue", "false", 0, 1, ElementValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(jobTypeEClass, JobType.class, "JobType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getJobType_Context(), theXMLTypePackage.getString(), "context", null, 0, 1, JobType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJobType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, JobType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(logsTypeEClass, LogsType.class, "LogsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLogsType_LogToFile(), this.getLogToFileType(), null, "logToFile", null, 1, 1, LogsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLogsType_LogToDatabase(), this.getLogToDatabaseType(), null, "logToDatabase", null, 1, 1, LogsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLogsType_LogToStdOut(), this.getLogToStdOutType(), null, "logToStdOut", null, 1, 1, LogsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(logToDatabaseTypeEClass, LogToDatabaseType.class, "LogToDatabaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLogToDatabaseType_Database(), theXMLTypePackage.getString(), "database", null, 0, 1, LogToDatabaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLogToDatabaseType_Level(), theXMLTypePackage.getInt(), "level", null, 0, 1, LogToDatabaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLogToDatabaseType_Selected(), theXMLTypePackage.getBoolean(), "selected", null, 0, 1, LogToDatabaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(logToFileTypeEClass, LogToFileType.class, "LogToFileType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLogToFileType_Filename(), theXMLTypePackage.getString(), "filename", null, 0, 1, LogToFileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLogToFileType_Level(), theXMLTypePackage.getInt(), "level", null, 0, 1, LogToFileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLogToFileType_Selected(), theXMLTypePackage.getBoolean(), "selected", null, 0, 1, LogToFileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(logToStdOutTypeEClass, LogToStdOutType.class, "LogToStdOutType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLogToStdOutType_Level(), theXMLTypePackage.getInt(), "level", null, 0, 1, LogToStdOutType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLogToStdOutType_Selected(), theXMLTypePackage.getBoolean(), "selected", null, 0, 1, LogToStdOutType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(metadataTypeEClass, MetadataType.class, "MetadataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMetadataType_Column(), this.getColumnType(), null, "column", null, 0, -1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataType_Comment(), theXMLTypePackage.getString(), "comment", null, 0, 1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataType_Connector(), theXMLTypePackage.getString(), "connector", null, 0, 1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataType_Label(), theXMLTypePackage.getString(), "label", null, 0, 1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataType_Source(), theXMLTypePackage.getString(), "source", null, 0, 1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataType_Type(), ecorePackage.getEString(), "type", null, 0, 1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMetadataType_AdditionalProperties(), this.getAdditionalFieldMap(), null, "additionalProperties", null, 0, -1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(nodeTypeEClass, NodeType.class, "NodeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getNodeType_ElementParameter(), this.getElementParameterType(), null, "elementParameter", null, 1, -1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getNodeType_Metadata(), this.getMetadataType(), null, "metadata", null, 0, -1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_BinaryData(), theXMLTypePackage.getBase64Binary(), "binaryData", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_StringData(), theXMLTypePackage.getString(), "stringData", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_ComponentName(), theXMLTypePackage.getString(), "componentName", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_ComponentVersion(), theXMLTypePackage.getString(), "componentVersion", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_OffsetLabelX(), theXMLTypePackage.getInt(), "offsetLabelX", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_OffsetLabelY(), theXMLTypePackage.getInt(), "offsetLabelY", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_PosX(), theXMLTypePackage.getInt(), "posX", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_PosY(), theXMLTypePackage.getInt(), "posY", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_SizeX(), theXMLTypePackage.getInt(), "sizeX", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_SizeY(), theXMLTypePackage.getInt(), "sizeY", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNodeType_Screenshot(), theXMLTypePackage.getBase64Binary(), "screenshot", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getNodeType_NodeData(), this.getAbstractExternalData(), null, "nodeData", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getNodeType_NodeContainer(), this.getNodeContainerType(), null, "nodeContainer", null, 0, 1, NodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(noteTypeEClass, NoteType.class, "NoteType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getNoteType_Opaque(), theXMLTypePackage.getBoolean(), "opaque", null, 0, 1, NoteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNoteType_PosX(), theXMLTypePackage.getInt(), "posX", null, 0, 1, NoteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNoteType_PosY(), theXMLTypePackage.getInt(), "posY", null, 0, 1, NoteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNoteType_SizeHeight(), theXMLTypePackage.getInt(), "sizeHeight", null, 0, 1, NoteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNoteType_SizeWidth(), theXMLTypePackage.getInt(), "sizeWidth", null, 0, 1, NoteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNoteType_Text(), theXMLTypePackage.getString(), "text", null, 0, 1, NoteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getNoteType_ElementParameter(), this.getElementParameterType(), null, "elementParameter", null, 0, -1, NoteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(parametersTypeEClass, ParametersType.class, "ParametersType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getParametersType_ElementParameter(), this.getElementParameterType(), null, "elementParameter", null, 1, -1, ParametersType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getParametersType_RoutinesParameter(), this.getRoutinesParameterType(), null, "routinesParameter", null, 0, -1, ParametersType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processTypeEClass, ProcessType.class, "ProcessType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getProcessType_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_Required(), this.getRequiredType(), null, "required", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_Context(), this.getContextType(), null, "context", null, 0, -1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_Parameters(), this.getParametersType(), null, "parameters", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_Node(), this.getNodeType(), null, "node", null, 0, -1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_Connection(), this.getConnectionType(), null, "connection", null, 0, -1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_Note(), this.getNoteType(), null, "note", null, 0, -1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_Logs(), this.getLogsType(), null, "logs", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_Author(), theXMLTypePackage.getString(), "author", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_Comment(), theXMLTypePackage.getString(), "comment", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_DefaultContext(), theXMLTypePackage.getString(), "defaultContext", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_Purpose(), theXMLTypePackage.getString(), "purpose", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_RepositoryContextId(), theXMLTypePackage.getString(), "repositoryContextId", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_Status(), theXMLTypePackage.getString(), "status", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_Version(), theXMLTypePackage.getString(), "version", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_Subjob(), this.getSubjobType(), null, "subjob", null, 0, -1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_Screenshot(), theXMLTypePackage.getBase64Binary(), "screenshot", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_Screenshots(), this.getScreenshotsMap(), null, "screenshots", null, 0, -1, ProcessType.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessType_RoutinesDependencies(), this.getItemInforType(), null, "routinesDependencies", null, 0, -1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_JobType(), theXMLTypePackage.getString(), "jobType", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessType_Framework(), theXMLTypePackage.getString(), "framework", null, 0, 1, ProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(requiredTypeEClass, RequiredType.class, "RequiredType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRequiredType_Job(), this.getJobType(), null, "job", null, 0, -1, RequiredType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(subjobTypeEClass, SubjobType.class, "SubjobType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSubjobType_ElementParameter(), this.getElementParameterType(), null, "elementParameter", null, 1, -1, SubjobType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(itemInforTypeEClass, ItemInforType.class, "ItemInforType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getItemInforType_IdOrName(), ecorePackage.getEString(), "idOrName", null, 0, 1, ItemInforType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getItemInforType_System(), ecorePackage.getEBoolean(), "system", null, 0, 1, ItemInforType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(abstractExternalDataEClass, AbstractExternalData.class, "AbstractExternalData", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(routinesParameterTypeEClass, RoutinesParameterType.class, "RoutinesParameterType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRoutinesParameterType_Id(), ecorePackage.getEString(), "id", null, 0, 1, RoutinesParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRoutinesParameterType_Name(), ecorePackage.getEString(), "name", null, 0, 1, RoutinesParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(nodeContainerTypeEClass, NodeContainerType.class, "NodeContainerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getNodeContainerType_ElementParameter(), this.getElementParameterType(), null, "elementParameter", null, 1, -1, NodeContainerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(screenshotsMapEClass, Map.Entry.class, "ScreenshotsMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getScreenshotsMap_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getScreenshotsMap_Value(), theXMLTypePackage.getBase64Binary(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(additionalFieldMapEClass, Map.Entry.class, "AdditionalFieldMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAdditionalFieldMap_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAdditionalFieldMap_Value(), ecorePackage.getEString(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
        // ExtendedMetaData
        createExtendedMetaData_1Annotations();
        // MapEntry
        createMapEntryAnnotations();
    }

    /**
     * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @generated
     */
    protected void createExtendedMetaDataAnnotations() {
        String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
        addAnnotation
          (this,
           source,
           new String[] {
               "qualified", "false"
           });
        addAnnotation
          (columnTypeEClass,
           source,
           new String[] {
               "name", "Column_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getColumnType_Comment(),
           source,
           new String[] {
               "kind", "element",
               "name", "Comment",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_DefaultValue(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "defaultValue",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_Key(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "key",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_Length(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "length",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_Name(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "name",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_Nullable(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "nullable",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_OriginalDbColumnName(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "originalDbColumnName",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_Pattern(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "pattern",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_Precision(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "precision",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_SourceType(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "sourceType",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_Type(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "type",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_RelatedEntity(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "relatedEntity",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getColumnType_RelationshipType(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "relationshipType",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (connectionTypeEClass,
           source,
           new String[] {
               "name", "Connection_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getConnectionType_ElementParameter(),
           source,
           new String[] {
               "kind", "element",
               "name", "ElementParameter",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_ConnectorName(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "connectorName",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_Label(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "label",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_LineStyle(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "lineStyle",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_MergeOrder(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "mergeOrder",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_Metaname(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "metaname",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_OffsetLabelX(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "offsetLabelX",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_OffsetLabelY(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "offsetLabelY",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_OutputId(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "outputId",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_Source(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "source",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getConnectionType_Target(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "target",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (contextParameterTypeEClass,
           source,
           new String[] {
               "name", "ContextParameter_._type",
               "kind", "empty"
           });
        addAnnotation
          (getContextParameterType_Comment(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "comment",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getContextParameterType_Name(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "name",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getContextParameterType_Prompt(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "prompt",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getContextParameterType_PromptNeeded(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "promptNeeded",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getContextParameterType_RepositoryContextId(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "repositoryContextId",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getContextParameterType_Type(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "type",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getContextParameterType_Value(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "value",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getContextParameterType_InternalId(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "inernalId",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (contextTypeEClass,
           source,
           new String[] {
               "name", "Context_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getContextType_ContextParameter(),
           source,
           new String[] {
               "kind", "element",
               "name", "ContextParameter",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getContextType_ConfirmationNeeded(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "confirmationNeeded",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getContextType_Name(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "name",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (documentRootEClass,
           source,
           new String[] {
               "name", "",
               "kind", "mixed"
           });
        addAnnotation
          (getDocumentRoot_Mixed(),
           source,
           new String[] {
               "kind", "elementWildcard",
               "name", ":mixed"
           });
        addAnnotation
          (getDocumentRoot_XMLNSPrefixMap(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "xmlns:prefix"
           });
        addAnnotation
          (getDocumentRoot_XSISchemaLocation(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "xsi:schemaLocation"
           });
        addAnnotation
          (getDocumentRoot_Connection(),
           source,
           new String[] {
               "kind", "element",
               "name", "Connection",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getDocumentRoot_Context(),
           source,
           new String[] {
               "kind", "element",
               "name", "Context",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getDocumentRoot_ElementParameter(),
           source,
           new String[] {
               "kind", "element",
               "name", "ElementParameter",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getDocumentRoot_Node(),
           source,
           new String[] {
               "kind", "element",
               "name", "Node",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getDocumentRoot_Note(),
           source,
           new String[] {
               "kind", "element",
               "name", "Note",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getDocumentRoot_Parameters(),
           source,
           new String[] {
               "kind", "element",
               "name", "Parameters",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getDocumentRoot_Process(),
           source,
           new String[] {
               "kind", "element",
               "name", "Process",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getDocumentRoot_Required(),
           source,
           new String[] {
               "kind", "element",
               "name", "Required",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getDocumentRoot_Subjob(),
           source,
           new String[] {
               "kind", "element",
               "name", "Subjob",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (elementParameterTypeEClass,
           source,
           new String[] {
               "name", "ElementParameter_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getElementParameterType_ElementValue(),
           source,
           new String[] {
               "kind", "element",
               "name", "ElementValue",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getElementParameterType_Field(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "field",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getElementParameterType_Name(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "name",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getElementParameterType_Value(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "value",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getElementParameterType_ContextMode(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "contextMode",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (elementValueTypeEClass,
           source,
           new String[] {
               "name", "ElementValue_._type",
               "kind", "empty"
           });
        addAnnotation
          (getElementValueType_ElementRef(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "elementRef",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getElementValueType_Value(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "value",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (jobTypeEClass,
           source,
           new String[] {
               "name", "Job_._type",
               "kind", "empty"
           });
        addAnnotation
          (getJobType_Context(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "context",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getJobType_Name(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "name",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (logsTypeEClass,
           source,
           new String[] {
               "name", "Logs_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getLogsType_LogToFile(),
           source,
           new String[] {
               "kind", "element",
               "name", "LogToFile",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getLogsType_LogToDatabase(),
           source,
           new String[] {
               "kind", "element",
               "name", "LogToDatabase",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getLogsType_LogToStdOut(),
           source,
           new String[] {
               "kind", "element",
               "name", "LogToStdOut",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (logToDatabaseTypeEClass,
           source,
           new String[] {
               "name", "LogToDatabase_._type",
               "kind", "empty"
           });
        addAnnotation
          (getLogToDatabaseType_Database(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "database",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getLogToDatabaseType_Level(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "level",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getLogToDatabaseType_Selected(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "selected",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (logToFileTypeEClass,
           source,
           new String[] {
               "name", "LogToFile_._type",
               "kind", "empty"
           });
        addAnnotation
          (getLogToFileType_Filename(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "filename",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getLogToFileType_Level(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "level",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getLogToFileType_Selected(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "selected",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (logToStdOutTypeEClass,
           source,
           new String[] {
               "name", "LogToStdOut_._type",
               "kind", "empty"
           });
        addAnnotation
          (getLogToStdOutType_Level(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "level",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getLogToStdOutType_Selected(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "selected",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (metadataTypeEClass,
           source,
           new String[] {
               "name", "Metadata_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getMetadataType_Column(),
           source,
           new String[] {
               "kind", "element",
               "name", "Column",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getMetadataType_Comment(),
           source,
           new String[] {
               "kind", "element",
               "name", "Comment",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getMetadataType_Connector(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "connector",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getMetadataType_Label(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "label",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getMetadataType_Name(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "name",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getMetadataType_Source(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "source",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (nodeTypeEClass,
           source,
           new String[] {
               "name", "Node_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getNodeType_ElementParameter(),
           source,
           new String[] {
               "kind", "element",
               "name", "ElementParameter",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_Metadata(),
           source,
           new String[] {
               "kind", "element",
               "name", "Metadata",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_BinaryData(),
           source,
           new String[] {
               "kind", "element",
               "name", "BinaryData",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_StringData(),
           source,
           new String[] {
               "kind", "element",
               "name", "StringData",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_ComponentName(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "componentName",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_ComponentVersion(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "componentVersion",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_OffsetLabelX(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "offsetLabelX",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_OffsetLabelY(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "offsetLabelY",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_PosX(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "posX",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_PosY(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "posY",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_SizeX(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "sizeX",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeType_SizeY(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "sizeY",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (noteTypeEClass,
           source,
           new String[] {
               "name", "Note_._type",
               "kind", "empty"
           });
        addAnnotation
          (getNoteType_Opaque(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "opaque",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNoteType_PosX(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "posX",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNoteType_PosY(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "posY",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNoteType_SizeHeight(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "sizeHeight",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNoteType_SizeWidth(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "sizeWidth",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNoteType_Text(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "text",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (parametersTypeEClass,
           source,
           new String[] {
               "name", "Parameters_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getParametersType_ElementParameter(),
           source,
           new String[] {
               "kind", "element",
               "name", "ElementParameter",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (processTypeEClass,
           source,
           new String[] {
               "name", "Process_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getProcessType_Description(),
           source,
           new String[] {
               "kind", "element",
               "name", "Description",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Required(),
           source,
           new String[] {
               "kind", "element",
               "name", "Required",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Context(),
           source,
           new String[] {
               "kind", "element",
               "name", "Context",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Parameters(),
           source,
           new String[] {
               "kind", "element",
               "name", "Parameters",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Node(),
           source,
           new String[] {
               "kind", "element",
               "name", "Node",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Connection(),
           source,
           new String[] {
               "kind", "element",
               "name", "Connection",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Note(),
           source,
           new String[] {
               "kind", "element",
               "name", "Note",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Logs(),
           source,
           new String[] {
               "kind", "element",
               "name", "Logs",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Author(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "author",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Comment(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "comment",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_DefaultContext(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "defaultContext",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Name(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "name",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Purpose(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "purpose",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_RepositoryContextId(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "repositoryContextId",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Status(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "status",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Version(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "version",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Subjob(),
           source,
           new String[] {
               "kind", "element",
               "name", "Subjob",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_JobType(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "name",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getProcessType_Framework(),
           source,
           new String[] {
               "kind", "attribute",
               "name", "name",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (requiredTypeEClass,
           source,
           new String[] {
               "name", "Required_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getRequiredType_Job(),
           source,
           new String[] {
               "kind", "element",
               "name", "Job",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (subjobTypeEClass,
           source,
           new String[] {
               "name", "Subjob_._type",
               "kind", "elementOnly"
           });
        addAnnotation
          (getSubjobType_ElementParameter(),
           source,
           new String[] {
               "kind", "element",
               "name", "ElementParameter",
               "namespace", "##targetNamespace"
           });
        addAnnotation
          (getNodeContainerType_ElementParameter(),
           source,
           new String[] {
               "kind", "element",
               "name", "ElementParameter",
               "namespace", "##targetNamespace"
           });
    }

    /**
     * Initializes the annotations for <b>ExtendedMetaData</b>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected void createExtendedMetaData_1Annotations() {
        String source = "ExtendedMetaData";
        addAnnotation
          (getNoteType_ElementParameter(),
           source,
           new String[] {
               "kind", "element",
               "name", "ElementParameterType",
               "namespace", "##targetNamespace"
           });
    }

    /**
     * Initializes the annotations for <b>MapEntry</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createMapEntryAnnotations() {
        String source = "MapEntry";
        addAnnotation
          (screenshotsMapEClass,
           source,
           new String[] {
           });
        addAnnotation
          (additionalFieldMapEClass,
           source,
           new String[] {
           });
    }

} // TalendFilePackageImpl
