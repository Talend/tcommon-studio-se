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
package org.talend.commons.utils.data.container;

/**
 * PTODO SML Delete this class.<br/>
 * 
 * @param <K> - DOC SML
 * @param <V> - type the container manages
 * 
 * $Id$
 * 
 */
public class Content<K, V> {

    private Container<K, V> parent;

    private V content;

    private K key;

    /**
     * DOC smallet Content constructor comment.
     * 
     * @param parent
     * @param content
     * @param key
     */
    public Content(Container<K, V> parent, V content, K key) {
        super();
        this.parent = parent;
        this.content = content;
        this.key = key;
    }

    /**
     * Getter for content.
     * 
     * @return the content
     */
    public V getContent() {
        return this.content;
    }

    /**
     * Sets the content.
     * 
     * @param content the content to set
     */
    public void setContent(V content) {
        this.content = content;
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public Object getKey() {
        return this.key;
    }

    /**
     * Sets the key.
     * 
     * @param key the key to set
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Getter for parent.
     * 
     * @return the parent
     */
    public Container<K, V> getParent() {
        return this.parent;
    }

    /**
     * Sets the parent.
     * 
     * @param parent the parent to set
     */
    public void setParent(Container<K, V> parent) {
        this.parent = parent;
    }

}
