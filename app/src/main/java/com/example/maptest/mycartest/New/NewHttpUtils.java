package com.example.maptest.mycartest.New;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.Entity.AppVersion;
import com.example.maptest.mycartest.Entity.EnableTimeBean;
import com.example.maptest.mycartest.Entity.NbDevice;
import com.example.maptest.mycartest.Entity.PictureBean;
import com.example.maptest.mycartest.UI.SetUi.service.AudioQuerryService;
import com.example.maptest.mycartest.UI.SetUi.service.AudioRequsetBean;
import com.example.maptest.mycartest.UI.SetUi.service.AvideoBean;
import com.example.maptest.mycartest.UI.SetUi.service.Command;
import com.example.maptest.mycartest.UI.SetUi.service.CommandFindService;
import com.example.maptest.mycartest.UI.TyreUi.TyreListStatuBean;
import com.example.maptest.mycartest.UI.warn.DeleteAllService;
import com.example.maptest.mycartest.UI.warn.PostReadService;
import com.example.maptest.mycartest.UI.warn.QuerryWarnService;
import com.example.maptest.mycartest.UI.warn.WarnListBean;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.http.EditInfoService;
import com.example.maptest.mycartest.Utils.http.QuerryDeviceService;
import com.example.maptest.mycartest.Utils.http.QuerryService;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by ${Author} on 2018/3/9.
 * Use to
 */

public class NewHttpUtils {
    public static Dialog dialog;
    public static Call<ResponseBody> callLoca;

    public static OkHttpClient getClient(final Context context) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        final Request.Builder builder = chain.request().newBuilder();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                        //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可
                        Observable.just(sharedPreferences.getString("cookie", ""))
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String cookie) {
                                        //添加cookie
                                        builder.addHeader("Cookie", cookie);
                                    }
                                });
                        return chain.proceed(builder.build());
                    }
                }).build();
        return okHttpClient;

    }
    public static OkHttpClient loginClient(final Context context) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response originalResponse = chain.proceed(chain.request());
                        //这里获取请求返回的cookie
                        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                            final StringBuffer cookieBuffer = new StringBuffer();
                            //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可.大家可以用别的方法保存cookie数据
                            Observable.from(originalResponse.headers("Set-Cookie"))
                                    .map(new Func1<String, String>() {
                                        @Override
                                        public String call(String s) {
                                            String[] cookieArray = s.split(";");
                                            return cookieArray[0];
                                        }
                                    })
                                    .subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String cookie) {
                                            cookieBuffer.append(cookie).append(";");
                                        }
                                    });
                            SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("cookie", cookieBuffer.toString());
                            editor.commit();
                        }

                        return originalResponse;

                    }
                }).build();
        return okHttpClient;

    }

    public static void modifyMessage(String obj, Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EditInfoService service = retrofit.create(EditInfoService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("editResponse", string);
                        EditResultBean bean = parseObject(string, EditResultBean.class);
                        responseCallback.TaskCallBack(bean);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("editResponse", "null");
                    responseCallback.FailCallBack("fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("editThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });

    }

    public static void querrAgent(String obj, Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryService service = retrofit.create(QuerryService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("当前代理商列表",string);
                        List<AgentlistBean> list = new ArrayList<AgentlistBean>();
                        list = parseObject(string.toString(), new TypeReference<List<AgentlistBean>>() {
                        });
                        if (responseCallback != null) {
                            responseCallback.TaskCallBack(list);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseCallback.FailCallBack("fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("editThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });

    }

    public static void psotAgent(String obj, Context context, final ResponseCallback responseCallback) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.ORDERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryService service = retrofit.create(QuerryService.class);
        Call<ResponseBody> call = service.psotAgent(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("Post",string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("editThrowable", t.toString());

            }
        });

    }

    public static void querrUpAgent(String obj, Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryService service = retrofit.create(QuerryService.class);
        Call<ResponseBody> call = service.findAgent(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("querrUpAgent",string);
                        responseCallback.TaskCallBack(string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseCallback.FailCallBack("fail");

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });

    }



    public static void querrUpAgentTEST(String obj, Context context) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryService service = retrofit.create(QuerryService.class);
        Call<ResponseBody> call = service.getBattery(obj);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("querrUpAgent",string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure", t.toString());
            }
        });

    }

    public static void getAppName(String appName, Context context) {
        Log.d("getAppName",appName);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryService service = retrofit.create(QuerryService.class);
        Call<ResponseBody> call = service.getAppNmae(appName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.d("getAppName",string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("getAppName",response.message());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("getAppName", t.toString());
            }
        });

    }



    public static void querrDevice(String obj, Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryDeviceService service = retrofit.create(QuerryDeviceService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("device", string);
                        List<LocationListBean> list = new ArrayList<LocationListBean>();
                        try {
                            list = parseObject(string, new TypeReference<List<LocationListBean>>() {
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            responseCallback.FailCallBack(null);
                        }

                        Log.e("Device", string);
                        if (list.size() == 0) {
                            responseCallback.FailCallBack(null);
                        } else {
                            responseCallback.TaskCallBack(list);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseCallback.FailCallBack(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("editThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });

    }

    public static void getEnableTime(String terminalID, Context context, final ResponseCallback responseCallback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryLocationService service = retrofit.create(QuerryLocationService.class);
        Call<ResponseBody> call = service.getEnableTime(terminalID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null){
                    try {
                        String string = new String(response.body().bytes());
                        EnableTimeBean enableTimeBean = JSONObject.parseObject(string,EnableTimeBean.class);
                        responseCallback.TaskCallBack(enableTimeBean);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public static void querrLoocation(String obj, Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryLocationService service = retrofit.create(QuerryLocationService.class);
        callLoca = service.getMessage(body);
        callLoca.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("location", string);
                        List<LocationListBean> list = new ArrayList<LocationListBean>();
                        try {
                            list = parseObject(string, new TypeReference<List<LocationListBean>>() {
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            responseCallback.FailCallBack("fail");
                        }
                        responseCallback.TaskCallBack(list);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseCallback.FailCallBack("fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("locationThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });

    }

    public static void cancelCall() {
        if (callLoca != null && callLoca.isExecuted()) {
            callLoca.cancel();
            Log.e("执行取消", "call cancel");
        }
    }

    public static void querrAlarms(String obj, Context context, final ResponseCallback responseCallback) {
        if (dialog == null) {
            dialog = WeiboDialogUtils.createLoadingDialog(context, "发送中");
        } else {
            dialog = null;
            dialog = WeiboDialogUtils.createLoadingDialog(context, "发送中");
            dialog.show();
        }
        Log.e("obj",obj);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryTrackService service = retrofit.create(QuerryTrackService.class);
        Call<ResponseBody> call = service.findAlarms(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog != null){
                    dialog.dismiss();
                }
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("track", string);
                        responseCallback.TaskCallBack(string);

                    } catch (IOException e) {
                        e.printStackTrace();
                        responseCallback.FailCallBack("fail");
                    }
                } else {
                    responseCallback.FailCallBack("fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("trackThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });

    }

    public static void querrTrack(String obj, Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryTrackService service = retrofit.create(QuerryTrackService.class);
        Call<ResponseBody> call = service.findNewTrack(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("track", string);
                        TrickCountBean trickCountBean = parseObject(string, TrickCountBean.class);
                        AppCons.trickCountBean = trickCountBean;
                        Log.e("history", trickCountBean.getSize() + " 大小 " + trickCountBean.getTotalPages() + "页数 " + trickCountBean.getTotalElements() + "总数");
                        List<TrickListBean> list = new ArrayList<TrickListBean>();
                        JSONObject jsonObject = parseObject(string);
                        JSONArray dates = jsonObject.getJSONArray("content");
                        list = parseObject(dates.toString(), new TypeReference<List<TrickListBean>>() {
                        });
                        responseCallback.TaskCallBack(list);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseCallback.FailCallBack("fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("trackThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });

    }

    public static void querrOldTrack(String obj,  Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryTrackService service = retrofit.create(QuerryTrackService.class);
        Call<ResponseBody> call = service.findOldTrack(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("track", string);
                        TrickCountBean trickCountBean = parseObject(string, TrickCountBean.class);
                        AppCons.trickCountBean = trickCountBean;
                        Log.e("history", trickCountBean.getSize() + " 大小 " + trickCountBean.getTotalPages() + "页数 " + trickCountBean.getTotalElements() + "总数");
                        List<TrickListBean> list = new ArrayList<TrickListBean>();
                        JSONObject jsonObject = parseObject(string);
                        JSONArray dates = jsonObject.getJSONArray("content");
                        list = parseObject(dates.toString(), new TypeReference<List<TrickListBean>>() {
                        });
                        responseCallback.TaskCallBack(list);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseCallback.FailCallBack("fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("trackThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });

    }


    public static void exportPic(String obj,File file, Context context, final ResponseCallback responseCallback) {
        if (dialog == null){
            dialog = WeiboDialogUtils.createLoadingDialog(context, "提交中");
        }else{
            dialog.show();
        }

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryTrackService service = retrofit.create(QuerryTrackService.class);
        Call<ResponseBody> call = service.exportPic(description,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog != null){
                    dialog.dismiss();
                }
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("上传string",string);
                        PictureBean pictureBean = JSONObject.parseObject(string,PictureBean.class);
                        responseCallback.TaskCallBack(pictureBean);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("上传string error",e.toString());
                        responseCallback.FailCallBack("fail");
                    }
                } else {
                    responseCallback.FailCallBack("fail");
                    try {
                        String string = new String(response.errorBody().bytes());
                        Log.e("上传string失败",string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("trackThrowable", t.toString());
                if (dialog != null){
                    dialog.dismiss();
                }
                responseCallback.FailCallBack(t.toString());
            }
        });

    }

    public static void uploadApk(String obj,String appName,String content, File file, Context context, final ResponseCallback responseCallback) {
        if (dialog == null){
            dialog = WeiboDialogUtils.createLoadingDialog(context, "提交中");
        }
        dialog.show();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), content);
        RequestBody version =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), obj);
        RequestBody apkName =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), appName);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryTrackService service = retrofit.create(QuerryTrackService.class);
        Call<ResponseBody> call = service.uploadApk(version,description,apkName,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog != null){
                    dialog.dismiss();
                }
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("上传string",string);
                        PictureBean pictureBean = JSONObject.parseObject(string,PictureBean.class);
                        responseCallback.TaskCallBack(pictureBean);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("上传string error",e.toString());
                        responseCallback.FailCallBack("fail");
                    }
                } else {
                    responseCallback.FailCallBack("fail");
                    try {
                        String string = new String(response.errorBody().bytes());
                        Log.e("上传string失败",string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("trackThrowable", t.toString());
                if (dialog != null){
                    dialog.dismiss();
                }
                responseCallback.FailCallBack(t.toString());
            }
        });

    }




    public static void querrTire(String obj, Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryTyreService service = retrofit.create(QuerryTyreService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("tire", string);
                        TyreListStatuBean bean = parseObject(string, TyreListStatuBean.class);
                        if (responseCallback != null) {
                            responseCallback.TaskCallBack(bean);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseCallback.FailCallBack("fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("tireThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });

    }

    public static void querrWarn(String obj, Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryWarnService service = retrofit.create(QuerryWarnService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("querrWarn",string);
                        List<WarnListBean> warnList = new ArrayList<WarnListBean>();
                        warnList = parseObject(string, new TypeReference<List<WarnListBean>>() {
                        });
                        if (responseCallback != null) {
                            responseCallback.TaskCallBack(warnList);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                } else {
                    responseCallback.FailCallBack("fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("warnThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });
    }

    public static void querrWarnOnly(String obj, Context context, final ResponseCallback responseCallback) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuerryWarnService service = retrofit.create(QuerryWarnService.class);
        Call<ResponseBody> call = service.getMessageOnlyOne(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("querrWarn",string);
                        if (responseCallback != null) {
                            responseCallback.TaskCallBack(string);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                } else {
                    responseCallback.FailCallBack("fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("warnThrowable", t.toString());
                responseCallback.FailCallBack(t.toString());
            }
        });
    }


    public static void saveRead(String obj, Context context) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostReadService service = retrofit.create(PostReadService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("warnThrowable", t.toString());

            }
        });
    }

    public static void deleteId(String obj, Context context, ResponseCallback responseCallback) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostReadService service = retrofit.create(PostReadService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("warnThrowable", t.toString());
            }
        });
    }

    public static void deleteAll(String obj, Context context) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DeleteAllService service = retrofit.create(DeleteAllService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("warnThrowable", t.toString());
            }
        });
    }

    public static void loginOut(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient(context))
                .build();
        LoginOutService service = retrofit.create(LoginOutService.class);
        Call<ResponseBody> call = service.loginOut();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void findCommand(String obj, Context context, final ResponseCallback responseCallback) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommandFindService service = retrofit.create(CommandFindService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("commond", string);
                        responseCallback.TaskCallBack(string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("command", "null");
                    responseCallback.FailCallBack(  "");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Throwable", t.toString());
                responseCallback.FailCallBack(t.getMessage());
            }
        });
    }

    public static void querryCommand(String obj, Context context, final ResponseCallback responseCallback) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CommandFindService service = retrofit.create(CommandFindService.class);
        Call<ResponseBody> call = service.findCommands(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("commond", string);
                        List<Command>list = new ArrayList<Command>();
                        if (string != null){
                            JSONArray json = JSON.parseArray(string);
                            list = json.toJavaList(Command.class);

                            responseCallback.TaskCallBack(list);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        responseCallback.FailCallBack("");
                    }
                } else {
                    Log.e("command", "null");
                    responseCallback.FailCallBack("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Throwable", t.toString());
                responseCallback.FailCallBack(t.getMessage());
            }
        });
    }

    public static void postCommand(String obj, Context context, final ResponseCallback responseCallback) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommandFindService service = retrofit.create(CommandFindService.class);
        Call<ResponseBody> call = service.saveLixianCommands(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("postCommand", string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("postCommand", "null");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Throwable", t.toString());
            }
        });
    }

    public static void saveCommand(String obj, Context context, final ResponseCallback responseCallback) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommandFindService service = retrofit.create(CommandFindService.class);
        Call<ResponseBody> call = service.saveMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("command", string);
                        responseCallback.TaskCallBack(string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("command", "null");
                    responseCallback.FailCallBack("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Throwable", t.toString());
                responseCallback.TaskCallBack(t.getMessage());
            }
        });
    }

    public static void sendOrder(String obj, Context context, final ResponseCallback responseCallback) {
        if (AppCons.mNbDevice != null && AppCons.mNbDevice.getData() != null && AppCons.locationListBean != null && AppCons.locationListBean.getDevice() != null
                && AppCons.locationListBean.getLocation().getDeviceProtocol() == 11){
            Map<Object,Object> map = new HashMap<Object, Object>();
            map = new Gson().fromJson(obj,Map.class);
            map.put("deviceId",AppCons.mNbDevice.getData());
            map.put("deviceProtocol","11");
            obj = new Gson().toJson(map);
        }
        Log.e("转换后",obj);
        if (dialog == null) {
            dialog = WeiboDialogUtils.createLoadingDialog(context, "发送中");
        } else {
            dialog = null;
            dialog = WeiboDialogUtils.createLoadingDialog(context, "发送中");
            dialog.show();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.ORDERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommandFindService service = retrofit.create(CommandFindService.class);
        Call<ResponseBody> call = service.saveCommands(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("sends", string);
                        if (string != null) {
                            CommandResponse commandResponse = JSONObject.parseObject(string,CommandResponse.class);
                            responseCallback.TaskCallBack(commandResponse);
                        } else {
                            responseCallback.FailCallBack("");
                        }

                    } catch (IOException e) {
                        responseCallback.FailCallBack("");
                        e.printStackTrace();
                    }
                } else {
                    Log.e("sends", "null");
                    responseCallback.FailCallBack("");
                }
                WeiboDialogUtils.closeDialog(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("sendsThrowable", t.toString());
                WeiboDialogUtils.closeDialog(dialog);
                responseCallback.FailCallBack(t.getMessage());
            }
        });
    }

    public static void querryAudio(String obj, final ResponseCallback responseCallback){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AudioQuerryService service = retrofit.create(AudioQuerryService.class);
        Call<ResponseBody> call = service.getMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                List<AvideoBean>list = new ArrayList<AvideoBean>();
                try {
                    String string = new String(response.body().bytes());
                    Log.e("onResponse",string);
                    if (string != null){
                        AudioRequsetBean bean = JSON.parseObject(string,AudioRequsetBean.class);
                        list = bean.getData();
                        responseCallback.TaskCallBack(list);
                    }else {
                        responseCallback.FailCallBack("null");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    responseCallback.FailCallBack("null");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseCallback.FailCallBack("null");
            }
        });
    }

    public static void readAudio(String obj){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AudioQuerryService service = retrofit.create(AudioQuerryService.class);
        Call<ResponseBody> call = service.postRead(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String string = new String(response.body().bytes());
                    Log.e("onResponse",string);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void deleteAudioOrder(String obj) {

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.ORDERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommandFindService service = retrofit.create(CommandFindService.class);
        Call<ResponseBody> call = service.delCommands(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("sends", string);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                } else {

                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public static void deleteAudio(String obj, final ResponseCallback responseCallback){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AudioQuerryService service = retrofit.create(AudioQuerryService.class);
        Call<ResponseBody> call = service.deleteAudio(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String string = new String(response.body().bytes());
                    Log.e("onResponse",string);
                    responseCallback.TaskCallBack(string);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void checkPassword(String password,String terminalId, Context context, final ResponseCallback responseCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CommandFindService service = retrofit.create(CommandFindService.class);
        Call<ResponseBody> call = service.checkPassword(password,terminalId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("commond", string);
                            responseCallback.TaskCallBack(string);
                    } catch (IOException e) {
                        e.printStackTrace();
                        responseCallback.FailCallBack("");
                    }
                } else {
                    Log.e("command", "null");
                    responseCallback.FailCallBack("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Throwable", t.toString());
                responseCallback.FailCallBack(t.getMessage());
            }
        });
    }


    public static void getAppVersion(String appName, Context context, final ResponseCallback responseCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CommandFindService service = retrofit.create(CommandFindService.class);
        Call<AppVersion> call = service.getAppVersion(appName);
        call.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                if (response.body() != null) {
                    try {
//                        String string = new String(response.body().bytes());
//                        Log.e("commond", string);
                        responseCallback.TaskCallBack(response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                        responseCallback.FailCallBack("");
                    }
                } else {
                    Log.e("command", "null");
                    responseCallback.FailCallBack("");
                }
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                Log.e("Throwable", t.toString());
                responseCallback.FailCallBack(t.getMessage());
            }
        });
    }



    public static void sethireExpirationTime(String obj, Context context, final ResponseCallback responseCallback) {
        if (dialog == null){
            dialog = WeiboDialogUtils.createLoadingDialog(context, "提交中");
        }
        dialog.show();
        Log.e("提交的数据",obj);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Log.e("提交的数据",body.toString());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CommandFindService service = retrofit.create(CommandFindService.class);
        Call<ResponseBody> call = service.hireExpirationTime(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog != null){
                    dialog.dismiss();
                }
                if (response.body() != null) {
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("commond", string);
                        responseCallback.TaskCallBack(string);

                    } catch (IOException e) {
                        e.printStackTrace();
                        responseCallback.FailCallBack("");
                    }
                } else {
                    Log.e("command", "null");
                    responseCallback.FailCallBack("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Throwable", t.toString());
                if (dialog != null){
                    dialog.dismiss();
                }
                responseCallback.FailCallBack(t.getMessage());
            }
        });
    }

    public static void findDeviceId(String terminalID, Context context, final ResponseCallback responseCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CommandFindService service = retrofit.create(CommandFindService.class);
        Call<NbDevice> call = service.findDeviceId(terminalID);
        call.enqueue(new Callback<NbDevice>() {
            @Override
            public void onResponse(Call<NbDevice> call, Response<NbDevice> response) {
                if (response.body() != null) {
                    try {
                        responseCallback.TaskCallBack(response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                        responseCallback.FailCallBack("");
                    }
                } else {
                    Log.e("command", "null");
                    responseCallback.FailCallBack("");
                }
            }

            @Override
            public void onFailure(Call<NbDevice> call, Throwable t) {
                Log.e("Throwable", t.toString());
                responseCallback.FailCallBack(t.getMessage());
            }
        });
    }

}
