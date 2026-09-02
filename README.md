<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="app/src/main/assets/logo_dark.png">
    <img src="app/src/main/assets/logo.png" alt="Dentalogic8 Logo" width="320">
  </picture>
</p>

<p align="center">
  <strong>Smart Real-Time Dental Caries Detection & ICDAS Staging Powered by On-Device YOLOv12 AI</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/badge/Language-Kotlin%202.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose">
  <img src="https://img.shields.io/badge/AI%20Engine-ONNX%20Runtime%20Mobile-005CED?style=for-the-badge&logo=onnx&logoColor=white" alt="ONNX">
  <img src="https://img.shields.io/badge/Model-YOLOv12%20ICDAS-FF6F00?style=for-the-badge" alt="YOLOv12">
  <img src="https://img.shields.io/badge/Version-2.2.0-0284C7?style=for-the-badge" alt="Version">
</p>

---

## Overview

**Dentalogic8** is an advanced native Android application designed to assist dentists, researchers, and individuals in screening and detecting dental caries directly on mobile devices. 

By leveraging an on-device **YOLOv12 deep learning model** running on **Microsoft ONNX Runtime Mobile**, Dentalogic8 classifies and bounds dental lesions in real-time according to the internationally recognized **ICDAS (International Caries Detection and Assessment System) D0–D6 standard** — without sending any image data to external servers.

---

## App Showcase

### Light Mode vs. Dark Mode Comparison

| Feature / Screen | Light Theme | Dark Theme |
| :--- | :---: | :---: |
| **Home Screen**<br>_Quick actions, recent scans, feature cards_ | <img src="app-image/home-light.png" width="320" alt="Home Light"> | <img src="app-image/home-dark.png" width="320" alt="Home Dark"> |
| **Upload & Image Analysis**<br>_YOLOv12 detection with bounding boxes_ | <img src="app-image/upload-light.png" width="320" alt="Upload Light"> | <img src="app-image/upload-dark.png" width="320" alt="Upload Dark"> |
| **Clinical ICDAS Guide**<br>_Educational guide with custom D0–D6 icons_ | <img src="app-image/guide-light.png" width="320" alt="Guide Light"> | <img src="app-image/guide-dark.png" width="320" alt="Guide Dark"> |
| **Scan History**<br>_Detailed scan records and clinical risk tracking_ | <img src="app-image/history-light.png" width="320" alt="History Light"> | <img src="app-image/history-dark.png" width="320" alt="History Dark"> |
| **Profile & Settings**<br>_Branding, theme switcher, app information_ | <img src="app-image/profile-light.png" width="320" alt="Profile Light"> | <img src="app-image/profile-dark.png" width="320" alt="Profile Dark"> |

---

## Key Features

<table>
  <tr>
    <td width="33%" align="center">
      <img src="app/src/main/assets/instant.png" width="100" alt="Instant AI Scan"><br>
      <h4>Real-Time Camera Scan</h4>
      <p>Instantaneous on-device detection using CameraX with full-screen viewfinder, flashlight torch, camera switcher, and bounding box overlays.</p>
    </td>
    <td width="33%" align="center">
      <img src="app/src/main/assets/upload.png" width="100" alt="Upload Image"><br>
      <h4>Gallery Photo Analysis</h4>
      <p>Analyze pre-captured intraoral photographs. Detects caries, overlays ICDAS bounding boxes, and calculates clinical risk severity.</p>
    </td>
    <td width="33%" align="center">
      <img src="app/src/main/assets/complete_privacy.png" width="100" alt="Complete Privacy"><br>
      <h4>100% Offline & Private</h4>
      <p>All neural network inference runs entirely on-device with zero network latency. No images or patient data ever leave the device.</p>
    </td>
  </tr>
  <tr>
    <td width="33%" align="center">
      <img src="app/src/main/assets/icdas.png" width="100" alt="ICDAS Standard"><br>
      <h4>ICDAS D0–D6 Standard</h4>
      <p>Comprehensive 7-stage caries severity assessment covering everything from healthy enamel to deep pulpal cavitation.</p>
    </td>
    <td width="33%" align="center">
      <img src="app/src/main/assets/history.png" width="100" alt="Scan History"><br>
      <h4>Scan History & Details</h4>
      <p>Persistent storage for uploaded scan results, including high-res image previews, annotated bounding boxes, and lesion breakdowns.</p>
    </td>
    <td width="33%" align="center">
      <img src="app/src/main/assets/profile.png" width="100" alt="Theme Support"><br>
      <h4>Dynamic Theme Engine</h4>
      <p>Custom Tailwind Sky Blue palette with dedicated Light and Dark wallpapers, adaptive status bars, and auto-switching logos.</p>
    </td>
  </tr>
</table>

---

## ICDAS Classification Scale (D0 – D6)

Dentalogic8 categorizes caries into 7 distinct stages according to the ICDAS clinical criteria:

| Code | Illustration | Severity | Clinical Description |
| :---: | :---: | :---: | :--- |
| **D0** | <img src="app/src/main/assets/d0.png" width="60" alt="D0"> | `Normal` | **Healthy Tooth (Sound)**<br>No evidence of visible dental caries or enamel lesions. |
| **D1** | <img src="app/src/main/assets/d1.png" width="60" alt="D1"> | `Mild` | **Initial Enamel Lesion**<br>First visual change in enamel (white/brown spot) seen only after prolonged air drying. |
| **D2** | <img src="app/src/main/assets/d2.png" width="60" alt="D2"> | `Mild` | **Distinct Enamel Lesion**<br>Distinct visual change in enamel visible even when wet. |
| **D3** | <img src="app/src/main/assets/d3.png" width="60" alt="D3"> | `Moderate` | **Localized Enamel Breakdown**<br>Micro-cavitation in enamel without visible underlying dentin. |
| **D4** | <img src="app/src/main/assets/d4.png" width="60" alt="D4"> | `Moderate` | **Underlying Dentin Shadow**<br>Dark shadow of dentin shining through intact or minimally broken enamel. |
| **D5** | <img src="app/src/main/assets/d5.png" width="60" alt="D5"> | `Severe` | **Distinct Cavity with Visible Dentin**<br>Cavitation exposing underlying dentin surface. |
| **D6** | <img src="app/src/main/assets/d6.png" width="60" alt="D6"> | `Critical` | **Extensive Cavity Involving Pulp**<br>Extensive distinct cavity involving pulp with high risk of infection. |

---

## Tech Stack & Architecture

- **Language**: Kotlin 2.0 (100% native)
- **UI Toolkit**: Jetpack Compose with Material 3 Expressive Design
- **Architecture**: MVI / Clean Architecture with unidirectional data flow
- **Camera Pipeline**: CameraX (`CameraX ImageAnalysis` + `PreviewView`)
- **Deep Learning Model**: YOLOv12 Object Detection & Classification (`best_opset21.onnx`)
- **Inference Runtime**: Microsoft ONNX Runtime Mobile (`ai.onnxruntime:onnxruntime-android:1.20.0`)
- **State Management**: Kotlin Coroutines, StateFlow, Compose MutableState
- **Data Persistence**: Android Internal Storage (JPEG image files) + JSON-serialized scan records
- **Design System**: Tailored Tailwind Sky Blue palette, custom gradient scrims, glassmorphism surfaces

---

## Project Structure

```text
dentalogic8-new/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── assets/
│   │       │   ├── best_opset21.onnx      # YOLOv12 ONNX model file
│   │       │   ├── changelog.json         # Version release history
│   │       │   ├── logo.png / logo_dark.png
│   │       │   ├── background.png / background_dark.png
│   │       │   └── d0.png ... d6.png      # ICDAS clinical illustrations
│   │       ├── java/com/dentalogic/app/
│   │       │   ├── core/                  # Domain models, DentalCondition enum
│   │       │   ├── data/                  # ScanHistoryRepository & JSON persistence
│   │       │   ├── ml/                    # ONNX Runtime session & YOLOv12 pre/post-processing
│   │       │   └── ui/
│   │       │       ├── components/        # BoundingBoxOverlay, FloatingNavBar, BackNavBar
│   │       │       ├── screens/           # HomeScreen, ScanScreen, HistoryScreen, GuideScreen, ProfileTab, ScanDetailScreen
│   │       │       └── theme/             # Color tokens, Typography, Theme switcher
│   │       └── res/                       # Drawables, mipmaps, strings, and XML configurations
├── app-image/                             # Light and dark mode application screenshots
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Getting Started

### Prerequisites
- **Android Studio**: Ladybug (2024.2.1) or newer
- **JDK**: Java 17
- **Android SDK**: Compile SDK 35, Min SDK 26 (Android 8.0 Oreo+)

### Building from Source
```bash
# Clone the repository
git clone https://github.com/jodypangaribuan/dentalogic8-kotlin.git
cd dentalogic8-kotlin

# Build Debug APK
./gradlew assembleDebug

# Output APK location:
# app/build/outputs/apk/debug/app-debug.apk
```

### How to Run

#### Option 1: Using Android Studio (Recommended)
1. Open **Android Studio** (Ladybug 2024.2.1 or newer).
2. Select **Open** and choose the `dentalogic8-kotlin` project folder.
3. Wait for the Gradle project sync to complete.
4. Connect an Android device (via USB or Wireless ADB) with **USB Debugging** enabled.
   > **Note**: A physical device is strongly recommended to test real-time CameraX preview, flashlight torch, and ONNX neural network inference.
5. Select your target device in the toolbar and click **Run** (`Shift + F10` or the green Play icon).
6. Grant the **Camera Permission** when prompted on the initial launch.

#### Option 2: Using Command Line & ADB
```bash
# Ensure your device is connected and authorized
adb devices

# Build, install, and launch directly to connected device
./gradlew installDebug

# Launch the main application activity
adb shell am start -n com.dentalogic.app/.MainActivity
```

---

## Changelog Summary

### Version 2.2.0
- **Upload Photo Analysis**: Run YOLOv12 on-device caries detection on gallery photos.
- **Custom Illustration Suite**: Fully customized 3D-style icons for navigation bar, home cards, and clinical guides.
- **Dynamic Dual-Themed Branding**: Auto-switching app logos (`logo.png` & `logo_dark.png`) and custom gradient background wallpapers.
- **Full-Page Scan Details**: Comprehensive inspection screen with bounding box overlay and clinical risk assessment.

---

## Star History

<p align="center">
  <a href="https://star-history.com/#jodypangaribuan/dentalogic8-kotlin&Date">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=jodypangaribuan/dentalogic8-kotlin&type=Date&theme=dark" />
      <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=jodypangaribuan/dentalogic8-kotlin&type=Date" />
      <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=jodypangaribuan/dentalogic8-kotlin&type=Date" />
    </picture>
  </a>
</p>

---

## License

This project is distributed under the **MIT License**. See the `LICENSE` file for more details.

---

<p align="center">
  Developed by <strong>Dentalogic8 Team</strong>
</p>
