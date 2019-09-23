/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.softwaredeployment.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;

import org.talend.core.model.metadata.builder.connection.impl.ConnectionPackageImpl;

import org.talend.cwm.constants.ConstantsPackage;

import org.talend.cwm.constants.impl.ConstantsPackageImpl;

import org.talend.cwm.relational.impl.RelationalPackageImpl;

import org.talend.cwm.softwaredeployment.SoftwaredeploymentFactory;
import org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage;
import org.talend.cwm.softwaredeployment.TdDataManager;
import org.talend.cwm.softwaredeployment.TdMachine;
import org.talend.cwm.softwaredeployment.TdSoftwareSystem;

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

import orgomg.cwm.foundation.typemapping.TypemappingPackage;

import orgomg.cwm.management.warehouseoperation.WarehouseoperationPackage;

import orgomg.cwm.management.warehouseprocess.WarehouseprocessPackage;

import orgomg.cwm.objectmodel.behavioral.BehavioralPackage;

import orgomg.cwm.objectmodel.core.CorePackage;

import orgomg.cwm.objectmodel.instance.InstancePackage;

import orgomg.cwm.objectmodel.relationships.RelationshipsPackage;

import orgomg.cwm.resource.multidimensional.MultidimensionalPackage;

import orgomg.cwm.resource.record.RecordPackage;

import orgomg.cwm.resource.relational.RelationalPackage;

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
public class SoftwaredeploymentPackageImpl extends EPackageImpl implements SoftwaredeploymentPackage {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdDataManagerEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdSoftwareSystemEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tdMachineEClass = null;

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
     * @see org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private SoftwaredeploymentPackageImpl() {
        super(eNS_URI, SoftwaredeploymentFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link SoftwaredeploymentPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static SoftwaredeploymentPackage init() {
        if (isInited)
            return (SoftwaredeploymentPackage) EPackage.Registry.INSTANCE.getEPackage(SoftwaredeploymentPackage.eNS_URI);

        // Obtain or create and register package
        SoftwaredeploymentPackageImpl theSoftwaredeploymentPackage = (SoftwaredeploymentPackageImpl) (EPackage.Registry.INSTANCE
                .get(eNS_URI) instanceof SoftwaredeploymentPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
                : new SoftwaredeploymentPackageImpl());

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
        orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.eINSTANCE.eClass();
        TypemappingPackage.eINSTANCE.eClass();
        RelationalPackage.eINSTANCE.eClass();
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
        RelationalPackageImpl theRelationalPackage_1 = (RelationalPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.relational.RelationalPackage.eNS_URI) instanceof RelationalPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.relational.RelationalPackage.eNS_URI)
                : org.talend.cwm.relational.RelationalPackage.eINSTANCE);
        ConstantsPackageImpl theConstantsPackage = (ConstantsPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ConstantsPackage.eNS_URI) instanceof ConstantsPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(ConstantsPackage.eNS_URI) : ConstantsPackage.eINSTANCE);
        XmlPackageImpl theXmlPackage_1 = (XmlPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.xml.XmlPackage.eNS_URI) instanceof XmlPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.xml.XmlPackage.eNS_URI) : org.talend.cwm.xml.XmlPackage.eINSTANCE);

        // Create package meta-data objects
        theSoftwaredeploymentPackage.createPackageContents();
        theConnectionPackage.createPackageContents();
        theRelationalPackage_1.createPackageContents();
        theConstantsPackage.createPackageContents();
        theXmlPackage_1.createPackageContents();

        // Initialize created meta-data
        theSoftwaredeploymentPackage.initializePackageContents();
        theConnectionPackage.initializePackageContents();
        theRelationalPackage_1.initializePackageContents();
        theConstantsPackage.initializePackageContents();
        theXmlPackage_1.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theSoftwaredeploymentPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(SoftwaredeploymentPackage.eNS_URI, theSoftwaredeploymentPackage);
        return theSoftwaredeploymentPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdDataManager() {
        return tdDataManagerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdSoftwareSystem() {
        return tdSoftwareSystemEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTdMachine() {
        return tdMachineEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SoftwaredeploymentFactory getSoftwaredeploymentFactory() {
        return (SoftwaredeploymentFactory) getEFactoryInstance();
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
        tdDataManagerEClass = createEClass(TD_DATA_MANAGER);

        tdSoftwareSystemEClass = createEClass(TD_SOFTWARE_SYSTEM);

        tdMachineEClass = createEClass(TD_MACHINE);
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
        orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage theSoftwaredeploymentPackage_1 = (orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage) EPackage.Registry.INSTANCE
                .getEPackage(orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        tdDataManagerEClass.getESuperTypes().add(theSoftwaredeploymentPackage_1.getDataManager());
        tdSoftwareSystemEClass.getESuperTypes().add(theSoftwaredeploymentPackage_1.getSoftwareSystem());
        tdMachineEClass.getESuperTypes().add(theSoftwaredeploymentPackage_1.getMachine());

        // Initialize classes and features; add operations and parameters
        initEClass(tdDataManagerEClass, TdDataManager.class, "TdDataManager", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);

        initEClass(tdSoftwareSystemEClass, TdSoftwareSystem.class, "TdSoftwareSystem", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);

        initEClass(tdMachineEClass, TdMachine.class, "TdMachine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    }

} //SoftwaredeploymentPackageImpl
