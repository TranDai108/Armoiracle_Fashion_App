# 👗 Armoiracle Fashion App

A mobile application that serves as a personal wardrobe manager and smart outfit advisor, helping you digitize your closet and discover your personal style.

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android" alt="Platform Android">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin" alt="Language Kotlin">
  <img src="https://img.shields.io/badge/UI-Material%20Design-757575?style=for-the-badge&logo=material-design" alt="Material Design">
  <img src="https://img.shields.io/badge/API-Retrofit-informational?style=for-the-badge" alt="Retrofit">
</div>

---

## 📖 Overview

Armoiracle is a personal wardrobe manager and smart outfit advisor. With this app, users can digitize their closet, mix & match clothing items, and receive personalized outfit recommendations. The app aims to streamline the daily task of choosing what to wear by providing suggestions tailored to the user’s fashion personality, which is determined through a fun style quiz. Whether you want to keep track of your clothes, get new outfit ideas, or refine your fashion style, Armoiracle is your personal stylist in your pocket.

---

## ✨ Features

-   **Digital Closet Management:**
    -   Add clothing items to a virtual closet with photos from your camera or gallery.
    -   View all items in a grid view with details like item name and category.

-   **Mix & Match Outfit Suggestions:**
    -   Receive daily outfit ideas and style inspiration on the Home screen.
    -   Discover new ways to combine your clothes for various occasions (Vacation, Hangout, etc.).

-   **Personalized Style Quiz:**
    -   Take an interactive quiz to determine your fashion personality (e.g., creative, minimalist, dynamic).
    -   The app uses your quiz results to personalize all outfit recommendations.

-   **User Registration & Login:**
    -   Secure user accounts with a full registration and login process.
    -   Supports a "Remember Me" option for automatic login.
    -   Includes password visibility toggles and real-time validation for a smooth experience.

-   **Profile Management:**
    -   View and edit personal details, profile picture, and style preferences.
    -   Options to change your password or delete your account.

-   **Add Outfit (Planned Feature):**
    -   A UI for creating and saving custom outfits by combining items from your wardrobe. *(Note: This feature is a planned enhancement and not fully implemented in the current version.)*

-   **Logout and Account Deletion:**
    -   Securely log out to clear saved sessions.
    -   A confirmation-protected option to permanently delete your account and data.

---

## 🏗️ Architecture

Armoiracle is built with a client-server architecture, cleanly separating the UI from data and business logic.

### Android Mobile App (Frontend)

The frontend is a native Android application written in **Kotlin**, following modern Android Jetpack principles.

-   **UI:** Designed with Material Design components and `ConstraintLayout`. Key screens include `LoginActivity`, a multi-step `SignUp` flow, `HomeFragment`, `WardrobeFragment`, and `MeActivity`.
-   **Navigation:** Uses a `BottomNavigationView` for the five primary sections: Home, Profile (Me), Add (outfit), Closet, and Style Quiz.
-   **Image Loading:** Leverages **Glide** for efficient image loading and caching. The **CircleImageView** library is used for round profile images.
-   **Local Data & State:** Uses `SharedPreferences` to store login state for the "Remember Me" functionality.
-   **Asynchronous Operations:** Network calls are made with **Kotlin Coroutines** (`lifecycleScope`) to avoid blocking the main thread.

### Backend API (Server)

The app communicates with a cloud-hosted RESTful API service (base URL: `https://armoiracle-fashion-api.onrender.com/`) for data persistence.

-   **Endpoints:** Provides endpoints for user authentication, user management, quiz content, and personalized recommendations.
-   **Communication:** The app communicates with the API using **Retrofit** with **Gson** for JSON serialization/deserialization. The `ApiService` interface defines all endpoints.

### Recommendation Logic (Personalization)

Personalization is implemented on the server and client using a rule-based approach.

1.  **Quiz Analysis:** Each quiz answer is pre-tagged with fashion personality labels. The app aggregates these tags to determine the user's dominant personality type.
2.  **Profile Mapping:** The app sends the quiz result to the server, linking the user's profile to specific style categories.
3.  **Fetching Recommendations:** The app then requests recommendations, and the server returns clothing items that match the user's saved style profile.

---

## 🚀 Installation / Setup

1.  **Clone the Repository:**
    ```sh
    git clone [https://github.com/your-username/armoiracle-fashion-app.git](https://github.com/your-username/armoiracle-fashion-app.git)
    ```
2.  **Android Studio Setup:** Open the project in **Android Studio** (Arctic Fox or newer). Allow Gradle to sync and download all required dependencies.
3.  **Configure API Endpoint (if needed):** The app is pre-configured to use the live API. If you are using a different backend, update the `BASE_URL` in `RetrofitInstance.kt`:
    ```kotlin
    private const val BASE_URL = "https://your-api-url/"
    ```
4.  **Build and Run:** Connect an Android device or start an emulator and run the app. Ensure your device meets the minimum SDK version specified in the project's configuration.
5.  **Permissions:** Grant camera and storage permissions when prompted to enable all features.

---

## 💡 Usage Guide

### Authentication
-   **Login:** Enter your credentials on the Login screen. Use the "Remember me" checkbox to stay logged in.
-   **Sign Up:** Complete the two-step registration process by providing your personal details and setting a profile picture.

### Main Features
-   **Home Screen:** View a carousel of outfit themes, a preview of your closet, and personalized outfit suggestions under "Gợi ý phối đồ".
-   **Wardrobe:** Tap the closet icon (👕) to see all your clothing items in a grid. Use the "+" button to add new items from your gallery.
-   **Style Quiz:** Tap the gift icon (🎁) to start the quiz. Answer 10 multiple-choice questions to discover your fashion personality. Your results are saved to your profile and used for future recommendations.
-   **Profile (Me):** Tap the user icon to manage your account. Here you can edit your personal information, change your profile picture, retake the style quiz, log out, or delete your account.

---

## 📸 Screenshots



---

## 👥 Credits / Team

Armoiracle Fashion App was designed and developed by the **Armoiracle Team**. This project was created as a part of our passion for fashion and technology, aiming to make daily style decisions easier and more fun for everyone.
