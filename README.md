# SocialMedia

[![Kotlin](https://img.shields.io/badge/Kotlin-1.8-blue)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Compose-UI-orange)](https://developer.android.com/jetpack/compose)
[![MVVM](https://img.shields.io/badge/Architecture-MVVM-green)]
[![Hilt](https://img.shields.io/badge/DI-Hilt-purple)]
[![Retrofit](https://img.shields.io/badge/Networking-Retrofit-lightgrey)]

## Overview

**SocialMedia** is an Android application built as a task for a company process.  
It allows users to browse posts on the home screen, create new posts (with multipart image upload), view post details, and edit or delete existing posts.  
The app follows clean **MVVM architecture**, uses **Hilt** for dependency injection, **Retrofit** for networking, and modern UI built with **Jetpack Compose**.

---

## Features

- 📰 **Home Screen** – Browse a list of posts  
- ✍ **Create Post** – Add a post with image upload (multipart/form-data)  
- 📄 **Post Details** – View, edit, or delete posts  
- ⚡ Reactive UI with Jetpack Compose  
- 🔗 MVVM + Coroutines + Retrofit + Hilt  

---

### Design Pattern
MVVM (Model–View–ViewModel)

Ensures a clean separation between UI, business logic, and data layers.

Makes the app easier to test and maintain.

---
### External Packages Used
<li> Retrofit → To handle API requests in a clean and maintainable way.</li>

<li> Hilt → To implement dependency injection for easier testing and scalability.</li>

<li> Glide → For efficient image loading and caching.</li>

<li> Jetpack Compose Navigation → To manage navigation between app screens.</li>

---

## Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Architecture:** MVVM  
- **Dependency Injection:** Hilt  
- **Networking:** Retrofit  
- **File Upload:** Multipart (Retrofit `@Multipart`)  
- **Asynchronous:** Kotlin Coroutines  
- **Navigation:** Compose Navigation  

---

## Screenshots
<div style="display: flex; gap: 10px;">
  <img src="https://github.com/user-attachments/assets/9dad9898-a121-4e93-98a7-059533730b40" alt="Splash Screen" width="200"/>
  <img src="https://github.com/user-attachments/assets/46fc6491-8186-4d79-b0b1-6c354b8954ad" alt="Home Screen" width="200"/>
  <img src="https://github.com/user-attachments/assets/ba464dc9-688a-4ea9-888f-78a800608540" alt="Create New Post" width="200"/>
  <img src="https://github.com/user-attachments/assets/4c6053a9-3494-49ea-9ecb-2a32067e14f8" alt="Post Detail" width="200"/>
</div>
---

---

## Setup

### Requirements

- Android Studio Flamingo or newer  
- Kotlin 1.7+  
- Minimum SDK 21+  
- Internet permission in `AndroidManifest.xml`:
  ```xml
  <uses-permission android:name="android.permission.INTERNET" />

### Installation

1-Clone the repository:
```bash
git clone https://github.com/<your-username>/SocialMedia.git
cd SocialMedia
```
2-Open in Android Studio.

3-Sync Gradle.

4-Configure API base URL in BuildConfig or local.properties.

5-Run the app.

---

### License
MIT License

---
### Contact
Ziad Helaly – ziadhelaly99@gmail.com




