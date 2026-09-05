# Public reputation and verifiable references

Independently checkable links for SignPath Foundation and reviewers. All product branding is **NFTFlicks** / **NFT Flicks**.

## Active public source (this project)

| Item | URL |
| --- | --- |
| Public OSS repository | https://github.com/nftflicks/nftflicks-viewer |
| License | Apache-2.0 — [LICENSE](../LICENSE) |
| Release / SignPath workflow | https://github.com/nftflicks/nftflicks-viewer/blob/master/.github/workflows/release.yml |
| Actions | https://github.com/nftflicks/nftflicks-viewer/actions |

## Live product (same software clients serve)

| Item | URL |
| --- | --- |
| Production site | https://nftflicks.com |
| Downloads / help | https://nftflicks.com/help.html#downloads |
| Android APK | https://nftflicks.com/downloads/NFTFlicks.apk |
| Checksums | https://nftflicks.com/downloads/SHA256SUMS.txt |
| Provenance (Ed25519-signed when key present) | https://nftflicks.com/downloads/provenance.json |
| Component SBOM | https://nftflicks.com/downloads/sbom.spdx.json |
| Malware-scan evidence | https://nftflicks.com/downloads/malware-scan.json |
| Rollback proof | https://nftflicks.com/downloads/rollback.json |

## Honest release status

- Soft-launch browsing on https://nftflicks.com is live.
- Public Android APK is published with checksum / provenance evidence.
- Windows `NFTFlicks.exe` remains unpublished until SignPath Foundation trusted signing is approved and CI produces a signed artifact. Unsigned EXE is not served.

## Dual-repo note (standard industry practice)

- **Public:** this viewer repo (SignPath reviews license, source, build/signing workflow).
- **Private:** proprietary platform (`nftflicks/nftflicks-platform`) — backend, payments, admin. Not submitted for Foundation OSS review. Secrets stay in local env / keystores / GitHub Actions secrets after approval — never committed.
