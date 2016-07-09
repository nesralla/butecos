package com.riosistemas.butecos;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import com.riosistemas.butecos.ImageThreadLoader.ImageLoadedListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * Created by nesralla on 7/6/16.
 */

public class Foods extends Activity {

    private ListView lvfoods;
    private ArrayList<FoodsInfo> mfoods;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foods);
        mfoods = getFoods("");
        lvfoods = (ListView)findViewById(R.id.listFoods);
        lvfoods.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                FoodDetails(arg2);


            }
        });
        lvfoods.setAdapter(new ListAdapter(this,R.id.listFoods,mfoods));
        ListAutoCompleteFoods();



    }
    public void FoodDetails(int arg3){
        //modificar aqui !
        Intent its = new Intent(getBaseContext(),FoodsDetails.class);
        if (mfoods.size() > 0) {
            for (int i = 0; i < mfoods.size(); i++) {
                if (i == arg3) {
                    Bundle b = new Bundle();
                    b.putLong("key",mfoods.get(i).id);
                    its.putExtras(b);
                    startActivity(its);

                }

            }


        }

    }
    public void ListAutoCompleteFoods(){
        String pre_city = "";
        String pre_ano = "";
        String[] FROM = {"_id","foo_name","foo_description","foo_image","res_id","foo_image_small","foo_curtir","foo_ano","foo_winner","foo_city","foo_state"};
        DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(this);
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
            // TODO: handle exception
            //Log.e("*** select ",e.getMessage());
        }
        dbAdapter.openDataBase();
        Cursor c = dbAdapter.selectRecordsFromDB("foods", FROM,null,null,null,null,null);
        ArrayList<String> foods_names = new ArrayList<String>();
        for(c.moveToFirst(); c.moveToNext(); c.isAfterLast())
        {
            String mTitleRaw = c.getString(c.getColumnIndex("foo_name"));
            int mAnoRaw = c.getInt(c.getColumnIndex("foo_ano"));
            String mCityRaw = c.getString(c.getColumnIndex("foo_city"));
            pre_ano = Preferences.getPre_ano();
            pre_city = Preferences.getPre_city();
            if (!pre_ano.equals("Todos")) {
                int yearPref =  Integer.parseInt(pre_ano);
                if (mAnoRaw == yearPref) {
                    if (!pre_city.equals("Todas")) {
                        if (pre_city.equals(mCityRaw)) {
                            foods_names.add(mTitleRaw);
                        }
                    }else {
                        foods_names.add(mTitleRaw);
                    }
                }
            }else {
                if (!pre_city.equals("Todas")) {
                    if (pre_city.equals(mCityRaw)) {
                        foods_names.add(mTitleRaw);
                    }
                }else {
                    foods_names.add(mTitleRaw);
                }
            }
            //foods_names.add(mTitleRaw);

        }
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompletePetiscos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.food_row_2, foods_names);
        textView.setAdapter(adapter);
        textView.addTextChangedListener(textChecker);
        c.close();
        c.deactivate();
        dbAdapter.close();
    }
    final TextWatcher textChecker = new TextWatcher() {
        public void afterTextChanged(Editable s) {

        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

            ListFoods(s.toString());

        }
    };
    public void ListFoods(String filter){

        mfoods = getFoods(filter);
        if (mfoods.size() > 0) {
            lvfoods.setAdapter(new ListAdapter(this,R.id.listFoods,mfoods));
        }

    }
    public ArrayList<FoodsInfo> getFoods(String filter)
    {
        String pre_city = "";
        String pre_ano = "";
        String[] FROM = {"_id","foo_name","foo_description","foo_image","res_id","foo_image_small","foo_curtir","foo_ano","foo_winner","foo_city","foo_state"};
        DBAdapter dbAdapter=DBAdapter.getDBAdapterInstance(this);
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
            //Log.i("*** select ",e.getMessage());
        }
        dbAdapter.openDataBase();
        String query="SELECT * FROM foods ";
        ArrayList<ArrayList<String>> stringList = new ArrayList<ArrayList<String>>();
        if (filter.length() > 0 ) {
            String whereClause =  " foo_name= ? ";
            String[] args = {filter};
            stringList = dbAdapter.selectRecordsFromDBList("foods", FROM, whereClause, args, null, null, null);
        }else {
            stringList = dbAdapter.selectRecordsFromDBList(query, null);
        }
        dbAdapter.close();


        ArrayList<FoodsInfo> foodsList = new ArrayList<FoodsInfo>();
        for (int i = 0; i < stringList.size(); i++) {
            ArrayList<String> list = stringList.get(i);
            FoodsInfo food = new FoodsInfo();
            try {
                food.id = Integer.parseInt(list.get(0));
                food.foo_name = list.get(1);
                food.foo_description = list.get(2);
                food.foo_image = list.get(3);
                food.res_id = Integer.parseInt(list.get(4));
                food.foo_image_small = list.get(5);
                food.foo_curtir = Integer.parseInt(list.get(6));
                food.foo_ano = Integer.parseInt(list.get(7));
                food.foo_winner = list.get(8);
                food.foo_city =  list.get(9);
                food.foo_state = list.get(10);
            } catch (Exception e) {
                //Log.i("***" + Foods.class.toString(), e.getMessage());
            }
            pre_ano = Preferences.getPre_ano();
            pre_city = Preferences.getPre_city();
            if (!pre_ano.equals("Todos")) {
                int yearPref =  Integer.parseInt(pre_ano);
                if (food.foo_ano == yearPref) {
                    if (!pre_city.equals("Todas")) {
                        if (pre_city.equals(food.foo_city)) {
                            foodsList.add(food);
                        }
                    }else {
                        foodsList.add(food);
                    }
                }
            }else {
                if (!pre_city.equals("Todas")) {
                    if (pre_city.equals(food.foo_city)) {
                        foodsList.add(food);
                    }
                }else {
                    foodsList.add(food);
                }
            }

        }
        return foodsList;


    }
    private class ListAdapter extends ArrayAdapter<FoodsInfo> {  // --CloneChangeRequired
        private ArrayList<FoodsInfo> mList;  // --CloneChangeRequired
        private Context mContext;
        private ImageThreadLoader imageLoader ;
        public ListAdapter(Context context, int textViewResourceId,ArrayList<FoodsInfo> list) { // --CloneChangeRequired
            super(context, textViewResourceId, list);
            this.mList = list;
            this.mContext = context;

        }

        public View getView(int position, View convertView, ViewGroup parent){
            View view = convertView;
            final ImageView image ;
            imageLoader =  new ImageThreadLoader(mContext);
            try{
                if (view == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.food_row, null); 	// --CloneChangeRequired(list_item)
                }
                final FoodsInfo listItem = mList.get(position); 	// --CloneChangeRequired
                if (listItem != null) {
                    // setting list_item views
                    ( (TextView) view.findViewById(R.id._id) ).setText( listItem.getId()+"");
                    ( (TextView) view.findViewById(R.id.foo_name) ).setText( listItem.getFoo_name()+" - "+listItem.getFoo_ano() );
                    ( (TextView) view.findViewById(R.id.foo_description) ).setText( listItem.getFoo_description());
                    image = (ImageView)view.findViewById(R.id.imageViewFood);
                    Bitmap cachedImage = null;
                    try {
                        cachedImage = imageLoader.loadImage(listItem.getFoo_image_small(),new ImageLoadedListener()  {

                            public void imageLoaded(Bitmap imageBitmap) {
                                // TODO Auto-generated method stub
                                image.setImageBitmap(imageBitmap);
                                notifyDataSetChanged();
                            }
                        });

                    } catch (MalformedURLException e) {
                        // TODO: handle exception
                        //Log.e("Fooods.class", "Bad remote image URL: " + listItem.foo_image_small+" posiÁ„o "+position, e);

                    }
                    if (cachedImage !=null) {
                        image.setImageBitmap(cachedImage);
                    }

                }}catch(Exception e){
                //Log.e("Fooods.class", "Bad error: "+e.getMessage()+" posiÁ„o "+position, e);

            }
            return view;
        }

    }

}
