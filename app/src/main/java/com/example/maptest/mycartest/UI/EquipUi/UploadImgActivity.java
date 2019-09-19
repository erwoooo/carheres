package com.example.maptest.mycartest.UI.EquipUi;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ${Author} on 2019/4/1.
 * Use to上传图片
 */

public class UploadImgActivity  extends BaseActivity{
    private Button button_local,button_photo,button_upload;
    private ImageView image_yl;
    private String path;
    private Uri uri;
    File file;
    private String mPublicPhotoPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        path = getIntent().getStringExtra("url");



        image_yl = (ImageView) findViewById(R.id.image_yl);

        Log.e("image_yl","path");
        uri = Uri.parse(path);
        file = new File(path);
        Log.e("uri",uri.toString());
        file.mkdirs();
        path = uri.getPath();
//            image_yl.setImageURI(uri);
        image_yl.setImageURI(uri);

        button_local = (Button) findViewById(R.id.button_local);
        button_photo = (Button) findViewById(R.id.button_phtot);
        button_upload = (Button) findViewById(R.id.button_upload);
        button_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromAlbum();
            }
        });
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.CAMERA},11);
                    }else {
                        startTake();
                    }

                }else {
                    startTake();
                }

            }
        });
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadFile();
            }


        });
    }


    private void upLoadFile() {
        if (file != null){
            Log.e("file",file.length() + " ; " + file.getTotalSpace() + " ; " + file.getParent());
            NewHttpUtils.exportPic(AppCons.locationListBean.getTerminalID(), file, this, new ResponseCallback() {
                @Override
                public void TaskCallBack(Object object) {

                }

                @Override
                public void FailCallBack(Object object) {

                }
            });
        }
    }

    public void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, 1);
    }
    //获取本地图片



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("grantResults[0]",grantResults[0] + "");
        if (requestCode == 11 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startTake();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String img_url = uri.getPath();//这是本机的图片路径
                image_yl.setImageURI(uri);
                file = new File(img_url);
                file.mkdirs();
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
//                 image_yl.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }
        }else {
            if (resultCode != Activity.RESULT_OK) return;
            uri = Uri.parse(mPublicPhotoPath);
            Log.e("uri",uri.toString());
            path = uri.getPath();
//            image_yl.setImageURI(uri);
            image_yl.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
       /*     ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                Log.e("bitmap",bitmap.toString());
                image_yl.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void startTake() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断是否有相机应用
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //创建临时图片文件
            File photoFile = null;
            try {
                photoFile = createPublicImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //设置Action为拍照
            if (photoFile != null) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/test/" + System.currentTimeMillis() + ".jpg");
                    file.getParentFile().mkdirs();

                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                //这里加入flag
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
                    Uri photoURI = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", file);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, 2);
                }else{
                    Uri photoURI = Uri.parse(photoFile.getPath());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, 2);
                }

            }
        }
        //将照片添加到相册中
        galleryAddPic(mPublicPhotoPath, this);
    }

    /**
     * 照片添加到相册
     * @param mPublicPhotoPath
     * @param context
     */
    public static void galleryAddPic(String mPublicPhotoPath, Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mPublicPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
    private File createPublicImageFile() throws IOException {
        File path = null;
        if (hasSdcard()) {
            path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);
        }
        Date date = new Date();
        String timeStamp = getTime(date, "yyyyMMdd_HHmmss", Locale.CHINA);
        String imageFileName = "Camera/" + "IMG_" + timeStamp + ".jpg";
        File image = new File(path, imageFileName);
        mPublicPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getTime(Date date, String mode, Locale locale) {
        SimpleDateFormat format = new SimpleDateFormat(mode, locale);
        return format.format(date);
    }

    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

}
