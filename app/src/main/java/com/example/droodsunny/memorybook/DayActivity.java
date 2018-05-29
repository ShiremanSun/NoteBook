package com.example.droodsunny.memorybook;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

public class DayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static DayActivity dayActivity;
    public List<Note> noteList=null;
    private String month;
    private String year;
    private long exitTime = 0;

    private int count = 0;
    // 第一次点击的时间 long型
    private long firstClick = 0;
    // 最后一次点击的时间
    private long lastClick = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        dayActivity=this;


        TextView text_year = (TextView) findViewById(R.id.year);
        TextView text_month = (TextView) findViewById(R.id.month);
        month=getIntent().getStringExtra("month");
        year=getIntent().getStringExtra("year");

        text_year.setText(year);
        text_month.setText(month);
        text_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DayActivity.this,YearActivity.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//关掉所要到的界面所有的的activity
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        text_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.dayRecycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void ListTitle() {
        noteList= DataSupport.where("year = ? and month = ?",year,month).find(Note.class);
        for ( int i = 0 ; i < noteList.size() - 1 ; i ++ ) {
            for ( int j = noteList.size() - 1 ; j > i; j -- ) {
                if (noteList.get(j).getDay().equals(noteList.get(i).getDay())) {
                    noteList.remove(j);
                }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
            ListTitle();
            if(noteList.size()==0){
            Note note = new Note("无笔记","无笔记","无笔记","无笔记","无笔记","无笔记");
            noteList.add(note);
            NoteAdapter noteAdapter;
            noteAdapter = new NoteAdapter(noteList);
            recyclerView.setAdapter(noteAdapter);
        }
        else {
            NoteAdapter noteAdapter;
            noteAdapter = new NoteAdapter(noteList);
            recyclerView.setAdapter(noteAdapter);
        }


    }
       /*意图跳转*/
    public static void actionStart(Context context, Note note){
        Intent intent= new Intent(context,DayActivity.class);
        intent.putExtra("title",note.getTitle());
        intent.putExtra("year",note.getYear());
        intent.putExtra("month",note.getMonth());
        intent.putExtra("day",note.getDay());
        intent.putExtra("content",note.getContent());
        intent.putExtra("location",note.getLocation());
        context.startActivity(intent);

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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(DayActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                MonthActivity.monthActivity.finish();
                YearActivity.yearActivity.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (firstClick != 0 && System.currentTimeMillis() - firstClick > 500) {
                count = 0;
            }
            count++;
            if (count == 1) {
                firstClick = System.currentTimeMillis();
            } else if (count == 2) {
                lastClick = System.currentTimeMillis();
                // 两次点击小于500ms 也就是连续点击
                if (lastClick - firstClick < 500) {
                    //Log.v("Double", "Double");
                    count = 0;
                    firstClick = 0;
                    lastClick = 0;
                    finish();
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            }

        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dayActivity=null;
    }
}
