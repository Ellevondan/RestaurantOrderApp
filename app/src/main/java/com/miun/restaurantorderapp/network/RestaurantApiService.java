package com.miun.restaurantorderapp.network;

import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.models.OrderStatusResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.DELETE;

public interface RestaurantApiService {

    // ===== MENY =====

    /**
     * Hämta aktiv carte-meny med alla items
     * GET /api/carte-menu/active
     *
     * VIKTIGT: Backend skickar MenuItem + CarteAtributes separat!
     * Du behöver en CarteMenuItem DTO i backend som kombinerar båda,
     * eller hantera mappningen i appen.
     */
    @GET("carte-menu/active")
    Call<List<MenuItem>> getActiveCarteMenu();

    /**
     * Alias för getActiveCarteMenu()
     */
    @GET("carte-menu/active")
    Call<List<MenuItem>> getMenus();


    // ===== SKAPA GRUPP =====

    /**
     * Skapa en ny grupp/order
     * POST /api/orders
     *
     * Backend: service.saveOrder(new OrderGroup())
     * Returnerar: Long (orderGroup ID)
     *
     * VIKTIGT: Backend tar INTE emot @Body data just nu!
     */
    @POST("orders")
    Call<Long> createGroup();


    // ===== SKICKA ORDER =====

    /**
     * Skicka ny order med data
     * POST /api/orders
     *
     * KRITISKT PROBLEM: Backend implementationen gör bara:
     *   service.saveOrder(new OrderGroup())
     * och ignorerar @Body!
     *
     * LÖSNINGAR:
     * A) Uppdatera backend OrderCaptureAPI.capture() till:
     *    public Response capture(OrderGroup orderGroup) {
     *        long id = service.saveOrder(orderGroup);
     *        return Response.ok(id).build();
     *    }
     *
     * B) Eller använd createGroup() och uppdatera ordern separat
     */
    @POST("orders")
    Call<Long> sendOrder(@Body OrderBundle orderBundle);


    // ===== HÄMTA ORDERS =====

    /**
     * Hämta orders för ett bord
     * GET /api/forwardsTo
     *
     * PROBLEM: Backend har inget tableNumber-filter!
     *
     * TILLFÄLLIG LÖSNING: Hämta alla och filtrera i appen:
     *   List<OrderBundle> allOrders = response.body();
     *   List<OrderBundle> tableOrders = allOrders.stream()
     *       .filter(o -> o.getTableNumber() == tableNumber)
     *       .collect(Collectors.toList());
     *
     * PERMANENT LÖSNING: Lägg till i backend:
     *   @GET
     *   @Produces(MediaType.APPLICATION_JSON)
     *   public Response getForwardedOrders(@QueryParam("tableNumber") Integer tableNum) {
     *       List<OrderGroup> orders = tableNum != null
     *           ? orderService.getOrdersByTable(tableNum)
     *           : orderService.getActiveOrders();
     *       return Response.ok(orders).build();
     *   }
     */
    @GET("forwardsTo")
    Call<List<OrderBundle>> getOrdersForTable(@Query("tableNumber") int tableNumber);

    /**
     * Hämta alla aktiva orders (för köket)
     * GET /api/forwardsTo
     */
    @GET("forwardsTo")
    Call<List<OrderBundle>> getKitchenOrders();


    // ===== UPPDATERA ORDER STATUS =====

    /**
     * Markera order som klar
     * PUT /api/orders/{orderId}/reset
     *
     * PROBLEM: Backend tar inte emot @Body data!
     * Backend gör bara: service.markOrderasDone(id)
     *
     * Du kan skicka orderBundle men det ignoreras av backend.
     */
    @PUT("orders/{orderId}/reset")
    Call<Void> updateOrderStatus(
            @Path("orderId") long orderId,
            @Body OrderBundle orderBundle
    );

    /**
     * Kolla om order är klar
     * GET /api/forwardsTo/{orderId}/isDone
     *
     * Backend returnerar: {"isDone": true/false}
     * Din OrderStatusResponse måste matcha detta!
     */
    @GET("forwardsTo/{orderId}/isDone")
    Call<OrderStatusResponse> checkOrderStatus(@Path("orderId") long orderId);


    // ===== ÖVRIGA (som INTE finns i backend) =====

    /**
     * FINNS INTE I BACKEND - kommer att ge 404
     * Ta bort eller skapa endpoint i backend
     */
    @GET("test")
    Call<String> testConnection();

    /**
     * Hämta orders för en grupp
     * GET /api/forwardsTo
     *
     * SAMMA PROBLEM som getOrdersForTable() - inget groupID-filter i backend.
     * Filtrera i appen tills vidare.
     */
    @GET("forwardsTo")
    Call<List<OrderBundle>> fetchGroupOrders(@Query("groupID") long groupId);

    /**
     * FINNS INTE I BACKEND - kommer att ge 404
     */
    @GET("groups/active")
    Call<List<Long>> getActiveGroupIds();

    /**
     * FINNS INTE I BACKEND - kommer att ge 404
     */
    @DELETE("groups/{groupId}")
    Call<Void> deleteGroup(@Path("groupId") long groupId);
}