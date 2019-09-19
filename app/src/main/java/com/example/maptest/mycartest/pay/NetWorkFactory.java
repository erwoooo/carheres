package com.example.maptest.mycartest.pay;


import android.util.Log;


import com.example.maptest.mycartest.pay.test.QueryOrderRequest;
import com.thoughtworks.xstream.XStream;

import static com.example.maptest.mycartest.Utils.AppCons.QUERRY_ORDER;
import static com.example.maptest.mycartest.Utils.AppCons.UNIFIED_ORDER;

/**
 * Created by xmg on 2016/12/5.
 */

public class NetWorkFactory {
    public interface Listerner {
        void Success(String data);

        void Faiulre(String data);
    }

    /**
     * 本地模拟 统一下单 生成微信预支付Id 这一步放在服务器端生成
     * @param orederSendInfo
     * @param listerner
     */
    public static void UnfiedOrder(OrederSendInfo orederSendInfo, final Listerner listerner) {

        //生成sign签名
        String sign = WXpayUtils.genSign(orederSendInfo);
        Log.e("生成的签名",sign);
        //生成所需参数，为xml格式
        orederSendInfo.setSign(sign.toUpperCase());
        XStream xstream = new XStream();
        xstream.alias("xml", OrederSendInfo.class);
        Log.e("orederSendInfo",orederSendInfo.toString());
        final String xml = xstream.toXML(orederSendInfo).replaceAll("__","_");

        //调起接口，获取预支付ID
        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                String data = response;
                Log.e("预支付response",response);
                data = data.replaceAll("<!\\[CDATA\\[","").replaceAll("]]>","");
                listerner.Success(data);
            }

            @Override
            public void onFailure(Exception e) {
                listerner.Faiulre(e.toString());
            }
        };
        Log.e("xml",xml);

        OkHttpUtils.post(UNIFIED_ORDER,resultCallback,xml);
    }

    public static void qUERRYOrder(QueryOrderRequest orederSendInfo, final Listerner listerner) {

        //生成sign签名
        String sign = WXpayUtils.genSign(orederSendInfo);
        Log.e("生成的签名",sign);
        //生成所需参数，为xml格式
        orederSendInfo.setSign(sign.toUpperCase());
        XStream xstream = new XStream();
        xstream.alias("xml", OrederSendInfo.class);
        Log.e("查询订单orederSendInfo",orederSendInfo.toString());
        final String xml = xstream.toXML(orederSendInfo).replaceAll("__","_");

        //调起接口，获取预支付ID
        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                String data = response;
                Log.e("查询订单",response);
                data = data.replaceAll("<!\\[CDATA\\[","").replaceAll("]]>","");
                listerner.Success(data);
            }

            @Override
            public void onFailure(Exception e) {
                listerner.Faiulre(e.toString());
            }
        };
        Log.e("查询订单xml",xml);

        OkHttpUtils.post(QUERRY_ORDER,resultCallback,xml);
    }
}
