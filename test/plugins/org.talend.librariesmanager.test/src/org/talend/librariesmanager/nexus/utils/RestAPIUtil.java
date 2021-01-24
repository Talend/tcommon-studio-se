package org.talend.librariesmanager.nexus.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RestAPIUtil {
	public static String[] doRequest(String uri, String httpmethod, String user, String password, String data)
			throws Exception {
		String[] ret = new String[2];
		StringBuffer response = new StringBuffer();
		URL url = new URL(uri);
		String auth = user + ":" + password;
		try {
			byte[] encodedAuth = Base64.getEncoder().encode((auth.getBytes(StandardCharsets.UTF_8)));
			String authHeaderValue = "Basic " + new String(encodedAuth);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization", authHeaderValue);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod(httpmethod);
			if (data != null) {
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.connect();
				try (OutputStreamWriter streamWriter = new OutputStreamWriter(conn.getOutputStream());) {
					streamWriter.write(data);
					streamWriter.flush();
				}
			}
			int responsecode = conn.getResponseCode();
			debug(responsecode + " is returned for " + httpmethod + " on the endpoint:" + uri);
			ret[0] = Integer.toString(responsecode);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
			}
			ret[1] = response.toString();
			conn.disconnect();
		} catch (Exception ex) {
			debug(ex.getMessage());
			throw ex;
		}
		return ret;
	}
	
	private static void debug(String message) {
	    System.out.println("[DEBUG]:" + message);
    }

}
