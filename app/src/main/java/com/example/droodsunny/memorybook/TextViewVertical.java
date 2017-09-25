package com.example.droodsunny.memorybook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DroodSunny on 2017/9/25.
 */

public class TextViewVertical extends View {
    private AttributeSet attrs;
    private Paint paint;
    private Paint.FontMetrics fm;
    private String text="";
    private int fontWidth = 0,fontHeight;// 字宽,字高
    private int viewWidt = 0,viewHeight = 0;// 视图的宽,高

    private float fontSize = 40;// 默认字体大小为24
    private int fontColor= Color.parseColor("#333333");// 默认字体颜色

    public TextViewVertical(Context context){
        super(context);
        initPaint();
        initFontInfo();
    }

    public TextViewVertical(Context context,AttributeSet attrs){
        super(context, attrs);
        this.attrs=attrs;
        initPaint();
        initFontInfo();
    }

    /**设置内容*/
    public void setText(String text){
        this.text = text;
        invalidate();
        requestLayout();
    }

    /**设置字体大小*/
    public final void setTextSize(float size) {
        if (size != paint.getTextSize()) {
            fontSize=size;
            initFontInfo();
            invalidate();
            requestLayout();
        }
    }

    /**设置字体颜色*/
    public final void setTextColor(int color) {
        if (color != paint.getColor()) {
            paint.setColor(color);
            invalidate();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        // 为了初始化的时候获取到控件的高用于判断超出绘制的高度的后换列
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        initMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int columnHeight = 0;// 当前列的高度
        int columnHeightMax = 0;// 缓存最大的高度
        int column = 1;// 列数
        char ch;
        for(int i = 0;i < text.length();i++){
            ch = text.charAt(i);
            if(ch == '\n'){
                column = column + 1;
                columnHeight = 0;
            }else{
                // 超出绘制的高度的后换列显示
                columnHeight += fontHeight;
                if(columnHeight > this.viewHeight){
                    column++;// 换列
                    i--;
                    columnHeight = 0;
                }else{
                    if(columnHeightMax < columnHeight){
                        columnHeightMax = columnHeight;
                    }
                }

            }
        }
        viewWidt = fontWidth * column;// 计算文字总宽度
        viewHeight = columnHeightMax + fontHeight / 2;// 计算最高高度
        int width = resolveSizeAndState_1(viewWidt, widthMeasureSpec, 0);
        int height = resolveSizeAndState_1(viewHeight, heightMeasureSpec, 0);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        drawFont(canvas);
    }

    private void drawFont(Canvas canvas){
        char ch;
        float fontX = fontWidth / 2;
        float fontY = 0;
        for(int i = 0;i < text.length();i++){
            ch = text.charAt(i);
            if(ch == '\n'){
                fontX += fontWidth;// 换列
                fontY = 0;
            }else{
                fontY += fontHeight;
                if(fontY > this.viewHeight){
                    // 超出绘制的高度的后换列显示
                    fontX += fontWidth;// 换列
                    i--;
                    fontY = 0;
                }else{
                    canvas.drawText(String.valueOf(ch), fontX, fontY, paint);
                }

            }
        }
    }

    private void initPaint(){
        paint = new Paint();// 新建画笔
        paint.setTextAlign(Paint.Align.CENTER);// 文字居中
        paint.setAntiAlias(true);// 平滑处理

        if(attrs != null){
            try{
                fontSize = Float.parseFloat(attrs.getAttributeValue(null, "textSize"));// 获取字体大小属性
                fontColor= Integer.parseInt(attrs.getAttributeValue(null, "textColor"));// 获取字体大小属性
                text= attrs.getAttributeValue(null, "text");// 获取字体大小属性
            }catch(Exception e){
            }
        }

    }
    private void initFontInfo(){

        paint.setColor(fontColor);// 默认文字颜色
        paint.setTextSize(fontSize);

        // 获得字宽
        float[] widths = new float[1];
        paint.getTextWidths("正", widths);// 获取单个汉字的宽度
        fontWidth = (int) Math.ceil(widths[0] * 1.1 + 2);

        // 获得字属性
        fm = paint.getFontMetrics();
        fontHeight = (int) (Math.ceil(fm.descent - fm.top) * 0.9);

        //返回包围整个字符串的最小的一个Rect区域,但是在canvas.drawText()不好确定x,y位置
//      Paint pFont = new Paint();
//      Rect rect = new Rect();
//      pFont.getTextBounds(str, 0, 1, rect);
//      strwid = rect.width();
//      strhei = rect.height();

    }
    // 新的api(resolveSizeAndState)无法兼容3.0以下,所以挖出来
    private int resolveSizeAndState_1(int size,int measureSpec,int childMeasuredState){
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch(specMode){
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if(specSize < size){
                    result = specSize | 0x01000000;
                }else{
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & 0xff000000);
    }
}
