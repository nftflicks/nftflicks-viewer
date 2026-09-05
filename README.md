# NFT Flicks Viewer (open source)

**Actively maintained** public clients for [NFT Flicks](https://nftflicks.com): Android APK and Windows portable EXE. This repository is the SignPath Foundation review surface (Apache-2.0).

Backend, payments, admin, and studio tools remain **proprietary** in a separate private repository and are **not** included here. Clients connect to `https://nftflicks.com` at runtime.

**SPDX-License-Identifier:** Apache-2.0 — see [LICENSE](LICENSE) and [NOTICE](NOTICE).

## Repository

- Public OSS (this repo): [github.com/nftflicks/nftflicks-viewer](https://github.com/nftflicks/nftflicks-viewer)
- Product / downloads: [nftflicks.com/help.html#downloads](https://nftflicks.com/help.html#downloads)
- Reputation / verifiable refs: [docs/REPUTATION.md](docs/REPUTATION.md)
- OSS boundary (public vs private): [docs/OSS-BOUNDARY.md](docs/OSS-BOUNDARY.md)

## Download

- Website: [nftflicks.com/help.html#downloads](https://nftflicks.com/help.html#downloads)
- Public Android APK + checksums / provenance on the site (commerce freeze may still withhold Windows EXE until SignPath signing)
- GitHub Releases: after the first SignPath-signed Windows release

## License

Apache License 2.0 (OSI-approved) — see [LICENSE](LICENSE).

## Code signing (SignPath)

Windows builds are signed in CI via [SignPath Foundation](https://signpath.org/) after approval. No PFX or signing passwords are stored in this repository. The signing private key remains on SignPath’s HSM.

Attribution (required on download pages after signed releases ship):

> Free code signing provided by SignPath.io, certificate by SignPath Foundation

Until the first signed EXE ships, use the website at nftflicks.com on Windows.

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

## CI / release

See [`.github/workflows/release.yml`](.github/workflows/release.yml). SignPath submit and GitHub Release jobs are gated on repository variable `SIGNPATH_ENABLED` (off until Foundation approval).

## Apply for / renew SignPath

See [docs/SIGNPATH-APPLICATION.md](docs/SIGNPATH-APPLICATION.md).

## Trademark

"NFT Flicks" / "NFTFlicks" are trademarks of the operator. This license does not grant trademark rights.
