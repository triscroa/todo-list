# To-Do List Application

**Team Name:** Vibing Coders  
**Team Members:** Tristan Roa, Jake Hamilton, Tyler Tiffany, Ibrahim Azam  

---

## 📱 Project Description
A full-featured Android To-Do application built in Android Studio focusing on responsive UI design. The app handles standard CRUD (create, read, update, delete) operations through dynamic adapters and a clean interface, complemented by a custom home screen widget that lets users track daily tasks at a glance without launching the main activity.

The architecture leans heavily on lifecycle-aware components, efficient state management, and local storage integration to keep data synced seamlessly between the app and widget views across various device configurations.

---

## 🛠 Features & Requirement Specifications

### 📄 UI & Interactive Task Views
- Interactive UI elements including text input fields, buttons, and dynamic list views.
- Dedicated text views and input dialogs to manage task titles, descriptions, due dates, and priority levels.
- Full CRUD operations (Create, Read, Update, Delete) dynamically handled within the main task list.
- Checkbox / completion status toggle that visually updates completed tasks.
- Task filtering and sorting options (e.g., by due date, priority, or completion status).

### ✏️ Task Details & Edit Management
- Validation errors and toast feedback when attempting to save tasks with empty required fields.
- Detailed task view displaying specific attributes when a list item is selected.
- Editable input fields for modifying existing task titles, notes, and deadlines.
- Save and cancel actions to commit modifications to local storage or discard changes safely.
- Proper Android lifecycle management to protect unsaved edits against crashes or memory leaks.

### 📲 Home Screen Widget & Synchronization
- Custom Android home screen widget displaying daily or weekly upcoming tasks at a glance.
- Remote adapter implementation to dynamically populate task lists inside the widget layout.
- PendingIntents and BroadcastReceivers enabling quick task interactions directly from the home screen widget.
- Automatic sync between main application state and widget views whenever task data changes.

### 💾 Local Data Persistence & Architecture
- Local data persistence using a structured database approach (SQLite / Room).
- Reliable data reload across application restarts and full device reboots.
- Lifecycle-aware background threading (e.g., Coroutines, LiveData/Flow) to run database operations off the main UI thread.
- Error handling and logging for potential data corruption or storage exceptions with fallback user notifications.

---

## 👥 Team Member Task Division

| Team Member | Module Focus | Assigned Requirements & Responsibilities |
| :--- | :--- | :--- |
| **Tristan Roa** | **Main UI & List Display** | • Dynamic task list view & adapters<br>• CRUD operations UI integration<br>• Task completion toggles<br>• Task sorting and filtering capabilities |
| **Jake Hamilton** | **Task Creation & Editing** | • Task input dialogs & detail views<br>• Field validation & error toasts<br>• Save/Cancel edit handling<br>• Lifecycle management for unsaved edits |
| **Tyler Tiffany** | **Home Screen Widget** | • Widget layout & RemoteViews adapter<br>• PendingIntents & Broadcast Receivers<br>• Real-time widget data synchronization<br>• Daily/Weekly task preview logic |
| **Ibrahim Azam** | **Database & Architecture** | • SQLite/Room database schema setup<br>• Background queries using lifecycle components<br>• Data persistence across restarts/reboots<br>• Exception handling & error diagnostic logging |

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio:** Jellyfish or newer recommended
- **Build System:** Gradle (Kotlin DSL / Groovy)
- **Minimum SDK:** API Level 21 (Android 5.0) or higher
- **Target SDK:** API Level 34+

### Setup & Installation
1. **Clone the repository:**
   ```bash
   git clone https://github.com/triscroa/todo-list
   ```
2. **Open Project:**
   Open Android Studio, select **Open**, and navigate to the cloned directory.
3. **Build:**
   Allow Gradle to download dependencies and sync the project.
4. **Run Application:**
   Select an emulator or physical device running Android 5.0+ and click **Run (Shift + F10)**.

---

## 🧪 Testing the Widget
1. Launch the app and create a few tasks with upcoming due dates.
2. Go to your Android device Home Screen.
3. Long-press on an empty space, tap **Widgets**, find **To-Do List**, and drag it to your screen.
4. Interact with the widget tasks to verify automatic synchronization with the main app.