package dle.appmarket.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dle.appmarket.R;

/**
 * Created by dle on 2016/9/22.
 */
public class FirstActivity extends AppCompatActivity implements View.OnClickListener
{
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private Button btn;
    private EditText edt;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_first_ip);

        BindsViews();

    }

    private void BindsViews()
    {
        sp = getSharedPreferences("dle", MODE_PRIVATE);
        ed = sp.edit();

        edt = (EditText) findViewById(R.id.ip_in);
        btn = (Button) findViewById(R.id.ip_input);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ip_input:

                if (NuLLCheck())
                {
                    ed.putString("Ip", edt.getText().toString().trim());
                    ed.commit();

                    pd = new ProgressDialog(FirstActivity.this);
                    pd.setCancelable(true);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setMessage("稍后...");
                    pd.show();




                    Intent in = new Intent();
                    in.setClass(FirstActivity.this, MainActivity.class);
                    startActivity(in);

                    pd.cancel();

                    finish();
                }
                break;
        }
    }

    private Boolean NuLLCheck()
    {
        String IP = edt.getText().toString().trim();
        if (IP.length() == 0 || IP.equals(null))
        {
            Toast.makeText(FirstActivity.this, "输入IP", Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;

    }
}
