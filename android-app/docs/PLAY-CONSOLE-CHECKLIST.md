# Google Play Console checklist — NFT Flicks

Use this when creating the Play listing for `com.nftflicks.app` (consumer viewer only).

## Product model (disclose accurately)

- **In-app:** login, browse, watch entitled titles, Cast, vault view, Legal menu, account deletion.
- **Website (system browser):** buy NFTs / Stripe checkout, sell/transfer marketplace, producer commerce.
- **No Google Play Billing** for digital film/NFT purchases in this build.

## Before you upload

- [ ] Google Play Developer account ($25) verified (organization preferred for company brand)
- [ ] Create an upload keystore locally (never commit `keystore.properties` or `.jks` files)
- [ ] Set production `SITE_URL` / `SITE_HOST` / `API_HOST` in `app/build.gradle.kts` before **public** production launch
- [ ] Build **App Bundle**:

```bat
cd android-app
gradlew.bat bundleRelease
```

Output: `app\build\outputs\bundle\release\app-release.aab`

- [ ] Confirm AAB is signed with the **upload** keystore (not Android Debug)
- [ ] Upload **only** the consumer app AAB (`com.nftflicks.app`)
- [ ] Enroll in **Play App Signing** when uploading the AAB (see `PLAY-APP-SIGNING.md`)

## App content (Play Console)

| Field | Value |
| --- | --- |
| Privacy policy URL | `https://nftflicks.com/privacy.html` |
| Account deletion URL | `https://nftflicks.com/delete-account.html` |
| App access | Browse may be open; sign-in required to watch library |
| Ads | No ads |
| Content rating | Complete IARC (films / entertainment; UGC filmmaker uploads); **18+** |
| Target audience | 18+; do **not** target children |
| Data safety | Fill using `docs/DATA-SAFETY.md` |
| Financial features | Viewer/library in-app; buy/sell on website. **No Play Billing.** |

## Technical (already in the APK/AAB)

| Requirement | Status |
| --- | --- |
| `targetSdk` / `compileSdk` 36 | Yes |
| `minSdk` 26 | Yes |
| HTTPS only / no cleartext | Yes |
| Backup disabled for WebView data | Yes |
| Round + adaptive launcher icon | Yes |
| Account deletion in-app + web | Yes |
| Google Sign-In via Custom Tabs | Yes |
| Buy/sell → system browser | Yes (not in-app Stripe) |
| Release signing with upload keystore | Via gitignored `keystore.properties` |

## Store assets (prepared in-repo)

| Asset | Spec | Path |
| --- | --- | --- |
| App icon | 512×512 PNG | `docs/play-store-icon-512.png` |
| Feature graphic | 1024×500 PNG | `docs/play-feature-graphic-1024x500.png` |
| Phone screenshots | ≥2 | `docs/play-screenshots/` |
| Short / full description | Copy | `docs/STORE-LISTING.md` |

## Payments / digital goods

Disclose: this is a **viewer/library** app; digital purchases complete on the website. Read [Google Play payments policy](https://support.google.com/googleplay/android-developer/answer/9858738).

## Chromecast

- Env has `CAST_APP_ID` when configured.
- While the Cast receiver app is **Unpublished**, add your Chromecast device serial in the Google Cast Developer Console.
- Publish the receiver when you want all devices to work without serial allowlisting.

## After upload

- [ ] Internal testing track live; opt-in link shared
- [ ] Data safety form matches `privacy.html` + `DATA-SAFETY.md`
- [ ] Account deletion URL works signed-in and signed-out
- [ ] Google Sign-In return via HTTPS App Links (no custom scheme)
- [ ] Confirm buy CTAs open the **system browser**, not an in-app checkout
