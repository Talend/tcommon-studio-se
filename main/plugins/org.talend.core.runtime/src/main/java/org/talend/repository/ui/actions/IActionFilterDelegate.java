package org.talend.repository.ui.actions;

import org.talend.repository.model.RepositoryNode;

public interface IActionFilterDelegate {

    public boolean testAttribute (RepositoryNode node, String value);
    
}
