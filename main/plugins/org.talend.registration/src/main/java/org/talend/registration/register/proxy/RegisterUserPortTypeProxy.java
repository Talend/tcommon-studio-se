package org.talend.registration.register.proxy;

public class RegisterUserPortTypeProxy implements org.talend.registration.register.proxy.RegisterUserPortType {

    private String _endpoint = null;

    private org.talend.registration.register.proxy.RegisterUserPortType registerUserPortType = null;

    public RegisterUserPortTypeProxy() {
        _initRegisterUserPortTypeProxy();
    }

    public RegisterUserPortTypeProxy(String endpoint) {
        _endpoint = endpoint;
        _initRegisterUserPortTypeProxy();
    }

    private void _initRegisterUserPortTypeProxy() {
        try {
            registerUserPortType = (new org.talend.registration.register.proxy.RegisterUserLocator()).getRegisterUserPort();
        } catch (javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
    }

    public org.talend.registration.register.proxy.RegisterUserPortType getRegisterUserPortType() {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType;
    }

    public boolean registerUser(java.lang.String email, java.lang.String country, java.lang.String designerversion)
            throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.registerUser(email, country, designerversion);
    }

    public boolean registerUserWithProductName(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname) throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.registerUserWithProductName(email, country, designerversion, productname);
    }

    public boolean registerUserWithAllUserInformations(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname, java.lang.String projectLanguage,
            java.lang.String osName, java.lang.String osVersion, java.lang.String javaVersion, java.lang.String totalMemory,
            java.lang.String memRAM, java.lang.String nbProc) throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.registerUserWithAllUserInformations(email, country, designerversion, productname,
                projectLanguage, osName, osVersion, javaVersion, totalMemory, memRAM, nbProc);
    }

    public java.math.BigInteger registerUserWithAllUserInformationsAndReturnId(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname, java.lang.String projectLanguage,
            java.lang.String osName, java.lang.String osVersion, java.lang.String javaVersion, java.lang.String totalMemory,
            java.lang.String memRAM, java.lang.String nbProc) throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.registerUserWithAllUserInformationsAndReturnId(email, country, designerversion, productname,
                projectLanguage, osName, osVersion, javaVersion, totalMemory, memRAM, nbProc);
    }

    public java.math.BigInteger registerUserWithAllUserInformationsUniqueIdAndReturnId(java.lang.String email,
            java.lang.String country, java.lang.String designerversion, java.lang.String productname,
            java.lang.String projectLanguage, java.lang.String osName, java.lang.String osVersion, java.lang.String javaVersion,
            java.lang.String totalMemory, java.lang.String memRAM, java.lang.String nbProc, java.lang.String uniqueId)
            throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.registerUserWithAllUserInformationsUniqueIdAndReturnId(email, country, designerversion,
                productname, projectLanguage, osName, osVersion, javaVersion, totalMemory, memRAM, nbProc, uniqueId);
    }

    public org.talend.registration.register.proxy.UserRegistration[] listUsers() throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.listUsers();
    }

    public java.lang.String checkUser(java.lang.String email) throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.checkUser(email);
    }

    public java.math.BigInteger createUser(java.lang.String email, java.lang.String pseudo, java.lang.String password,
            java.lang.String firstname, java.lang.String lastname, java.lang.String country, java.lang.String designerversion,
            java.lang.String productname, java.lang.String osName, java.lang.String osVersion, java.lang.String javaVersion,
            java.lang.String totalMemory, java.lang.String memRAM, java.lang.String nbProc) throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.createUser(email, pseudo, password, firstname, lastname, country, designerversion,
                productname, osName, osVersion, javaVersion, totalMemory, memRAM, nbProc);
    }

    public java.math.BigInteger updateUser(java.lang.String email, java.lang.String pseudo, java.lang.String passwordOld,
            java.lang.String passwordNew, java.lang.String firstname, java.lang.String lastname, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname, java.lang.String osName, java.lang.String osVersion,
            java.lang.String javaVersion, java.lang.String totalMemory, java.lang.String memRAM, java.lang.String nbProc)
            throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.updateUser(email, pseudo, passwordOld, passwordNew, firstname, lastname, country,
                designerversion, productname, osName, osVersion, javaVersion, totalMemory, memRAM, nbProc);
    }

    public java.math.BigInteger createUser50(java.lang.String pseudo, java.lang.String password, java.lang.String firstname,
            java.lang.String lastname, java.lang.String country, java.lang.String designerversion, java.lang.String productname,
            java.lang.String osName, java.lang.String osVersion, java.lang.String javaVersion, java.lang.String totalMemory,
            java.lang.String memRAM, java.lang.String nbProc) throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.createUser50(pseudo, password, firstname, lastname, country, designerversion, productname,
                osName, osVersion, javaVersion, totalMemory, memRAM, nbProc);
    }

    public java.math.BigInteger createUser53(java.lang.String email, java.lang.String pseudo, java.lang.String password,
            java.lang.String firstname, java.lang.String lastname, java.lang.String country, java.lang.String designerversion,
            java.lang.String productname, java.lang.String osName, java.lang.String osVersion, java.lang.String javaVersion,
            java.lang.String totalMemory, java.lang.String memRAM, java.lang.String nbProc, java.lang.String uniqueId)
            throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.createUser53(email, pseudo, password, firstname, lastname, country, designerversion,
                productname, osName, osVersion, javaVersion, totalMemory, memRAM, nbProc, uniqueId);
    }

    public java.math.BigInteger updateUser53(java.lang.String pseudo, java.lang.String password, java.lang.String firstname,
            java.lang.String lastname, java.lang.String country, java.lang.String designerversion, java.lang.String productname,
            java.lang.String osName, java.lang.String osVersion, java.lang.String javaVersion, java.lang.String totalMemory,
            java.lang.String memRAM, java.lang.String nbProc, java.lang.String uniqueId) throws java.rmi.RemoteException {
        if (registerUserPortType == null)
            _initRegisterUserPortTypeProxy();
        return registerUserPortType.updateUser53(pseudo, password, firstname, lastname, country, designerversion, productname,
                osName, osVersion, javaVersion, totalMemory, memRAM, nbProc, uniqueId);
    }
}
