package com.miun.restaurantorderapp.network;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import com.miun.restaurantorderapp.network.ApiCallback;
import com.miun.restaurantorderapp.network.ApiRepository;
import com.miun.restaurantorderapp.models.OrderStatusResponse;

public class PollingService extends Service {
    private Handler handler;
    private Runnable pollingRunnable;
    private ApiRepository apiRepository;
    private Long currentOrderId;
    private static final int POLLING_INTERVAL = 1000; // 1 sek

    @Override
    public void onCreate() {
        super.onCreate();
        apiRepository = new ApiRepository();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentOrderId = intent.getLongExtra("orderId", -1);
        startPolling();
        return START_STICKY;
    }

    private void startPolling() {
        handler = new Handler(Looper.getMainLooper());
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                checkOrderStatus();
                handler.postDelayed(this, POLLING_INTERVAL);
            }
        };
        handler.post(pollingRunnable);
    }

    private void checkOrderStatus() {
        apiRepository.checkOrderStatus(currentOrderId, new ApiCallback<OrderStatusResponse>() {
            @Override
            public void onSuccess(OrderStatusResponse result) {
                if (result.isDone()) {
                    // Rätten är klar! Visa notification
                    showNotification();
                    stopPolling();
                }
            }
            @Override
            public void onError(String errorMessage) {
                // Logga fel men fortsätt polling
            }
        });
    }

    private void showNotification() {
        // TODO: Implementera notification för waiter
    }

    private void stopPolling() {
        if (handler != null && pollingRunnable != null) {
            handler.removeCallbacks(pollingRunnable);
        }
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPolling();
    }
}