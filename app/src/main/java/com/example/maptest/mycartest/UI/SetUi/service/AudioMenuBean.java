package com.example.maptest.mycartest.UI.SetUi.service;

/**
 * Created by ${Author} on 2018/7/18.
 * Use to
 */

public class AudioMenuBean {
    private String menuName;
    private boolean menuSelect = false;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public boolean isMenuSelect() {
        return menuSelect;
    }

    public void setMenuSelect(boolean menuSelect) {
        this.menuSelect = menuSelect;
    }

    public AudioMenuBean(String menuName, boolean menuSelect) {
        this.menuName = menuName;
        this.menuSelect = menuSelect;
    }
}
