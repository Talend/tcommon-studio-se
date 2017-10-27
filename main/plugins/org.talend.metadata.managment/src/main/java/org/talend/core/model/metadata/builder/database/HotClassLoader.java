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
package org.talend.core.model.metadata.builder.database;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.talend.commons.exception.ExceptionHandler;

/**
 * 
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class HotClassLoader extends URLClassLoader {

    // Commentted by Marvin Wang on Feb. 4, 2012 for TDI-23833.
    // // qli modified to fix the bug 6281.
    // private static HotClassLoader instance;
    //
    // public static HotClassLoader getInstance() {
    // // bug 17800 fixed
    // // if (instance == null) {
    // instance = new HotClassLoader();
    // // }
    // return instance;
    // }

    public HotClassLoader() {
        super(new URL[0], ClassLoader.getSystemClassLoader());
    }

    public void addPath(String paths) {
        if (paths == null || paths.length() <= 0) {
            return;
        }
        String separator = System.getProperty("path.separator"); //$NON-NLS-1$
        String[] pathToAdds = paths.split(separator);
        for (String pathToAdd2 : pathToAdds) {
            if (pathToAdd2 != null && pathToAdd2.length() > 0) {
                try {
                    File pathToAdd = new File(pathToAdd2).getCanonicalFile();
                    addURL(pathToAdd.toURL());
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

    // public static String getClassFile(String name){
    // if(name != null && !"".equals(name))
    // return name.replace(".", "/").concat(".class");
    // return null;
    // }
    //
    // /**
    // * "hive driver"
    // */
    // public Class findClass(String name) throws ClassNotFoundException{
    // Class clazz = null;
    // byte[] data = loadClassData(name);
    //
    // clazz = defineClass(name, data, 0, data.length);
    // URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
    // return clazz;
    // }
    //
    //
    // public byte[] loadClassData(String name) throws ClassNotFoundException{
    // InputStream input = null;
    // ByteArrayOutputStream output = null;
    // byte[] data = null;
    // URL[] urls = this.getURLs();
    // if(urls != null){
    // boolean isFind = false;
    // for(URL url : urls){
    // try {
    // String newDriverName = HotClassLoader.getClassFile(name);
    // URL newURL = new URL("jar:" + url.toString() + "!/"+ newDriverName);
    // input = newURL.openStream();
    // output = new ByteArrayOutputStream();
    // int ch = 0;
    // while((ch = input.read()) != -1){
    // output.write(ch);
    // }
    // data = output.toByteArray();
    // isFind = true;
    // break;
    // } catch (IOException e) {
    // }
    // }
    // if(!isFind)
    // throw new ClassNotFoundException("Can not find " + name + "!");
    // }
    // return data;
    // }

}
