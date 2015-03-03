package com.comp3111.localendar.calendar;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.R.id;
import com.comp3111.localendar.map.MyGoogleMap;
import com.comp3111.localendar.map.Place;



public class OnlineEventDetail extends Activity {
	 
    TextView txtName;
    TextView txtPrice;
    TextView txtDesc;
    TextView txtCreatedAt;
    Button btnSave;
    Button btnMap;
    Button deleteButton;
    Button addButton;
 
    String pid;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static final String url_product_detials = "http://eea258.ee.ust.hk/develop/xmeng/elec4010b/get_product_details.php";
    private static final String url_delete_product = "http://eea258.ee.ust.hk/develop/xmeng/elec4010b/delete_product.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setTitle("Online Event Detail");
        setContentView(R.layout.online_event_detail);
 
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnMap = (Button) findViewById(R.id.btnMap);
        deleteButton=(Button) findViewById(R.id.btn_delete);
        addButton=(Button) findViewById(R.id.btn_add);
 
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_PID);
 
        // Getting complete product details in background thread
        new GetProductDetails().execute();
 
        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                finish();
            }
        });
 
        // Delete button click event
        btnMap.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                showOnMap();
            }
        });
        
        deleteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DeleteProduct().execute();
			}
		});
        addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addToCalendar();
			}
		});
 
    }
     public Boolean showOnMap(){
    	 String time = txtPrice.getText().toString();
    	 String title= txtName.getText().toString();
    	 String location = txtDesc.getText().toString();
    	 Place mlocation= Place.getPlaceFromAddress(location);
		    if(MyGoogleMap.addmarker1(mlocation, true, title, time)) {
	    		Localendar.instance.setPagerIndex(1);
				this.finish();
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				return true;
	    	}
	    	else
	    		return false;
     }
     public void addToCalendar() {
		Intent in = new Intent(this,AddEventActivity.class);
		String eventDescription,eventTime,eventLocation;
		eventDescription=txtName.getText().toString();
		eventTime = txtPrice.getText().toString();
		eventLocation= txtDesc.getText().toString();
		in.putExtra("DES", eventDescription);
		in.putExtra("TIME", eventTime);
		in.putExtra("location",eventLocation);
		startActivity(in);
		this.finish();
	}
    /**
     * Background Async Task to Get complete product details
     * */
   private class GetProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OnlineEventDetail.this);
            pDialog.setMessage("Loading event details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {
 
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));
 
                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_detials, "GET", params);
 
                        // check your log for json response
                        Log.d("Single Product Details", json.toString());
 
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array
 
                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);
 
                            // product with this pid found
                            // Edit Text
                            txtName = (TextView) findViewById(R.id.inputName);
                            txtPrice = (TextView) findViewById(R.id.inputPrice);
                            txtDesc = (TextView) findViewById(R.id.inputDesc);
 
                            // display product data in EditText
                            txtName.setText(product.getString(TAG_NAME));
                            txtPrice.setText(product.getString(TAG_PRICE));
                            txtDesc.setText(product.getString(TAG_DESCRIPTION));
 
                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }
   private class DeleteProduct extends AsyncTask<String, String, String> {
	   
       /**
        * Before starting background thread Show Progress Dialog
        * */
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(OnlineEventDetail.this);
           pDialog.setMessage("Deleting Event...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }

       /**
        * Deleting product
        * */
       protected String doInBackground(String... args) {

           // Check for success tag
           int success;
           try {
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("pid", pid));

               // getting product details by making HTTP request
               JSONObject json = jsonParser.makeHttpRequest(
                       url_delete_product, "POST", params);

               // check your log for json response
               Log.d("Delete Product", json.toString());

               // json success tag
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
                   // product successfully deleted
                   // notify previous activity by sending code 100
                   Intent i = getIntent();
                   // send result code 100 to notify about product deletion
                   setResult(100, i);
                   finish();
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return null;
       }

       /**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(String file_url) {
           // dismiss the dialog once product deleted
           pDialog.dismiss();

       }

   }
}

