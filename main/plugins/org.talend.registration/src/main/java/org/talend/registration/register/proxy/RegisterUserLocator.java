/**
 * RegisterUserLocator.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.talend.registration.register.proxy;

public class RegisterUserLocator implements
        org.talend.registration.register.proxy.RegisterUser {

    public RegisterUserLocator() {
    }

    // Use to get a proxy class for RegisterUserPort
    private java.lang.String RegisterUserPort_address = "https://www.talend.com/TalendRegisterWS/registerws.php";

    public java.lang.String getRegisterUserPortAddress() {
        return RegisterUserPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegisterUserPortWSDDServiceName = "RegisterUserPort";

    public java.lang.String getRegisterUserPortWSDDServiceName() {
        return RegisterUserPortWSDDServiceName;
    }

    public void setRegisterUserPortWSDDServiceName(java.lang.String name) {
        RegisterUserPortWSDDServiceName = name;
    }

    public org.talend.registration.register.proxy.RegisterUserPortType getRegisterUserPort()
            throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegisterUserPort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegisterUserPort(endpoint);
    }

    public org.talend.registration.register.proxy.RegisterUserPortType getRegisterUserPort(java.net.URL portAddress)
            throws javax.xml.rpc.ServiceException {
        try {
            org.talend.registration.register.proxy.RegisterUserBindingStub _stub = new org.talend.registration.register.proxy.RegisterUserBindingStub(
                    portAddress.toString());
            return _stub;
        } catch (org.apache.axis2.AxisFault e) {
            return null;
        }
    }

    public void setRegisterUserPortEndpointAddress(java.lang.String address) {
        RegisterUserPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.talend.registration.register.proxy.RegisterUserPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                org.talend.registration.register.proxy.RegisterUserBindingStub _stub = new org.talend.registration.register.proxy.RegisterUserBindingStub(
                        RegisterUserPort_address);
                return _stub;
            }
        } catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
                + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
            throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("RegisterUserPort".equals(inputPortName)) {
            return getRegisterUserPort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", "RegisterUser");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl", "RegisterUserPort"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("RegisterUserPort".equals(portName)) {
            setRegisterUserPortEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address)
            throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
