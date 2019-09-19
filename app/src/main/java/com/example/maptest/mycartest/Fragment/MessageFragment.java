package com.example.maptest.mycartest.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.maptest.mycartest.Entity.EnableTimeBean;
import com.example.maptest.mycartest.Entity.PictureBean;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.EquipUi.BkRulerActivity;
import com.example.maptest.mycartest.UI.EquipUi.EditMessageActivity;
import com.example.maptest.mycartest.UI.InformationActivity;
import com.example.maptest.mycartest.UI.warn.WarnMationActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.MyGlideUrl;
import com.example.maptest.mycartest.Utils.TimePickActivity;
import com.example.maptest.mycartest.Utils.UtcDateChang;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.maptest.mycartest.Utils.AppCons.locationListBean;

/**
 * Created by ${Author} on 2017/3/13.
 * Use to 每个车辆的信息
 */

public class MessageFragment extends Fragment implements View.OnClickListener{

    private TextView textView_eqpname,textView_eqpno,textView_nolca,textView_mode,textView_tell,textView_lvtime,textView_serve;
    private RelativeLayout relativeLayout_break,relativeLayout_warninfo,relative_serve;
    private ImageView imageView_edit,imageView_quit,image_picture;
    private ImageView button_yl,button_sc;
    private String startTime;
    InformationActivity activity = InformationActivity.instance;
    private File file,photoFile;
    private Uri imgUri;
    private Uri mCutUri;// 图片裁剪时返回的uri
    private View view,view_pic;
    private String mPublicPhotoPath;
    private Dialog dialog_picture;
    public static final String IMAGE_UNSPECIFIED = "image/*";//随意图片类型
    private static final int PHOTORESOULT = 98,TAKE_PHOTO = 99,SELECT_PHOTO = 100,ExpirationTime = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    LocationListBean use = (LocationListBean) msg.obj;
                    Log.d("uuuu",use.toString());
                    textView_eqpname.setText(use.getNickname());
                    textView_eqpno.setText(use.getCarNumber()+"");
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param activity
     * 获取宿主activity里的数据
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_eqpinfo,null);
        initView();
        initClick();
        initDatas();
        return view;
    }

    private void initDatas() {
        textView_eqpname.setText(locationListBean.getNickname());
        textView_eqpno.setText(locationListBean.getCarNumber()+"");
        textView_nolca.setText(AppCons.ADDRESS);
        textView_mode.setText(locationListBean.getDevice().getDeviceType());
        textView_tell.setText(locationListBean.getDevice().getDeviceNumber());

        NewHttpUtils.getEnableTime(locationListBean.getTerminalID(), getActivity(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                EnableTimeBean enableTimeBean = (EnableTimeBean) object;
                if (enableTimeBean != null && enableTimeBean.getData() != null){
                    textView_lvtime.setText(UtcDateChang.UtcDatetoLocaTime(enableTimeBean.getData().getEnableTime()));     /*激活日期暂定创建日期*/
                }

            }

            @Override
            public void FailCallBack(Object object) {

            }
        });
        try {
            if (locationListBean.getDevice().getHireExpirationTime().equals("")){
                textView_serve.setText("终身");  /*服务日期暂定设备到期日期*/
            }else {
                textView_serve.setText(locationListBean.getDevice().getHireExpirationTime());  /*服务日期暂定设备到期日期*/
            }
        }catch (Exception e){
            e.printStackTrace();
            textView_serve.setText("终身");  /*服务日期暂定设备到期日期*/
        }


    }

    private void initClick() {
        relativeLayout_warninfo.setOnClickListener(this);
        relativeLayout_break.setOnClickListener(this);
        imageView_edit.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        button_yl.setOnClickListener(this);
        button_sc.setOnClickListener(this);
    }

    private void initView() {
        relative_serve = view.findViewById(R.id.relative_serve);
        button_yl = view.findViewById(R.id.button_yl);
        button_sc = view.findViewById(R.id.button_sc);
        imageView_quit = (ImageView) view.findViewById(R.id.image_quitmsg);
        imageView_edit = (ImageView) view.findViewById(R.id.image_editinfo);
        relativeLayout_break = (RelativeLayout)  view.findViewById(R.id.relative_break);
        relativeLayout_warninfo = (RelativeLayout)  view.findViewById(R.id.relative_warninfo);
        textView_eqpname = (TextView)  view.findViewById(R.id.text_eqpname);
        textView_eqpno = (TextView)  view.findViewById(R.id.text_platenos);
        textView_nolca = (TextView)  view.findViewById(R.id.text_eqplocation);
        textView_mode = (TextView)  view.findViewById(R.id.text_models);
        textView_tell = (TextView)  view.findViewById(R.id.text_eqptellnub);
        textView_lvtime = (TextView)  view.findViewById(R.id.text_lives);
        textView_serve = (TextView)  view.findViewById(R.id.text_serve);
        if (AppCons.loginDataBean.getData().getRole() != 3) {
            textView_serve.setOnClickListener(this);
            textView_serve.setClickable(true);
        }else {
            textView_serve.setClickable(false);
        }
    }

    private void showPicDialog(){
        if (locationListBean.getPicture().equals("")){
            Toast.makeText(getContext(),"当前设备没有车辆图片",Toast.LENGTH_SHORT).show();
            return;
        }
        if (dialog_picture == null){
            Log.e("图骗路径",locationListBean.getPicture());
            mPublicPhotoPath = locationListBean.getPicture();
            dialog_picture = new Dialog(getActivity(),R.style.ActionDialogStyle);
            view_pic = getActivity().getLayoutInflater().inflate(R.layout.dialog_picture,null);
            image_picture = view_pic.findViewById(R.id.image_picture);
            Glide.with(getActivity()).load(AppCons.locationListBean.getPicture()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_picture);
            dialog_picture.setContentView(view_pic);
            dialog_picture.setCanceledOnTouchOutside(true);
            Window dialogWindow = dialog_picture.getWindow();
            dialogWindow.setGravity( Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            WindowManager wm = (WindowManager) getActivity()
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            lp.height = (int) (height * 0.6);//设置Dialog距离底部的距离
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            Log.e("lp.height",lp.height + " ; lp.height: " + height);
            view_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_picture.dismiss();
                }
            });
//       将属性设置给窗体
            dialogWindow.setAttributes(lp);
            dialog_picture.show();
        }else {
            if (mPublicPhotoPath.equals(locationListBean.getPicture())){
                Log.e("图骗相等路径",locationListBean.getPicture());
                Log.e("图骗相等路径",mPublicPhotoPath);

                if (dialog_picture != null && !dialog_picture.isShowing())
                    dialog_picture.show();
            }else {
                Log.e("图骗不相等路径",locationListBean.getPicture());
                Log.e("图骗不相等路径",mPublicPhotoPath);
                mPublicPhotoPath = locationListBean.getPicture();
                if (dialog_picture != null && !dialog_picture.isShowing()){
                    Glide.with(this).load(new MyGlideUrl(locationListBean.getPicture())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_picture);
                    dialog_picture.show();
                }

            }
        }


    }


    private void setTimeDialog(final String data){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setIcon(R.drawable.iflytek_dialog_image);
        normalDialog.setTitle("设置服务日期");
        normalDialog.setMessage("确定设置服务日期为:"+data);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        textView_serve.setText(data);
                        locationListBean.getDevice().setHireExpirationTime(data);
                        Map<Object,Object>map = new HashMap<Object, Object>();
                        map.put("hireExpirationTime",data);
                        map.put("list",new String[]{locationListBean.getTerminalID()});
                        NewHttpUtils.sethireExpirationTime(new Gson().toJson(map), getActivity(), new ResponseCallback() {
                            @Override
                            public void TaskCallBack(Object object) {

                            }

                            @Override
                            public void FailCallBack(Object object) {

                            }
                        });
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_break:
                startActivity(new Intent(getActivity(),BkRulerActivity.class));
                break;
            case R.id.relative_warninfo:
                qurryMation();
                break;
            case R.id.image_editinfo:
                etidmessage();
                break;
            case R.id.image_quitmsg:
                getActivity().finish();
                break;
            case R.id.button_yl:
//                    if (activity == null){
//                        activity = InformationActivity.instance;
//                    }
//                    activity.showPicDialog();
                showPicDialog();
                break;
            case R.id.button_sc:
                showPopwindow();
                break;
            case R.id.text_serve:
                hireExpirationTime();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        showPicDialog();
    }

    /**
     * 查询车辆报警信息
     */
    private void qurryMation() {
        AppCons.WARNTYPE = 1;
        Bundle bundle = new Bundle();
        bundle.putString("TimID", locationListBean.getTerminalID());
        bundle.putString("AgtID", locationListBean.getId()+"");
        startActivity(new Intent(getActivity(), WarnMationActivity.class).putExtras(bundle));
    }


    /**
     * 编辑车辆信息
     */
    private void etidmessage() {
        Intent intent = new Intent(getActivity(), EditMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppCons.TEST_INT, locationListBean);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    /**
     * 选择时间
     */
    private void hireExpirationTime() {
        Intent intent = new Intent(getActivity(), TimePickActivity.class);
        intent.putExtra("date", textView_serve.getText());
        startActivityForResult(intent, ExpirationTime);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * 获取到返回修改后的信息，并更新UI
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 0:
                    LocationListBean beans = (LocationListBean) data.getExtras().getSerializable(AppCons.TEST_INT);
                    Message message = new Message();
                    message.what = 0;
                    message.obj = beans;
                    handler.sendMessage(message);
                    break;
                case TAKE_PHOTO:
//                    cropPhoto(imgUri, true);
                    Log.e("拍照的照片",imgUri + "====" + imgUri.getPath());
//                    String mPhotoUri =  getRealPathFromUri(getActivity(),imgUri);
//                        file = new File(imgUri.getPath());
//                        file.mkdirs();
                    Log.e("文件存在",photoFile.exists() + ";");
                        upLoaderFile(photoFile);
                    break;
                case SELECT_PHOTO:
                    Log.e("相册的照片",data.getData() + "");
//                    cropPhoto(data.getData(), false);
                    if (Build.VERSION.SDK_INT >= 28){
                        String mUri =  getRealPathFromUri(getActivity(),data.getData());
                        Log.e("转换后的图片",mUri);
                        file = new File(mUri);
                        file.mkdirs();
                        upLoaderFile(file);
                    }else {
                        file = new File(data.getData().getPath());
                        file.mkdirs();
                        upLoaderFile(file);
                    }

                    break;
                case PHOTORESOULT:  //开始上传
                    Log.e("mCutUri","裁剪后的照片：" + mCutUri);
//                    if (Build.VERSION.SDK_INT >= 28){
//                        String mPhotoUri =  getRealPathFromUri(getActivity(),mCutUri);
//                        file = new File(mPhotoUri);
//                        file.mkdirs();
//                        upLoaderFile(file);
//                    }else {
                        file = new File(mCutUri.getPath());
                        file.mkdirs();
                        upLoaderFile(file);
//                    }

                    break;
                case ExpirationTime:
                    String date = data.getStringExtra("date");
                    if (!textView_serve.getText().toString().equals(date)) {
                        startTime = data.getStringExtra("date");
                    } else {
                        System.out.println("选择未变");
                    }
                    setTimeDialog(startTime);
                    break;
            }
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    private void upLoaderFile(File file){
        if (file == null){
            Toast.makeText(getContext(),"文件未选择成功",Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            NewHttpUtils.exportPic(AppCons.locationListBean.getTerminalID(), file, getContext(), new ResponseCallback() {
                @Override
                public void TaskCallBack(Object object) {
                    PictureBean pictureBean = (PictureBean) object;
                    if (pictureBean != null && pictureBean.getMeta().isSuccess() && pictureBean.getMeta().getMessage().equals("200")){
                        Toast.makeText(getContext(),"图片上传成功",Toast.LENGTH_SHORT).show();
                        Log.e("上传成功之后",pictureBean.getData());
                        AppCons.locationListBean.setPicture(pictureBean.getData());
                    }else {
                        Toast.makeText(getContext(),"图片上传失败",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void FailCallBack(Object object) {
                    Toast.makeText(getContext(),"图片上传失败",Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    // 图片裁剪
    private void cropPhoto(Uri uri, boolean fromCapture) {
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);

        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
        if (fromCapture) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致
            mCutUri = uri;
        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
            String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
            String fileName = "photo_" + time;
            File mCutFile = new File(Environment.getExternalStorageDirectory() + "/take_photo", fileName + ".jpeg");
            if (!mCutFile.getParentFile().exists()) {
                mCutFile.getParentFile().mkdirs();
            }
            mCutUri = getUriForFile(getActivity(), mCutFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        Toast.makeText(getActivity(), "剪裁图片", Toast.LENGTH_SHORT).show();
        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        getActivity().sendBroadcast(intentBc);

        startActivityForResult(intent, PHOTORESOULT); //设置裁剪参数显示图片至ImageVie
    }

    public void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11&&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePhone();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        activity = null;
        if (photoFile != null && photoFile.exists()){
            photoFile.delete();
            photoFile = null;
        }
    }

    private void showPopwindow() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewPop = inflater.inflate(R.layout.dialog_popicture, null);
        final PopupWindow window = new PopupWindow(viewPop, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb000000);
        window.setBackgroundDrawable(dw);
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(view.findViewById(R.id.relative_warninfo), Gravity.BOTTOM, 0, 0);
        LinearLayout linearLayoutall = (LinearLayout) viewPop.findViewById(R.id.linnerall);
        TextView textView_all = (TextView) viewPop.findViewById(R.id.text_removeinfoall);
        TextView textView_remove = (TextView) viewPop.findViewById(R.id.text_removeinfo);
        TextView textView_cancel = (TextView) viewPop.findViewById(R.id.text_cancelremove);

        textView_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();

                window.dismiss();
            }
        });
        textView_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.CAMERA},11);
                    }else {
                        takePhone();
                    }

                }else {
                    takePhone();
                }
                window.dismiss();
            }
        });
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    /**
     * 拍照
     */
    private void takePhone() {
        // 要保存的文件名
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = "photo_" + time;
        // 创建一个文件夹
        String path = Environment.getExternalStorageDirectory() + "/take_photo";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 要保存的图片文件
        File imgFile = new File(file, fileName + ".jpeg");
        // 将file转换成uri
        // 注意7.0及以上与之前获取的uri不一样了，返回的是provider路径
        imgUri = getUriForFile(getActivity(), imgFile);
        photoFile = imgFile;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 添加Uri读取权限
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        // 添加图片保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }


    // 从file中获取uri
    // 7.0及以上使用的uri是contentProvider content://com.rain.takephotodemo.FileProvider/images/photo_20180824173621.jpg
    // 6.0使用的uri为file:///storage/emulated/0/take_photo/photo_20180824171132.jpg
    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        Log.e("Build.VERSION.SDK_INT",Build.VERSION.SDK_INT + "");
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName()+".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){

        }else {
            textView_nolca.setText(AppCons.ADDRESS);   //更新地址
        }
    }
}
