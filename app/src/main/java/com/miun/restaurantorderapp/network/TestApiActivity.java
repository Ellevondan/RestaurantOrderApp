package com.miun.restaurantorderapp.network;
import com.miun.restaurantorderapp.R;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.miun.restaurantorderapp.network.ApiCallback;
import com.miun.restaurantorderapp.network.ApiService;
import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.ModifiedItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.models.OrderStatusResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Test Activity för att testa API-anrop med dummy data
 */
public class TestApiActivity extends AppCompatActivity {

    private static final String TAG = "TestApiActivity";
    private MockApiService apiService;  // ← CHANGED: Using MockApiService
    private TextView resultTextView;
    private Long testOrderId = null;
    private Long testGroupId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);

        // Use MockApiService instead of real ApiService
        apiService = new MockApiService();  // ← CHANGED: Mock API
        resultTextView = findViewById(R.id.resultTextView);

        // Test 1: Hämta meny
        Button btnFetchMenu = findViewById(R.id.btnFetchMenu);
        btnFetchMenu.setOnClickListener(v -> testFetchMenu());

        // Test 2: Skicka test-order
        Button btnSendOrder = findViewById(R.id.btnSendOrder);
        btnSendOrder.setOnClickListener(v -> testSendOrder());

        // Test 3: Kolla order status
        Button btnCheckStatus = findViewById(R.id.btnCheckStatus);
        btnCheckStatus.setOnClickListener(v -> testCheckStatus());

        // Test 4: Test connection
        Button btnTestConnection = findViewById(R.id.btnTestConnection);
        btnTestConnection.setOnClickListener(v -> testConnection());
    }

    /**
     * TEST 1: Hämta meny från server
     */
    private void testFetchMenu() {
        showLoading("Hämtar meny...");

        apiService.fetchMenu(new ApiCallback<List<MenuItem>>() {
            @Override
            public void onSuccess(List<MenuItem> menuItems) {
                Log.d(TAG, "✅ Menu fetched successfully!");
                Log.d(TAG, "Number of items: " + menuItems.size());

                StringBuilder result = new StringBuilder("✅ MENY HÄMTAD!\n\n");
                result.append("Antal rätter: ").append(menuItems.size()).append("\n\n");

                // Visa första 5 rätter
                int count = Math.min(5, menuItems.size());
                for (int i = 0; i < count; i++) {
                    MenuItem item = menuItems.get(i);
                    result.append(item.getName())
                            .append(" - ")
                            .append(item.getPrice())
                            .append(" kr\n");

                    Log.d(TAG, "Item: " + item.getName() +
                            " | Price: " + item.getPrice() +
                            " | Allergens: " + item.getAllergens());
                }

                showResult(result.toString());
                Toast.makeText(TestApiActivity.this,
                        "Meny hämtad! " + menuItems.size() + " rätter",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "❌ Failed to fetch menu: " + errorMessage);
                showResult("❌ FEL VID HÄMTNING:\n" + errorMessage);
                Toast.makeText(TestApiActivity.this,
                        "Fel: " + errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * TEST 2: Skicka test-order
     */
    private void testSendOrder() {
        showLoading("Skickar test-order...");

        // Skapa dummy OrderBundle
        OrderBundle testOrder = createDummyOrder();

        apiService.sendOrder(testOrder, new ApiCallback<OrderBundle>() {
            @Override
            public void onSuccess(OrderBundle response) {
                testOrderId = response.getId();  // Spara för test 3

                Log.d(TAG, "✅ Order sent successfully!");
                Log.d(TAG, "Order ID: " + testOrderId);

                String result = "✅ ORDER SKICKAD!\n\n" +
                        "Order ID: " + testOrderId + "\n" +
                        "Group ID: " + response.getGroupID() + "\n" +
                        "Antal rätter: " + response.getOrders().size();

                showResult(result);
                Toast.makeText(TestApiActivity.this,
                        "Order skickad! ID: " + testOrderId,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "❌ Failed to send order: " + errorMessage);
                showResult("❌ FEL VID SKICKANDE:\n" + errorMessage);
                Toast.makeText(TestApiActivity.this,
                        "Fel: " + errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * TEST 3: Kolla order status
     */
    private void testCheckStatus() {
        if (testOrderId == null) {
            Toast.makeText(this,
                    "Skicka en order först (Test 2)!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading("Kollar status för order " + testOrderId + "...");

        apiService.checkOrderStatus(testOrderId, new ApiCallback<OrderStatusResponse>() {
            @Override
            public void onSuccess(OrderStatusResponse response) {
                Log.d(TAG, "✅ Status checked!");
                Log.d(TAG, "Order ID: " + response.getId());
                Log.d(TAG, "Is Done: " + response.isDone());

                String status = response.isDone() ? "✅ KLAR!" : "⏳ INTE KLAR ÄN";
                String result = "STATUS KONTROLLERAD\n\n" +
                        "Order ID: " + response.getId() + "\n" +
                        "Status: " + status;

                showResult(result);
                Toast.makeText(TestApiActivity.this,
                        "Status: " + status,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "❌ Failed to check status: " + errorMessage);
                showResult("❌ FEL VID STATUS-KOLL:\n" + errorMessage);
                Toast.makeText(TestApiActivity.this,
                        "Fel: " + errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * TEST 4: Test connection
     */
    private void testConnection() {
        showLoading("Testar anslutning...");

        apiService.testConnection(new ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "✅ Connection successful!");
                Log.d(TAG, "Response: " + response);

                showResult("✅ ANSLUTNING OK!\n\n" + response);
                Toast.makeText(TestApiActivity.this,
                        "Anslutning fungerar!",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "❌ Connection failed: " + errorMessage);
                showResult("❌ ANSLUTNING MISSLYCKADES:\n\n" + errorMessage);
                Toast.makeText(TestApiActivity.this,
                        "Anslutning misslyckades: " + errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Skapar en dummy OrderBundle för testning
     */
    private OrderBundle createDummyOrder() {
        OrderBundle order = new OrderBundle();
        order.setGroupID(42L);  // Test group ID

        List<ModifiedItem> items = new ArrayList<>();

        // Dummy item 1
        ModifiedItem item1 = new ModifiedItem();
        item1.setId(1L);  // Cheeseburger ID från backend data
        item1.setName("Cheeseburger");
        item1.setQuantity(2);
        item1.setSelectedAllergens("milk, eggs");
        item1.setSpecialInstructions("Medium done, extra cheese");
        item1.setComments("Test order from Android app");
        item1.setOrderedAt(getCurrentTimestamp());

        // Dummy item 2
        ModifiedItem item2 = new ModifiedItem();
        item2.setId(2L);  // Walnut Garden Salad ID
        item2.setName("Walnut Garden Salad");
        item2.setQuantity(1);
        item2.setSelectedAllergens("nuts");
        item2.setSpecialInstructions("Dressing on the side");
        item2.setComments("");
        item2.setOrderedAt(getCurrentTimestamp());

        items.add(item1);
        items.add(item2);

        order.setOrders(items);

        return order;
    }

    /**
     * Hämta nuvarande timestamp i backend format
     */
    private String getCurrentTimestamp() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return now.format(formatter);
        } else {
            // Fallback för äldre Android versioner
            return "2025-12-12T12:00:00";
        }
    }

    /**
     * Visa loading-meddelande
     */
    private void showLoading(String message) {
        resultTextView.setText(message + "\n⏳");
    }

    /**
     * Visa resultat
     */
    private void showResult(String result) {
        resultTextView.setText(result);
    }
}