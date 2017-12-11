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
package org.talend.core.model.components;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.temp.ECodePart;

/**
 * Interface that describes the functions that a must implements a component manager. <br/>
 * 
 * $Id: IComponent.java 40568 2010-04-19 02:10:00Z nrousseau $
 */
public interface IComponent {

    String JOBLET_PID = "org.talend.designer.joblet"; //$NON-NLS-1$
    
    String SPARK_JOBLET_PID = "org.talend.designer.sparkjoblet"; //$NON-NLS-1$
    
    String SPARK_JOBLET_STREAMING_PID = "org.talend.designer.sparkstreamingjoblet"; //$NON-NLS-1$

    String PROP_NAME = "NAME"; //$NON-NLS-1$

    String PROP_LONG_NAME = "LONG_NAME"; //$NON-NLS-1$

    String PROP_FAMILY = "FAMILY"; //$NON-NLS-1$

    String PROP_MENU = "MENU"; //$NON-NLS-1$

    String PROP_LINK = "LINK"; //$NON-NLS-1$

    String PROP_HELP = "HELP"; //$NON-NLS-1$

    String JOBLET_FAMILY = "Joblets"; //$NON-NLS-1$
    
    String SPARK_JOBLET_FAMILY = "Spark Joblets"; //$NON-NLS-1$
    
    String SPARK_STREAMING_JOBLET_FAMILY = "Spark Streaming Joblets"; //$NON-NLS-1$

    public String getName();
    
    public String getOriginalName();

    public String getLongName();

    public String getOriginalFamilyName();

    public String getTranslatedFamilyName();

    public void setImageRegistry(Map<String, ImageDescriptor> imageRegistry);

    public Map<String, ImageDescriptor> getImageRegistry();

    public ImageDescriptor getIcon32();

    public ImageDescriptor getIcon24();

    public ImageDescriptor getIcon16();

    public List<? extends IElementParameter> createElementParameters(INode node);

    public List<? extends INodeReturn> createReturns(INode node);

    public List<? extends INodeConnector> createConnectors(INode node);

    public boolean hasConditionalOutputs();

    public boolean isMultiplyingOutputs();

    /**
     * Should only be set for external components, for others should be null.
     * 
     * @return
     */
    public String getPluginExtension();

    public boolean isSchemaAutoPropagated();

    public boolean isDataAutoPropagated();

    public boolean isHashComponent();

    public boolean useMerge();

    public boolean useLookup();

    public String getVersion();

    public List<IMultipleComponentManager> getMultipleComponentManagers();

    public boolean isLoaded();

    /**
     * To avoid since a component can be visible in one family but displayed in another one.<br>
     * Better use isVisible(String family) to check if component is visible in current family.
     * 
     * @return
     */
    @Deprecated
    public boolean isVisible();

    public boolean isVisible(String family);

    
    /**
     * Get the default modules needed for the component.
     * @return
     */
    public List<ModuleNeeded> getModulesNeeded();

    /**
     * Get the modules needed according the setup of a defined component.
     * @return
     */
    public List<ModuleNeeded> getModulesNeeded(INode node);

    public String getPathSource();

    public List<ECodePart> getAvailableCodeParts();

    public List<String> getPluginDependencies();

    public boolean isMultipleOutput();

    public boolean useImport();

    public EComponentType getComponentType();

    /**
     * Return true if this component is technical, means should not be displayed in the palette but must be generated
     * 
     * @return
     */
    public boolean isTechnical();

    public boolean isVisibleInComponentDefinition();

    public boolean isSingleton();

    public boolean isMainCodeCalled();

    public boolean canParallelize();

    public String getShortName();

    // see 17353
    public String getCombine();

    public IProcess getProcess();

    public String getPaletteType();

    public void setPaletteType(String paletteType);

    public String getRepositoryType();

    public boolean isLog4JEnabled();

    public EList getCONNECTORList();

    /**
     * This is just added in <code>HEADER</code>, it is used to present the component belongs to common process, M/R
     * process and etc. About the type please refer to the {@link ComponentCategory}. Added by Marvin Wang on Jan 11,
     * 2013.
     * 
     * @return
     */
    String getType();

    /**
     * This is used to present if the current component includes reduce code or not. Added by Marvin Wang on Jan 11,
     * 2013.
     * 
     * @return
     */
    boolean isReduce();

    /**
     * This method is used to get the type of input that can go in a BigData component DOC rdubois Comment method
     * "getInputType".
     * 
     * @return
     */

    String getInputType();

    /**
     * This method is used to get the type of output that can go out a BigData component DOC rdubois Comment method
     * "getOutputType".
     * 
     * @return
     */

    String getOutputType();

    /**
     * This method is used to define if a component generates a Spark Action.
     * 
     * @return a boolean which defines if the component generates a Spark Action.
     */

    boolean isSparkAction();

    String getPartitioning();

    boolean isSupportDbType();

    boolean isAllowedPropagated();

    String getTemplateFolder();

    String getTemplateNamePrefix();
}
