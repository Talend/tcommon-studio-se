package org.talend.core.ui.documentation.generation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.items.importexport.handlers.ImportExportHandlersManager;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;
import org.talend.repository.items.importexport.ui.managers.FileResourcesUnityManager;
import org.talend.repository.items.importexport.ui.managers.ResourcesManagerFactory;

public class InternalNodeComponentHandlerTest {

    @Test
    public void testGenerateComponentInfo() throws Exception {
        ProcessItem processItem = null;
        URL testJobURL = FileLocator.find(Platform.getBundle("org.talend.core.ui.test"), new Path("/resources/testViewDoc.zip"),
                null);
        if (testJobURL != null) {
            testJobURL = FileLocator.toFileURL(testJobURL);
        }
        File srcFile = new File(testJobURL.getFile());
        ImportExportHandlersManager importManager = new ImportExportHandlersManager();
        FileResourcesUnityManager fileUnityManager = ResourcesManagerFactory.getInstance().createFileUnityManager(srcFile);
        ResourcesManager resManager = fileUnityManager.doUnify();
        List<ImportItem> projectRecords = importManager.populateImportingItems(resManager, true, new NullProgressMonitor());
        assertTrue(projectRecords.size() > 0);
        importManager.importItemRecords(new NullProgressMonitor(), resManager, projectRecords, true,
                projectRecords.toArray(new ImportItem[0]), null, false);

        IRepositoryViewObject obj = ProxyRepositoryFactory.getInstance().getLastVersion("_bFW5AAU9Eeagf6XEimuCGw");
        Item item = obj.getProperty().getItem();
        assertTrue(item instanceof ProcessItem);
        processItem = (ProcessItem) item;

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    IDesignerCoreService.class);
            List<Map> map = service.getMaps();
            IProcess process = service.getProcessFromProcessItem(processItem);
            Document dom = new DOMDocument();
            Element element = dom.addElement("test");
            List allComponentsList = process.getGraphicalNodes();
            InternalNodeComponentHandler handler = new InternalNodeComponentHandler(new HashMap<String, String>(), element,
                    allComponentsList, new HashMap<String, List>(), new HashMap<String, List>(), service, map.get(0), map.get(1),
                    new HashMap<String, Object>());
            handler.generateComponentInfo();
            Element component = (Element) element.content().get(0);
            for (int i = 0; i < component.content().size(); i++) {
                Element params = (Element) component.content().get(i);
                if (params.getName().equals("parameters")) {
                    List paramList = params.content();
                    for (int j = 0; j < paramList.size(); j++) {
                        Element param = (Element) paramList.get(j);
                        String name = param.attributeValue("name");
                        String value = param.attributeValue("value");
                        assertFalse(name.startsWith("!!!") || name.endsWith("!!!")); // like !!!NOTE.NAME!!!
                        if (name.equals("Property Type") || name.equals("Schema")) {
                            assertTrue(value.equals("Built-In"));
                        }
                    }
                }
            }
        }

    }

}
