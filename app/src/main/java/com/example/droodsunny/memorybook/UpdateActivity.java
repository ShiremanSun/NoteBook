package com.example.droodsunny.memorybook;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class UpdateActivity extends AppCompatActivity {

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        final EditText update_title = (EditText) findViewById(R.id.update_title);
        final EditText update_content = (EditText) findViewById(R.id.update_content);
        final EditText update_location = (EditText) findViewById(R.id.update_location);
        ImageButton update = (ImageButton) findViewById(R.id.update_note);


        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String location = getIntent().getStringExtra("location");
        id = getIntent().getIntExtra("id", 1);
        update_title.setText(title);
        update_content.setText(content);
        update_location.setText(location);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("return_title", update_title.getText().toString());
                intent.putExtra("return_content", update_content.getText().toString());
                intent.putExtra("return_location", update_location.getText().toString());
                setResult(RESULT_OK, intent);
                Note note = new Note();
                note.setContent(update_content.getText().toString());
                note.setLocation(update_location.getText().toString());
                note.setTitle(update_title.getText().toString());
                Log.d("id",id+"");
                note.update(id);
               finish();
               LookActivity.sLookActivity.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });


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
