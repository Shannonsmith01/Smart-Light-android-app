package com.example.smartlightingcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private EditText userName, password;
    private Button loginButton;

    private static final String TAG = "MainActivity";

    // Check if internet is available
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);  // Fixed typo: 'password' reference
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String username = userName.getText().toString();
            String passwordText = password.getText().toString();  // Fixed typo

            if (!isInternetAvailable()) {
                Toast.makeText(MainActivity.this, "No internet connection.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!username.isEmpty() && !passwordText.isEmpty()) {
                authenticateUser(username, passwordText);
            } else {
                Toast.makeText(MainActivity.this, "Please fill out both fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Authentication logic using a POST request
    private void authenticateUser(String username, String password) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            OutputStreamWriter writer = null;
            Scanner scanner = null;
            try {
                URL url = new URL("https://vu-nit3213-api.onrender.com/ort/auth");
                Log.d(TAG, "Connecting to URL: " + url.toString());

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setConnectTimeout(60000);  // 60 seconds timeout
                connection.setReadTimeout(60000);

                // Writing request body
                JSONObject requestBody = new JSONObject();
                requestBody.put("username", username);
                requestBody.put("password", password);
                Log.d(TAG, "Request body: " + requestBody.toString());

                writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(requestBody.toString());
                writer.flush();

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Response code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    scanner = new Scanner(connection.getInputStream());
                    String response = scanner.useDelimiter("\\A").next();
                    Log.d(TAG, "Response: " + response);

                    // Handle response
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        String keypass = responseObject.getString("keypass");
                        Log.d(TAG, "Keypass: " + keypass);

                        if (keypass != null && !keypass.isEmpty()) {
                            // Save keypass using SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("KEYPASS", keypass);
                            editor.apply();

                            // Pass keypass to HomeActivity
                            runOnUiThread(() -> {
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                intent.putExtra("KEYPASS", keypass);  // Use "KEYPASS" as the key
                                startActivity(intent);
                            });
                        } else {
                            Log.e(TAG, "Keypass is null or empty.");
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Invalid response from server.", Toast.LENGTH_SHORT).show());
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Invalid response from server.", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Log.e(TAG, "Login failed with response code: " + responseCode);
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Login failed with code: " + responseCode, Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error during authentication: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } finally {
                // Close resources
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to close OutputStreamWriter.", e);
                    }
                }
                if (scanner != null) {
                    scanner.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}