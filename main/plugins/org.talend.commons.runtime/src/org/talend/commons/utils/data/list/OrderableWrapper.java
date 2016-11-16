// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils.data.list;

/**
 * @param <B> bean which must be ordered by <code>order</code> value
 */
class OrderableWrapper<B> implements Comparable<OrderableWrapper> {

    private int order;

    private B bean;

    public OrderableWrapper(int order, B bean) {
        super();
        this.order = order;
        this.bean = bean;
    }

    public int compareTo(OrderableWrapper o) {
        if (o == null) {
            return 1;
        }
        if (o == this) {
            return 0;
        }
        if (o.order == this.order) {
            return 0;
        }
        if (o.order > this.order) {
            return -1;
        }
        if (o.order < this.order) {
            return 1;
        }

        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.bean == null) ? 0 : this.bean.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderableWrapper other = (OrderableWrapper) obj;
        if (this.bean == null) {
            if (other.bean != null) {
                return false;
            }
        } else if (this.bean != other.bean) {
            return false;
        }
        return true;
    }

    /**
     * Getter for bean.
     * 
     * @return the bean
     */
    public B getBean() {
        return this.bean;
    }

    /**
     * Sets the bean.
     * 
     * @param bean the bean to set
     */
    public void setBean(B bean) {
        this.bean = bean;
    }

    /**
     * Getter for priorityCalled.
     * 
     * @return the priorityCalled
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * Sets the priorityCalled.
     * 
     * @param priorityCalled the priorityCalled to set
     */
    public void setOrder(int priorityCalled) {
        this.order = priorityCalled;
    }

    @Override
    public String toString() {
        return "bean=" + bean.hashCode() + ", order=" + order; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
