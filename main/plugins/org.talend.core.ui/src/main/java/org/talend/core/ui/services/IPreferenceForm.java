package org.talend.core.ui.services;


public interface IPreferenceForm {

    void setLayoutData(Object layoutData);

    boolean performApply();

    boolean performDefaults();
    
    boolean isComplete();

}
