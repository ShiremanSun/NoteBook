package com.example.droodsunny.memorybook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LocationClient mLocationClient=null;
    private SharedPreferences preferences;
    private RecyclerView recyclerView=null;
    public String city=null;
    public List<Note> noteList=null;
    private String month;
    private String year;
    private String day;
   public static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity=this;

        MyLocationListener myListener=new MyLocationListener();
        mLocationClient=new LocationClient(this);
        LocationClientOption option=new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();

        month=getIntent().getStringExtra("month");
        year=getIntent().getStringExtra("year");
        day=getIntent().getStringExtra("day");

        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

    }
    /*点击空白区域添加笔记*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            noteList.clear();
            Intent intent=new Intent(MainActivity.this,EditActivity.class);
            startActivity(intent);
            return super.onTouchEvent(event);
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            return true;
        }
        return super.onTouchEvent(event);
    }
    /*
            * 实现定位功能*/
    private class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            try{
                city= bdLocation.getCity();

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

/*沉浸式状态栏*/
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
    private void ListTitle(){
        noteList= DataSupport.where("year = ? and month = ? and day = ?",year,month,day).find(Note.class);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ListTitle();
        NoteAdapter noteAdapter=new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);
    }
    public static void actionStart(Context context, Note note){
        Intent intent= new Intent(context,MainActivity.class);

        intent.putExtra("title",note.getTitle());
        intent.putExtra("year",note.getYear());
        intent.putExtra("month",note.getMonth());
        intent.putExtra("day",note.getDay());
        intent.putExtra("content",note.getContent());
        intent.putExtra("location",note.getLocation());
        context.startActivity(intent);
    }
}
