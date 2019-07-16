package org.talend.repository.metadata.i18n;

import java.util.ResourceBundle;

import org.talend.commons.i18n.MessagesCore;

public class Messages extends MessagesCore {

    private static final String BUNDLE_NAME = "org.talend.repository.metadata.i18n.messages"; //$NON-NLS-1$

    public static final String PLUGIN_ID = "org.talend.repository.metadata"; //$NON-NLS-1$

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Returns the i18n formatted message for <i>key</i> in the class bundle.
     * 
     * @param key - the key for the desired string
     * @return the string for the given key in the class resource bundle
     * @see MessagesCore#getString(String, ResourceBundle)
     */
    public static String getString(String key) {
        return getString(key, PLUGIN_ID, resourceBundle);
    }

    /**
     * Returns the i18n formatted message for <i>key</i> and <i>args</i> in the specified bundle.
     * 
     * @param key - the key for the desired string
     * @param args - arg to include in the string
     * @return the string for the given key in the given resource bundle
     * @see MessagesCore#getString(String, ResourceBundle, Object[])
     */
    public static String getString(String key, Object... args) {
        return getString(key, PLUGIN_ID, resourceBundle, args);
    }
}
