# Play Console — Data safety answers (NFT Flicks)

Align these answers with `privacy.html` (last updated July 19, 2026). Update both if collection changes.

## Overview answers

| Question | Answer |
| --- | --- |
| Does your app collect or share user data? | **Yes** |
| Is all user data encrypted in transit? | **Yes** (HTTPS / TLS) |
| Do you provide a way for users to request deletion? | **Yes** |
| Deletion URL | `https://nftflicks.com/delete-account.html` |
| Independent security review | No (unless you commission one) |

## Data types collected

Mark **Collected** (and **Shared** only where a third party receives it for their own purposes).

### Personal info

| Type | Collected | Shared | Purpose | Required / Optional |
| --- | --- | --- | --- | --- |
| Email address | Yes | Shared with email provider (transactional mail) | Account management | Required for email accounts |
| Name (display) | Yes (if provided) | No | Account management | Optional |
| User IDs | Yes | No | Account / fraud | Required when signed in |
| Address | No | — | — | — |
| Phone | No | — | — | — |

### Financial info

| Type | Collected | Shared | Purpose |
| --- | --- | --- | --- |
| Purchase history | Yes if same account bought on the **website** | Stripe processes cards on website checkout | App functionality / fraud prevention |
| Credit card number | **No** (Stripe handles PCI on web) | Stripe | — |
| Play Billing / in-app digital goods | **No** | — | Viewer/library app; buy/sell on website |

### Photos / videos

| Type | Notes |
| --- | --- |
| Photos / videos | **Not collected in the Android app.** Producer uploads are website-only (WebView has no file chooser). |

### App activity

| Type | Collected | Purpose |
| --- | --- | --- |
| App interactions | Yes (pages, playback entitlement checks) | App functionality, analytics (first-party logs) |
| In-app search history | No (unless you add search logging) | — |
| Other actions | Watch / library; purchases on website | App functionality |

### Web browsing

Not collected beyond in-app WebView navigation to our Service / allowlisted partners.

### App info and performance

| Type | Collected | Purpose |
| --- | --- | --- |
| Crash logs | Possible via OS / future tooling | Analytics / stability |
| Diagnostics | Server request logs (IP, UA, path) | Security / fraud / performance |
| Other app performance | No dedicated SDK today | — |

### Device or other IDs

| Type | Collected | Purpose |
| --- | --- | --- |
| Device IDs | Trusted-device fingerprints / UA for security | Fraud prevention / account security |

## Data sharing (third parties)

Declare **sharing** or **processing** as appropriate:

| Party | Data | Role |
| --- | --- | --- |
| **Stripe** | Payment metadata, customer identifiers | Payment processor (card checkout) |
| **Google** | OAuth profile/email when user chooses Google Sign-In | Identity provider |
| **YouTube** | Playback embeds may set cookies | Video hosting (trailers / embeds) |
| **Blockchain RPCs / WAX / marketplaces** | Public wallet addresses & on-chain txs | Network infrastructure (public chain data) |
| **Email delivery** (e.g. Gmail API / SMTP) | Email address, message content for codes | Transactional email |

**Advertising / data brokers:** No.  
**Sell personal info (CCPA):** No.

## Security practices

- Encryption in transit: Yes  
- Encryption at rest for vault ciphertext: Yes (device E2E + server stores ciphertext)  
- Users can request deletion: Yes (in-app + web + email)

## Account deletion (policy)

- In-app path: **Delete account** nav control → `delete-account.html`  
- Web resource for Play form: same URL  
- Also: `privacy@nftflicks.com`  
- Blockchain history cannot be deleted
