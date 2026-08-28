# Play Store listing copy — NFT Flicks

Edit before publish. Character limits are hard caps in Play Console.

**Product model:** Android is a **viewer / library** app. Log in, browse, and watch titles you own. **Buy and sell** (Stripe / crypto / marketplace) happen on the **NFT Flicks website** in the system browser — **not** via Google Play Billing and **not** as in-app Stripe Checkout.

## Short description (≤ 80 characters)

```text
Watch films you own. Sign in, stream, Cast. Buy editions on the website.
```

(73 characters)

## Full description (≤ 4000 characters)

```text
NFT Flicks is the official Android app for the NFT Flicks streaming platform from Savage Arts Pictures.

WHAT YOU CAN DO IN THE APP
• Sign in with email or Google
• Browse featured films and your library
• Watch titles you already own in a secure screening room
• Cast to compatible TVs when configured
• Open Legal docs (Terms, Privacy, Risks, Refunds, DMCA, and more)
• Delete your account from the in-app Legal menu

BUY & SELL ON THE WEBSITE
Purchases and marketplace sales are completed on the NFT Flicks website in your browser. This app does not use Google Play Billing for digital film/NFT purchases.

HOW IT WORKS
Create an account, own a title (via the website), then open the app to watch. Card buyers get streaming access after checkout on the web. Collectible minting follows our risk-hold process described on the site.

PRIVACY & ACCOUNT CONTROL
Read our Privacy Policy in the app Legal menu or on the website. Delete your account anytime (in-app or /delete-account.html). Public blockchain records cannot be erased.

IMPORTANT
• Ages 18+
• Digital purchases on the website are generally non-refundable (see Refund Policy)
• NFT and crypto markets involve risk — see Risk Disclosures
• Access NFTs are not securities and are not investment advice
• This app uses a secure browser shell of the live NFT Flicks service

Privacy: https://nftflicks.com/privacy.html
Delete account: https://nftflicks.com/delete-account.html
Terms: https://nftflicks.com/terms.html

Support: privacy@nftflicks.com · legal@nftflicks.com
```

## Categorization suggestions

| Field | Suggestion |
| --- | --- |
| Category | Entertainment (or Video Players & Editors if that fits better) |
| Tags | movies, streaming, film, collectibles |
| Contact email | <privacy@nftflicks.com> or a dedicated support@ |
| Website | <https://NFTFlicks.com> (must serve privacy + delete-account) |

## What’s new (version 1.0.9)

```text
Security harden: narrower WebView host allowlist. Checkout redirects only to trusted Stripe/site hosts. Viewer 1.0.9 (versionCode 10).
```

## What’s new (version 1.0.8)

```text
Viewer/library focus: watch and Cast in-app; buy/sell open in your browser. Expanded Legal menu (DMCA, Marketplace, Producer License, Funding Terms). Branded launcher icon. Soft-launch hardening for Screening Room and downloads.
```

## Play Internal testing (install without sideload warning)

1. Upload `app\build\outputs\bundle\release\app-release.aab` to **Testing → Internal testing → Create release**.
2. Enroll **Play App Signing** if prompted (`PLAY-APP-SIGNING.md`).
3. Add tester Gmail accounts; share the Play **opt-in link**.

## Store assets (in-repo)

| Asset | Path |
| --- | --- |
| Icon 512 | `docs/play-store-icon-512.png` |
| Feature graphic 1024×500 | `docs/play-feature-graphic-1024x500.png` |
| Screenshots | `docs/play-screenshots/` |
