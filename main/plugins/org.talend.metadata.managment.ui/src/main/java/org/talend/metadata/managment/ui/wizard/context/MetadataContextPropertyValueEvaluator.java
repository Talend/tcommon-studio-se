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
package org.talend.metadata.managment.ui.wizard.context;

import java.util.List;

import org.apache.avro.Schema;
import org.talend.commons.runtime.model.components.IComponentConstants;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.runtime.evaluator.AbstractPropertyValueEvaluator;
import org.talend.daikon.properties.property.Property;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;

/**
 * created by ycbai on 2016年2月6日 Detailled comment
 *
 */
public class MetadataContextPropertyValueEvaluator extends AbstractPropertyValueEvaluator {

    private Connection connection;

    public MetadataContextPropertyValueEvaluator(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object evaluate(Property property, Object storedValue) {
        if (storedValue == null) {
            return storedValue;
        }
        if (storedValue instanceof Schema || storedValue instanceof Enum
                || storedValue instanceof Boolean) {
            return storedValue;
        }
        boolean isPropertySupportContext = false;
        if (Boolean.valueOf(String.valueOf(property.getTaggedValue(IComponentConstants.SUPPORT_CONTEXT)))) {
            isPropertySupportContext = true;
        }
        if (connection != null && connection.isContextMode() && isPropertySupportContext) {
            ContextType contextType = ConnectionContextHelper.context;
            if(contextType == null){
                contextType = ConnectionContextHelper.getContextTypeForContextMode(connection,
                        connection.getContextName(), false);
            }
            if(storedValue instanceof List){
                storedValue = ContextParameterUtils.getOriginalList(contextType, String.valueOf(storedValue));
            }else{
                storedValue = ContextParameterUtils.getOriginalValue(contextType, String.valueOf(storedValue));
            }
            
        }
        return getTypedValue(property, storedValue);
    }

}
