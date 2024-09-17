## **Android Application Development Project: Smart-Light-android-app**

### **Objective**
Develop an Android application to demonstrate proficiency in API integration, user interface design, and Android development best practices.

### **Project Overview**
This app consists of three main screens: 
1. **Login Screen**: Allows users to authenticate using an API.
2. **Dashboard Screen**: Displays data retrieved from an API.
3. **Details Screen**: Provides detailed information about a selected item.

### **API Details**
- **Base URL**: `https://vu-nit3213-api.onrender.com`
- **Endpoints**:
  - **Login**: `/auth` (use appropriate location endpoint: `/footscray/auth`, `/sydney/auth`, `/ort/auth`)
  - **Dashboard**: `/dashboard/{keypass}`

### **App Features**
1. **Login Screen:**
   - User inputs for authentication.
   - POST request to the login API endpoint.
   - Error handling for unsuccessful login attempts.

2. **Dashboard Screen:**
   - Displays a list of entities retrieved from the API.
   - Allows navigation to the Details screen on item selection.

3. **Details Screen:**
   - Shows detailed information about a selected entity.

### **Technical Implementation**
- Utilizes dependency injection for better code management.
- Follows clean code principles.
- Includes basic unit tests.
- Managed through Git with version control best practices.

### **Setup Instructions**

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/Shannonsmith01/Smart-Light-android-app.git
