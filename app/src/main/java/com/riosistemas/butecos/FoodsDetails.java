package com.riosistemas.butecos;

import java.net.MalformedURLException;
import java.util.ArrayList;

import com.riosistemas.butecos.ImageThreadLoader.ImageLoadedListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FoodsDetails extends Activity {
    private ProgressDialog pd;
    SimpleCursorAdapter sAdapter;
    private ListView lvFoods;
    private ArrayList<FoodsInfo> mfoods;
    private ImageThreadLoader imageLoader;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.foods_details);
            Bundle extra = getIntent().getExtras();
            if (extra != null) {
                Long mRowId = extra.getLong("key",0);
                if (mRowId != 0) {
                    String[] FROM = {"_id","foo_name","foo_description","foo_image","res_id","foo_image_small","foo_curtir","foo_ano","foo_winner","foo_city","foo_state"};
                    String[] FROMRESTAURANT = {"_id","res_name","res_address","res_number","res_neiborhood","res_image","res_lat","res_long","res_city","res_state","res_zip","res_ano","res_obs","res_curtir","res_phone","res_visited"};
                    String WHERE = "_id = ?";
                    String[] ARGS = {mRowId.toString()};
                    String WHERERESTAURANT = "_id = ?";
                    DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(this);
                    try {
                        dbAdapter.createDataBase();
                        dbAdapter.openDataBase();
                        Cursor c = dbAdapter.selectRecordsFromDB("foods", FROM,WHERE,ARGS,null,null,null);
                        c.moveToFirst();
                        String foo_name = c.getString(c.getColumnIndex("foo_name"));
                        String foo_description = c.getString(c.getColumnIndex("foo_description"));
                        String foo_image = c.getString(c.getColumnIndex("foo_image"));
                        int res_id = c.getInt(c.getColumnIndex("res_id"));
                        String foo_image_small = c.getString(c.getColumnIndex("foo_image_small"));
                        int foo_curtir = c.getInt(c.getColumnIndex("foo_curtir"));
                        String foo_winner = c.getString(c.getColumnIndex("foo_winner"));
                        String[] ARGSRESTAURANT = {Integer.toString(res_id)};
                        //ARGSRESTAURANT.add(Integer.toString(res_id));
                        Cursor d = dbAdapter.selectRecordsFromDB("restaurants", FROMRESTAURANT, WHERERESTAURANT, ARGSRESTAURANT,null, null,null);
                        d.moveToFirst();
                        String res_name = d.getString(d.getColumnIndex("res_name"));
                        String res_address = d.getString(d.getColumnIndex("res_address"));
                        String res_number = d.getString(d.getColumnIndex("res_number"));
                        String res_neiborhood = d.getString(d.getColumnIndex("res_neiborhood"));
                        String res_city = d.getString(d.getColumnIndex("res_city"));
                        String res_state = d.getString(d.getColumnIndex("res_state"));
                        String res_zip = d.getString(d.getColumnIndex("res_zip"));
                        this.setTitle(foo_name);

                        TextView txtFooName =(TextView)findViewById(R.id.foo_name);
                        TextView txtFooDescription = (TextView)findViewById(R.id.foo_description);
                        imageLoader = new ImageThreadLoader(this);
                        final ImageView image = (ImageView)findViewById(R.id.imageViewFood);
                        Bitmap cachedImage = null;
                        try {
                            cachedImage = imageLoader.loadImage(foo_image,new ImageLoadedListener()  {

                                public void imageLoaded(Bitmap imageBitmap) {
                                    // TODO Auto-generated method stub
                                    image.setImageBitmap(imageBitmap);

                                }
                            });

                        } catch (MalformedURLException e) {
                            // TODO: handle exception
                            //Log.e("Fooods.class", "Bad remote image URL: " + foo_image+" posiÁ„o ", e);

                        }
                        if (cachedImage !=null) {
                            image.setImageBitmap(cachedImage);
                        }
                        txtFooName.setText(foo_name);
                        txtFooDescription.setText(foo_description);

                        TextView txtResName = (TextView)findViewById(R.id.res_name);
                        TextView txtResAddress = (TextView)findViewById(R.id.res_address);
                        txtResName.setText(res_name);
                        txtResAddress.setText(res_address+","+res_number+"\n"+res_neiborhood+" - "
                                +res_city+" - "+res_state+"\n"+res_zip);

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }



        } catch (Exception e) {
            // TODO: handle exception
        }

    }

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
    @Override
    public void onDestroy(){
        super.onDestroy();

    }

}
