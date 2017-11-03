package cn.com.ljy.testphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import xl.com.jph.takephoto.app.TakePhoto;
import xl.com.jph.takephoto.app.TakePhotoImpl;
import xl.com.jph.takephoto.model.InvokeParam;
import xl.com.jph.takephoto.model.TContextWrap;
import xl.com.jph.takephoto.model.TResult;
import xl.com.jph.takephoto.permission.InvokeListener;
import xl.com.jph.takephoto.permission.PermissionManager;
import xl.com.jph.takephoto.permission.TakePhotoInvocationHandler;


public class MainActivity extends AppCompatActivity implements TakePhoto.TakeResultListener,InvokeListener {
    private static final int REQUEST_IMAGE = 2;

    private InvokeParam mInvokeParam;
    private TakePhoto mTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadHeadUtil.showChoosePhotoMethod(MainActivity.this, v, mTakePhoto);
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        System.out.println("takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        System.out.println("取消操作");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (mTakePhoto == null){
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return mTakePhoto;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this, type, mInvokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.mInvokeParam = invokeParam;
        }
        return type;
    }
}
