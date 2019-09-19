package com.example.maptest.mycartest.pay;


import android.widget.Toast;


import com.example.maptest.mycartest.SwipRecycleView.App;
import com.example.maptest.mycartest.pay.test.QueryOrderRequest;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


import static com.example.maptest.mycartest.Utils.AppCons.API_KEY;
import static com.example.maptest.mycartest.Utils.AppCons.APP_ID;
import static com.example.maptest.mycartest.Utils.AppCons.MCH_ID;

/**
 * Created by xmg on 2016/12/5.
 */

public class WXpayUtils {

    private static IWXAPI iwxapi;
    private static PayReq req;

    public static IWXAPI getWXAPI(){
        if (iwxapi == null){
            //通过WXAPIFactory创建IWAPI实例
            iwxapi = WXAPIFactory.createWXAPI(App.getContextObject(),APP_ID, false);
            req = new PayReq();
            //将应用的appid注册到微信
            iwxapi.registerApp(APP_ID);
        }
        return iwxapi;
    }

    //生成随机字符串
    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    //获得时间戳
    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    //生成预支付随机签名
    public static String genSign(OrederSendInfo info) {
        StringBuffer sb = new StringBuffer(info.toString());
        if (API_KEY.equals("")){
            Toast.makeText(App.getContextObject(),"APP_ID为空",Toast.LENGTH_LONG).show();
        }
        //拼接密钥
        sb.append("key=");
        sb.append(API_KEY);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());

        return appSign;
    }

    //生成预支付随机签名
    public static String genSign(QueryOrderRequest info) {
        StringBuffer sb = new StringBuffer(info.toString());
        if (API_KEY.equals("")){
            Toast.makeText(App.getContextObject(),"APP_ID为空",Toast.LENGTH_LONG).show();
        }
        //拼接密钥
        sb.append("key=");
        sb.append(API_KEY);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());

        return appSign;
    }

    //生成支付随机签名
    private static String genAppSign(List<OkHttpUtils.Param> params){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).key);
            sb.append('=');
            sb.append(params.get(i).value);
            sb.append('&');
        }
        //拼接密钥
        sb.append("key=");
        sb.append(API_KEY);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign.toUpperCase();
    }

    //生成支付参数
    private static void genPayReq(String prepayid) {
        req.appId = APP_ID;
        req.partnerId = MCH_ID;
        req.prepayId = prepayid;
        req.packageValue = "Sign=" + prepayid;
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<OkHttpUtils.Param> signParams = new LinkedList<OkHttpUtils.Param>();
        signParams.add(new OkHttpUtils.Param("appid", req.appId));
        signParams.add(new OkHttpUtils.Param("noncestr", req.nonceStr));
        signParams.add(new OkHttpUtils.Param("package", req.packageValue));
        signParams.add(new OkHttpUtils.Param("partnerid", req.partnerId));
        signParams.add(new OkHttpUtils.Param("prepayid", req.prepayId));
        signParams.add(new OkHttpUtils.Param("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);
    }

    public static void Pay(String prepayid){
        if (judgeCanGo()){
            genPayReq(prepayid);
            iwxapi.registerApp(APP_ID);
            iwxapi.sendReq(req);
        }
    }

    private static boolean judgeCanGo(){
        getWXAPI();
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(App.getContextObject(), "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return false;
        } else if (iwxapi.getWXAppSupportAPI()< Build.PAY_SUPPORTED_SDK_INT) {
            Toast.makeText(App.getContextObject(), "请先更新微信应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
