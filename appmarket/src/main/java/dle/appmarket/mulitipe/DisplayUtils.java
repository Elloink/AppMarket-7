package dle.appmarket.mulitipe;

import android.content.Context;

/**
 * Created by zsl on 2016/9/3.
 * 屏幕显示转换工具dip与px转换
 */
public class DisplayUtils {
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}
