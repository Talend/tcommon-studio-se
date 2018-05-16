// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.metadata;

import java.util.List;
import java.util.Set;

/**
 * 
 * DOC amaumont TypesManager class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class Dbms {

	private String id;

	private String product;

	private String label;

	private String defaultDbType;

	private boolean defaultDbms;

	private List<String> dbmsTypes;

	private Set<MappingType> dbToTalendTypes;

	private Set<MappingType> talendToDbTypes;

	private List<DbDefaultLengthAndPrecision> defaultLengthPrecision;

	private List<DbIgnoreLengthAndPrecision> ignoreLengthPrecision;

	private List<DbPreBeforeLength> prebeforelength;

	public List<DbPreBeforeLength> getPrebeforelength() {
		return prebeforelength;
	}

	public void setPrebeforelength(List<DbPreBeforeLength> prebeforelength) {
		this.prebeforelength = prebeforelength;
	}

	public List<DbDefaultLengthAndPrecision> getDefaultLengthPrecision() {
		return defaultLengthPrecision;
	}

	public void setDefaultLengthPrecision(
			List<DbDefaultLengthAndPrecision> defaultLengthPrecision) {
		this.defaultLengthPrecision = defaultLengthPrecision;
	}

	/**
	 * DOC amaumont Dbms constructor comment.
	 * 
	 * @param id
	 * @param label
	 * @param dbmsTypes
	 * @param mappingTypes
	 */
	public Dbms(String id, String label, boolean defaultDbms,
			List<String> dbmsTypes) {
		super();
		this.id = id;
		this.label = label;
		this.dbmsTypes = dbmsTypes;
		this.defaultDbms = defaultDbms;
	}

	/**
	 * DOC amaumont Dbms constructor comment.
	 * 
	 * @param dbmsIdValue
	 */
	protected Dbms(String id) {
		this.id = id;
	}

	/**
	 * Getter for dbmsTypes.
	 * 
	 * @return the dbmsTypes
	 */
	public List<String> getDbTypes() {
		return this.dbmsTypes;
	}

	/**
	 * Sets the dbmsTypes.
	 * 
	 * @param dbmsTypes
	 *            the dbmsTypes to set
	 */
	protected void setDbmsTypes(List<String> dbmsTypes) {
		this.dbmsTypes = dbmsTypes;
	}

	/**
	 * Getter for id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	protected void setId(String id) {
		this.id = id;
	}

	/**
	 * Getter for label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            the label to set
	 */
	protected void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Getter for mappingTypes.
	 * 
	 * @return the mappingTypes
	 */
	public Set<MappingType> getDbToTalendTypes() {
		return this.dbToTalendTypes;
	}

	/**
	 * Sets the mappingTypes.
	 * 
	 * @param mappingTypes
	 *            the mappingTypes to set
	 */
	protected void setDbToTalendTypes(Set<MappingType> mappingTypes) {
		this.dbToTalendTypes = mappingTypes;
	}

	/**
	 * Getter for talendToDbTypes.
	 * 
	 * @return the talendToDbTypes
	 */
	public Set<MappingType> getTalendToDbTypes() {
		return this.talendToDbTypes;
	}

	/**
	 * Sets the talendToDbTypes.
	 * 
	 * @param talendToDbTypes
	 *            the talendToDbTypes to set
	 */
	protected void setTalendToDbTypes(Set<MappingType> talendToDbTypes) {
		this.talendToDbTypes = talendToDbTypes;
	}

	/**
	 * Getter for product.
	 * 
	 * @return the product
	 */
	public String getProduct() {
		return this.product;
	}

	/**
	 * Sets the product.
	 * 
	 * @param product
	 *            the product to set
	 */
	protected void setProduct(String product) {
		this.product = product;
	}

	/**
	 * Getter for defaultDbType.
	 * 
	 * @return the defaultDbType
	 */
	public String getDefaultDbType() {
		return this.defaultDbType;
	}

	/**
	 * Sets the defaultDbType.
	 * 
	 * @param defaultDbType
	 *            the defaultDbType to set
	 */
	protected void setDefaultDbType(String defaultDbType) {
		this.defaultDbType = defaultDbType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Dbms other = (Dbms) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * toString method: creates a String representation of the object
	 * 
	 * @return the String representation
	 * @author
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Dbms["); //$NON-NLS-1$
		buffer.append("product = ").append(product); //$NON-NLS-1$
		buffer.append(", id = ").append(id); //$NON-NLS-1$
		buffer.append(", label = ").append(label); //$NON-NLS-1$
		buffer.append(", defaultDbType = ").append(defaultDbType); //$NON-NLS-1$
		buffer.append(", dbmsTypes = ").append(dbmsTypes); //$NON-NLS-1$
		buffer.append(", mappingTypes = ").append(dbToTalendTypes); //$NON-NLS-1$
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

	/**
	 * Getter for defaultDbms.
	 * 
	 * @return the defaultDbms
	 */
	protected boolean isDefaultDbms() {
		return defaultDbms;
	}

	/**
	 * Sets the defaultDbms.
	 * 
	 * @param defaultDbms
	 *            the defaultDbms to set
	 */
	protected void setDefaultDbms(boolean defaultDbms) {
		this.defaultDbms = defaultDbms;
	}

	public List<DbIgnoreLengthAndPrecision> getIgnoreLengthPrecision() {
		return ignoreLengthPrecision;
	}

	public void setIgnoreLengthPrecision(
			List<DbIgnoreLengthAndPrecision> ignoreLengthPrecision) {
		this.ignoreLengthPrecision = ignoreLengthPrecision;
	}

}
