// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.runtime.services;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.apache.avro.Schema;
import org.talend.components.api.component.ComponentDefinition;
import org.talend.components.api.component.ComponentImageType;
import org.talend.components.api.component.Connector;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.wizard.ComponentWizard;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.components.api.wizard.WizardImageType;
import org.talend.daikon.properties.PropertyValueEvaluator;
import org.talend.daikon.properties.service.Repository;

/**
 * created by ycbai on 2016年2月4日 Detailled comment
 *
 */
public class ComponentServiceWithValueEvaluator implements ComponentService {

    private ComponentService service;

    private PropertyValueEvaluator valueEvaluator;

    public ComponentServiceWithValueEvaluator(ComponentService service, PropertyValueEvaluator valueEvaluator) {
        this.service = service;
        this.valueEvaluator = valueEvaluator;
    }

    @Override
    public String storeProperties(ComponentProperties properties, String name, String repositoryLocation,
            String schemaPropertyName) {
        return service.storeProperties(properties, name, repositoryLocation, schemaPropertyName);
    }

    @Override
    public ComponentProperties afterFormBack(String formName, ComponentProperties properties) throws Throwable {
        properties.setValueEvaluator(valueEvaluator);
        return service.afterFormBack(formName, properties);
    }

    @Override
    public ComponentProperties afterFormFinish(String formName, ComponentProperties properties) throws Throwable {
        properties.setValueEvaluator(valueEvaluator);
        return service.afterFormFinish(formName, properties);
    }

    @Override
    public ComponentProperties afterFormNext(String formName, ComponentProperties properties) throws Throwable {
        properties.setValueEvaluator(valueEvaluator);
        return service.afterFormNext(formName, properties);
    }

    @Override
    public ComponentProperties afterProperty(String propName, ComponentProperties properties) throws Throwable {
        properties.setValueEvaluator(valueEvaluator);
        return service.afterProperty(propName, properties);
    }

    @Override
    public ComponentProperties beforeFormPresent(String formName, ComponentProperties properties) throws Throwable {
        properties.setValueEvaluator(valueEvaluator);
        return service.beforeFormPresent(formName, properties);
    }

    @Override
    public ComponentProperties beforePropertyActivate(String propName, ComponentProperties properties) throws Throwable {
        properties.setValueEvaluator(valueEvaluator);
        return service.beforePropertyActivate(propName, properties);
    }

    @Override
    public ComponentProperties beforePropertyPresent(String propName, ComponentProperties properties) throws Throwable {
        properties.setValueEvaluator(valueEvaluator);
        return service.beforePropertyPresent(propName, properties);
    }

    @Override
    public Set<String> getAllComponentNames() {
        return service.getAllComponentNames();
    }

    @Override
    public Set<ComponentDefinition> getAllComponents() {
        return service.getAllComponents();
    }

    @Override
    public ComponentDefinition getComponentDefinition(String name) {
        return service.getComponentDefinition(name);
    }

    @Override
    public InputStream getComponentPngImage(String componentName, ComponentImageType imageType) {
        return service.getComponentPngImage(componentName, imageType);
    }

    @Override
    public ComponentProperties getComponentProperties(String name) {
        return service.getComponentProperties(name);
    }

    @Override
    public ComponentWizard getComponentWizard(String name, String location) {
        return service.getComponentWizard(name, location);
    }

    @Override
    public List<ComponentWizard> getComponentWizardsForProperties(ComponentProperties properties, String location) {
        return service.getComponentWizardsForProperties(properties, location);
    }

    @Override
    public Set<String> getMavenUriDependencies(String componentName) {
        return service.getMavenUriDependencies(componentName);
    }

    @Override
    public List<ComponentDefinition> getPossibleComponents(ComponentProperties... properties) throws Throwable {
        return service.getPossibleComponents(properties);
    }

    @Override
    public Set<ComponentWizardDefinition> getTopLevelComponentWizards() {
        return service.getTopLevelComponentWizards();
    }

    @Override
    public InputStream getWizardPngImage(String wizardName, WizardImageType imageType) {
        return service.getWizardPngImage(wizardName, imageType);
    }

    @Override
    public ComponentProperties makeFormCancelable(ComponentProperties properties, String formName) {
        return service.makeFormCancelable(properties, formName);
    }

    @Override
    public void setRepository(Repository repository) {
        service.setRepository(repository);
    }

    @Override
    public ComponentProperties validateProperty(String propName, ComponentProperties properties) throws Throwable {
        properties.setValueEvaluator(valueEvaluator);
        return service.validateProperty(propName, properties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.daikon.properties.service.PropertiesService#cancelFormValues(org.talend.daikon.properties.Properties,
     * java.lang.String)
     */
    @Override
    public ComponentProperties cancelFormValues(ComponentProperties arg0, String arg1) {
        return service.cancelFormValues(arg0, arg1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.components.api.service.ComponentService#getSchema(org.talend.components.api.properties.ComponentProperties
     * , org.talend.components.api.component.Connector, boolean)
     */
    @Override
    public Schema getSchema(ComponentProperties arg0, Connector arg1, boolean arg2) {
        return service.getSchema(arg0, arg1, arg2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.components.api.service.ComponentService#getAvailableConnectors(org.talend.components.api.properties
     * .ComponentProperties, java.util.Set, boolean)
     */
    @Override
    public Set<? extends Connector> getAvailableConnectors(ComponentProperties arg0, Set<? extends Connector> arg1, boolean arg2) {
        return service.getAvailableConnectors(arg0, arg1, arg2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.components.api.service.ComponentService#setSchema(org.talend.components.api.properties.ComponentProperties
     * , org.talend.components.api.component.Connector, org.apache.avro.Schema, boolean)
     */
    @Override
    public void setSchema(ComponentProperties arg0, Connector arg1, Schema arg2, boolean arg3) {
        service.setSchema(arg0, arg1, arg2, arg3);
    }

    @Override
    public boolean setNestedPropertiesValues(ComponentProperties targetProperties, ComponentProperties nestedValues) {
        return targetProperties.updateNestedProperties(nestedValues);
    }

}
