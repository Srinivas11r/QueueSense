# QueueSense - Smart Queue Intelligence

QueueSense is a modern Android application designed to help users avoid long wait times by providing real-time crowd status updates for various locations like Government offices, Hospitals, Banks, and Stores.

## 🚀 Features

- **Real-time Queue Status**: View live updates on crowd levels (Short, Moderate, Long) and estimated wait times.
- **Smart Discovery**: Find nearest queues based on your current GPS location, sorted by distance.
- **Category Filtering**: Easily browse locations by category: Govt, Health, Banks, Food, and more.
- **Live Reporting**: Contribute to the community by reporting real-time crowd status from your current location.
- **User Profiles**: Manage your personal information and track your reporting history.
- **Reviews & Ratings**: Share your experience and read feedback from other users.
- **Dark Mode Support**: Clean and intuitive UI built with Jetpack Compose.

## 🛠️ Tech Stack

- **Kotlin**: Primary language for Android development.
- **Jetpack Compose**: Modern UI toolkit for building native Android interfaces.
- **Firebase Authentication**: Secure user sign-in and sign-up.
- **Firebase Firestore**: Real-time NoSQL database for locations, reviews, and history.
- **Google Play Services Location**: For precise distance calculation and location-based features.
- **Coil**: Efficient image loading for profile pictures and location images.
- **MVVM Architecture**: Clean and maintainable code structure using ViewModels and Repositories.

## ⚙️ Setup Instructions

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/Srinivas11r/QueueSense.git
    ```
2.  **Firebase Configuration**:
    - Add your `google-services.json` file to the `app/` directory.
    - Enable **Email/Password** authentication in the Firebase Console.
    - Create a **Firestore** database.
3.  **Build & Run**:
    - Open the project in **Android Studio (Ladybug or newer)**.
    - Sync Gradle and run the app on an emulator or physical device.

## 🤝 Contributing

Contributions are welcome! If you find a bug or have a feature request, please open an issue or submit a pull request.

---
Developed by [Srinivasulu R](https://github.com/Srinivas11r)
