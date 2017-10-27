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
package org.talend.core.model.genhtml;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.utils.ResourceModelHelper;
import org.talend.repository.ProjectManager;

/**
 * Utility class for generating HTML file.
 * 
 */
public class HTMLDocUtils {

    /**
     * Strings handler.
     * 
     * @param str <code>String</code>
     * @return string
     */
    public static String checkString(String str) {
        if (str == null) {
            return ""; //$NON-NLS-1$
        }
        return str;
    }

    /**
     * Dates handler.
     * 
     * @param date
     * @return
     */
    public static String checkDate(Date date) {
        if (date == null) {
            return ""; //$NON-NLS-1$
        }
        DateFormat formatter = DateFormat.getDateTimeInstance();
        return formatter.format(date);
    }

    /**
     * Gets the temporary folder.
     * 
     * @return a string representing temporary folder
     */
    public static String getTmpFolder() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject physProject;
        String tmpFolder = System.getProperty("user.dir"); //$NON-NLS-1$
        try {
            physProject = ResourceModelHelper.getProject(project);
            tmpFolder = physProject.getFolder("temp").getLocation().toPortableString(); //$NON-NLS-1$
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        tmpFolder = tmpFolder + File.separatorChar + IHTMLDocConstants.TEMP_FOLDER_NAME;
        File file = new File(tmpFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return tmpFolder;
    }

    /**
     * 
     * cli Comment method "deleteTempFiles".
     */
    public static void deleteTempFiles() {
        String tmpFold = HTMLDocUtils.getTmpFolder();
        File dir = new File(tmpFold);
        if (dir.exists()) {
            deleteDirectory(dir);
        }
    }

    /**
     * This method is used for deleting a directory.
     * 
     * @param dir
     */
    private static void deleteDirectory(File dir) {
        File[] entries = dir.listFiles();
        for (File file : entries) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                // System.out.println("" + entries[i].delete() + " *** " + entries[i]);
                file.delete();
            }
        }
        dir.delete();
    }

    /**
     * Checks if inputed list is null or length is 0.
     * 
     * @param tables
     */
    public static boolean checkList(List tables) {
        boolean isValid = (tables != null && tables.size() > 0);
        return isValid;
    }

    public static String getPreviewPicPath(INode node) {
        String previewImagePath = ""; //$NON-NLS-1$
        previewImagePath = getPreviewImagePath(node.getElementParameters());
        if (previewImagePath == null || previewImagePath.length() == 0) {
            return previewImagePath;
        }
        return IHTMLDocConstants.PICTUREFOLDERPATH + previewImagePath;
    }

    /**
     * Gets tMap's preview image path base on a list of <code>IElementParameter</code>
     * 
     * @param elementParameters
     * @return
     */
    private static String getPreviewImagePath(List<? extends IElementParameter> elementParameters) {

        for (IElementParameter parameter : elementParameters) {
            IElementParameter type = parameter;
            if (type.getName().equals("PREVIEW")) { //$NON-NLS-1$
                return type.getValue().toString();
            }
        }
        return ""; //$NON-NLS-1$
    }
}
