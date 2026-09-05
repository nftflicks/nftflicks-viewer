# SignPath Foundation — application copy-paste

Submit / update at [signpath.org](https://signpath.org/) using the **active NFTFlicks** public repository only.

## Project

- **Name:** NFT Flicks Viewer
- **Operator / GitHub:** NFTFlicks (`nftflicks`)
- **Description:** Open-source Android and Windows client for the NFT Flicks streaming platform. Connects to nftflicks.com for auth, entitlements, and playback. No payment processing in the client.
- **License:** Apache-2.0 ([LICENSE](../LICENSE)) — SPDX: Apache-2.0
- **Repository (active):** `https://github.com/nftflicks/nftflicks-viewer`
- **Download page:** [nftflicks.com/help.html#downloads](https://nftflicks.com/help.html#downloads)
- **Reputation:** [docs/REPUTATION.md](REPUTATION.md)
- **Artifacts to sign:** `NFTFlicks.exe` (Windows portable), optionally `NFTFlicks.apk`

## Build policy

- Binaries for signing are built only in GitHub Actions from tagged releases (`v*.*.*`) or gated workflow_dispatch.
- Only CI-built artifacts are submitted to SignPath.
- Private keys are never stored in the repository (SignPath HSM after approval).

## Download page text (required after approval)

Add to [nftflicks.com/help.html#downloads](https://nftflicks.com/help.html#downloads):

> Free code signing provided by [SignPath.io](https://signpath.io/), certificate by [SignPath Foundation](https://signpath.org/)

Until the first signed release ships, use:

> Windows code signing via SignPath Foundation is in progress. Use the website at nftflicks.com on Windows until the signed EXE is published.

## GitHub secrets (after approval)

Set on [nftflicks/nftflicks-viewer Actions secrets](https://github.com/nftflicks/nftflicks-viewer/settings/secrets/actions) only — never commit:

| Secret | Purpose |
| -------- | --------- |
| `SIGNPATH_API_TOKEN` | SignPath API |
| `SIGNPATH_ORGANIZATION_ID` | SignPath org id |
| `SIGNPATH_PROJECT_SLUG` | Project slug |
| `SIGNPATH_SIGNING_POLICY_SLUG` | Policy slug |

Also set repository variable `SIGNPATH_ENABLED=true` when ready. See `.github/workflows/release.yml`.
