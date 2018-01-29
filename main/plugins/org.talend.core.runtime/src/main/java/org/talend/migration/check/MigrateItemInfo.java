// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.migration.check;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * created by wchen on Dec 30, 2014 Detailled comment
 *
 */
public class MigrateItemInfo {

    private int id;

    private int project_id;

    private String type;

    private String type_name;

    private String label;

    private String version;

    private Date created;

    private Date last_modified;

    private String author;

    private String status;

    private List<Problem> problems = new ArrayList<Problem>();

    /**
     * Getter for id.
     * 
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the id.
     * 
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the type.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Getter for type_name.
     * 
     * @return the type_name
     */
    public String getType_name() {
        return this.type_name;
    }

    /**
     * Sets the type_name.
     * 
     * @param type_name the type_name to set
     */
    public void setType_name(String type_name) {
        this.type_name = type_name;
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
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for version.
     * 
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Sets the version.
     * 
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Getter for created.
     * 
     * @return the created
     */
    public Date getCreated() {
        return this.created;
    }

    /**
     * Sets the created.
     * 
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Getter for author.
     * 
     * @return the author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Sets the author.
     * 
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter for project_id.
     * 
     * @return the project_id
     */
    public int getProject_id() {
        return this.project_id;
    }

    /**
     * Sets the project_id.
     * 
     * @param project_id the project_id to set
     */
    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    /**
     * Getter for last_modified.
     * 
     * @return the last_modified
     */
    public Date getLast_modified() {
        return this.last_modified;
    }

    /**
     * Sets the last_modified.
     * 
     * @param last_modified the last_modified to set
     */
    public void setLast_modified(Date last_modified) {
        this.last_modified = last_modified;
    }

    /**
     * Getter for problems.
     * 
     * @return the problems
     */
    public List<Problem> getProblems() {
        return this.problems;
    }

    /**
     * Getter for status.
     * 
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Sets the status.
     * 
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
