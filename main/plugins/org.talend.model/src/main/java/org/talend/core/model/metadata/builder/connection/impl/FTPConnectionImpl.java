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
import org.talend.core.model.metadata.builder.connection.FTPConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>FTP Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getHost <em>Host</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getPort <em>Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getUsername <em>Username</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getPassword <em>Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getEcoding <em>Ecoding</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#isSFTP <em>SFTP</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#isFTPS <em>FTPS</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getMethod <em>Method</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getPrivatekey <em>Privatekey</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getPassphrase <em>Passphrase</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getKeystoreFile <em>Keystore File</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getKeystorePassword <em>Keystore Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#isUsesocks <em>Usesocks</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getProxyhost <em>Proxyhost</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getProxyport <em>Proxyport</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getProxyuser <em>Proxyuser</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getProxypassword <em>Proxypassword</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FTPConnectionImpl#getCustomEncode <em>Custom Encode</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FTPConnectionImpl extends ConnectionImpl implements FTPConnection {

    /**
     * The default value of the '{@link #getHost() <em>Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHost()
     * @generated
     * @ordered
     */
    protected static final String HOST_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHost() <em>Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHost()
     * @generated
     * @ordered
     */
    protected String host = HOST_EDEFAULT;

    /**
     * The default value of the '{@link #getPort() <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPort()
     * @generated
     * @ordered
     */
    protected static final String PORT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPort() <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPort()
     * @generated
     * @ordered
     */
    protected String port = PORT_EDEFAULT;

    /**
     * The default value of the '{@link #getUsername() <em>Username</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsername()
     * @generated
     * @ordered
     */
    protected static final String USERNAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUsername() <em>Username</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsername()
     * @generated
     * @ordered
     */
    protected String username = USERNAME_EDEFAULT;

    /**
     * The default value of the '{@link #getPassword() <em>Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPassword()
     * @generated
     * @ordered
     */
    protected static final String PASSWORD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPassword() <em>Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPassword()
     * @generated
     * @ordered
     */
    protected String password = PASSWORD_EDEFAULT;

    /**
     * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMode()
     * @generated
     * @ordered
     */
    protected static final String MODE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMode()
     * @generated
     * @ordered
     */
    protected String mode = MODE_EDEFAULT;

    /**
     * The default value of the '{@link #getEcoding() <em>Ecoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEcoding()
     * @generated
     * @ordered
     */
    protected static final String ECODING_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEcoding() <em>Ecoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEcoding()
     * @generated
     * @ordered
     */
    protected String ecoding = ECODING_EDEFAULT;

    /**
     * The default value of the '{@link #isSFTP() <em>SFTP</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSFTP()
     * @generated
     * @ordered
     */
    protected static final boolean SFTP_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSFTP() <em>SFTP</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSFTP()
     * @generated
     * @ordered
     */
    protected boolean sftp = SFTP_EDEFAULT;

    /**
     * The default value of the '{@link #isFTPS() <em>FTPS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isFTPS()
     * @generated
     * @ordered
     */
    protected static final boolean FTPS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isFTPS() <em>FTPS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isFTPS()
     * @generated
     * @ordered
     */
    protected boolean ftps = FTPS_EDEFAULT;

    /**
     * The default value of the '{@link #getMethod() <em>Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethod()
     * @generated
     * @ordered
     */
    protected static final String METHOD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMethod() <em>Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethod()
     * @generated
     * @ordered
     */
    protected String method = METHOD_EDEFAULT;

    /**
     * The default value of the '{@link #getPrivatekey() <em>Privatekey</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPrivatekey()
     * @generated
     * @ordered
     */
    protected static final String PRIVATEKEY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPrivatekey() <em>Privatekey</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPrivatekey()
     * @generated
     * @ordered
     */
    protected String privatekey = PRIVATEKEY_EDEFAULT;

    /**
     * The default value of the '{@link #getPassphrase() <em>Passphrase</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPassphrase()
     * @generated
     * @ordered
     */
    protected static final String PASSPHRASE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPassphrase() <em>Passphrase</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPassphrase()
     * @generated
     * @ordered
     */
    protected String passphrase = PASSPHRASE_EDEFAULT;

    /**
     * The default value of the '{@link #getKeystoreFile() <em>Keystore File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeystoreFile()
     * @generated
     * @ordered
     */
    protected static final String KEYSTORE_FILE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getKeystoreFile() <em>Keystore File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeystoreFile()
     * @generated
     * @ordered
     */
    protected String keystoreFile = KEYSTORE_FILE_EDEFAULT;

    /**
     * The default value of the '{@link #getKeystorePassword() <em>Keystore Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeystorePassword()
     * @generated
     * @ordered
     */
    protected static final String KEYSTORE_PASSWORD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getKeystorePassword() <em>Keystore Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeystorePassword()
     * @generated
     * @ordered
     */
    protected String keystorePassword = KEYSTORE_PASSWORD_EDEFAULT;

    /**
     * The default value of the '{@link #isUsesocks() <em>Usesocks</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUsesocks()
     * @generated
     * @ordered
     */
    protected static final boolean USESOCKS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUsesocks() <em>Usesocks</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUsesocks()
     * @generated
     * @ordered
     */
    protected boolean usesocks = USESOCKS_EDEFAULT;

    /**
     * The default value of the '{@link #getProxyhost() <em>Proxyhost</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyhost()
     * @generated
     * @ordered
     */
    protected static final String PROXYHOST_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProxyhost() <em>Proxyhost</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyhost()
     * @generated
     * @ordered
     */
    protected String proxyhost = PROXYHOST_EDEFAULT;

    /**
     * The default value of the '{@link #getProxyport() <em>Proxyport</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyport()
     * @generated
     * @ordered
     */
    protected static final String PROXYPORT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProxyport() <em>Proxyport</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyport()
     * @generated
     * @ordered
     */
    protected String proxyport = PROXYPORT_EDEFAULT;

    /**
     * The default value of the '{@link #getProxyuser() <em>Proxyuser</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyuser()
     * @generated
     * @ordered
     */
    protected static final String PROXYUSER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProxyuser() <em>Proxyuser</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxyuser()
     * @generated
     * @ordered
     */
    protected String proxyuser = PROXYUSER_EDEFAULT;

    /**
     * The default value of the '{@link #getProxypassword() <em>Proxypassword</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxypassword()
     * @generated
     * @ordered
     */
    protected static final String PROXYPASSWORD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProxypassword() <em>Proxypassword</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProxypassword()
     * @generated
     * @ordered
     */
    protected String proxypassword = PROXYPASSWORD_EDEFAULT;

    /**
     * The default value of the '{@link #getCustomEncode() <em>Custom Encode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCustomEncode()
     * @generated
     * @ordered
     */
    protected static final String CUSTOM_ENCODE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCustomEncode() <em>Custom Encode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCustomEncode()
     * @generated
     * @ordered
     */
    protected String customEncode = CUSTOM_ENCODE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FTPConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.FTP_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHost() {
        return host;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHost(String newHost) {
        String oldHost = host;
        host = newHost;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__HOST, oldHost, host));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPort() {
        return port;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPort(String newPort) {
        String oldPort = port;
        port = newPort;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__PORT, oldPort, port));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUsername() {
        return username;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsername(String newUsername) {
        String oldUsername = username;
        username = newUsername;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__USERNAME, oldUsername,
                    username));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPassword() {
        return password;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPassword(String newPassword) {
        String oldPassword = password;
        password = newPassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__PASSWORD, oldPassword,
                    password));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMode() {
        return mode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMode(String newMode) {
        String oldMode = mode;
        mode = newMode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__MODE, oldMode, mode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEcoding() {
        return ecoding;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEcoding(String newEcoding) {
        String oldEcoding = ecoding;
        ecoding = newEcoding;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__ECODING, oldEcoding, ecoding));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSFTP() {
        return sftp;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSFTP(boolean newSFTP) {
        boolean oldSFTP = sftp;
        sftp = newSFTP;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__SFTP, oldSFTP, sftp));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isFTPS() {
        return ftps;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFTPS(boolean newFTPS) {
        boolean oldFTPS = ftps;
        ftps = newFTPS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__FTPS, oldFTPS, ftps));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMethod() {
        return method;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMethod(String newMethod) {
        String oldMethod = method;
        method = newMethod;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__METHOD, oldMethod, method));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPrivatekey() {
        return privatekey;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPrivatekey(String newPrivatekey) {
        String oldPrivatekey = privatekey;
        privatekey = newPrivatekey;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__PRIVATEKEY, oldPrivatekey,
                    privatekey));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPassphrase(String newPassphrase) {
        String oldPassphrase = passphrase;
        passphrase = newPassphrase;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__PASSPHRASE, oldPassphrase,
                    passphrase));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getKeystoreFile() {
        return keystoreFile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setKeystoreFile(String newKeystoreFile) {
        String oldKeystoreFile = keystoreFile;
        keystoreFile = newKeystoreFile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__KEYSTORE_FILE,
                    oldKeystoreFile, keystoreFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getKeystorePassword() {
        return keystorePassword;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setKeystorePassword(String newKeystorePassword) {
        String oldKeystorePassword = keystorePassword;
        keystorePassword = newKeystorePassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__KEYSTORE_PASSWORD,
                    oldKeystorePassword, keystorePassword));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUsesocks() {
        return usesocks;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesocks(boolean newUsesocks) {
        boolean oldUsesocks = usesocks;
        usesocks = newUsesocks;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__USESOCKS, oldUsesocks,
                    usesocks));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProxyhost() {
        return proxyhost;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProxyhost(String newProxyhost) {
        String oldProxyhost = proxyhost;
        proxyhost = newProxyhost;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__PROXYHOST, oldProxyhost,
                    proxyhost));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProxyport() {
        return proxyport;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProxyport(String newProxyport) {
        String oldProxyport = proxyport;
        proxyport = newProxyport;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__PROXYPORT, oldProxyport,
                    proxyport));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProxyuser() {
        return proxyuser;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProxyuser(String newProxyuser) {
        String oldProxyuser = proxyuser;
        proxyuser = newProxyuser;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__PROXYUSER, oldProxyuser,
                    proxyuser));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProxypassword() {
        return proxypassword;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProxypassword(String newProxypassword) {
        String oldProxypassword = proxypassword;
        proxypassword = newProxypassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__PROXYPASSWORD,
                    oldProxypassword, proxypassword));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCustomEncode() {
        return customEncode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCustomEncode(String newCustomEncode) {
        String oldCustomEncode = customEncode;
        customEncode = newCustomEncode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FTP_CONNECTION__CUSTOM_ENCODE,
                    oldCustomEncode, customEncode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.FTP_CONNECTION__HOST:
            return getHost();
        case ConnectionPackage.FTP_CONNECTION__PORT:
            return getPort();
        case ConnectionPackage.FTP_CONNECTION__USERNAME:
            return getUsername();
        case ConnectionPackage.FTP_CONNECTION__PASSWORD:
            return getPassword();
        case ConnectionPackage.FTP_CONNECTION__MODE:
            return getMode();
        case ConnectionPackage.FTP_CONNECTION__ECODING:
            return getEcoding();
        case ConnectionPackage.FTP_CONNECTION__SFTP:
            return isSFTP();
        case ConnectionPackage.FTP_CONNECTION__FTPS:
            return isFTPS();
        case ConnectionPackage.FTP_CONNECTION__METHOD:
            return getMethod();
        case ConnectionPackage.FTP_CONNECTION__PRIVATEKEY:
            return getPrivatekey();
        case ConnectionPackage.FTP_CONNECTION__PASSPHRASE:
            return getPassphrase();
        case ConnectionPackage.FTP_CONNECTION__KEYSTORE_FILE:
            return getKeystoreFile();
        case ConnectionPackage.FTP_CONNECTION__KEYSTORE_PASSWORD:
            return getKeystorePassword();
        case ConnectionPackage.FTP_CONNECTION__USESOCKS:
            return isUsesocks();
        case ConnectionPackage.FTP_CONNECTION__PROXYHOST:
            return getProxyhost();
        case ConnectionPackage.FTP_CONNECTION__PROXYPORT:
            return getProxyport();
        case ConnectionPackage.FTP_CONNECTION__PROXYUSER:
            return getProxyuser();
        case ConnectionPackage.FTP_CONNECTION__PROXYPASSWORD:
            return getProxypassword();
        case ConnectionPackage.FTP_CONNECTION__CUSTOM_ENCODE:
            return getCustomEncode();
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
        case ConnectionPackage.FTP_CONNECTION__HOST:
            setHost((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__PORT:
            setPort((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__USERNAME:
            setUsername((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__PASSWORD:
            setPassword((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__MODE:
            setMode((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__ECODING:
            setEcoding((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__SFTP:
            setSFTP((Boolean) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__FTPS:
            setFTPS((Boolean) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__METHOD:
            setMethod((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__PRIVATEKEY:
            setPrivatekey((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__PASSPHRASE:
            setPassphrase((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__KEYSTORE_FILE:
            setKeystoreFile((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__KEYSTORE_PASSWORD:
            setKeystorePassword((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__USESOCKS:
            setUsesocks((Boolean) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__PROXYHOST:
            setProxyhost((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__PROXYPORT:
            setProxyport((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__PROXYUSER:
            setProxyuser((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__PROXYPASSWORD:
            setProxypassword((String) newValue);
            return;
        case ConnectionPackage.FTP_CONNECTION__CUSTOM_ENCODE:
            setCustomEncode((String) newValue);
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
        case ConnectionPackage.FTP_CONNECTION__HOST:
            setHost(HOST_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__PORT:
            setPort(PORT_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__USERNAME:
            setUsername(USERNAME_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__PASSWORD:
            setPassword(PASSWORD_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__MODE:
            setMode(MODE_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__ECODING:
            setEcoding(ECODING_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__SFTP:
            setSFTP(SFTP_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__FTPS:
            setFTPS(FTPS_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__METHOD:
            setMethod(METHOD_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__PRIVATEKEY:
            setPrivatekey(PRIVATEKEY_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__PASSPHRASE:
            setPassphrase(PASSPHRASE_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__KEYSTORE_FILE:
            setKeystoreFile(KEYSTORE_FILE_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__KEYSTORE_PASSWORD:
            setKeystorePassword(KEYSTORE_PASSWORD_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__USESOCKS:
            setUsesocks(USESOCKS_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__PROXYHOST:
            setProxyhost(PROXYHOST_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__PROXYPORT:
            setProxyport(PROXYPORT_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__PROXYUSER:
            setProxyuser(PROXYUSER_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__PROXYPASSWORD:
            setProxypassword(PROXYPASSWORD_EDEFAULT);
            return;
        case ConnectionPackage.FTP_CONNECTION__CUSTOM_ENCODE:
            setCustomEncode(CUSTOM_ENCODE_EDEFAULT);
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
        case ConnectionPackage.FTP_CONNECTION__HOST:
            return HOST_EDEFAULT == null ? host != null : !HOST_EDEFAULT.equals(host);
        case ConnectionPackage.FTP_CONNECTION__PORT:
            return PORT_EDEFAULT == null ? port != null : !PORT_EDEFAULT.equals(port);
        case ConnectionPackage.FTP_CONNECTION__USERNAME:
            return USERNAME_EDEFAULT == null ? username != null : !USERNAME_EDEFAULT.equals(username);
        case ConnectionPackage.FTP_CONNECTION__PASSWORD:
            return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
        case ConnectionPackage.FTP_CONNECTION__MODE:
            return MODE_EDEFAULT == null ? mode != null : !MODE_EDEFAULT.equals(mode);
        case ConnectionPackage.FTP_CONNECTION__ECODING:
            return ECODING_EDEFAULT == null ? ecoding != null : !ECODING_EDEFAULT.equals(ecoding);
        case ConnectionPackage.FTP_CONNECTION__SFTP:
            return sftp != SFTP_EDEFAULT;
        case ConnectionPackage.FTP_CONNECTION__FTPS:
            return ftps != FTPS_EDEFAULT;
        case ConnectionPackage.FTP_CONNECTION__METHOD:
            return METHOD_EDEFAULT == null ? method != null : !METHOD_EDEFAULT.equals(method);
        case ConnectionPackage.FTP_CONNECTION__PRIVATEKEY:
            return PRIVATEKEY_EDEFAULT == null ? privatekey != null : !PRIVATEKEY_EDEFAULT.equals(privatekey);
        case ConnectionPackage.FTP_CONNECTION__PASSPHRASE:
            return PASSPHRASE_EDEFAULT == null ? passphrase != null : !PASSPHRASE_EDEFAULT.equals(passphrase);
        case ConnectionPackage.FTP_CONNECTION__KEYSTORE_FILE:
            return KEYSTORE_FILE_EDEFAULT == null ? keystoreFile != null : !KEYSTORE_FILE_EDEFAULT.equals(keystoreFile);
        case ConnectionPackage.FTP_CONNECTION__KEYSTORE_PASSWORD:
            return KEYSTORE_PASSWORD_EDEFAULT == null ? keystorePassword != null : !KEYSTORE_PASSWORD_EDEFAULT
                    .equals(keystorePassword);
        case ConnectionPackage.FTP_CONNECTION__USESOCKS:
            return usesocks != USESOCKS_EDEFAULT;
        case ConnectionPackage.FTP_CONNECTION__PROXYHOST:
            return PROXYHOST_EDEFAULT == null ? proxyhost != null : !PROXYHOST_EDEFAULT.equals(proxyhost);
        case ConnectionPackage.FTP_CONNECTION__PROXYPORT:
            return PROXYPORT_EDEFAULT == null ? proxyport != null : !PROXYPORT_EDEFAULT.equals(proxyport);
        case ConnectionPackage.FTP_CONNECTION__PROXYUSER:
            return PROXYUSER_EDEFAULT == null ? proxyuser != null : !PROXYUSER_EDEFAULT.equals(proxyuser);
        case ConnectionPackage.FTP_CONNECTION__PROXYPASSWORD:
            return PROXYPASSWORD_EDEFAULT == null ? proxypassword != null : !PROXYPASSWORD_EDEFAULT.equals(proxypassword);
        case ConnectionPackage.FTP_CONNECTION__CUSTOM_ENCODE:
            return CUSTOM_ENCODE_EDEFAULT == null ? customEncode != null : !CUSTOM_ENCODE_EDEFAULT.equals(customEncode);
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
        result.append(" (Host: ");
        result.append(host);
        result.append(", Port: ");
        result.append(port);
        result.append(", Username: ");
        result.append(username);
        result.append(", Password: ");
        result.append(password);
        result.append(", Mode: ");
        result.append(mode);
        result.append(", Ecoding: ");
        result.append(ecoding);
        result.append(", SFTP: ");
        result.append(sftp);
        result.append(", FTPS: ");
        result.append(ftps);
        result.append(", Method: ");
        result.append(method);
        result.append(", Privatekey: ");
        result.append(privatekey);
        result.append(", Passphrase: ");
        result.append(passphrase);
        result.append(", KeystoreFile: ");
        result.append(keystoreFile);
        result.append(", KeystorePassword: ");
        result.append(keystorePassword);
        result.append(", Usesocks: ");
        result.append(usesocks);
        result.append(", Proxyhost: ");
        result.append(proxyhost);
        result.append(", Proxyport: ");
        result.append(proxyport);
        result.append(", Proxyuser: ");
        result.append(proxyuser);
        result.append(", Proxypassword: ");
        result.append(proxypassword);
        result.append(", CustomEncode: ");
        result.append(customEncode);
        result.append(')');
        return result.toString();
    }

} //FTPConnectionImpl
