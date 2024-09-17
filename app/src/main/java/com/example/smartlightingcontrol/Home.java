package com.example.smartlightingcontrol;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {
    private ProgressBar loadingSpinner;
    private TextView eventsTextView;  // TextView to display the events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadingSpinner = findViewById(R.id.loading_spinner);
        eventsTextView = findViewById(R.id.events_text_view);

        String keypass = getIntent().getStringExtra("KEYPASS");  // Get the keypass from login
        Log.d(TAG, "get KEYPASS: " + keypass);
        if (keypass == null || keypass.isEmpty()) {
            Log.d(TAG, "KEYPASS is null or empty: " + keypass);
            Toast.makeText(this, "Invalid KEYPASS", Toast.LENGTH_SHORT).show();
        } else {
            fetchDashboardData(keypass);  // Call method to fetch the data
        }


        Button DetailPageButton = findViewById(R.id.DetailPage);
        DetailPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SettingsActivity when the button is clicked
                Intent intent = new Intent(Home.this, DetailPage.class);
                startActivity(intent);
            }
        });
    }

    private void fetchDashboardData(String keypass) {
        // Show loading spinner
        loadingSpinner.setVisibility(View.VISIBLE);

        // Construct URL
        String url = "https://vu-nit3213-api.onrender.com/dashboard/" + keypass;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    // Hide loading spinner and show error
                    loadingSpinner.setVisibility(View.GONE);
                    Toast.makeText(Home.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse the response
                    String responseData = response.body().string();
                    runOnUiThread(() -> {
                        // Hide loading spinner
                        loadingSpinner.setVisibility(View.GONE);
                        // Update UI with the events data
                        updateUIWithEvents(responseData);
                    });
                } else {
                    runOnUiThread(() -> {
                        loadingSpinner.setVisibility(View.GONE);
                        Toast.makeText(Home.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void updateUIWithEvents(String responseData) {
        // Parse the JSON response
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray entities = jsonObject.getJSONArray("entities");

            StringBuilder eventsText = new StringBuilder();
            for (int i = 0; i < entities.length(); i++) {
                JSONObject event = entities.getJSONObject(i);
                String eventName = event.getString("event");
                String description = event.getString("description");

                eventsText.append("Event: ").append(eventName).append("\n")
                        .append("Description: ").append(description).append("\n\n");
            }

            // Set the events to the TextView
            eventsTextView.setText(eventsText.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Home.this, "Error parsing data", Toast.LENGTH_SHORT).show();
        }
    }
}