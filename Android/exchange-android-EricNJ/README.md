# Currency Exchange Platform

## Overview

This platform provides LBP/USD currency exchange services, offering real-time exchange rates, transaction handling,a robust chatting system, and user management functionalities. Designed for Android devices, it leverages Kotlin, Retrofit, and Compose to deliver a seamless user experience.

## System Requirements

- **OS**: Android SDK 21 (Lollipop) or higher
- **Development Environment**: Android Studio 4.0 or higher
- **Java Version**: Java 8
- **Kotlin Version**: 1.8.0

## Installation Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/EECE-430L/ExchangeRate-7
   ```
2. **Open Android Studio**:
   - Open the project in Android Studio by selecting "Open an existing project".
3. **Sync Gradle**:
   - Ensure that all dependencies are correctly synced by Android Studio.

## Configuration

- **API Base URL**: Set the API base URL in the `ExchangeService` object.
  ```kotlin
  private const val API_URL: String = "https://jason.hydra-polaris.ts.net/"
  ```
- **Build Configurations**: Adjust build configurations as necessary in `build.gradle`.

## Dependencies

Ensure these dependencies are included in your `build.gradle` file:

```gradle
implementation 'androidx.core:core-ktx:1.8.0'
implementation platform('org.jetbrains.kotlin:kotlin-bom:1.8.0')
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.3.1'
implementation 'androidx.activity:activity-compose:1.5.1'
implementation platform('androidx.compose:compose-bom:2022.10.00')
implementation 'androidx.compose.ui:ui'
implementation 'androidx.compose.ui:ui-graphics'
implementation 'androidx.compose.material3:material3'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'de.hdodenhof:circleimageview:3.1.0'
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
implementation 'androidx.viewpager2:viewpager2:1.0.0'
```

## Usage

To run the application:

1. **Build the application**:
   - Use the build option in Android Studio to build the APK.
2. **Run the application**:
   - Use an Android emulator or a physical device to run the app.

## API Endpoints

- `GET /exchangeRate`: Fetches current exchange rates.
- `POST /transaction`: Submits a new transaction.
- `GET /transaction`: Retrieves all transactions.
- Detailed API documentation can be found [here](#) (add the link to your API documentation).

## Troubleshooting

- Ensure all required Android SDKs are installed.
- Check network permissions if API calls are failing.
