package com.example.maptest.mycartest.test;

/**
 * Created by ${Author} on 2018/1/24.
 * Use to
 */

public class CmdInfo {
    private String a;
    private String b;
    private String[] c;

    public CmdInfo() {
    }

    public CmdInfo(String var1, String var2, String[] var3) {
        this.a = var1;
        this.b = var2;
        this.c = var3;
    }

    public String getCtpId() {
        return this.a;
    }

    public void setCtpId(String var1) {
        this.a = var1;
    }

    public String getCommand() {
        return this.b;
    }

    public void setCommand(String var1) {
        this.b = var1;
    }

    public String[] getCmdParams() {
        return this.c;
    }

    public void setCmdParams(String[] var1) {
        this.c = var1;
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder(128);
        var1.append('{');
        var1.append("ctpId:");
        var1.append(this.a);
        var1.append(',');
        var1.append("command:");
        var1.append(this.b);
        if(this.c != null && this.c.length > 0) {
            var1.append(',');
            var1.append("cmdParams:");
            String[] var2 = this.c;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String var5 = var2[var4];
                var1.append(' ');
                var1.append(var5);
            }
        }

        var1.append('}');
        return var1.toString();
    }
}
