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
package org.talend.core.service;

import java.util.List;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderRenderer;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.IService;

/**
 * created by zshen on Aug 16, 2013 Detailled comment
 * 
 */
public interface IMatchRuleUIService extends IService {

    /**
     * 
     * DOC zshen Comment method "getImageByName".
     * 
     * @param imageName can be found from {@link org.talend.dataquality.record.linkage.ui.composite.utils.ImageLib}
     * @return
     */
    public Image getImageByName(String imageName);

    /**
     * 
     * get image which is like "Plus".
     * 
     * 
     * @return
     */
    public Image getAddImage();

    /**
     * 
     * create MatchRuleCTabFolderRenderer instance
     * 
     * @return
     */
    public CTabFolderRenderer createMatchRuleCTabFolderRenderer(CTabFolder configurationFolder);

    /**
     * 
     * Create match rule table
     * 
     * @return
     */
    public Composite createMatchRuleTable(SashForm parent, List<String> columnNames);

    /**
     * 
     * create match rule chart
     * 
     * @param parent
     * @param viewData the data for grouping result
     * @param columnNames the name of columns
     * @return
     */
    public Composite createMatchRuleDataChart(Composite parent, List<String[]> viewData, List<String> columnNames);

    /**
     * set the the value of count which need to display charts
     * 
     * @param count if the count of someone grouping more of this parameter the data will be display
     */
    public void setNeedDisplayCount(int count, Composite chart);

    /**
     * 
     * refresh the chart of match rule
     * 
     * @param chart
     */
    public void refreshMatchRuleChart(List<String[]> viewData, Composite chart, List<String> columnNames);

    /**
     * do layout again for the chart
     */
    public void layoutMatchRuleChart(Composite chart);

    /**
     * 
     * dispose current chart
     * 
     * @param chart
     */
    public void disposeMatchRuleChart(Composite chart);
}
