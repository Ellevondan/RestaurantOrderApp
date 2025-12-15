package com.miun.restaurantorderapp.network;

import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.ModifiedItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.models.OrderStatusResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyDataProvider {

    private static final Random random = new Random();
    private static long orderIdCounter = 100;
    private static long groupIdCounter = 40;

    /**
     * Get dummy menu items matching backend JSON structure
     */
    public static List<MenuItem> getDummyMenu() {
        List<MenuItem> menu = new ArrayList<>();

        // Huvudrätter
        menu.add(createMenuItem(1L, "Cheeseburger", "Classic cheeseburger with house sauce",
                129.0, "milk, eggs", true, false, false, true, false, 11, 14, 25, false));

        menu.add(createMenuItem(2L, "Walnut Garden Salad", "Fresh garden salad with roasted walnuts",
                99.0, "nuts", false, true, false, true, true, 11, 14, 25, false));

        menu.add(createMenuItem(3L, "Grilled Salmon", "Grilled salmon with lemon butter",
                169.0, "", false, false, false, true, false, 11, 14, 25, false));

        menu.add(createMenuItem(5L, "Tofu Veggie Bowl", "Stir-fried tofu bowl with vegetables",
                109.0, "soy", false, true, false, true, true, 11, 14, 25, false));

        menu.add(createMenuItem(6L, "Spaghetti Bolognese", "Spaghetti bolognese with parmesan",
                139.0, "gluten", true, false, false, true, false, 11, 14, 25, false));

        // Förrätter
        menu.add(createMenuItem(1L, "Cheeseburger", "Classic cheeseburger with house sauce",
                129.0, "milk, eggs", false, false, true, false, false, 5, 5, 10, true));

        menu.add(createMenuItem(2L, "Walnut Garden Salad", "Fresh garden salad with roasted walnuts",
                99.0, "nuts", false, true, true, false, true, 5, 5, 10, true));

        // Efterrätter
        menu.add(createMenuItem(4L, "Lava Cake", "Chocolate lava cake with ice cream",
                89.0, "", false, false, false, false, true, 8, 7, 15, false));

        return menu;
    }

    /**
     * Helper method to create MenuItem
     */
    private static MenuItem createMenuItem(Long id, String name, String description,
                                           Double price, String allergens, boolean isMeat,
                                           boolean isVegan, boolean isAppetizer, boolean isHuvud,
                                           boolean isDessert, int activeTime, int waitingTime,
                                           int totalTime, boolean canSubstitute) {
        MenuItem item = new MenuItem();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setAllergens(allergens);
        item.setIsMeat(isMeat);
        item.setIsVegan(isVegan);
        item.setIsAppetizer(isAppetizer);
        item.setIsHuvud(isHuvud);
        item.setIsDessert(isDessert);
        item.setIsGlutenFree(false);
        item.setActiveTime(activeTime);
        item.setWaitingTime(waitingTime);
        item.setTotalTime(totalTime);
        item.setCanSubstitute(canSubstitute);
        item.setCarteAttributesId(1);
        return item;
    }

    /**
     * Create a dummy order response (simulates server response after sending order)
     */
    public static OrderStatusResponse createDummyOrderResponse() {
        OrderStatusResponse response = new OrderStatusResponse();
        response.setId(orderIdCounter++);
        response.setDone(false);  // Initially not done
        return response;
    }

    /**
     * Create a dummy group ID
     */
    public static Long createDummyGroupId() {
        return groupIdCounter++;
    }

    /**
     * Simulate order status check
     * Returns done=true after being called 5 times (simulates cooking time)
     */
    private static int statusCheckCount = 0;
    public static OrderStatusResponse checkDummyOrderStatus(Long orderId) {
        statusCheckCount++;
        OrderStatusResponse response = new OrderStatusResponse();
        response.setId(orderId);

        // After 5 checks (5 seconds of polling), mark as done
        response.setDone(statusCheckCount >= 5);

        if (response.isDone()) {
            statusCheckCount = 0;  // Reset for next order
        }

        return response;
    }

    /**
     * Provides a static list of dummy group IDs.
     * @return A list of Longs representing group IDs.
     */
    public static List<Long> getDummyGroupIds() {
        List<Long> groupIds = new ArrayList<>();
        groupIds.add(101L);
        groupIds.add(202L);
        groupIds.add(303L);
        // You can add more dummy IDs here if you want
        return groupIds;
    }

    /**
     * Create dummy orders for a group (for checkout testing)
     */
    public static List<OrderBundle> getDummyOrdersForGroup(Long groupId) {
        List<OrderBundle> orders = new ArrayList<>();

        // Order 1
        OrderBundle order1 = new OrderBundle();
        order1.setId(100L);
        order1.setGroupID(groupId);
        order1.setDone(true);

        List<ModifiedItem> items1 = new ArrayList<>();
        ModifiedItem item1 = new ModifiedItem();
        item1.setId(1L);
        item1.setName("Cheeseburger");
        item1.setQuantity(2);
        item1.setSelectedAllergens("milk, eggs");
        item1.setSpecialInstructions("Medium done");
        item1.setComments("");
        item1.setOrderedAt(getCurrentTimestamp());
        items1.add(item1);

        order1.setOrders(items1);
        orders.add(order1);

        // Order 2
        OrderBundle order2 = new OrderBundle();
        order2.setId(101L);
        order2.setGroupID(groupId);
        order2.setDone(true);

        List<ModifiedItem> items2 = new ArrayList<>();
        ModifiedItem item2 = new ModifiedItem();
        item2.setId(4L);
        item2.setName("Lava Cake");
        item2.setQuantity(1);
        item2.setSelectedAllergens("");
        item2.setSpecialInstructions("");
        item2.setComments("");
        item2.setOrderedAt(getCurrentTimestamp());
        items2.add(item2);

        order2.setOrders(items2);
        orders.add(order2);

        return orders;
    }

    /**
     * Get current timestamp in backend format
     */
    private static String getCurrentTimestamp() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return now.format(formatter);
        } else {
            return "2025-12-12T12:00:00";
        }
    }

    /**
     * Reset counters
     */
    public static void resetCounters() {
        orderIdCounter = 100;
        groupIdCounter = 40;
        statusCheckCount = 0;
    }
}