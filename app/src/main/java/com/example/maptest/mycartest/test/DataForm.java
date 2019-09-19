package com.example.maptest.mycartest.test;

import android.text.TextUtils;

/**
 * Created by ${Author} on 2018/1/24.
 * Use to
 */

public class DataForm {
    private String a;
    private String b;
    private String c;

    public DataForm() {
    }

    public String getId() {
        return this.a;
    }

    public void setId(String var1) {
        this.a = var1;
    }

    public String getCmd() {
        return this.b;
    }

    public void setCmd(String var1) {
        this.b = var1;
    }

    public String getParams() {
        return this.c;
    }

    public void setParams(String var1) {
        this.c = var1;
    }

    public String toString() {
        String var1 = "";
        if(!TextUtils.isEmpty(this.a)) {
            var1 = "id : " + this.a + "\n";
        }

        if(!TextUtils.isEmpty(this.b)) {
            var1 = var1 + "cmd : " + this.b + "\n";
        }

        if(!TextUtils.isEmpty(this.c)) {
            var1 = var1 + "params : " + this.c;
        }

        return var1;
    }
}
