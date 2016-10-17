package dle.appmarket.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import dle.appmarket.mulitipe.DisplayUtils;

/**
 * Created by zsl on 2016/9/20.
 */

/**
 * Created by zsl on 2016/9/21.
 */
public class DownloadView extends View {
    private static final int START=0;
    private static final int DOWNLOAD=1;
    private static final int PAUSE=2;
    private int state=START;
    private int progress=0;
    private Paint paint;

    private Context context;
    private int paddingLeft;
    private int paddingTop;
    private int strokeWidth;
    private int radius;
    private int width;
    private int height;

    public DownloadView(Context context) {
        super(context);
        init();
    }

    public DownloadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255,43,220,224));
        context=getContext();
        //padding
        paddingLeft=DisplayUtils.dip2px(context, 20);
        paddingTop=DisplayUtils.dip2px(context, 20);
        //circle
        radius=DisplayUtils.dip2px(context,20);
        strokeWidth=DisplayUtils.dip2px(context,5);
        //rect
        width = DisplayUtils.dip2px(context, 8);
        height = DisplayUtils.dip2px(context, 16);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        if (progress==360)
            state=START;
        if(progress==0)
            state=START;

        switch (state){
            case START:  draw1(canvas);break;
            case DOWNLOAD:draw2(canvas);break;
            case PAUSE:draw3(canvas);break;
        }

    }

    //暫停界面
    private void draw3(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255,226,229,43));
        canvas.drawRect(paddingLeft-0.5f*width,paddingTop+0.2f*height,0.2f*width+paddingLeft,1.25f*height+paddingTop,paint);
        canvas.drawRect(paddingLeft+0.5f*width,paddingTop+0.2f*height,1.3f*width+paddingLeft,1.25f*height+paddingTop,paint);
        canvas.save();
        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(DisplayUtils.dip2px(context,3));
        canvas.drawCircle(paddingLeft+width/2.0f,height*0.75f+paddingTop,radius,paint);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawArc(paddingLeft+width/2.0f-radius,height*0.75f+paddingTop-radius,paddingLeft+width/2.0f+radius,height*0.75f+paddingTop+radius,0,progress,false,paint);
        canvas.restore();
    }


    //初始界面
    private void draw1(Canvas canvas) {


        canvas.drawRect(paddingLeft,paddingTop,width+paddingLeft,height+paddingTop,paint);
        //绘制三角形
        Path path=new Path();
        path.moveTo(-width/2+paddingLeft,height+paddingTop);
        path.lineTo(width*1.5f+paddingLeft,height+paddingTop);
        path.lineTo(width*0.5f+paddingLeft,height*1.5f+paddingTop);
        path.lineTo(-width/2+paddingLeft,height+paddingTop);
        canvas.drawPath(path,paint);
        //绘制圆环
        canvas.save();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(paddingLeft+width/2.0f,height*0.75f+paddingTop,radius,paint);
        canvas.restore();
    }
    //正在下載
    private void draw2(Canvas canvas) {

        //矩形
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(paddingLeft-0.5f*width,paddingTop+0.2f*height,0.2f*width+paddingLeft,1.25f*height+paddingTop,paint);
        canvas.drawRect(paddingLeft+0.5f*width,paddingTop+0.2f*height,1.3f*width+paddingLeft,1.25f*height+paddingTop,paint);

        canvas.save();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(DisplayUtils.dip2px(context,3));
        canvas.drawCircle(paddingLeft+width/2.0f,height*0.75f+paddingTop,radius,paint);

        paint.setColor(Color.argb(255,43,220,224));
        paint.setStrokeWidth(strokeWidth);
        canvas.drawArc(paddingLeft+width/2.0f-radius,height*0.75f+paddingTop-radius,paddingLeft+width/2.0f+radius,
                height*0.75f+paddingTop+radius,0,progress,false,paint);
        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(progress==100){
                    Toast.makeText(getContext(), "下载已经完成,不用重复下载!", Toast.LENGTH_SHORT).show();
                    return super.onTouchEvent(event);
                }

                if(state==START){
                    state=DOWNLOAD;

                }else if(state==DOWNLOAD){
                    //// TODO: 2016/9/23 未实现暂停功能
                    return true;
                    // state=PAUSE;
                }else{
                    state=DOWNLOAD;
                }

                invalidate();
                break;
        }
        return super.onTouchEvent(event);

    }
    // 下载进度0-100
    public void setProgress(int progress){
        state=DOWNLOAD;
        this.progress=progress*36/10;
        invalidate();
    }
    //获取下载进度
    public int getProgress(){
        return progress*10/36;
    }

    public void setState(int state){
        this.state=state;
        invalidate();
    }


}
