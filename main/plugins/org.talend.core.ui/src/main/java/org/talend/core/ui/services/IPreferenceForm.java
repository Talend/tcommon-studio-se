package org.talend.core.ui.services;


public interface IPreferenceForm {

    void setLayoutData(Object layoutData);

    boolean performApply();

    void performDefaults();

}
