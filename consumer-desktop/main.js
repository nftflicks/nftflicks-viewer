"use strict";

const { app, BrowserWindow, Menu, shell, ipcMain, net } = require("electron");
const path = require("path");
const shellHosts = require("../shared/electron-shell-hosts");

const LOCAL_ORIGIN = "http://127.0.0.1:8095";
const REMOTE_ORIGIN = "https://nftflicks.com";
const SITE_HOST = "nftflicks.com";

const SHELL_UA_SUFFIX = " NFTFlicksShell/1.0";

let mainWindow = null;
let currentMode = "offline"; // local | remote | offline

function siteUrl(origin, page = "index.html") {
  return `${origin.replace(/\/$/, "")}/${page}`;
}

function openExternalSafe(urlString) {
  shellHosts.openExternalSafe(urlString, shell);
}

function isAllowedNavigation(urlString) {
  return shellHosts.isAllowedNavigation(urlString);
}

function applyShellUserAgent(session) {
  shellHosts.applyShellUserAgent(session, SHELL_UA_SUFFIX);
}

function probe(url, timeoutMs = 2500) {
  return new Promise((resolve) => {
    const request = net.request({ method: "GET", url });
    let done = false;
    const finish = (ok) => {
      if (done) return;
      done = true;
      resolve(ok);
    };
    const timer = setTimeout(() => {
      try {
        request.abort();
      } catch (_) {
        /* ignore */
      }
      finish(false);
    }, timeoutMs);
    request.on("response", (response) => {
      clearTimeout(timer);
      finish(response.statusCode >= 200 && response.statusCode < 500);
      response.on("data", () => {});
      response.on("end", () => {});
    });
    request.on("error", () => {
      clearTimeout(timer);
      finish(false);
    });
    request.end();
  });
}

async function resolveTarget() {
  if (await probe(`${LOCAL_ORIGIN}/healthz`)) {
    return { mode: "local", url: siteUrl(LOCAL_ORIGIN) };
  }
  if (await probe(siteUrl(REMOTE_ORIGIN))) {
    return { mode: "remote", url: siteUrl(REMOTE_ORIGIN) };
  }
  return { mode: "offline", url: null };
}

function setStatus(mode) {
  currentMode = mode;
  const label =
    mode === "local" ? "Local" : mode === "remote" ? "Remote" : "Offline";
  if (mainWindow && !mainWindow.isDestroyed()) {
    mainWindow.setTitle(`NFT Flicks — ${label}`);
    mainWindow.webContents.send("shell:status", { mode, label });
  }
}

async function loadSite() {
  const target = await resolveTarget();
  setStatus(target.mode);
  if (!mainWindow || mainWindow.isDestroyed()) return target;
  if (target.url) {
    try {
      await mainWindow.webContents.session.clearCache();
    } catch (_) {
      /* ignore */
    }
    await mainWindow.loadURL(target.url, { extraHeaders: "Cache-Control: no-cache\n" });
  } else {
    await mainWindow.loadFile(path.join(__dirname, "offline.html"));
  }
  return target;
}

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1280,
    height: 860,
    minWidth: 960,
    minHeight: 640,
    title: "NFT Flicks",
    backgroundColor: "#0a0a0a",
    autoHideMenuBar: true,
    icon: path.join(__dirname, "assets", "icon.ico"),
    webPreferences: {
      preload: path.join(__dirname, "preload.js"),
      contextIsolation: true,
      nodeIntegration: false,
      sandbox: true,
      webSecurity: true,
      allowRunningInsecureContent: false,
      devTools: !app.isPackaged,
    },
  });

  if (app.isPackaged) {
    mainWindow.webContents.on("before-input-event", (event, input) => {
      const key = String(input.key || "").toLowerCase();
      if (input.type === "keyDown" && (key === "f12" || (input.control && input.shift && (key === "i" || key === "j")))) {
        event.preventDefault();
      }
    });
  }

  const template = [
    {
      label: "NFT Flicks",
      submenu: [
        {
          label: "Reconnect (prefer local)",
          accelerator: "CmdOrCtrl+R",
          click: () => loadSite(),
        },
        { type: "separator" },
        { role: "quit", label: "Quit" },
      ],
    },
    {
      label: "View",
      submenu: [
        { role: "reload" },
        ...(!app.isPackaged ? [{ role: "toggleDevTools" }] : []),
        { type: "separator" },
        { role: "resetZoom" },
        { role: "zoomIn" },
        { role: "zoomOut" },
        { type: "separator" },
        { role: "togglefullscreen" },
      ],
    },
  ];
  Menu.setApplicationMenu(Menu.buildFromTemplate(template));

  mainWindow.webContents.setWindowOpenHandler(({ url }) => {
    if (isAllowedNavigation(url)) {
      return { action: "allow" };
    }
    openExternalSafe(url);
    return { action: "deny" };
  });

  mainWindow.webContents.on("will-navigate", (event, url) => {
    if (!isAllowedNavigation(url) && !url.startsWith("file://")) {
      event.preventDefault();
      openExternalSafe(url);
    }
  });

  mainWindow.on("closed", () => {
    mainWindow = null;
  });

  applyShellUserAgent(mainWindow.webContents.session);
  loadSite();
}

ipcMain.handle("shell:get-status", () => ({
  mode: currentMode,
  label:
    currentMode === "local"
      ? "Local"
      : currentMode === "remote"
        ? "Remote"
        : "Offline",
}));

ipcMain.handle("shell:reconnect", async () => loadSite());

const gotLock = app.requestSingleInstanceLock();
if (!gotLock) {
  app.quit();
} else {
  app.on("second-instance", () => {
    if (mainWindow) {
      if (mainWindow.isMinimized()) mainWindow.restore();
      mainWindow.focus();
    }
  });

  app.whenReady().then(() => {
    createWindow();
    app.on("activate", () => {
      if (BrowserWindow.getAllWindows().length === 0) createWindow();
    });
  });

  app.on("window-all-closed", () => {
    if (process.platform !== "darwin") app.quit();
  });
}
