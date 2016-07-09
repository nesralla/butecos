package com.riosistemas.butecos;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Created by nesralla on 7/6/16.
 */

public class Help extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        this.setTitle("");
        Button btnFechar = (Button)findViewById(R.id.close);
        btnFechar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();

    }


}
