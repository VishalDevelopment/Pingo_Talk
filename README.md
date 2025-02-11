PingoTalk - The Next-Gen Chatting Experience! 🚀

PingoTalk is a modern and interactive messaging app designed to make communication seamless, fast, and fun. Whether you're chatting with friends, family, or colleagues, PingoTalk offers an intuitive and feature-rich experience to stay connected like never before.

📌 Features

Real-time Messaging - Chat with friends instantly using Firestore as the backend.

Secure Authentication - Firebase Authentication for seamless login and sign-up.

AI-Powered Chatbot - Integrated Google Gemini AI (Bard API) for intelligent conversations.

Modern UI with Jetpack Compose - A fully responsive, beautiful, and user-friendly interface.

Dependency Injection with Hilt - Ensuring a clean and scalable architecture.

MVVM Architecture - Proper separation of concerns for maintainability.

Kotlin Coroutines & Flow - Efficient asynchronous operations.

🛠 Tech Stack

Kotlin - Primary programming language.

Jetpack Compose - UI toolkit for modern Android development.

Firebase Firestore - Real-time NoSQL database.

Firebase Authentication - Secure user authentication.

Google Gemini AI (Bard API) - AI-powered chatbot.

Hilt - Dependency Injection for better app structure.

MVVM Architecture - Clean and scalable code structure.

Kotlin Coroutines & Flow - For managing background tasks efficiently.

🚀 Getting Started

Prerequisites

Android Studio (Latest Version)

Firebase Account (For Firestore & Auth Setup)

Google Cloud Account (For Bard API Integration)

🔧 Installation Steps

Clone the Repository

git clone https://github.com/yourusername/PingoTalk.git
cd PingoTalk

Setup Firebase

Create a Firebase project and enable Firestore & Authentication.

Download google-services.json and place it inside the app directory.

Enable Google Gemini AI (Bard API)

Go to Google Cloud Console and enable the Gemini AI API.

Generate an API key and add it to your local.properties:

BARD_API_KEY=your_api_key_here

Build & Run the App

./gradlew build

Open the project in Android Studio and run the app on an emulator or device.

📂 Project Structure

PingoTalk/
│── app/
│   ├── src/main/java/com/example/pingotalk/
│   │   ├── di/         # Hilt Modules
│   │   ├── data/       # Repository & Models
│   │   ├── ui/         # Jetpack Compose Screens
│   │   ├── viewmodel/  # MVVM ViewModels
│── build.gradle
│── local.properties

🤝 Contributing

We welcome contributions! Feel free to open a PR or issue.

Developer: Vishal

Email: vishalgoswami165342@gmail.com

GitHub: [GitHub Profile](https://github.com/VishalDevelopment)
