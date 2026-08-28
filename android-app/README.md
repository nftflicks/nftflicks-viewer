# NFT Flicks Android app

Secure Android shell for the live NFT Flicks website (login, vault, marketplace, screening room) inside a locked-down WebView + Chrome Custom Tabs for Google/Stripe.

## Google Play

See **`docs/PLAY-CONSOLE-CHECKLIST.md`**, **`docs/DATA-SAFETY.md`**, and **`docs/STORE-LISTING.md`**.

Quick path to a Play upload:

```bat
cd android-app
powershell -File tools\create-upload-keystore.ps1
gradlew.bat bundleRelease
```

Upload `app\build\outputs\bundle\release\app-release.aab` to Play Console.

Privacy / deletion URLs (required by Play):

- `https://YOUR_HOST/privacy.html`
- `https://YOUR_HOST/delete-account.html`

## Security

- HTTPS only (`usesCleartextTraffic=false` + network security config)
- Host allowlist (site, API, OAuth, Stripe, YouTube, WAX)
- Google OAuth + Stripe Checkout open in **Custom Tabs** (not the WebView)
- OAuth return via `nftflicks://oauth` deep link
- TLS errors never ignored
- File/content access disabled in WebView; mixed content blocked
- Safe Browsing when supported
- Backups disabled for WebView data
- `FLAG_SECURE` on release builds (use debug build for store screenshots)
- Device vault encryption remains end-to-end in the page (WebCrypto)

## Build APK (sideload / testing)

```bat
cd android-app
gradlew.bat assembleRelease
```

Requires `keystore.properties` (from the script above). For **local-only** testing without a keystore:

```bat
gradlew.bat assembleRelease -PUSE_DEBUG_SIGNING=true
```

Do **not** upload a debug-signed APK to Play.

## Configure URLs

Edit `app/build.gradle.kts` `SITE_URL`, `SITE_HOST`, `API_HOST` for production before public release.
