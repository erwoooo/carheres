package com.example.maptest.mycartest.wxapi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.pay.NetWorkFactory;
import com.example.maptest.mycartest.pay.OrederSendInfo;
import com.example.maptest.mycartest.pay.PrepayIdInfo;
import com.example.maptest.mycartest.pay.WXpayUtils;
import com.example.maptest.mycartest.pay.test.QueryOrderRequest;
import com.thoughtworks.xstream.XStream;

import java.text.SimpleDateFormat;
import java.util.Date;


import static com.example.maptest.mycartest.Utils.AppCons.APP_ID;
import static com.example.maptest.mycartest.Utils.AppCons.MCH_ID;


/**
 * Created by ${Author} on 2019/4/4.
 * Use to
 */

public class TestPayWxActivity extends BaseActivity {
    private Button button_sc,button_zf,button_tk;
    private TextView text_data,text_dddata,text_tkdata;
    PrepayIdInfo bean;
    private String out_trade_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paywx);
        text_dddata = (TextView) findViewById(R.id.text_dddata);
        text_tkdata = (TextView) findViewById(R.id.text_tkdata);
        button_sc = (Button) findViewById(R.id.button_sc);
        button_zf = (Button) findViewById(R.id.button_zf);
        text_data = (TextView) findViewById(R.id.text_data);
        button_tk = (Button) findViewById(R.id.button_tk);
        button_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = new Date();
                System.out.println(d);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                String dateNowStr = sdf.format(d);
                System.out.println("格式化后的日期：" + dateNowStr);

                OrederSendInfo sendInfo = new OrederSendInfo(APP_ID,MCH_ID, WXpayUtils.genNonceStr(),"鹅豆-旅游2",dateNowStr + "866551032690047","1","127.0.0.1","http://120.79.104.78:8380/Ch_manage_controller/wechat/paynotify","APP");
                text_data.setText(sendInfo.toString());
                out_trade_no = dateNowStr;
                NetWorkFactory.UnfiedOrder(sendInfo, new NetWorkFactory.Listerner() {
                    @Override
                    public void Success(String data) {
                        Toast.makeText(getApplicationContext(),"生成预支付Id成功",Toast.LENGTH_LONG).show();
                        Log.e("生成预支付Id成功data",data);
                        XStream stream = new XStream();
                        stream.processAnnotations(PrepayIdInfo.class);
                        bean = (PrepayIdInfo) stream.fromXML(data);
                        text_dddata.setText(bean.toString());
                    }
                    @Override
                    public void Faiulre(String data) {
                        Log.e("生成失败",data);
                    }
                });
            }
        });


        button_zf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("预订单ID",bean.toString());
                WXpayUtils.Pay(bean.getPrepay_id());
            }
        });

        button_tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryOrderRequest sendInfo = new QueryOrderRequest();
                sendInfo.setAppid(APP_ID);
                sendInfo.setMch_id(MCH_ID);
                sendInfo.setOut_trade_no("20190417171133");
                sendInfo.setNonce_str(WXpayUtils.genNonceStr());
                NetWorkFactory.qUERRYOrder(sendInfo, new NetWorkFactory.Listerner() {
                    @Override
                    public void Success(String data) {
                        Toast.makeText(getApplicationContext(),"生成预支付Id成功",Toast.LENGTH_LONG).show();
                        Log.e("生成预支付Id成功data",data);
                        XStream stream = new XStream();
                        stream.processAnnotations(PrepayIdInfo.class);
                        bean = (PrepayIdInfo) stream.fromXML(data);
                        text_dddata.setText(bean.toString());

                    }

                    @Override
                    public void Faiulre(String data) {
                        Log.e("生成失败",data);
                    }
                });
            }
        });
    }
}
