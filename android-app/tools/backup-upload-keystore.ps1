# Back up Play upload keystore + keystore.properties to Desktop\NFTFlicks-offline-backup
# Usage:  powershell -File tools\backup-upload-keystore.ps1
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$jks = Join-Path $root "nftflicks-upload.jks"
$props = Join-Path $root "keystore.properties"

if (-not (Test-Path $jks)) {
  throw "Missing $jks - run create-upload-keystore.ps1 first."
}
if (-not (Test-Path $props)) {
  throw "Missing $props"
}

$backupDir = Join-Path ([Environment]::GetFolderPath("Desktop")) "NFTFlicks-offline-backup"
New-Item -ItemType Directory -Force -Path $backupDir | Out-Null

Copy-Item -Force $jks (Join-Path $backupDir "nftflicks-upload.jks")
Copy-Item -Force $props (Join-Path $backupDir "keystore.properties")

$readme = Join-Path $backupDir "README.txt"
@(
  "NFT Flicks offline backup"
  "========================="
  "nftflicks-upload.jks + keystore.properties = Google Play UPLOAD key."
  "If you lose these, you cannot ship updates with the same upload key without Play recovery."
  ""
  "Also keep a copy on a USB drive or password manager vault."
  "Never commit these files to git or upload them to chat."
  ""
  "Play upload: use Desktop\NFTFlicks.aab (or android-app\app\build\outputs\bundle\release\app-release.aab)"
  "Enroll Play App Signing on first AAB upload (see android-app\docs\PLAY-APP-SIGNING.md)."
) | Set-Content -Path $readme -Encoding UTF8

Write-Host "Backed up to $backupDir"
Get-ChildItem $backupDir | Format-Table Name, Length, LastWriteTime
