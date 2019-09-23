/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.constants.impl;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.impl.ConnectionPackageImpl;
import org.talend.cwm.constants.ConstantsFactory;
import org.talend.cwm.constants.ConstantsPackage;
import org.talend.cwm.constants.DevelopmentStatus;
import org.talend.cwm.relational.impl.RelationalPackageImpl;
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
public class ConstantsPackageImpl extends EPackageImpl implements ConstantsPackage {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum developmentStatusEEnum = null;

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
     * @see org.talend.cwm.constants.ConstantsPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ConstantsPackageImpl() {
        super(eNS_URI, ConstantsFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link ConstantsPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ConstantsPackage init() {
        if (isInited)
            return (ConstantsPackage) EPackage.Registry.INSTANCE.getEPackage(ConstantsPackage.eNS_URI);

        // Obtain or create and register package
        ConstantsPackageImpl theConstantsPackage = (ConstantsPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ConstantsPackageImpl ? EPackage.Registry.INSTANCE
                .get(eNS_URI) : new ConstantsPackageImpl());

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
        SoftwaredeploymentPackageImpl theSoftwaredeploymentPackage_1 = (SoftwaredeploymentPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eNS_URI) instanceof SoftwaredeploymentPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eNS_URI)
                : org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage.eINSTANCE);
        XmlPackageImpl theXmlPackage_1 = (XmlPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.xml.XmlPackage.eNS_URI) instanceof XmlPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(org.talend.cwm.xml.XmlPackage.eNS_URI) : org.talend.cwm.xml.XmlPackage.eINSTANCE);

        // Create package meta-data objects
        theConstantsPackage.createPackageContents();
        theConnectionPackage.createPackageContents();
        theRelationalPackage_1.createPackageContents();
        theSoftwaredeploymentPackage_1.createPackageContents();
        theXmlPackage_1.createPackageContents();

        // Initialize created meta-data
        theConstantsPackage.initializePackageContents();
        theConnectionPackage.initializePackageContents();
        theRelationalPackage_1.initializePackageContents();
        theSoftwaredeploymentPackage_1.initializePackageContents();
        theXmlPackage_1.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theConstantsPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(ConstantsPackage.eNS_URI, theConstantsPackage);
        return theConstantsPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getDevelopmentStatus() {
        return developmentStatusEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConstantsFactory getConstantsFactory() {
        return (ConstantsFactory) getEFactoryInstance();
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

        // Create enums
        developmentStatusEEnum = createEEnum(DEVELOPMENT_STATUS);
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

        // Initialize enums and add enum literals
        initEEnum(developmentStatusEEnum, DevelopmentStatus.class, "DevelopmentStatus");
        addEEnumLiteral(developmentStatusEEnum, DevelopmentStatus.DRAFT);
        addEEnumLiteral(developmentStatusEEnum, DevelopmentStatus.PROD);
    }

} //ConstantsPackageImpl
