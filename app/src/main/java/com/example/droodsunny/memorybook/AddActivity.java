package com.example.droodsunny.memorybook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.Calendar;


public class AddActivity extends AppCompatActivity {


    private String location;
    private TextView city=null;
    private TextView date=null;
    private EditText title_edit;
    private EditText content_edit;
    private Note note;
    private ImageButton add_Note;
    public String citystring;
   public  String dates;
    public LocationClient mLocationClient = null; //初始化LocationClient类
    private static final int BAIDU_READ_PHONE_STATE =100;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        add_Note=(ImageButton)findViewById(R.id.addNote);
        date=(TextView)findViewById(R.id.date);
        city=(TextView)findViewById(R.id.city);
        title_edit=(EditText)findViewById(R.id.edit_title);
        content_edit=(EditText)findViewById(R.id.edit_content);


        note=new Note();

        if(Build.VERSION.SDK_INT>=23){
            showContacts();
        }
        else {
            dingwei();
        }

       java.util.Calendar c= java.util.Calendar.getInstance();
        int  year = c.get(Calendar.YEAR);
        int   month = c.get(Calendar.MONTH);
        int  day = c.get(Calendar.DAY_OF_MONTH);

        final String years=NumberFormatUtil.formatInteger(year);

        final String months=NumberFormatUtil.mdformatInteger(month+1);

        final String days= NumberFormatUtil.mdformatInteger(day);
        dates=years+"年"+months+"月"+ days+"日";
        date.setText(dates);

        add_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=title_edit.getText().toString();
                String content=content_edit.getText().toString();
                location=city.getText().toString();
                if(TextUtils.isEmpty(title)||TextUtils.isEmpty(content)) {
                    Toast.makeText(AddActivity.this,"请输入完整笔记",Toast.LENGTH_SHORT).show();
                }else{
                    Note note = new Note();
                    note.setYear(years + "年");
                    note.setMonth(months + "月");
                    note.setDay(days + "日");
                    note.setLocation("于"+location);
                    note.setTitle(title_edit.getText().toString());
                    note.setContent(content_edit.getText().toString());
                    note.save();
                    finish();
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    Toast.makeText(AddActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    /*安卓6.0以上系统获取权限*/
    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(AddActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            dingwei();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                   dingwei();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //*沉浸式状态栏*//**//*
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    /* 实现定位功能*/
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            try{
                citystring= bdLocation.getCity();
                city.setText(citystring);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }
    private void dingwei(){
        BDAbstractLocationListener myListener = new MyLocationListener();
        mLocationClient=new LocationClient(this);
        LocationClientOption option=new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}

