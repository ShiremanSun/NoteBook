package com.example.droodsunny.memorybook;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.droodsunny.memorybook.Util.TextViewVertical;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LookActivity extends AppCompatActivity {

    private TextView date_text;
    private TextView location_text;
    private TextViewVertical content_text;
    private TextView title_text;
    private ImageButton image_update;
    private ImageButton image_delete;

    private ImageButton save_update;
    private LinearLayout linearLayout;
    private EditText update_title;
    private EditText update_content;
    private EditText update_location;
    private RelativeLayout relativeLayout;

    private int count = 0;
    // 第一次点击的时间 long型
    private long firstClick = 0;
    // 最后一次点击的时间
    private long lastClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        linearLayout=(LinearLayout)findViewById(R.id.linear);


        date_text=(TextView)findViewById(R.id.date);
        location_text=(TextView)findViewById(R.id.location);
        content_text=(TextViewVertical)findViewById(R.id.content);
        title_text=(TextView)findViewById(R.id.titlel);
        image_update=(ImageButton)findViewById(R.id.update);
        image_delete=(ImageButton)findViewById(R.id.delete);
        save_update=(ImageButton)findViewById(R.id.update_note);
        content_text.setTextSize(60);

       // Typeface face=Typeface.createFromAsset(getAssets(),"fonts/textK.ttf");
       // content_text.setTypeface(face);
        content_text.setLineWidth(100);

        save_update=(ImageButton)findViewById(R.id.update_note);
        update_title=(EditText)findViewById(R.id.update_title);
        update_content=(EditText)findViewById(R.id.update_content);
        update_location=(EditText)findViewById(R.id.update_location);
       relativeLayout=(RelativeLayout)findViewById(R.id.relative);


        String date=getIntent().getStringExtra("year")+getIntent().getStringExtra("month")+getIntent().getStringExtra("day");
        String location=getIntent().getStringExtra("location");
        String content=getIntent().getStringExtra("content");
        String title=getIntent().getStringExtra("title");

        List<Note> id= DataSupport.select("id").where("year = ? and month = ? and day = ? and title = ?",
                getIntent().getStringExtra("year"),getIntent().getStringExtra("month"),getIntent().getStringExtra("day"),getIntent().getStringExtra("title")).find(Note.class);

        date_text.setText(date);

        location_text.setText(location);

        content_text.setText(content);
        title_text.setText(title);
        update_title.setText(title);
        update_content.setText(content);
        update_location.setText(location);

        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(Note.class,"year = ? and month = ? and day = ? and title = ? ",
                        getIntent().getStringExtra("year"),getIntent().getStringExtra("month"),getIntent().getStringExtra("day"),getIntent().getStringExtra("title"));
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        image_update.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        image_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                update_title.clearFocus();
                update_content.requestFocus();
                String contents=update_content.getText().toString();
                update_content.setSelection(contents.length());
            }
        });


        save_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatecontent=update_content.getText().toString();
                String updatetitle=update_title.getText().toString();
                String updatelocation=update_location.getText().toString();
                Note note = new Note();
                note.setContent(updatecontent);
                note.setTitle(updatetitle);
                note.setLocation(updatelocation);
                note.updateAll("year = ? and month = ? and day = ? and title = ? ",
                        getIntent().getStringExtra("year"),getIntent().getStringExtra("month"),getIntent().getStringExtra("day"),getIntent().getStringExtra("title"));
                linearLayout.setVisibility(View.GONE);

                location_text.setText(updatelocation);
                content_text.setText(updatecontent);
                title_text.setText(updatetitle);
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });
    }
    public static void actionStart(Context context, Note note){
        Intent intent= new Intent(context,LookActivity.class);
        intent.putExtra("title",note.getTitle());
        intent.putExtra("year",note.getYear());
        intent.putExtra("month",note.getMonth());
        intent.putExtra("day",note.getDay());
        intent.putExtra("content",note.getContent());
        intent.putExtra("location",note.getLocation());
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(linearLayout.getVisibility()==View.VISIBLE){
            linearLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);

        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
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
     /*双击返回*/
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
}
