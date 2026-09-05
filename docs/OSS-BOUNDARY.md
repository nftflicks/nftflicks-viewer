# OSS boundary — what is public vs private

This repository ([nftflicks/nftflicks-viewer](https://github.com/nftflicks/nftflicks-viewer)) is the **public Apache-2.0** consumer client for SignPath Foundation review and community builds.

## Public (this repo)

- Android WebView shell (`android-app`)
- Windows Electron shell (`consumer-desktop`)
- Minimal shared client helpers (e.g. shell host allowlists)
- Docs for building the consumer clients
- GitHub Actions release workflow (SignPath gated; secrets never committed)

## Private (not in this repo)

| Repo | Contents |
| --- | --- |
| `nftflicks/nftflicks-platform` | Backend API, payments, admin, jobs, secrets via local `.env` |
| `nftflicks/nftflicks-website` | Full website HTML/assets, screening rooms, admin portal pages |

## Must never appear in public exports

- `.env`, `signing.env`, keystores (`.jks` / `.pfx`), Stripe secret keys / webhook signing secrets
- Wallet private keys, mnemonics, PEM private keys
- Server CSP inventories listing staff or title-specific secure routes
- Absolute operator machine paths (Windows user profile paths)
- Links into private monorepo operator runbooks

Public clients may reference **https://nftflicks.com** as the product host. That is intentional and not a secret.

## Export

From the private monorepo:

```powershell
powershell -File tools/export-viewer-oss.ps1
```

The exporter allowlists shared client files and fails if banned patterns are detected.
