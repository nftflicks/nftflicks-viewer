"use strict";
const { contextBridge, ipcRenderer } = require("electron");

contextBridge.exposeInMainWorld("NftFlicksShell", {
  getStatus: () => ipcRenderer.invoke("shell:get-status"),
  reconnect: () => ipcRenderer.invoke("shell:reconnect"),
  onStatus: (cb) => {
    const handler = (_event, payload) => cb(payload);
    ipcRenderer.on("shell:status", handler);
    return () => ipcRenderer.removeListener("shell:status", handler);
  },
});
