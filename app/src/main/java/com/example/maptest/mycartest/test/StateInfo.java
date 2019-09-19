package com.example.maptest.mycartest.test;

import java.io.Serializable;

/**
 * Created by ${Author} on 2018/1/24.
 * Use to
 */

public class StateInfo implements Serializable {

    private String a = "-404";
    private String[] b = null;

    public StateInfo() {
    }

    public String getCmdNumber() {
        return this.a;
    }

    public void setCmdNumber(String var1) {
        this.a = var1;
    }

    public String[] getParam() {
        return this.b;
    }

    public void setParam(String[] var1) {
        this.b = var1;
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        String[] var2 = this.b;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            var1.append(var5).append(" ");
        }

        return "cmdNumber=" + this.a + ", content=" + var1.toString().trim();
    }

}
