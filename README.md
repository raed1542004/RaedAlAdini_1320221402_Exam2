# TabAuthApp

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/Language-Java-007396?logo=java&logoColor=white)
![minSdk](https://img.shields.io/badge/minSdk-24-4CAF50)
![Target SDK](https://img.shields.io/badge/targetSdk-34-2196F3)
![AGP](https://img.shields.io/badge/AGP-8.2.2-673AB7)

**Raed AlAdini** — Student ID **1320221402**
Android development coursework — **Exam 2**: secure authentication with a modern, tabbed user interface.

---

## Overview

TabAuthApp is a compact Android application built with **Java**, **AndroidX**, and **Material Components**. It demonstrates two core Android patterns working together:

1. A gated entry screen (`MainActivity`) where the Login button activates only after live validation of an email address and a six-character password.
2. A modern tabbed screen (`ModeActivity`) built on `TabLayout` + `ViewPager2`, with custom back-stack behavior, a runtime theme-switching popup menu, and ancestral (Up) navigation.

The project was developed **entirely in a cloud environment** — see [Cloud Development](#cloud-development) for what that means for this repository.

## Requirements Summary

The exam brief defines two requirements:

**Requirement 1 — Authentication & App Exit (`MainActivity`)**

- Email + Password fields and a Login button.
- `TextWatcher`-based dynamic validation: the button enables only when the email is valid and the password is at least six characters long.
- Custom exit logic: the first back press shows a Snackbar ("Press back again to exit"); a second press within two seconds exits the application.
- Login navigates to `ModeActivity`.

**Requirement 2 — Swipeable Tabs & Navigation (`ModeActivity`)**

- `TabLayout` with `ViewPager2` providing three swipeable views.
- Three distinct tabs named **First**, **Second**, and **Third**.
- Back-stack behavior: back on the Second/Third tab returns to the First tab; back on the First tab returns to `MainActivity`.
- A **Set Mode** button opens a popup menu with **Light / Night / System** mode options.
- The Up button (ancestral navigation) returns directly to `MainActivity`.

## Implemented Features

### 1. Dynamic authentication logic — `MainActivity.java`

A single `TextWatcher` is attached to both input fields; every text change re-runs `validateInputs()`:

```java
private void validateInputs() {
    String email = emailField.getText() != null ? emailField.getText().toString().trim() : "";
    String pass = passField.getText() != null ? passField.getText().toString() : "";

    boolean validEmail = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    boolean validPass = pass.length() >= 6;

    loginBtn.setEnabled(validEmail && validPass);
}
```

- The button starts **disabled** (`android:enabled="false"` in `activity_main.xml`) and never becomes clickable until both conditions hold.
- Email correctness uses the platform's `Patterns.EMAIL_ADDRESS`; the password check is `length() >= 6`.
- A successful login starts `ModeActivity` through an explicit `Intent`.

### 2. Double-back-press exit — `MainActivity.java`

```java
@Override
public void onBackPressed() {
    long now = System.currentTimeMillis();
    if (now - lastBackTime < 2000) {
        finishAffinity();
        return;
    }
    lastBackTime = now;
    Snackbar.make(findViewById(R.id.main_root), R.string.exit_prompt, Snackbar.LENGTH_SHORT).show();
}
```

- First press: a Snackbar shows **"Press back again to exit"**.
- Second press inside the 2000 ms window: `finishAffinity()` closes the whole task.

### 3. Swipeable tabs — `ModeActivity.java`

- `ViewPager2` hosts three `TabFragment` pages through a `TabsAdapter` (`FragmentStateAdapter`).
- `TabLayoutMediator` binds the `TabLayout` to the pager and labels each tab from string resources (`First`, `Second`, `Third`).
- `TabFragment` follows the state-safe fragment pattern: `newInstance(String title)` plus `Bundle` arguments, so every page survives recreation.

### 4. Custom back-stack behavior — `ModeActivity.java`

```java
@Override
public void onBackPressed() {
    if (pager != null && pager.getCurrentItem() > 0) {
        pager.setCurrentItem(0);
        return;
    }
    super.onBackPressed();
}
```

- On the **Second or Third** tab, back returns to the **First** tab.
- On the **First** tab, back leaves `ModeActivity` and returns to `MainActivity`, where the exit-Snackbar logic applies.

### 5. Popup menu and Up navigation — `ModeActivity.java`, `mode_menu.xml`

- The **Set Mode** button inflates `mode_menu.xml` into a `PopupMenu` with three options wired to `AppCompatDelegate`:

| Menu item | Behaviour |
| --- | --- |
| Light | `MODE_NIGHT_NO` |
| Night | `MODE_NIGHT_YES` |
| System | `MODE_NIGHT_FOLLOW_SYSTEM` |

- Up navigation uses the standard ancestor setup: `parentActivityName` plus the `PARENT_ACTIVITY` meta-data in the manifest, `setDisplayHomeAsUpEnabled(true)` at runtime, and `finish()` on `android.R.id.home` — returning directly to `MainActivity`.

## Grading Criteria Mapping

| Criterion | Marks | Where it lives in this repository |
| --- | ---: | --- |
| Authentication Logic (functional `TextWatcher`s) | 2.0 | `MainActivity.java` — `validateInputs()`, wired in `onCreate()` |
| Exit Mechanism (Snackbar + double back) | 1.5 | `MainActivity.java` — `onBackPressed()` |
| Swipe Views (`TabLayout` + `ViewPager2`, 3 tabs) | 2.0 | `ModeActivity.java`, `TabsAdapter.java`, `TabFragment.java`, `activity_mode.xml` |
| Back-stack Management (2nd/3rd tab back to the 1st) | 1.5 | `ModeActivity.java` — `onBackPressed()` |
| Advanced UI (popup menu + Up button) | 1.5 | `ModeActivity.java`, `mode_menu.xml`, `AndroidManifest.xml` |
| Demo & Submission (video + valid repository link) | 1.5 | This public GitHub repository |
| **Total** | **10.0** | |

## Project Structure

```text
RaedAlAdini_1320221402_Exam2/
├── app/
│   ├── build.gradle                  # namespace, minSdk 24, compileSdk 34, dependencies
│   └── src/main/
│       ├── AndroidManifest.xml       # activities, launcher intent-filter, Up-navigation metadata
│       ├── java/com/example/tabauthapp/
│       │   ├── MainActivity.java     # validation + double-back exit + navigation to ModeActivity
│       │   ├── ModeActivity.java     # tabs, popup menu, Up navigation, custom back-stack
│       │   ├── TabFragment.java      # one page per tab (title passed via arguments)
│       │   └── TabsAdapter.java      # FragmentStateAdapter for ViewPager2
│       └── res/
│           ├── layout/
│           │   ├── activity_main.xml # login form: two EditTexts + initially disabled Login button
│           │   ├── activity_mode.xml # Set Mode button + TabLayout + ViewPager2
│           │   └── fragment_tab.xml  # single TextView shown by each tab page
│           ├── menu/
│           │   └── mode_menu.xml     # Light / Night / System options
│           └── values/
│               └── strings.xml       # all UI text, including exit prompt and tab names
├── gradle/wrapper/                   # Gradle 8.7 wrapper (jar + properties)
├── build.gradle                      # Android Gradle Plugin 8.2.2
├── gradle.properties                 # AndroidX flags
├── gradlew                           # Gradle wrapper script
└── settings.gradle                   # repositories + ':app' module
```

The build configuration committed in this repository: **Java 8** source/target compatibility, **AGP 8.2.2**, **Gradle 8.7** via the wrapper, `compileSdk`/`targetSdk` **34**, `minSdk` **24**, and the libraries `androidx.appcompat:appcompat:1.6.1`, `com.google.android.material:material:1.11.0`, and `androidx.viewpager2:viewpager2:1.0.0`.

## Cloud Development

This project was produced **without a local development machine**: the code was written, reviewed, and committed entirely through a cloud-based workflow, and everything in this repository was delivered from that environment.

What this means in practice:

- **Authoring**: all Java, XML, and Gradle files were written and edited in a cloud workspace, not on a personal laptop.
- **Version control**: the project is versioned with Git and pushed straight to this GitHub repository; the history starts from a single clean `Initial commit - TabAuthApp`.
- **No emulator run**: the cloud environment provides no local emulator or display, so the application was not executed on a device there. Because of that, this README intentionally contains no emulator screenshots — the sections above document the implementation directly from the committed source.
- **Ready for review**: the repository ships a complete Gradle setup (wrapper included), so anyone reviewing it — including the instructor — can sync and run it in a standard Android Studio setup by following the steps below.

## How to Run

1. Clone the repository:

   ```bash
   git clone https://github.com/raed1542004/RaedAlAdini_1320221402_Exam2.git
   ```

2. Open **Android Studio** (Flamingo 2022.2.1 or newer — required by AGP 8.2.2, which needs JDK 17) and choose **Open**, then select the cloned folder.
3. Let Gradle sync finish — the wrapper automatically downloads **Gradle 8.7**; no manual configuration is needed.
4. Run the `app` configuration on a device or emulator running **API 24 (Android 7.0) or higher**.
5. A quick test route that covers every requirement:
   - Leave the fields empty — the Login button stays disabled.
   - Type an invalid email, then a valid one with a password of six or more characters — the button enables.
   - Log in and swipe across the **First / Second / Third** tabs.
   - Press back from the Second or Third tab — you land on the First tab; press back again — you are back in `MainActivity`.
   - Press back once — the Snackbar appears; press back again within two seconds — the app exits.
   - Choose **Set Mode → Night** — the whole UI switches to the dark theme; the Up button in the action bar returns you directly to `MainActivity`.

## Challenges & What I Learned

- **Wiring `ViewPager2` correctly**: `ViewPager2` does not talk to `TabLayout` on its own; using `TabLayoutMediator` instead of the older adapter APIs was the key to keeping tab labels and pages in sync.
- **State-safe fragments**: passing the tab title through `newInstance()` arguments rather than constructors or fields keeps pages stable across configuration changes.
- **Validation as UX, not a gate**: enabling the Login button continuously through a `TextWatcher` gives instant feedback instead of showing errors only after a click.
- **Owning the back button**: overriding `onBackPressed()` at two levels — returning to the first tab inside `ModeActivity`, and the timed double-press exit in `MainActivity` — shows how back-stack behavior can be tailored without breaking platform navigation.
- **Day/night theming**: one call to `AppCompatDelegate.setDefaultNightMode()` plus the `DayNight` theme switches the entire UI; the popup menu simply maps menu ids to the three modes.
- **Working cloud-first**: building and shipping a project with no local machine reinforced disciplined source control — the repository itself is the deliverable, so it must stay complete, clean, and buildable by anyone who clones it.

---

*Raed AlAdini — 1320221402*
