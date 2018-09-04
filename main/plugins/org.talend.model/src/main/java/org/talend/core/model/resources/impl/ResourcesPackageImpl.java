/**
 */
package org.talend.core.model.resources.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.gmf.runtime.notation.NotationPackage;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;

import org.talend.core.model.properties.PropertiesPackage;

import org.talend.core.model.resources.ResourceItem;
import org.talend.core.model.resources.ResourcesFactory;
import org.talend.core.model.resources.ResourcesPackage;

import org.talend.designer.business.model.business.BusinessPackage;

import org.talend.designer.core.model.utils.emf.component.ComponentPackage;

import org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage;

import org.talend.designer.joblet.model.JobletPackage;

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
public class ResourcesPackageImpl extends EPackageImpl implements ResourcesPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass resourceItemEClass = null;

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
     * @see org.talend.core.model.resources.ResourcesPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ResourcesPackageImpl() {
        super(eNS_URI, ResourcesFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link ResourcesPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ResourcesPackage init() {
        if (isInited) return (ResourcesPackage)EPackage.Registry.INSTANCE.getEPackage(ResourcesPackage.eNS_URI);

        // Obtain or create and register package
        ResourcesPackageImpl theResourcesPackage = (ResourcesPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ResourcesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ResourcesPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        BusinessPackage.eINSTANCE.eClass();
        ComponentPackage.eINSTANCE.eClass();
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
        EcorePackage.eINSTANCE.eClass();
        JobletPackage.eINSTANCE.eClass();
        ConnectionPackage.eINSTANCE.eClass();
        NotationPackage.eINSTANCE.eClass();
        PropertiesPackage.eINSTANCE.eClass();
        TalendFilePackage.eINSTANCE.eClass();
        XMLTypePackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theResourcesPackage.createPackageContents();

        // Initialize created meta-data
        theResourcesPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theResourcesPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(ResourcesPackage.eNS_URI, theResourcesPackage);
        return theResourcesPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getResourceItem() {
        return resourceItemEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceItem_BindingExtension() {
        return (EAttribute)resourceItemEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourcesFactory getResourcesFactory() {
        return (ResourcesFactory)getEFactoryInstance();
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
        resourceItemEClass = createEClass(RESOURCE_ITEM);
        createEAttribute(resourceItemEClass, RESOURCE_ITEM__BINDING_EXTENSION);
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
        PropertiesPackage thePropertiesPackage = (PropertiesPackage)EPackage.Registry.INSTANCE.getEPackage(PropertiesPackage.eNS_URI);
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        resourceItemEClass.getESuperTypes().add(thePropertiesPackage.getFileItem());

        // Initialize classes and features; add operations and parameters
        initEClass(resourceItemEClass, ResourceItem.class, "ResourceItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getResourceItem_BindingExtension(), theXMLTypePackage.getString(), "bindingExtension", null, 0, 1, ResourceItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Create resource
        createResource(eNS_URI);
    }

} //ResourcesPackageImpl
