# NFT Flicks Viewer (open source)

Public **Android APK** and **Windows portable EXE** for watching NFT Flicks films. Backend, payments, admin, and studio tools remain proprietary and are not in this repository.

## Download

- Website: https://nftflicks.com/help.html#downloads
- GitHub Releases: (after first SignPath-signed release)

## License

Apache License 2.0 — see [LICENSE](LICENSE).

## Code signing (SignPath)

Windows builds are signed in CI via [SignPath Foundation](https://signpath.org/) (free for qualifying OSS). No PFX or signing passwords are stored in this repo.

Attribution (required on download pages):

> Free code signing provided by [SignPath.io](https://signpath.io/), certificate by [SignPath Foundation](https://signpath.org/)

### Code signing policy

- **Authors / committers:** Savage Arts Pictures maintainers with write access to this repository
- **Reviewers:** Pull requests require maintainer review before merge to `master`
- **Approvers:** Release tags (`v*.*.*`) trigger CI build; SignPath signing requests require maintainer approval in SignPath.io after subscription is active
- **Privacy:** This program loads the NFT Flicks website for auth and playback. See https://nftflicks.com/privacy.html

## Build locally

### Android

```powershell
cd android-app
.\gradlew.bat assembleRelease
```

Output: `android-app/app/build/outputs/apk/release/app-release.apk`

### Windows (Electron)

```powershell
cd consumer-desktop
npm install
npm run dist
```

Output: `consumer-desktop/dist-build/NFTFlicks.exe`

## Apply for / renew SignPath

See [docs/SIGNPATH-APPLICATION.md](docs/SIGNPATH-APPLICATION.md).

## Trademark

"NFT Flicks" is a trademark of the operator. This license does not grant trademark rights.
