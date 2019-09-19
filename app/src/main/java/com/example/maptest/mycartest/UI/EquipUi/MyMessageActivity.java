package com.example.maptest.mycartest.UI.EquipUi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.maptest.mycartest.Entity.EditInfo;
import com.example.maptest.mycartest.New.EditResultBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.SwipRecycleView.App;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.BitmapUtil;
import com.example.maptest.mycartest.Utils.CheckNumBer;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.example.maptest.mycartest.test.LoginDataBean;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static com.example.maptest.mycartest.Utils.AppCons.locationListBean;
import static com.example.maptest.mycartest.Utils.AppCons.loginDataBean;


/**
 * Created by ${Author} on 2017/3/21.
 * Use to 修改用户信息
 */

public class MyMessageActivity extends BaseActivity implements View.OnClickListener{
    private ImageView imageView_quit,imageView_photo,imageView_showcode;
    private EditText editText_phone,editText_addr;
    private TextView textView_comp, editText_user,textView_id;
    private String account,tell,addr,acct;
    private String temId,agtId,pwd;
    EditInfo editInfo;
    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;
    private Bitmap bm = null;
    public static RequestQueue queue;
//    private LoginDataBean loginDataBean;
    private String URL;
    private  Bitmap bitmap;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_SHORT).show();
                    reMation();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(),"修改失败,昵称重复",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymessage);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        initView();
        initClick();
    }
    private void initClick() {
        imageView_quit.setOnClickListener(this);
        imageView_photo.setOnClickListener(this);
        textView_id.setOnClickListener(this);
        textView_comp.setOnClickListener(this);
    }

    /**
     * 初始化控件，显示用户信息
     */
    private void initView() {
        textView_id = (TextView) findViewById(R.id.text_id);
        textView_comp = (TextView) findViewById(R.id.text_mycomplete);
        editText_addr = (EditText) findViewById(R.id.edit_location);
        editText_phone = (EditText) findViewById(R.id.edit_tell);
        editText_user = (TextView) findViewById(R.id.edit_user);
        imageView_quit = (ImageView) findViewById(R.id.image_quitmy);
        imageView_photo = (ImageView) findViewById(R.id.image_myphoto);

        imageView_showcode = (ImageView) findViewById(R.id.image_showcode);
        bm = (Bitmap) getIntent().getExtras().get("photos");
        editText_user.setText(AppCons.loginDataBean.getData().getNickname());
        editText_phone.setText(AppCons.loginDataBean.getData().getTelephone());
        editText_addr.setText(AppCons.loginDataBean.getData().getAddress());
        if (bm != null)
            BitmapUtil.getCropped2Bitmap(bm,imageView_photo);
        textView_id.setText(AppCons.loginDataBean.getData().getUsername());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_quitmy:
                backs();
                break;
            case R.id.image_myphoto:
                showPopwindow();
                break;
            case R.id.text_mycomplete:      //正则表达式判断字符串格式
                if ( !editText_phone.getText().toString().isEmpty() && CheckNumBer.rexCheckTell(editText_phone.getText().toString())){
                    try {
                        initDates();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"请检查输入的内容是否合法或为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        createCode();
    }

    /**
     * 生成二维码
     */
    private void createCode() {
        JSONObject jsonObject = new JSONObject();
        Log.d("code1","1");
//        if (temId.equals("")){
//            jsonObject.put("name",agtId);
//            Log.d("code1","2");
//        }else {
//            jsonObject.put("name",temId);
//            Log.d("code1","3");
//        }
        jsonObject.put("name",acct);            //用户名
        jsonObject.put("password",pwd);         //密码
        Log.d("code1","4");
        bitmap= com.example.maptest.mycartest.zxing.encoding.EncodingUtils.createQRCode(jsonObject.toString(), 500, 500, null);
        Log.d("code1","5");
        imageView_showcode.setImageBitmap(bitmap);
        Log.d("code1","6");
    }

    /**
     * 〈修改用户资料〉
     * 〈功能详细描述〉
     * 将用户资料发送到服务器
     */
    private void initDates() throws UnsupportedEncodingException {
//        queue = Volley.newRequestQueue(getApplicationContext());
        tell = editText_phone.getText().toString();  //电话
        addr = editText_addr.getText().toString();   //地址
        if (tell.equals(loginDataBean.getData().getTelephone()) && addr.equals(loginDataBean.getData().getAddress())){
            Toast.makeText(getApplicationContext(),"信息未发生修改",Toast.LENGTH_SHORT).show();
        }else {
            if (!tell.equals(loginDataBean.getData().getTelephone()) && !addr.equals(loginDataBean.getData().getAddress())){
                editInfo = new EditInfo(loginDataBean.getData().getId(),loginDataBean.getData().getRole(),tell,addr);
            }else if (tell.equals(loginDataBean.getData().getTelephone()) && !addr.equals(loginDataBean.getData().getAddress())){
                editInfo = new EditInfo(loginDataBean.getData().getId(),loginDataBean.getData().getRole(),addr);
            }else if (!tell.equals(loginDataBean.getData().getTelephone()) && addr.equals(loginDataBean.getData().getAddress())){
                editInfo = new EditInfo(loginDataBean.getData().getId(),tell,loginDataBean.getData().getRole());
            }
            Gson gson=new Gson();
            String obj = gson.toJson(editInfo);
            NewHttpUtils.modifyMessage(obj,MyMessageActivity.this,messageCallback);
        }

    }
        private ResponseCallback messageCallback = new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                if (object != null){
                    EditResultBean bean = (EditResultBean) object;
                    if (bean.getMeta().getMessage().equals("200")){
                        AppCons.loginDataBean.getData().setTelephone(tell);
                        AppCons.loginDataBean.getData().setAddress(addr);
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                    }else if (bean.getMeta().getMessage().equals("252")){   //重复
                      Toast.makeText(getApplicationContext(),"用户昵称重复",Toast.LENGTH_SHORT).show();
                    }else if (bean.getMeta().getMessage().equals("251")){   //重复
                        Toast.makeText(getApplicationContext(),"用户名重复",Toast.LENGTH_SHORT).show();
                    }else if (bean.getMeta().getMessage().equals("253")){   //重复
                        Toast.makeText(getApplicationContext(),"车牌号重复",Toast.LENGTH_SHORT).show();
                    }else {
                        Message message = new Message();
                        message.what = 4;
                        handler.sendMessage(message);
                    }
                }else {
                    Message message = new Message();
                    message.what = 4;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void FailCallBack(Object object) {
                Message message = new Message();
                message.what = 4;
                handler.sendMessage(message);
            }
        };

    /**
     * 返回给前一个界面显示
     */
    private void reMation(){
        tell = editText_phone.getText().toString();
        addr = editText_addr.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("tell",tell);
        bundle.putString("addr",addr);
        bundle.putParcelable("bitmap",bm);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(0,intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            backs();
        }
        return true;

    }

    private void backs() {
        Intent intent = new Intent();
        setResult(0,intent);

        if(bm != null && !bm.isRecycled()){
            bm.recycle();
            bm = null;
        }
        if (queue != null)
            queue = null;
        temId = null;
        addr = null;
        agtId = null;
        tell = null;
        account = null;
        if (bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
        }
        bitmap = null;
        finish();
        System.gc();
    }
    /**
     * 〈popwindow〉
     * 〈功能详细描述〉
     * 提示用户信息
     */
    private void showPopwindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_popmenu,null);
        // 得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //pop弹出可点击
        window.setFocusable(true);
        //实例化一个半透明的颜色
        ColorDrawable dw = new ColorDrawable(0xb000000);
        window.setBackgroundDrawable(dw);
        //设置显示和消失的动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        //在底部显示
        window.showAtLocation(MyMessageActivity.this.findViewById(R.id.image_myphoto), Gravity.BOTTOM,0,0);
        //pop里面的button要可以点击
        TextView textView_carme = (TextView) view.findViewById(R.id.text_getcareme);
        TextView textView_ablum = (TextView) view.findViewById(R.id.text_album);
        TextView textView_cancel = (TextView) view.findViewById(R.id.text_cancel);
        textView_carme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照选择
                chooseFromCamera();
                Toast.makeText(MyMessageActivity.this,"拍照",Toast.LENGTH_SHORT).show();
            }
        });
        textView_ablum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //从相册选取
                chooseFromGallery();
                Toast.makeText(MyMessageActivity.this,"从相册选择",Toast.LENGTH_SHORT).show();
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
                System.out.print("消失");
            }
        });

    }

    /**
     * 拍照选择图片
     */
    private void chooseFromCamera() {
        //构建隐式Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //调用系统相机
        startActivityForResult(intent, CAMERA_CODE);
    }
    /**
     * 从相册选择图片
     */
    private void chooseFromGallery() {
        //构建一个内容选择的Intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //设置选择类型为图片类型
        intent.setType("image/*");
        //打开图片选择
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CAMERA_CODE:
                //用户点击了取消
                if(data == null){
                    return;
                }else{
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        //获得拍的照片
                        Bitmap bm = extras.getParcelable("data");
                        //将Bitmap转化为uri
                        Uri uri = saveBitmap(bm, "temp");
                        //启动图像裁剪
                        startImageZoom(uri);
                    }
                }
                break;
            case GALLERY_CODE:
                if (data == null){
                    return;
                }else{
                    //用户从图库选择图片后会返回所选图片的Uri
                    Uri uri;
                    //获取到用户所选图片的Uri
                    uri = data.getData();
                    //返回的Uri为content类型的Uri,不能进行复制等操作,需要转换为文件Uri
                    uri = convertUri(uri);
                    startImageZoom(uri);
                }
                break;
            case CROP_CODE:
                if (data == null){
                    return;
                }else{
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        //获取到裁剪后的图像
                         bm= extras.getParcelable("data");
                        imageView_photo.setImageBitmap(bm);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 将content类型的Uri转化为文件类型的Uri
     * @param uri
     * @return
     */
    private Uri convertUri(Uri uri){
        InputStream is;
        try {
            //Uri ----> InputStream
            is = getContentResolver().openInputStream(uri);
            //InputStream ----> Bitmap
            Bitmap bm = BitmapFactory.decodeStream(is);
            //关闭流
            is.close();
            return saveBitmap(bm, "temp");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将Bitmap写入SD卡中的一个文件中,并返回写入文件的Uri
     * @param bm
     * @param dirPath
     * @return
     */
    private Uri saveBitmap(Bitmap bm, String dirPath) {
        //新建文件夹用于存放裁剪后的图片
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/" + dirPath);
        if (!tmpDir.exists()){
            tmpDir.mkdir();
        }

        //新建文件存储裁剪后的图片
        File img = new File(tmpDir.getAbsolutePath() + "/avator.png");
        try {
            //打开文件输出流
            FileOutputStream fos = new FileOutputStream(img);
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            //刷新输出流
            fos.flush();
            //关闭输出流
            fos.close();
            //返回File类型的Uri
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过Uri传递图像信息以供裁剪
     * @param uri
     */
    private void startImageZoom(Uri uri){
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bm != null && !bm.isRecycled()){
            bm.recycle();
            bm = null;
        }
        if (queue != null)
            queue = null;
        temId = null;
        addr = null;
        agtId = null;
        tell = null;
        account = null;
        bitmap = null;
        acct = null;
        pwd = null;
    }

//    private boolean numBers(String str){
//        try {
//            int num =Integer.valueOf(str);//把字符串强制转换为数字
//            Log.e("number",num +" ---- " + str);
//            return true;//如果是数字，返回True
//        } catch (Exception e) {
//            return false;//如果抛出异常，返回False
//        }
//    }
}
