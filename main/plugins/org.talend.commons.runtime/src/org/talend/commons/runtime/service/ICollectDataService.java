package org.talend.commons.runtime.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.CommonExceptionHandler;

public interface ICollectDataService {

    final String KEY_SOURCE = "source";

    //
    final String AMC_FILE_TYPE_USED = "FILE_TYPE_USED";

    final String AMC_DATABASE_TYPE_USED = "DATABASE_TYPE_USED";

    final String AMC_PREVIEW_KEY = "amc.datasource";

    final String AMC_PREVIEW_FILEVALUE = "File";

    final String AMC_PREVIEW_DATABASEVALUE = "Database";

    /**
     * @return json string
     */
    String getCollectedDataJSON();

    Properties getCollectedData();

    public static ICollectDataService getInstance(String from) throws Exception {
        BundleContext bc = FrameworkUtil.getBundle(ICollectDataService.class).getBundleContext();
        Collection<ServiceReference<ICollectDataService>> tacokitServices = Collections.emptyList();
        try {
            tacokitServices = bc.getServiceReferences(ICollectDataService.class, null);
        } catch (InvalidSyntaxException e) {
            CommonExceptionHandler.process(e);
        }

        if (tacokitServices != null) {
            for (ServiceReference<ICollectDataService> sr : tacokitServices) {
                if (from == null || from.equals(sr.getProperty(KEY_SOURCE))) {
                    ICollectDataService tacokitService = bc.getService(sr);
                    if (tacokitService != null) {
                        return tacokitService;
                    }
                }
            }
        }
        return null;
    }
}
