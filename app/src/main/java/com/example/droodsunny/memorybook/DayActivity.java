package com.example.droodsunny.memorybook;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class DayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static DayActivity dayActivity;
    public List<Note> noteList=null;
    private TextView text_year;
    private TextView text_month;
    private String month;
    private String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        dayActivity=this;


        text_year=(TextView)findViewById(R.id.year);
        text_month=(TextView)findViewById(R.id.month);
        month=getIntent().getStringExtra("month");
        year=getIntent().getStringExtra("year");

        text_year.setText(year);
        text_month.setText(month);

        text_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DayActivity.this,YearActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//关掉所要到的界面的activity
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
