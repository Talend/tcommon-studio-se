// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.ui.setting.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.preference.PreferenceStore;
import org.talend.designer.maven.template.MavenTemplateManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryMavenSettingStore extends PreferenceStore {

    private final String defaultKey;

    private final IFile file;

    public RepositoryMavenSettingStore(String key, IFile file) {
        super();
        this.defaultKey = key;
        this.file = file;

        this.setFilename(file.getLocation().toString());
    }

    public IFile getFile() {
        return file;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceStore#load()
     */
    @Override
    public void load() throws IOException {
        File file2 = getFile().getLocation().toFile();
        if (!file2.exists()) {
            throw new FileNotFoundException(file2.toString());
        }
        super.load();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceStore#load(java.io.InputStream)
     */
    @Override
    public void load(InputStream in) throws IOException {
        String content = MavenTemplateManager.getContentFromInputStream(in);
        this.setValue(defaultKey, content);
        setDirty(false);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceStore#save()
     */
    @Override
    public void save() throws IOException {
        File file2 = getFile().getLocation().toFile();
        File parentFile = file2.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        super.save();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceStore#save(java.io.OutputStream, java.lang.String)
     */
    @Override
    public void save(OutputStream out, String header) throws IOException {
        String content = this.getString(defaultKey);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(out);
            writer.write(content);
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        setDirty(false);
    }

    private void setDirty(boolean dirty) {
        try {
            Field dirtyField = PreferenceStore.class.getDeclaredField("dirty"); //$NON-NLS-1$
            dirtyField.setAccessible(true);
            dirtyField.set(this, dirty);
        } catch (NoSuchFieldException e) {
            //
        } catch (SecurityException e) {
            //
        } catch (IllegalArgumentException e) {
            //
        } catch (IllegalAccessException e) {
            //
        }
    }
}
