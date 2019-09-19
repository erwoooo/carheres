//package com.example.maptest.mycartest.test;
//
//import android.os.Handler;
//import android.os.SystemClock;
//import android.text.TextUtils;
//import android.util.Log;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.util.Locale;
//import java.util.concurrent.LinkedBlockingQueue;
//
///**
// * Created by ${Author} on 2018/1/24.
// * Use to
// */
//
//public class CommandHub implements ICommon {
//    private static final String a = CommandHub.class.getSimpleName();
//    private static Socket socket;
//    private static OutputStream outputStream = null;
//    private static volatile CommandHub commandHub = null;
//    private static final Handler handler = new Handler();
//    private static int f = 0;
//    public static final int ERROR_CONNECTION_EXCEPTION = 1;
//    public static final int ERROR_CONNECTION_TIMEOUT = 2;
//    private CommandHub.b g;
//    private CommandHub.a h;
//    private CommandHub.c i;
//    private static boolean j = false;
//    private String k;
//    private static volatile boolean l = false;
//    private static volatile boolean m = false;
//    private static volatile int n = 0;
//    private static String o;
//    private static int SaveAlarmBean;
//    private static CommandHub.OnDeviceListener deviceListener;
//    private static int r = 10000;
//    private static int s = 5;
//
//    public static CommandHub getInstance() {
//        if(commandHub  == null) {
//            Class var0 = CommandHub.class;
//            synchronized(CommandHub.class) {
//                if(commandHub == null) {
//                    commandHub = new CommandHub();
//                }
//            }
//        }
//
//        return commandHub;
//    }
//
//    private CommandHub() {
//    }
//
//    public void setLocalAppVersion(String var1) {
//        this.k = var1;
//    }
//
//    public String getDeviceIP() {
//        return o;
//    }
//
//    public int getDevicePort() {
//        return SaveAlarmBean;
//    }
//
//    public void createClient() {
//        this.createClient("192.168.1.1", 2222);
//    }
//
//    public void createClient(final String var1, final int var2) {
//        if(this.i != null) {
//            if(this.i.b) {
//                this.i.a();
//            }
//
//            this.i = null;
//        }
//
//        if(this.g != null) {
//            this.g.a();
//            this.g = null;
//        }
//
//        if(this.h != null) {
//            this.h.a();
//            this.h = null;
//        }
//
//        if(socket != null) {
//            Log.w(a, "Socket Client is already exist.");
//            (new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        if(CommandHub.socket != null) {
//                            CommandHub.socket.close();
//                        } else {
//                            Log.w(CommandHub.a, "Socket Client has null or been closed.");
//                        }
//                    } catch (IOException var2) {
//                        var2.printStackTrace();
//                    }
//
//                }
//            })).start();
//        }
//
//        Log.i(a, "prepare Socket Client .");
//        Thread var3 = new Thread(new Runnable() {
//            public void run() {
//                int var1x = 0;
//
//                while(true) {
//                    try {
//                        CommandHub.socket = new Socket(var1, var2);
//                    } catch (IOException var4) {
//                        var4.printStackTrace();
//                        ++var1x;
//                        CommandHub.socket = null;
//                        if(var1x >= 5) {
//                            CommandHub.handler.post(new Runnable() {
//                                public void run() {
//                                    if(CommandHub.deviceListener != null) {
//                                        CommandHub.deviceListener.onError(1);
//                                    }
//
//                                }
//                            });
//                            Log.i(CommandHub.a, "ERROR_CONNECTION_EXCEPTION------------mNetworkIsUnreachableCount ：" + var1x);
//                            break;
//                        }
//
//                        Log.e(CommandHub.a, "prepareClient: create socket failure");
//                    }
//
//                    if(CommandHub.socket != null) {
//                        try {
//                            CommandHub.outputStream = CommandHub.socket.getOutputStream();
//                        } catch (IOException var3) {
//                            var3.printStackTrace();
//                        }
//
//                        if(CommandHub.outputStream != null) {
//                            CommandHub.o = var1;
//                            CommandHub.SaveAlarmBean = var2;
//                            if(CommandHub.this.i == null) {
//                                CommandHub.this.i = new CommandHub.c(null);
//                                CommandHub.this.i.b = true;
//                                CommandHub.this.i.start();
//                            }
//
//                            if(CommandHub.this.h == null) {
//                                CommandHub.this.h = new CommandHub.a(null);
//                                CommandHub.this.h.start();
//                            }
//
//                            if(CommandHub.this.g == null) {
//                                Log.i(CommandHub.a, "Create HeartbeatTask success--------------");
//                                CommandHub.this.g = new CommandHub.b(null);
//                                CommandHub.this.g.start();
//                            }
//
//                            CommandHub.this.sendCommand("1", "0029", new String[]{"0", CommandHub.this.k});
//                            CommandHub.this.sendCommand("1", "0005", new String[]{" "});
//                            CommandHub.this.requestStatus("1", "0011");
//                            CommandHub.this.requestStatus("1", "0026");
//                            if(CommandHub.deviceListener != null) {
//                                CommandHub.deviceListener.onConnected();
//                            }
//
//                            Log.i(CommandHub.a, "Create socket success--------------");
//                        } else {
//                            Log.e(CommandHub.a, "prepareClient: mOutputStream is null.");
//                            CommandHub.handler.post(new Runnable() {
//                                public void run() {
//                                    if(CommandHub.deviceListener != null) {
//                                        CommandHub.deviceListener.onError(1);
//                                    }
//
//                                }
//                            });
//                        }
//                        break;
//                    }
//
//                    Log.e(CommandHub.a, "mSocketClient is null.");
//                    SystemClock.sleep(1000L);
//                }
//
//            }
//        });
//        var3.start();
//    }
//
//    public void closeClient() {
//        Thread var1 = new Thread(new Runnable() {
//            public void run() {
//                if(CommandHub.socket != null) {
//                    try {
//                        CommandHub.socket.close();
//                        Log.d(CommandHub.a, "Socket Client is closed");
//                    } catch (IOException var2) {
//                        var2.printStackTrace();
//                    }
//                }
//
//                Log.w(CommandHub.a, "Release SocketClient ............");
//            }
//        });
//        var1.start();
//        socket = null;
//        outputStream = null;
//        if(this.i != null) {
//            if(this.i.b) {
//                this.i.a();
//            }
//
//            this.i = null;
//        }
//
//        if(this.g != null) {
//            this.g.a();
//            this.g = null;
//        }
//
//        if(this.h != null) {
//            this.h.a();
//            this.h = null;
//        }
//
//        deviceListener = null;
//        commandHub = null;
//        handler.removeCallbacksAndMessages((Object)null);
//    }
//
//    public void requestStatus(String var1, String var2) {
//        if(this.i == null) {
//            this.i = new CommandHub.c(null);
//            this.i.b = true;
//            this.i.start();
//        }
//
//        if(!this.i.b) {
//            this.i.b = true;
//        }
//
//        DataForm var3 = new DataForm();
//        var3.setId(var1);
//        var3.setCmd(var2);
//        var3.setParams((String)null);
//        this.i.a(var3);
//    }
//
//    public void sendCommand(String var1, String var2, String... var3) {
//        if(var3 != null && var3.length > 0) {
//            StringBuilder var4 = new StringBuilder();
//            String[] var5 = var3;
//            int var6 = var3.length;
//
//            for(int var7 = 0; var7 < var6; ++var7) {
//                String var8 = var5[var7];
//                var4.append(var8).append(' ');
//            }
//
//            String var9 = var4.toString().trim();
//            if(this.i == null) {
//                this.i = new CommandHub.c(null);
//                this.i.b = true;
//                this.i.start();
//            }
//
//            if(!this.i.b) {
//                this.i.b = true;
//            }
//
//            DataForm var10 = new DataForm();
//            var10.setId(var1);
//            var10.setCmd(var2);
//            var10.setParams(var9);
//            this.i.a(var10);
//        }
//
//    }
//
//    private static synchronized boolean b(String var0, String var1) {
//        if(TextUtils.isEmpty(var1)) {
//            Log.e(a, "data is null ");
//            return false;
//        } else {
//            if(outputStream == null && socket != null) {
//                try {
//                    outputStream = socket.getOutputStream();
//                } catch (IOException var3) {
//                    var3.printStackTrace();
//                }
//            }
//
//            if(outputStream != null) {
//                b(var0);
//
//                try {
//                    byte[] var2 = var1.getBytes();
//                   outputStream.write(var2);
//                    Log.w(a, "=============send[" + var1 + "]");
//                    return true;
//                } catch (IOException var4) {
//                    Log.e(a, "Error: sendData failed");
//                    var4.printStackTrace();
//                }
//            } else {
//                Log.e(a, "====sendData: OutputStream is null.");
//            }
//
//            return false;
//        }
//    }
//
//    public boolean isActive() {
//        try {
//            if(this.i == null) {
//                this.i = new CommandHub.c(null);
//                this.i.b = true;
//                this.i.start();
//            }
//
//            if(!this.i.b) {
//                this.i.b = true;
//            }
//
//            DataForm var1 = new DataForm();
//            var1.setId("9999");
//            var1.setCmd("9993");
//            var1.setParams((String)null);
//            this.i.a(var1);
//        } catch (Exception var2) {
//            var2.printStackTrace();
//        }
//
//        return j;
//    }
//
//    public synchronized void setOnDeviceListener(CommandHub.OnDeviceListener var1) {
//        Log.w(a, "setOnDeviceListener: listener=" + var1);
//        deviceListener = var1;
//    }
//
//    public synchronized void setHeartbeatTimeout(int var1) {
//        s = var1;
//    }
//
//    public int getHeartbeatTimeout() {
//        return s;
//    }
//
//    public synchronized void setHeartbeat(int var1) {
//        r = var1;
//    }
//
//    public int getHearbeat() {
//        return r;
//    }
//
//    boolean a() {
//        return l;
//    }
//
//    boolean b() {
//        return m;
//    }
//
//    int c() {
//        return n;
//    }
//
//    private static void b(String var0) {
//        byte var2 = -1;
//        switch(var0.hashCode()) {
//            case 1477757:
//                if(var0.equals("0041")) {
//                    var2 = 0;
//                }
//                break;
//            case 1477822:
//                if(var0.equals("0064")) {
//                    var2 = 1;
//                }
//        }
//
//        switch(var2) {
//            case 0:
//                n = 1;
//                break;
//            case 1:
//                n = 2;
//        }
//
//    }
//
//    private static class b extends Thread {
//        private boolean a;
//
//        private b() {
//            this.a = false;
//        }
//
//        public void a() {
//            this.a = false;
//            CommandHub.f = 0;
//        }
//
//        public void run() {
//            super.run();
//            this.a = true;
//            CommandHub.f = 0;
//            String var1 = "CTP:9999 9993 0000 ";
//
//            while(this.a) {
//                CommandHub.b("9993", "CTP:9999 9993 0000 ");
//                SystemClock.sleep((long)CommandHub.r);
//                Log.i(CommandHub.a, "HeartbeatTask: mTimeoutCount=" + CommandHub.f);
//                CommandHub.getInstance();
//                if(CommandHub.f > CommandHub.s) {
//                    this.a = false;
//                    if(CommandHub.deviceListener != null) {
//                        CommandHub.handler.post(new Runnable() {
//                            public void run() {
//                                CommandHub.deviceListener.onError(2);
//                            }
//                        });
//                    }
//                    break;
//                }
//            }
//
//            Log.i(CommandHub.a, "HeartbeatTask ending" + CommandHub.f);
//        }
//    }
//
//    public interface OnDeviceListener {
//        void onConnected();
//
//        void onError(int var1);
//
//        void onReceive(StateInfo var1);
//    }
//
//    private static class c extends Thread {
//        private final LinkedBlockingQueue<DataForm> a;
//        private boolean b;
//        private volatile boolean c;
//
//        private c() {
//            this.a = new LinkedBlockingQueue();
//            this.b = false;
//            this.c = false;
//        }
//
//        private void a(DataForm var1) {
//            try {
//                this.a.put(var1);
//            } catch (InterruptedException var5) {
//                var5.printStackTrace();
//            }
//
//            if(this.c) {
//                LinkedBlockingQueue var2 = this.a;
//                synchronized(this.a) {
//                    this.a.notify();
//                }
//            }
//
//        }
//
//        private void a() {
//            this.b = false;
//            LinkedBlockingQueue var1 = this.a;
//            synchronized(this.a) {
//                this.a.notify();
//                this.a.clear();
//            }
//        }
//
//        public void run() {
//            super.run();
//            LinkedBlockingQueue var1 = this.a;
//            synchronized(this.a) {
//                while(this.b) {
//                    if(this.a.isEmpty()) {
//                        try {
//                            this.c = true;
//                            this.a.wait();
//                        } catch (Exception var10) {
//                            var10.printStackTrace();
//                        }
//                    } else {
//                        this.c = false;
//                        DataForm var2 = (DataForm)this.a.remove();
//                        if(var2 != null) {
//                            String var3 = var2.getId();
//                            String var4 = var2.getCmd();
//                            String var5 = var2.getParams();
//                            int var6 = -65535;
//
//                            try {
//                                if(!TextUtils.isEmpty(var3)) {
//                                    var6 = Integer.parseInt(var3);
//                                }
//                            } catch (Exception var12) {
//                                var12.printStackTrace();
//                            }
//
//                            int var7 = -65535;
//
//                            try {
//                                if(!TextUtils.isEmpty(var4)) {
//                                    var7 = Integer.parseInt(var4);
//                                }
//                            } catch (Exception var11) {
//                                var11.printStackTrace();
//                            }
//
//                            if(var6 != -65535 && var7 != -65535) {
//                                Log.w(CommandHub.a, "sendDataThread  id = " + var6 + " , cmdNum = " + var7);
//                                if(!TextUtils.isEmpty(var5)) {
//                                    CommandHub.j = CommandHub.b(var4, String.format(Locale.US, "CTP:%04d %04d %04d " + var5, new Object[]{Integer.valueOf(var6), Integer.valueOf(var7), Integer.valueOf((var5 + "").length())}));
//                                } else {
//                                    CommandHub.j = CommandHub.b(var4, String.format(Locale.US, "CTP:%04d %04d %04d ", new Object[]{Integer.valueOf(var6), Integer.valueOf(var7), Integer.valueOf(0)}));
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//    }
//
//    private static class a extends Thread {
//        private static final byte[] a = new byte[4];
//        private boolean b;
//        private int c;
//        private final CmdInfo d;
//
//        private a() {
//            this.b = false;
//            this.c = 0;
//            this.d = new CmdInfo();
//        }
//
//        void a() {
//            this.b = false;
//        }
//
//        public void run() {
//            super.run();
//            byte[] var1 = new byte[1024];
//            Log.i(CommandHub.a, "Receiver thread is running...");
//
//            for(this.b = true; this.b; SystemClock.sleep(10L)) {
//                if(CommandHub.socket != null && !CommandHub.socket.isClosed()) {
//                    try {
//                        int var2 = CommandHub.socket.getInputStream().read(var1);
//                        if(var2 > 0) {
//                            byte[] var3 = new byte[var2];
//                            System.arraycopy(var1, 0, var3, 0, var2);
//                            this.a(var3);
//                        }
//
//                        this.c = 0;
//                    } catch (IOException var4) {
//                        var4.printStackTrace();
//                        SystemClock.sleep(1000L);
//                        ++this.c;
//                        if(CommandHub.deviceListener != null && this.c > 5) {
//                            this.c = 0;
//                            CommandHub.handler.post(new Runnable() {
//                                public void run() {
//                                    CommandHub.deviceListener.onError(1);
//                                }
//                            });
//                            break;
//                        }
//                    }
//                } else {
//                    SystemClock.sleep(1000L);
//                }
//            }
//
//        }
//
//        private synchronized void a(byte[] var1) {
//            String var2 = new String(var1);
//            if(var2.length() < 19) {
//                Log.e(CommandHub.a, "data error: " + var2.length());
//            } else {
//                int var3 = 0;
//                Log.w(CommandHub.a, "==========receive[" + var2 + "]");
//
//                for(; var3 < var2.length(); var3 += 19) {
//                    final StateInfo var4 = new StateInfo();
//                    System.arraycopy(var1, var3 + 9, a, 0, 4);
//                    String var5 = new String(a);
//                    var4.setCmdNumber(var5);
//                    System.arraycopy(var1, var3 + 14, a, 0, 4);
//                    int var6 = Integer.parseInt(new String(a));
//                    byte[] var7 = new byte[var6];
//                    System.arraycopy(var1, var3 + 19, var7, 0, var6);
//                    String[] var8 = (new String(var7)).split(" ");
//                    var4.setParam(var8);
//                    byte var10 = -1;
//                    switch(var5.hashCode()) {
//                        case 1477637:
//                            if(var5.equals("0005")) {
//                                var10 = 8;
//                            }
//                            break;
//                        case 1477638:
//                            if(var5.equals("0006")) {
//                                var10 = 9;
//                            }
//                            break;
//                        case 1477639:
//                            if(var5.equals("0007")) {
//                                var10 = 10;
//                            }
//                            break;
//                        case 1477640:
//                            if(var5.equals("0008")) {
//                                var10 = 11;
//                            }
//                            break;
//                        case 1477756:
//                            if(var5.equals("0040")) {
//                                var10 = 12;
//                            }
//                            break;
//                        case 1477757:
//                            if(var5.equals("0041")) {
//                                var10 = 0;
//                            }
//                            break;
//                        case 1477759:
//                            if(var5.equals("0043")) {
//                                var10 = 4;
//                            }
//                            break;
//                        case 1477762:
//                            if(var5.equals("0046")) {
//                                var10 = 2;
//                            }
//                            break;
//                        case 1477787:
//                            if(var5.equals("0050")) {
//                                var10 = 13;
//                            }
//                            break;
//                        case 1477788:
//                            if(var5.equals("0051")) {
//                                var10 = 14;
//                            }
//                            break;
//                        case 1477795:
//                            if(var5.equals("0058")) {
//                                var10 = 6;
//                            }
//                            break;
//                        case 1477819:
//                            if(var5.equals("0061")) {
//                                var10 = 15;
//                            }
//                            break;
//                        case 1477820:
//                            if(var5.equals("0062")) {
//                                var10 = 16;
//                            }
//                            break;
//                        case 1477822:
//                            if(var5.equals("0064")) {
//                                var10 = 1;
//                            }
//                            break;
//                        case 1477824:
//                            if(var5.equals("0066")) {
//                                var10 = 5;
//                            }
//                            break;
//                        case 1477827:
//                            if(var5.equals("0069")) {
//                                var10 = 3;
//                            }
//                            break;
//                        case 1477851:
//                            if(var5.equals("0072")) {
//                                var10 = 7;
//                            }
//                    }
//
//                    switch(var10) {
//                        case 0:
//                            CommandHub.n = 1;
//                            break;
//                        case 1:
//                            CommandHub.n = 2;
//                            break;
//                        case 2:
//                        case 3:
//                        case 4:
//                        case 5:
//                        case 6:
//                        case 7:
//                            EventBus.getDefault().post(var4);
//                            break;
//                        case 8:
//                        case 9:
//                        case 10:
//                        case 11:
//                        case 12:
//                            String[] var11 = new String[]{new String(var7)};
//                            var4.setParam(var11);
//                            break;
//                        case 13:
//                            if("0".equals(var8[0])) {
//                                if("1".equals(var8[2])) {
//                                    CommandHub.l = true;
//                                } else if("0".equals(var8[2])) {
//                                    CommandHub.l = false;
//                                }
//                            }
//                            break;
//                        case 14:
//                            if("0".equals(var8[0])) {
//                                CommandHub.l = false;
//                            }
//                            break;
//                        case 15:
//                            if("0".equals(var8[0])) {
//                                if("1".equals(var8[2])) {
//                                    CommandHub.m = true;
//                                } else if("0".equals(var8[2])) {
//                                    CommandHub.m = false;
//                                }
//                            }
//                            break;
//                        case 16:
//                            if("0".equals(var8[0])) {
//                                CommandHub.m = false;
//                            }
//                    }
//
//                    this.d.setCmdParams(var8);
//                    this.d.setCommand(var5);
//                    this.d.setCtpId("1");
//                    var10 = -1;
//                    switch(var5.hashCode()) {
//                        case 1477670:
//                            if(var5.equals("0017")) {
//                                var10 = 3;
//                            }
//                            break;
//                        case 1477694:
//                            if(var5.equals("0020")) {
//                                var10 = 1;
//                            }
//                            break;
//                        case 1477695:
//                            if(var5.equals("0021")) {
//                                var10 = 2;
//                            }
//                            break;
//                        case 1477696:
//                            if(var5.equals("0022")) {
//                                var10 = 4;
//                            }
//                            break;
//                        case 1477697:
//                            if(var5.equals("0023")) {
//                                var10 = 5;
//                            }
//                            break;
//                        case 1754682:
//                            if(var5.equals("9993")) {
//                                var10 = 0;
//                            }
//                    }
//
//                    switch(var10) {
//                        case 0:
//                            CommandHub.f = 0;
//                        case 1:
//                        case 2:
//                        case 3:
//                        case 4:
//                        case 5:
//                            break;
//                        default:
//                            if(CommandHub.deviceListener != null) {
//                                CommandHub.handler.post(new Runnable() {
//                                    public void run() {
//                                        if(CommandHub.deviceListener != null) {
//                                            CommandHub.deviceListener.onReceive(var4);
//                                        }
//
//                                    }
//                                });
//                            } else {
//                                Log.e(CommandHub.a, "OnReceiverListener is null, you MUST implement it." + CommandHub.deviceListener);
//                            }
//                    }
//
//                    if(var6 > 0) {
//                        var3 += var6;
//                    }
//                }
//            }
//
//        }
//    }
//}
//
