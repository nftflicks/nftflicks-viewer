# SignPath Foundation — application copy-paste

Submit at https://signpath.org/ when this repo is public on GitHub.

## Project

- **Name:** NFT Flicks Viewer
- **Description:** Open-source Android and Windows client for the NFT Flicks streaming platform. Connects to nftflicks.com for auth, entitlements, and playback. No payment processing in the client.
- **License:** Apache-2.0 ([LICENSE](../LICENSE))
- **Repository:** `https://github.com/<org>/nftflicks-viewer`
- **Download page:** https://nftflicks.com/help.html#downloads
- **Artifacts to sign:** `NFTFlicks.exe` (Windows portable), optionally `NFTFlicks.apk`

## Build policy

- Binaries are built only in GitHub Actions from tagged releases.
- Only CI-built artifacts are submitted to SignPath.
- Private keys are never stored in the repository.

## Download page text (required after approval)

Add to https://nftflicks.com/help.html#downloads:

> Free code signing provided by [SignPath.io](https://signpath.io/), certificate by [SignPath Foundation](https://signpath.org/)

Until the first signed release ships, use:

> Windows code signing via SignPath Foundation is in progress. Use the website at nftflicks.com on Windows until the signed EXE is published.

## GitHub secrets (after approval)

| Secret | Purpose |
|--------|---------|
| `SIGNPATH_API_TOKEN` | SignPath API |
| `SIGNPATH_ORGANIZATION_ID` | SignPath org slug |
| `SIGNPATH_PROJECT_SLUG` | Project slug |
| `SIGNPATH_SIGNING_POLICY_SLUG` | Policy slug |

See `.github/workflows/release.yml`.
