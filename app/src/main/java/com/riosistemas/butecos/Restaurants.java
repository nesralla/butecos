package com.riosistemas.butecos;
import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
/**
 * Created by nesralla on 7/6/16.
 */

public class Restaurants extends Activity{
    String[] anosAutoComplete;
    String[] anos ;
    SimpleCursorAdapter sAdapter;
    private ListView lvRestaurants;
    private ArrayList<RestaurantsInfo> mRestaurants;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.restaurants);
            mRestaurants = getRestaurants("");
            lvRestaurants = (ListView)findViewById(R.id.listRestaurants);
            lvRestaurants.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    RestaurantDetails(arg2);
                }
            });
            lvRestaurants.setAdapter(new ListAdapter(this,R.id.listRestaurants,mRestaurants));
            ListAutoCompleteRestaurants();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("oncreate resta",e.getMessage());
        }


    }
    public void RestaurantDetails(int arg2){

        Intent its = new Intent(getBaseContext(),RestaurantsDetails.class);
        if (mRestaurants.size() > 0) {
            for (int i = 0; i < mRestaurants.size(); i++) {
                if (i == arg2) {
                    Bundle b = new Bundle();
                    b.putLong("key",mRestaurants.get(i).id);
                    its.putExtras(b);
                    startActivity(its);
                }

            }

        }
    }
    public ArrayList<RestaurantsInfo>getRestaurants(String filter){

        String pre_city = "";
        String pre_ano = "";
        String[] FROM = {"_id","res_name","res_address","res_number","res_neiborhood","res_image","res_lat","res_long","res_city","res_state","res_zip","res_ano","res_obs","res_curtir","res_phone","res_visited"};
        DBAdapter dbAdapter=DBAdapter.getDBAdapterInstance(this);
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
            //Log.i("*** select ",e.getMessage());
        }

        dbAdapter.openDataBase();
        ArrayList<ArrayList<String>> stringList = new ArrayList<ArrayList<String>>();
        if (filter.length() > 0 ) {
            String whereClause =  " res_name= ? ";
            String[] args = {filter};
            stringList = dbAdapter.selectRecordsFromDBList("restaurants", FROM, whereClause, args, null, null, null);

        }else {
            stringList =  dbAdapter.selectRecordsFromDBList("restaurants", FROM, null, null, null, null, null);
        }
        dbAdapter.close();


        ArrayList<RestaurantsInfo> restaurantsList = new ArrayList<RestaurantsInfo>();
        for (int i = 0; i < stringList.size(); i++) {
            ArrayList<String> list = stringList.get(i);
            RestaurantsInfo restaurants = new RestaurantsInfo();
            try {
                restaurants.id = Integer.parseInt(list.get(0));
                restaurants.res_name = list.get(1);
                restaurants.res_address = list.get(2);
                restaurants.res_number = list.get(3);
                restaurants.res_neiborhood = list.get(4);
                restaurants.res_image = list.get(5);
                restaurants.res_lat = list.get(6);
                restaurants.res_long = list.get(7);
                restaurants.res_city = list.get(8);
                restaurants.res_state = list.get(9);
                restaurants.res_zip = list.get(10);
                restaurants.res_ano = list.get(11);
                restaurants.res_obs = list.get(12);
                restaurants.res_curtir = Integer.parseInt(list.get(13));
                restaurants.res_phone = list.get(14);
                restaurants.res_visited = Integer.parseInt(list.get(15));

            } catch (Exception e) {
                //Log.i("***" + Foods.class.toString(), e.getMessage());
            }
            pre_city = Preferences.getPre_city();
            pre_ano = Preferences.getPre_ano();
            if (!pre_ano.equals("Todos")) {
                anos = restaurants.res_ano.split("\\|");
                if (anos.length > 0) {
                    for (int j = 0; j < anos.length; j++) {
                        int year = Integer.parseInt(anos[j]);
                        int yearPref = Integer.parseInt(pre_ano);
                        if ( yearPref==year ) {
                            if (!pre_city.equals("Todas")) {
                                if (pre_city.equals(restaurants.res_city)) {
                                    restaurantsList.add(restaurants);
                                }
                            }else {
                                restaurantsList.add(restaurants);
                            }

                        }
                    }

                }
            }else {
                if (!pre_city.equals("Todas")) {
                    if (pre_city.equals(restaurants.res_city)) {
                        restaurantsList.add(restaurants);
                    }
                }else {
                    restaurantsList.add(restaurants);
                }

            }


        }
        return restaurantsList;
    }
    public void ListRestaurants(String filter){

        mRestaurants = getRestaurants(filter);
        if (mRestaurants.size() > 0) {
            lvRestaurants.setAdapter(new ListAdapter(this,R.id.listRestaurants,mRestaurants));
        }

    }
    public void ListAutoCompleteRestaurants(){

        String pre_city = "";
        String pre_ano = "";
        String[] FROM = {"_id","res_name","res_address","res_number","res_neiborhood","res_image","res_lat","res_long","res_city","res_state","res_zip","res_ano","res_obs","res_curtir","res_phone","res_visited"};
        DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(this);
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
            // TODO: handle exception
            //Log.e("*** select ",e.getMessage());
        }
        dbAdapter.openDataBase();
        Cursor c = dbAdapter.selectRecordsFromDB("restaurants", FROM,null,null,null,null,null);
        ArrayList<String> restaurants_names = new ArrayList<String>();
        for(c.moveToFirst(); c.moveToNext(); c.isAfterLast())
        {
            String mTitleRaw = c.getString(c.getColumnIndex("res_name"));
            String mAnoRaw = c.getString(c.getColumnIndex("res_ano"));
            String mCityRaw = c.getString(c.getColumnIndex("res_city"));
            pre_city = Preferences.getPre_city();
            pre_ano = Preferences.getPre_ano();
            if (!pre_ano.equals("Todos")) {
                anosAutoComplete = mAnoRaw.split("\\|");
                if (anosAutoComplete.length > 0) {
                    for (int j = 0; j < anosAutoComplete.length; j++) {
                        int year = Integer.parseInt(anosAutoComplete[j]);
                        int yearPref = Integer.parseInt(pre_ano);
                        if ( yearPref==year ) {
                            if (!pre_city.equals("Todas")) {
                                if (pre_city.equals(mCityRaw)) {
                                    restaurants_names.add(mTitleRaw);
                                }
                            }else {
                                restaurants_names.add(mTitleRaw);
                            }

                        }
                    }

                }
            }else {
                if (!pre_city.equals("Todas")) {
                    if (pre_city.equals(mCityRaw)) {
                        restaurants_names.add(mTitleRaw);
                    }
                }else {
                    restaurants_names.add(mTitleRaw);
                }

            }
            //restaurants_names.add(mTitleRaw);
        }
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteButecos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.restaurant_row_2, restaurants_names);
        textView.setAdapter(adapter);
        textView.addTextChangedListener(textChecker);
        dbAdapter.close();
    }
    final TextWatcher textChecker = new TextWatcher() {
        public void afterTextChanged(Editable s) {


        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            ListRestaurants(s.toString());
        }
    };
    private class ListAdapter extends ArrayAdapter<RestaurantsInfo> {  // --CloneChangeRequired
        private ArrayList<RestaurantsInfo> mList;  // --CloneChangeRequired
        private Context mContext;
        public ListAdapter(Context context, int textViewResourceId,ArrayList<RestaurantsInfo> list) { // --CloneChangeRequired
            super(context, textViewResourceId, list);
            this.mList = list;
            this.mContext = context;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View view = convertView;

            try{
                if (view == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.restaurant_row, null); 	// --CloneChangeRequired(list_item)
                }
                final RestaurantsInfo listItem = mList.get(position); 	// --CloneChangeRequired
                if (listItem != null) {
                    // setting list_item views
                    ( (TextView) view.findViewById(R.id._id) ).setText( listItem.getId()+"");
                    ( (TextView) view.findViewById(R.id.res_name) ).setText( listItem.getRes_name());
                    ( (TextView) view.findViewById(R.id.res_address) ).setText( listItem.getRes_address());
                    ( (TextView) view.findViewById(R.id.res_number) ).setText( listItem.getRes_number());
                    ( (TextView) view.findViewById(R.id.res_neiborhood) ).setText( listItem.getRes_neiborhood());
                    ( (TextView) view.findViewById(R.id.res_city) ).setText( listItem.getRes_city());
                    ( (TextView) view.findViewById(R.id.res_state) ).setText( listItem.getRes_state());
                    ( (TextView) view.findViewById(R.id.res_phone) ).setText( listItem.getRes_phone());

                }}catch(Exception e){
                //Log.e("Fooods.class", "Bad error: "+e.getMessage()+" posiÁ„o "+position, e);

            }
            return view;
        }

    }

}

