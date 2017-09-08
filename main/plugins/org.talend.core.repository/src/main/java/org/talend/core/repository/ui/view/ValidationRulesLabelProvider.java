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
package org.talend.core.repository.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.connection.ValidationRulesConnection;
import org.talend.core.model.process.IElement;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ValidationRulesConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class ValidationRulesLabelProvider extends RepositoryLabelProvider {

    public static final Color MERGED_REFERENCED_ITEMS_COLOR = new Color(null, 120, 120, 120);

    private IElement elem;

    /**
     * DOC wf ValidationRulesLabelProvider constructor comment.
     * 
     * @param view
     */
    public ValidationRulesLabelProvider(IRepositoryView view, IElement elem) {
        super(view);
        this.elem = elem;
    }

    @Override
    public Color getForeground(Object element) {
        RepositoryNode node = (RepositoryNode) element;
        List<IRepositoryViewObject> objs = getRelatedValidationRuleObjs(elem);
        if (node.getObjectType() == ERepositoryObjectType.METADATA_VALIDATION_RULES) {
            for (IRepositoryViewObject object : objs) {
                if (node.getObject().getId().equals(object.getId())) {
                    return super.getForeground(element);
                }
            }
        }
        return MERGED_REFERENCED_ITEMS_COLOR;
    }

    public static List<IRepositoryViewObject> getRelatedValidationRuleObjs(IElement element) {
        String repId = (String) element.getPropertyValue("REPOSITORY_SCHEMA_TYPE");
        return getRelatedValidationRuleObjs(repId);
    }

    public static List<IRepositoryViewObject> getRelatedValidationRuleObjs(String schemaId) {
        List<IRepositoryViewObject> rulesObjs = new ArrayList<IRepositoryViewObject>();

        if (schemaId != null) {
            try {
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                List<IRepositoryViewObject> members = factory.getAll(ERepositoryObjectType.METADATA_VALIDATION_RULES);
                if (members != null && members.size() > 0) {
                    for (IRepositoryViewObject member : members) {
                        if (member != null && member.getProperty() != null) {
                            Item item = member.getProperty().getItem();
                            if (item != null && item instanceof ValidationRulesConnectionItem) {
                                ValidationRulesConnectionItem validItem = (ValidationRulesConnectionItem) item;
                                ValidationRulesConnection connection = (ValidationRulesConnection) validItem.getConnection();
                                // if (connection != null && schemaId.equals(connection.getBaseSchema())
                                // && !rulesObjs.contains(member)) {
                                if (connection != null && !rulesObjs.contains(member)
                                        && schemaId.equals(connection.getBaseSchema())) {
                                    rulesObjs.add(member);
                                }
                            }
                        }
                    }
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }

        return rulesObjs;
    }
}
