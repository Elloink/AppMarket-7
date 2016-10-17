package dle.appmarket.mulitipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zsl on 2016/9/20.
 */
public class InputStream2String {
    public static String read(InputStream in) throws IOException {
        String str;
        StringBuilder buffer = new StringBuilder();
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        while((str=(br.readLine()))!=null){
          buffer.append(str);
        }

    return buffer.toString();
    }
}
