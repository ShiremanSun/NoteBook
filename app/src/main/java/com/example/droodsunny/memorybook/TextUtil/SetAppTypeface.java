package com.example.droodsunny.memorybook.TextUtil;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.example.droodsunny.memorybook.Note;

import org.litepal.LitePal;

import java.lang.reflect.Field;
/**
 * Created by DroodSunny on 2017/9/27.
 */
public class SetAppTypeface extends Application {
    public static Typeface typeFace;
    private SharedPreferences preferences;
    @Override
    public void onCreate() {
        super.onCreate();
       //LitePal数据库初始化
        LitePal.initialize(this);
        Initdata();
        setTypeface();
    }
    public void setTypeface(){
        //华文彩云，加载外部字体assets/front/fonts/textK.ttf
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/textK.ttf");
        try
        {
            Field field_3 = Typeface.class.getDeclaredField("SANS_SERIF");
            field_3.setAccessible(true);
            field_3.set(null, typeFace);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
    public void Initdata(){
         /*第一次运行APP时初始化数据*/
        String notice="由于此应用程序需要获取使用者所在城市，所以请打开设备的网络连接授予应用获取位置的权限。" +"\n"+
                "此应用操作简单，双击空白处返回上一级，双击返回键退出应用。" +"\n"+
                "使用愉快！";
        String first="那时我们有梦， \n" +
                "关于文学， \n" +
                "关于爱情， \n" +
                "关于穿越世界的旅行。 \n" +
                "如今我们深夜饮酒， \n" +
                "杯子碰到一起， \n" +
                "都是梦破碎的声音。\n";
        String second="我和这个世界不熟。这并非是我安静的原因。" +
                "我依旧有很多问题，问南方问故里，问希望，问距离。" +
                "我和这个世界不熟。这并非是我绝望的原因。" +
                "我依旧有很多热情，给分开，给死亡，给昨天，给安寂。我和这个世界不熟。" +
                "这并非是我虚假的原因。我依旧有很多真诚，离不开，放不下，活下去，爱得起。";
        String third="谁校对时间 \n" +
                "谁就会突然老去";
        preferences = getSharedPreferences("count", MODE_PRIVATE);
        int count = preferences.getInt("count", 0);
        if(count==0){
            Note note1=new Note("用户须知","用户须知","用户须知",notice,"用户须知","于大连市");
            note1.save();
            Note note2=new Note("二零一六年","八月","八日",first,"关于文学","于北京市");
            note2.save();

            Note note3=new Note("二零一七年","九月","二十五日",second,"我和这个世界并不熟","于大连");

            Note note4 = new Note("二零一七年","八月","十五日",third,"无题","于大连");
            note4.save();
            note3.save();

        }
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", ++count);
        //提交修改
        editor.apply();
        //editor.commit();

    }


}
