package org.talend.core.repository.services;

import java.util.Collection;

public interface IGitContentService {

    public abstract void setMenu(Object object);

    public abstract boolean isGIT();

    public abstract void createDropdownCombo(Object composite);

    public abstract void configureCombo(String descriptor);

    public abstract Collection<Object> frezzeEditors(Collection<String> modifiledFiles) throws Exception;

    public abstract void defrezzeEditors(Collection<Object> frezzedEditors) throws Exception;

    public void refresh();
}
