package com.example.lweather;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView t1,t2,t3,t4;
    LocalTime modo;
    ConstraintLayout pantalla;
    ImageView iconoTiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = (TextView)findViewById(R.id.textViewTemperatura);
        t2 = (TextView)findViewById(R.id.textViewCiudad);
        t3 = (TextView)findViewById(R.id.textViewTiempo);
        t4 = (TextView)findViewById(R.id.textViewFecha);
        pantalla = (ConstraintLayout)findViewById(R.id.fondo);
        iconoTiempo = (ImageView)findViewById(R.id.imageViewIconoTiempo);

        recibirDatos();
    }

    public void recibirDatos(){
        String url="http://api.openweathermap.org/data/2.5/weather?q=Basauri,es&appid=a5e3396b536f33fd894098e50ab57f5c&units=Imperial&lang=es";

        JsonObjectRequest hor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String desc = object.getString("description");
                    String city = response.getString("name");
                    desc= capitalize(desc);

                   // t1.setText(temp);
                    t2.setText(city);
                    t3.setText(desc);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
                    String formatted_date = sdf.format(calendar.getTime());

                    int hora = calendar.get(Calendar.HOUR_OF_DAY);

                    if(hora > 20 && hora <=24 || hora >= 0 && hora < 7 ){
                        pantalla.setBackgroundColor(Color.BLACK);
                        t1.setTextColor(Color.WHITE);
                        t2.setTextColor(Color.WHITE);
                        t3.setTextColor(Color.WHITE);
                        t4.setTextColor(Color.WHITE);


                            iconoTiempo.setImageResource(R.drawable.night);
                            Log.d("AA", "onResponse: ");

                    }



                    t4.setText(formatted_date);

                    double temp_init = Double.parseDouble(temp);
                    double centi = (temp_init -32)/1.8000;
                    centi = Math.round(centi);
                    int i = (int) centi;
                    t1.setText(String.valueOf(i)+"º");

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(hor);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
