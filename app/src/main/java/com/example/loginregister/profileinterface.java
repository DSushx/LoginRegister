package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Calendar;

public class profileinterface extends AppCompatActivity {

    Button button;
    String Heroes;
    String username;
    String password;
    String email;
    String height;
    String weight;
    String birthday;
    EditText username1;
    HttpURLConnection httpURLConnection;

    String editor;

    TextView textname,textpassword,textemail,textheight;

    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileinterface);
       getJSON("http://192.168.100.35/LoginRegister/profile.php");
       textname = (TextView)  findViewById(R.id.id1);
        textpassword = (TextView)  findViewById(R.id.id2);
        textemail = (TextView)  findViewById(R.id.id3);
        textheight = (TextView)  findViewById(R.id.id4);

    }
        private void getJSON(final String urlWebService){

            class GetJSON extends AsyncTask<Void,Void,String> implements com.example.loginregister.GetJSON {
                @Override
                protected void onPreExecute(){super.onPreExecute();}
                @Override
                protected  void onPostExecute(String s){
                    super.onPostExecute(s);
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                    try{
                        loadIntoListView(s);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                protected String doInBackground(Void... voids){
                    try{
                        URL url = new URL(urlWebService);
                        HttpURLConnection con=(HttpURLConnection)  url.openConnection();
                        StringBuilder sb=new StringBuilder();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String json;
                        while((json=bufferedReader.readLine())!=null){
                            sb.append(json+"\n");
                        }
                        return sb.toString().trim();
                    } catch (Exception e){
                        return "";
                    }
                }

            }
            GetJSON getJSON=new GetJSON();
            getJSON.execute();
        }

        private void  loadIntoListView(String json) throws JSONException{
            JSONArray jsonArray = new JSONArray(json);
            String[] heroes = new String[jsonArray.length()];
            for (int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                textname.setVisibility(View.VISIBLE);
                textemail.setVisibility(View.VISIBLE);
                textheight.setVisibility(View.VISIBLE);
                textpassword.setVisibility(View.VISIBLE);

                textname.setText(obj.getString("username"));
                textemail.setText(obj.getString("email"));
                textheight.setText(obj.getString("height"));
                textpassword.setText(obj.getString("password"));
            }
        }


}