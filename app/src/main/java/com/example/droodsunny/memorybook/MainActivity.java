package com.example.droodsunny.memorybook;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.droodsunny.memorybook.TextUtil.SetAppTypeface;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    public String city = null;
    public List<Note> noteList = null;
    private RecyclerView recyclerView = null;
    private String month;
    private String year;
    private String day;
    private long exitTime = 0;

    private int count = 0;
    // 第一次点击的时间 long型
    private long firstClick = 0;
    // 最后一次点击的时间
    private long lastClick = 0;

    public static void actionStart(Context context, Note note) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("title", note.getTitle());
        intent.putExtra("year", note.getYear());
        intent.putExtra("month", note.getMonth());
        intent.putExtra("day", note.getDay());
        intent.putExtra("content", note.getContent());
        intent.putExtra("location", note.getLocation());
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=19){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.activity_main);


        month = getIntent().getStringExtra("month");
        year = getIntent().getStringExtra("year");
        day = getIntent().getStringExtra("day");

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
    }



    private void ListTitle() {
        noteList = DataSupport.where("year = ? and month = ? and day = ?", year, month, day).find(Note.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListTitle();
        NoteAdapter noteAdapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);
        Log.d("Lifecircle","onResume");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                SetAppTypeface.exit();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                  if(System.currentTimeMillis()-exitTime>500){
                      exitTime=System.currentTimeMillis();
                  }else {
                      finish();
                      overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                  }
        }
       return super.onTouchEvent(event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Lifecircle","ActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }
}

