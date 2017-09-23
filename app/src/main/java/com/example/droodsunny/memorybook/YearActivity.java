package com.example.droodsunny.memorybook;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class YearActivity extends AppCompatActivity {

    public List<Note> noteList=null;
    private SharedPreferences preferences;
    private RecyclerView recyclerView=null;
    public  static SQLiteDatabase db=null;
    public static YearActivity yearActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
        yearActivity=this;
        db= LitePal.getDatabase();
        recyclerView=(RecyclerView)findViewById(R.id.yearRecycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        /*第一次运行APP时初始化数据*/
        preferences = getSharedPreferences("count", MODE_WORLD_READABLE);
        int count = preferences.getInt("count", 0);
        if(count==0){
            Note note= new Note("二零一七年","九月","二十三日","第一篇笔记","第一篇笔记","大连");
            note.save();
            Note note1=new Note("二零一六年","九月","二十二日","第一篇笔记","第一篇笔记","大连");
            note1.save();
        }
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", ++count);
        //提交修改
        editor.commit();

    }
    private void ListTitle(){
       noteList= DataSupport.findAll(Note.class);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ListTitle();
        NoteAdapter noteAdapter=new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);
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
}
