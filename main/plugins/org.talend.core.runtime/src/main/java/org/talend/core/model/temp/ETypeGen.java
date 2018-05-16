// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.core.model.temp;

/**
 * DOC rdubois class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public enum ETypeGen {
    ETL("etl"), //$NON-NLS-1$
    CAMEL("camel"), //$NON-NLS-1$
    MR("mr"), //$NON-NLS-1$
    STORM("storm"), //$NON-NLS-1$
    SPARK("spark"); //$NON-NLS-1$

    private ETypeGen(String name) {
        this.name = name;
    }

    private String name;

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return getName();
    }

    public static ETypeGen getTypeGenByName(String name) {
        for (ETypeGen gen : values()) {
            if (gen.getName().equals(name)) {
                return gen;
            }
        }
        return null;
    }
}
