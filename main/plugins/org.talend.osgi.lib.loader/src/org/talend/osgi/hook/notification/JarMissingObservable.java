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
package org.talend.osgi.hook.notification;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.osgi.storage.BundleInfo.Generation;
import org.talend.osgi.hook.URIUtil;

/**
 * This observable is registered as an OSGI service and can be used to register Observers by clients that whishes to be
 * notified when a jar is missing in a "org.talend.*" bundle. the notification is done in a synchronous manner. The
 * notification is done with a JarMissingEvent parameter. created by sgandon on 12 sept. 2013 Detailled comment
 * 
 */
public class JarMissingObservable extends Observable {

    public final ReentrantLock prenventNotificationLock = new ReentrantLock();

    public static class JarMissingEvent {

        private final String jarName;

        private final String expectedLibFolder;

        private Generation   generation;

        /**
         * DOC sgandon JarMissingObservable.JarMissingEvent constructor comment.
         */
        public JarMissingEvent(String jarName, Generation generation, String expectedLibFolder) {
            this.jarName = jarName;
            this.generation = generation;
            this.expectedLibFolder = expectedLibFolder;

        }

        /**
         * return the name of the missing jar.
         * 
         * @return the jarName
         */
        public String getJarName() {
            return this.jarName;
        }

        /**
         * return the name of the bundle that has a missing jar.
         *
         * @return the bundleName
         */
        public String getBundleSymbolicName() {
            return generation.getRevision().getSymbolicName();
        }

        /**
         * return the folder path where the jar is excepted to be found
         * 
         * @return the expectedLibFolder
         */
        public String getExpectedLibFolder() {
            return this.expectedLibFolder;
        }

        /**
         * return the OSGI Bundle Id
         * */
        public long getBundleId() {
            return generation.getBundleInfo().getBundleId();
        }

        public URI getMvnUri() throws URISyntaxException {
            return URIUtil.getMvnUri( jarName, generation, false );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    @Override
    public void notifyObservers(Object arg) {
        if (!prenventNotificationLock.isLocked()) {
            super.setChanged();// this is required for notification to actually really happend.
            super.notifyObservers( arg );
        }
    }
}
