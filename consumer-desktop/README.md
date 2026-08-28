# NFTFlicks.exe

Consumer Electron shell for the public NFT Flicks site (watch, buy, marketplace).

## Build

```powershell
cd "C:\Users\User\Desktop\Cursor\nft flicks beta\consumer-desktop"
npm install
npm run dist
```

Output: `consumer-desktop\dist\NFTFlicks.exe`

Public download path (site): `Website\nftflickbeta\downloads\NFTFlicks.exe`  
Also synced into `server\data\apps\` and the AFM USB pack `Desktop\NFT Flicks\` by:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File ".\tools\package-all-clients.ps1"
```

(from the `nft flicks beta` repo root)

Loads `http://127.0.0.1:8095/index.html` when the local API is up, otherwise the remote public host. Does **not** open staff portals.
