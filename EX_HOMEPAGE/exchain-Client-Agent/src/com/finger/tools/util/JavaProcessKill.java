package com.finger.tools.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.finger.tools.util.lang.StrUtil;

public class JavaProcessKill {

    public JavaProcessKill() { }

    public void killJavaProcess(String className) {
        String javaPid = getJavaPid(className);
        System.out.println((new StringBuilder("className: ")).append(className).append(" | PID: "+javaPid).toString());
        terminateJavaPid ( javaPid );
    }

    private String getJavaPid(String className) {
        String result = "";
        String args[] = new String[2];
        args[0] = "jps";
        args[1] = "-V";
        BufferedReader br = null;
        try {
            Process process = Runtime.getRuntime().exec(args);
            br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()));
            String sLine = "";
            while((sLine = br.readLine()) != null) {
                int nIndex = sLine.indexOf(className);
                if(nIndex != -1) {
                    result = sLine.substring(0, nIndex).trim();
                    break;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch(IOException e) { e.printStackTrace(); }

        return result;
    }

    private void terminateJavaPid(String javaPid) {
        if (StrUtil.isNull(javaPid))
            return;

        Runtime runtime = Runtime.getRuntime();
        BufferedReader br = null;
        try {
            String args[];
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                args = new String[4];
                args[0] = "taskkill";
                args[1] = "/PID";
                args[2] = javaPid;
                args[3] = "/F";
            }
            else {
                args = new String[3];
                args[0] = "kill";
                args[1] = "-9";
                args[2] = javaPid;
            }
            Process process = runtime.exec(args);
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String sLine = "";
            while((sLine = br.readLine()) != null)
                System.out.println(Charset.defaultCharset() +" | "+ sLine );
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        runtime.exit(0);
        return;
    }

    public static void main(String args[]) {
        JavaProcessKill kill = new JavaProcessKill();
        kill.killJavaProcess(args[0]);
    }
}