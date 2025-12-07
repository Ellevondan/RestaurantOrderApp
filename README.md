# Restaurant Order App - Waiter Application

Android tablet application for restaurant servers to take orders and manage tables.

## Overview

This is the waiter-facing application in a restaurant order management system. Servers use this tablet app to:
- Select which table they're working with (1-12)
- Browse menu items and place orders
- Submit orders to the kitchen via Payara application server
- Track order status in real-time
- Generate final bills for customers

## System Architecture

This app communicates with:
- **Payara Application Server**: Handles all order data and menu items
- **SQL Database**: Stores orders, menu items, and group information
- **Kitchen Application**: Separate app where chefs view and manage orders

## App Flow

1. **Launch**: App requests server to create a new group ID (stored locally)
2. **Menu Load**: Fetches all menu items from server (CarteMenu with MenuItems)
3. **Table Selection**: Server selects which table to work on (1-12)
4. **Order Placement**: Server adds menu items to order with categories (appetizer/main/dessert/rushed)
5. **Order Submission**: Orders sent to Payara server
6. **Status Polling**: App continuously polls for order status updates
7. **Billing**: Fetch all orders for group and calculate total

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
├── MainActivity.java          # Table selection screen
├── OrderActivity.java         # Order placement screen
├── models/
│   ├── MenuItem.java         # Menu item entity
│   ├── CarteMenu.java        # Menu collection
│   └── OrderBundle.java      # Order entity
└── network/
    └── ApiService.java       # REST API communication
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
   - Select Tablet category (e.g., Pixel Tablet)
   - Choose API level 24 or higher
   - Set orientation to Landscape

6. Run the app

## Development Workflow

### Setting up for team development

1. Each team member should:
   - Clone the repository
   - Create their own feature branch
   - Implement TODOs in assigned files

2. Coordinate with backend team for:
   - API endpoint URLs
   - Request/response JSON formats
   - MenuItem and CarteMenu entity structure
   - OrderBundle fields and status values

### TODO Implementation Guide

All files contain detailed TODO comments. Start with:

1. **Models** (`models/` package):
   - Implement MenuItem.java fields matching backend
   - Implement CarteMenu.java
   - Implement OrderBundle.java with enums

2. **Network** (`ApiService.java`):
   - Add HTTP library (Retrofit recommended)
   - Implement API methods
   - Get endpoint URLs from backend team

3. **UI Layouts** (`res/layout/`):
   - Design activity_main.xml (table selection grid)
   - Design activity_order.xml (split layout: menu items + order summary)

4. **Activities**:
   - Implement MainActivity logic
   - Implement OrderActivity logic
   - Connect UI to backend

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
  "price": Double
}
```

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

## Features to Implement

- [x] Project structure and TODOs
- [ ] Table selection UI
- [ ] Menu items grid display
- [ ] Order summary panel
- [ ] Group ID management
- [ ] API integration
- [ ] Order submission
- [ ] Status polling
- [ ] Bill calculation
- [ ] Error handling
- [ ] Loading states

## Team Coordination

### Before Backend is Ready
- Implement UI layouts
- Create model classes
- Mock API responses for testing

### After Backend is Ready
- Update BASE_URL in ApiService
- Test all API endpoints
- Verify data formats match
- Implement error handling

## Notes

- Group ID is stored in SharedPreferences and persists across app launches
- All screens locked to landscape orientation for tablet
- Orders are categorized (appetizer/main/dessert/rushed) for kitchen workflow
- App polls server for order status updates
- Kitchen app (separate project) receives and processes orders

## License

[Add your license here]

## Contributors

[Add team member names]
