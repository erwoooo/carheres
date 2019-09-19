package com.example.maptest.mycartest.Utils;


import com.example.maptest.mycartest.New.LocationListBean;

/**
 * Created by ${Author} on 2017/3/18.
 * Use to
 */

public class EventIntentUtil {
    private LocationListBean recCarBean;

    public EventIntentUtil(LocationListBean recCarBean) {
        this.recCarBean = recCarBean;
    }

    public LocationListBean getRecCarBean() {
        return recCarBean;
    }
}
