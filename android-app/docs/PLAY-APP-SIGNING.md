# Play App Signing — enroll when uploading the AAB

Google Play App Signing is enrolled in **Play Console** on first upload (not in this repo). Your upload keystore is already ready.

## What you already have

| Artifact | Location |
|----------|----------|
| Upload keystore | `android-app/nftflicks-upload.jks` (local only; gitignored) |
| Keystore config | `android-app/keystore.properties` (gitignored) |
| Signed AAB | `android-app/app/build/outputs/bundle/release/app-release.aab` |

**Back up** the `.jks` + `keystore.properties` offline. If you lose the upload key, recovery requires Play support.

## Enroll (first release upload)

1. Open [Google Play Console](https://play.google.com/console) → create app **NFT Flicks** (`com.nftflicks.app`) if needed.
2. Go to **Testing → Internal testing** (recommended first) → **Create new release**.
3. When prompted for **Play App Signing**:
   - Choose **Continue** / enroll with **Google-managed app signing key** (default).
   - Do **not** opt out unless you have a specific reason.
4. **Upload** `NFTFlicks.aab` (or `app-release.aab`).
5. Play verifies the AAB is signed with your **upload key**, then generates and stores the **app signing key**.
6. Complete release notes → **Save** → **Review release** → **Start rollout to Internal testing**.

After enrollment:

- You keep signing AABs with the **upload** keystore (`nftflicks-upload.jks`).
- Play re-signs installs with the **app signing** key.
- Under **Setup → App signing** you can view certificate fingerprints (useful for API key restrictions).

## Rebuild AAB (if needed)

```bat
cd android-app
gradlew.bat bundleRelease
```

Copy output:

```text
app\build\outputs\bundle\release\app-release.aab
```

## Optional: confirm upload-key signature

```bat
jarsigner -verify -verbose -certs app\build\outputs\bundle\release\app-release.aab
```

You should see the upload certificate (alias `nftflicks`), not the Android debug cert.

## After first upload

- [ ] Confirm **App signing** page shows “App signing by Google Play”
- [ ] Download **app signing** and **upload** key certificates if offered
- [ ] Store upload keystore backup in a second location
- [ ] Continue listing / Data safety / content rating from `PLAY-CONSOLE-CHECKLIST.md`
