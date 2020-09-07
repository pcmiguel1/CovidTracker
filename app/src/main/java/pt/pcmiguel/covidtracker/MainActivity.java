package pt.pcmiguel.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView numCases, numConfirmed, numDeaths, numRecovered, todayCases, todayDeaths, todayRecovered, lastUpdate;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numCases = findViewById(R.id.num_cases);
        numConfirmed = findViewById(R.id.num_confirmed_cases);
        numDeaths = findViewById(R.id.num_deaths);
        numRecovered = findViewById(R.id.num_recovered);

        todayCases = findViewById(R.id.today_cases);
        todayDeaths = findViewById(R.id.today_deaths);
        todayRecovered = findViewById(R.id.today_recovered);

        lastUpdate = findViewById(R.id.last_update);

        progressBar = findViewById(R.id.progress_circular);


        getData();
    }

    private void getData() {

        // Get country name
        final String country = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();

        String url = "https://corona.lmao.ninja/v2/countries";

        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {

                            progressBar.setVisibility(View.GONE);

                            try {

                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    if (data.getString("country").equalsIgnoreCase(country)) {

                                        long timeUpdate = Long.valueOf(data.getString("updated"));

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(timeUpdate);

                                        int mYear = calendar.get(Calendar.YEAR);
                                        int mMonth = calendar.get(Calendar.MONTH)+1;
                                        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                                        String finalDate = "Last update: " + mDay + "/" + mMonth + "/" + mYear;

                                        lastUpdate.setText(finalDate);

                                        numCases.setText(data.getString("active"));
                                        numConfirmed.setText(data.getString("cases"));
                                        numDeaths.setText(data.getString("deaths"));
                                        numRecovered.setText(data.getString("recovered"));
                                        todayCases.setText("+" + data.getString("todayCases"));
                                        todayDeaths.setText("+" + data.getString("todayDeaths"));
                                        todayRecovered.setText("+" + data.getString("todayRecovered"));
                                    }
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
