package com.example.lms.page;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lms.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PaymentActivity extends AppCompatActivity {

    private static final String BACKEND_URL = "http://10.0.2.2:5000/api/user/purchase"; // Thay bằng URL ngrok của bạn
    private static final String STRIPE_PUBLISHABLE_KEY = "pk_test_51QwKooLZiIsCjmBnuf9Cy7xoNUnvXiAmEGccBqAIk6I6hyisjpX6Hr9BEt9z9RvanxCvaTsaP7UMwnBwiXbMqUdu00sqiSHCwx";

    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private double amount;
    private String userId;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize Stripe
        PaymentConfiguration.init(getApplicationContext(), STRIPE_PUBLISHABLE_KEY);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        // Get data from intent
        amount = getIntent().getDoubleExtra("amount", 0.0);
        userId = getIntent().getStringExtra("userId");
        courseId = getIntent().getStringExtra("courseId");

        // Validate data
        if (userId == null || courseId == null) {
            Toast.makeText(this, "Missing user or course information", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Bind the views
        TextView amountToPay = findViewById(R.id.amountToPay);
        Button payNowButton = findViewById(R.id.payNowButton);

        // Display the amount
        amountToPay.setText("Amount: $" + String.format("%.2f", amount));

        // Handle the "Pay Now" button click
        payNowButton.setOnClickListener(v -> {
            // Fetch the PaymentIntent client secret from the backend
            fetchPaymentIntent();
        });
    }

    private void fetchPaymentIntent() {
        new Thread(() -> {
            try {
                // Create the request body
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("amount", Math.round(amount * 100)); // Chuyển $ thành cent (ví dụ: $10.99 -> 1099)
                jsonBody.put("currency", "usd"); // Use 'vnd' for Vietnamese Dong if needed
                jsonBody.put("userId", userId);
                jsonBody.put("courseId", courseId);

                // Log the request body
                Log.d("PaymentActivity", "Request body: " + jsonBody.toString());

                // Set up the HTTP connection
                URL url = new URL(BACKEND_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Send the request
                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes());
                os.flush();
                os.close();

                // Check the response code
                int responseCode = conn.getResponseCode();
                Log.d("PaymentActivity", "Response code: " + responseCode);

                // Read the response
                BufferedReader br;
                if (responseCode >= 200 && responseCode < 300) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                // Log the raw response
                Log.d("PaymentActivity", "Raw response: " + response.toString());

                // Parse the response
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("clientSecret")) {
                    paymentIntentClientSecret = jsonResponse.getString("clientSecret");
                    Log.d("PaymentActivity", "Client secret: " + paymentIntentClientSecret);
                    // Present the payment sheet on the main thread
                    runOnUiThread(this::presentPaymentSheet);
                } else if (jsonResponse.has("error")) {
                    String errorMessage = jsonResponse.getString("error");
                    Log.e("PaymentActivity", "Backend error: " + errorMessage);
                    runOnUiThread(() -> Toast.makeText(this, "Backend error: " + errorMessage, Toast.LENGTH_LONG).show());
                } else {
                    Log.e("PaymentActivity", "No clientSecret in response");
                    runOnUiThread(() -> Toast.makeText(this, "Payment setup failed: No clientSecret in response", Toast.LENGTH_LONG).show());
                }

            } catch (Exception e) {
                Log.e("PaymentActivity", "Error fetching PaymentIntent: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void presentPaymentSheet() {
        if (paymentIntentClientSecret != null) {
            PaymentSheet.Configuration config = new PaymentSheet.Configuration.Builder("LMS App")
                    .allowsDelayedPaymentMethods(true)
                    .build();

            paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, config);
        } else {
            Toast.makeText(this, "Payment setup failed. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Payment successful
            Toast.makeText(this, "Payment of $" + String.format("%.2f", amount) + " successful!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            // Payment canceled by the user
            Toast.makeText(this, "Payment canceled.", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            // Payment failed
            String errorMessage = ((PaymentSheetResult.Failed) paymentSheetResult).getError().getMessage();
            Toast.makeText(this, "Payment failed: " + errorMessage, Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}