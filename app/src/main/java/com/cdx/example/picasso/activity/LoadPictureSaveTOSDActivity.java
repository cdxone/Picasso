package com.cdx.example.picasso.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cdx.example.picasso.R;
import com.cdx.example.picasso.utils.FileUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadPictureSaveTOSDActivity extends AppCompatActivity {

    private static final String TAG = LoadPictureSaveTOSDActivity.class.getSimpleName();
    private Context mContext;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_picture_save_tosd);

        mContext = this;
        mIv = findViewById(R.id.iv);

        //1、申请读写SD卡的权限。
        //逻辑：
        //1、第一次安装，然后点击
        //2、由于没有在rationale中执行弹出对话框的逻辑，只是执行了executor.execute();,表示继续执行申请权限的操作
        //3、权限申请成功的时候，执行onGranted的方法
        //4、权限申请失败的时候，执行onDenied的方法
        //4.1、当权限申请失败的时候，会在方法中判断，是否是永远禁止权限了AndPermission.hasAlwaysDeniedPermission
        //如果是永远禁止权限了，弹出来对话框进行设置。
        AndPermission.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)//申请的权限
                .rationale(new Rationale() {

                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        //中间过程就继续执行下去
                        executor.execute();
                    }
                })//设置请求的中间状态,有可能会弹出一个对话框
                .onGranted(new Action() {//授权同意
                    @Override
                    public void onAction(List<String> permissions) {
                        requestPic();
                    }
                })
                .onDenied(new Action() {//授权失败
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        //某些特权永久禁用，可能需要在执行中设置。
                        if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                            showSettingDialog(mContext, permissions);
                        }
                    }
                })
                .start();
    }

    /**
     * 请求图片
     */
    private void requestPic() {

        String picUrl = "http://img2.imgtn.bdimg.com/it/u=3078873712,1340878922&fm=26&gp=0.jpg";
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mIv.setImageBitmap(bitmap);

                String imageName = System.currentTimeMillis() + ".png";
                File picsDir = FileUtil.getExternalStorageDirectory(mContext, "pics");
                if (!picsDir.exists()){
                    picsDir.mkdir();
                }
                File dcimFile = new File(picsDir,imageName);
                Log.e(TAG,dcimFile.toString());
                FileOutputStream ostream = null;
                try {
                    ostream = new FileOutputStream(dcimFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                    Log.e(TAG,"保存成功！");
                } catch (Exception e) {
                    Log.e(TAG,e.toString());
                    e.printStackTrace();
                }
                Toast.makeText(mContext, "保存成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(mContext).load(picUrl).into(target);
    }

    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    /**
     * 1、跳转到设置界面
     * 2、返回的时候，执行onComeback方法。
     */
    private void setPermission() {
        AndPermission.permissionSetting(mContext).execute();
    }
}
