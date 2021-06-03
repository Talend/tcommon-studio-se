/**
 * UserRegistration.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.talend.registration.register.proxy;

public class UserRegistration  implements java.io.Serializable {
    private int id;

    private java.lang.String email;

    private java.lang.String country;

    private java.lang.String designer_version;

    private java.lang.String productname;

    private java.lang.String registration_date;

    public UserRegistration() {
    }

    public UserRegistration(
           int id,
           java.lang.String email,
           java.lang.String country,
           java.lang.String designer_version,
           java.lang.String productname,
           java.lang.String registration_date) {
           this.id = id;
           this.email = email;
           this.country = country;
           this.designer_version = designer_version;
           this.productname = productname;
           this.registration_date = registration_date;
    }


    /**
     * Gets the id value for this UserRegistration.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this UserRegistration.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the email value for this UserRegistration.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this UserRegistration.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the country value for this UserRegistration.
     * 
     * @return country
     */
    public java.lang.String getCountry() {
        return country;
    }


    /**
     * Sets the country value for this UserRegistration.
     * 
     * @param country
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }


    /**
     * Gets the designer_version value for this UserRegistration.
     * 
     * @return designer_version
     */
    public java.lang.String getDesigner_version() {
        return designer_version;
    }


    /**
     * Sets the designer_version value for this UserRegistration.
     * 
     * @param designer_version
     */
    public void setDesigner_version(java.lang.String designer_version) {
        this.designer_version = designer_version;
    }


    /**
     * Gets the productname value for this UserRegistration.
     * 
     * @return productname
     */
    public java.lang.String getProductname() {
        return productname;
    }


    /**
     * Sets the productname value for this UserRegistration.
     * 
     * @param productname
     */
    public void setProductname(java.lang.String productname) {
        this.productname = productname;
    }


    /**
     * Gets the registration_date value for this UserRegistration.
     * 
     * @return registration_date
     */
    public java.lang.String getRegistration_date() {
        return registration_date;
    }


    /**
     * Sets the registration_date value for this UserRegistration.
     * 
     * @param registration_date
     */
    public void setRegistration_date(java.lang.String registration_date) {
        this.registration_date = registration_date;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserRegistration)) return false;
        UserRegistration other = (UserRegistration) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.country==null && other.getCountry()==null) || 
             (this.country!=null &&
              this.country.equals(other.getCountry()))) &&
            ((this.designer_version==null && other.getDesigner_version()==null) || 
             (this.designer_version!=null &&
              this.designer_version.equals(other.getDesigner_version()))) &&
            ((this.productname==null && other.getProductname()==null) || 
             (this.productname!=null &&
              this.productname.equals(other.getProductname()))) &&
            ((this.registration_date==null && other.getRegistration_date()==null) || 
             (this.registration_date!=null &&
              this.registration_date.equals(other.getRegistration_date())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getId();
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getCountry() != null) {
            _hashCode += getCountry().hashCode();
        }
        if (getDesigner_version() != null) {
            _hashCode += getDesigner_version().hashCode();
        }
        if (getProductname() != null) {
            _hashCode += getProductname().hashCode();
        }
        if (getRegistration_date() != null) {
            _hashCode += getRegistration_date().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
