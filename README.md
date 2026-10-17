# VaultLab 🏦📱

> **Synthetic Digital Banking Training Environment & Mobile Security Laboratory**

VaultLab is a hands-on Android banking application designed specifically for cybersecurity training, mobile penetration testing, and static/dynamic security analysis. It simulates a digital banking platform with intentionally embedded, controlled security challenges and vulnerabilities for educational purposes.

---

## 📌 Repository Short Description

> *A synthetic Android digital banking application designed for mobile app security testing, reverse engineering, and hands-on vulnerability labs.*

---

## 📑 Table of Contents

- [Features](#-features)
- [Tech Stack & Architecture](#-tech-stack--architecture)
- [Security Challenges Overview](#-security-challenges-overview)
  - [M1 — Hardcoded Admin Credentials](#m1--hardcoded-admin-credentials)
- [Prerequisites](#-prerequisites)
- [Building & Installation](#-building--installation)
- [Running Tests](#-running-tests)
- [Challenge Walkthrough (M1)](#-challenge-walkthrough-m1)
- [Disclaimer](#-disclaimer)

---

## ✨ Features

- **Realistic Banking UI & Navigation:** Dashboard with account balances, transaction history, profile details, and app settings.
- **Multi-User Synthetic Authentication:** Pre-configured training user accounts and administrative roles.
- **Embedded Security Challenges:** Guided reverse-engineering and static/dynamic analysis challenges.
- **Local Isolation:** Completely offline application with no network transmission or real user credentials.

---

## 🛠 Tech Stack & Architecture

- **Language:** Kotlin
- **Build System:** Gradle (Kotlin DSL, AGP `9.2.1`)
- **Android SDK:** Compile SDK `37`, Target SDK `36`, Min SDK `24`
- **UI Architecture:** AndroidX AppCompat, Material Design Components, Fragment Navigation
- **Data Layer:** `SyntheticRepository` (Local `SharedPreferences` session management)

```
VaultLab
├── app
│   ├── src
│   │   ├── main
│   │   │   ├── java/com/example/vaultlab
│   │   │   │   ├── data/             # Models (User, Transaction) & SyntheticRepository
│   │   │   │   ├── ui/               # Auth, Dashboard, Transactions, Account, Settings
│   │   │   │   └── MainActivity.kt   # Core Navigation Host
│   │   │   └── res/                  # Layouts, Navigation, Resources (strings.xml)
│   │   └── test/                     # Unit Tests (SyntheticRepositoryTest)
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🔐 Security Challenges Overview

### M1 — Hardcoded Admin Credentials
- **Category:** Reverse Engineering & Static Analysis (OWASP Mobile Top 10 - M1: Improper Credential Usage)
- **Objective:** Discover hidden administrative credentials embedded inside the application binary using decompilation tools like **JADX**.
- **Completion Criteria:** Log in using the discovered administrative credentials to unlock the challenge success notification:
  > *"🎉 أحسنت! اكتشفت بيانات اعتماد Admin المخفية وأكملت تحدّي M1."*

---

## 🚀 Prerequisites

- **Android Studio:** Ladybug (2024.2+) or newer
- **JDK:** Java 11 or higher
- **Decompilation Tool (for Trainees):** [JADX GUI / CLI](https://github.com/skylot/jadx) or Bytecode Viewer

---

## ⚙️ Building & Installation

### 1. Clone the Repository
```bash
git clone https://github.com/YourUsername/VaultLab.git
cd VaultLab
```

### 2. Build Debug APK
```bash
./gradlew :app:assembleDebug
```
*Output Path:* `app/build/outputs/apk/debug/app-debug.apk`

### 3. Build Release APK (Unsigned)
```bash
./gradlew :app:assembleRelease
```
*Output Path:* `app/build/outputs/apk/release/app-release-unsigned.apk`

---

## 🧪 Running Tests

To run local unit tests and verify authentication and challenge logic:

```bash
./gradlew testDebugUnitTest
```

---

## 💡 Challenge Walkthrough (M1)

### Step 1: Obtain the Application APK
Build or download the APK file (`app-debug.apk` or `app-release-unsigned.apk`).

### Step 2: Decompile with JADX
Open the APK in **JADX-GUI** or decompile using CLI:
```bash
jadx app-debug.apk
```

### Step 3: Analyze Authentication Logic
1. Navigate to package `com.example.vaultlab.data` and open `SyntheticRepository`.
2. Inspect the companion object properties:
   - `ADMIN_USER_BYTES` = `new byte[]{97, 100, 109, 105, 110}` ➔ `"admin"`
   - `ADMIN_PASS_BYTES` = `new byte[]{86, 97, 117, 108, 116, 65, 100, 109, 105, 110, 50, 48, 50, 53, 83, 101, 99, 114, 101, 116, 33}` ➔ `"VaultAdmin2025Secret!"`

### Step 4: Validate Credentials in App
1. Launch **VaultLab** on an emulator or Android device.
2. Enter the discovered credentials on the Login screen:
   - **Username:** `admin`
   - **Password:** `VaultAdmin2025Secret!`
3. Click **Sign In** and verify challenge completion!

---

## ⚠️ Disclaimer

VaultLab is strictly created for **educational and training purposes only**. The vulnerabilities intentionally included in this application (such as hardcoded byte-represented credentials) are synthetic and designed solely for mobile application security assessments in controlled laboratory environments. Do not reuse synthetic credentials or insecure patterns in production applications.
