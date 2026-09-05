# NFTFlicks.exe (open source)

Public Electron shell for the NFT Flicks website (watch, marketplace). Backend, payments, and admin tools are **not** in this repository.

## Build

```powershell
cd consumer-desktop
npm install
npm run dist
```

Output: `consumer-desktop/dist-build/NFTFlicks.exe` (or `dist/`, depending on electron-builder config).

Loads the public site at `https://nftflicks.com` (or local API when developing against a private backend you run yourself). Does **not** open staff portals.

## License

Apache-2.0 — see the repository root `LICENSE`.
