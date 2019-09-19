package com.example.maptest.mycartest.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.example.maptest.mycartest.Utils.BaseActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.utils.Log;

/**
 * Created by ${Author} on 2019/4/1.
 * Use to 微信支付
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx69735e61217fc6ce");
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "支付返回, errCode = " + baseReq.toString());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d(TAG, "baseResp支付返回, errCode = " + baseResp.errCode + " ; baseResp.errStr :" + baseResp.errStr + " baseResp.openId;" + baseResp.openId
        + " ;baseResp.transaction: " + baseResp.transaction + " ; baseResp.checkArgs:" + baseResp.checkArgs());
        Log.d(TAG, "onPayFinish, errCode = " + baseResp.errCode);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0) {   //我在这里，多少钱
                finish();
            } else if (baseResp.errCode == -2) {
                Toast.makeText(this, "您已取消付款!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
