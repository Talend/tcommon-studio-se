/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.SwitchHelpers;
import orgomg.cwm.analysis.transformation.ClassifierFeatureMap;
import orgomg.cwm.analysis.transformation.FeatureMap;
import orgomg.cwm.analysis.transformation.TransformationPackage;
import orgomg.cwm.foundation.datatypes.DatatypesPackage;
import orgomg.cwm.foundation.datatypes.Union;
import orgomg.cwm.foundation.expressions.ExpressionsPackage;
import orgomg.cwm.foundation.expressions.FeatureNode;
import orgomg.cwm.foundation.keysindexes.IndexedFeature;
import orgomg.cwm.foundation.keysindexes.KeyRelationship;
import orgomg.cwm.foundation.keysindexes.KeysindexesPackage;
import orgomg.cwm.foundation.keysindexes.UniqueKey;
import orgomg.cwm.objectmodel.core.Attribute;
import orgomg.cwm.objectmodel.core.ChangeableKind;
import orgomg.cwm.objectmodel.core.Classifier;
import orgomg.cwm.objectmodel.core.CorePackage;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.Feature;
import orgomg.cwm.objectmodel.core.Multiplicity;
import orgomg.cwm.objectmodel.core.OrderingKind;
import orgomg.cwm.objectmodel.core.ScopeKind;
import orgomg.cwm.objectmodel.core.StructuralFeature;
import orgomg.cwm.objectmodel.instance.InstancePackage;
import orgomg.cwm.objectmodel.instance.Slot;
import orgomg.cwm.resource.record.Field;
import orgomg.cwm.resource.record.RecordPackage;
import orgomg.cwmx.resource.dmsii.DataItem;
import orgomg.cwmx.resource.dmsii.DmsiiPackage;
import orgomg.cwmx.resource.dmsii.Remap;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Metadata Column</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getOwnerScope <em>Owner Scope
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getOwner <em>Owner</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getFeatureNode <em>Feature Node
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getFeatureMap <em>Feature Map
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getCfMap <em>Cf Map</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getChangeability <em>
 * Changeability</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getMultiplicity <em>Multiplicity
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getOrdering <em>Ordering</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getTargetScope <em>Target Scope
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getType <em>Type</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getSlot <em>Slot</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getDiscriminatedUnion <em>
 * Discriminated Union</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getIndexedFeature <em>Indexed
 * Feature</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getKeyRelationship <em>Key
 * Relationship</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getUniqueKey <em>Unique Key
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getDataItem <em>Data Item</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getRemap <em>Remap</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getInitialValue <em>Initial
 * Value</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getLength <em>Length</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getPrecision <em>Precision</em>}
 * </li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getScale <em>Scale</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getSourceType <em>Source Type
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getDefaultValue <em>Default
 * Value</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getTalendType <em>Talend Type
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#isKey <em>Key</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#isNullable <em>Nullable</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getTable <em>Table</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getOriginalField <em>Original
 * Field</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getPattern <em>Pattern</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl#getDisplayField <em>Display
 * Field</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class MetadataColumnImpl extends AbstractMetadataObjectImpl implements MetadataColumn {

    /**
     * The default value of the '{@link #getOwnerScope() <em>Owner Scope</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getOwnerScope()
     * @generated
     * @ordered
     */
    protected static final ScopeKind OWNER_SCOPE_EDEFAULT = ScopeKind.SK_INSTANCE;

    /**
     * The cached value of the '{@link #getOwnerScope() <em>Owner Scope</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getOwnerScope()
     * @generated
     * @ordered
     */
    protected ScopeKind ownerScope = OWNER_SCOPE_EDEFAULT;

    /**
     * The cached value of the '{@link #getFeatureNode() <em>Feature Node</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureNode()
     * @generated
     * @ordered
     */
    protected EList<FeatureNode> featureNode;

    /**
     * The cached value of the '{@link #getFeatureMap() <em>Feature Map</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureMap()
     * @generated
     * @ordered
     */
    protected EList<FeatureMap> featureMap;

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
     * The default value of the '{@link #getChangeability() <em>Changeability</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChangeability()
     * @generated
     * @ordered
     */
    protected static final ChangeableKind CHANGEABILITY_EDEFAULT = ChangeableKind.CK_CHANGEABLE;

    /**
     * The cached value of the '{@link #getChangeability() <em>Changeability</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChangeability()
     * @generated
     * @ordered
     */
    protected ChangeableKind changeability = CHANGEABILITY_EDEFAULT;

    /**
     * The cached value of the '{@link #getMultiplicity() <em>Multiplicity</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getMultiplicity()
     * @generated
     * @ordered
     */
    protected Multiplicity multiplicity;

    /**
     * The default value of the '{@link #getOrdering() <em>Ordering</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getOrdering()
     * @generated
     * @ordered
     */
    protected static final OrderingKind ORDERING_EDEFAULT = OrderingKind.OK_UNORDERED;

    /**
     * The cached value of the '{@link #getOrdering() <em>Ordering</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getOrdering()
     * @generated
     * @ordered
     */
    protected OrderingKind ordering = ORDERING_EDEFAULT;

    /**
     * The default value of the '{@link #getTargetScope() <em>Target Scope</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetScope()
     * @generated
     * @ordered
     */
    protected static final ScopeKind TARGET_SCOPE_EDEFAULT = ScopeKind.SK_INSTANCE;

    /**
     * The cached value of the '{@link #getTargetScope() <em>Target Scope</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getTargetScope()
     * @generated
     * @ordered
     */
    protected ScopeKind targetScope = TARGET_SCOPE_EDEFAULT;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getType()
     * @generated
     * @ordered
     */
    protected Classifier type;

    /**
     * The cached value of the '{@link #getSlot() <em>Slot</em>}' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getSlot()
     * @generated
     * @ordered
     */
    protected EList<Slot> slot;

    /**
     * The cached value of the '{@link #getDiscriminatedUnion() <em>Discriminated Union</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDiscriminatedUnion()
     * @generated
     * @ordered
     */
    protected EList<Union> discriminatedUnion;

    /**
     * The cached value of the '{@link #getIndexedFeature() <em>Indexed Feature</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getIndexedFeature()
     * @generated
     * @ordered
     */
    protected EList<IndexedFeature> indexedFeature;

    /**
     * The cached value of the '{@link #getKeyRelationship() <em>Key Relationship</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getKeyRelationship()
     * @generated
     * @ordered
     */
    protected EList<KeyRelationship> keyRelationship;

    /**
     * The cached value of the '{@link #getUniqueKey() <em>Unique Key</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUniqueKey()
     * @generated
     * @ordered
     */
    protected EList<UniqueKey> uniqueKey;

    /**
     * The cached value of the '{@link #getDataItem() <em>Data Item</em>}' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDataItem()
     * @generated
     * @ordered
     */
    protected EList<DataItem> dataItem;

    /**
     * The cached value of the '{@link #getRemap() <em>Remap</em>}' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getRemap()
     * @generated
     * @ordered
     */
    protected EList<Remap> remap;

    /**
     * The cached value of the '{@link #getInitialValue() <em>Initial Value</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getInitialValue()
     * @generated
     * @ordered
     */
    protected Expression initialValue;

    /**
     * The default value of the '{@link #getLength() <em>Length</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLength()
     * @generated
     * @ordered
     */
    protected static final long LENGTH_EDEFAULT = 0L;

    /**
     * The cached value of the '{@link #getLength() <em>Length</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLength()
     * @generated
     * @ordered
     */
    protected long length = LENGTH_EDEFAULT;

    /**
     * The default value of the '{@link #getPrecision() <em>Precision</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPrecision()
     * @generated
     * @ordered
     */
    protected static final long PRECISION_EDEFAULT = 0L;

    /**
     * The cached value of the '{@link #getPrecision() <em>Precision</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPrecision()
     * @generated
     * @ordered
     */
    protected long precision = PRECISION_EDEFAULT;

    /**
     * The default value of the '{@link #getScale() <em>Scale</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getScale()
     * @generated
     * @ordered
     */
    protected static final long SCALE_EDEFAULT = 0L;

    /**
     * The cached value of the '{@link #getScale() <em>Scale</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getScale()
     * @generated
     * @ordered
     */
    protected long scale = SCALE_EDEFAULT;

    /**
     * The default value of the '{@link #getSourceType() <em>Source Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getSourceType()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSourceType() <em>Source Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getSourceType()
     * @generated
     * @ordered
     */
    protected String sourceType = SOURCE_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultValue()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_VALUE_EDEFAULT = ""; //$NON-NLS-1$

    /**
     * The default value of the '{@link #getTalendType() <em>Talend Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getTalendType()
     * @generated
     * @ordered
     */
    protected static final String TALEND_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTalendType() <em>Talend Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getTalendType()
     * @generated
     * @ordered
     */
    protected String talendType = TALEND_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #isKey() <em>Key</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isKey()
     * @generated
     * @ordered
     */
    protected static final boolean KEY_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isKey() <em>Key</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isKey()
     * @generated
     * @ordered
     */
    protected boolean key = KEY_EDEFAULT;

    /**
     * The default value of the '{@link #isNullable() <em>Nullable</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isNullable()
     * @generated
     * @ordered
     */
    protected static final boolean NULLABLE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isNullable() <em>Nullable</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isNullable()
     * @generated
     * @ordered
     */
    protected boolean nullable = NULLABLE_EDEFAULT;

    /**
     * The default value of the '{@link #getOriginalField() <em>Original Field</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOriginalField()
     * @generated
     * @ordered
     */
    protected static final String ORIGINAL_FIELD_EDEFAULT = ""; //$NON-NLS-1$

    /**
     * The default value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPattern()
     * @generated
     * @ordered
     */
    protected static final String PATTERN_EDEFAULT = "";

    /**
     * The cached value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPattern()
     * @generated
     * @ordered
     */
    protected String pattern = PATTERN_EDEFAULT;

    /**
     * The default value of the '{@link #getDisplayField() <em>Display Field</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDisplayField()
     * @generated
     * @ordered
     */
    protected static final String DISPLAY_FIELD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDisplayField() <em>Display Field</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDisplayField()
     * @generated
     * @ordered
     */
    protected String displayField = DISPLAY_FIELD_EDEFAULT;

    /**
     * The default value of the '{@link #getOriginalLength() <em>Original Length</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getOriginalLength()
     * @generated
     * @ordered
     */
    protected static final long ORIGINAL_LENGTH_EDEFAULT = 0L;

    /**
     * The cached value of the '{@link #getOriginalLength() <em>Original Length</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getOriginalLength()
     * @generated
     * @ordered
     */
    protected long originalLength = ORIGINAL_LENGTH_EDEFAULT;

    /**
     * The default value of the '{@link #getRelatedEntity() <em>Related Entity</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelatedEntity()
     * @generated
     * @ordered
     */
    protected static final String RELATED_ENTITY_EDEFAULT = "";

    /**
     * The cached value of the '{@link #getRelatedEntity() <em>Related Entity</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelatedEntity()
     * @generated
     * @ordered
     */
    protected String relatedEntity = RELATED_ENTITY_EDEFAULT;

    /**
     * The default value of the '{@link #getRelationshipType() <em>Relationship Type</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getRelationshipType()
     * @generated
     * @ordered
     */
    protected static final String RELATIONSHIP_TYPE_EDEFAULT = "";

    /**
     * The cached value of the '{@link #getRelationshipType() <em>Relationship Type</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getRelationshipType()
     * @generated
     * @ordered
     */
    protected String relationshipType = RELATIONSHIP_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected MetadataColumnImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.METADATA_COLUMN;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ScopeKind getOwnerScope() {
        return ownerScope;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setOwnerScope(ScopeKind newOwnerScope) {
        ScopeKind oldOwnerScope = ownerScope;
        ownerScope = newOwnerScope == null ? OWNER_SCOPE_EDEFAULT : newOwnerScope;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__OWNER_SCOPE, oldOwnerScope,
                    ownerScope));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Classifier getOwner() {
        if (eContainerFeatureID() != ConnectionPackage.METADATA_COLUMN__OWNER)
            return null;
        return (Classifier) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOwner(Classifier newOwner, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newOwner, ConnectionPackage.METADATA_COLUMN__OWNER, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setOwner(Classifier newOwner) {
        if (newOwner != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.METADATA_COLUMN__OWNER && newOwner != null)) {
            if (EcoreUtil.isAncestor(this, newOwner))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newOwner != null)
                msgs = ((InternalEObject) newOwner).eInverseAdd(this, CorePackage.CLASSIFIER__FEATURE, Classifier.class, msgs);
            msgs = basicSetOwner(newOwner, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__OWNER, newOwner, newOwner));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<FeatureNode> getFeatureNode() {
        if (featureNode == null) {
            featureNode = new EObjectWithInverseResolvingEList<FeatureNode>(FeatureNode.class, this,
                    ConnectionPackage.METADATA_COLUMN__FEATURE_NODE, ExpressionsPackage.FEATURE_NODE__FEATURE);
        }
        return featureNode;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<FeatureMap> getFeatureMap() {
        if (featureMap == null) {
            featureMap = new EObjectWithInverseResolvingEList.ManyInverse<FeatureMap>(FeatureMap.class, this,
                    ConnectionPackage.METADATA_COLUMN__FEATURE_MAP, TransformationPackage.FEATURE_MAP__TARGET);
        }
        return featureMap;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<ClassifierFeatureMap> getCfMap() {
        if (cfMap == null) {
            cfMap = new EObjectWithInverseResolvingEList.ManyInverse<ClassifierFeatureMap>(ClassifierFeatureMap.class, this,
                    ConnectionPackage.METADATA_COLUMN__CF_MAP, TransformationPackage.CLASSIFIER_FEATURE_MAP__FEATURE);
        }
        return cfMap;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ChangeableKind getChangeability() {
        return changeability;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setChangeability(ChangeableKind newChangeability) {
        ChangeableKind oldChangeability = changeability;
        changeability = newChangeability == null ? CHANGEABILITY_EDEFAULT : newChangeability;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__CHANGEABILITY,
                    oldChangeability, changeability));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMultiplicity(Multiplicity newMultiplicity, NotificationChain msgs) {
        Multiplicity oldMultiplicity = multiplicity;
        multiplicity = newMultiplicity;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.METADATA_COLUMN__MULTIPLICITY, oldMultiplicity, newMultiplicity);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setMultiplicity(Multiplicity newMultiplicity) {
        if (newMultiplicity != multiplicity) {
            NotificationChain msgs = null;
            if (multiplicity != null)
                msgs = ((InternalEObject) multiplicity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.METADATA_COLUMN__MULTIPLICITY, null, msgs);
            if (newMultiplicity != null)
                msgs = ((InternalEObject) newMultiplicity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.METADATA_COLUMN__MULTIPLICITY, null, msgs);
            msgs = basicSetMultiplicity(newMultiplicity, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__MULTIPLICITY,
                    newMultiplicity, newMultiplicity));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public OrderingKind getOrdering() {
        return ordering;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setOrdering(OrderingKind newOrdering) {
        OrderingKind oldOrdering = ordering;
        ordering = newOrdering == null ? ORDERING_EDEFAULT : newOrdering;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__ORDERING, oldOrdering,
                    ordering));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ScopeKind getTargetScope() {
        return targetScope;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setTargetScope(ScopeKind newTargetScope) {
        ScopeKind oldTargetScope = targetScope;
        targetScope = newTargetScope == null ? TARGET_SCOPE_EDEFAULT : newTargetScope;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__TARGET_SCOPE,
                    oldTargetScope, targetScope));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Classifier getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) type;
            type = (Classifier) eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConnectionPackage.METADATA_COLUMN__TYPE, oldType,
                            type));
            }
        }
        return type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Classifier basicGetType() {
        return type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetType(Classifier newType, NotificationChain msgs) {
        Classifier oldType = type;
        type = newType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.METADATA_COLUMN__TYPE, oldType, newType);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setType(Classifier newType) {
        if (newType != type) {
            NotificationChain msgs = null;
            if (type != null)
                msgs = ((InternalEObject) type).eInverseRemove(this, CorePackage.CLASSIFIER__STRUCTURAL_FEATURE,
                        Classifier.class, msgs);
            if (newType != null)
                msgs = ((InternalEObject) newType).eInverseAdd(this, CorePackage.CLASSIFIER__STRUCTURAL_FEATURE,
                        Classifier.class, msgs);
            msgs = basicSetType(newType, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__TYPE, newType, newType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Slot> getSlot() {
        if (slot == null) {
            slot = new EObjectWithInverseResolvingEList<Slot>(Slot.class, this, ConnectionPackage.METADATA_COLUMN__SLOT,
                    InstancePackage.SLOT__FEATURE);
        }
        return slot;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Union> getDiscriminatedUnion() {
        if (discriminatedUnion == null) {
            discriminatedUnion = new EObjectWithInverseResolvingEList<Union>(Union.class, this,
                    ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION, DatatypesPackage.UNION__DISCRIMINATOR);
        }
        return discriminatedUnion;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<IndexedFeature> getIndexedFeature() {
        if (indexedFeature == null) {
            indexedFeature = new EObjectWithInverseResolvingEList<IndexedFeature>(IndexedFeature.class, this,
                    ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE, KeysindexesPackage.INDEXED_FEATURE__FEATURE);
        }
        return indexedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<KeyRelationship> getKeyRelationship() {
        if (keyRelationship == null) {
            keyRelationship = new EObjectWithInverseResolvingEList.ManyInverse<KeyRelationship>(KeyRelationship.class, this,
                    ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP, KeysindexesPackage.KEY_RELATIONSHIP__FEATURE);
        }
        return keyRelationship;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<UniqueKey> getUniqueKey() {
        if (uniqueKey == null) {
            uniqueKey = new EObjectWithInverseResolvingEList.ManyInverse<UniqueKey>(UniqueKey.class, this,
                    ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY, KeysindexesPackage.UNIQUE_KEY__FEATURE);
        }
        return uniqueKey;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<DataItem> getDataItem() {
        if (dataItem == null) {
            dataItem = new EObjectWithInverseResolvingEList<DataItem>(DataItem.class, this,
                    ConnectionPackage.METADATA_COLUMN__DATA_ITEM, DmsiiPackage.DATA_ITEM__STRUCTURE);
        }
        return dataItem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Remap> getRemap() {
        if (remap == null) {
            remap = new EObjectWithInverseResolvingEList<Remap>(Remap.class, this, ConnectionPackage.METADATA_COLUMN__REMAP,
                    DmsiiPackage.REMAP__STRUCTURE);
        }
        return remap;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Expression getInitialValue() {
        return initialValue;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInitialValue(Expression newInitialValue, NotificationChain msgs) {
        Expression oldInitialValue = initialValue;
        initialValue = newInitialValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE, oldInitialValue, newInitialValue);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setInitialValue(Expression newInitialValue) {
        if (newInitialValue != initialValue) {
            NotificationChain msgs = null;
            if (initialValue != null)
                msgs = ((InternalEObject) initialValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE, null, msgs);
            if (newInitialValue != null)
                msgs = ((InternalEObject) newInitialValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE, null, msgs);
            msgs = basicSetInitialValue(newInitialValue, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE,
                    newInitialValue, newInitialValue));
    }

    @Override
    public boolean isReadOnly() {
        MetadataTable table = getTable();
        return table == null ? false : table.isReadOnly();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setSourceType(String newSourceType) {
        String oldSourceType = sourceType;
        sourceType = newSourceType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__SOURCE_TYPE, oldSourceType,
                    sourceType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public String getDefaultValue() {
        return ColumnHelper.getDefaultValue(this);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public void setDefaultValue(String newDefaultValue) {
        String oldDefaultValue = getDefaultValue();
        ColumnHelper.setDefaultValue(this, newDefaultValue);
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__DEFAULT_VALUE,
                    oldDefaultValue, newDefaultValue));
        } // else no notification required
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getTalendType() {
        return talendType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setTalendType(String newTalendType) {
        String oldTalendType = talendType;
        talendType = newTalendType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__TALEND_TYPE, oldTalendType,
                    talendType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isKey() {
        return key;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setKey(boolean newKey) {
        boolean oldKey = key;
        key = newKey;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__KEY, oldKey, key));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setNullable(boolean newNullable) {
        boolean oldNullable = nullable;
        nullable = newNullable;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__NULLABLE, oldNullable,
                    nullable));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public MetadataTable getTable() {
        if (this.getOwner() != null) {
            return SwitchHelpers.METADATA_TABLE_SWITCH.doSwitch(this.getOwner());
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public void setTable(MetadataTable newTable) {
        Classifier oldOwner = getOwner();
        setOwner(newTable);
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__TABLE, oldOwner, newTable));
        }// else no notification required
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public String getOriginalField() {
        return getName();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public void setOriginalField(String newOriginalField) {
        String oldName = getName();
        setName(newOriginalField);
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__ORIGINAL_FIELD, oldName,
                    newOriginalField));
        }// else no notification required
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPattern(String newPattern) {
        String oldPattern = pattern;
        pattern = newPattern;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__PATTERN, oldPattern, pattern));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDisplayField() {
        return displayField;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDisplayField(String newDisplayField) {
        String oldDisplayField = displayField;
        displayField = newDisplayField;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__DISPLAY_FIELD,
                    oldDisplayField, displayField));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public long getOriginalLength() {
        return originalLength;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setOriginalLength(long newOriginalLength) {
        long oldOriginalLength = originalLength;
        originalLength = newOriginalLength;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__ORIGINAL_LENGTH,
                    oldOriginalLength, originalLength));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getRelatedEntity() {
        return relatedEntity;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setRelatedEntity(String newRelatedEntity) {
        String oldRelatedEntity = relatedEntity;
        relatedEntity = newRelatedEntity;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__RELATED_ENTITY,
                    oldRelatedEntity, relatedEntity));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getRelationshipType() {
        return relationshipType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setRelationshipType(String newRelationshipType) {
        String oldRelationshipType = relationshipType;
        relationshipType = newRelationshipType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__RELATIONSHIP_TYPE,
                    oldRelationshipType, relationshipType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public long getLength() {
        return length;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setLength(long newLength) {
        long oldLength = length;
        length = newLength;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__LENGTH, oldLength, length));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public long getPrecision() {
        return precision;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPrecision(long newPrecision) {
        long oldPrecision = precision;
        precision = newPrecision;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__PRECISION, oldPrecision,
                    precision));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public long getScale() {
        return scale;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setScale(long newScale) {
        long oldScale = scale;
        scale = newScale;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.METADATA_COLUMN__SCALE, oldScale, scale));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.METADATA_COLUMN__OWNER:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetOwner((Classifier) otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__FEATURE_NODE:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getFeatureNode()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__FEATURE_MAP:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getFeatureMap()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__CF_MAP:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getCfMap()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__TYPE:
            if (type != null)
                msgs = ((InternalEObject) type).eInverseRemove(this, CorePackage.CLASSIFIER__STRUCTURAL_FEATURE,
                        Classifier.class, msgs);
            return basicSetType((Classifier) otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__SLOT:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getSlot()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getDiscriminatedUnion()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getIndexedFeature()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getKeyRelationship()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getUniqueKey()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__DATA_ITEM:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getDataItem()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__REMAP:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getRemap()).basicAdd(otherEnd, msgs);
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
        case ConnectionPackage.METADATA_COLUMN__OWNER:
            return basicSetOwner(null, msgs);
        case ConnectionPackage.METADATA_COLUMN__FEATURE_NODE:
            return ((InternalEList<?>) getFeatureNode()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__FEATURE_MAP:
            return ((InternalEList<?>) getFeatureMap()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__CF_MAP:
            return ((InternalEList<?>) getCfMap()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__MULTIPLICITY:
            return basicSetMultiplicity(null, msgs);
        case ConnectionPackage.METADATA_COLUMN__TYPE:
            return basicSetType(null, msgs);
        case ConnectionPackage.METADATA_COLUMN__SLOT:
            return ((InternalEList<?>) getSlot()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION:
            return ((InternalEList<?>) getDiscriminatedUnion()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE:
            return ((InternalEList<?>) getIndexedFeature()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP:
            return ((InternalEList<?>) getKeyRelationship()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY:
            return ((InternalEList<?>) getUniqueKey()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__DATA_ITEM:
            return ((InternalEList<?>) getDataItem()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__REMAP:
            return ((InternalEList<?>) getRemap()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE:
            return basicSetInitialValue(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID()) {
        case ConnectionPackage.METADATA_COLUMN__OWNER:
            return eInternalContainer().eInverseRemove(this, CorePackage.CLASSIFIER__FEATURE, Classifier.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.METADATA_COLUMN__OWNER_SCOPE:
            return getOwnerScope();
        case ConnectionPackage.METADATA_COLUMN__OWNER:
            return getOwner();
        case ConnectionPackage.METADATA_COLUMN__FEATURE_NODE:
            return getFeatureNode();
        case ConnectionPackage.METADATA_COLUMN__FEATURE_MAP:
            return getFeatureMap();
        case ConnectionPackage.METADATA_COLUMN__CF_MAP:
            return getCfMap();
        case ConnectionPackage.METADATA_COLUMN__CHANGEABILITY:
            return getChangeability();
        case ConnectionPackage.METADATA_COLUMN__MULTIPLICITY:
            return getMultiplicity();
        case ConnectionPackage.METADATA_COLUMN__ORDERING:
            return getOrdering();
        case ConnectionPackage.METADATA_COLUMN__TARGET_SCOPE:
            return getTargetScope();
        case ConnectionPackage.METADATA_COLUMN__TYPE:
            if (resolve)
                return getType();
            return basicGetType();
        case ConnectionPackage.METADATA_COLUMN__SLOT:
            return getSlot();
        case ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION:
            return getDiscriminatedUnion();
        case ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE:
            return getIndexedFeature();
        case ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP:
            return getKeyRelationship();
        case ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY:
            return getUniqueKey();
        case ConnectionPackage.METADATA_COLUMN__DATA_ITEM:
            return getDataItem();
        case ConnectionPackage.METADATA_COLUMN__REMAP:
            return getRemap();
        case ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE:
            return getInitialValue();
        case ConnectionPackage.METADATA_COLUMN__LENGTH:
            return getLength();
        case ConnectionPackage.METADATA_COLUMN__PRECISION:
            return getPrecision();
        case ConnectionPackage.METADATA_COLUMN__SCALE:
            return getScale();
        case ConnectionPackage.METADATA_COLUMN__SOURCE_TYPE:
            return getSourceType();
        case ConnectionPackage.METADATA_COLUMN__DEFAULT_VALUE:
            return getDefaultValue();
        case ConnectionPackage.METADATA_COLUMN__TALEND_TYPE:
            return getTalendType();
        case ConnectionPackage.METADATA_COLUMN__KEY:
            return isKey();
        case ConnectionPackage.METADATA_COLUMN__NULLABLE:
            return isNullable();
        case ConnectionPackage.METADATA_COLUMN__TABLE:
            return getTable();
        case ConnectionPackage.METADATA_COLUMN__ORIGINAL_FIELD:
            return getOriginalField();
        case ConnectionPackage.METADATA_COLUMN__PATTERN:
            return getPattern();
        case ConnectionPackage.METADATA_COLUMN__DISPLAY_FIELD:
            return getDisplayField();
        case ConnectionPackage.METADATA_COLUMN__ORIGINAL_LENGTH:
            return getOriginalLength();
        case ConnectionPackage.METADATA_COLUMN__RELATED_ENTITY:
            return getRelatedEntity();
        case ConnectionPackage.METADATA_COLUMN__RELATIONSHIP_TYPE:
            return getRelationshipType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * overides the eSet to avoid name=label behaviour in super.setLabel
     * to fix TDI-26322
     * */
    @Override
    public void eSet(int featureID, Object newValue) {
        if (ConnectionPackage.ABSTRACT_METADATA_OBJECT__LABEL == featureID) {
            setLabelFix((String) newValue);
            return;
        }
        eSetGen(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public void eSetGen(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.METADATA_COLUMN__OWNER_SCOPE:
            setOwnerScope((ScopeKind) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__OWNER:
            setOwner((Classifier) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__FEATURE_NODE:
            getFeatureNode().clear();
            getFeatureNode().addAll((Collection<? extends FeatureNode>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__FEATURE_MAP:
            getFeatureMap().clear();
            getFeatureMap().addAll((Collection<? extends FeatureMap>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__CF_MAP:
            getCfMap().clear();
            getCfMap().addAll((Collection<? extends ClassifierFeatureMap>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__CHANGEABILITY:
            setChangeability((ChangeableKind) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__MULTIPLICITY:
            setMultiplicity((Multiplicity) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__ORDERING:
            setOrdering((OrderingKind) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__TARGET_SCOPE:
            setTargetScope((ScopeKind) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__TYPE:
            setType((Classifier) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__SLOT:
            getSlot().clear();
            getSlot().addAll((Collection<? extends Slot>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION:
            getDiscriminatedUnion().clear();
            getDiscriminatedUnion().addAll((Collection<? extends Union>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE:
            getIndexedFeature().clear();
            getIndexedFeature().addAll((Collection<? extends IndexedFeature>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP:
            getKeyRelationship().clear();
            getKeyRelationship().addAll((Collection<? extends KeyRelationship>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY:
            getUniqueKey().clear();
            getUniqueKey().addAll((Collection<? extends UniqueKey>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__DATA_ITEM:
            getDataItem().clear();
            getDataItem().addAll((Collection<? extends DataItem>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__REMAP:
            getRemap().clear();
            getRemap().addAll((Collection<? extends Remap>) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE:
            setInitialValue((Expression) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__LENGTH:
            setLength((Long) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__PRECISION:
            setPrecision((Long) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__SCALE:
            setScale((Long) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__SOURCE_TYPE:
            setSourceType((String) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__DEFAULT_VALUE:
            setDefaultValue((String) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__TALEND_TYPE:
            setTalendType((String) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__KEY:
            setKey((Boolean) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__NULLABLE:
            setNullable((Boolean) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__TABLE:
            setTable((MetadataTable) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__ORIGINAL_FIELD:
            setOriginalField((String) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__PATTERN:
            setPattern((String) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__DISPLAY_FIELD:
            setDisplayField((String) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__ORIGINAL_LENGTH:
            setOriginalLength((Long) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__RELATED_ENTITY:
            setRelatedEntity((String) newValue);
            return;
        case ConnectionPackage.METADATA_COLUMN__RELATIONSHIP_TYPE:
            setRelationshipType((String) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * original setLabel used for eSet (deserialization) to override
     * super.setLabel bbehaviour that should also set the Name
     * */
    private void setLabelFix(String newValue) {
        String oldLabel = label;
        label = newValue;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.ABSTRACT_METADATA_OBJECT__LABEL, oldLabel,
                    label));
        }

    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case ConnectionPackage.METADATA_COLUMN__OWNER_SCOPE:
            setOwnerScope(OWNER_SCOPE_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__OWNER:
            setOwner((Classifier) null);
            return;
        case ConnectionPackage.METADATA_COLUMN__FEATURE_NODE:
            getFeatureNode().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__FEATURE_MAP:
            getFeatureMap().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__CF_MAP:
            getCfMap().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__CHANGEABILITY:
            setChangeability(CHANGEABILITY_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__MULTIPLICITY:
            setMultiplicity((Multiplicity) null);
            return;
        case ConnectionPackage.METADATA_COLUMN__ORDERING:
            setOrdering(ORDERING_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__TARGET_SCOPE:
            setTargetScope(TARGET_SCOPE_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__TYPE:
            setType((Classifier) null);
            return;
        case ConnectionPackage.METADATA_COLUMN__SLOT:
            getSlot().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION:
            getDiscriminatedUnion().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE:
            getIndexedFeature().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP:
            getKeyRelationship().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY:
            getUniqueKey().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__DATA_ITEM:
            getDataItem().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__REMAP:
            getRemap().clear();
            return;
        case ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE:
            setInitialValue((Expression) null);
            return;
        case ConnectionPackage.METADATA_COLUMN__LENGTH:
            setLength(LENGTH_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__PRECISION:
            setPrecision(PRECISION_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__SCALE:
            setScale(SCALE_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__SOURCE_TYPE:
            setSourceType(SOURCE_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__DEFAULT_VALUE:
            setDefaultValue(DEFAULT_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__TALEND_TYPE:
            setTalendType(TALEND_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__KEY:
            setKey(KEY_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__NULLABLE:
            setNullable(NULLABLE_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__TABLE:
            setTable((MetadataTable) null);
            return;
        case ConnectionPackage.METADATA_COLUMN__ORIGINAL_FIELD:
            setOriginalField(ORIGINAL_FIELD_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__PATTERN:
            setPattern(PATTERN_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__DISPLAY_FIELD:
            setDisplayField(DISPLAY_FIELD_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__ORIGINAL_LENGTH:
            setOriginalLength(ORIGINAL_LENGTH_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__RELATED_ENTITY:
            setRelatedEntity(RELATED_ENTITY_EDEFAULT);
            return;
        case ConnectionPackage.METADATA_COLUMN__RELATIONSHIP_TYPE:
            setRelationshipType(RELATIONSHIP_TYPE_EDEFAULT);
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
        case ConnectionPackage.METADATA_COLUMN__OWNER_SCOPE:
            return ownerScope != OWNER_SCOPE_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__OWNER:
            return getOwner() != null;
        case ConnectionPackage.METADATA_COLUMN__FEATURE_NODE:
            return featureNode != null && !featureNode.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__FEATURE_MAP:
            return featureMap != null && !featureMap.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__CF_MAP:
            return cfMap != null && !cfMap.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__CHANGEABILITY:
            return changeability != CHANGEABILITY_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__MULTIPLICITY:
            return multiplicity != null;
        case ConnectionPackage.METADATA_COLUMN__ORDERING:
            return ordering != ORDERING_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__TARGET_SCOPE:
            return targetScope != TARGET_SCOPE_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__TYPE:
            return type != null;
        case ConnectionPackage.METADATA_COLUMN__SLOT:
            return slot != null && !slot.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION:
            return discriminatedUnion != null && !discriminatedUnion.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE:
            return indexedFeature != null && !indexedFeature.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP:
            return keyRelationship != null && !keyRelationship.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY:
            return uniqueKey != null && !uniqueKey.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__DATA_ITEM:
            return dataItem != null && !dataItem.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__REMAP:
            return remap != null && !remap.isEmpty();
        case ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE:
            return initialValue != null;
        case ConnectionPackage.METADATA_COLUMN__LENGTH:
            return length != LENGTH_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__PRECISION:
            return precision != PRECISION_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__SCALE:
            return scale != SCALE_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__SOURCE_TYPE:
            return SOURCE_TYPE_EDEFAULT == null ? sourceType != null : !SOURCE_TYPE_EDEFAULT.equals(sourceType);
        case ConnectionPackage.METADATA_COLUMN__DEFAULT_VALUE:
            return DEFAULT_VALUE_EDEFAULT == null ? getDefaultValue() != null : !DEFAULT_VALUE_EDEFAULT.equals(getDefaultValue());
        case ConnectionPackage.METADATA_COLUMN__TALEND_TYPE:
            return TALEND_TYPE_EDEFAULT == null ? talendType != null : !TALEND_TYPE_EDEFAULT.equals(talendType);
        case ConnectionPackage.METADATA_COLUMN__KEY:
            return key != KEY_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__NULLABLE:
            return nullable != NULLABLE_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__TABLE:
            return getTable() != null;
        case ConnectionPackage.METADATA_COLUMN__ORIGINAL_FIELD:
            return ORIGINAL_FIELD_EDEFAULT == null ? getOriginalField() != null : !ORIGINAL_FIELD_EDEFAULT
                    .equals(getOriginalField());
        case ConnectionPackage.METADATA_COLUMN__PATTERN:
            return PATTERN_EDEFAULT == null ? pattern != null : !PATTERN_EDEFAULT.equals(pattern);
        case ConnectionPackage.METADATA_COLUMN__DISPLAY_FIELD:
            return DISPLAY_FIELD_EDEFAULT == null ? displayField != null : !DISPLAY_FIELD_EDEFAULT.equals(displayField);
        case ConnectionPackage.METADATA_COLUMN__ORIGINAL_LENGTH:
            return originalLength != ORIGINAL_LENGTH_EDEFAULT;
        case ConnectionPackage.METADATA_COLUMN__RELATED_ENTITY:
            return RELATED_ENTITY_EDEFAULT == null ? relatedEntity != null : !RELATED_ENTITY_EDEFAULT.equals(relatedEntity);
        case ConnectionPackage.METADATA_COLUMN__RELATIONSHIP_TYPE:
            return RELATIONSHIP_TYPE_EDEFAULT == null ? relationshipType != null : !RELATIONSHIP_TYPE_EDEFAULT
                    .equals(relationshipType);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == Feature.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.METADATA_COLUMN__OWNER_SCOPE:
                return CorePackage.FEATURE__OWNER_SCOPE;
            case ConnectionPackage.METADATA_COLUMN__OWNER:
                return CorePackage.FEATURE__OWNER;
            case ConnectionPackage.METADATA_COLUMN__FEATURE_NODE:
                return CorePackage.FEATURE__FEATURE_NODE;
            case ConnectionPackage.METADATA_COLUMN__FEATURE_MAP:
                return CorePackage.FEATURE__FEATURE_MAP;
            case ConnectionPackage.METADATA_COLUMN__CF_MAP:
                return CorePackage.FEATURE__CF_MAP;
            default:
                return -1;
            }
        }
        if (baseClass == StructuralFeature.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.METADATA_COLUMN__CHANGEABILITY:
                return CorePackage.STRUCTURAL_FEATURE__CHANGEABILITY;
            case ConnectionPackage.METADATA_COLUMN__MULTIPLICITY:
                return CorePackage.STRUCTURAL_FEATURE__MULTIPLICITY;
            case ConnectionPackage.METADATA_COLUMN__ORDERING:
                return CorePackage.STRUCTURAL_FEATURE__ORDERING;
            case ConnectionPackage.METADATA_COLUMN__TARGET_SCOPE:
                return CorePackage.STRUCTURAL_FEATURE__TARGET_SCOPE;
            case ConnectionPackage.METADATA_COLUMN__TYPE:
                return CorePackage.STRUCTURAL_FEATURE__TYPE;
            case ConnectionPackage.METADATA_COLUMN__SLOT:
                return CorePackage.STRUCTURAL_FEATURE__SLOT;
            case ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION:
                return CorePackage.STRUCTURAL_FEATURE__DISCRIMINATED_UNION;
            case ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE:
                return CorePackage.STRUCTURAL_FEATURE__INDEXED_FEATURE;
            case ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP:
                return CorePackage.STRUCTURAL_FEATURE__KEY_RELATIONSHIP;
            case ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY:
                return CorePackage.STRUCTURAL_FEATURE__UNIQUE_KEY;
            case ConnectionPackage.METADATA_COLUMN__DATA_ITEM:
                return CorePackage.STRUCTURAL_FEATURE__DATA_ITEM;
            case ConnectionPackage.METADATA_COLUMN__REMAP:
                return CorePackage.STRUCTURAL_FEATURE__REMAP;
            default:
                return -1;
            }
        }
        if (baseClass == Attribute.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE:
                return CorePackage.ATTRIBUTE__INITIAL_VALUE;
            default:
                return -1;
            }
        }
        if (baseClass == Field.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.METADATA_COLUMN__LENGTH:
                return RecordPackage.FIELD__LENGTH;
            case ConnectionPackage.METADATA_COLUMN__PRECISION:
                return RecordPackage.FIELD__PRECISION;
            case ConnectionPackage.METADATA_COLUMN__SCALE:
                return RecordPackage.FIELD__SCALE;
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
        if (baseClass == Feature.class) {
            switch (baseFeatureID) {
            case CorePackage.FEATURE__OWNER_SCOPE:
                return ConnectionPackage.METADATA_COLUMN__OWNER_SCOPE;
            case CorePackage.FEATURE__OWNER:
                return ConnectionPackage.METADATA_COLUMN__OWNER;
            case CorePackage.FEATURE__FEATURE_NODE:
                return ConnectionPackage.METADATA_COLUMN__FEATURE_NODE;
            case CorePackage.FEATURE__FEATURE_MAP:
                return ConnectionPackage.METADATA_COLUMN__FEATURE_MAP;
            case CorePackage.FEATURE__CF_MAP:
                return ConnectionPackage.METADATA_COLUMN__CF_MAP;
            default:
                return -1;
            }
        }
        if (baseClass == StructuralFeature.class) {
            switch (baseFeatureID) {
            case CorePackage.STRUCTURAL_FEATURE__CHANGEABILITY:
                return ConnectionPackage.METADATA_COLUMN__CHANGEABILITY;
            case CorePackage.STRUCTURAL_FEATURE__MULTIPLICITY:
                return ConnectionPackage.METADATA_COLUMN__MULTIPLICITY;
            case CorePackage.STRUCTURAL_FEATURE__ORDERING:
                return ConnectionPackage.METADATA_COLUMN__ORDERING;
            case CorePackage.STRUCTURAL_FEATURE__TARGET_SCOPE:
                return ConnectionPackage.METADATA_COLUMN__TARGET_SCOPE;
            case CorePackage.STRUCTURAL_FEATURE__TYPE:
                return ConnectionPackage.METADATA_COLUMN__TYPE;
            case CorePackage.STRUCTURAL_FEATURE__SLOT:
                return ConnectionPackage.METADATA_COLUMN__SLOT;
            case CorePackage.STRUCTURAL_FEATURE__DISCRIMINATED_UNION:
                return ConnectionPackage.METADATA_COLUMN__DISCRIMINATED_UNION;
            case CorePackage.STRUCTURAL_FEATURE__INDEXED_FEATURE:
                return ConnectionPackage.METADATA_COLUMN__INDEXED_FEATURE;
            case CorePackage.STRUCTURAL_FEATURE__KEY_RELATIONSHIP:
                return ConnectionPackage.METADATA_COLUMN__KEY_RELATIONSHIP;
            case CorePackage.STRUCTURAL_FEATURE__UNIQUE_KEY:
                return ConnectionPackage.METADATA_COLUMN__UNIQUE_KEY;
            case CorePackage.STRUCTURAL_FEATURE__DATA_ITEM:
                return ConnectionPackage.METADATA_COLUMN__DATA_ITEM;
            case CorePackage.STRUCTURAL_FEATURE__REMAP:
                return ConnectionPackage.METADATA_COLUMN__REMAP;
            default:
                return -1;
            }
        }
        if (baseClass == Attribute.class) {
            switch (baseFeatureID) {
            case CorePackage.ATTRIBUTE__INITIAL_VALUE:
                return ConnectionPackage.METADATA_COLUMN__INITIAL_VALUE;
            default:
                return -1;
            }
        }
        if (baseClass == Field.class) {
            switch (baseFeatureID) {
            case RecordPackage.FIELD__LENGTH:
                return ConnectionPackage.METADATA_COLUMN__LENGTH;
            case RecordPackage.FIELD__PRECISION:
                return ConnectionPackage.METADATA_COLUMN__PRECISION;
            case RecordPackage.FIELD__SCALE:
                return ConnectionPackage.METADATA_COLUMN__SCALE;
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
        result.append(" (ownerScope: ");
        result.append(ownerScope);
        result.append(", changeability: ");
        result.append(changeability);
        result.append(", ordering: ");
        result.append(ordering);
        result.append(", targetScope: ");
        result.append(targetScope);
        result.append(", length: ");
        result.append(length);
        result.append(", precision: ");
        result.append(precision);
        result.append(", scale: ");
        result.append(scale);
        result.append(", sourceType: ");
        result.append(sourceType);
        result.append(", talendType: ");
        result.append(talendType);
        result.append(", key: ");
        result.append(key);
        result.append(", nullable: ");
        result.append(nullable);
        result.append(", pattern: ");
        result.append(pattern);
        result.append(", displayField: ");
        result.append(displayField);
        result.append(", originalLength: ");
        result.append(originalLength);
        result.append(", relatedEntity: ");
        result.append(relatedEntity);
        result.append(", relationshipType: ");
        result.append(relationshipType);
        result.append(')');
        return result.toString();
    }

    @Override
    public String getName() {
        // MOD yyi 2011-06-23 22700: override the method for flatfile column
        return null == super.getName() ? this.getId() : super.getName();
    }
} // MetadataColumnImpl
