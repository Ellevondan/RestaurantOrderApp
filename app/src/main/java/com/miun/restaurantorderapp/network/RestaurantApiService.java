package com.miun.restaurantorderapp.network;

import com.miun.restaurantorderapp.models.CarteMenu;
import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.models.OrderStatusResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantApiService {

    // 1) Hämta meny (Waiter app)
    @GET("api/menu")
    Call<List<MenuItem>> getMenus();

    // 2) Skicka ny order (Waiter app)
    @POST("api/orders")
    Call<OrderBundle> sendOrder(@Body OrderBundle orderBundle);

    // 3–5) Exempel på fler lists
    @GET("api/orders")
    Call<List<OrderBundle>> getOrdersForTable(
            @Query("tableNumber") int tableNumber
    );

    @GET("api/kitchen/orders")
    Call<List<OrderBundle>> getKitchenOrders();

    // 6) Kocken uppdaterar status på en order
    @POST("api/orders/{orderId}/status")
    Call<OrderStatusResponse> updateOrderStatus(
            @Path("orderId") long orderId,
            @Body OrderBundle orderBundle
    );

    // 7) Waiter app pollar status på en specifik order
    @GET("api/orders/{orderId}/status")
    Call<OrderStatusResponse> checkOrderStatus(
            @Path("orderId") long orderId
    );

    @GET("api/test")
    Call<String> testConnection();

}
