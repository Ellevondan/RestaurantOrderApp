# Restaurant Order App - Waiter Application

Android tablet application for restaurant servers to take orders and manage tables.

## Overview

This is the waiter-facing application in a restaurant order management system. Servers use this tablet app to:
- Select which table they're working with (1-12)
- Browse menu items and place orders
- Submit orders to the kitchen via Payara application server
- Generate final bills for customers

## System Architecture

This app communicates with:
- **Payara Application Server**: Handles all order data and menu items
- **SQL Database**: Stores orders, menu items, and group information
- **Kitchen Application**: Separate app where chefs view and manage orders

## App Flow

1. **Launch**: App requests server to create a new group ID (stored locally)
2. **Menu Load**: Fetches all menu items from server
3. **Table Selection**: Server selects which table to work on (1-12)
4. **Order Placement**: Server adds menu items to order with categories (appetizer/main/dessert)
5. **Item Customization**: Customization overlay appears when menu item is clicked -
6. **Checkout/Close Tab**: View order summary with total price
7. **Order Submission**: Orders sent to Payara server
8. **Status Polling**: App continuously polls for order status updates

## Technical Stack

- **Language**: Java
- **Min SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 36
- **Device**: Tablet (landscape orientation, 600dp smallest width)
- **Backend**: Payara Application Server with REST API
- **Database**: SQL (managed by backend)

## Project Structure

```
app/src/main/java/com/miun/restaurantorderapp/
├── MainActivity.java               # Table selection screen (UI complete)
├── OrderActivity.java              # Order placement screen (UI complete)
├── CheckOutActivity.java           # Checkout/close tab screen (UI complete)
├── CustomizationFragment.java     # Item customization overlay (UI complete)
├── models/
│   ├── MenuItem.java              # Menu item entity (TODO)
│   ├── CarteMenu.java             # Menu collection (TODO)
│   └── OrderBundle.java           # Order entity (TODO)
└── network/
    └── ApiService.java            # REST API communication (TODO)
```

## Setup Instructions

### Prerequisites
- Android Studio (latest version)
- Android SDK API 24+
- Tablet emulator or physical tablet device

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd RestaurantOrderApp
   ```

2. Open project in Android Studio

3. Configure backend server URL:
   - Open `ApiService.java`
   - Update `BASE_URL` with your Payara server address
   - Get URL from backend team

4. Sync Gradle files

5. Create tablet AVD:
   - Tools > Device Manager > Create Device
   - Select Tablet category and Medium tablet
   - Choose API level 35

6. Run the app

## Backend Integration

### Required API Endpoints

Coordinate with backend team for these endpoints:

- `POST /groups` - Create new group, returns group ID
- `GET /menus` - Fetch all CarteMenu with MenuItems
- `POST /orders` - Submit new OrderBundle
- `GET /orders?groupId={id}` - Poll orders by group ID
- `GET /orders/billing?groupId={id}` - Get all orders for billing

### Data Models

#### MenuItem (from backend)
```java
{
  "id": Long,
  "name": String,
  "description": String,
  "price": Double  // Price in SEK
}
```

**Note**: UI displays prices in SEK format (e.g., "99 SEK" instead of "$9.99")

#### OrderBundle (to backend)
```java
{
  "groupId": String,
  "tableNumber": Integer,
  "menuItemId": Long,
  "quantity": Integer,
  "category": "APPETIZER" | "MAIN" | "DESSERT" | "RUSHED",
  "status": "PENDING" | "IN_PROGRESS" | "READY" | "COMPLETED"
}
```

## Features Implementation Status

### UI Components
- [x] Project structure and TODOs
- [x] Table selection screen (12 table buttons in 3x4 grid)
- [x] Menu items display with categories (Drinks, Appetizers, Mains, Desserts)
- [x] Order summary panel (right side of OrderActivity)
- [x] Customization fragment overlay
- [x] Checkout screen with order summary and total
- [x] Navigation between all screens
- [x] Currency display in SEK (Swedish Krona)

### Backend Integration (TODO)
- [ ] Group ID management (SharedPreferences + API)
- [ ] API integration (Retrofit/HTTP library)
- [ ] Fetch menu items from server
- [ ] Order submission to server
- [ ] Status polling for order updates
- [ ] Error handling
- [ ] Loading states
- [ ] Data model implementations (MenuItem, CarteMenu, OrderBundle)

## Team Coordination

### Current State
The app currently:
- Navigates through all screens (Table Selection → Order → Checkout)
- Displays sample menu items with prices in SEK
- Shows placeholder order data in checkout
- All buttons and navigation working

### Next Steps for Backend Integration
1. Implement data model classes (MenuItem, CarteMenu, OrderBundle)
2. Add HTTP library dependency (e.g., Retrofit) to build.gradle
3. Implement ApiService.java with backend endpoints
4. Update BASE_URL in ApiService
5. Connect UI to real data from server
6. Replace sample/placeholder data with API calls
7. Implement error handling and loading states
8. Test all API endpoints with backend team

## Important Notes

### Implemented Features
- All screens locked to landscape orientation for tablet
- Table selection: 12 tables in 3x4 grid layout
- Menu items organized by category: Drinks, Appetizers, Mains, Desserts
- Customization fragment appears as overlay when menu item is clicked
- Checkout screen displays order summary with total in SEK (Swedish Krona)
- All navigation working: MainActivity → OrderActivity → CheckOutActivity
- CheckOutActivity uses sample data for UI demonstration

### Still TODO
- Group ID management (SharedPreferences + server API)
- Fetching real menu items from Payara server
- Order submission to backend
- Order status polling
- Data models implementation (MenuItem, CarteMenu, OrderBundle)
- API service implementation
- Kitchen app integration (separate project)