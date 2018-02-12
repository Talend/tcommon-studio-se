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
package org.talend.core.ui.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.services.IDesignerCoreUIService;
import org.talend.designer.core.IPaletteFilter;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * TODO SML/NRO Move into org.talend.core ?
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ComponentPaletteUtilities {

    private static final String FAMILY_HIER_SEPARATOR = "/"; //$NON-NLS-1$

    private static final String FAMILY_SPEARATOR = "--FAMILY--"; //$NON-NLS-1$

    private static PaletteRoot paletteRoot;

    private static List<PaletteEntry> extraPaletteEntry;

    private static boolean skipUpdatePalette;

    private static boolean faState = false;

    // public static int histate = 0;

    private static boolean jobletFlag = false;

    private static boolean sparkJobletFlag = false;

    private static boolean sparkStreamingJobletFlag = false;

    public static int histate = 0;

    public static PaletteRoot getPaletteRoot() {
        if (paletteRoot == null) {
            paletteRoot = CoreUIPlugin.getDefault().getDesignerCoreUIService().createEmptyPalette();
        }
        return paletteRoot;
    }

    private static void setExtraEntryVisible(boolean visible) {
        jobletFlag = visible;
        if (extraPaletteEntry != null) {
            for (PaletteEntry entry : extraPaletteEntry) {
                entry.setVisible(visible);
            }
        }
    }

    private static void setExtraEntryVisible(ERepositoryObjectType itemType) {
        boolean isJoblet = false;
        jobletFlag = (itemType == ERepositoryObjectType.JOBLET);
        sparkJobletFlag = (itemType == ERepositoryObjectType.SPARK_JOBLET);
        sparkStreamingJobletFlag = (itemType == ERepositoryObjectType.SPARK_STREAMING_JOBLET);
        isJoblet = (itemType == ERepositoryObjectType.JOBLET || itemType == ERepositoryObjectType.SPARK_JOBLET || itemType == ERepositoryObjectType.SPARK_STREAMING_JOBLET);
        if (extraPaletteEntry != null) {
            for (PaletteEntry entry : extraPaletteEntry) {
                if (entry instanceof TalendCreationToolEntry) {
                    ERepositoryObjectType type = ((TalendCreationToolEntry) entry).getRepositoryObjectType();
                    if (type != null) {
                        entry.setVisible(type == itemType);
                    }
                } else {
                    entry.setVisible(isJoblet);
                }
            }
        }
    }

    public static void setSkipUpdatePalette(boolean skipUpdatePalette) {
        ComponentPaletteUtilities.skipUpdatePalette = skipUpdatePalette;
    }

    public static void updatePalette() {
        if (jobletFlag == true) {
            setExtraEntryVisible(ERepositoryObjectType.JOBLET);
        }
        if (sparkJobletFlag == true) {
            setExtraEntryVisible(ERepositoryObjectType.SPARK_JOBLET);
        }
        if (sparkStreamingJobletFlag == true) {
            setExtraEntryVisible(ERepositoryObjectType.SPARK_STREAMING_JOBLET);
        }
        if (skipUpdatePalette) {
            return;
        }
        DisplayUtils.getDisplay().syncExec(new Runnable() {

            @Override
            public void run() {
                IComponentsFactory components = ComponentsFactoryProvider.getInstance();

                final IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
                if (paletteRoot != null) {
                    List oldRoots = new ArrayList(paletteRoot.getChildren());

                    for (Iterator it = oldRoots.iterator(); it.hasNext();) {
                        Object obj = it.next();
                        if (obj instanceof TalendPaletteGroup) {
                            continue;
                        }
                        it.remove();
                    }
                    paletteRoot.setChildren(oldRoots);
                    paletteRoot = designerCoreUIService.createPalette(components, paletteRoot);

                } else {
                    paletteRoot = designerCoreUIService.createPalette(components);
                }

                if (extraPaletteEntry == null || extraPaletteEntry.size() == 0) {
                    extraPaletteEntry = designerCoreUIService.createPaletteEtnry();
                }
            }
        });
    }

    public static void updatePalette(final boolean isFavorite) {
        // if (jobletFlag == true) {
        // setExtraEntryVisible(true);
        // }
        faState = isFavorite;
        if (skipUpdatePalette) {
            return;
        }
        // DisplayUtils.getDisplay().asyncExec(new Runnable() {
        //
        // @Override
        // public void run() {
        IComponentsFactory components = ComponentsFactoryProvider.getInstance();
        Display display = DisplayUtils.getDisplay();

        final IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
        if (paletteRoot != null) {
            display.syncExec(new Runnable() {

                @Override
                public void run() {
                    final List oldRoots = new ArrayList(paletteRoot.getChildren());

                    for (Iterator it = oldRoots.iterator(); it.hasNext();) {
                        Object obj = it.next();
                        if (obj instanceof TalendPaletteGroup) {
                            continue;
                        }
                        it.remove();
                    }
                    paletteRoot.setChildren(oldRoots);
                }
            });
            paletteRoot = designerCoreUIService.createPalette(components, paletteRoot, isFavorite);
        } else {
            paletteRoot = designerCoreUIService.createPalette(components, isFavorite);
        }
        if (extraPaletteEntry == null || extraPaletteEntry.size() == 0) {
            display.syncExec(new Runnable() {

                @Override
                public void run() {
                    extraPaletteEntry = designerCoreUIService.createPaletteEtnry();
                }
            });
        }
        // }
        // });
    }

    /**
     * yzhang Comment method "filterPalette".
     * 
     * @param filer
     */
    public static void filterPalette(String filer) {
        CoreUIPlugin.getDefault().getDesignerCoreUIService().setPaletteFilter(filer);
        if (faState) {
            updatePalette(true);
        } else {
            updatePalette(false);
        }

        DisplayUtils.getDisplay().syncExec(new Runnable() {

            @Override
            public void run() {
                markEmptyDrawer(paletteRoot);
                emptyEntry.clear();
                recordEmptyDrawer(paletteRoot);
                removeEmptyDrawer();
            }
        });

    }

    private static List<PaletteEntry> emptyEntry = new ArrayList<PaletteEntry>();

    /**
     * yzhang Comment method "removeEmptyDrawer".
     */
    private static void removeEmptyDrawer() {
        for (PaletteEntry entry : emptyEntry) {
            PaletteContainer container = entry.getParent();
            if (container != null) {
                container.remove(entry);
            }
        }
    }

    /**
     * yzhang Comment method "recordEmptyDrawer".
     * 
     * @param entry
     */
    private static void recordEmptyDrawer(PaletteEntry entry) {
        if (entry instanceof PaletteRoot) {
            List<PaletteEntry> entries = ((PaletteRoot) entry).getChildren();
            for (PaletteEntry paletteEntry : entries) {
                if (paletteEntry instanceof PaletteDrawer) {
                    recordEmptyDrawer(paletteEntry);
                }
            }
        } else if (entry instanceof PaletteDrawer) {
            PaletteDrawer drawer = (PaletteDrawer) entry;
            if (drawer instanceof IPaletteFilter && ((IPaletteFilter) entry).isFiltered()) {
                emptyEntry.add(entry);
            } else {
                List children = drawer.getChildren();
                for (Object obj : children) {
                    recordEmptyDrawer((PaletteEntry) obj);
                }
            }
        }
    }

    /**
     * yzhang Comment method "filterEmptyDrawer".
     * 
     * @param entry
     */
    private static void markEmptyDrawer(PaletteEntry entry) {
        if (entry instanceof PaletteRoot) {
            List<PaletteEntry> entries = ((PaletteRoot) entry).getChildren();
            for (PaletteEntry paletteEntry : entries) {
                if (paletteEntry instanceof PaletteDrawer) {
                    markEmptyDrawer(paletteEntry);
                }
            }
        } else if (entry instanceof PaletteDrawer) {
            PaletteDrawer drawer = (PaletteDrawer) entry;
            for (Object obj : drawer.getChildren()) {
                markEmptyDrawer((PaletteEntry) obj);
            }
            return;
        }
        PaletteEntry parentEntry = entry.getParent();
        if (parentEntry instanceof IPaletteFilter) {
            ((IPaletteFilter) parentEntry).setFiltered(false);
        }
    }

    /**
     * DOC guanglong.du Comment method "updateFromRepositoryType".
     * 
     * @param itemType
     */
    public static void updateFromRepositoryType(ERepositoryObjectType itemType) {
        updatePalette(faState);
        setExtraEntryVisible(itemType);
    }

    public static List<PaletteEntry> getExtraPaletteEntry() {
        return extraPaletteEntry;
    }
}
