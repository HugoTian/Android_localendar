package com.comp3111.localendar.calendar;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.comp3111.localendar.R;

public class shareEvent extends Activity {
	 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    TextView description;
    TextView time;
    TextView location;
    
    String descriptionOfEvent;
    String timeOfEvent;
    String locationOfEvent;
    // url to create new product
    private static String url_create_product = "http://eea258.ee.ust.hk/develop/xmeng/elec4010b/create_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setTitle("Event Share");
        setContentView(R.layout.share_online);
 
        Intent intent = getIntent();
        descriptionOfEvent = intent.getExtras().getString("DESCRIPTION");
        timeOfEvent = intent.getExtras().getString("TIME");
        locationOfEvent = intent.getExtras().getString("LOCATION");
        
        description = (TextView) findViewById(R.id.description);
        time = (TextView) findViewById(R.id.time);
        location = (TextView) findViewById(R.id.loc);
        
        description.setText(descriptionOfEvent);
        time.setText(timeOfEvent);
        location.setText(locationOfEvent);

        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnshare);
 
        // button click event
        btnCreateProduct.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewProduct().execute();
            }
       });
        Button cancelButton = (Button) findViewById(R.id.btncancel);
        cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
    }
 
    /**
     * Background Async Task to Create new product
     * 
     * */
 public class CreateNewProduct extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
        * */
	         @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(shareEvent.this);
            pDialog.setMessage("Sharing ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Share event
         * */
        protected String doInBackground(String... args) {
            String name = description.getText().toString();
            String price = time.getText().toString();
            String description = location.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("price", price));
            params.add(new BasicNameValuePair("description", description));
 
            // getting JSON Object
            // Note that share event url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
               int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully created product
//                    Intent i = new Intent(getApplicationContext(), onlineEvent.class);
//                    startActivity(i);
 
                    // closing this screen
                    finish();
                } else {
                    // failed to share event
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
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }
}

