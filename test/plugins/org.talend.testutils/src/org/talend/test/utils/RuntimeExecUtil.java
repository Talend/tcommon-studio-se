package org.talend.test.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RuntimeExecUtil {

    public static void exec(String command, String execDir) {
        
        if (command == null || command.trim().equals("")) {
            return;
        }

        try {
            String osName = System.getProperty("os.name");
            String[] cmd = new String[3];
            if (osName.startsWith("Windows")) {
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = command;
            } else if (osName.startsWith("Mac")) {
                // FIXME
                return;
            } else {
                cmd[0] = "/bin/sh";
                cmd[1] = "/C";
                cmd[2] = command;
            }

            Runtime rt = Runtime.getRuntime();
            System.out.println("Exec " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
            Process proc = rt.exec(cmd, null, new File(execDir));
            // any error message
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

class StreamGobbler extends Thread {

    private InputStream in;

    private String type;

    StreamGobbler(InputStream in, String type) {
        this.in = in;
        this.type = type;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null)
                System.out.println(type + ">" + line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
