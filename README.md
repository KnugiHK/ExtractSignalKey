# ExtractSignalKey
## How to
1. Get ready for your environment, including:
    * An Android device that does not have a separate security chip like Google Pixel 4 and runs **Android 11 or below**.
    * Extract the shared preference from the device.
    * Enable developer mode in Android's setting.
    * [ADB](https://developer.android.com/tools/adb).
 
2. Clone this repo:
```sh
git clone https://github.com/KnugiHK/ExtractSignalKey
cd ExtractSignalKey
```

3. Paste the `ENCRYPTED_SECRET` found in `shared_pref` to [`MainActivity.java`](https://github.com/KnugiHK/ExtractSignalKey/blob/dfa0e1f4b93ed9e52c1193b747fbbc4d8051077c/app/src/main/java/com/icf/test/MainActivity.java#L30C5-L30C55).
```
private static final String ENCRYPTED_SECRET = "";
```

4. Build the app using gradle:
```sh 
chmod +x gradlew && ./gradlew build
```

For details see my [blog post](https://blog.knugi.com/202107/151300-Decrypting-Signal-Conversation-Database.html).
