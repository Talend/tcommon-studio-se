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
package org.talend.commons.runtime.utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 */
public class StreamCopier {

  public static void main(String[] args) {

    try {
      copy(System.in, System.out);
    }
    catch (IOException e) {
      System.err.println(e);
    }
    
  }

  public static void copy(InputStream in, OutputStream out) 
   throws IOException {

    // do not allow other threads to read from the
    // input or write to the output while copying is
    // taking place
    
    synchronized (in) {
      synchronized (out) {

        byte[] buffer = new byte[256];
        while (true) {
          int bytesRead = in.read(buffer);
          if (bytesRead == -1) break;
          out.write(buffer, 0, bytesRead);
        }
      }
    }
  }

}
