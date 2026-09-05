# Google Play Console checklist — NFT Flicks

Use this when creating the Play listing for `com.nftflicks.app`.

**Full launch map (Cast + Play + prod):** see `../../server/docs/LAUNCH-READY.md`.  
**Soft-launch leftovers (mail, Turnstile, counsel):** see `../../server/docs/SOFT-LAUNCH-OPERATOR.md`.

## Product model (disclose accurately)

- **In-app:** login, browse, watch entitled titles, Cast, vault view, Legal menu, account deletion.
- **Website (system browser):** buy NFTs / Stripe checkout, sell/transfer marketplace, producer commerce.
- **No Google Play Billing** for digital film/NFT purchases in this build.

## Before you upload

- [ ] Google Play Developer account ($25) verified (organization preferred for company brand)
- [ ] Run `tools\create-upload-keystore.ps1` once; **back up** `nftflicks-upload.jks` + `keystore.properties`
- [ ] Set production `SITE_URL` / `SITE_HOST` / `API_HOST` in `app/build.gradle.kts` (not beta tunnels) before **public** production launch (Internal testing may keep beta hosts)
- [ ] Build **App Bundle**:

```bat
cd android-app
gradlew.bat bundleRelease
```

Output: `app\build\outputs\bundle\release\app-release.aab`

- [ ] Confirm AAB is signed with the **upload** keystore (not Android Debug)
- [ ] Upload **only** `android-app/app/build/outputs/bundle/release/app-release.aab` (`com.nftflicks.app`). Never upload `android-cast-register` (`com.nftflicks.castregister`) or `android-admin`.
- [ ] Enroll in **Play App Signing** when uploading the AAB (see `PLAY-APP-SIGNING.md`)
  1. Play Console → **Testing → Internal testing → Create new release**
  2. When prompted, enroll **Google Play App Signing**
  3. Upload `app\build\outputs\bundle\release\app-release.aab`
  4. Confirm **Setup → App signing** shows “App signing by Google Play”

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
| `targetSdk` / `compileSdk` 36 | Yes (`1.0.8` / versionCode 9) |
| `minSdk` 26 | Yes |
| HTTPS only / no cleartext | Yes |
| Backup disabled for WebView data | Yes |
| Round + adaptive launcher icon | Yes |
| Account deletion in-app + web | Yes |
| Google Sign-In via Custom Tabs | Yes |
| Buy/sell → system browser | Yes (not in-app Stripe) |
| Release signing with upload keystore | Via `keystore.properties` |

## Store assets (prepared in-repo)

| Asset | Spec | Path |
| --- | --- | --- |
| App icon | 512×512 PNG | `docs/play-store-icon-512.png` |
| Feature graphic | 1024×500 PNG | `docs/play-feature-graphic-1024x500.png` |
| Phone screenshots | ≥2 | `docs/play-screenshots/01-home-phone.png`, `02-privacy-phone.png` |
| Short / full description | Copy | `docs/STORE-LISTING.md` |

## Payments / digital goods

Disclose: this is a **viewer/library** app; digital purchases complete on the website. Read [Google Play payments policy](https://support.google.com/googleplay/android-developer/answer/9858738). Counsel should confirm before **production** track.

## Chromecast

- Env has `CAST_APP_ID` when configured.
- While the Cast receiver app is **Unpublished**, add your Chromecast device serial in the Google Cast Developer Console.
- Publish the receiver when you want all devices to work without serial allowlisting.

## Operator steps (short)

1. Play Console → create app `NFT Flicks` (`com.nftflicks.app` only).
2. Paste listing from `docs/STORE-LISTING.md`; upload icon / feature graphic / screenshots from `docs/`.
3. Fill **Data safety** from `docs/DATA-SAFETY.md`; set privacy + account deletion URLs (table above).
4. Complete IARC; audience **18+**; disclose no Play Billing (website checkout).
5. Build and upload AAB (commands above); enroll **Play App Signing** on first upload (`PLAY-APP-SIGNING.md`).
6. Release to **Internal testing**; smoke login / watch / Cast / buy-opens-browser.
7. Before **production** track: switch `SITE_URL` / `SITE_HOST` / `API_HOST` off beta tunnels and re-bundle.

Staff APKs (`android-admin`, `android-studio` flavors, cast-register) are **sideload only** — never upload to Play.

## After upload

- [ ] Internal testing track live; opt-in link shared
- [ ] Data safety form matches `privacy.html` + `DATA-SAFETY.md`
- [ ] Account deletion URL works signed-in and signed-out
- [ ] Google Sign-In return via HTTPS App Links (no custom `nftflicks://` scheme)
- [ ] Confirm buy CTAs open the **system browser**, not an in-app checkout
