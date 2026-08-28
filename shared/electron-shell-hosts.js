"use strict";

/** Shared in-window navigation allowlist for NFT Flicks Electron shells. */
const ALLOWED_HOSTS = new Set([
  "127.0.0.1",
  "localhost",
  "nftflicks.com",
  "www.nftflicks.com",
  "accounts.google.com",
  "oauth2.googleapis.com",
  "checkout.stripe.com",
  "js.stripe.com",
  "billing.stripe.com",
  "pay.stripe.com",
  "www.youtube.com",
  "youtube.com",
  "www.youtube-nocookie.com",
  "youtube-nocookie.com",
  "all-access.wax.io",
  "wax.greymass.com",
  "wax.api.atomicassets.io",
  "api.atomicassets.io",
  "wax.bloks.io",
  "cdn.jsdelivr.net",
  "unpkg.com",
  "cdnjs.cloudflare.com",
  "challenges.cloudflare.com",
  "fonts.googleapis.com",
  "fonts.gstatic.com",
  "www.gstatic.com",
]);

const ALLOWED_HOST_SUFFIXES = [
  ".stripe.com",
  ".stripe.network",
  ".youtube.com",
  ".ytimg.com",
  ".googlevideo.com",
  ".wax.io",
  ".atomicassets.io",
  ".challenges.cloudflare.com",
  ".cloudflare.com",
  ".cloudflareinsights.com",
  ".google.com",
  ".googleapis.com",
  ".gstatic.com",
];

function hostAllowed(host) {
  if (ALLOWED_HOSTS.has(host)) return true;
  return ALLOWED_HOST_SUFFIXES.some((suffix) => host.endsWith(suffix));
}

function isAllowedNavigation(urlString) {
  try {
    const u = new URL(urlString);
    if (u.protocol !== "https:" && u.protocol !== "http:") return false;
    const host = u.hostname.toLowerCase();
    if (!hostAllowed(host)) return false;
    if (host === "127.0.0.1" || host === "localhost") {
      return u.port === "8095" || u.port === "";
    }
    return u.protocol === "https:";
  } catch {
    return false;
  }
}

function openExternalSafe(urlString, shell) {
  try {
    const u = new URL(urlString);
    if (u.protocol !== "https:") return;
    const host = u.hostname.toLowerCase();
    if (!hostAllowed(host)) return;
    shell.openExternal(u.toString()).catch(() => {});
  } catch {
    /* ignore */
  }
}

function applyShellUserAgent(session, suffix) {
  if (!session || !suffix) return;
  const base = session.getUserAgent();
  const token = suffix.trim();
  if (!token || base.includes(token.replace(/^\s+/, ""))) return;
  session.setUserAgent(base + (token.startsWith(" ") ? token : ` ${token}`));
}

module.exports = {
  ALLOWED_HOSTS,
  ALLOWED_HOST_SUFFIXES,
  hostAllowed,
  isAllowedNavigation,
  openExternalSafe,
  applyShellUserAgent,
};
