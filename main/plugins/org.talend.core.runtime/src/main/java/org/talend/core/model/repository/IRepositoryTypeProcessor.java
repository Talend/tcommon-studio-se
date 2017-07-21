package org.talend.core.model.repository;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.core.model.process.IElement;
import org.talend.repository.model.RepositoryNode;

/**
 * created by ycbai on 2013-3-4 Detailled comment
 * 
 * Move from RepositoryReviewDialog.ITypeProcessor to expand its use.
 * 
 */
public interface IRepositoryTypeProcessor {

    public boolean isSelectionValid(RepositoryNode node);

    public ViewerFilter makeFilter();

    public String getDialogTitle();

    public ILabelProvider getLabelProvider(IElement elem);

    public List<ERepositoryObjectType> getShowRootTypes();

    public void setAttributes(Map<String, Object> attributes);
}
