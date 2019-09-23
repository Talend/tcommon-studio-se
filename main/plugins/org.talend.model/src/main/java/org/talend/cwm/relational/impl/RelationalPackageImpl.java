/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational.impl;

import java.util.HashMap;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;

import org.talend.core.model.metadata.builder.connection.impl.ConnectionPackageImpl;

import org.talend.cwm.constants.ConstantsPackage;

import org.talend.cwm.constants.impl.ConstantsPackageImpl;

import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.RelationalPackage;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdProcedure;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdTrigger;
import org.talend.cwm.relational.TdView;

import org.talend.cwm.softwaredeployment.impl.SoftwaredeploymentPackageImpl;

import org.talend.cwm.xml.impl.XmlPackageImpl;

import orgomg.cwm.analysis.businessnomenclature.BusinessnomenclaturePackage;

import orgomg.cwm.analysis.datamining.DataminingPackage;

import orgomg.cwm.analysis.informationvisualization.InformationvisualizationPackage;

import orgomg.cwm.analysis.olap.OlapPackage;

import orgomg.cwm.analysis.transformation.TransformationPackage;

import orgomg.cwm.foundation.businessinformation.BusinessinformationPackage;

import orgomg.cwm.foundation.datatypes.DatatypesPackage;

import orgomg.cwm.foundation.expressions.ExpressionsPackage;

import orgomg.cwm.foundation.keysindexes.KeysindexesPackage;

import orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage;

import orgomg.cwm.foundation.typemapping.TypemappingPackage;

import orgomg.cwm.management.warehouseoperation.WarehouseoperationPackage;

import orgomg.cwm.management.warehouseprocess.WarehouseprocessPackage;

import orgomg.cwm.objectmodel.behavioral.BehavioralPackage;

import orgomg.cwm.objectmodel.core.CorePackage;

import orgomg.cwm.objectmodel.instance.InstancePackage;

import orgomg.cwm.objectmodel.relationships.RelationshipsPackage;

import orgomg.cwm.resource.multidimensional.MultidimensionalPackage;

import orgomg.cwm.resource.record.RecordPackage;

import orgomg.cwm.resource.relational.enumerations.EnumerationsPackage;

import orgomg.cwm.resource.xml.XmlPackage;

import orgomg.cwmmip.CwmmipPackage;

import orgomg.cwmx.analysis.informationreporting.InformationreportingPackage;

import orgomg.cwmx.analysis.informationset.InformationsetPackage;

import orgomg.cwmx.foundation.er.ErPackage;

import orgomg.cwmx.resource.coboldata.CoboldataPackage;

import orgomg.cwmx.resource.dmsii.DmsiiPackage;

import orgomg.cwmx.resource.essbase.EssbasePackage;

import orgomg.cwmx.resource.express.ExpressPackage;

import orgomg.cwmx.resource.imsdatabase.ImsdatabasePackage;

import orgomg.mof.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RelationalPackageImpl extends EPackageImpl implements RelationalPackage {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdTableEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdViewEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdColumnEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdSqlDataTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdTriggerEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdProcedureEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType javaHashMapEDataType = null;

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
     * @see org.talend.cwm.relational.RelationalPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private RelationalPackageImpl() {
        super(eNS_URI, RelationalFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link RelationalPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static RelationalPackage init() {
        if (isInited)
            return (RelationalPackage) EPackage.Registry.INSTANCE.getEPackage(RelationalPackage.eNS_URI);

        // Obtain or create and register package
        RelationalPackageImpl theRelationalPackage = (RelationalPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof RelationalPackageImpl ? EPackage.Registry.INSTANCE
                .get(eNS_URI) : new RelationalPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        CorePackage.eINSTANCE.eClass();
        BehavioralPackage.eINSTANCE.eClass();
        RelationshipsPackage.eINSTANCE.eClass();
        InstancePackage.eINSTANCE.eClass();
        BusinessinformationPackage.eINSTANCE.eClass();
        DatatypesPackage.eINSTANCE.eClass();
        ExpressionsPackage.eINSTANCE.eClass();
        KeysindexesPackage.eINSTANCE.eClass();
        SoftwaredeploymentPackage.eINSTANCE.eClass();
        TypemappingPackage.eINSTANCE.eClass();
        orgomg.cwm.resource.relational.RelationalPackage.eINSTANCE.eClass();
        RecordPackage.eINSTANCE.eClass();
        MultidimensionalPackage.eINSTANCE.eClass();
        XmlPackage.eINSTANCE.eClass();
        TransformationPackage.eINSTANCE.eClass();
        OlapPackage.eINSTANCE.eClass();
        DataminingPackage.eINSTANCE.eClass();
        InformationvisualizationPackage.eINSTANCE.eClass();
        BusinessnomenclaturePackage.eINSTANCE.eClass();
        WarehouseprocessPackage.eINSTANCE.eClass();
        WarehouseoperationPackage.eINSTANCE.eClass();
        ErPackage.eINSTANCE.eClass();
        CoboldataPackage.eINSTANCE.eClass();
        DmsiiPackage.eINSTANCE.eClass();
        ImsdatabasePackage.eINSTANCE.eClass();
        EssbasePackage.eINSTANCE.eClass();
        ExpressPackage.eINSTANCE.eClass();
        InformationsetPackage.eINSTANCE.eClass();
        InformationreportingPackage.eINSTANCE.eClass();
        CwmmipPackage.eINSTANCE.eClass();
        ModelPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        ConnectionPackageImpl theConnectionPackage = (ConnectionPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ConnectionPackage.eNS_URI) instanceof ConnectionPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(ConnectionPackage.eNS_URI) : ConnectionPackage.eINSTANCE);
        SoftwaredeploymentPackageImpl theSoftwaredeploymentPackage_1 = (SoftwaredeploymentPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eNS_URI) instanceof SoftwaredeploymentPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eNS_URI)
                : org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eINSTANCE);
        ConstantsPackageImpl theConstantsPackage = (ConstantsPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ConstantsPackage.eNS_URI) instanceof ConstantsPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(ConstantsPackage.eNS_URI) : ConstantsPackage.eINSTANCE);
        XmlPackageImpl theXmlPackage_1 = (XmlPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.xml.XmlPackage.eNS_URI) instanceof XmlPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.xml.XmlPackage.eNS_URI) : org.talend.cwm.xml.XmlPackage.eINSTANCE);

        // Create package meta-data objects
        theRelationalPackage.createPackageContents();
        theConnectionPackage.createPackageContents();
        theSoftwaredeploymentPackage_1.createPackageContents();
        theConstantsPackage.createPackageContents();
        theXmlPackage_1.createPackageContents();

        // Initialize created meta-data
        theRelationalPackage.initializePackageContents();
        theConnectionPackage.initializePackageContents();
        theSoftwaredeploymentPackage_1.initializePackageContents();
        theConstantsPackage.initializePackageContents();
        theXmlPackage_1.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theRelationalPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(RelationalPackage.eNS_URI, theRelationalPackage);
        return theRelationalPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdTable() {
        return tdTableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdView() {
        return tdViewEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdColumn() {
        return tdColumnEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTdColumn_SqlDataType() {
        return (EReference) tdColumnEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdSqlDataType() {
        return tdSqlDataTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdSqlDataType_JavaDataType() {
        return (EAttribute) tdSqlDataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdSqlDataType_Nullable() {
        return (EAttribute) tdSqlDataTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdSqlDataType_UnsignedAttribute() {
        return (EAttribute) tdSqlDataTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdSqlDataType_CaseSensitive() {
        return (EAttribute) tdSqlDataTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdSqlDataType_AutoIncrement() {
        return (EAttribute) tdSqlDataTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdSqlDataType_LocalTypeName() {
        return (EAttribute) tdSqlDataTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdSqlDataType_Searchable() {
        return (EAttribute) tdSqlDataTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdTrigger() {
        return tdTriggerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdProcedure() {
        return tdProcedureEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdExpression() {
        return tdExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdExpression_Version() {
        return (EAttribute) tdExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdExpression_ModificationDate() {
        return (EAttribute) tdExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdExpression_Name() {
        return (EAttribute) tdExpressionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTdExpression_ExpressionVariableMap() {
        return (EAttribute) tdExpressionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getjavaHashMap() {
        return javaHashMapEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelationalFactory getRelationalFactory() {
        return (RelationalFactory) getEFactoryInstance();
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
        if (isCreated)
            return;
        isCreated = true;

        // Create classes and their features
        tdTableEClass = createEClass(TD_TABLE);

        tdViewEClass = createEClass(TD_VIEW);

        tdColumnEClass = createEClass(TD_COLUMN);
        createEReference(tdColumnEClass, TD_COLUMN__SQL_DATA_TYPE);

        tdSqlDataTypeEClass = createEClass(TD_SQL_DATA_TYPE);
        createEAttribute(tdSqlDataTypeEClass, TD_SQL_DATA_TYPE__JAVA_DATA_TYPE);
        createEAttribute(tdSqlDataTypeEClass, TD_SQL_DATA_TYPE__NULLABLE);
        createEAttribute(tdSqlDataTypeEClass, TD_SQL_DATA_TYPE__UNSIGNED_ATTRIBUTE);
        createEAttribute(tdSqlDataTypeEClass, TD_SQL_DATA_TYPE__CASE_SENSITIVE);
        createEAttribute(tdSqlDataTypeEClass, TD_SQL_DATA_TYPE__AUTO_INCREMENT);
        createEAttribute(tdSqlDataTypeEClass, TD_SQL_DATA_TYPE__LOCAL_TYPE_NAME);
        createEAttribute(tdSqlDataTypeEClass, TD_SQL_DATA_TYPE__SEARCHABLE);

        tdTriggerEClass = createEClass(TD_TRIGGER);

        tdProcedureEClass = createEClass(TD_PROCEDURE);

        tdExpressionEClass = createEClass(TD_EXPRESSION);
        createEAttribute(tdExpressionEClass, TD_EXPRESSION__VERSION);
        createEAttribute(tdExpressionEClass, TD_EXPRESSION__MODIFICATION_DATE);
        createEAttribute(tdExpressionEClass, TD_EXPRESSION__NAME);
        createEAttribute(tdExpressionEClass, TD_EXPRESSION__EXPRESSION_VARIABLE_MAP);

        // Create data types
        javaHashMapEDataType = createEDataType(JAVA_HASH_MAP);
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
        if (isInitialized)
            return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        ConnectionPackage theConnectionPackage = (ConnectionPackage) EPackage.Registry.INSTANCE
                .getEPackage(ConnectionPackage.eNS_URI);
        orgomg.cwm.resource.relational.RelationalPackage theRelationalPackage_1 = (orgomg.cwm.resource.relational.RelationalPackage) EPackage.Registry.INSTANCE
                .getEPackage(orgomg.cwm.resource.relational.RelationalPackage.eNS_URI);
        CorePackage theCorePackage = (CorePackage) EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI);
        EnumerationsPackage theEnumerationsPackage = (EnumerationsPackage) EPackage.Registry.INSTANCE
                .getEPackage(EnumerationsPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        tdTableEClass.getESuperTypes().add(theConnectionPackage.getMetadataTable());
        tdTableEClass.getESuperTypes().add(theRelationalPackage_1.getTable());
        tdViewEClass.getESuperTypes().add(theConnectionPackage.getMetadataTable());
        tdViewEClass.getESuperTypes().add(theRelationalPackage_1.getView());
        tdColumnEClass.getESuperTypes().add(theConnectionPackage.getMetadataColumn());
        tdSqlDataTypeEClass.getESuperTypes().add(theRelationalPackage_1.getSQLSimpleType());
        tdTriggerEClass.getESuperTypes().add(theRelationalPackage_1.getTrigger());
        tdProcedureEClass.getESuperTypes().add(theRelationalPackage_1.getProcedure());
        tdExpressionEClass.getESuperTypes().add(theCorePackage.getExpression());

        // Initialize classes and features; add operations and parameters
        initEClass(tdTableEClass, TdTable.class, "TdTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(tdViewEClass, TdView.class, "TdView", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(tdColumnEClass, TdColumn.class, "TdColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTdColumn_SqlDataType(), this.getTdSqlDataType(), null, "sqlDataType", null, 0, 1, TdColumn.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        EOperation op = addEOperation(tdColumnEClass, null, "setContentType", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theCorePackage.getString(), "contentType", 0, 1, IS_UNIQUE, IS_ORDERED);

        addEOperation(tdColumnEClass, theCorePackage.getString(), "getContentType", 0, 1, IS_UNIQUE, IS_ORDERED);

        addEOperation(tdColumnEClass, ecorePackage.getEInt(), "getJavaType", 0, 1, IS_UNIQUE, IS_ORDERED);

        initEClass(tdSqlDataTypeEClass, TdSqlDataType.class, "TdSqlDataType", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTdSqlDataType_JavaDataType(), ecorePackage.getEInt(), "javaDataType", null, 0, 1, TdSqlDataType.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTdSqlDataType_Nullable(), theEnumerationsPackage.getNullableType(), "nullable", null, 0, 1,
                TdSqlDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getTdSqlDataType_UnsignedAttribute(), theCorePackage.getBoolean(), "unsignedAttribute", null, 0, 1,
                TdSqlDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getTdSqlDataType_CaseSensitive(), theCorePackage.getBoolean(), "caseSensitive", null, 0, 1,
                TdSqlDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getTdSqlDataType_AutoIncrement(), theCorePackage.getBoolean(), "autoIncrement", null, 0, 1,
                TdSqlDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getTdSqlDataType_LocalTypeName(), theCorePackage.getString(), "localTypeName", null, 0, 1,
                TdSqlDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getTdSqlDataType_Searchable(), ecorePackage.getEShort(), "searchable", null, 0, 1, TdSqlDataType.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tdTriggerEClass, TdTrigger.class, "TdTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(tdProcedureEClass, TdProcedure.class, "TdProcedure", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(tdExpressionEClass, TdExpression.class, "TdExpression", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTdExpression_Version(), theCorePackage.getString(), "version", null, 0, 1, TdExpression.class,
                !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTdExpression_ModificationDate(), theCorePackage.getTime(), "modificationDate", null, 0, 1,
                TdExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);
        initEAttribute(getTdExpression_Name(), theCorePackage.getString(), "name", null, 0, 1, TdExpression.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTdExpression_ExpressionVariableMap(), this.getjavaHashMap(), "expressionVariableMap", null, 0, 1,
                TdExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);

        // Initialize data types
        initEDataType(javaHashMapEDataType, HashMap.class, "javaHashMap", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS,
                "java.util.HashMap<java.lang.String, java.lang.String>");

        // Create annotations
        // http://www.eclipse.org/emf/2002/GenModel
        createGenModelAnnotations();
    }

    /**
     * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/GenModel</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createGenModelAnnotations() {
        String source = "http://www.eclipse.org/emf/2002/GenModel";
        addAnnotation(tdColumnEClass, source, new String[] { "documentation", "defines a DB related column" });
        addAnnotation(
                tdColumnEClass.getEOperations().get(0),
                source,
                new String[] {
                        "documentation",
                        "The type of the content of the column. This type is a meta-information either set by the user who knows what type of data is contained in the column, or infered from the data.\r\nThis is used for Datamining may be Nominal, Interval,...." });
        addAnnotation(
                tdColumnEClass.getEOperations().get(1),
                source,
                new String[] {
                        "documentation",
                        "The type of the content of the column. This type is a meta-information either set by the user who knows what type of data is contained in the column, or infered from the data." });
        addAnnotation(tdColumnEClass.getEOperations().get(2), source, new String[] { "documentation",
                "@deprecated use getTdSqlDataType.javaDataType\r\nSQL data type from java.sql.Types." });
        addAnnotation(
                tdSqlDataTypeEClass,
                source,
                new String[] { "documentation",
                        "defines the DB and java types and attributes of the column\r\nthe Name attribute is set to the JDBC TYPE_NAME value" });
        addAnnotation(getTdSqlDataType_JavaDataType(), source, new String[] { "documentation",
                "SQL data type from java.sql.Types.\r\nthis may not be changed by the user" });
        addAnnotation(
                getTdSqlDataType_Nullable(),
                source,
                new String[] {
                        "documentation",
                        "Is this column nullable, or not or unknow.\r\nThe value in one of\r\njava.sql.DatabaseMetaData.columnNoNulls \r\njava.sql.DatabaseMetaData.columnNullable \r\njava.sql.DatabaseMetaData.columnNullableUnknown \r\n\r\nThis may not be changed by the user" });
        addAnnotation(getTdSqlDataType_UnsignedAttribute(), source, new String[] { "documentation", "is it unsigned?" });
        addAnnotation(getTdSqlDataType_AutoIncrement(), source, new String[] { "documentation",
                "can it be used for an auto-increment value?" });
        addAnnotation(getTdSqlDataType_LocalTypeName(), source, new String[] { "documentation",
                "localized version of the type name (may be null)" });
        addAnnotation(getTdSqlDataType_Searchable(), source, new String[] { "documentation",
                "can you use \"WHERE\" based on this type." });
    }

} //RelationalPackageImpl
