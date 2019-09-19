package com.example.maptest.mycartest.Utils;

import com.example.maptest.mycartest.Bean.BlueOrderBean;
import com.example.maptest.mycartest.Entity.EightOrderBean;
import com.example.maptest.mycartest.Entity.NbDevice;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.New.TrickCountBean;
import com.example.maptest.mycartest.New.TrickListBean;
import com.example.maptest.mycartest.UI.SetUi.service.AvideoBean;
import com.example.maptest.mycartest.UI.SetUi.service.BSJ;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.Offline;
import com.example.maptest.mycartest.test.LoginDataBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Author} on 2017/3/14.
 * Use to  存放接口及bundle传值的key，app接口
 */
public class AppCons {

    public static String xgAccount;

    public static NbDevice mNbDevice;
    // appid
    public static final String APP_ID = "wx3b18f36021a16fd4";// yum

    // 商户号
    public static final String MCH_ID = "1529867971";// yum

    // API密钥，在商户平台设置
    public static final String API_KEY = "DPZlbzkmy6wqgyKSmTxFRJIZyv6vYpmw";// yum

    //微信统一下单接口
    public static final String UNIFIED_ORDER ="https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String QUERRY_ORDER ="https://api.mch.weixin.qq.com/pay/orderquery";
    //微信退款接口
    public static final String REFUND_ORDER  = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static boolean EDIT = false;
    public static String PWD = "";
    public static int versionCode = 55;
    public static String versionNum = "1.8.2";
    public static String TELL = "";
    public static List<AvideoBean>integerList = new ArrayList<>();
    public static int ETSELECT = 0;
    public static boolean SOCKET_CUTOFF = false;
    public static String HEART_BEAT_JSON = "";
    public static String ID = "";
    public static int trackType = 0;
    public static String addType = "online";
    public static String locationType = "Bds";
    public static K100B ORDERBEN;
    public static BSJ GWORDER;
    public static EightOrderBean ETORDER;
    public static Offline LXORDER;
    public static boolean WARN_TYPE = false;
    public static String WARN_TEMID = "";
    public static String COOKIE = "";
    public static int WARNTYPE = 0;
    public static long sTime;
    public static long eTime;
    public static TrickCountBean trickCountBean;
    public static List<TrickListBean>trickList = new ArrayList<>();
    public static List<LocationListBean>tyreList = new ArrayList<>();
    public static List<LocationListBean>locationList = new ArrayList<>();
    public static LocationListBean locationListBean;
    public static LocationListBean devicelistBean;
    public static LoginDataBean loginDataBean;
    public static String ADDRESS = "";
    public static Map<Integer,String> ACCOUNT_MAP = new HashMap<>();
    public static String ACCOUNT ="体验账号";
    public static boolean AGENT_SCOLL = true;
    public static  int AGENTID_SELECT = 1;
    public static  int AGENTID_USED = 1;
    public static  String AGENTID_SAVE = "6";
    public static final String TEST_DATA = "data";
    public static final String TEST_USE = "use";
    public static final String TEST_INT = "int";
    public static BlueOrderBean orderBean = null;
    public static boolean TEST_LG = true;
    public static boolean TEST_UPDATE = true;
    public static  final  String TEST_WARN = "warn";
    public static  final  String TEST_SEV= "sev";
    public static  final  String TEST_TYR= "tyr";
    public static final String URL = "http://app.carhere.net/appGetCommandInfo";
    public static final String QURRYURL = "http://www.stc.gov.cn/YLB/XXCX/JDCCX/201310/t20131030_443.htm";
    public static final String LIXIANURL = "http://app.carhere.net/appFindWireless";
    public static final String COMMONURL = "http://api.carhere.net/Ch_manage_controller/";
    public static final String ORDERURL = "http://order.carhere.net:8008/";
//    public static final String ORDERURL = "http://cmd.carhere.net/";
    public static final String POSTURL = "http://120.79.104.78:8380/";
//    public static final String COMMONURL = "http://192.168.0.121:8080/Ch_manage_controller/";
    public static final String DOWLOADURL = "http://api.carhere.net/app/";

}
