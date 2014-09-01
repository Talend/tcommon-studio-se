/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FTP Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getHost <em>Host</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getPort <em>Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getUsername <em>Username</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getPassword <em>Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getMode <em>Mode</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getEcoding <em>Ecoding</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#isSFTP <em>SFTP</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#isFTPS <em>FTPS</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getMethod <em>Method</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getPrivatekey <em>Privatekey</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getPassphrase <em>Passphrase</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getKeystoreFile <em>Keystore File</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getKeystorePassword <em>Keystore Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#isUsesocks <em>Usesocks</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getProxyhost <em>Proxyhost</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getProxyport <em>Proxyport</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getProxyuser <em>Proxyuser</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getProxypassword <em>Proxypassword</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getCustomEncode <em>Custom Encode</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection()
 * @model
 * @generated
 */
public interface FTPConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Host</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Host</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Host</em>' attribute.
     * @see #setHost(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Host()
     * @model
     * @generated
     */
    String getHost();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getHost <em>Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Host</em>' attribute.
     * @see #getHost()
     * @generated
     */
    void setHost(String value);

    /**
     * Returns the value of the '<em><b>Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Port</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Port</em>' attribute.
     * @see #setPort(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Port()
     * @model
     * @generated
     */
    String getPort();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getPort <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Port</em>' attribute.
     * @see #getPort()
     * @generated
     */
    void setPort(String value);

    /**
     * Returns the value of the '<em><b>Username</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Username</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Username</em>' attribute.
     * @see #setUsername(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Username()
     * @model
     * @generated
     */
    String getUsername();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getUsername <em>Username</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Username</em>' attribute.
     * @see #getUsername()
     * @generated
     */
    void setUsername(String value);

    /**
     * Returns the value of the '<em><b>Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Password</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Password</em>' attribute.
     * @see #setPassword(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Password()
     * @model
     * @generated
     */
    String getPassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getPassword <em>Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Password</em>' attribute.
     * @see #getPassword()
     * @generated
     */
    void setPassword(String value);

    /**
     * Returns the value of the '<em><b>Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mode</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mode</em>' attribute.
     * @see #setMode(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Mode()
     * @model
     * @generated
     */
    String getMode();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getMode <em>Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mode</em>' attribute.
     * @see #getMode()
     * @generated
     */
    void setMode(String value);

    /**
     * Returns the value of the '<em><b>Ecoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ecoding</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ecoding</em>' attribute.
     * @see #setEcoding(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Ecoding()
     * @model
     * @generated
     */
    String getEcoding();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getEcoding <em>Ecoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ecoding</em>' attribute.
     * @see #getEcoding()
     * @generated
     */
    void setEcoding(String value);

    /**
     * Returns the value of the '<em><b>SFTP</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>SFTP</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>SFTP</em>' attribute.
     * @see #setSFTP(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_SFTP()
     * @model
     * @generated
     */
    boolean isSFTP();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#isSFTP <em>SFTP</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>SFTP</em>' attribute.
     * @see #isSFTP()
     * @generated
     */
    void setSFTP(boolean value);

    /**
     * Returns the value of the '<em><b>FTPS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>FTPS</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>FTPS</em>' attribute.
     * @see #setFTPS(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_FTPS()
     * @model
     * @generated
     */
    boolean isFTPS();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#isFTPS <em>FTPS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>FTPS</em>' attribute.
     * @see #isFTPS()
     * @generated
     */
    void setFTPS(boolean value);

    /**
     * Returns the value of the '<em><b>Method</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Method</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Method</em>' attribute.
     * @see #setMethod(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Method()
     * @model
     * @generated
     */
    String getMethod();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getMethod <em>Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method</em>' attribute.
     * @see #getMethod()
     * @generated
     */
    void setMethod(String value);

    /**
     * Returns the value of the '<em><b>Privatekey</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Privatekey</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Privatekey</em>' attribute.
     * @see #setPrivatekey(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Privatekey()
     * @model
     * @generated
     */
    String getPrivatekey();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getPrivatekey <em>Privatekey</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Privatekey</em>' attribute.
     * @see #getPrivatekey()
     * @generated
     */
    void setPrivatekey(String value);

    /**
     * Returns the value of the '<em><b>Passphrase</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Passphrase</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Passphrase</em>' attribute.
     * @see #setPassphrase(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Passphrase()
     * @model
     * @generated
     */
    String getPassphrase();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getPassphrase <em>Passphrase</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Passphrase</em>' attribute.
     * @see #getPassphrase()
     * @generated
     */
    void setPassphrase(String value);

    /**
     * Returns the value of the '<em><b>Keystore File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Keystore File</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Keystore File</em>' attribute.
     * @see #setKeystoreFile(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_KeystoreFile()
     * @model
     * @generated
     */
    String getKeystoreFile();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getKeystoreFile <em>Keystore File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Keystore File</em>' attribute.
     * @see #getKeystoreFile()
     * @generated
     */
    void setKeystoreFile(String value);

    /**
     * Returns the value of the '<em><b>Keystore Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Keystore Password</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Keystore Password</em>' attribute.
     * @see #setKeystorePassword(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_KeystorePassword()
     * @model
     * @generated
     */
    String getKeystorePassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getKeystorePassword <em>Keystore Password</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Keystore Password</em>' attribute.
     * @see #getKeystorePassword()
     * @generated
     */
    void setKeystorePassword(String value);

    /**
     * Returns the value of the '<em><b>Usesocks</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Usesocks</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Usesocks</em>' attribute.
     * @see #setUsesocks(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Usesocks()
     * @model
     * @generated
     */
    boolean isUsesocks();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#isUsesocks <em>Usesocks</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Usesocks</em>' attribute.
     * @see #isUsesocks()
     * @generated
     */
    void setUsesocks(boolean value);

    /**
     * Returns the value of the '<em><b>Proxyhost</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Proxyhost</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Proxyhost</em>' attribute.
     * @see #setProxyhost(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Proxyhost()
     * @model
     * @generated
     */
    String getProxyhost();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getProxyhost <em>Proxyhost</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Proxyhost</em>' attribute.
     * @see #getProxyhost()
     * @generated
     */
    void setProxyhost(String value);

    /**
     * Returns the value of the '<em><b>Proxyport</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Proxyport</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Proxyport</em>' attribute.
     * @see #setProxyport(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Proxyport()
     * @model
     * @generated
     */
    String getProxyport();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getProxyport <em>Proxyport</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Proxyport</em>' attribute.
     * @see #getProxyport()
     * @generated
     */
    void setProxyport(String value);

    /**
     * Returns the value of the '<em><b>Proxyuser</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Proxyuser</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Proxyuser</em>' attribute.
     * @see #setProxyuser(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Proxyuser()
     * @model
     * @generated
     */
    String getProxyuser();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getProxyuser <em>Proxyuser</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Proxyuser</em>' attribute.
     * @see #getProxyuser()
     * @generated
     */
    void setProxyuser(String value);

    /**
     * Returns the value of the '<em><b>Proxypassword</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Proxypassword</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Proxypassword</em>' attribute.
     * @see #setProxypassword(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_Proxypassword()
     * @model
     * @generated
     */
    String getProxypassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getProxypassword <em>Proxypassword</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Proxypassword</em>' attribute.
     * @see #getProxypassword()
     * @generated
     */
    void setProxypassword(String value);

    /**
     * Returns the value of the '<em><b>Custom Encode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Custom Encode</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Custom Encode</em>' attribute.
     * @see #setCustomEncode(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFTPConnection_CustomEncode()
     * @model
     * @generated
     */
    String getCustomEncode();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FTPConnection#getCustomEncode <em>Custom Encode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Custom Encode</em>' attribute.
     * @see #getCustomEncode()
     * @generated
     */
    void setCustomEncode(String value);

} // FTPConnection
