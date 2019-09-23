/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.FileFormat;
import org.talend.core.model.metadata.builder.connection.RowSeparator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>File Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getServer <em>Server</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getFilePath <em>File Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getFieldSeparatorValue <em>Field Separator Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getRowSeparatorType <em>Row Separator Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getRowSeparatorValue <em>Row Separator Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getTextIdentifier <em>Text Identifier</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#isUseHeader <em>Use Header</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getHeaderValue <em>Header Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#isUseFooter <em>Use Footer</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getFooterValue <em>Footer Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#isUseLimit <em>Use Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getLimitValue <em>Limit Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#isFirstLineCaption <em>First Line Caption</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#isRemoveEmptyRow <em>Remove Empty Row</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getEscapeType <em>Escape Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getEscapeChar <em>Escape Char</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#getTextEnclosure <em>Text Enclosure</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileConnectionImpl#isCsvOption <em>Csv Option</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class FileConnectionImpl extends ConnectionImpl implements FileConnection {

    /**
     * The default value of the '{@link #getServer() <em>Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServer()
     * @generated
     * @ordered
     */
    protected static final String SERVER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getServer() <em>Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServer()
     * @generated
     * @ordered
     */
    protected String server = SERVER_EDEFAULT;

    /**
     * The default value of the '{@link #getFilePath() <em>File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilePath()
     * @generated
     * @ordered
     */
    protected static final String FILE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFilePath() <em>File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilePath()
     * @generated
     * @ordered
     */
    protected String filePath = FILE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected static final FileFormat FORMAT_EDEFAULT = FileFormat.UNIX_LITERAL;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected FileFormat format = FORMAT_EDEFAULT;

    /**
     * The default value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEncoding()
     * @generated
     * @ordered
     */
    protected static final String ENCODING_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEncoding()
     * @generated
     * @ordered
     */
    protected String encoding = ENCODING_EDEFAULT;

    /**
     * The default value of the '{@link #getFieldSeparatorValue() <em>Field Separator Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFieldSeparatorValue()
     * @generated
     * @ordered
     */
    protected static final String FIELD_SEPARATOR_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFieldSeparatorValue() <em>Field Separator Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFieldSeparatorValue()
     * @generated
     * @ordered
     */
    protected String fieldSeparatorValue = FIELD_SEPARATOR_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getRowSeparatorType() <em>Row Separator Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRowSeparatorType()
     * @ordered
     */
    protected static final RowSeparator ROW_SEPARATOR_TYPE_EDEFAULT = RowSeparator.STANDART_EOL_LITERAL;

    /**
     * The cached value of the '{@link #getRowSeparatorType() <em>Row Separator Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRowSeparatorType()
     * @generated
     * @ordered
     */
    protected RowSeparator rowSeparatorType = ROW_SEPARATOR_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getRowSeparatorValue() <em>Row Separator Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRowSeparatorValue()
     * @generated
     * @ordered
     */
    protected static final String ROW_SEPARATOR_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRowSeparatorValue() <em>Row Separator Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRowSeparatorValue()
     * @generated
     * @ordered
     */
    protected String rowSeparatorValue = ROW_SEPARATOR_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getTextIdentifier() <em>Text Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextIdentifier()
     * @generated
     * @ordered
     */
    protected static final String TEXT_IDENTIFIER_EDEFAULT = ""; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getTextIdentifier() <em>Text Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextIdentifier()
     * @generated
     * @ordered
     */
    protected String textIdentifier = TEXT_IDENTIFIER_EDEFAULT;

    /**
     * The default value of the '{@link #isUseHeader() <em>Use Header</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseHeader()
     * @generated
     * @ordered
     */
    protected static final boolean USE_HEADER_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseHeader() <em>Use Header</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseHeader()
     * @generated
     * @ordered
     */
    protected boolean useHeader = USE_HEADER_EDEFAULT;

    /**
     * The default value of the '{@link #getHeaderValue() <em>Header Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHeaderValue()
     * @generated
     * @ordered
     */
    protected static final String HEADER_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHeaderValue() <em>Header Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHeaderValue()
     * @generated
     * @ordered
     */
    protected String headerValue = HEADER_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #isUseFooter() <em>Use Footer</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseFooter()
     * @generated
     * @ordered
     */
    protected static final boolean USE_FOOTER_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseFooter() <em>Use Footer</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseFooter()
     * @generated
     * @ordered
     */
    protected boolean useFooter = USE_FOOTER_EDEFAULT;

    /**
     * The default value of the '{@link #getFooterValue() <em>Footer Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFooterValue()
     * @generated
     * @ordered
     */
    protected static final String FOOTER_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFooterValue() <em>Footer Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFooterValue()
     * @generated
     * @ordered
     */
    protected String footerValue = FOOTER_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #isUseLimit() <em>Use Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseLimit()
     * @generated
     * @ordered
     */
    protected static final boolean USE_LIMIT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseLimit() <em>Use Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseLimit()
     * @generated
     * @ordered
     */
    protected boolean useLimit = USE_LIMIT_EDEFAULT;

    /**
     * The default value of the '{@link #getLimitValue() <em>Limit Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLimitValue()
     * @generated
     * @ordered
     */
    protected static final String LIMIT_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLimitValue() <em>Limit Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLimitValue()
     * @generated
     * @ordered
     */
    protected String limitValue = LIMIT_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #isFirstLineCaption() <em>First Line Caption</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isFirstLineCaption()
     * @generated
     * @ordered
     */
    protected static final boolean FIRST_LINE_CAPTION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isFirstLineCaption() <em>First Line Caption</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isFirstLineCaption()
     * @generated
     * @ordered
     */
    protected boolean firstLineCaption = FIRST_LINE_CAPTION_EDEFAULT;

    /**
     * The default value of the '{@link #isRemoveEmptyRow() <em>Remove Empty Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isRemoveEmptyRow()
     * @generated
     * @ordered
     */
    protected static final boolean REMOVE_EMPTY_ROW_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isRemoveEmptyRow() <em>Remove Empty Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isRemoveEmptyRow()
     * @generated
     * @ordered
     */
    protected boolean removeEmptyRow = REMOVE_EMPTY_ROW_EDEFAULT;

    /**
     * The default value of the '{@link #getEscapeType() <em>Escape Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEscapeType()
     * @generated
     * @ordered
     */
    protected static final Escape ESCAPE_TYPE_EDEFAULT = Escape.DELIMITED;

    /**
     * The cached value of the '{@link #getEscapeType() <em>Escape Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEscapeType()
     * @generated
     * @ordered
     */
    protected Escape escapeType = ESCAPE_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getEscapeChar() <em>Escape Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEscapeChar()
     * @generated
     * @ordered
     */
    protected static final String ESCAPE_CHAR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEscapeChar() <em>Escape Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEscapeChar()
     * @generated
     * @ordered
     */
    protected String escapeChar = ESCAPE_CHAR_EDEFAULT;

    /**
     * The default value of the '{@link #getTextEnclosure() <em>Text Enclosure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextEnclosure()
     * @generated
     * @ordered
     */
    protected static final String TEXT_ENCLOSURE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTextEnclosure() <em>Text Enclosure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextEnclosure()
     * @generated
     * @ordered
     */
    protected String textEnclosure = TEXT_ENCLOSURE_EDEFAULT;

    /**
     * The default value of the '{@link #isCsvOption() <em>Csv Option</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isCsvOption()
     * @generated
     * @ordered
     */
    protected static final boolean CSV_OPTION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isCsvOption() <em>Csv Option</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isCsvOption()
     * @generated
     * @ordered
     */
    protected boolean csvOption = CSV_OPTION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FileConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.FILE_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getServer() {
        return server;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServer(String newServer) {
        String oldServer = server;
        server = newServer;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__SERVER, oldServer, server));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilePath(String newFilePath) {
        String oldFilePath = filePath;
        filePath = newFilePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__FILE_PATH, oldFilePath,
                    filePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FileFormat getFormat() {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat(FileFormat newFormat) {
        FileFormat oldFormat = format;
        format = newFormat == null ? FORMAT_EDEFAULT : newFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__FORMAT, oldFormat, format));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEncoding(String newEncoding) {
        String oldEncoding = encoding;
        encoding = newEncoding;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__ENCODING, oldEncoding,
                    encoding));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFieldSeparatorValue() {
        return fieldSeparatorValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFieldSeparatorValue(String newFieldSeparatorValue) {
        String oldFieldSeparatorValue = fieldSeparatorValue;
        fieldSeparatorValue = newFieldSeparatorValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__FIELD_SEPARATOR_VALUE,
                    oldFieldSeparatorValue, fieldSeparatorValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RowSeparator getRowSeparatorType() {
        return rowSeparatorType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRowSeparatorType(RowSeparator newRowSeparatorType) {
        RowSeparator oldRowSeparatorType = rowSeparatorType;
        rowSeparatorType = newRowSeparatorType == null ? ROW_SEPARATOR_TYPE_EDEFAULT : newRowSeparatorType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_TYPE,
                    oldRowSeparatorType, rowSeparatorType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRowSeparatorValue() {
        return rowSeparatorValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRowSeparatorValue(String newRowSeparatorValue) {
        String oldRowSeparatorValue = rowSeparatorValue;
        rowSeparatorValue = newRowSeparatorValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_VALUE,
                    oldRowSeparatorValue, rowSeparatorValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTextIdentifier() {
        return textIdentifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTextIdentifier(String newTextIdentifier) {
        String oldTextIdentifier = textIdentifier;
        textIdentifier = newTextIdentifier;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__TEXT_IDENTIFIER,
                    oldTextIdentifier, textIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseHeader() {
        return useHeader;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseHeader(boolean newUseHeader) {
        boolean oldUseHeader = useHeader;
        useHeader = newUseHeader;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__USE_HEADER, oldUseHeader,
                    useHeader));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHeaderValue() {
        return headerValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHeaderValue(String newHeaderValue) {
        String oldHeaderValue = headerValue;
        headerValue = newHeaderValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__HEADER_VALUE,
                    oldHeaderValue, headerValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseFooter() {
        return useFooter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseFooter(boolean newUseFooter) {
        boolean oldUseFooter = useFooter;
        useFooter = newUseFooter;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__USE_FOOTER, oldUseFooter,
                    useFooter));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFooterValue() {
        return footerValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFooterValue(String newFooterValue) {
        String oldFooterValue = footerValue;
        footerValue = newFooterValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__FOOTER_VALUE,
                    oldFooterValue, footerValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseLimit() {
        return useLimit;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseLimit(boolean newUseLimit) {
        boolean oldUseLimit = useLimit;
        useLimit = newUseLimit;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__USE_LIMIT, oldUseLimit,
                    useLimit));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLimitValue() {
        return limitValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLimitValue(String newLimitValue) {
        String oldLimitValue = limitValue;
        limitValue = newLimitValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__LIMIT_VALUE, oldLimitValue,
                    limitValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isFirstLineCaption() {
        return firstLineCaption;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFirstLineCaption(boolean newFirstLineCaption) {
        boolean oldFirstLineCaption = firstLineCaption;
        firstLineCaption = newFirstLineCaption;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__FIRST_LINE_CAPTION,
                    oldFirstLineCaption, firstLineCaption));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isRemoveEmptyRow() {
        return removeEmptyRow;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRemoveEmptyRow(boolean newRemoveEmptyRow) {
        boolean oldRemoveEmptyRow = removeEmptyRow;
        removeEmptyRow = newRemoveEmptyRow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__REMOVE_EMPTY_ROW,
                    oldRemoveEmptyRow, removeEmptyRow));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Escape getEscapeType() {
        return escapeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEscapeType(Escape newEscapeType) {
        Escape oldEscapeType = escapeType;
        escapeType = newEscapeType == null ? ESCAPE_TYPE_EDEFAULT : newEscapeType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__ESCAPE_TYPE, oldEscapeType,
                    escapeType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEscapeChar() {
        return escapeChar;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEscapeChar(String newEscapeChar) {
        String oldEscapeChar = escapeChar;
        escapeChar = newEscapeChar;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__ESCAPE_CHAR, oldEscapeChar,
                    escapeChar));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTextEnclosure() {
        return textEnclosure;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTextEnclosure(String newTextEnclosure) {
        String oldTextEnclosure = textEnclosure;
        textEnclosure = newTextEnclosure;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__TEXT_ENCLOSURE,
                    oldTextEnclosure, textEnclosure));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isCsvOption() {
        return csvOption;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCsvOption(boolean newCsvOption) {
        boolean oldCsvOption = csvOption;
        csvOption = newCsvOption;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_CONNECTION__CSV_OPTION, oldCsvOption,
                    csvOption));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.FILE_CONNECTION__SERVER:
            return getServer();
        case ConnectionPackage.FILE_CONNECTION__FILE_PATH:
            return getFilePath();
        case ConnectionPackage.FILE_CONNECTION__FORMAT:
            return getFormat();
        case ConnectionPackage.FILE_CONNECTION__ENCODING:
            return getEncoding();
        case ConnectionPackage.FILE_CONNECTION__FIELD_SEPARATOR_VALUE:
            return getFieldSeparatorValue();
        case ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_TYPE:
            return getRowSeparatorType();
        case ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_VALUE:
            return getRowSeparatorValue();
        case ConnectionPackage.FILE_CONNECTION__TEXT_IDENTIFIER:
            return getTextIdentifier();
        case ConnectionPackage.FILE_CONNECTION__USE_HEADER:
            return isUseHeader();
        case ConnectionPackage.FILE_CONNECTION__HEADER_VALUE:
            return getHeaderValue();
        case ConnectionPackage.FILE_CONNECTION__USE_FOOTER:
            return isUseFooter();
        case ConnectionPackage.FILE_CONNECTION__FOOTER_VALUE:
            return getFooterValue();
        case ConnectionPackage.FILE_CONNECTION__USE_LIMIT:
            return isUseLimit();
        case ConnectionPackage.FILE_CONNECTION__LIMIT_VALUE:
            return getLimitValue();
        case ConnectionPackage.FILE_CONNECTION__FIRST_LINE_CAPTION:
            return isFirstLineCaption();
        case ConnectionPackage.FILE_CONNECTION__REMOVE_EMPTY_ROW:
            return isRemoveEmptyRow();
        case ConnectionPackage.FILE_CONNECTION__ESCAPE_TYPE:
            return getEscapeType();
        case ConnectionPackage.FILE_CONNECTION__ESCAPE_CHAR:
            return getEscapeChar();
        case ConnectionPackage.FILE_CONNECTION__TEXT_ENCLOSURE:
            return getTextEnclosure();
        case ConnectionPackage.FILE_CONNECTION__CSV_OPTION:
            return isCsvOption();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.FILE_CONNECTION__SERVER:
            setServer((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__FILE_PATH:
            setFilePath((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__FORMAT:
            setFormat((FileFormat) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__ENCODING:
            setEncoding((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__FIELD_SEPARATOR_VALUE:
            setFieldSeparatorValue((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_TYPE:
            setRowSeparatorType((RowSeparator) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_VALUE:
            setRowSeparatorValue((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__TEXT_IDENTIFIER:
            setTextIdentifier((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__USE_HEADER:
            setUseHeader((Boolean) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__HEADER_VALUE:
            setHeaderValue((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__USE_FOOTER:
            setUseFooter((Boolean) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__FOOTER_VALUE:
            setFooterValue((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__USE_LIMIT:
            setUseLimit((Boolean) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__LIMIT_VALUE:
            setLimitValue((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__FIRST_LINE_CAPTION:
            setFirstLineCaption((Boolean) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__REMOVE_EMPTY_ROW:
            setRemoveEmptyRow((Boolean) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__ESCAPE_TYPE:
            setEscapeType((Escape) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__ESCAPE_CHAR:
            setEscapeChar((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__TEXT_ENCLOSURE:
            setTextEnclosure((String) newValue);
            return;
        case ConnectionPackage.FILE_CONNECTION__CSV_OPTION:
            setCsvOption((Boolean) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case ConnectionPackage.FILE_CONNECTION__SERVER:
            setServer(SERVER_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__FILE_PATH:
            setFilePath(FILE_PATH_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__FORMAT:
            setFormat(FORMAT_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__ENCODING:
            setEncoding(ENCODING_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__FIELD_SEPARATOR_VALUE:
            setFieldSeparatorValue(FIELD_SEPARATOR_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_TYPE:
            setRowSeparatorType(ROW_SEPARATOR_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_VALUE:
            setRowSeparatorValue(ROW_SEPARATOR_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__TEXT_IDENTIFIER:
            setTextIdentifier(TEXT_IDENTIFIER_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__USE_HEADER:
            setUseHeader(USE_HEADER_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__HEADER_VALUE:
            setHeaderValue(HEADER_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__USE_FOOTER:
            setUseFooter(USE_FOOTER_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__FOOTER_VALUE:
            setFooterValue(FOOTER_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__USE_LIMIT:
            setUseLimit(USE_LIMIT_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__LIMIT_VALUE:
            setLimitValue(LIMIT_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__FIRST_LINE_CAPTION:
            setFirstLineCaption(FIRST_LINE_CAPTION_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__REMOVE_EMPTY_ROW:
            setRemoveEmptyRow(REMOVE_EMPTY_ROW_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__ESCAPE_TYPE:
            setEscapeType(ESCAPE_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__ESCAPE_CHAR:
            setEscapeChar(ESCAPE_CHAR_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__TEXT_ENCLOSURE:
            setTextEnclosure(TEXT_ENCLOSURE_EDEFAULT);
            return;
        case ConnectionPackage.FILE_CONNECTION__CSV_OPTION:
            setCsvOption(CSV_OPTION_EDEFAULT);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case ConnectionPackage.FILE_CONNECTION__SERVER:
            return SERVER_EDEFAULT == null ? server != null : !SERVER_EDEFAULT.equals(server);
        case ConnectionPackage.FILE_CONNECTION__FILE_PATH:
            return FILE_PATH_EDEFAULT == null ? filePath != null : !FILE_PATH_EDEFAULT.equals(filePath);
        case ConnectionPackage.FILE_CONNECTION__FORMAT:
            return format != FORMAT_EDEFAULT;
        case ConnectionPackage.FILE_CONNECTION__ENCODING:
            return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
        case ConnectionPackage.FILE_CONNECTION__FIELD_SEPARATOR_VALUE:
            return FIELD_SEPARATOR_VALUE_EDEFAULT == null ? fieldSeparatorValue != null : !FIELD_SEPARATOR_VALUE_EDEFAULT
                    .equals(fieldSeparatorValue);
        case ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_TYPE:
            return rowSeparatorType != ROW_SEPARATOR_TYPE_EDEFAULT;
        case ConnectionPackage.FILE_CONNECTION__ROW_SEPARATOR_VALUE:
            return ROW_SEPARATOR_VALUE_EDEFAULT == null ? rowSeparatorValue != null : !ROW_SEPARATOR_VALUE_EDEFAULT
                    .equals(rowSeparatorValue);
        case ConnectionPackage.FILE_CONNECTION__TEXT_IDENTIFIER:
            return TEXT_IDENTIFIER_EDEFAULT == null ? textIdentifier != null : !TEXT_IDENTIFIER_EDEFAULT.equals(textIdentifier);
        case ConnectionPackage.FILE_CONNECTION__USE_HEADER:
            return useHeader != USE_HEADER_EDEFAULT;
        case ConnectionPackage.FILE_CONNECTION__HEADER_VALUE:
            return HEADER_VALUE_EDEFAULT == null ? headerValue != null : !HEADER_VALUE_EDEFAULT.equals(headerValue);
        case ConnectionPackage.FILE_CONNECTION__USE_FOOTER:
            return useFooter != USE_FOOTER_EDEFAULT;
        case ConnectionPackage.FILE_CONNECTION__FOOTER_VALUE:
            return FOOTER_VALUE_EDEFAULT == null ? footerValue != null : !FOOTER_VALUE_EDEFAULT.equals(footerValue);
        case ConnectionPackage.FILE_CONNECTION__USE_LIMIT:
            return useLimit != USE_LIMIT_EDEFAULT;
        case ConnectionPackage.FILE_CONNECTION__LIMIT_VALUE:
            return LIMIT_VALUE_EDEFAULT == null ? limitValue != null : !LIMIT_VALUE_EDEFAULT.equals(limitValue);
        case ConnectionPackage.FILE_CONNECTION__FIRST_LINE_CAPTION:
            return firstLineCaption != FIRST_LINE_CAPTION_EDEFAULT;
        case ConnectionPackage.FILE_CONNECTION__REMOVE_EMPTY_ROW:
            return removeEmptyRow != REMOVE_EMPTY_ROW_EDEFAULT;
        case ConnectionPackage.FILE_CONNECTION__ESCAPE_TYPE:
            return escapeType != ESCAPE_TYPE_EDEFAULT;
        case ConnectionPackage.FILE_CONNECTION__ESCAPE_CHAR:
            return ESCAPE_CHAR_EDEFAULT == null ? escapeChar != null : !ESCAPE_CHAR_EDEFAULT.equals(escapeChar);
        case ConnectionPackage.FILE_CONNECTION__TEXT_ENCLOSURE:
            return TEXT_ENCLOSURE_EDEFAULT == null ? textEnclosure != null : !TEXT_ENCLOSURE_EDEFAULT.equals(textEnclosure);
        case ConnectionPackage.FILE_CONNECTION__CSV_OPTION:
            return csvOption != CSV_OPTION_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (Server: ");
        result.append(server);
        result.append(", FilePath: ");
        result.append(filePath);
        result.append(", Format: ");
        result.append(format);
        result.append(", Encoding: ");
        result.append(encoding);
        result.append(", FieldSeparatorValue: ");
        result.append(fieldSeparatorValue);
        result.append(", RowSeparatorType: ");
        result.append(rowSeparatorType);
        result.append(", RowSeparatorValue: ");
        result.append(rowSeparatorValue);
        result.append(", TextIdentifier: ");
        result.append(textIdentifier);
        result.append(", UseHeader: ");
        result.append(useHeader);
        result.append(", HeaderValue: ");
        result.append(headerValue);
        result.append(", UseFooter: ");
        result.append(useFooter);
        result.append(", FooterValue: ");
        result.append(footerValue);
        result.append(", UseLimit: ");
        result.append(useLimit);
        result.append(", LimitValue: ");
        result.append(limitValue);
        result.append(", FirstLineCaption: ");
        result.append(firstLineCaption);
        result.append(", RemoveEmptyRow: ");
        result.append(removeEmptyRow);
        result.append(", EscapeType: ");
        result.append(escapeType);
        result.append(", EscapeChar: ");
        result.append(escapeChar);
        result.append(", TextEnclosure: ");
        result.append(textEnclosure);
        result.append(", CsvOption: ");
        result.append(csvOption);
        result.append(')');
        return result.toString();
    }

} //FileConnectionImpl
