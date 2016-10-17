package dle.appmarket.mulitipe;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;

/**
 * Created by zsl on 2016/9/23.
 */
public class InstallUtils {
    private static final String TAG = "InstallUtils";

    public static void install(String target, Context context){

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.parse("file://" + target),"application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    public static boolean isInstalled(Context context,String packageName){

        PackageManager manager=context.getPackageManager();
        List<PackageInfo> infos=manager.getInstalledPackages(0);
        for (int i = 0; i < infos.size(); i++) {
            if(infos.get(i).packageName.equalsIgnoreCase(packageName))
            return true;
        }

        return false;
    }
    public static void openApp(Context context,String packageName){
     try {
         Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
         context.startActivity(intent);
     }catch (Exception e){
         Toast.makeText(context, "打开app失败!", Toast.LENGTH_SHORT).show();
     }

    }
}
