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

> Free code signing provided by SignPath.io, certificate by SignPath Foundation

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

Output: `consumer-desktop/dist/NFTFlicks.exe`

## Apply for / renew SignPath

See [docs/SIGNPATH-APPLICATION.md](docs/SIGNPATH-APPLICATION.md).

## Trademark

"NFT Flicks" is a trademark of the operator. This license does not grant trademark rights.
