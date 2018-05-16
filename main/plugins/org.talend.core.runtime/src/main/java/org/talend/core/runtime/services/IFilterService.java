package org.talend.core.runtime.services;

import org.talend.core.IService;
import org.talend.core.model.properties.Item;

public interface IFilterService extends IService {

    public String checkFilterError(String filter);

	public boolean isFilterAccepted(Item item, String filter);

}
