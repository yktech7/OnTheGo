package com.onthegodevelopers.onthego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Dictionary;

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;
    BackgroundWorker(Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
//        String login_url= "http://10.0.2.2/login.php";
        String login_url= "http://ayyoramamovie.in/adworms.in/fetch_user_data.php";
        String signup_url= "http://ayyoramamovie.in/adworms.in/insert_users_data.php";
        if(type.equals("login")){
            try {
                String user_name = params[1];
                String user_pass = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data=URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+URLEncoder.encode("user_pass","UTF-8")+"="+URLEncoder.encode(user_pass,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream =httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line= "";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("SignUp")){
            try {
                String user_name = params[1];
                String user_pass = params[2];
                String user_mobile = params[3];
                String user_email = params[4];
                URL url = new URL(signup_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data=URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+URLEncoder.encode("user_pass","UTF-8")+"="+URLEncoder.encode(user_pass,"UTF-8")+"&"+URLEncoder.encode("user_mobile","UTF-8")+"="+URLEncoder.encode(user_mobile,"UTF-8")+"&"+URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
//                InputStream inputStream =httpURLConnection.getInputStream();
//                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
//                String result = "";
//                String line= "";
//                while ((line = bufferedReader.readLine())!=null){
//                    result +=line;
//                }
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
                String result = "Sign Up successful, Please login";
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
   }

    @Override
    protected void onPreExecute() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage("Information saved successfully ! Add Another Info?")
//                .setCancelable(false)
//                .setPositiveButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent intent = new Intent(((Dialog)dialog).getContext(), Login.class);
//                        //startActivity(new Intent(((Dialog)dialog).getContext(),Login.class));
//                        startActivity(intent);
//                    }
//                })
//                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        dialog = alert;

        alertDialog =new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login status");

    }

    @Override
    protected void onPostExecute(String result) {
        if (result == "Sign Up successful, Please login"){
            alertDialog.setMessage(result);
            alertDialog.dismiss();
            alertDialog.show();
            Intent anothercallActivity=new Intent(context,Login.class);
            context.startActivity(anothercallActivity);
        }
        else
        {
            alertDialog.setMessage(result);
            alertDialog.dismiss();
            alertDialog.show();
        }

//        Intent anothercallActivity=new Intent(context,Login.class);
//        context.startActivity(anothercallActivity);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
