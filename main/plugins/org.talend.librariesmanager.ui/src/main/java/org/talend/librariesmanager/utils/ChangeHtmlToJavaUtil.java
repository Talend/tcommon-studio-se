package org.talend.librariesmanager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ChangeHtmlToJavaUtil {

    private static final String LIBRARY_DATA_LIBRARY = "library";

    private static final String LIBRARY_DATA_LICENSES = "licenses";

    private static final String LIBRARY_DATA_NAME = "name";

    private static final String LIBRARY_DATA_URL = "url";

    private static final String LICENSE_UNKNOWN = "UNKNOWN";

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        addLineBreakMark("D:\\Apache License Version 2.txt");
        // JSONArray resolveIndexfileToJSONArray = resolveIndexfileToJSONArray("D:\\library_data（下载前）.index");
        // Map<String, String> map = new HashMap<String, String>();
        // putLicenseInfoIntoMap(map, resolveIndexfileToJSONArray);
    }

    public static void addLineBreakMark(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        BufferedReader br = null;
        FileWriter fw = null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\\n");
            }
            String fileName = file.getName();
            String newFileName = getFileNameNoEx(fileName) + "_addLineBreak" + "." + getExtensionName(fileName);
            fw = new FileWriter(new File(file.getParent() + newFileName));
            String s = sb.toString().replaceAll("\"", "\\\"");
            System.out.print(s);
            fw.write(sb.toString().replaceAll("\"", "\\\\\""));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    private static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;

    }

    public static JSONArray resolveIndexfileToJSONArray(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        BufferedReader br = null;
        JSONArray jsonArray = null;
        JSONObject obj = null;
        try {
            if (file.exists()) {
                jsonArray = new JSONArray();
                obj = new JSONObject();
                br = new BufferedReader(new FileReader(file));
                StringBuffer buffer = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                obj = JSONObject.fromObject(buffer.toString());
                jsonArray = obj.getJSONArray(LIBRARY_DATA_LIBRARY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static void putLicenseInfoIntoMap(Map map, JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.size() == 0 || map == null) {
            return;
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter("D:\\license-url.txt");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONArray licenseList = obj.getJSONArray(LIBRARY_DATA_LICENSES);
                if (licenseList != null && licenseList.size() > 0) {
                    for (int j = 0; j < licenseList.size(); j++) {
                        JSONObject license = licenseList.getJSONObject(j);
                        String licenseName = license.getString(LIBRARY_DATA_NAME);
                        System.out.println(obj.getString("mvnUrl") + "============== " + i);
                        if ("mvn:net.iharder/base64/2.3.8/jar".equals(obj.getString("mvnUrl"))) {
                            // this one has license info but no url
                            continue;
                        }
                        if (LICENSE_UNKNOWN.equals(licenseName)) {
                            continue;
                        }
                        String licenseUrl = license.getString(LIBRARY_DATA_URL);
                        if (map.get(licenseName) == null) {
                            map.put(licenseName, licenseUrl);
                            fw.append(licenseName + " ; " + licenseUrl + "\r\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
