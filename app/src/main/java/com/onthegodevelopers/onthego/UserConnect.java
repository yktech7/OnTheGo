package com.onthegodevelopers.onthego;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.onthegodevelopers.onthego.MapActivity.TAG;

//public class MainActivity extends AppCompatActivity {
//
//    private String TAG = MainActivity.class.getSimpleName();
//
//    private ProgressDialog pDialog;
//    private ListView lv;
//
//    // URL to get contacts JSON
//    private static String url = "https://api.androidhive.info/contacts/";
//
//    ArrayList<HashMap<String, String>> contactList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        contactList = new ArrayList<>();
//
//        lv = (ListView) findViewById(R.id.list);
//
//        new GetContacts().execute();
//    }import static com.onthegodevelopers.onthego.MapActivity.TAG;

/**
     * Async task class to get json by making HTTP call
     */
    public class UserConnect extends AsyncTask<String,Void,String> {
    Context context;
    private ProgressDialog pDialog;
    AlertDialog alertDialog;

    UserConnect(Context ctx) {
        context = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Nidurge Bhosda...");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params){

        String getUserUrl="http://ayyoramamovie.in/adworms.in/fetch_user_data.php";

        try {
            //creating a URL
            URL url = new URL(getUserUrl);

            //Opening the URL using HttpURLConnection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //StringBuilder object to read the string from the service
            StringBuilder sb = new StringBuilder();

            //We will use a buffered reader to read the string from service
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            //A simple string to read values from each line
            String json;

            //reading until we don't find null
            while ((json = bufferedReader.readLine()) != null) {

                //appending it to string builder
                sb.append(json + "\n");
            }

            //finally returning the read string
            return sb.toString().trim();
        } catch (Exception e) {
            return null;
        }

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        Log.d("INFO",result);
        if (pDialog.isShowing())
            pDialog.dismiss();
        //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}
//            /**
//             * Updating parsed JSON data into ListView
//             * */
//            ListAdapter adapter = new SimpleAdapter(
//                    MainActivity.this, contactList,
//                    R.layout.list_item, new String[]{"name", "email",
//                    "mobile"}, new int[]{R.id.name,
//                    R.id.email, R.id.mobile});
//
//            lv.setAdapter(adapter);
//        }
//
//    }
//}


