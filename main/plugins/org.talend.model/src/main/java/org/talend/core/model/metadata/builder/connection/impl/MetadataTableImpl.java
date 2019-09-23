/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.helper.TableHelper;
import orgomg.cwm.analysis.transformation.ClassifierFeatureMap;
import orgomg.cwm.analysis.transformation.ClassifierMap;
import orgomg.cwm.analysis.transformation.TransformationPackage;
import orgomg.cwm.foundation.datatypes.DatatypesPackage;
import orgomg.cwm.foundation.datatypes.TypeAlias;
import orgomg.cwm.foundation.expressions.ExpressionNode;
import orgomg.cwm.foundation.expressions.ExpressionsPackage;
import orgomg.cwm.foundation.keysindexes.Index;
import orgomg.cwm.foundation.keysindexes.KeysindexesPackage;
import orgomg.cwm.foundation.typemapping.TypeMapping;
import orgomg.cwm.foundation.typemapping.TypemappingPackage;
import orgomg.cwm.objectmodel.behavioral.BehavioralPackage;
import orgomg.cwm.objectmodel.behavioral.Parameter;
import orgomg.cwm.objectmodel.core.Classifier;
import orgomg.cwm.objectmodel.core.CorePackage;
import orgomg.cwm.objectmodel.core.Feature;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.StructuralFeature;
import orgomg.cwm.objectmodel.instance.Instance;
import orgomg.cwm.objectmodel.instance.InstancePackage;
import orgomg.cwm.objectmodel.relationships.Generalization;
import orgomg.cwm.objectmodel.relationships.RelationshipsPackage;
import orgomg.cwmx.foundation.er.Domain;
import orgomg.cwmx.foundation.er.ErPackage;
import orgomg.cwmx.resource.express.ExpressPackage;
import orgomg.cwmx.resource.express.SimpleDimension;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Metadata Table</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getOwnedElement <em>Owned Element</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#isIsAbstract <em>Is Abstract</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getFeature <em>Feature</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getStructuralFeature <em>Structural Feature</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getGeneralization <em>Generalization</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getSpecialization <em>Specialization</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getInstance <em>Instance</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getAlias <em>Alias</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getExpressionNode <em>Expression Node</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getMappingFrom <em>Mapping From</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getMappingTo <em>Mapping To</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getClassifierMap <em>Classifier Map</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getCfMap <em>Cf Map</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getDomain <em>Domain</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getSimpleDimension <em>Simple Dimension</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getSourceName <em>Source Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getTableType <em>Table Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#isAttachedCDC <em>Attached CDC</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#isActivatedCDC <em>Activated CDC</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getColumns <em>Columns</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getAdditionalProperties <em>Additional Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MetadataTableImpl extends AbstractMetadataObjectImpl implements MetadataTable {

    /**
     * The cached value of the '{@link #getOwnedElement() <em>Owned Element</em>}' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOwnedElement()
     * @generated
     * @ordered
     */
    protected EList<ModelElement> ownedElement;

    /**
     * The default value of the '{@link #isIsAbstract() <em>Is Abstract</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isIsAbstract()
     * @generated
     * @ordered
     */
    protected static final boolean IS_ABSTRACT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsAbstract() <em>Is Abstract</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isIsAbstract()
     * @generated
     * @ordered
     */
    protected boolean isAbstract = IS_ABSTRACT_EDEFAULT;

    /**
     * The cached value of the '{@link #getFeature() <em>Feature</em>}' containment reference list. <!-- begin-user-doc
     * --> This is not generated because this type has been specialized in this class so that getColumns returns alist
     * of Columns changed for EList<Feature> to EList<? extends Feature>.<br>
     * see getFeature() and getColumns()
     *
     * <!-- end-user-doc -->
     *
     * @see #getFeature()
     * @generated NOT
     * @ordered
     */
    protected EList<? extends Feature> feature;

    /**
     * The cached value of the '{@link #getStructuralFeature() <em>Structural Feature</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getStructuralFeature()
     * @generated
     * @ordered
     */
    protected EList<StructuralFeature> structuralFeature;

    /**
     * The cached value of the '{@link #getParameter() <em>Parameter</em>}' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getParameter()
     * @generated
     * @ordered
     */
    protected EList<Parameter> parameter;

    /**
     * The cached value of the '{@link #getGeneralization() <em>Generalization</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getGeneralization()
     * @generated
     * @ordered
     */
    protected EList<Generalization> generalization;

    /**
     * The cached value of the '{@link #getSpecialization() <em>Specialization</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getSpecialization()
     * @generated
     * @ordered
     */
    protected EList<Generalization> specialization;

    /**
     * The cached value of the '{@link #getInstance() <em>Instance</em>}' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getInstance()
     * @generated
     * @ordered
     */
    protected EList<Instance> instance;

    /**
     * The cached value of the '{@link #getAlias() <em>Alias</em>}' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getAlias()
     * @generated
     * @ordered
     */
    protected EList<TypeAlias> alias;

    /**
     * The cached value of the '{@link #getExpressionNode() <em>Expression Node</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getExpressionNode()
     * @generated
     * @ordered
     */
    protected EList<ExpressionNode> expressionNode;

    /**
     * The cached value of the '{@link #getMappingFrom() <em>Mapping From</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMappingFrom()
     * @generated
     * @ordered
     */
    protected EList<TypeMapping> mappingFrom;

    /**
     * The cached value of the '{@link #getMappingTo() <em>Mapping To</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMappingTo()
     * @generated
     * @ordered
     */
    protected EList<TypeMapping> mappingTo;

    /**
     * The cached value of the '{@link #getClassifierMap() <em>Classifier Map</em>}' reference list.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getClassifierMap()
     * @generated
     * @ordered
     */
    protected EList<ClassifierMap> classifierMap;

    /**
     * The cached value of the '{@link #getCfMap() <em>Cf Map</em>}' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getCfMap()
     * @generated
     * @ordered
     */
    protected EList<ClassifierFeatureMap> cfMap;

    /**
     * The cached value of the '{@link #getDomain() <em>Domain</em>}' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDomain()
     * @generated
     * @ordered
     */
    protected EList<Domain> domain;

    /**
     * The cached value of the '{@link #getSimpleDimension() <em>Simple Dimension</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getSimpleDimension()
     * @generated
     * @ordered
     */
    protected EList<SimpleDimension> simpleDimension;

    /**
     * The cached value of the '{@link #getIndex() <em>Index</em>}' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getIndex()
     * @generated
     * @ordered
     */
    protected EList<Index> index;

    /**
     * The default value of the '{@link #getSourceName() <em>Source Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getSourceName()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_NAME_EDEFAULT = null;

    /**
     * The default value of the '{@link #getTableType() <em>Table Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getTableType()
     * @generated
     * @ordered
     */
    protected static final String TABLE_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTableType() <em>Table Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getTableType()
     * @generated
     * @ordered
     */
    protected String tableType = TABLE_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #isAttachedCDC() <em>Attached CDC</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isAttachedCDC()
     * @generated
     * @ordered
     */
    protected static final boolean ATTACHED_CDC_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isAttachedCDC() <em>Attached CDC</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isAttachedCDC()
     * @generated
     * @ordered
     */
    protected boolean attachedCDC = ATTACHED_CDC_EDEFAULT;

    /**
     * The default value of the '{@link #isActivatedCDC() <em>Activated CDC</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isActivatedCDC()
     * @generated
     * @ordered
     */
    protected static final boolean ACTIVATED_CDC_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isActivatedCDC() <em>Activated CDC</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isActivatedCDC()
     * @generated
     * @ordered
     */
    protected boolean activatedCDC = ACTIVATED_CDC_EDEFAULT;

    /**
     * The cached value of the '{@link #getAdditionalProperties() <em>Additional Properties</em>}' map. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getAdditionalProperties()
     * @generated
     * @ordered
     */
    protected EMap<String, String> additionalProperties;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected MetadataTableImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.METADATA_TABLE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<ModelElement> getOwnedElement() {
        if (ownedElement == null) {
            ownedElement = new EObjectContainmentWithInverseEList<ModelElement>(ModelElement.class, this,
                    ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT, CorePackage.MODEL_ELEMENT__NAMESPACE);
        }
        return ownedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsAbstract() {
        return isAbstract;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setIsAbstract(boolean newIsAbstract) {
        boolean oldIsAbstract = isAbstract;
        isAbstract = newIsAbstract;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_TABLE__IS_ABSTRACT, oldIsAbstract,
                    isAbstract));
    }

    /**
     * this is no more generated because we changed the type of feature to specialize is to store only TdColumns
     *
     * @generated NOT
     */
    @SuppressWarnings("unchecked")
    public EList<Feature> getFeature() {
        if (feature == null) {
            feature = new EObjectContainmentWithInverseEList<MetadataColumn>(Feature.class, this,
                    ConnectionPackage.METADATA_TABLE__FEATURE, CorePackage.FEATURE__OWNER);
        }
        return (EList<Feature>) feature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<StructuralFeature> getStructuralFeature() {
        if (structuralFeature == null) {
            structuralFeature = new EObjectWithInverseResolvingEList<StructuralFeature>(StructuralFeature.class, this,
                    ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE, CorePackage.STRUCTURAL_FEATURE__TYPE);
        }
        return structuralFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Parameter> getParameter() {
        if (parameter == null) {
            parameter = new EObjectWithInverseResolvingEList<Parameter>(Parameter.class, this,
                    ConnectionPackage.METADATA_TABLE__PARAMETER, BehavioralPackage.PARAMETER__TYPE);
        }
        return parameter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Generalization> getGeneralization() {
        if (generalization == null) {
            generalization = new EObjectWithInverseResolvingEList<Generalization>(Generalization.class, this,
                    ConnectionPackage.METADATA_TABLE__GENERALIZATION, RelationshipsPackage.GENERALIZATION__CHILD);
        }
        return generalization;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Generalization> getSpecialization() {
        if (specialization == null) {
            specialization = new EObjectWithInverseResolvingEList<Generalization>(Generalization.class, this,
                    ConnectionPackage.METADATA_TABLE__SPECIALIZATION, RelationshipsPackage.GENERALIZATION__PARENT);
        }
        return specialization;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Instance> getInstance() {
        if (instance == null) {
            instance = new EObjectWithInverseResolvingEList<Instance>(Instance.class, this,
                    ConnectionPackage.METADATA_TABLE__INSTANCE, InstancePackage.INSTANCE__CLASSIFIER);
        }
        return instance;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<TypeAlias> getAlias() {
        if (alias == null) {
            alias = new EObjectWithInverseResolvingEList<TypeAlias>(TypeAlias.class, this,
                    ConnectionPackage.METADATA_TABLE__ALIAS, DatatypesPackage.TYPE_ALIAS__TYPE);
        }
        return alias;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<ExpressionNode> getExpressionNode() {
        if (expressionNode == null) {
            expressionNode = new EObjectWithInverseResolvingEList<ExpressionNode>(ExpressionNode.class, this,
                    ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE, ExpressionsPackage.EXPRESSION_NODE__TYPE);
        }
        return expressionNode;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<TypeMapping> getMappingFrom() {
        if (mappingFrom == null) {
            mappingFrom = new EObjectWithInverseResolvingEList<TypeMapping>(TypeMapping.class, this,
                    ConnectionPackage.METADATA_TABLE__MAPPING_FROM, TypemappingPackage.TYPE_MAPPING__SOURCE_TYPE);
        }
        return mappingFrom;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<TypeMapping> getMappingTo() {
        if (mappingTo == null) {
            mappingTo = new EObjectWithInverseResolvingEList<TypeMapping>(TypeMapping.class, this,
                    ConnectionPackage.METADATA_TABLE__MAPPING_TO, TypemappingPackage.TYPE_MAPPING__TARGET_TYPE);
        }
        return mappingTo;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<ClassifierMap> getClassifierMap() {
        if (classifierMap == null) {
            classifierMap = new EObjectWithInverseResolvingEList.ManyInverse<ClassifierMap>(ClassifierMap.class, this,
                    ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP, TransformationPackage.CLASSIFIER_MAP__SOURCE);
        }
        return classifierMap;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<ClassifierFeatureMap> getCfMap() {
        if (cfMap == null) {
            cfMap = new EObjectWithInverseResolvingEList.ManyInverse<ClassifierFeatureMap>(ClassifierFeatureMap.class, this,
                    ConnectionPackage.METADATA_TABLE__CF_MAP, TransformationPackage.CLASSIFIER_FEATURE_MAP__CLASSIFIER);
        }
        return cfMap;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Domain> getDomain() {
        if (domain == null) {
            domain = new EObjectWithInverseResolvingEList<Domain>(Domain.class, this, ConnectionPackage.METADATA_TABLE__DOMAIN,
                    ErPackage.DOMAIN__BASE_TYPE);
        }
        return domain;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<SimpleDimension> getSimpleDimension() {
        if (simpleDimension == null) {
            simpleDimension = new EObjectWithInverseResolvingEList<SimpleDimension>(SimpleDimension.class, this,
                    ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION, ExpressPackage.SIMPLE_DIMENSION__DATA_TYPE);
        }
        return simpleDimension;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Index> getIndex() {
        if (index == null) {
            index = new EObjectWithInverseResolvingEList<Index>(Index.class, this, ConnectionPackage.METADATA_TABLE__INDEX,
                    KeysindexesPackage.INDEX__SPANNED_CLASS);
        }
        return index;
    }

    @Override
    public boolean isReadOnly() {
        Connection connection = getConnection();
        return connection == null ? false : connection.isReadOnly();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public String getSourceName() {
        return getName();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public void setSourceName(String newSourceName) {
        String oldSourceName = getSourceName();
        setName(newSourceName);
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_TABLE__SOURCE_NAME, oldSourceName,
                    newSourceName));
        }
    }

    /**
     * this is no more generated because we changed the type of feature to specialize is to store only TdColumns and
     * return it here
     *
     * @generated NOT
     */
    @SuppressWarnings("unchecked")
    public EList<MetadataColumn> getColumns() {
        getFeature();
        return (EList<MetadataColumn>) feature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Connection getConnection() {
        Connection connection = basicGetConnection();
        return connection != null && connection.eIsProxy() ? (Connection) eResolveProxy((InternalEObject) connection)
                : connection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Connection basicGetConnection() {
        return TableHelper.getFirstConnection(this);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EMap<String, String> getAdditionalProperties() {
        if (additionalProperties == null) {
            additionalProperties = new EcoreEMap<String, String>(ConnectionPackage.Literals.ADDITIONAL_PROPERTIES,
                    AdditionalPropertiesImpl.class, this, ConnectionPackage.METADATA_TABLE__ADDITIONAL_PROPERTIES);
        }
        return additionalProperties;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getTableType() {
        return tableType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setTableType(String newTableType) {
        String oldTableType = tableType;
        tableType = newTableType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_TABLE__TABLE_TYPE, oldTableType,
                    tableType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isAttachedCDC() {
        return attachedCDC;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setAttachedCDC(boolean newAttachedCDC) {
        boolean oldAttachedCDC = attachedCDC;
        attachedCDC = newAttachedCDC;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_TABLE__ATTACHED_CDC, oldAttachedCDC,
                    attachedCDC));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isActivatedCDC() {
        return activatedCDC;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setActivatedCDC(boolean newActivatedCDC) {
        boolean oldActivatedCDC = activatedCDC;
        activatedCDC = newActivatedCDC;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_TABLE__ACTIVATED_CDC,
                    oldActivatedCDC, activatedCDC));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedElement()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__FEATURE:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getFeature()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getStructuralFeature()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__PARAMETER:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getParameter()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__GENERALIZATION:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getGeneralization()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__SPECIALIZATION:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getSpecialization()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__INSTANCE:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getInstance()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__ALIAS:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getAlias()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getExpressionNode()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__MAPPING_FROM:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getMappingFrom()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__MAPPING_TO:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getMappingTo()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getClassifierMap()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__CF_MAP:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getCfMap()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__DOMAIN:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getDomain()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getSimpleDimension()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__INDEX:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getIndex()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT:
            return ((InternalEList<?>) getOwnedElement()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__FEATURE:
            return ((InternalEList<?>) getFeature()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE:
            return ((InternalEList<?>) getStructuralFeature()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__PARAMETER:
            return ((InternalEList<?>) getParameter()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__GENERALIZATION:
            return ((InternalEList<?>) getGeneralization()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__SPECIALIZATION:
            return ((InternalEList<?>) getSpecialization()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__INSTANCE:
            return ((InternalEList<?>) getInstance()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__ALIAS:
            return ((InternalEList<?>) getAlias()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE:
            return ((InternalEList<?>) getExpressionNode()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__MAPPING_FROM:
            return ((InternalEList<?>) getMappingFrom()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__MAPPING_TO:
            return ((InternalEList<?>) getMappingTo()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP:
            return ((InternalEList<?>) getClassifierMap()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__CF_MAP:
            return ((InternalEList<?>) getCfMap()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__DOMAIN:
            return ((InternalEList<?>) getDomain()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION:
            return ((InternalEList<?>) getSimpleDimension()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__INDEX:
            return ((InternalEList<?>) getIndex()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_TABLE__ADDITIONAL_PROPERTIES:
            return ((InternalEList<?>) getAdditionalProperties()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT:
            return getOwnedElement();
        case ConnectionPackage.METADATA_TABLE__IS_ABSTRACT:
            return isIsAbstract();
        case ConnectionPackage.METADATA_TABLE__FEATURE:
            return getFeature();
        case ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE:
            return getStructuralFeature();
        case ConnectionPackage.METADATA_TABLE__PARAMETER:
            return getParameter();
        case ConnectionPackage.METADATA_TABLE__GENERALIZATION:
            return getGeneralization();
        case ConnectionPackage.METADATA_TABLE__SPECIALIZATION:
            return getSpecialization();
        case ConnectionPackage.METADATA_TABLE__INSTANCE:
            return getInstance();
        case ConnectionPackage.METADATA_TABLE__ALIAS:
            return getAlias();
        case ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE:
            return getExpressionNode();
        case ConnectionPackage.METADATA_TABLE__MAPPING_FROM:
            return getMappingFrom();
        case ConnectionPackage.METADATA_TABLE__MAPPING_TO:
            return getMappingTo();
        case ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP:
            return getClassifierMap();
        case ConnectionPackage.METADATA_TABLE__CF_MAP:
            return getCfMap();
        case ConnectionPackage.METADATA_TABLE__DOMAIN:
            return getDomain();
        case ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION:
            return getSimpleDimension();
        case ConnectionPackage.METADATA_TABLE__INDEX:
            return getIndex();
        case ConnectionPackage.METADATA_TABLE__SOURCE_NAME:
            return getSourceName();
        case ConnectionPackage.METADATA_TABLE__TABLE_TYPE:
            return getTableType();
        case ConnectionPackage.METADATA_TABLE__ATTACHED_CDC:
            return isAttachedCDC();
        case ConnectionPackage.METADATA_TABLE__ACTIVATED_CDC:
            return isActivatedCDC();
        case ConnectionPackage.METADATA_TABLE__COLUMNS:
            return getColumns();
        case ConnectionPackage.METADATA_TABLE__CONNECTION:
            if (resolve)
                return getConnection();
            return basicGetConnection();
        case ConnectionPackage.METADATA_TABLE__ADDITIONAL_PROPERTIES:
            if (coreType)
                return getAdditionalProperties();
            else
                return getAdditionalProperties().map();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT:
            getOwnedElement().clear();
            getOwnedElement().addAll((Collection<? extends ModelElement>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__IS_ABSTRACT:
            setIsAbstract((Boolean) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__FEATURE:
            getFeature().clear();
            getFeature().addAll((Collection<? extends Feature>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE:
            getStructuralFeature().clear();
            getStructuralFeature().addAll((Collection<? extends StructuralFeature>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__PARAMETER:
            getParameter().clear();
            getParameter().addAll((Collection<? extends Parameter>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__GENERALIZATION:
            getGeneralization().clear();
            getGeneralization().addAll((Collection<? extends Generalization>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__SPECIALIZATION:
            getSpecialization().clear();
            getSpecialization().addAll((Collection<? extends Generalization>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__INSTANCE:
            getInstance().clear();
            getInstance().addAll((Collection<? extends Instance>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__ALIAS:
            getAlias().clear();
            getAlias().addAll((Collection<? extends TypeAlias>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE:
            getExpressionNode().clear();
            getExpressionNode().addAll((Collection<? extends ExpressionNode>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__MAPPING_FROM:
            getMappingFrom().clear();
            getMappingFrom().addAll((Collection<? extends TypeMapping>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__MAPPING_TO:
            getMappingTo().clear();
            getMappingTo().addAll((Collection<? extends TypeMapping>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP:
            getClassifierMap().clear();
            getClassifierMap().addAll((Collection<? extends ClassifierMap>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__CF_MAP:
            getCfMap().clear();
            getCfMap().addAll((Collection<? extends ClassifierFeatureMap>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__DOMAIN:
            getDomain().clear();
            getDomain().addAll((Collection<? extends Domain>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION:
            getSimpleDimension().clear();
            getSimpleDimension().addAll((Collection<? extends SimpleDimension>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__INDEX:
            getIndex().clear();
            getIndex().addAll((Collection<? extends Index>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__SOURCE_NAME:
            setSourceName((String) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__TABLE_TYPE:
            setTableType((String) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__ATTACHED_CDC:
            setAttachedCDC((Boolean) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__ACTIVATED_CDC:
            setActivatedCDC((Boolean) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__COLUMNS:
            getColumns().clear();
            getColumns().addAll((Collection<? extends MetadataColumn>) newValue);
            return;
        case ConnectionPackage.METADATA_TABLE__ADDITIONAL_PROPERTIES:
            ((EStructuralFeature.Setting) getAdditionalProperties()).set(newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT:
            getOwnedElement().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__IS_ABSTRACT:
            setIsAbstract(IS_ABSTRACT_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_TABLE__FEATURE:
            getFeature().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE:
            getStructuralFeature().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__PARAMETER:
            getParameter().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__GENERALIZATION:
            getGeneralization().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__SPECIALIZATION:
            getSpecialization().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__INSTANCE:
            getInstance().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__ALIAS:
            getAlias().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE:
            getExpressionNode().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__MAPPING_FROM:
            getMappingFrom().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__MAPPING_TO:
            getMappingTo().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP:
            getClassifierMap().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__CF_MAP:
            getCfMap().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__DOMAIN:
            getDomain().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION:
            getSimpleDimension().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__INDEX:
            getIndex().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__SOURCE_NAME:
            setSourceName(SOURCE_NAME_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_TABLE__TABLE_TYPE:
            setTableType(TABLE_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_TABLE__ATTACHED_CDC:
            setAttachedCDC(ATTACHED_CDC_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_TABLE__ACTIVATED_CDC:
            setActivatedCDC(ACTIVATED_CDC_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_TABLE__COLUMNS:
            getColumns().clear();
            return;
        case ConnectionPackage.METADATA_TABLE__ADDITIONAL_PROPERTIES:
            getAdditionalProperties().clear();
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT:
            return ownedElement != null && !ownedElement.isEmpty();
        case ConnectionPackage.METADATA_TABLE__IS_ABSTRACT:
            return isAbstract != IS_ABSTRACT_EDEFAULT;
        case ConnectionPackage.METADATA_TABLE__FEATURE:
            return feature != null && !feature.isEmpty();
        case ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE:
            return structuralFeature != null && !structuralFeature.isEmpty();
        case ConnectionPackage.METADATA_TABLE__PARAMETER:
            return parameter != null && !parameter.isEmpty();
        case ConnectionPackage.METADATA_TABLE__GENERALIZATION:
            return generalization != null && !generalization.isEmpty();
        case ConnectionPackage.METADATA_TABLE__SPECIALIZATION:
            return specialization != null && !specialization.isEmpty();
        case ConnectionPackage.METADATA_TABLE__INSTANCE:
            return instance != null && !instance.isEmpty();
        case ConnectionPackage.METADATA_TABLE__ALIAS:
            return alias != null && !alias.isEmpty();
        case ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE:
            return expressionNode != null && !expressionNode.isEmpty();
        case ConnectionPackage.METADATA_TABLE__MAPPING_FROM:
            return mappingFrom != null && !mappingFrom.isEmpty();
        case ConnectionPackage.METADATA_TABLE__MAPPING_TO:
            return mappingTo != null && !mappingTo.isEmpty();
        case ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP:
            return classifierMap != null && !classifierMap.isEmpty();
        case ConnectionPackage.METADATA_TABLE__CF_MAP:
            return cfMap != null && !cfMap.isEmpty();
        case ConnectionPackage.METADATA_TABLE__DOMAIN:
            return domain != null && !domain.isEmpty();
        case ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION:
            return simpleDimension != null && !simpleDimension.isEmpty();
        case ConnectionPackage.METADATA_TABLE__INDEX:
            return index != null && !index.isEmpty();
        case ConnectionPackage.METADATA_TABLE__SOURCE_NAME:
            return SOURCE_NAME_EDEFAULT == null ? getSourceName() != null : !SOURCE_NAME_EDEFAULT.equals(getSourceName());
        case ConnectionPackage.METADATA_TABLE__TABLE_TYPE:
            return TABLE_TYPE_EDEFAULT == null ? tableType != null : !TABLE_TYPE_EDEFAULT.equals(tableType);
        case ConnectionPackage.METADATA_TABLE__ATTACHED_CDC:
            return attachedCDC != ATTACHED_CDC_EDEFAULT;
        case ConnectionPackage.METADATA_TABLE__ACTIVATED_CDC:
            return activatedCDC != ACTIVATED_CDC_EDEFAULT;
        case ConnectionPackage.METADATA_TABLE__COLUMNS:
            return !getColumns().isEmpty();
        case ConnectionPackage.METADATA_TABLE__CONNECTION:
            return basicGetConnection() != null;
        case ConnectionPackage.METADATA_TABLE__ADDITIONAL_PROPERTIES:
            return additionalProperties != null && !additionalProperties.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == Namespace.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT:
                return CorePackage.NAMESPACE__OWNED_ELEMENT;
            default:
                return -1;
            }
        }
        if (baseClass == Classifier.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.METADATA_TABLE__IS_ABSTRACT:
                return CorePackage.CLASSIFIER__IS_ABSTRACT;
            case ConnectionPackage.METADATA_TABLE__FEATURE:
                return CorePackage.CLASSIFIER__FEATURE;
            case ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE:
                return CorePackage.CLASSIFIER__STRUCTURAL_FEATURE;
            case ConnectionPackage.METADATA_TABLE__PARAMETER:
                return CorePackage.CLASSIFIER__PARAMETER;
            case ConnectionPackage.METADATA_TABLE__GENERALIZATION:
                return CorePackage.CLASSIFIER__GENERALIZATION;
            case ConnectionPackage.METADATA_TABLE__SPECIALIZATION:
                return CorePackage.CLASSIFIER__SPECIALIZATION;
            case ConnectionPackage.METADATA_TABLE__INSTANCE:
                return CorePackage.CLASSIFIER__INSTANCE;
            case ConnectionPackage.METADATA_TABLE__ALIAS:
                return CorePackage.CLASSIFIER__ALIAS;
            case ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE:
                return CorePackage.CLASSIFIER__EXPRESSION_NODE;
            case ConnectionPackage.METADATA_TABLE__MAPPING_FROM:
                return CorePackage.CLASSIFIER__MAPPING_FROM;
            case ConnectionPackage.METADATA_TABLE__MAPPING_TO:
                return CorePackage.CLASSIFIER__MAPPING_TO;
            case ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP:
                return CorePackage.CLASSIFIER__CLASSIFIER_MAP;
            case ConnectionPackage.METADATA_TABLE__CF_MAP:
                return CorePackage.CLASSIFIER__CF_MAP;
            case ConnectionPackage.METADATA_TABLE__DOMAIN:
                return CorePackage.CLASSIFIER__DOMAIN;
            case ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION:
                return CorePackage.CLASSIFIER__SIMPLE_DIMENSION;
            default:
                return -1;
            }
        }
        if (baseClass == orgomg.cwm.objectmodel.core.Class.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.METADATA_TABLE__INDEX:
                return CorePackage.CLASS__INDEX;
            default:
                return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == Namespace.class) {
            switch (baseFeatureID) {
            case CorePackage.NAMESPACE__OWNED_ELEMENT:
                return ConnectionPackage.METADATA_TABLE__OWNED_ELEMENT;
            default:
                return -1;
            }
        }
        if (baseClass == Classifier.class) {
            switch (baseFeatureID) {
            case CorePackage.CLASSIFIER__IS_ABSTRACT:
                return ConnectionPackage.METADATA_TABLE__IS_ABSTRACT;
            case CorePackage.CLASSIFIER__FEATURE:
                return ConnectionPackage.METADATA_TABLE__FEATURE;
            case CorePackage.CLASSIFIER__STRUCTURAL_FEATURE:
                return ConnectionPackage.METADATA_TABLE__STRUCTURAL_FEATURE;
            case CorePackage.CLASSIFIER__PARAMETER:
                return ConnectionPackage.METADATA_TABLE__PARAMETER;
            case CorePackage.CLASSIFIER__GENERALIZATION:
                return ConnectionPackage.METADATA_TABLE__GENERALIZATION;
            case CorePackage.CLASSIFIER__SPECIALIZATION:
                return ConnectionPackage.METADATA_TABLE__SPECIALIZATION;
            case CorePackage.CLASSIFIER__INSTANCE:
                return ConnectionPackage.METADATA_TABLE__INSTANCE;
            case CorePackage.CLASSIFIER__ALIAS:
                return ConnectionPackage.METADATA_TABLE__ALIAS;
            case CorePackage.CLASSIFIER__EXPRESSION_NODE:
                return ConnectionPackage.METADATA_TABLE__EXPRESSION_NODE;
            case CorePackage.CLASSIFIER__MAPPING_FROM:
                return ConnectionPackage.METADATA_TABLE__MAPPING_FROM;
            case CorePackage.CLASSIFIER__MAPPING_TO:
                return ConnectionPackage.METADATA_TABLE__MAPPING_TO;
            case CorePackage.CLASSIFIER__CLASSIFIER_MAP:
                return ConnectionPackage.METADATA_TABLE__CLASSIFIER_MAP;
            case CorePackage.CLASSIFIER__CF_MAP:
                return ConnectionPackage.METADATA_TABLE__CF_MAP;
            case CorePackage.CLASSIFIER__DOMAIN:
                return ConnectionPackage.METADATA_TABLE__DOMAIN;
            case CorePackage.CLASSIFIER__SIMPLE_DIMENSION:
                return ConnectionPackage.METADATA_TABLE__SIMPLE_DIMENSION;
            default:
                return -1;
            }
        }
        if (baseClass == orgomg.cwm.objectmodel.core.Class.class) {
            switch (baseFeatureID) {
            case CorePackage.CLASS__INDEX:
                return ConnectionPackage.METADATA_TABLE__INDEX;
            default:
                return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (isAbstract: ");
        result.append(isAbstract);
        result.append(", tableType: ");
        result.append(tableType);
        result.append(", attachedCDC: ");
        result.append(attachedCDC);
        result.append(", activatedCDC: ");
        result.append(activatedCDC);
        result.append(')');
        return result.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see orgomg.cwm.objectmodel.core.impl.ModelElementImpl#getName()
     */
    @Override
    public String getName() {
        if (super.getName() == null) {
            return this.label;
        } else {
            return super.getName();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.metadata.builder.connection.impl.AbstractMetadataObjectImpl#getLabel()
     */
    @Override
    public String getLabel() {
        if (StringUtils.isEmpty(this.label)) {
            return getName();
        }
        return this.label;
    }

    public String getOriginalLabel() {
        return this.label;
    }

} // MetadataTableImpl
