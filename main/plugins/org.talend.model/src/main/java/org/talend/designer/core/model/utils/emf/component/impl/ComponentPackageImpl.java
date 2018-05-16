/**
 */
package org.talend.designer.core.model.utils.emf.component.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.talend.designer.core.model.utils.emf.component.ADVANCEDPARAMETERSType;
import org.talend.designer.core.model.utils.emf.component.ARGType;
import org.talend.designer.core.model.utils.emf.component.CODEGENERATIONType;
import org.talend.designer.core.model.utils.emf.component.COLUMNType;
import org.talend.designer.core.model.utils.emf.component.COMPONENTType;
import org.talend.designer.core.model.utils.emf.component.CONNECTORSType;
import org.talend.designer.core.model.utils.emf.component.CONNECTORType;
import org.talend.designer.core.model.utils.emf.component.ComponentFactory;
import org.talend.designer.core.model.utils.emf.component.ComponentPackage;
import org.talend.designer.core.model.utils.emf.component.DEFAULTType;
import org.talend.designer.core.model.utils.emf.component.DOCUMENTATIONType;
import org.talend.designer.core.model.utils.emf.component.DocumentRoot;
import org.talend.designer.core.model.utils.emf.component.FAMILIESType;
import org.talend.designer.core.model.utils.emf.component.FORMATType;
import org.talend.designer.core.model.utils.emf.component.HEADERType;
import org.talend.designer.core.model.utils.emf.component.IMPORTSType;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.component.INSTALLType;
import org.talend.designer.core.model.utils.emf.component.ITEMSType;
import org.talend.designer.core.model.utils.emf.component.ITEMType;
import org.talend.designer.core.model.utils.emf.component.JAVACOMMANDType;
import org.talend.designer.core.model.utils.emf.component.LINKTOType;
import org.talend.designer.core.model.utils.emf.component.PARAMETERSType;
import org.talend.designer.core.model.utils.emf.component.PARAMETERType;
import org.talend.designer.core.model.utils.emf.component.PLUGINDEPENDENCIESType;
import org.talend.designer.core.model.utils.emf.component.PLUGINDEPENDENCYType;
import org.talend.designer.core.model.utils.emf.component.RETURNSType;
import org.talend.designer.core.model.utils.emf.component.RETURNType;
import org.talend.designer.core.model.utils.emf.component.SQLTEMPLATESType;
import org.talend.designer.core.model.utils.emf.component.SQLTEMPLATEType;
import org.talend.designer.core.model.utils.emf.component.TABLEType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATEPARAMType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATESType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATEType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ComponentPackageImpl extends EPackageImpl implements ComponentPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass advancedparametersTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass argTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass codegenerationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass columnTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass connectorsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass connectorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass defaultTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass documentationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass documentRootEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass familiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass formatTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass headerTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass importsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass importTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass installTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass itemsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass itemTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass javacommandTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass linktoTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass parametersTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass parameterTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass plugindependenciesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass plugindependencyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass returnsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass returnTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sqltemplatesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sqltemplateTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tableTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass templateparamTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass templatesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass templateTypeEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.talend.designer.core.model.utils.emf.component.ComponentPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ComponentPackageImpl() {
        super(eNS_URI, ComponentFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link ComponentPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ComponentPackage init() {
        if (isInited) return (ComponentPackage)EPackage.Registry.INSTANCE.getEPackage(ComponentPackage.eNS_URI);

        // Obtain or create and register package
        ComponentPackageImpl theComponentPackage = (ComponentPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ComponentPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ComponentPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        XMLTypePackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theComponentPackage.createPackageContents();

        // Initialize created meta-data
        theComponentPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theComponentPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(ComponentPackage.eNS_URI, theComponentPackage);
        return theComponentPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getADVANCEDPARAMETERSType() {
        return advancedparametersTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getADVANCEDPARAMETERSType_PARAMETER() {
        return (EReference)advancedparametersTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getARGType() {
        return argTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getARGType_Value() {
        return (EAttribute)argTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCODEGENERATIONType() {
        return codegenerationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCODEGENERATIONType_TEMPLATES() {
        return (EReference)codegenerationTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCODEGENERATIONType_IMPORTS() {
        return (EReference)codegenerationTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCOLUMNType() {
        return columnTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_COMMENT() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_CUSTOM() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_DEFAULT() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_KEY() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_LENGTH() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_NAME() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_NULLABLE() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_PATTERN() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_PRECISION() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_READONLY() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_RELATEDENTITY() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_RELATIONSHIPTYPE() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_TYPE() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCOLUMNType_DBTYPE() {
        return (EAttribute)columnTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCOMPONENTType() {
        return componentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_HEADER() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_FAMILIES() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_DOCUMENTATION() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_CONNECTORS() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_SQLTEMPLATES() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_PARAMETERS() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_ADVANCEDPARAMETERS() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_CODEGENERATION() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_RETURNS() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCOMPONENTType_PLUGINDEPENDENCIES() {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCONNECTORSType() {
        return connectorsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCONNECTORSType_CONNECTOR() {
        return (EReference)connectorsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCONNECTORType() {
        return connectorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_BASESCHEMA() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_BUILTIN() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_COLOR() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_COMPONENT() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_CTYPE() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_INPUTLINKSELECTION() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_LINESTYLE() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_MAXINPUT() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_MAXOUTPUT() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_MERGEALLOWDIFFERENTSCHEMA() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_MININPUT() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_MINOUTPUT() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_MULTISCHEMA() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_NAME() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_NOTSHOWIF() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCONNECTORType_SHOWIF() {
        return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDEFAULTType() {
        return defaultTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDEFAULTType_Value() {
        return (EAttribute)defaultTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDEFAULTType_IF() {
        return (EAttribute)defaultTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDEFAULTType_NOTIF() {
        return (EAttribute)defaultTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDOCUMENTATIONType() {
        return documentationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDOCUMENTATIONType_URL() {
        return (EAttribute)documentationTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDocumentRoot() {
        return documentRootEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Mixed() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XMLNSPrefixMap() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XSISchemaLocation() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ADVANCEDPARAMETERS() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CODEGENERATION() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_COMPONENT() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CONNECTORS() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DOCUMENTATION() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FAMILIES() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_HEADER() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ITEMS() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PARAMETER() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PARAMETERS() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PLUGINDEPENDENCIES() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_RETURNS() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SQLTEMPLATES() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFAMILIESType() {
        return familiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFAMILIESType_FAMILY() {
        return (EAttribute)familiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFORMATType() {
        return formatTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFORMATType_CONNECTION() {
        return (EAttribute)formatTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFORMATType_HINT() {
        return (EAttribute)formatTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFORMATType_LABEL() {
        return (EAttribute)formatTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getHEADERType() {
        return headerTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_SIGNATURE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHEADERType_FORMAT() {
        return (EReference)headerTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_AUTHOR() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_COMBINE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_COMPATIBILITY() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_DATAAUTOPROPAGATE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_EXTENSION() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_HASCONDITIONALOUTPUTS() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_HASHCOMPONENT() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_ISMULTIPLYINGOUTPUTS() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_MAINCODECALLED() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_NUMBERPARALLELIZE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_PARALLELIZE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_PLATEFORM() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_RELEASEDATE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_SCHEMAAUTOPROPAGATE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_SERIAL() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_SHORTNAME() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_SINGLETON() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_STARTABLE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_STATUS() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_SUBJOBCOLOR() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_SUBJOBTITLECOLOR() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_TECHNICAL() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_TSTATCATCHERSTATS() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_VERSION() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_VISIBLE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_EQUIVALENT() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(32);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_INPUTTYPE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(33);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_OUTPUTTYPE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(34);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_SPARKACTION() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(35);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_ACTIVE_DB_COLUMNS() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(36);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_TYPE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_REDUCE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_PARTITIONING() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_SUPPORTS_DB_TYPE() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHEADERType_LOG4J_ENABLED() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIMPORTSType() {
        return importsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTSType_REQUIREDIF() {
        return (EAttribute)importsTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getIMPORTSType_IMPORT() {
        return (EReference)importsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getIMPORTSType_IMPORTS() {
        return (EReference)importsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIMPORTType() {
        return importTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getIMPORTType_INSTALL() {
        return (EReference)importTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_URL() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_BundleID() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_MESSAGE() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_MODULE() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_NAME() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_REQUIRED() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_SHOW() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_UrlPath() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_MVN() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_REQUIREDIF() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_MRREQUIRED() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIMPORTType_MODULEGROUP() {
        return (EAttribute)importTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getINSTALLType() {
        return installTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getINSTALLType_COMMAND() {
        return (EAttribute)installTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getINSTALLType_OS() {
        return (EAttribute)installTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getITEMSType() {
        return itemsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getITEMSType_ITEM() {
        return (EReference)itemsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMSType_BASEDONINPUTSCHEMAS() {
        return (EAttribute)itemsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMSType_BASEDONSCHEMA() {
        return (EAttribute)itemsTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMSType_BASEDONSUBJOBSTARTS() {
        return (EAttribute)itemsTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMSType_COLUMNSBASEDONSCHEMA() {
        return (EAttribute)itemsTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMSType_DEFAULT() {
        return (EAttribute)itemsTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getITEMType() {
        return itemTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getITEMType_ITEMS() {
        return (EReference)itemTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_CONTEXT() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_DISPLAYNAMEASVALUE() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_FIELD() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_FILTER() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_NAME() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_NOCONTEXTASSIST() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_NOTREADONLYIF() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_NOTSHOWIF() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_READONLY() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_READONLYIF() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_REPOSITORYITEM() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_SHOWIF() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_VALUE() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getITEMType_RAW() {
        return (EAttribute)itemTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getJAVACOMMANDType() {
        return javacommandTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getJAVACOMMANDType_ARG() {
        return (EReference)javacommandTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJAVACOMMANDType_CLASS() {
        return (EAttribute)javacommandTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJAVACOMMANDType_FUNCTION() {
        return (EAttribute)javacommandTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJAVACOMMANDType_JAR() {
        return (EAttribute)javacommandTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLINKTOType() {
        return linktoTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLINKTOType_CTYPE() {
        return (EAttribute)linktoTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLINKTOType_NAME() {
        return (EAttribute)linktoTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPARAMETERSType() {
        return parametersTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPARAMETERSType_PARAMETER() {
        return (EReference)parametersTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPARAMETERType() {
        return parameterTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPARAMETERType_DEFAULT() {
        return (EReference)parameterTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPARAMETERType_ITEMS() {
        return (EReference)parameterTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPARAMETERType_TABLE() {
        return (EReference)parameterTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPARAMETERType_JAVACOMMAND() {
        return (EReference)parameterTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_BACKGROUND() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_COLOR() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_CONTEXT() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_CONTEXTMODE() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_DYNAMICSETTINGS() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_FIELD() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_FILTER() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_GROUP() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_MAXLENGTH() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_NAME() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_NBLINES() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_NOCONTEXTASSIST() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_NOTREADONLYIF() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_NOTSHOWIF() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_NUMROW() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_READONLY() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_READONLYIF() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_REPOSITORYVALUE() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_REQUIRED() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_REQUIREDIF() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_SHOW() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_SHOWIF() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_RAW() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPARAMETERType_LOG4J_ENABLED() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPLUGINDEPENDENCIESType() {
        return plugindependenciesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPLUGINDEPENDENCIESType_PLUGINDEPENDENCY() {
        return (EReference)plugindependenciesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPLUGINDEPENDENCYType() {
        return plugindependencyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPLUGINDEPENDENCYType_ID() {
        return (EAttribute)plugindependencyTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRETURNSType() {
        return returnsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRETURNSType_RETURN() {
        return (EReference)returnsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRETURNType() {
        return returnTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRETURNType_AVAILABILITY() {
        return (EAttribute)returnTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRETURNType_NAME() {
        return (EAttribute)returnTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRETURNType_TYPE() {
        return (EAttribute)returnTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRETURNType_SHOWIF() {
        return (EAttribute)returnTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSQLTEMPLATESType() {
        return sqltemplatesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSQLTEMPLATESType_SQLTEMPLATE() {
        return (EReference)sqltemplatesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSQLTEMPLATESType_DB() {
        return (EAttribute)sqltemplatesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSQLTEMPLATEType() {
        return sqltemplateTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSQLTEMPLATEType_CONTENT() {
        return (EAttribute)sqltemplateTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSQLTEMPLATEType_NAME() {
        return (EAttribute)sqltemplateTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTABLEType() {
        return tableTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTABLEType_COLUMN() {
        return (EReference)tableTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTABLEType_IF() {
        return (EAttribute)tableTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTABLEType_NOTIF() {
        return (EAttribute)tableTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTABLEType_READONLYCOLUMNPOSITION() {
        return (EAttribute)tableTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTABLEType_READONLY() {
        return (EAttribute)tableTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTEMPLATEPARAMType() {
        return templateparamTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTEMPLATEPARAMType_SOURCE() {
        return (EAttribute)templateparamTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTEMPLATEPARAMType_TARGET() {
        return (EAttribute)templateparamTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTEMPLATEPARAMType_VALUE() {
        return (EAttribute)templateparamTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTEMPLATESType() {
        return templatesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTEMPLATESType_TEMPLATE() {
        return (EReference)templatesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTEMPLATESType_TEMPLATEPARAM() {
        return (EReference)templatesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTEMPLATESType_CONNECTOR() {
        return (EAttribute)templatesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTEMPLATESType_INPUT() {
        return (EAttribute)templatesTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTEMPLATESType_LOOKUP() {
        return (EAttribute)templatesTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTEMPLATESType_OUTPUT() {
        return (EAttribute)templatesTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTEMPLATEType() {
        return templateTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTEMPLATEType_LINKTO() {
        return (EReference)templateTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTEMPLATEType_COMPONENT() {
        return (EAttribute)templateTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTEMPLATEType_NAME() {
        return (EAttribute)templateTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentFactory getComponentFactory() {
        return (ComponentFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        advancedparametersTypeEClass = createEClass(ADVANCEDPARAMETERS_TYPE);
        createEReference(advancedparametersTypeEClass, ADVANCEDPARAMETERS_TYPE__PARAMETER);

        argTypeEClass = createEClass(ARG_TYPE);
        createEAttribute(argTypeEClass, ARG_TYPE__VALUE);

        codegenerationTypeEClass = createEClass(CODEGENERATION_TYPE);
        createEReference(codegenerationTypeEClass, CODEGENERATION_TYPE__TEMPLATES);
        createEReference(codegenerationTypeEClass, CODEGENERATION_TYPE__IMPORTS);

        columnTypeEClass = createEClass(COLUMN_TYPE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__COMMENT);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__CUSTOM);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__DBTYPE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__DEFAULT);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__KEY);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__LENGTH);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__NAME);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__NULLABLE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__PATTERN);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__PRECISION);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__READONLY);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__RELATEDENTITY);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__RELATIONSHIPTYPE);
        createEAttribute(columnTypeEClass, COLUMN_TYPE__TYPE);

        componentTypeEClass = createEClass(COMPONENT_TYPE);
        createEReference(componentTypeEClass, COMPONENT_TYPE__HEADER);
        createEReference(componentTypeEClass, COMPONENT_TYPE__FAMILIES);
        createEReference(componentTypeEClass, COMPONENT_TYPE__DOCUMENTATION);
        createEReference(componentTypeEClass, COMPONENT_TYPE__CONNECTORS);
        createEReference(componentTypeEClass, COMPONENT_TYPE__SQLTEMPLATES);
        createEReference(componentTypeEClass, COMPONENT_TYPE__PARAMETERS);
        createEReference(componentTypeEClass, COMPONENT_TYPE__ADVANCEDPARAMETERS);
        createEReference(componentTypeEClass, COMPONENT_TYPE__CODEGENERATION);
        createEReference(componentTypeEClass, COMPONENT_TYPE__RETURNS);
        createEReference(componentTypeEClass, COMPONENT_TYPE__PLUGINDEPENDENCIES);

        connectorsTypeEClass = createEClass(CONNECTORS_TYPE);
        createEReference(connectorsTypeEClass, CONNECTORS_TYPE__CONNECTOR);

        connectorTypeEClass = createEClass(CONNECTOR_TYPE);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__BASESCHEMA);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__BUILTIN);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__COLOR);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__COMPONENT);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__CTYPE);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__INPUTLINKSELECTION);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__LINESTYLE);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__MAXINPUT);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__MAXOUTPUT);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__MERGEALLOWDIFFERENTSCHEMA);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__MININPUT);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__MINOUTPUT);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__MULTISCHEMA);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__NAME);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__NOTSHOWIF);
        createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__SHOWIF);

        defaultTypeEClass = createEClass(DEFAULT_TYPE);
        createEAttribute(defaultTypeEClass, DEFAULT_TYPE__VALUE);
        createEAttribute(defaultTypeEClass, DEFAULT_TYPE__IF);
        createEAttribute(defaultTypeEClass, DEFAULT_TYPE__NOTIF);

        documentationTypeEClass = createEClass(DOCUMENTATION_TYPE);
        createEAttribute(documentationTypeEClass, DOCUMENTATION_TYPE__URL);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ADVANCEDPARAMETERS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CODEGENERATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COMPONENT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CONNECTORS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DOCUMENTATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__FAMILIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__HEADER);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ITEMS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PARAMETER);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PARAMETERS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PLUGINDEPENDENCIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__RETURNS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SQLTEMPLATES);

        familiesTypeEClass = createEClass(FAMILIES_TYPE);
        createEAttribute(familiesTypeEClass, FAMILIES_TYPE__FAMILY);

        formatTypeEClass = createEClass(FORMAT_TYPE);
        createEAttribute(formatTypeEClass, FORMAT_TYPE__CONNECTION);
        createEAttribute(formatTypeEClass, FORMAT_TYPE__HINT);
        createEAttribute(formatTypeEClass, FORMAT_TYPE__LABEL);

        headerTypeEClass = createEClass(HEADER_TYPE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__SIGNATURE);
        createEReference(headerTypeEClass, HEADER_TYPE__FORMAT);
        createEAttribute(headerTypeEClass, HEADER_TYPE__AUTHOR);
        createEAttribute(headerTypeEClass, HEADER_TYPE__COMBINE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__COMPATIBILITY);
        createEAttribute(headerTypeEClass, HEADER_TYPE__DATAAUTOPROPAGATE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__EXTENSION);
        createEAttribute(headerTypeEClass, HEADER_TYPE__HASCONDITIONALOUTPUTS);
        createEAttribute(headerTypeEClass, HEADER_TYPE__HASHCOMPONENT);
        createEAttribute(headerTypeEClass, HEADER_TYPE__ISMULTIPLYINGOUTPUTS);
        createEAttribute(headerTypeEClass, HEADER_TYPE__LOG4J_ENABLED);
        createEAttribute(headerTypeEClass, HEADER_TYPE__MAINCODECALLED);
        createEAttribute(headerTypeEClass, HEADER_TYPE__NUMBERPARALLELIZE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__PARALLELIZE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__PARTITIONING);
        createEAttribute(headerTypeEClass, HEADER_TYPE__PLATEFORM);
        createEAttribute(headerTypeEClass, HEADER_TYPE__REDUCE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__RELEASEDATE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__SCHEMAAUTOPROPAGATE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__SERIAL);
        createEAttribute(headerTypeEClass, HEADER_TYPE__SHORTNAME);
        createEAttribute(headerTypeEClass, HEADER_TYPE__SINGLETON);
        createEAttribute(headerTypeEClass, HEADER_TYPE__STARTABLE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__STATUS);
        createEAttribute(headerTypeEClass, HEADER_TYPE__SUBJOBCOLOR);
        createEAttribute(headerTypeEClass, HEADER_TYPE__SUBJOBTITLECOLOR);
        createEAttribute(headerTypeEClass, HEADER_TYPE__SUPPORTS_DB_TYPE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__TECHNICAL);
        createEAttribute(headerTypeEClass, HEADER_TYPE__TSTATCATCHERSTATS);
        createEAttribute(headerTypeEClass, HEADER_TYPE__TYPE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__VERSION);
        createEAttribute(headerTypeEClass, HEADER_TYPE__VISIBLE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__EQUIVALENT);
        createEAttribute(headerTypeEClass, HEADER_TYPE__INPUTTYPE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__OUTPUTTYPE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__SPARKACTION);
        createEAttribute(headerTypeEClass, HEADER_TYPE__ACTIVE_DB_COLUMNS);

        importsTypeEClass = createEClass(IMPORTS_TYPE);
        createEReference(importsTypeEClass, IMPORTS_TYPE__IMPORT);
        createEReference(importsTypeEClass, IMPORTS_TYPE__IMPORTS);
        createEAttribute(importsTypeEClass, IMPORTS_TYPE__REQUIREDIF);

        importTypeEClass = createEClass(IMPORT_TYPE);
        createEReference(importTypeEClass, IMPORT_TYPE__INSTALL);
        createEAttribute(importTypeEClass, IMPORT_TYPE__URL);
        createEAttribute(importTypeEClass, IMPORT_TYPE__BUNDLE_ID);
        createEAttribute(importTypeEClass, IMPORT_TYPE__MESSAGE);
        createEAttribute(importTypeEClass, IMPORT_TYPE__MODULE);
        createEAttribute(importTypeEClass, IMPORT_TYPE__MODULEGROUP);
        createEAttribute(importTypeEClass, IMPORT_TYPE__MRREQUIRED);
        createEAttribute(importTypeEClass, IMPORT_TYPE__NAME);
        createEAttribute(importTypeEClass, IMPORT_TYPE__REQUIRED);
        createEAttribute(importTypeEClass, IMPORT_TYPE__REQUIREDIF);
        createEAttribute(importTypeEClass, IMPORT_TYPE__SHOW);
        createEAttribute(importTypeEClass, IMPORT_TYPE__URL_PATH);
        createEAttribute(importTypeEClass, IMPORT_TYPE__MVN);

        installTypeEClass = createEClass(INSTALL_TYPE);
        createEAttribute(installTypeEClass, INSTALL_TYPE__COMMAND);
        createEAttribute(installTypeEClass, INSTALL_TYPE__OS);

        itemsTypeEClass = createEClass(ITEMS_TYPE);
        createEReference(itemsTypeEClass, ITEMS_TYPE__ITEM);
        createEAttribute(itemsTypeEClass, ITEMS_TYPE__BASEDONINPUTSCHEMAS);
        createEAttribute(itemsTypeEClass, ITEMS_TYPE__BASEDONSCHEMA);
        createEAttribute(itemsTypeEClass, ITEMS_TYPE__BASEDONSUBJOBSTARTS);
        createEAttribute(itemsTypeEClass, ITEMS_TYPE__COLUMNSBASEDONSCHEMA);
        createEAttribute(itemsTypeEClass, ITEMS_TYPE__DEFAULT);

        itemTypeEClass = createEClass(ITEM_TYPE);
        createEReference(itemTypeEClass, ITEM_TYPE__ITEMS);
        createEAttribute(itemTypeEClass, ITEM_TYPE__CONTEXT);
        createEAttribute(itemTypeEClass, ITEM_TYPE__DISPLAYNAMEASVALUE);
        createEAttribute(itemTypeEClass, ITEM_TYPE__FIELD);
        createEAttribute(itemTypeEClass, ITEM_TYPE__FILTER);
        createEAttribute(itemTypeEClass, ITEM_TYPE__NAME);
        createEAttribute(itemTypeEClass, ITEM_TYPE__NOCONTEXTASSIST);
        createEAttribute(itemTypeEClass, ITEM_TYPE__NOTREADONLYIF);
        createEAttribute(itemTypeEClass, ITEM_TYPE__NOTSHOWIF);
        createEAttribute(itemTypeEClass, ITEM_TYPE__RAW);
        createEAttribute(itemTypeEClass, ITEM_TYPE__READONLY);
        createEAttribute(itemTypeEClass, ITEM_TYPE__READONLYIF);
        createEAttribute(itemTypeEClass, ITEM_TYPE__REPOSITORYITEM);
        createEAttribute(itemTypeEClass, ITEM_TYPE__SHOWIF);
        createEAttribute(itemTypeEClass, ITEM_TYPE__VALUE);

        javacommandTypeEClass = createEClass(JAVACOMMAND_TYPE);
        createEReference(javacommandTypeEClass, JAVACOMMAND_TYPE__ARG);
        createEAttribute(javacommandTypeEClass, JAVACOMMAND_TYPE__CLASS);
        createEAttribute(javacommandTypeEClass, JAVACOMMAND_TYPE__FUNCTION);
        createEAttribute(javacommandTypeEClass, JAVACOMMAND_TYPE__JAR);

        linktoTypeEClass = createEClass(LINKTO_TYPE);
        createEAttribute(linktoTypeEClass, LINKTO_TYPE__CTYPE);
        createEAttribute(linktoTypeEClass, LINKTO_TYPE__NAME);

        parametersTypeEClass = createEClass(PARAMETERS_TYPE);
        createEReference(parametersTypeEClass, PARAMETERS_TYPE__PARAMETER);

        parameterTypeEClass = createEClass(PARAMETER_TYPE);
        createEReference(parameterTypeEClass, PARAMETER_TYPE__DEFAULT);
        createEReference(parameterTypeEClass, PARAMETER_TYPE__ITEMS);
        createEReference(parameterTypeEClass, PARAMETER_TYPE__TABLE);
        createEReference(parameterTypeEClass, PARAMETER_TYPE__JAVACOMMAND);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__BACKGROUND);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__COLOR);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__CONTEXT);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__CONTEXTMODE);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__DYNAMICSETTINGS);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__FIELD);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__FILTER);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__GROUP);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__LOG4J_ENABLED);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__MAXLENGTH);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__NAME);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__NBLINES);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__NOCONTEXTASSIST);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__NOTREADONLYIF);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__NOTSHOWIF);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__NUMROW);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__RAW);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__READONLY);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__READONLYIF);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__REPOSITORYVALUE);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__REQUIRED);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__REQUIREDIF);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__SHOW);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__SHOWIF);

        plugindependenciesTypeEClass = createEClass(PLUGINDEPENDENCIES_TYPE);
        createEReference(plugindependenciesTypeEClass, PLUGINDEPENDENCIES_TYPE__PLUGINDEPENDENCY);

        plugindependencyTypeEClass = createEClass(PLUGINDEPENDENCY_TYPE);
        createEAttribute(plugindependencyTypeEClass, PLUGINDEPENDENCY_TYPE__ID);

        returnsTypeEClass = createEClass(RETURNS_TYPE);
        createEReference(returnsTypeEClass, RETURNS_TYPE__RETURN);

        returnTypeEClass = createEClass(RETURN_TYPE);
        createEAttribute(returnTypeEClass, RETURN_TYPE__AVAILABILITY);
        createEAttribute(returnTypeEClass, RETURN_TYPE__NAME);
        createEAttribute(returnTypeEClass, RETURN_TYPE__SHOWIF);
        createEAttribute(returnTypeEClass, RETURN_TYPE__TYPE);

        sqltemplatesTypeEClass = createEClass(SQLTEMPLATES_TYPE);
        createEReference(sqltemplatesTypeEClass, SQLTEMPLATES_TYPE__SQLTEMPLATE);
        createEAttribute(sqltemplatesTypeEClass, SQLTEMPLATES_TYPE__DB);

        sqltemplateTypeEClass = createEClass(SQLTEMPLATE_TYPE);
        createEAttribute(sqltemplateTypeEClass, SQLTEMPLATE_TYPE__CONTENT);
        createEAttribute(sqltemplateTypeEClass, SQLTEMPLATE_TYPE__NAME);

        tableTypeEClass = createEClass(TABLE_TYPE);
        createEReference(tableTypeEClass, TABLE_TYPE__COLUMN);
        createEAttribute(tableTypeEClass, TABLE_TYPE__IF);
        createEAttribute(tableTypeEClass, TABLE_TYPE__NOTIF);
        createEAttribute(tableTypeEClass, TABLE_TYPE__READONLYCOLUMNPOSITION);
        createEAttribute(tableTypeEClass, TABLE_TYPE__READONLY);

        templateparamTypeEClass = createEClass(TEMPLATEPARAM_TYPE);
        createEAttribute(templateparamTypeEClass, TEMPLATEPARAM_TYPE__SOURCE);
        createEAttribute(templateparamTypeEClass, TEMPLATEPARAM_TYPE__TARGET);
        createEAttribute(templateparamTypeEClass, TEMPLATEPARAM_TYPE__VALUE);

        templatesTypeEClass = createEClass(TEMPLATES_TYPE);
        createEReference(templatesTypeEClass, TEMPLATES_TYPE__TEMPLATE);
        createEReference(templatesTypeEClass, TEMPLATES_TYPE__TEMPLATEPARAM);
        createEAttribute(templatesTypeEClass, TEMPLATES_TYPE__CONNECTOR);
        createEAttribute(templatesTypeEClass, TEMPLATES_TYPE__INPUT);
        createEAttribute(templatesTypeEClass, TEMPLATES_TYPE__LOOKUP);
        createEAttribute(templatesTypeEClass, TEMPLATES_TYPE__OUTPUT);

        templateTypeEClass = createEClass(TEMPLATE_TYPE);
        createEReference(templateTypeEClass, TEMPLATE_TYPE__LINKTO);
        createEAttribute(templateTypeEClass, TEMPLATE_TYPE__COMPONENT);
        createEAttribute(templateTypeEClass, TEMPLATE_TYPE__NAME);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
        initEClass(advancedparametersTypeEClass, ADVANCEDPARAMETERSType.class, "ADVANCEDPARAMETERSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getADVANCEDPARAMETERSType_PARAMETER(), this.getPARAMETERType(), null, "pARAMETER", null, 0, -1, ADVANCEDPARAMETERSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(argTypeEClass, ARGType.class, "ARGType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getARGType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ARGType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(codegenerationTypeEClass, CODEGENERATIONType.class, "CODEGENERATIONType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCODEGENERATIONType_TEMPLATES(), this.getTEMPLATESType(), null, "tEMPLATES", null, 0, -1, CODEGENERATIONType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCODEGENERATIONType_IMPORTS(), this.getIMPORTSType(), null, "iMPORTS", null, 0, 1, CODEGENERATIONType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(columnTypeEClass, COLUMNType.class, "COLUMNType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCOLUMNType_COMMENT(), theXMLTypePackage.getString(), "cOMMENT", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_CUSTOM(), theXMLTypePackage.getBoolean(), "cUSTOM", "false", 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_DBTYPE(), theXMLTypePackage.getString(), "dBTYPE", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_DEFAULT(), theXMLTypePackage.getString(), "dEFAULT", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_KEY(), theXMLTypePackage.getBoolean(), "kEY", "false", 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_LENGTH(), theXMLTypePackage.getInt(), "lENGTH", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_NAME(), theXMLTypePackage.getString(), "nAME", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_NULLABLE(), theXMLTypePackage.getBoolean(), "nULLABLE", "true", 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_PATTERN(), theXMLTypePackage.getString(), "pATTERN", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_PRECISION(), theXMLTypePackage.getInt(), "pRECISION", "0", 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_READONLY(), theXMLTypePackage.getBoolean(), "rEADONLY", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_RELATEDENTITY(), theXMLTypePackage.getString(), "rELATEDENTITY", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_RELATIONSHIPTYPE(), theXMLTypePackage.getString(), "rELATIONSHIPTYPE", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCOLUMNType_TYPE(), theXMLTypePackage.getString(), "tYPE", null, 0, 1, COLUMNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentTypeEClass, COMPONENTType.class, "COMPONENTType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCOMPONENTType_HEADER(), this.getHEADERType(), null, "hEADER", null, 1, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCOMPONENTType_FAMILIES(), this.getFAMILIESType(), null, "fAMILIES", null, 1, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCOMPONENTType_DOCUMENTATION(), this.getDOCUMENTATIONType(), null, "dOCUMENTATION", null, 1, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCOMPONENTType_CONNECTORS(), this.getCONNECTORSType(), null, "cONNECTORS", null, 1, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCOMPONENTType_SQLTEMPLATES(), this.getSQLTEMPLATESType(), null, "sQLTEMPLATES", null, 0, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCOMPONENTType_PARAMETERS(), this.getPARAMETERSType(), null, "pARAMETERS", null, 1, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCOMPONENTType_ADVANCEDPARAMETERS(), this.getADVANCEDPARAMETERSType(), null, "aDVANCEDPARAMETERS", null, 0, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCOMPONENTType_CODEGENERATION(), this.getCODEGENERATIONType(), null, "cODEGENERATION", null, 1, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCOMPONENTType_RETURNS(), this.getRETURNSType(), null, "rETURNS", null, 1, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCOMPONENTType_PLUGINDEPENDENCIES(), this.getPLUGINDEPENDENCIESType(), null, "pLUGINDEPENDENCIES", null, 0, 1, COMPONENTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(connectorsTypeEClass, CONNECTORSType.class, "CONNECTORSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCONNECTORSType_CONNECTOR(), this.getCONNECTORType(), null, "cONNECTOR", null, 1, -1, CONNECTORSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(connectorTypeEClass, CONNECTORType.class, "CONNECTORType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCONNECTORType_BASESCHEMA(), theXMLTypePackage.getString(), "bASESCHEMA", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_BUILTIN(), theXMLTypePackage.getBoolean(), "bUILTIN", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_COLOR(), theXMLTypePackage.getString(), "cOLOR", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_COMPONENT(), theXMLTypePackage.getString(), "cOMPONENT", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_CTYPE(), theXMLTypePackage.getString(), "cTYPE", null, 1, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_INPUTLINKSELECTION(), theXMLTypePackage.getBoolean(), "iNPUTLINKSELECTION", "false", 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_LINESTYLE(), theXMLTypePackage.getInt(), "lINESTYLE", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_MAXINPUT(), theXMLTypePackage.getInt(), "mAXINPUT", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_MAXOUTPUT(), theXMLTypePackage.getInt(), "mAXOUTPUT", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_MERGEALLOWDIFFERENTSCHEMA(), theXMLTypePackage.getBoolean(), "mERGEALLOWDIFFERENTSCHEMA", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_MININPUT(), theXMLTypePackage.getInt(), "mININPUT", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_MINOUTPUT(), theXMLTypePackage.getInt(), "mINOUTPUT", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_MULTISCHEMA(), theXMLTypePackage.getBoolean(), "mULTISCHEMA", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_NAME(), theXMLTypePackage.getString(), "nAME", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_NOTSHOWIF(), theXMLTypePackage.getString(), "nOTSHOWIF", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCONNECTORType_SHOWIF(), theXMLTypePackage.getString(), "sHOWIF", null, 0, 1, CONNECTORType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(defaultTypeEClass, DEFAULTType.class, "DEFAULTType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDEFAULTType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, DEFAULTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDEFAULTType_IF(), theXMLTypePackage.getString(), "iF", null, 0, 1, DEFAULTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDEFAULTType_NOTIF(), theXMLTypePackage.getString(), "nOTIF", null, 0, 1, DEFAULTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentationTypeEClass, DOCUMENTATIONType.class, "DOCUMENTATIONType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDOCUMENTATIONType_URL(), theXMLTypePackage.getString(), "uRL", null, 1, 1, DOCUMENTATIONType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ADVANCEDPARAMETERS(), this.getADVANCEDPARAMETERSType(), null, "aDVANCEDPARAMETERS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CODEGENERATION(), this.getCODEGENERATIONType(), null, "cODEGENERATION", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_COMPONENT(), this.getCOMPONENTType(), null, "cOMPONENT", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CONNECTORS(), this.getCONNECTORSType(), null, "cONNECTORS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DOCUMENTATION(), this.getDOCUMENTATIONType(), null, "dOCUMENTATION", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_FAMILIES(), this.getFAMILIESType(), null, "fAMILIES", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_HEADER(), this.getHEADERType(), null, "hEADER", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ITEMS(), this.getITEMSType(), null, "iTEMS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PARAMETER(), this.getPARAMETERType(), null, "pARAMETER", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PARAMETERS(), this.getPARAMETERSType(), null, "pARAMETERS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PLUGINDEPENDENCIES(), this.getPLUGINDEPENDENCIESType(), null, "pLUGINDEPENDENCIES", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_RETURNS(), this.getRETURNSType(), null, "rETURNS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_SQLTEMPLATES(), this.getSQLTEMPLATESType(), null, "sQLTEMPLATES", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(familiesTypeEClass, FAMILIESType.class, "FAMILIESType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFAMILIESType_FAMILY(), theXMLTypePackage.getString(), "fAMILY", null, 1, -1, FAMILIESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(formatTypeEClass, FORMATType.class, "FORMATType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFORMATType_CONNECTION(), theXMLTypePackage.getString(), "cONNECTION", null, 0, 1, FORMATType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFORMATType_HINT(), theXMLTypePackage.getString(), "hINT", null, 0, 1, FORMATType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFORMATType_LABEL(), theXMLTypePackage.getString(), "lABEL", null, 0, 1, FORMATType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(headerTypeEClass, HEADERType.class, "HEADERType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getHEADERType_SIGNATURE(), theXMLTypePackage.getString(), "sIGNATURE", null, 1, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getHEADERType_FORMAT(), this.getFORMATType(), null, "fORMAT", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_AUTHOR(), theXMLTypePackage.getString(), "aUTHOR", null, 1, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_COMBINE(), theXMLTypePackage.getString(), "cOMBINE", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_COMPATIBILITY(), theXMLTypePackage.getString(), "cOMPATIBILITY", null, 1, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_DATAAUTOPROPAGATE(), theXMLTypePackage.getBoolean(), "dATAAUTOPROPAGATE", "true", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_EXTENSION(), theXMLTypePackage.getString(), "eXTENSION", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_HASCONDITIONALOUTPUTS(), theXMLTypePackage.getBoolean(), "hASCONDITIONALOUTPUTS", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_HASHCOMPONENT(), theXMLTypePackage.getBoolean(), "hASHCOMPONENT", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_ISMULTIPLYINGOUTPUTS(), theXMLTypePackage.getBoolean(), "iSMULTIPLYINGOUTPUTS", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_LOG4J_ENABLED(), theXMLTypePackage.getBoolean(), "lOG4J_ENABLED", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_MAINCODECALLED(), theXMLTypePackage.getBoolean(), "mAINCODECALLED", "true", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_NUMBERPARALLELIZE(), theXMLTypePackage.getString(), "nUMBERPARALLELIZE", "2", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_PARALLELIZE(), theXMLTypePackage.getBoolean(), "pARALLELIZE", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_PARTITIONING(), ecorePackage.getEString(), "pARTITIONING", "AUTO", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_PLATEFORM(), theXMLTypePackage.getString(), "pLATEFORM", null, 1, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_REDUCE(), ecorePackage.getEBoolean(), "rEDUCE", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_RELEASEDATE(), theXMLTypePackage.getString(), "rELEASEDATE", null, 1, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_SCHEMAAUTOPROPAGATE(), theXMLTypePackage.getBoolean(), "sCHEMAAUTOPROPAGATE", "true", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_SERIAL(), theXMLTypePackage.getString(), "sERIAL", null, 1, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_SHORTNAME(), theXMLTypePackage.getString(), "sHORTNAME", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_SINGLETON(), theXMLTypePackage.getBoolean(), "sINGLETON", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_STARTABLE(), theXMLTypePackage.getBoolean(), "sTARTABLE", null, 1, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_STATUS(), theXMLTypePackage.getString(), "sTATUS", null, 1, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_SUBJOBCOLOR(), theXMLTypePackage.getString(), "sUBJOBCOLOR", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_SUBJOBTITLECOLOR(), theXMLTypePackage.getString(), "sUBJOBTITLECOLOR", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_SUPPORTS_DB_TYPE(), theXMLTypePackage.getBoolean(), "sUPPORTS_DB_TYPE", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_TECHNICAL(), theXMLTypePackage.getBoolean(), "tECHNICAL", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_TSTATCATCHERSTATS(), theXMLTypePackage.getBoolean(), "tSTATCATCHERSTATS", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_TYPE(), theXMLTypePackage.getString(), "tYPE", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_VERSION(), theXMLTypePackage.getDecimal(), "vERSION", null, 1, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_VISIBLE(), theXMLTypePackage.getBoolean(), "vISIBLE", "true", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_EQUIVALENT(), theXMLTypePackage.getString(), "eQUIVALENT", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_INPUTTYPE(), theXMLTypePackage.getString(), "iNPUTTYPE", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_OUTPUTTYPE(), theXMLTypePackage.getString(), "oUTPUTTYPE", null, 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_SPARKACTION(), theXMLTypePackage.getBoolean(), "sPARKACTION", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHEADERType_ACTIVE_DB_COLUMNS(), theXMLTypePackage.getBoolean(), "aCTIVE_DB_COLUMNS", "false", 0, 1, HEADERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(importsTypeEClass, IMPORTSType.class, "IMPORTSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIMPORTSType_IMPORT(), this.getIMPORTType(), null, "iMPORT", null, 0, -1, IMPORTSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIMPORTSType_IMPORTS(), this.getIMPORTSType(), null, "iMPORTS", null, 0, -1, IMPORTSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTSType_REQUIREDIF(), ecorePackage.getEString(), "rEQUIREDIF", null, 0, 1, IMPORTSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(importTypeEClass, IMPORTType.class, "IMPORTType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIMPORTType_INSTALL(), this.getINSTALLType(), null, "iNSTALL", null, 0, -1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_URL(), theXMLTypePackage.getString(), "uRL", null, 0, -1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_BundleID(), theXMLTypePackage.getString(), "bundleID", null, 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_MESSAGE(), theXMLTypePackage.getString(), "mESSAGE", null, 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_MODULE(), theXMLTypePackage.getString(), "mODULE", null, 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_MODULEGROUP(), theXMLTypePackage.getString(), "mODULEGROUP", null, 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_MRREQUIRED(), theXMLTypePackage.getBoolean(), "mRREQUIRED", "false", 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_NAME(), theXMLTypePackage.getString(), "nAME", null, 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_REQUIRED(), theXMLTypePackage.getBoolean(), "rEQUIRED", null, 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_REQUIREDIF(), ecorePackage.getEString(), "rEQUIREDIF", null, 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_SHOW(), theXMLTypePackage.getBoolean(), "sHOW", "true", 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_UrlPath(), theXMLTypePackage.getString(), "urlPath", null, 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIMPORTType_MVN(), theXMLTypePackage.getString(), "MVN", null, 0, 1, IMPORTType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(installTypeEClass, INSTALLType.class, "INSTALLType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getINSTALLType_COMMAND(), theXMLTypePackage.getString(), "cOMMAND", null, 1, 1, INSTALLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getINSTALLType_OS(), theXMLTypePackage.getString(), "oS", null, 0, 1, INSTALLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(itemsTypeEClass, ITEMSType.class, "ITEMSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getITEMSType_ITEM(), this.getITEMType(), null, "iTEM", null, 0, -1, ITEMSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMSType_BASEDONINPUTSCHEMAS(), theXMLTypePackage.getBoolean(), "bASEDONINPUTSCHEMAS", "false", 0, 1, ITEMSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMSType_BASEDONSCHEMA(), theXMLTypePackage.getBoolean(), "bASEDONSCHEMA", "false", 0, 1, ITEMSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMSType_BASEDONSUBJOBSTARTS(), theXMLTypePackage.getBoolean(), "bASEDONSUBJOBSTARTS", "false", 0, 1, ITEMSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMSType_COLUMNSBASEDONSCHEMA(), theXMLTypePackage.getBoolean(), "cOLUMNSBASEDONSCHEMA", "false", 0, 1, ITEMSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMSType_DEFAULT(), theXMLTypePackage.getString(), "dEFAULT", null, 0, 1, ITEMSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(itemTypeEClass, ITEMType.class, "ITEMType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getITEMType_ITEMS(), this.getITEMSType(), null, "iTEMS", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_CONTEXT(), theXMLTypePackage.getString(), "cONTEXT", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_DISPLAYNAMEASVALUE(), theXMLTypePackage.getBoolean(), "dISPLAYNAMEASVALUE", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_FIELD(), theXMLTypePackage.getString(), "fIELD", "TEXT", 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_FILTER(), theXMLTypePackage.getString(), "fILTER", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_NAME(), theXMLTypePackage.getString(), "nAME", null, 1, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_NOCONTEXTASSIST(), theXMLTypePackage.getBoolean(), "nOCONTEXTASSIST", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_NOTREADONLYIF(), theXMLTypePackage.getString(), "nOTREADONLYIF", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_NOTSHOWIF(), theXMLTypePackage.getString(), "nOTSHOWIF", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_RAW(), theXMLTypePackage.getBoolean(), "rAW", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_READONLY(), theXMLTypePackage.getBoolean(), "rEADONLY", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_READONLYIF(), theXMLTypePackage.getString(), "rEADONLYIF", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_REPOSITORYITEM(), theXMLTypePackage.getString(), "rEPOSITORYITEM", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_SHOWIF(), theXMLTypePackage.getString(), "sHOWIF", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getITEMType_VALUE(), theXMLTypePackage.getString(), "vALUE", null, 0, 1, ITEMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(javacommandTypeEClass, JAVACOMMANDType.class, "JAVACOMMANDType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getJAVACOMMANDType_ARG(), this.getARGType(), null, "aRG", null, 0, -1, JAVACOMMANDType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJAVACOMMANDType_CLASS(), theXMLTypePackage.getString(), "cLASS", null, 0, 1, JAVACOMMANDType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJAVACOMMANDType_FUNCTION(), theXMLTypePackage.getString(), "fUNCTION", null, 0, 1, JAVACOMMANDType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJAVACOMMANDType_JAR(), theXMLTypePackage.getString(), "jAR", null, 0, 1, JAVACOMMANDType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(linktoTypeEClass, LINKTOType.class, "LINKTOType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLINKTOType_CTYPE(), theXMLTypePackage.getString(), "cTYPE", null, 0, 1, LINKTOType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLINKTOType_NAME(), theXMLTypePackage.getString(), "nAME", null, 0, 1, LINKTOType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(parametersTypeEClass, PARAMETERSType.class, "PARAMETERSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPARAMETERSType_PARAMETER(), this.getPARAMETERType(), null, "pARAMETER", null, 0, -1, PARAMETERSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(parameterTypeEClass, PARAMETERType.class, "PARAMETERType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPARAMETERType_DEFAULT(), this.getDEFAULTType(), null, "dEFAULT", null, 0, -1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPARAMETERType_ITEMS(), this.getITEMSType(), null, "iTEMS", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPARAMETERType_TABLE(), this.getTABLEType(), null, "tABLE", null, 0, -1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPARAMETERType_JAVACOMMAND(), this.getJAVACOMMANDType(), null, "jAVACOMMAND", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_BACKGROUND(), theXMLTypePackage.getString(), "bACKGROUND", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_COLOR(), theXMLTypePackage.getString(), "cOLOR", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_CONTEXT(), theXMLTypePackage.getString(), "cONTEXT", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_CONTEXTMODE(), theXMLTypePackage.getBoolean(), "cONTEXTMODE", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_DYNAMICSETTINGS(), theXMLTypePackage.getBoolean(), "dYNAMICSETTINGS", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_FIELD(), theXMLTypePackage.getString(), "fIELD", null, 1, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_FILTER(), theXMLTypePackage.getString(), "fILTER", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_GROUP(), theXMLTypePackage.getString(), "gROUP", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_LOG4J_ENABLED(), theXMLTypePackage.getBoolean(), "lOG4J_ENABLED", "true", 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_MAXLENGTH(), theXMLTypePackage.getInt(), "mAXLENGTH", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_NAME(), theXMLTypePackage.getString(), "nAME", null, 1, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_NBLINES(), theXMLTypePackage.getInt(), "nBLINES", "3", 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_NOCONTEXTASSIST(), theXMLTypePackage.getBoolean(), "nOCONTEXTASSIST", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_NOTREADONLYIF(), theXMLTypePackage.getString(), "nOTREADONLYIF", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_NOTSHOWIF(), theXMLTypePackage.getString(), "nOTSHOWIF", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_NUMROW(), theXMLTypePackage.getInt(), "nUMROW", null, 1, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_RAW(), theXMLTypePackage.getBoolean(), "rAW", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_READONLY(), theXMLTypePackage.getBoolean(), "rEADONLY", "false", 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_READONLYIF(), theXMLTypePackage.getString(), "rEADONLYIF", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_REPOSITORYVALUE(), theXMLTypePackage.getString(), "rEPOSITORYVALUE", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_REQUIRED(), theXMLTypePackage.getBoolean(), "rEQUIRED", "false", 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_REQUIREDIF(), theXMLTypePackage.getString(), "rEQUIREDIF", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_SHOW(), theXMLTypePackage.getBoolean(), "sHOW", "true", 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPARAMETERType_SHOWIF(), theXMLTypePackage.getString(), "sHOWIF", null, 0, 1, PARAMETERType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(plugindependenciesTypeEClass, PLUGINDEPENDENCIESType.class, "PLUGINDEPENDENCIESType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPLUGINDEPENDENCIESType_PLUGINDEPENDENCY(), this.getPLUGINDEPENDENCYType(), null, "pLUGINDEPENDENCY", null, 0, -1, PLUGINDEPENDENCIESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(plugindependencyTypeEClass, PLUGINDEPENDENCYType.class, "PLUGINDEPENDENCYType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPLUGINDEPENDENCYType_ID(), theXMLTypePackage.getString(), "iD", null, 1, 1, PLUGINDEPENDENCYType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(returnsTypeEClass, RETURNSType.class, "RETURNSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRETURNSType_RETURN(), this.getRETURNType(), null, "rETURN", null, 0, -1, RETURNSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(returnTypeEClass, RETURNType.class, "RETURNType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRETURNType_AVAILABILITY(), theXMLTypePackage.getString(), "aVAILABILITY", null, 1, 1, RETURNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRETURNType_NAME(), theXMLTypePackage.getString(), "nAME", null, 1, 1, RETURNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRETURNType_SHOWIF(), theXMLTypePackage.getString(), "sHOWIF", null, 0, 1, RETURNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRETURNType_TYPE(), theXMLTypePackage.getString(), "tYPE", null, 1, 1, RETURNType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sqltemplatesTypeEClass, SQLTEMPLATESType.class, "SQLTEMPLATESType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSQLTEMPLATESType_SQLTEMPLATE(), this.getSQLTEMPLATEType(), null, "sQLTEMPLATE", null, 0, -1, SQLTEMPLATESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSQLTEMPLATESType_DB(), theXMLTypePackage.getString(), "dB", null, 0, 1, SQLTEMPLATESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sqltemplateTypeEClass, SQLTEMPLATEType.class, "SQLTEMPLATEType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSQLTEMPLATEType_CONTENT(), theXMLTypePackage.getString(), "cONTENT", null, 0, 1, SQLTEMPLATEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSQLTEMPLATEType_NAME(), theXMLTypePackage.getString(), "nAME", null, 0, 1, SQLTEMPLATEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tableTypeEClass, TABLEType.class, "TABLEType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTABLEType_COLUMN(), this.getCOLUMNType(), null, "cOLUMN", null, 0, -1, TABLEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTABLEType_IF(), theXMLTypePackage.getString(), "iF", null, 0, 1, TABLEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTABLEType_NOTIF(), theXMLTypePackage.getString(), "nOTIF", null, 0, 1, TABLEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTABLEType_READONLYCOLUMNPOSITION(), theXMLTypePackage.getString(), "rEADONLYCOLUMNPOSITION", null, 0, 1, TABLEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTABLEType_READONLY(), theXMLTypePackage.getBoolean(), "rEADONLY", "false", 0, 1, TABLEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(templateparamTypeEClass, TEMPLATEPARAMType.class, "TEMPLATEPARAMType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTEMPLATEPARAMType_SOURCE(), theXMLTypePackage.getString(), "sOURCE", null, 0, 1, TEMPLATEPARAMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTEMPLATEPARAMType_TARGET(), theXMLTypePackage.getString(), "tARGET", null, 0, 1, TEMPLATEPARAMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTEMPLATEPARAMType_VALUE(), theXMLTypePackage.getString(), "vALUE", null, 0, 1, TEMPLATEPARAMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(templatesTypeEClass, TEMPLATESType.class, "TEMPLATESType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTEMPLATESType_TEMPLATE(), this.getTEMPLATEType(), null, "tEMPLATE", null, 0, -1, TEMPLATESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTEMPLATESType_TEMPLATEPARAM(), this.getTEMPLATEPARAMType(), null, "tEMPLATEPARAM", null, 0, -1, TEMPLATESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTEMPLATESType_CONNECTOR(), theXMLTypePackage.getString(), "cONNECTOR", null, 0, 1, TEMPLATESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTEMPLATESType_INPUT(), theXMLTypePackage.getString(), "iNPUT", null, 0, 1, TEMPLATESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTEMPLATESType_LOOKUP(), theXMLTypePackage.getBoolean(), "lOOKUP", "false", 0, 1, TEMPLATESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTEMPLATESType_OUTPUT(), theXMLTypePackage.getString(), "oUTPUT", null, 0, 1, TEMPLATESType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(templateTypeEClass, TEMPLATEType.class, "TEMPLATEType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTEMPLATEType_LINKTO(), this.getLINKTOType(), null, "lINKTO", null, 0, -1, TEMPLATEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTEMPLATEType_COMPONENT(), theXMLTypePackage.getString(), "cOMPONENT", null, 0, 1, TEMPLATEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTEMPLATEType_NAME(), theXMLTypePackage.getString(), "nAME", null, 0, 1, TEMPLATEType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
    }

    /**
     * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
          (advancedparametersTypeEClass, 
           source, 
           new String[] {
             "name", "ADVANCED_PARAMETERS_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getADVANCEDPARAMETERSType_PARAMETER(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PARAMETER",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (argTypeEClass, 
           source, 
           new String[] {
             "name", "ARG_._type",
             "kind", "simple"
           });	
        addAnnotation
          (getARGType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });	
        addAnnotation
          (codegenerationTypeEClass, 
           source, 
           new String[] {
             "name", "CODEGENERATION_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getCODEGENERATIONType_TEMPLATES(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TEMPLATES",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCODEGENERATIONType_IMPORTS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "IMPORTS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (columnTypeEClass, 
           source, 
           new String[] {
             "name", "COLUMN_._type",
             "kind", "empty"
           });	
        addAnnotation
          (getCOLUMNType_COMMENT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "COMMENT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_CUSTOM(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CUSTOM",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_DBTYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "DBTYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_DEFAULT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "DEFAULT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_KEY(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "KEY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_LENGTH(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "LENGTH",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_NAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NAME",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_NULLABLE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NULLABLE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_PATTERN(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "PATTERN",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_PRECISION(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "PRECISION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_READONLY(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "READONLY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_RELATEDENTITY(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "RELATEDENTITY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_RELATIONSHIPTYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "RELATIONSHIPTYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOLUMNType_TYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "TYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (componentTypeEClass, 
           source, 
           new String[] {
             "name", "COMPONENT_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getCOMPONENTType_HEADER(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "HEADER",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOMPONENTType_FAMILIES(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FAMILIES",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOMPONENTType_DOCUMENTATION(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DOCUMENTATION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOMPONENTType_CONNECTORS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CONNECTORS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOMPONENTType_SQLTEMPLATES(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SQLTEMPLATES",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOMPONENTType_PARAMETERS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PARAMETERS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOMPONENTType_ADVANCEDPARAMETERS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ADVANCED_PARAMETERS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOMPONENTType_CODEGENERATION(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CODEGENERATION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOMPONENTType_RETURNS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RETURNS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCOMPONENTType_PLUGINDEPENDENCIES(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PLUGINDEPENDENCIES",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (connectorsTypeEClass, 
           source, 
           new String[] {
             "name", "CONNECTORS_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getCONNECTORSType_CONNECTOR(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CONNECTOR",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (connectorTypeEClass, 
           source, 
           new String[] {
             "name", "CONNECTOR_._type",
             "kind", "empty"
           });	
        addAnnotation
          (getCONNECTORType_BASESCHEMA(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "BASE_SCHEMA",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_BUILTIN(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "BUILTIN",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_COLOR(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "COLOR",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_COMPONENT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "COMPONENT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_CTYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CTYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_INPUTLINKSELECTION(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "INPUT_LINK_SELECTION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_LINESTYLE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "LINE_STYLE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_MAXINPUT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MAX_INPUT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_MAXOUTPUT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MAX_OUTPUT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_MERGEALLOWDIFFERENTSCHEMA(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MERGE_ALLOW_DIFFERENT_SCHEMA",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_MININPUT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MIN_INPUT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_MINOUTPUT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MIN_OUTPUT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_MULTISCHEMA(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MULTI_SCHEMA",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_NAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NAME",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_NOTSHOWIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NOT_SHOW_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCONNECTORType_SHOWIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SHOW_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (defaultTypeEClass, 
           source, 
           new String[] {
             "name", "DEFAULT_._type",
             "kind", "simple"
           });	
        addAnnotation
          (getDEFAULTType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });	
        addAnnotation
          (getDEFAULTType_IF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDEFAULTType_NOTIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NOT_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (documentationTypeEClass, 
           source, 
           new String[] {
             "name", "DOCUMENTATION_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getDOCUMENTATIONType_URL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "URL",
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
          (getDocumentRoot_ADVANCEDPARAMETERS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ADVANCED_PARAMETERS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_CODEGENERATION(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CODEGENERATION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_COMPONENT(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "COMPONENT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_CONNECTORS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CONNECTORS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_DOCUMENTATION(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DOCUMENTATION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_FAMILIES(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FAMILIES",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_HEADER(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "HEADER",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_ITEMS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ITEMS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_PARAMETER(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PARAMETER",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_PARAMETERS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PARAMETERS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_PLUGINDEPENDENCIES(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PLUGINDEPENDENCIES",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_RETURNS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RETURNS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_SQLTEMPLATES(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SQLTEMPLATES",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (familiesTypeEClass, 
           source, 
           new String[] {
             "name", "FAMILIES_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getFAMILIESType_FAMILY(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FAMILY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (formatTypeEClass, 
           source, 
           new String[] {
             "name", "FORMAT_._type",
             "kind", "empty"
           });	
        addAnnotation
          (getFORMATType_CONNECTION(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CONNECTION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getFORMATType_HINT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "HINT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getFORMATType_LABEL(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "LABEL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (headerTypeEClass, 
           source, 
           new String[] {
             "name", "HEADER_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getHEADERType_SIGNATURE(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SIGNATURE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_FORMAT(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FORMAT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_AUTHOR(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "AUTHOR",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_COMBINE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "COMBINE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_COMPATIBILITY(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "COMPATIBILITY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_DATAAUTOPROPAGATE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "DATA_AUTO_PROPAGATE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_EXTENSION(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "EXTENSION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_HASCONDITIONALOUTPUTS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "HAS_CONDITIONAL_OUTPUTS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_HASHCOMPONENT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "HASH_COMPONENT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_ISMULTIPLYINGOUTPUTS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "IS_MULTIPLYING_OUTPUTS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_LOG4J_ENABLED(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "LOG4J_ENABLED",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_MAINCODECALLED(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MAIN_CODE_CALLED",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_NUMBERPARALLELIZE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NUMBER_PARALLELIZE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_PARALLELIZE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "PARALLELIZE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_PARTITIONING(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "PARTITIONING",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_PLATEFORM(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "PLATEFORM",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_REDUCE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "REDUCE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_RELEASEDATE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "RELEASE_DATE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_SCHEMAAUTOPROPAGATE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SCHEMA_AUTO_PROPAGATE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_SERIAL(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SERIAL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_SHORTNAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SHORTNAME",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_SINGLETON(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SINGLETON",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_STARTABLE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "STARTABLE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_STATUS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "STATUS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_SUBJOBCOLOR(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SUBJOB_COLOR",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_SUBJOBTITLECOLOR(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SUBJOB_TITLE_COLOR",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_SUPPORTS_DB_TYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SUPPORTS_DB_TYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_TECHNICAL(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "TECHNICAL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_TSTATCATCHERSTATS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "TSTATCATCHER_STATS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_TYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "TYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_VERSION(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "VERSION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_VISIBLE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "VISIBLE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_EQUIVALENT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "EQUIVALENT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_INPUTTYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "INPUTTYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_OUTPUTTYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "OUTPUTTYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_SPARKACTION(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SPARKACTION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getHEADERType_ACTIVE_DB_COLUMNS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "ACTIVE_DB_COLUMNS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (importsTypeEClass, 
           source, 
           new String[] {
             "name", "IMPORTS_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getIMPORTSType_IMPORT(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "IMPORT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTSType_IMPORTS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "IMPORTS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTSType_REQUIREDIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "REQUIRED_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (importTypeEClass, 
           source, 
           new String[] {
             "name", "IMPORT_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getIMPORTType_INSTALL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "INSTALL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_URL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "URL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_BundleID(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "BundleID",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_MESSAGE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MESSAGE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_MODULE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MODULE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_MODULEGROUP(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MODULE_GROUP",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_MRREQUIRED(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MRREQUIRED",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_NAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NAME",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_REQUIRED(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "REQUIRED",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_REQUIREDIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "REQUIRED_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_SHOW(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SHOW",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getIMPORTType_UrlPath(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "UrlPath",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (installTypeEClass, 
           source, 
           new String[] {
             "name", "INSTALL_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getINSTALLType_COMMAND(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "COMMAND",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getINSTALLType_OS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "OS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (itemsTypeEClass, 
           source, 
           new String[] {
             "name", "ITEMS_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getITEMSType_ITEM(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ITEM",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMSType_BASEDONINPUTSCHEMAS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "BASED_ON_INPUT_SCHEMAS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMSType_BASEDONSCHEMA(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "BASED_ON_SCHEMA",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMSType_BASEDONSUBJOBSTARTS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "BASED_ON_SUBJOB_STARTS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMSType_COLUMNSBASEDONSCHEMA(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "COLUMNS_BASED_ON_SCHEMA",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMSType_DEFAULT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "DEFAULT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (itemTypeEClass, 
           source, 
           new String[] {
             "name", "ITEM_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getITEMType_ITEMS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ITEMS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_CONTEXT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CONTEXT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_DISPLAYNAMEASVALUE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "DISPLAY_NAME_AS_VALUE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_FIELD(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "FIELD",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_FILTER(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "FILTER",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_NAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NAME",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_NOCONTEXTASSIST(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NO_CONTEXT_ASSIST",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_NOTREADONLYIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NOT_READONLY_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_NOTSHOWIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NOT_SHOW_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_RAW(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "RAW",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_READONLY(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "READONLY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_READONLYIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "READONLY_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_REPOSITORYITEM(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "REPOSITORY_ITEM",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_SHOWIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SHOW_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getITEMType_VALUE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "VALUE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (javacommandTypeEClass, 
           source, 
           new String[] {
             "name", "JAVACOMMAND_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getJAVACOMMANDType_ARG(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ARG",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getJAVACOMMANDType_CLASS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CLASS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getJAVACOMMANDType_FUNCTION(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "FUNCTION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getJAVACOMMANDType_JAR(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "JAR",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (linktoTypeEClass, 
           source, 
           new String[] {
             "name", "LINK_TO_._type",
             "kind", "empty"
           });	
        addAnnotation
          (getLINKTOType_CTYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CTYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getLINKTOType_NAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NAME",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (parametersTypeEClass, 
           source, 
           new String[] {
             "name", "PARAMETERS_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getPARAMETERSType_PARAMETER(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PARAMETER",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (parameterTypeEClass, 
           source, 
           new String[] {
             "name", "PARAMETER_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getPARAMETERType_DEFAULT(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DEFAULT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_ITEMS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ITEMS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_TABLE(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TABLE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_JAVACOMMAND(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "JAVACOMMAND",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_BACKGROUND(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "BACKGROUND",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_COLOR(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "COLOR",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_CONTEXT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CONTEXT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_CONTEXTMODE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CONTEXT_MODE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_DYNAMICSETTINGS(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "DYNAMIC_SETTINGS",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_FIELD(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "FIELD",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_FILTER(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "FILTER",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_GROUP(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "GROUP",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_LOG4J_ENABLED(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "LOG4J_ENABLED",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_MAXLENGTH(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "MAX_LENGTH",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_NAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NAME",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_NBLINES(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NB_LINES",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_NOCONTEXTASSIST(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NO_CONTEXT_ASSIST",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_NOTREADONLYIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NOT_READONLY_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_NOTSHOWIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NOT_SHOW_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_NUMROW(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NUM_ROW",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_RAW(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "RAW",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_READONLY(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "READONLY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_READONLYIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "READONLY_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_REPOSITORYVALUE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "REPOSITORY_VALUE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_REQUIRED(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "REQUIRED",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_REQUIREDIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "REQUIRED_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_SHOW(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SHOW",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getPARAMETERType_SHOWIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SHOW_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (plugindependenciesTypeEClass, 
           source, 
           new String[] {
             "name", "PLUGINDEPENDENCIES_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getPLUGINDEPENDENCIESType_PLUGINDEPENDENCY(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PLUGINDEPENDENCY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (plugindependencyTypeEClass, 
           source, 
           new String[] {
             "name", "PLUGINDEPENDENCY_._type",
             "kind", "empty"
           });	
        addAnnotation
          (getPLUGINDEPENDENCYType_ID(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "ID",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (returnsTypeEClass, 
           source, 
           new String[] {
             "name", "RETURNS_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getRETURNSType_RETURN(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RETURN",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (returnTypeEClass, 
           source, 
           new String[] {
             "name", "RETURN_._type",
             "kind", "empty"
           });	
        addAnnotation
          (getRETURNType_AVAILABILITY(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "AVAILABILITY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getRETURNType_NAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NAME",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getRETURNType_SHOWIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SHOW_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getRETURNType_TYPE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "TYPE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (sqltemplatesTypeEClass, 
           source, 
           new String[] {
             "name", "SQLTEMPLATES_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getSQLTEMPLATESType_SQLTEMPLATE(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SQLTEMPLATE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getSQLTEMPLATESType_DB(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "DB",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (sqltemplateTypeEClass, 
           source, 
           new String[] {
             "name", "SQLTEMPLATE_._type",
             "kind", "empty"
           });	
        addAnnotation
          (getSQLTEMPLATEType_CONTENT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CONTENT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getSQLTEMPLATEType_NAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NAME",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (tableTypeEClass, 
           source, 
           new String[] {
             "name", "TABLE_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getTABLEType_COLUMN(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "COLUMN",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTABLEType_IF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTABLEType_NOTIF(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NOT_IF",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTABLEType_READONLYCOLUMNPOSITION(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "READ_ONLY_COLUMN_POSITION",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTABLEType_READONLY(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "READONLY",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (templateparamTypeEClass, 
           source, 
           new String[] {
             "name", "TEMPLATE_PARAM_._type",
             "kind", "empty"
           });	
        addAnnotation
          (getTEMPLATEPARAMType_SOURCE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "SOURCE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTEMPLATEPARAMType_TARGET(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "TARGET",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTEMPLATEPARAMType_VALUE(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "VALUE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (templatesTypeEClass, 
           source, 
           new String[] {
             "name", "TEMPLATES_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getTEMPLATESType_TEMPLATE(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TEMPLATE",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTEMPLATESType_TEMPLATEPARAM(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TEMPLATE_PARAM",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTEMPLATESType_CONNECTOR(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "CONNECTOR",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTEMPLATESType_INPUT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "INPUT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTEMPLATESType_LOOKUP(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "LOOKUP",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTEMPLATESType_OUTPUT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "OUTPUT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (templateTypeEClass, 
           source, 
           new String[] {
             "name", "TEMPLATE_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getTEMPLATEType_LINKTO(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LINK_TO",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTEMPLATEType_COMPONENT(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "COMPONENT",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTEMPLATEType_NAME(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "NAME",
             "namespace", "##targetNamespace"
           });
    }

} //ComponentPackageImpl
