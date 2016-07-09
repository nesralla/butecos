package com.riosistemas.butecos;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.provider.Settings.System;
/**
 * Created by nesralla on 7/6/16.
 */

public class Settings extends Activity {

    private String city ;
    private String ano;
    private String email;
    private String imei;
    private final String[] FROM = {"_id","pre_name","pre_email","pre_city","pre_ano"};
    private final String WHERE = "pre_name = ?";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        this.setTitle("ConfiguraÁıes");
        Spinner sCity = (Spinner)findViewById(R.id.spinnerCity);
        ArrayAdapter<?> cidades = ArrayAdapter.createFromResource(this, R.array.citys, android.R.layout.simple_spinner_item);
        cidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCity.setAdapter(cidades);

        Spinner sYear = (Spinner)findViewById(R.id.spinnerAno);
        ArrayAdapter<?> anos = ArrayAdapter.createFromResource(this, R.array.anos, android.R.layout.simple_spinner_item);
        anos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sYear.setAdapter(anos);

        imei = System.getString(this.getContentResolver(), System.ANDROID_ID);
        String[] ARGS = {imei};
        DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(Settings.this);
        dbAdapter.openDataBase();
        Cursor c = dbAdapter.selectRecordsFromDB("preferences", FROM, WHERE, ARGS, null,null,null);
        if (c.getCount() != 0) {
            if (c.moveToFirst()) {
                String _id = c.getString(c.getColumnIndex("_id"));
                String pre_name = c.getString(c.getColumnIndex("pre_name"));
                String pre_city = c.getString(c.getColumnIndex("pre_city"));
                String pre_email = c.getString(c.getColumnIndex("pre_email"));
                String pre_ano = c.getString(c.getColumnIndex("pre_ano"));
                ArrayAdapter cidades2 = (ArrayAdapter) sCity.getAdapter();
                int posCidade2 = cidades2.getPosition(pre_city);
                sCity.setSelection(posCidade2);
                ArrayAdapter anos2 = (ArrayAdapter)sYear.getAdapter();
                int posAnos2 = anos2.getPosition(pre_ano);
                sYear.setSelection(posAnos2);
            }

        }
        c.close();
        c.deactivate();
        sCity.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                city = arg0.getItemAtPosition(arg2).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //pref.setPre_city("Todas") ;
            }
        });
        sYear.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                ano = arg0.getItemAtPosition(arg2).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //pref.setPre_ano("Todos");
            }

        });

        Button btnSalvar = (Button)findViewById(R.id.salvar);
        btnSalvar.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String[] ARGS = {imei};
                DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(Settings.this);
                dbAdapter.openDataBase();
                Cursor c = dbAdapter.selectRecordsFromDB("preferences", FROM, WHERE, ARGS, null,null,null);
                ContentValues initialValues = new ContentValues();
                initialValues.put("pre_name",imei);
                initialValues.put("pre_email","na");
                initialValues.put("pre_city",city);
                initialValues.put("pre_ano",ano);
                if (c.getCount() == 0) {
                    long n = dbAdapter.insertRecordsInDB("preferences", null, initialValues);
                    Toast.makeText(Settings.this, n+" - Preferencias aplicadas !", Toast.LENGTH_SHORT).show();
                    Preferences.setPre_city(city);
                    Preferences.setPre_ano(ano);
                }else {
                    long n = dbAdapter.updateRecordsInDB("preferences", initialValues, WHERE, ARGS);
                    Toast.makeText(Settings.this, n+" - Preferencias atualizadas !", Toast.LENGTH_SHORT).show();
                    Preferences.setPre_city(city);
                    Preferences.setPre_ano(ano);
                }
                c.close();
                c.deactivate();
                finish();
                Intent i = new Intent();
                i.setClassName("com.riosistemas.butecos", "com.riosistemas.butecos.Splash");
                startActivity(i);
            }
        });
        Button btnFechar = (Button)findViewById(R.id.close);
        btnFechar.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                Intent i = new Intent();
                i.setClassName("com.riosistemas.butecos", "com.riosistemas.butecos.Splash");
                startActivity(i);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.about:
                startActivity(new Intent(this,About.class));
                return true;
            case R.id.help:
                startActivity(new Intent(this,Help.class));
                return true;
            case R.id.exit:
                finish();


        }
        return false;
    }
}
