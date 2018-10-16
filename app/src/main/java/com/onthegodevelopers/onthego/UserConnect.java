package com.onthegodevelopers.onthego;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

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
    protected String doInBackground(String... params) {
        String type = params[0];
//        String login_url= "http://10.0.2.2/login.php";
        String login_url = "http://10.0.2.2/login.php";
        String signup_url = "http://10.0.2.2/insert_user_data.php";
        if (type.equals("login")) {
            String user_name = params[1];
            String user_pass = params[2];
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeLoginServiceCall(login_url, user_name, user_pass);
            Log.e(TAG, "Response from url: " + jsonStr);
            try {
                JSONArray jArray = new JSONArray(jsonStr);
                String name = jArray.getJSONObject(0).getString("Name");
                Log.d("INFO", name);
                return name;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
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


