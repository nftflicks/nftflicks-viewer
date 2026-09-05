"use strict";

/** Exact WAX HTTPS/WSS endpoints (Retest 8 — no *.wax.io wildcards). */
const WAX_CONNECT_HTTPS = [
  "https://all-access.wax.io",
  "https://wax.greymass.com",
  "https://wax.api.atomicassets.io",
  "https://api.atomicassets.io",
  "https://wax.bloks.io",
];

const WAX_CONNECT_WSS = ["wss://wax.greymass.com", "wss://wax.api.atomicassets.io"];

const SITE_CONNECT = [
  "https://nftflicks.com",
  "https://www.nftflicks.com",
  "wss://nftflicks.com",
  "wss://www.nftflicks.com",
];

const CHAIN_CONNECT = [
  "https://polygon-rpc.com",
  "https://polygon-bor-rpc.publicnode.com",
  "https://ethereum.publicnode.com",
  "https://api.mainnet-beta.solana.com",
];

const AUTH_CONNECT = [
  "https://accounts.google.com",
  "https://oauth2.googleapis.com",
  "https://api.stripe.com",
  "https://challenges.cloudflare.com",
];

const IMG_SRC = [
  "'self'",
  "data:",
  "blob:",
  "https://i.ibb.co",
  "https://www.cinemaclock.com",
  "https://i.ytimg.com",
  "https://s.ytimg.com",
  "https://lh3.googleusercontent.com",
];

/** App-tier pages: producer, market, secure room, etc. */
const APP_PAGES = new Set([
  "index.html",
  "market.html",
  "producer.html",
  "studio.html",
  "financing.html",
  "cast.html",
  "secure.html",
  "secure-officer.html",
  "secure-gunrun.html",
  "secure-fatal.html",
  "b36de07888d9.html",
]);

/** Legal/static tier — no unsafe-inline, no WAX. */
const LEGAL_PAGES = new Set([
  "legal.html",
  "terms.html",
  "privacy.html",
  "cookie-policy.html",
  "risk-disclosures.html",
  "refund-policy.html",
  "dmca.html",
  "marketplace-terms.html",
  "funding-terms.html",
  "producer-license.html",
  "delete-account.html",
  "help.html",
  "activate.html",
  "admin.html",
  "studio-login.html",
  "tv.html",
  "filmmakers.html",
]);

function connectSrcApp(extraOrigins = []) {
  return [
    "'self'",
    "blob:",
    ...SITE_CONNECT,
    ...WAX_CONNECT_HTTPS,
    ...WAX_CONNECT_WSS,
    ...extraOrigins.filter(Boolean),
    ...CHAIN_CONNECT,
    ...AUTH_CONNECT,
  ];
}

function connectSrcLegal() {
  return ["'self'", "https://nftflicks.com", "https://www.nftflicks.com", "https://challenges.cloudflare.com"];
}

/** Helmet CSP directives for the Node API (app-tier superset). */
function helmetDirectives(extraConnectOrigins = []) {
  return {
    "default-src": ["'self'"],
    "script-src": [
      "'self'",
      "https://www.youtube.com",
      "https://s.ytimg.com",
      "https://cdnjs.cloudflare.com",
      "https://challenges.cloudflare.com",
      "https://www.gstatic.com",
      "https://cast.google.com",
      "https://accounts.google.com",
    ],
    "frame-src": [
      "'self'",
      "https://www.youtube-nocookie.com",
      "https://www.youtube.com",
      "https://youtube.com",
      "https://all-access.wax.io",
      "https://accounts.google.com",
      "https://challenges.cloudflare.com",
    ],
    "img-src": IMG_SRC,
    "style-src": ["'self'", "'unsafe-inline'", "https://cdnjs.cloudflare.com", "https://fonts.googleapis.com"],
    "font-src": ["'self'", "https://cdnjs.cloudflare.com", "https://fonts.gstatic.com", "data:"],
    "media-src": ["'self'", "blob:"],
    "worker-src": ["'self'", "blob:"],
    "connect-src": connectSrcApp(extraConnectOrigins),
    "frame-ancestors": ["'self'"],
    "object-src": ["'none'"],
    "base-uri": ["'self'"],
    "form-action": ["'self'", "https://accounts.google.com", "https://checkout.stripe.com"],
  };
}

function joinDirective(name, values) {
  return `${name} ${values.join(" ")}`;
}

function metaCspApp() {
  return [
    joinDirective("default-src", ["'self'"]),
    joinDirective("script-src", [
      "'self'",
      "https://www.youtube.com",
      "https://s.ytimg.com",
      "https://cdnjs.cloudflare.com",
      "https://challenges.cloudflare.com",
      "https://www.gstatic.com",
      "https://cast.google.com",
      "https://accounts.google.com",
    ]),
    joinDirective("style-src", ["'self'", "'unsafe-inline'", "https://cdnjs.cloudflare.com", "https://fonts.googleapis.com"]),
    joinDirective("img-src", IMG_SRC),
    joinDirective("font-src", ["'self'", "https://cdnjs.cloudflare.com", "https://fonts.gstatic.com", "data:"]),
    joinDirective("connect-src", connectSrcApp()),
    joinDirective("media-src", ["'self'", "blob:"]),
    joinDirective("worker-src", ["'self'", "blob:"]),
    joinDirective("frame-src", [
      "'self'",
      "https://www.youtube.com",
      "https://www.youtube-nocookie.com",
      "https://challenges.cloudflare.com",
      "https://accounts.google.com",
      "https://all-access.wax.io",
    ]),
    joinDirective("object-src", ["'none'"]),
    joinDirective("base-uri", ["'self'"]),
  ].join("; ") + ";";
}

function metaCspAppCompact() {
  return [
    joinDirective("default-src", ["'self'"]),
    joinDirective("script-src", ["'self'", "https://cdnjs.cloudflare.com", "https://challenges.cloudflare.com", "https://www.gstatic.com", "https://accounts.google.com"]),
    joinDirective("style-src", ["'self'", "'unsafe-inline'", "https://cdnjs.cloudflare.com"]),
    joinDirective("img-src", IMG_SRC),
    joinDirective("font-src", ["'self'", "https://cdnjs.cloudflare.com", "data:"]),
    joinDirective("connect-src", connectSrcApp()),
    joinDirective("frame-src", ["'self'", "https://challenges.cloudflare.com"]),
    joinDirective("object-src", ["'none'"]),
    joinDirective("base-uri", ["'self'"]),
  ].join("; ") + ";";
}

function metaCspAppSecure() {
  return [
    joinDirective("default-src", ["'self'"]),
    joinDirective("script-src", ["'self'", "https://www.gstatic.com", "https://www.youtube.com", "https://s.ytimg.com"]),
    joinDirective("style-src", ["'self'", "'unsafe-inline'"]),
    joinDirective("img-src", IMG_SRC),
    joinDirective("media-src", ["'self'", "blob:"]),
    joinDirective("connect-src", connectSrcApp()),
    joinDirective("worker-src", ["'self'", "blob:"]),
    joinDirective("frame-src", ["'self'", "https://www.youtube.com", "https://www.youtube-nocookie.com"]),
    joinDirective("object-src", ["'none'"]),
    joinDirective("base-uri", ["'self'"]),
  ].join("; ") + ";";
}

function metaCspAppMarket() {
  return [
    joinDirective("default-src", ["'self'"]),
    joinDirective("script-src", ["'self'", "https://challenges.cloudflare.com", "https://www.gstatic.com"]),
    joinDirective("style-src", ["'self'", "'unsafe-inline'"]),
    joinDirective("img-src", IMG_SRC),
    joinDirective("connect-src", connectSrcApp()),
    joinDirective("frame-src", ["'self'", "https://challenges.cloudflare.com", "https://all-access.wax.io"]),
    joinDirective("object-src", ["'none'"]),
    joinDirective("base-uri", ["'self'"]),
  ].join("; ") + ";";
}

function metaCspAppAdmin() {
  return [
    joinDirective("default-src", ["'self'"]),
    joinDirective("script-src", ["'self'", "https://challenges.cloudflare.com", "https://www.gstatic.com"]),
    joinDirective("style-src", ["'self'", "'unsafe-inline'", "https://fonts.googleapis.com"]),
    joinDirective("img-src", IMG_SRC),
    joinDirective("font-src", ["'self'", "https://fonts.gstatic.com", "data:"]),
    joinDirective("connect-src", connectSrcApp()),
    joinDirective("frame-src", ["'self'", "https://challenges.cloudflare.com"]),
    joinDirective("object-src", ["'none'"]),
    joinDirective("base-uri", ["'self'"]),
  ].join("; ") + ";";
}

function metaCspLegal() {
  return [
    joinDirective("default-src", ["'self'"]),
    joinDirective("script-src", ["'self'", "https://challenges.cloudflare.com"]),
    joinDirective("style-src", ["'self'", "https://fonts.googleapis.com"]),
    joinDirective("img-src", ["'self'", "data:"]),
    joinDirective("font-src", ["'self'", "https://fonts.gstatic.com", "data:"]),
    joinDirective("connect-src", connectSrcLegal()),
    joinDirective("frame-src", ["'self'", "https://challenges.cloudflare.com"]),
    joinDirective("object-src", ["'none'"]),
    joinDirective("base-uri", ["'self'"]),
  ].join("; ") + ";";
}

function metaCspLegalExtended() {
  return [
    joinDirective("default-src", ["'self'"]),
    joinDirective("script-src", ["'self'", "https://challenges.cloudflare.com", "https://www.gstatic.com"]),
    joinDirective("style-src", ["'self'", "https://fonts.googleapis.com"]),
    joinDirective("img-src", IMG_SRC),
    joinDirective("font-src", ["'self'", "https://fonts.gstatic.com", "data:"]),
    joinDirective("connect-src", connectSrcLegal()),
    joinDirective("frame-src", ["'self'", "https://challenges.cloudflare.com"]),
    joinDirective("object-src", ["'none'"]),
    joinDirective("base-uri", ["'self'"]),
  ].join("; ") + ";";
}

/** Map HTML filename to CSP meta string. */
function metaCspForPage(filename) {
  switch (filename) {
    case "index.html":
      return metaCspApp();
    case "market.html":
      return metaCspAppMarket();
    case "producer.html":
    case "studio.html":
      return metaCspAppCompact();
    case "financing.html":
      return [
        joinDirective("default-src", ["'self'"]),
        joinDirective("script-src", ["'self'", "https://challenges.cloudflare.com", "https://www.gstatic.com"]),
        joinDirective("style-src", ["'self'", "'unsafe-inline'"]),
        joinDirective("img-src", IMG_SRC),
        joinDirective("connect-src", connectSrcApp()),
        joinDirective("frame-src", ["'self'", "https://challenges.cloudflare.com"]),
        joinDirective("object-src", ["'none'"]),
        joinDirective("base-uri", ["'self'"]),
      ].join("; ") + ";";
    case "cast.html":
      return metaCspAppSecure();
    case "secure.html":
    case "secure-officer.html":
    case "secure-gunrun.html":
    case "secure-fatal.html":
      return metaCspAppSecure();
    case "b36de07888d9.html":
      return metaCspAppAdmin();
    case "filmmakers.html":
      return metaCspLegalExtended();
    default:
      if (LEGAL_PAGES.has(filename)) return metaCspLegal();
      if (APP_PAGES.has(filename)) return metaCspAppCompact();
      return metaCspLegal();
  }
}

module.exports = {
  WAX_CONNECT_HTTPS,
  WAX_CONNECT_WSS,
  APP_PAGES,
  LEGAL_PAGES,
  helmetDirectives,
  metaCspForPage,
  metaCspApp,
  metaCspLegal,
  connectSrcApp,
};
