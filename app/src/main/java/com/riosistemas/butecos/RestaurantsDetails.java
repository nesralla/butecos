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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class RestaurantsDetails extends Activity {
	
	SimpleCursorAdapter sAdapter;
	private ListView lvFoods;
	private ArrayList<FoodsInfo> mfoods;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			 setContentView(R.layout.restaurants_details);	
			 
			 Bundle extra = getIntent().getExtras();			
			 if (extra != null) {
				 Long mRowId = extra.getLong("key",0);
				 if (mRowId != 0) {
				 String[] FROM = {"_id","res_name","res_address","res_number","res_neiborhood","res_image","res_lat","res_long","res_city","res_state","res_zip","res_ano","res_obs","res_curtir","res_phone","res_visited"};					 
					 String WHERE = "_id = ?";					 
					 String[] ARGS = {mRowId.toString()};					 				 
					 DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(this);
					 dbAdapter.createDataBase();
					 dbAdapter.openDataBase();
					 Cursor c = dbAdapter.selectRecordsFromDB("restaurants", FROM,WHERE,ARGS,null,null,null);					
					 c.moveToFirst();					 
					 String res_name = c.getString(c.getColumnIndex("res_name"));					 
					 String res_address = c.getString(c.getColumnIndex("res_address"));
					 String res_number = c.getString(c.getColumnIndex("res_number"));
					 String res_neiborhood = c.getString(c.getColumnIndex("res_neiborhood"));
					 String res_image = c.getString(c.getColumnIndex("res_image"));
					 String res_lat = c.getString(c.getColumnIndex("res_lat"));
					 String res_long = c.getString(c.getColumnIndex("res_long"));
					 String res_city = c.getString(c.getColumnIndex("res_city"));
					 String res_state =c.getString(c.getColumnIndex("res_state"));
					 String res_zip = c.getString(c.getColumnIndex("res_zip"));
					 String res_ano = c.getString(c.getColumnIndex("res_ano"));
					 String res_obs = c.getString(c.getColumnIndex("res_obs"));
					 String res_curtir = c.getString(c.getColumnIndex("res_curtir"));	
					 String res_phone = c.getString(c.getColumnIndex("res_phone"));
					 String res_visited = c.getString(c.getColumnIndex("res_visited"));
					TextView txtResName = (TextView)findViewById(R.id.res_name);
					TextView txtResAddress = (TextView)findViewById(R.id.res_address);
					TextView txtResPhone = (TextView)findViewById(R.id.res_phone);
					this.setTitle(res_name);
					txtResName.setText(res_name);
					txtResAddress.setText(res_address+","+res_number+"\n"+res_neiborhood+" - "
										 +res_city+" - "+res_state+"\n"+res_zip);
					txtResPhone.setText(res_phone);
					 final long _idRes = mRowId;
					 final ImageButton imgMapButton = (ImageButton)findViewById(R.id.imageButtonMaps);
					 final ImageButton imgPhoneButton = (ImageButton)findViewById(R.id.imageButtonPhone);
					 c.deactivate();
					 c.close();		
					 
					 mfoods = getFoodsByRestaurants(mRowId);
					 lvFoods = (ListView)findViewById(R.id.listFoods);
					 lvFoods.setAdapter(new ListAdapter(this,R.id.listFoods,mfoods));
					 lvFoods.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							 FoodDetails(arg2);
						}
					
					 });
					 imgMapButton.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							// TODO Auto-generated method stub
							/*Intent its = new Intent(getBaseContext(),Map.class);
							Bundle b = new Bundle();
							b.putLong("key",_idRes);
							its.putExtras(b);						
							startActivity(its);*/
							
						}
					});
					 imgPhoneButton.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								// TODO Auto-generated method stub
								TextView num=(TextView)findViewById(R.id.res_phone);         
								String number = "tel:0" + num.getText().toString().trim();        
								Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));         
								startActivity(callIntent);
								
							}
						});
					 
				}
			}
			 
		} catch (Exception e) {
			// TODO: handle exception
			//Log.e("oncreate resta",e.getMessage());
		}
		
		
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
					finish();
				}
				
			}
			
			
		}		
		
	}
	public ArrayList<FoodsInfo> getFoodsByRestaurants(long mRowId)
	{
		DBAdapter dbAdapter=DBAdapter.getDBAdapterInstance(this);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
			//Log.i("*** select ",e.getMessage());
		}
    	dbAdapter.openDataBase();		
		String query="SELECT _id,foo_name,foo_description,foo_image,res_id,foo_image_small,foo_ano,foo_curtir,foo_winner,foo_city,foo_state FROM foods WHERE res_id ="+mRowId+" ORDER BY foo_ano DESC;";
		ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query, null);
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
				food.foo_ano = Integer.parseInt(list.get(6));
				food.foo_curtir = Integer.parseInt(list.get(7));
			} catch (Exception e) {
				//Log.i("***" + Foods.class.toString(), e.getMessage());
			}
			foodsList.add(food);
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
							//notifyDataSetChanged();
						}
					});
					
				} catch (MalformedURLException e) {
					// TODO: handle exception
					//Log.e("Fooods.class", "Bad remote image URL: " + listItem.foo_image_small+" posi��o "+position, e);
					
				}
				if (cachedImage !=null) {
					image.setImageBitmap(cachedImage);
				}
		
			}}catch(Exception e){
				//Log.e("RestaurantsDetails.class", "Bad error: "+e.getMessage()+" posi��o "+position, e);
				
			}
			return view;
		}
		
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
	@Override
    public void onDestroy(){
        super.onDestroy();       
       
    }
}
