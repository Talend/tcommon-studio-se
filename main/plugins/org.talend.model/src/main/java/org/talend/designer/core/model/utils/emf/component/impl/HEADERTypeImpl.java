/**
 */
package org.talend.designer.core.model.utils.emf.component.impl;

import java.math.BigDecimal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.talend.designer.core.model.utils.emf.component.ComponentPackage;
import org.talend.designer.core.model.utils.emf.component.FORMATType;
import org.talend.designer.core.model.utils.emf.component.HEADERType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>HEADER Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getSIGNATURE <em>SIGNATURE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getFORMAT <em>FORMAT</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getAUTHOR <em>AUTHOR</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getCOMBINE <em>COMBINE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getCOMPATIBILITY <em>COMPATIBILITY</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isDATAAUTOPROPAGATE <em>DATAAUTOPROPAGATE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getEXTENSION <em>EXTENSION</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isHASCONDITIONALOUTPUTS <em>HASCONDITIONALOUTPUTS</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isHASHCOMPONENT <em>HASHCOMPONENT</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isISMULTIPLYINGOUTPUTS <em>ISMULTIPLYINGOUTPUTS</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isLOG4J_ENABLED <em>LOG4J ENABLED</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isMAINCODECALLED <em>MAINCODECALLED</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getNUMBERPARALLELIZE <em>NUMBERPARALLELIZE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isPARALLELIZE <em>PARALLELIZE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getPARTITIONING <em>PARTITIONING</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getPLATEFORM <em>PLATEFORM</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isREDUCE <em>REDUCE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getRELEASEDATE <em>RELEASEDATE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isSCHEMAAUTOPROPAGATE <em>SCHEMAAUTOPROPAGATE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getSERIAL <em>SERIAL</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getSHORTNAME <em>SHORTNAME</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isSINGLETON <em>SINGLETON</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isSTARTABLE <em>STARTABLE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getSTATUS <em>STATUS</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getSUBJOBCOLOR <em>SUBJOBCOLOR</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getSUBJOBTITLECOLOR <em>SUBJOBTITLECOLOR</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isSUPPORTS_DB_TYPE <em>SUPPORTS DB TYPE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isTECHNICAL <em>TECHNICAL</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isTSTATCATCHERSTATS <em>TSTATCATCHERSTATS</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getTYPE <em>TYPE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getVERSION <em>VERSION</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isVISIBLE <em>VISIBLE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getEQUIVALENT <em>EQUIVALENT</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getINPUTTYPE <em>INPUTTYPE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#getOUTPUTTYPE <em>OUTPUTTYPE</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isSPARKACTION <em>SPARKACTION</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.component.impl.HEADERTypeImpl#isACTIVE_DB_COLUMNS <em>ACTIVE DB COLUMNS</em>}</li>
 * </ul>
 *
 * @generated
 */
public class HEADERTypeImpl extends EObjectImpl implements HEADERType {
    /**
     * The default value of the '{@link #getSIGNATURE() <em>SIGNATURE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSIGNATURE()
     * @generated
     * @ordered
     */
    protected static final String SIGNATURE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSIGNATURE() <em>SIGNATURE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSIGNATURE()
     * @generated
     * @ordered
     */
    protected String sIGNATURE = SIGNATURE_EDEFAULT;

    /**
     * The cached value of the '{@link #getFORMAT() <em>FORMAT</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFORMAT()
     * @generated
     * @ordered
     */
    protected FORMATType fORMAT;

    /**
     * The default value of the '{@link #getAUTHOR() <em>AUTHOR</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAUTHOR()
     * @generated
     * @ordered
     */
    protected static final String AUTHOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAUTHOR() <em>AUTHOR</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAUTHOR()
     * @generated
     * @ordered
     */
    protected String aUTHOR = AUTHOR_EDEFAULT;

    /**
     * The default value of the '{@link #getCOMBINE() <em>COMBINE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCOMBINE()
     * @generated
     * @ordered
     */
    protected static final String COMBINE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCOMBINE() <em>COMBINE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCOMBINE()
     * @generated
     * @ordered
     */
    protected String cOMBINE = COMBINE_EDEFAULT;

    /**
     * The default value of the '{@link #getCOMPATIBILITY() <em>COMPATIBILITY</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCOMPATIBILITY()
     * @generated
     * @ordered
     */
    protected static final String COMPATIBILITY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCOMPATIBILITY() <em>COMPATIBILITY</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCOMPATIBILITY()
     * @generated
     * @ordered
     */
    protected String cOMPATIBILITY = COMPATIBILITY_EDEFAULT;

    /**
     * The default value of the '{@link #isDATAAUTOPROPAGATE() <em>DATAAUTOPROPAGATE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDATAAUTOPROPAGATE()
     * @generated
     * @ordered
     */
    protected static final boolean DATAAUTOPROPAGATE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isDATAAUTOPROPAGATE() <em>DATAAUTOPROPAGATE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDATAAUTOPROPAGATE()
     * @generated
     * @ordered
     */
    protected boolean dATAAUTOPROPAGATE = DATAAUTOPROPAGATE_EDEFAULT;

    /**
     * This is true if the DATAAUTOPROPAGATE attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean dATAAUTOPROPAGATEESet;

    /**
     * The default value of the '{@link #getEXTENSION() <em>EXTENSION</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEXTENSION()
     * @generated
     * @ordered
     */
    protected static final String EXTENSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEXTENSION() <em>EXTENSION</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEXTENSION()
     * @generated
     * @ordered
     */
    protected String eXTENSION = EXTENSION_EDEFAULT;

    /**
     * The default value of the '{@link #isHASCONDITIONALOUTPUTS() <em>HASCONDITIONALOUTPUTS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isHASCONDITIONALOUTPUTS()
     * @generated
     * @ordered
     */
    protected static final boolean HASCONDITIONALOUTPUTS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isHASCONDITIONALOUTPUTS() <em>HASCONDITIONALOUTPUTS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isHASCONDITIONALOUTPUTS()
     * @generated
     * @ordered
     */
    protected boolean hASCONDITIONALOUTPUTS = HASCONDITIONALOUTPUTS_EDEFAULT;

    /**
     * This is true if the HASCONDITIONALOUTPUTS attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean hASCONDITIONALOUTPUTSESet;

    /**
     * The default value of the '{@link #isHASHCOMPONENT() <em>HASHCOMPONENT</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isHASHCOMPONENT()
     * @generated
     * @ordered
     */
    protected static final boolean HASHCOMPONENT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isHASHCOMPONENT() <em>HASHCOMPONENT</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isHASHCOMPONENT()
     * @generated
     * @ordered
     */
    protected boolean hASHCOMPONENT = HASHCOMPONENT_EDEFAULT;

    /**
     * This is true if the HASHCOMPONENT attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean hASHCOMPONENTESet;

    /**
     * The default value of the '{@link #isISMULTIPLYINGOUTPUTS() <em>ISMULTIPLYINGOUTPUTS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isISMULTIPLYINGOUTPUTS()
     * @generated
     * @ordered
     */
    protected static final boolean ISMULTIPLYINGOUTPUTS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isISMULTIPLYINGOUTPUTS() <em>ISMULTIPLYINGOUTPUTS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isISMULTIPLYINGOUTPUTS()
     * @generated
     * @ordered
     */
    protected boolean iSMULTIPLYINGOUTPUTS = ISMULTIPLYINGOUTPUTS_EDEFAULT;

    /**
     * This is true if the ISMULTIPLYINGOUTPUTS attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean iSMULTIPLYINGOUTPUTSESet;

    /**
     * The default value of the '{@link #isLOG4J_ENABLED() <em>LOG4J ENABLED</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLOG4J_ENABLED()
     * @generated
     * @ordered
     */
    protected static final boolean LOG4J_ENABLED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isLOG4J_ENABLED() <em>LOG4J ENABLED</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLOG4J_ENABLED()
     * @generated
     * @ordered
     */
    protected boolean lOG4J_ENABLED = LOG4J_ENABLED_EDEFAULT;

    /**
     * This is true if the LOG4J ENABLED attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean lOG4J_ENABLEDESet;

    /**
     * The default value of the '{@link #isMAINCODECALLED() <em>MAINCODECALLED</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isMAINCODECALLED()
     * @generated
     * @ordered
     */
    protected static final boolean MAINCODECALLED_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isMAINCODECALLED() <em>MAINCODECALLED</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isMAINCODECALLED()
     * @generated
     * @ordered
     */
    protected boolean mAINCODECALLED = MAINCODECALLED_EDEFAULT;

    /**
     * This is true if the MAINCODECALLED attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean mAINCODECALLEDESet;

    /**
     * The default value of the '{@link #getNUMBERPARALLELIZE() <em>NUMBERPARALLELIZE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNUMBERPARALLELIZE()
     * @generated
     * @ordered
     */
    protected static final String NUMBERPARALLELIZE_EDEFAULT = "2";

    /**
     * The cached value of the '{@link #getNUMBERPARALLELIZE() <em>NUMBERPARALLELIZE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNUMBERPARALLELIZE()
     * @generated
     * @ordered
     */
    protected String nUMBERPARALLELIZE = NUMBERPARALLELIZE_EDEFAULT;

    /**
     * This is true if the NUMBERPARALLELIZE attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean nUMBERPARALLELIZEESet;

    /**
     * The default value of the '{@link #isPARALLELIZE() <em>PARALLELIZE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isPARALLELIZE()
     * @generated
     * @ordered
     */
    protected static final boolean PARALLELIZE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isPARALLELIZE() <em>PARALLELIZE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isPARALLELIZE()
     * @generated
     * @ordered
     */
    protected boolean pARALLELIZE = PARALLELIZE_EDEFAULT;

    /**
     * This is true if the PARALLELIZE attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean pARALLELIZEESet;

    /**
     * The default value of the '{@link #getPARTITIONING() <em>PARTITIONING</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPARTITIONING()
     * @generated
     * @ordered
     */
    protected static final String PARTITIONING_EDEFAULT = "AUTO";

    /**
     * The cached value of the '{@link #getPARTITIONING() <em>PARTITIONING</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPARTITIONING()
     * @generated
     * @ordered
     */
    protected String pARTITIONING = PARTITIONING_EDEFAULT;

    /**
     * The default value of the '{@link #getPLATEFORM() <em>PLATEFORM</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPLATEFORM()
     * @generated
     * @ordered
     */
    protected static final String PLATEFORM_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPLATEFORM() <em>PLATEFORM</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPLATEFORM()
     * @generated
     * @ordered
     */
    protected String pLATEFORM = PLATEFORM_EDEFAULT;

    /**
     * The default value of the '{@link #isREDUCE() <em>REDUCE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isREDUCE()
     * @generated
     * @ordered
     */
    protected static final boolean REDUCE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isREDUCE() <em>REDUCE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isREDUCE()
     * @generated
     * @ordered
     */
    protected boolean rEDUCE = REDUCE_EDEFAULT;

    /**
     * The default value of the '{@link #getRELEASEDATE() <em>RELEASEDATE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRELEASEDATE()
     * @generated
     * @ordered
     */
    protected static final String RELEASEDATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRELEASEDATE() <em>RELEASEDATE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRELEASEDATE()
     * @generated
     * @ordered
     */
    protected String rELEASEDATE = RELEASEDATE_EDEFAULT;

    /**
     * The default value of the '{@link #isSCHEMAAUTOPROPAGATE() <em>SCHEMAAUTOPROPAGATE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSCHEMAAUTOPROPAGATE()
     * @generated
     * @ordered
     */
    protected static final boolean SCHEMAAUTOPROPAGATE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isSCHEMAAUTOPROPAGATE() <em>SCHEMAAUTOPROPAGATE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSCHEMAAUTOPROPAGATE()
     * @generated
     * @ordered
     */
    protected boolean sCHEMAAUTOPROPAGATE = SCHEMAAUTOPROPAGATE_EDEFAULT;

    /**
     * This is true if the SCHEMAAUTOPROPAGATE attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean sCHEMAAUTOPROPAGATEESet;

    /**
     * The default value of the '{@link #getSERIAL() <em>SERIAL</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSERIAL()
     * @generated
     * @ordered
     */
    protected static final String SERIAL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSERIAL() <em>SERIAL</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSERIAL()
     * @generated
     * @ordered
     */
    protected String sERIAL = SERIAL_EDEFAULT;

    /**
     * The default value of the '{@link #getSHORTNAME() <em>SHORTNAME</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSHORTNAME()
     * @generated
     * @ordered
     */
    protected static final String SHORTNAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSHORTNAME() <em>SHORTNAME</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSHORTNAME()
     * @generated
     * @ordered
     */
    protected String sHORTNAME = SHORTNAME_EDEFAULT;

    /**
     * The default value of the '{@link #isSINGLETON() <em>SINGLETON</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSINGLETON()
     * @generated
     * @ordered
     */
    protected static final boolean SINGLETON_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSINGLETON() <em>SINGLETON</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSINGLETON()
     * @generated
     * @ordered
     */
    protected boolean sINGLETON = SINGLETON_EDEFAULT;

    /**
     * This is true if the SINGLETON attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean sINGLETONESet;

    /**
     * The default value of the '{@link #isSTARTABLE() <em>STARTABLE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSTARTABLE()
     * @generated
     * @ordered
     */
    protected static final boolean STARTABLE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSTARTABLE() <em>STARTABLE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSTARTABLE()
     * @generated
     * @ordered
     */
    protected boolean sTARTABLE = STARTABLE_EDEFAULT;

    /**
     * This is true if the STARTABLE attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean sTARTABLEESet;

    /**
     * The default value of the '{@link #getSTATUS() <em>STATUS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSTATUS()
     * @generated
     * @ordered
     */
    protected static final String STATUS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSTATUS() <em>STATUS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSTATUS()
     * @generated
     * @ordered
     */
    protected String sTATUS = STATUS_EDEFAULT;

    /**
     * The default value of the '{@link #getSUBJOBCOLOR() <em>SUBJOBCOLOR</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSUBJOBCOLOR()
     * @generated
     * @ordered
     */
    protected static final String SUBJOBCOLOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSUBJOBCOLOR() <em>SUBJOBCOLOR</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSUBJOBCOLOR()
     * @generated
     * @ordered
     */
    protected String sUBJOBCOLOR = SUBJOBCOLOR_EDEFAULT;

    /**
     * The default value of the '{@link #getSUBJOBTITLECOLOR() <em>SUBJOBTITLECOLOR</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSUBJOBTITLECOLOR()
     * @generated
     * @ordered
     */
    protected static final String SUBJOBTITLECOLOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSUBJOBTITLECOLOR() <em>SUBJOBTITLECOLOR</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSUBJOBTITLECOLOR()
     * @generated
     * @ordered
     */
    protected String sUBJOBTITLECOLOR = SUBJOBTITLECOLOR_EDEFAULT;

    /**
     * The default value of the '{@link #isSUPPORTS_DB_TYPE() <em>SUPPORTS DB TYPE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSUPPORTS_DB_TYPE()
     * @generated
     * @ordered
     */
    protected static final boolean SUPPORTS_DB_TYPE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSUPPORTS_DB_TYPE() <em>SUPPORTS DB TYPE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSUPPORTS_DB_TYPE()
     * @generated
     * @ordered
     */
    protected boolean sUPPORTS_DB_TYPE = SUPPORTS_DB_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #isTECHNICAL() <em>TECHNICAL</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isTECHNICAL()
     * @generated
     * @ordered
     */
    protected static final boolean TECHNICAL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isTECHNICAL() <em>TECHNICAL</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isTECHNICAL()
     * @generated
     * @ordered
     */
    protected boolean tECHNICAL = TECHNICAL_EDEFAULT;

    /**
     * This is true if the TECHNICAL attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean tECHNICALESet;

    /**
     * The default value of the '{@link #isTSTATCATCHERSTATS() <em>TSTATCATCHERSTATS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isTSTATCATCHERSTATS()
     * @generated
     * @ordered
     */
    protected static final boolean TSTATCATCHERSTATS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isTSTATCATCHERSTATS() <em>TSTATCATCHERSTATS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isTSTATCATCHERSTATS()
     * @generated
     * @ordered
     */
    protected boolean tSTATCATCHERSTATS = TSTATCATCHERSTATS_EDEFAULT;

    /**
     * This is true if the TSTATCATCHERSTATS attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean tSTATCATCHERSTATSESet;

    /**
     * The default value of the '{@link #getTYPE() <em>TYPE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTYPE()
     * @generated
     * @ordered
     */
    protected static final String TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTYPE() <em>TYPE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTYPE()
     * @generated
     * @ordered
     */
    protected String tYPE = TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getVERSION() <em>VERSION</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVERSION()
     * @generated
     * @ordered
     */
    protected static final BigDecimal VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVERSION() <em>VERSION</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVERSION()
     * @generated
     * @ordered
     */
    protected BigDecimal vERSION = VERSION_EDEFAULT;

    /**
     * The default value of the '{@link #isVISIBLE() <em>VISIBLE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isVISIBLE()
     * @generated
     * @ordered
     */
    protected static final boolean VISIBLE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isVISIBLE() <em>VISIBLE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isVISIBLE()
     * @generated
     * @ordered
     */
    protected boolean vISIBLE = VISIBLE_EDEFAULT;

    /**
     * This is true if the VISIBLE attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean vISIBLEESet;

    /**
     * The default value of the '{@link #getEQUIVALENT() <em>EQUIVALENT</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEQUIVALENT()
     * @generated
     * @ordered
     */
    protected static final String EQUIVALENT_EDEFAULT = "false";

    /**
     * The cached value of the '{@link #getEQUIVALENT() <em>EQUIVALENT</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEQUIVALENT()
     * @generated
     * @ordered
     */
    protected String eQUIVALENT = EQUIVALENT_EDEFAULT;

    /**
     * This is true if the EQUIVALENT attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean eQUIVALENTESet;

    /**
     * The default value of the '{@link #getINPUTTYPE() <em>INPUTTYPE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getINPUTTYPE()
     * @generated
     * @ordered
     */
    protected static final String INPUTTYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getINPUTTYPE() <em>INPUTTYPE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getINPUTTYPE()
     * @generated
     * @ordered
     */
    protected String iNPUTTYPE = INPUTTYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getOUTPUTTYPE() <em>OUTPUTTYPE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOUTPUTTYPE()
     * @generated
     * @ordered
     */
    protected static final String OUTPUTTYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOUTPUTTYPE() <em>OUTPUTTYPE</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOUTPUTTYPE()
     * @generated
     * @ordered
     */
    protected String oUTPUTTYPE = OUTPUTTYPE_EDEFAULT;

    /**
     * The default value of the '{@link #isSPARKACTION() <em>SPARKACTION</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSPARKACTION()
     * @generated
     * @ordered
     */
    protected static final boolean SPARKACTION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSPARKACTION() <em>SPARKACTION</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSPARKACTION()
     * @generated
     * @ordered
     */
    protected boolean sPARKACTION = SPARKACTION_EDEFAULT;

    /**
     * This is true if the SPARKACTION attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean sPARKACTIONESet;

    /**
     * The default value of the '{@link #isACTIVE_DB_COLUMNS() <em>ACTIVE DB COLUMNS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isACTIVE_DB_COLUMNS()
     * @generated
     * @ordered
     */
    protected static final boolean ACTIVE_DB_COLUMNS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isACTIVE_DB_COLUMNS() <em>ACTIVE DB COLUMNS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isACTIVE_DB_COLUMNS()
     * @generated
     * @ordered
     */
    protected boolean aCTIVE_DB_COLUMNS = ACTIVE_DB_COLUMNS_EDEFAULT;

    /**
     * This is true if the ACTIVE DB COLUMNS attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean aCTIVE_DB_COLUMNSESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected HEADERTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return ComponentPackage.Literals.HEADER_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSIGNATURE() {
        return sIGNATURE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSIGNATURE(String newSIGNATURE) {
        String oldSIGNATURE = sIGNATURE;
        sIGNATURE = newSIGNATURE;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__SIGNATURE, oldSIGNATURE, sIGNATURE));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FORMATType getFORMAT() {
        return fORMAT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFORMAT(FORMATType newFORMAT, NotificationChain msgs) {
        FORMATType oldFORMAT = fORMAT;
        fORMAT = newFORMAT;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__FORMAT, oldFORMAT, newFORMAT);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFORMAT(FORMATType newFORMAT) {
        if (newFORMAT != fORMAT) {
            NotificationChain msgs = null;
            if (fORMAT != null)
                msgs = ((InternalEObject)fORMAT).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ComponentPackage.HEADER_TYPE__FORMAT, null, msgs);
            if (newFORMAT != null)
                msgs = ((InternalEObject)newFORMAT).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ComponentPackage.HEADER_TYPE__FORMAT, null, msgs);
            msgs = basicSetFORMAT(newFORMAT, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__FORMAT, newFORMAT, newFORMAT));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAUTHOR() {
        return aUTHOR;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAUTHOR(String newAUTHOR) {
        String oldAUTHOR = aUTHOR;
        aUTHOR = newAUTHOR;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__AUTHOR, oldAUTHOR, aUTHOR));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCOMBINE() {
        return cOMBINE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCOMBINE(String newCOMBINE) {
        String oldCOMBINE = cOMBINE;
        cOMBINE = newCOMBINE;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__COMBINE, oldCOMBINE, cOMBINE));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCOMPATIBILITY() {
        return cOMPATIBILITY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCOMPATIBILITY(String newCOMPATIBILITY) {
        String oldCOMPATIBILITY = cOMPATIBILITY;
        cOMPATIBILITY = newCOMPATIBILITY;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__COMPATIBILITY, oldCOMPATIBILITY, cOMPATIBILITY));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isDATAAUTOPROPAGATE() {
        return dATAAUTOPROPAGATE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDATAAUTOPROPAGATE(boolean newDATAAUTOPROPAGATE) {
        boolean oldDATAAUTOPROPAGATE = dATAAUTOPROPAGATE;
        dATAAUTOPROPAGATE = newDATAAUTOPROPAGATE;
        boolean oldDATAAUTOPROPAGATEESet = dATAAUTOPROPAGATEESet;
        dATAAUTOPROPAGATEESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__DATAAUTOPROPAGATE, oldDATAAUTOPROPAGATE, dATAAUTOPROPAGATE, !oldDATAAUTOPROPAGATEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetDATAAUTOPROPAGATE() {
        boolean oldDATAAUTOPROPAGATE = dATAAUTOPROPAGATE;
        boolean oldDATAAUTOPROPAGATEESet = dATAAUTOPROPAGATEESet;
        dATAAUTOPROPAGATE = DATAAUTOPROPAGATE_EDEFAULT;
        dATAAUTOPROPAGATEESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__DATAAUTOPROPAGATE, oldDATAAUTOPROPAGATE, DATAAUTOPROPAGATE_EDEFAULT, oldDATAAUTOPROPAGATEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetDATAAUTOPROPAGATE() {
        return dATAAUTOPROPAGATEESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEXTENSION() {
        return eXTENSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEXTENSION(String newEXTENSION) {
        String oldEXTENSION = eXTENSION;
        eXTENSION = newEXTENSION;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__EXTENSION, oldEXTENSION, eXTENSION));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isHASCONDITIONALOUTPUTS() {
        return hASCONDITIONALOUTPUTS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHASCONDITIONALOUTPUTS(boolean newHASCONDITIONALOUTPUTS) {
        boolean oldHASCONDITIONALOUTPUTS = hASCONDITIONALOUTPUTS;
        hASCONDITIONALOUTPUTS = newHASCONDITIONALOUTPUTS;
        boolean oldHASCONDITIONALOUTPUTSESet = hASCONDITIONALOUTPUTSESet;
        hASCONDITIONALOUTPUTSESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__HASCONDITIONALOUTPUTS, oldHASCONDITIONALOUTPUTS, hASCONDITIONALOUTPUTS, !oldHASCONDITIONALOUTPUTSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetHASCONDITIONALOUTPUTS() {
        boolean oldHASCONDITIONALOUTPUTS = hASCONDITIONALOUTPUTS;
        boolean oldHASCONDITIONALOUTPUTSESet = hASCONDITIONALOUTPUTSESet;
        hASCONDITIONALOUTPUTS = HASCONDITIONALOUTPUTS_EDEFAULT;
        hASCONDITIONALOUTPUTSESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__HASCONDITIONALOUTPUTS, oldHASCONDITIONALOUTPUTS, HASCONDITIONALOUTPUTS_EDEFAULT, oldHASCONDITIONALOUTPUTSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetHASCONDITIONALOUTPUTS() {
        return hASCONDITIONALOUTPUTSESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isHASHCOMPONENT() {
        return hASHCOMPONENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHASHCOMPONENT(boolean newHASHCOMPONENT) {
        boolean oldHASHCOMPONENT = hASHCOMPONENT;
        hASHCOMPONENT = newHASHCOMPONENT;
        boolean oldHASHCOMPONENTESet = hASHCOMPONENTESet;
        hASHCOMPONENTESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__HASHCOMPONENT, oldHASHCOMPONENT, hASHCOMPONENT, !oldHASHCOMPONENTESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetHASHCOMPONENT() {
        boolean oldHASHCOMPONENT = hASHCOMPONENT;
        boolean oldHASHCOMPONENTESet = hASHCOMPONENTESet;
        hASHCOMPONENT = HASHCOMPONENT_EDEFAULT;
        hASHCOMPONENTESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__HASHCOMPONENT, oldHASHCOMPONENT, HASHCOMPONENT_EDEFAULT, oldHASHCOMPONENTESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetHASHCOMPONENT() {
        return hASHCOMPONENTESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isISMULTIPLYINGOUTPUTS() {
        return iSMULTIPLYINGOUTPUTS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setISMULTIPLYINGOUTPUTS(boolean newISMULTIPLYINGOUTPUTS) {
        boolean oldISMULTIPLYINGOUTPUTS = iSMULTIPLYINGOUTPUTS;
        iSMULTIPLYINGOUTPUTS = newISMULTIPLYINGOUTPUTS;
        boolean oldISMULTIPLYINGOUTPUTSESet = iSMULTIPLYINGOUTPUTSESet;
        iSMULTIPLYINGOUTPUTSESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__ISMULTIPLYINGOUTPUTS, oldISMULTIPLYINGOUTPUTS, iSMULTIPLYINGOUTPUTS, !oldISMULTIPLYINGOUTPUTSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetISMULTIPLYINGOUTPUTS() {
        boolean oldISMULTIPLYINGOUTPUTS = iSMULTIPLYINGOUTPUTS;
        boolean oldISMULTIPLYINGOUTPUTSESet = iSMULTIPLYINGOUTPUTSESet;
        iSMULTIPLYINGOUTPUTS = ISMULTIPLYINGOUTPUTS_EDEFAULT;
        iSMULTIPLYINGOUTPUTSESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__ISMULTIPLYINGOUTPUTS, oldISMULTIPLYINGOUTPUTS, ISMULTIPLYINGOUTPUTS_EDEFAULT, oldISMULTIPLYINGOUTPUTSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetISMULTIPLYINGOUTPUTS() {
        return iSMULTIPLYINGOUTPUTSESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isMAINCODECALLED() {
        return mAINCODECALLED;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMAINCODECALLED(boolean newMAINCODECALLED) {
        boolean oldMAINCODECALLED = mAINCODECALLED;
        mAINCODECALLED = newMAINCODECALLED;
        boolean oldMAINCODECALLEDESet = mAINCODECALLEDESet;
        mAINCODECALLEDESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__MAINCODECALLED, oldMAINCODECALLED, mAINCODECALLED, !oldMAINCODECALLEDESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetMAINCODECALLED() {
        boolean oldMAINCODECALLED = mAINCODECALLED;
        boolean oldMAINCODECALLEDESet = mAINCODECALLEDESet;
        mAINCODECALLED = MAINCODECALLED_EDEFAULT;
        mAINCODECALLEDESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__MAINCODECALLED, oldMAINCODECALLED, MAINCODECALLED_EDEFAULT, oldMAINCODECALLEDESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetMAINCODECALLED() {
        return mAINCODECALLEDESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getNUMBERPARALLELIZE() {
        return nUMBERPARALLELIZE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNUMBERPARALLELIZE(String newNUMBERPARALLELIZE) {
        String oldNUMBERPARALLELIZE = nUMBERPARALLELIZE;
        nUMBERPARALLELIZE = newNUMBERPARALLELIZE;
        boolean oldNUMBERPARALLELIZEESet = nUMBERPARALLELIZEESet;
        nUMBERPARALLELIZEESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__NUMBERPARALLELIZE, oldNUMBERPARALLELIZE, nUMBERPARALLELIZE, !oldNUMBERPARALLELIZEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetNUMBERPARALLELIZE() {
        String oldNUMBERPARALLELIZE = nUMBERPARALLELIZE;
        boolean oldNUMBERPARALLELIZEESet = nUMBERPARALLELIZEESet;
        nUMBERPARALLELIZE = NUMBERPARALLELIZE_EDEFAULT;
        nUMBERPARALLELIZEESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__NUMBERPARALLELIZE, oldNUMBERPARALLELIZE, NUMBERPARALLELIZE_EDEFAULT, oldNUMBERPARALLELIZEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetNUMBERPARALLELIZE() {
        return nUMBERPARALLELIZEESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isPARALLELIZE() {
        return pARALLELIZE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPARALLELIZE(boolean newPARALLELIZE) {
        boolean oldPARALLELIZE = pARALLELIZE;
        pARALLELIZE = newPARALLELIZE;
        boolean oldPARALLELIZEESet = pARALLELIZEESet;
        pARALLELIZEESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__PARALLELIZE, oldPARALLELIZE, pARALLELIZE, !oldPARALLELIZEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetPARALLELIZE() {
        boolean oldPARALLELIZE = pARALLELIZE;
        boolean oldPARALLELIZEESet = pARALLELIZEESet;
        pARALLELIZE = PARALLELIZE_EDEFAULT;
        pARALLELIZEESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__PARALLELIZE, oldPARALLELIZE, PARALLELIZE_EDEFAULT, oldPARALLELIZEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetPARALLELIZE() {
        return pARALLELIZEESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPLATEFORM() {
        return pLATEFORM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPLATEFORM(String newPLATEFORM) {
        String oldPLATEFORM = pLATEFORM;
        pLATEFORM = newPLATEFORM;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__PLATEFORM, oldPLATEFORM, pLATEFORM));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRELEASEDATE() {
        return rELEASEDATE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRELEASEDATE(String newRELEASEDATE) {
        String oldRELEASEDATE = rELEASEDATE;
        rELEASEDATE = newRELEASEDATE;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__RELEASEDATE, oldRELEASEDATE, rELEASEDATE));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSCHEMAAUTOPROPAGATE() {
        return sCHEMAAUTOPROPAGATE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSCHEMAAUTOPROPAGATE(boolean newSCHEMAAUTOPROPAGATE) {
        boolean oldSCHEMAAUTOPROPAGATE = sCHEMAAUTOPROPAGATE;
        sCHEMAAUTOPROPAGATE = newSCHEMAAUTOPROPAGATE;
        boolean oldSCHEMAAUTOPROPAGATEESet = sCHEMAAUTOPROPAGATEESet;
        sCHEMAAUTOPROPAGATEESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__SCHEMAAUTOPROPAGATE, oldSCHEMAAUTOPROPAGATE, sCHEMAAUTOPROPAGATE, !oldSCHEMAAUTOPROPAGATEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetSCHEMAAUTOPROPAGATE() {
        boolean oldSCHEMAAUTOPROPAGATE = sCHEMAAUTOPROPAGATE;
        boolean oldSCHEMAAUTOPROPAGATEESet = sCHEMAAUTOPROPAGATEESet;
        sCHEMAAUTOPROPAGATE = SCHEMAAUTOPROPAGATE_EDEFAULT;
        sCHEMAAUTOPROPAGATEESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__SCHEMAAUTOPROPAGATE, oldSCHEMAAUTOPROPAGATE, SCHEMAAUTOPROPAGATE_EDEFAULT, oldSCHEMAAUTOPROPAGATEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetSCHEMAAUTOPROPAGATE() {
        return sCHEMAAUTOPROPAGATEESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSERIAL() {
        return sERIAL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSERIAL(String newSERIAL) {
        String oldSERIAL = sERIAL;
        sERIAL = newSERIAL;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__SERIAL, oldSERIAL, sERIAL));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSHORTNAME() {
        return sHORTNAME;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSHORTNAME(String newSHORTNAME) {
        String oldSHORTNAME = sHORTNAME;
        sHORTNAME = newSHORTNAME;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__SHORTNAME, oldSHORTNAME, sHORTNAME));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSINGLETON() {
        return sINGLETON;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSINGLETON(boolean newSINGLETON) {
        boolean oldSINGLETON = sINGLETON;
        sINGLETON = newSINGLETON;
        boolean oldSINGLETONESet = sINGLETONESet;
        sINGLETONESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__SINGLETON, oldSINGLETON, sINGLETON, !oldSINGLETONESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetSINGLETON() {
        boolean oldSINGLETON = sINGLETON;
        boolean oldSINGLETONESet = sINGLETONESet;
        sINGLETON = SINGLETON_EDEFAULT;
        sINGLETONESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__SINGLETON, oldSINGLETON, SINGLETON_EDEFAULT, oldSINGLETONESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetSINGLETON() {
        return sINGLETONESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSTARTABLE() {
        return sTARTABLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSTARTABLE(boolean newSTARTABLE) {
        boolean oldSTARTABLE = sTARTABLE;
        sTARTABLE = newSTARTABLE;
        boolean oldSTARTABLEESet = sTARTABLEESet;
        sTARTABLEESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__STARTABLE, oldSTARTABLE, sTARTABLE, !oldSTARTABLEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetSTARTABLE() {
        boolean oldSTARTABLE = sTARTABLE;
        boolean oldSTARTABLEESet = sTARTABLEESet;
        sTARTABLE = STARTABLE_EDEFAULT;
        sTARTABLEESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__STARTABLE, oldSTARTABLE, STARTABLE_EDEFAULT, oldSTARTABLEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetSTARTABLE() {
        return sTARTABLEESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSTATUS() {
        return sTATUS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSTATUS(String newSTATUS) {
        String oldSTATUS = sTATUS;
        sTATUS = newSTATUS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__STATUS, oldSTATUS, sTATUS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSUBJOBCOLOR() {
        return sUBJOBCOLOR;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSUBJOBCOLOR(String newSUBJOBCOLOR) {
        String oldSUBJOBCOLOR = sUBJOBCOLOR;
        sUBJOBCOLOR = newSUBJOBCOLOR;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__SUBJOBCOLOR, oldSUBJOBCOLOR, sUBJOBCOLOR));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSUBJOBTITLECOLOR() {
        return sUBJOBTITLECOLOR;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSUBJOBTITLECOLOR(String newSUBJOBTITLECOLOR) {
        String oldSUBJOBTITLECOLOR = sUBJOBTITLECOLOR;
        sUBJOBTITLECOLOR = newSUBJOBTITLECOLOR;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__SUBJOBTITLECOLOR, oldSUBJOBTITLECOLOR, sUBJOBTITLECOLOR));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isTECHNICAL() {
        return tECHNICAL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTECHNICAL(boolean newTECHNICAL) {
        boolean oldTECHNICAL = tECHNICAL;
        tECHNICAL = newTECHNICAL;
        boolean oldTECHNICALESet = tECHNICALESet;
        tECHNICALESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__TECHNICAL, oldTECHNICAL, tECHNICAL, !oldTECHNICALESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetTECHNICAL() {
        boolean oldTECHNICAL = tECHNICAL;
        boolean oldTECHNICALESet = tECHNICALESet;
        tECHNICAL = TECHNICAL_EDEFAULT;
        tECHNICALESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__TECHNICAL, oldTECHNICAL, TECHNICAL_EDEFAULT, oldTECHNICALESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetTECHNICAL() {
        return tECHNICALESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isTSTATCATCHERSTATS() {
        return tSTATCATCHERSTATS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTSTATCATCHERSTATS(boolean newTSTATCATCHERSTATS) {
        boolean oldTSTATCATCHERSTATS = tSTATCATCHERSTATS;
        tSTATCATCHERSTATS = newTSTATCATCHERSTATS;
        boolean oldTSTATCATCHERSTATSESet = tSTATCATCHERSTATSESet;
        tSTATCATCHERSTATSESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__TSTATCATCHERSTATS, oldTSTATCATCHERSTATS, tSTATCATCHERSTATS, !oldTSTATCATCHERSTATSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetTSTATCATCHERSTATS() {
        boolean oldTSTATCATCHERSTATS = tSTATCATCHERSTATS;
        boolean oldTSTATCATCHERSTATSESet = tSTATCATCHERSTATSESet;
        tSTATCATCHERSTATS = TSTATCATCHERSTATS_EDEFAULT;
        tSTATCATCHERSTATSESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__TSTATCATCHERSTATS, oldTSTATCATCHERSTATS, TSTATCATCHERSTATS_EDEFAULT, oldTSTATCATCHERSTATSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetTSTATCATCHERSTATS() {
        return tSTATCATCHERSTATSESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigDecimal getVERSION() {
        return vERSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVERSION(BigDecimal newVERSION) {
        BigDecimal oldVERSION = vERSION;
        vERSION = newVERSION;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__VERSION, oldVERSION, vERSION));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isVISIBLE() {
        return vISIBLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVISIBLE(boolean newVISIBLE) {
        boolean oldVISIBLE = vISIBLE;
        vISIBLE = newVISIBLE;
        boolean oldVISIBLEESet = vISIBLEESet;
        vISIBLEESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__VISIBLE, oldVISIBLE, vISIBLE, !oldVISIBLEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetVISIBLE() {
        boolean oldVISIBLE = vISIBLE;
        boolean oldVISIBLEESet = vISIBLEESet;
        vISIBLE = VISIBLE_EDEFAULT;
        vISIBLEESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__VISIBLE, oldVISIBLE, VISIBLE_EDEFAULT, oldVISIBLEESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetVISIBLE() {
        return vISIBLEESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEQUIVALENT() {
        return eQUIVALENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEQUIVALENT(String newEQUIVALENT) {
        String oldEQUIVALENT = eQUIVALENT;
        eQUIVALENT = newEQUIVALENT;
        boolean oldEQUIVALENTESet = eQUIVALENTESet;
        eQUIVALENTESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__EQUIVALENT, oldEQUIVALENT, eQUIVALENT, !oldEQUIVALENTESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetEQUIVALENT() {
        String oldEQUIVALENT = eQUIVALENT;
        boolean oldEQUIVALENTESet = eQUIVALENTESet;
        eQUIVALENT = EQUIVALENT_EDEFAULT;
        eQUIVALENTESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__EQUIVALENT, oldEQUIVALENT, EQUIVALENT_EDEFAULT, oldEQUIVALENTESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetEQUIVALENT() {
        return eQUIVALENTESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getINPUTTYPE() {
        return iNPUTTYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setINPUTTYPE(String newINPUTTYPE) {
        String oldINPUTTYPE = iNPUTTYPE;
        iNPUTTYPE = newINPUTTYPE;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__INPUTTYPE, oldINPUTTYPE, iNPUTTYPE));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOUTPUTTYPE() {
        return oUTPUTTYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOUTPUTTYPE(String newOUTPUTTYPE) {
        String oldOUTPUTTYPE = oUTPUTTYPE;
        oUTPUTTYPE = newOUTPUTTYPE;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__OUTPUTTYPE, oldOUTPUTTYPE, oUTPUTTYPE));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSPARKACTION() {
        return sPARKACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSPARKACTION(boolean newSPARKACTION) {
        boolean oldSPARKACTION = sPARKACTION;
        sPARKACTION = newSPARKACTION;
        boolean oldSPARKACTIONESet = sPARKACTIONESet;
        sPARKACTIONESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__SPARKACTION, oldSPARKACTION, sPARKACTION, !oldSPARKACTIONESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetSPARKACTION() {
        boolean oldSPARKACTION = sPARKACTION;
        boolean oldSPARKACTIONESet = sPARKACTIONESet;
        sPARKACTION = SPARKACTION_EDEFAULT;
        sPARKACTIONESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__SPARKACTION, oldSPARKACTION, SPARKACTION_EDEFAULT, oldSPARKACTIONESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetSPARKACTION() {
        return sPARKACTIONESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isACTIVE_DB_COLUMNS() {
        return aCTIVE_DB_COLUMNS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setACTIVE_DB_COLUMNS(boolean newACTIVE_DB_COLUMNS) {
        boolean oldACTIVE_DB_COLUMNS = aCTIVE_DB_COLUMNS;
        aCTIVE_DB_COLUMNS = newACTIVE_DB_COLUMNS;
        boolean oldACTIVE_DB_COLUMNSESet = aCTIVE_DB_COLUMNSESet;
        aCTIVE_DB_COLUMNSESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__ACTIVE_DB_COLUMNS, oldACTIVE_DB_COLUMNS, aCTIVE_DB_COLUMNS, !oldACTIVE_DB_COLUMNSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetACTIVE_DB_COLUMNS() {
        boolean oldACTIVE_DB_COLUMNS = aCTIVE_DB_COLUMNS;
        boolean oldACTIVE_DB_COLUMNSESet = aCTIVE_DB_COLUMNSESet;
        aCTIVE_DB_COLUMNS = ACTIVE_DB_COLUMNS_EDEFAULT;
        aCTIVE_DB_COLUMNSESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__ACTIVE_DB_COLUMNS, oldACTIVE_DB_COLUMNS, ACTIVE_DB_COLUMNS_EDEFAULT, oldACTIVE_DB_COLUMNSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetACTIVE_DB_COLUMNS() {
        return aCTIVE_DB_COLUMNSESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTYPE() {
        return tYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTYPE(String newTYPE) {
        String oldTYPE = tYPE;
        tYPE = newTYPE;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__TYPE, oldTYPE, tYPE));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isREDUCE() {
        return rEDUCE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setREDUCE(boolean newREDUCE) {
        boolean oldREDUCE = rEDUCE;
        rEDUCE = newREDUCE;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__REDUCE, oldREDUCE, rEDUCE));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPARTITIONING() {
        return pARTITIONING;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPARTITIONING(String newPARTITIONING) {
        String oldPARTITIONING = pARTITIONING;
        pARTITIONING = newPARTITIONING;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__PARTITIONING, oldPARTITIONING, pARTITIONING));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSUPPORTS_DB_TYPE() {
        return sUPPORTS_DB_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSUPPORTS_DB_TYPE(boolean newSUPPORTS_DB_TYPE) {
        boolean oldSUPPORTS_DB_TYPE = sUPPORTS_DB_TYPE;
        sUPPORTS_DB_TYPE = newSUPPORTS_DB_TYPE;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__SUPPORTS_DB_TYPE, oldSUPPORTS_DB_TYPE, sUPPORTS_DB_TYPE));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isLOG4J_ENABLED() {
        return lOG4J_ENABLED;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLOG4J_ENABLED(boolean newLOG4J_ENABLED) {
        boolean oldLOG4J_ENABLED = lOG4J_ENABLED;
        lOG4J_ENABLED = newLOG4J_ENABLED;
        boolean oldLOG4J_ENABLEDESet = lOG4J_ENABLEDESet;
        lOG4J_ENABLEDESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.HEADER_TYPE__LOG4J_ENABLED, oldLOG4J_ENABLED, lOG4J_ENABLED, !oldLOG4J_ENABLEDESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetLOG4J_ENABLED() {
        boolean oldLOG4J_ENABLED = lOG4J_ENABLED;
        boolean oldLOG4J_ENABLEDESet = lOG4J_ENABLEDESet;
        lOG4J_ENABLED = LOG4J_ENABLED_EDEFAULT;
        lOG4J_ENABLEDESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.HEADER_TYPE__LOG4J_ENABLED, oldLOG4J_ENABLED, LOG4J_ENABLED_EDEFAULT, oldLOG4J_ENABLEDESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetLOG4J_ENABLED() {
        return lOG4J_ENABLEDESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case ComponentPackage.HEADER_TYPE__FORMAT:
                return basicSetFORMAT(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ComponentPackage.HEADER_TYPE__SIGNATURE:
                return getSIGNATURE();
            case ComponentPackage.HEADER_TYPE__FORMAT:
                return getFORMAT();
            case ComponentPackage.HEADER_TYPE__AUTHOR:
                return getAUTHOR();
            case ComponentPackage.HEADER_TYPE__COMBINE:
                return getCOMBINE();
            case ComponentPackage.HEADER_TYPE__COMPATIBILITY:
                return getCOMPATIBILITY();
            case ComponentPackage.HEADER_TYPE__DATAAUTOPROPAGATE:
                return isDATAAUTOPROPAGATE() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__EXTENSION:
                return getEXTENSION();
            case ComponentPackage.HEADER_TYPE__HASCONDITIONALOUTPUTS:
                return isHASCONDITIONALOUTPUTS() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__HASHCOMPONENT:
                return isHASHCOMPONENT() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__ISMULTIPLYINGOUTPUTS:
                return isISMULTIPLYINGOUTPUTS() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__LOG4J_ENABLED:
                return isLOG4J_ENABLED() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__MAINCODECALLED:
                return isMAINCODECALLED() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__NUMBERPARALLELIZE:
                return getNUMBERPARALLELIZE();
            case ComponentPackage.HEADER_TYPE__PARALLELIZE:
                return isPARALLELIZE() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__PARTITIONING:
                return getPARTITIONING();
            case ComponentPackage.HEADER_TYPE__PLATEFORM:
                return getPLATEFORM();
            case ComponentPackage.HEADER_TYPE__REDUCE:
                return isREDUCE() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__RELEASEDATE:
                return getRELEASEDATE();
            case ComponentPackage.HEADER_TYPE__SCHEMAAUTOPROPAGATE:
                return isSCHEMAAUTOPROPAGATE() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__SERIAL:
                return getSERIAL();
            case ComponentPackage.HEADER_TYPE__SHORTNAME:
                return getSHORTNAME();
            case ComponentPackage.HEADER_TYPE__SINGLETON:
                return isSINGLETON() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__STARTABLE:
                return isSTARTABLE() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__STATUS:
                return getSTATUS();
            case ComponentPackage.HEADER_TYPE__SUBJOBCOLOR:
                return getSUBJOBCOLOR();
            case ComponentPackage.HEADER_TYPE__SUBJOBTITLECOLOR:
                return getSUBJOBTITLECOLOR();
            case ComponentPackage.HEADER_TYPE__SUPPORTS_DB_TYPE:
                return isSUPPORTS_DB_TYPE() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__TECHNICAL:
                return isTECHNICAL() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__TSTATCATCHERSTATS:
                return isTSTATCATCHERSTATS() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__TYPE:
                return getTYPE();
            case ComponentPackage.HEADER_TYPE__VERSION:
                return getVERSION();
            case ComponentPackage.HEADER_TYPE__VISIBLE:
                return isVISIBLE() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__EQUIVALENT:
                return getEQUIVALENT();
            case ComponentPackage.HEADER_TYPE__INPUTTYPE:
                return getINPUTTYPE();
            case ComponentPackage.HEADER_TYPE__OUTPUTTYPE:
                return getOUTPUTTYPE();
            case ComponentPackage.HEADER_TYPE__SPARKACTION:
                return isSPARKACTION() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.HEADER_TYPE__ACTIVE_DB_COLUMNS:
                return isACTIVE_DB_COLUMNS() ? Boolean.TRUE : Boolean.FALSE;
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case ComponentPackage.HEADER_TYPE__SIGNATURE:
                setSIGNATURE((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__FORMAT:
                setFORMAT((FORMATType)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__AUTHOR:
                setAUTHOR((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__COMBINE:
                setCOMBINE((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__COMPATIBILITY:
                setCOMPATIBILITY((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__DATAAUTOPROPAGATE:
                setDATAAUTOPROPAGATE(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__EXTENSION:
                setEXTENSION((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__HASCONDITIONALOUTPUTS:
                setHASCONDITIONALOUTPUTS(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__HASHCOMPONENT:
                setHASHCOMPONENT(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__ISMULTIPLYINGOUTPUTS:
                setISMULTIPLYINGOUTPUTS(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__LOG4J_ENABLED:
                setLOG4J_ENABLED(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__MAINCODECALLED:
                setMAINCODECALLED(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__NUMBERPARALLELIZE:
                setNUMBERPARALLELIZE((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__PARALLELIZE:
                setPARALLELIZE(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__PARTITIONING:
                setPARTITIONING((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__PLATEFORM:
                setPLATEFORM((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__REDUCE:
                setREDUCE(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__RELEASEDATE:
                setRELEASEDATE((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__SCHEMAAUTOPROPAGATE:
                setSCHEMAAUTOPROPAGATE(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__SERIAL:
                setSERIAL((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__SHORTNAME:
                setSHORTNAME((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__SINGLETON:
                setSINGLETON(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__STARTABLE:
                setSTARTABLE(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__STATUS:
                setSTATUS((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__SUBJOBCOLOR:
                setSUBJOBCOLOR((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__SUBJOBTITLECOLOR:
                setSUBJOBTITLECOLOR((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__SUPPORTS_DB_TYPE:
                setSUPPORTS_DB_TYPE(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__TECHNICAL:
                setTECHNICAL(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__TSTATCATCHERSTATS:
                setTSTATCATCHERSTATS(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__TYPE:
                setTYPE((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__VERSION:
                setVERSION((BigDecimal)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__VISIBLE:
                setVISIBLE(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__EQUIVALENT:
                setEQUIVALENT((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__INPUTTYPE:
                setINPUTTYPE((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__OUTPUTTYPE:
                setOUTPUTTYPE((String)newValue);
                return;
            case ComponentPackage.HEADER_TYPE__SPARKACTION:
                setSPARKACTION(((Boolean)newValue).booleanValue());
                return;
            case ComponentPackage.HEADER_TYPE__ACTIVE_DB_COLUMNS:
                setACTIVE_DB_COLUMNS(((Boolean)newValue).booleanValue());
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case ComponentPackage.HEADER_TYPE__SIGNATURE:
                setSIGNATURE(SIGNATURE_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__FORMAT:
                setFORMAT((FORMATType)null);
                return;
            case ComponentPackage.HEADER_TYPE__AUTHOR:
                setAUTHOR(AUTHOR_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__COMBINE:
                setCOMBINE(COMBINE_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__COMPATIBILITY:
                setCOMPATIBILITY(COMPATIBILITY_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__DATAAUTOPROPAGATE:
                unsetDATAAUTOPROPAGATE();
                return;
            case ComponentPackage.HEADER_TYPE__EXTENSION:
                setEXTENSION(EXTENSION_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__HASCONDITIONALOUTPUTS:
                unsetHASCONDITIONALOUTPUTS();
                return;
            case ComponentPackage.HEADER_TYPE__HASHCOMPONENT:
                unsetHASHCOMPONENT();
                return;
            case ComponentPackage.HEADER_TYPE__ISMULTIPLYINGOUTPUTS:
                unsetISMULTIPLYINGOUTPUTS();
                return;
            case ComponentPackage.HEADER_TYPE__LOG4J_ENABLED:
                unsetLOG4J_ENABLED();
                return;
            case ComponentPackage.HEADER_TYPE__MAINCODECALLED:
                unsetMAINCODECALLED();
                return;
            case ComponentPackage.HEADER_TYPE__NUMBERPARALLELIZE:
                unsetNUMBERPARALLELIZE();
                return;
            case ComponentPackage.HEADER_TYPE__PARALLELIZE:
                unsetPARALLELIZE();
                return;
            case ComponentPackage.HEADER_TYPE__PARTITIONING:
                setPARTITIONING(PARTITIONING_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__PLATEFORM:
                setPLATEFORM(PLATEFORM_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__REDUCE:
                setREDUCE(REDUCE_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__RELEASEDATE:
                setRELEASEDATE(RELEASEDATE_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__SCHEMAAUTOPROPAGATE:
                unsetSCHEMAAUTOPROPAGATE();
                return;
            case ComponentPackage.HEADER_TYPE__SERIAL:
                setSERIAL(SERIAL_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__SHORTNAME:
                setSHORTNAME(SHORTNAME_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__SINGLETON:
                unsetSINGLETON();
                return;
            case ComponentPackage.HEADER_TYPE__STARTABLE:
                unsetSTARTABLE();
                return;
            case ComponentPackage.HEADER_TYPE__STATUS:
                setSTATUS(STATUS_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__SUBJOBCOLOR:
                setSUBJOBCOLOR(SUBJOBCOLOR_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__SUBJOBTITLECOLOR:
                setSUBJOBTITLECOLOR(SUBJOBTITLECOLOR_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__SUPPORTS_DB_TYPE:
                setSUPPORTS_DB_TYPE(SUPPORTS_DB_TYPE_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__TECHNICAL:
                unsetTECHNICAL();
                return;
            case ComponentPackage.HEADER_TYPE__TSTATCATCHERSTATS:
                unsetTSTATCATCHERSTATS();
                return;
            case ComponentPackage.HEADER_TYPE__TYPE:
                setTYPE(TYPE_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__VERSION:
                setVERSION(VERSION_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__VISIBLE:
                unsetVISIBLE();
                return;
            case ComponentPackage.HEADER_TYPE__EQUIVALENT:
                unsetEQUIVALENT();
                return;
            case ComponentPackage.HEADER_TYPE__INPUTTYPE:
                setINPUTTYPE(INPUTTYPE_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__OUTPUTTYPE:
                setOUTPUTTYPE(OUTPUTTYPE_EDEFAULT);
                return;
            case ComponentPackage.HEADER_TYPE__SPARKACTION:
                unsetSPARKACTION();
                return;
            case ComponentPackage.HEADER_TYPE__ACTIVE_DB_COLUMNS:
                unsetACTIVE_DB_COLUMNS();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case ComponentPackage.HEADER_TYPE__SIGNATURE:
                return SIGNATURE_EDEFAULT == null ? sIGNATURE != null : !SIGNATURE_EDEFAULT.equals(sIGNATURE);
            case ComponentPackage.HEADER_TYPE__FORMAT:
                return fORMAT != null;
            case ComponentPackage.HEADER_TYPE__AUTHOR:
                return AUTHOR_EDEFAULT == null ? aUTHOR != null : !AUTHOR_EDEFAULT.equals(aUTHOR);
            case ComponentPackage.HEADER_TYPE__COMBINE:
                return COMBINE_EDEFAULT == null ? cOMBINE != null : !COMBINE_EDEFAULT.equals(cOMBINE);
            case ComponentPackage.HEADER_TYPE__COMPATIBILITY:
                return COMPATIBILITY_EDEFAULT == null ? cOMPATIBILITY != null : !COMPATIBILITY_EDEFAULT.equals(cOMPATIBILITY);
            case ComponentPackage.HEADER_TYPE__DATAAUTOPROPAGATE:
                return isSetDATAAUTOPROPAGATE();
            case ComponentPackage.HEADER_TYPE__EXTENSION:
                return EXTENSION_EDEFAULT == null ? eXTENSION != null : !EXTENSION_EDEFAULT.equals(eXTENSION);
            case ComponentPackage.HEADER_TYPE__HASCONDITIONALOUTPUTS:
                return isSetHASCONDITIONALOUTPUTS();
            case ComponentPackage.HEADER_TYPE__HASHCOMPONENT:
                return isSetHASHCOMPONENT();
            case ComponentPackage.HEADER_TYPE__ISMULTIPLYINGOUTPUTS:
                return isSetISMULTIPLYINGOUTPUTS();
            case ComponentPackage.HEADER_TYPE__LOG4J_ENABLED:
                return isSetLOG4J_ENABLED();
            case ComponentPackage.HEADER_TYPE__MAINCODECALLED:
                return isSetMAINCODECALLED();
            case ComponentPackage.HEADER_TYPE__NUMBERPARALLELIZE:
                return isSetNUMBERPARALLELIZE();
            case ComponentPackage.HEADER_TYPE__PARALLELIZE:
                return isSetPARALLELIZE();
            case ComponentPackage.HEADER_TYPE__PARTITIONING:
                return PARTITIONING_EDEFAULT == null ? pARTITIONING != null : !PARTITIONING_EDEFAULT.equals(pARTITIONING);
            case ComponentPackage.HEADER_TYPE__PLATEFORM:
                return PLATEFORM_EDEFAULT == null ? pLATEFORM != null : !PLATEFORM_EDEFAULT.equals(pLATEFORM);
            case ComponentPackage.HEADER_TYPE__REDUCE:
                return rEDUCE != REDUCE_EDEFAULT;
            case ComponentPackage.HEADER_TYPE__RELEASEDATE:
                return RELEASEDATE_EDEFAULT == null ? rELEASEDATE != null : !RELEASEDATE_EDEFAULT.equals(rELEASEDATE);
            case ComponentPackage.HEADER_TYPE__SCHEMAAUTOPROPAGATE:
                return isSetSCHEMAAUTOPROPAGATE();
            case ComponentPackage.HEADER_TYPE__SERIAL:
                return SERIAL_EDEFAULT == null ? sERIAL != null : !SERIAL_EDEFAULT.equals(sERIAL);
            case ComponentPackage.HEADER_TYPE__SHORTNAME:
                return SHORTNAME_EDEFAULT == null ? sHORTNAME != null : !SHORTNAME_EDEFAULT.equals(sHORTNAME);
            case ComponentPackage.HEADER_TYPE__SINGLETON:
                return isSetSINGLETON();
            case ComponentPackage.HEADER_TYPE__STARTABLE:
                return isSetSTARTABLE();
            case ComponentPackage.HEADER_TYPE__STATUS:
                return STATUS_EDEFAULT == null ? sTATUS != null : !STATUS_EDEFAULT.equals(sTATUS);
            case ComponentPackage.HEADER_TYPE__SUBJOBCOLOR:
                return SUBJOBCOLOR_EDEFAULT == null ? sUBJOBCOLOR != null : !SUBJOBCOLOR_EDEFAULT.equals(sUBJOBCOLOR);
            case ComponentPackage.HEADER_TYPE__SUBJOBTITLECOLOR:
                return SUBJOBTITLECOLOR_EDEFAULT == null ? sUBJOBTITLECOLOR != null : !SUBJOBTITLECOLOR_EDEFAULT.equals(sUBJOBTITLECOLOR);
            case ComponentPackage.HEADER_TYPE__SUPPORTS_DB_TYPE:
                return sUPPORTS_DB_TYPE != SUPPORTS_DB_TYPE_EDEFAULT;
            case ComponentPackage.HEADER_TYPE__TECHNICAL:
                return isSetTECHNICAL();
            case ComponentPackage.HEADER_TYPE__TSTATCATCHERSTATS:
                return isSetTSTATCATCHERSTATS();
            case ComponentPackage.HEADER_TYPE__TYPE:
                return TYPE_EDEFAULT == null ? tYPE != null : !TYPE_EDEFAULT.equals(tYPE);
            case ComponentPackage.HEADER_TYPE__VERSION:
                return VERSION_EDEFAULT == null ? vERSION != null : !VERSION_EDEFAULT.equals(vERSION);
            case ComponentPackage.HEADER_TYPE__VISIBLE:
                return isSetVISIBLE();
            case ComponentPackage.HEADER_TYPE__EQUIVALENT:
                return isSetEQUIVALENT();
            case ComponentPackage.HEADER_TYPE__INPUTTYPE:
                return INPUTTYPE_EDEFAULT == null ? iNPUTTYPE != null : !INPUTTYPE_EDEFAULT.equals(iNPUTTYPE);
            case ComponentPackage.HEADER_TYPE__OUTPUTTYPE:
                return OUTPUTTYPE_EDEFAULT == null ? oUTPUTTYPE != null : !OUTPUTTYPE_EDEFAULT.equals(oUTPUTTYPE);
            case ComponentPackage.HEADER_TYPE__SPARKACTION:
                return isSetSPARKACTION();
            case ComponentPackage.HEADER_TYPE__ACTIVE_DB_COLUMNS:
                return isSetACTIVE_DB_COLUMNS();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (sIGNATURE: ");
        result.append(sIGNATURE);
        result.append(", aUTHOR: ");
        result.append(aUTHOR);
        result.append(", cOMBINE: ");
        result.append(cOMBINE);
        result.append(", cOMPATIBILITY: ");
        result.append(cOMPATIBILITY);
        result.append(", dATAAUTOPROPAGATE: ");
        if (dATAAUTOPROPAGATEESet) result.append(dATAAUTOPROPAGATE); else result.append("<unset>");
        result.append(", eXTENSION: ");
        result.append(eXTENSION);
        result.append(", hASCONDITIONALOUTPUTS: ");
        if (hASCONDITIONALOUTPUTSESet) result.append(hASCONDITIONALOUTPUTS); else result.append("<unset>");
        result.append(", hASHCOMPONENT: ");
        if (hASHCOMPONENTESet) result.append(hASHCOMPONENT); else result.append("<unset>");
        result.append(", iSMULTIPLYINGOUTPUTS: ");
        if (iSMULTIPLYINGOUTPUTSESet) result.append(iSMULTIPLYINGOUTPUTS); else result.append("<unset>");
        result.append(", lOG4J_ENABLED: ");
        if (lOG4J_ENABLEDESet) result.append(lOG4J_ENABLED); else result.append("<unset>");
        result.append(", mAINCODECALLED: ");
        if (mAINCODECALLEDESet) result.append(mAINCODECALLED); else result.append("<unset>");
        result.append(", nUMBERPARALLELIZE: ");
        if (nUMBERPARALLELIZEESet) result.append(nUMBERPARALLELIZE); else result.append("<unset>");
        result.append(", pARALLELIZE: ");
        if (pARALLELIZEESet) result.append(pARALLELIZE); else result.append("<unset>");
        result.append(", pARTITIONING: ");
        result.append(pARTITIONING);
        result.append(", pLATEFORM: ");
        result.append(pLATEFORM);
        result.append(", rEDUCE: ");
        result.append(rEDUCE);
        result.append(", rELEASEDATE: ");
        result.append(rELEASEDATE);
        result.append(", sCHEMAAUTOPROPAGATE: ");
        if (sCHEMAAUTOPROPAGATEESet) result.append(sCHEMAAUTOPROPAGATE); else result.append("<unset>");
        result.append(", sERIAL: ");
        result.append(sERIAL);
        result.append(", sHORTNAME: ");
        result.append(sHORTNAME);
        result.append(", sINGLETON: ");
        if (sINGLETONESet) result.append(sINGLETON); else result.append("<unset>");
        result.append(", sTARTABLE: ");
        if (sTARTABLEESet) result.append(sTARTABLE); else result.append("<unset>");
        result.append(", sTATUS: ");
        result.append(sTATUS);
        result.append(", sUBJOBCOLOR: ");
        result.append(sUBJOBCOLOR);
        result.append(", sUBJOBTITLECOLOR: ");
        result.append(sUBJOBTITLECOLOR);
        result.append(", sUPPORTS_DB_TYPE: ");
        result.append(sUPPORTS_DB_TYPE);
        result.append(", tECHNICAL: ");
        if (tECHNICALESet) result.append(tECHNICAL); else result.append("<unset>");
        result.append(", tSTATCATCHERSTATS: ");
        if (tSTATCATCHERSTATSESet) result.append(tSTATCATCHERSTATS); else result.append("<unset>");
        result.append(", tYPE: ");
        result.append(tYPE);
        result.append(", vERSION: ");
        result.append(vERSION);
        result.append(", vISIBLE: ");
        if (vISIBLEESet) result.append(vISIBLE); else result.append("<unset>");
        result.append(", eQUIVALENT: ");
        if (eQUIVALENTESet) result.append(eQUIVALENT); else result.append("<unset>");
        result.append(", iNPUTTYPE: ");
        result.append(iNPUTTYPE);
        result.append(", oUTPUTTYPE: ");
        result.append(oUTPUTTYPE);
        result.append(", sPARKACTION: ");
        if (sPARKACTIONESet) result.append(sPARKACTION); else result.append("<unset>");
        result.append(", aCTIVE_DB_COLUMNS: ");
        if (aCTIVE_DB_COLUMNSESet) result.append(aCTIVE_DB_COLUMNS); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //HEADERTypeImpl
