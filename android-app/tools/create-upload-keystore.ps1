# Creates a Play upload keystore + keystore.properties (gitignored).
# Run once from android-app:  powershell -File tools\create-upload-keystore.ps1
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
Set-Location $root

$jks = Join-Path $root "nftflicks-upload.jks"
$props = Join-Path $root "keystore.properties"

if (Test-Path $jks) {
  Write-Host "Keystore already exists: $jks"
  Write-Host "Not overwriting. Delete it manually only if you intend to rotate keys."
  exit 0
}

$pass = -join ((48..57) + (65..90) + (97..122) | Get-Random -Count 28 | ForEach-Object { [char]$_ })
$alias = "nftflicks"

Write-Host "Generating upload keystore (back up the password - Play needs this forever)..."
keytool -genkeypair -v `
  -keystore $jks `
  -storetype JKS `
  -keyalg RSA `
  -keysize 2048 `
  -validity 10000 `
  -alias $alias `
  -storepass $pass `
  -keypass $pass `
  -dname "CN=NFT Flicks, OU=Mobile, O=Savage Arts Pictures, L=Vancouver, ST=BC, C=CA"

@(
  "storeFile=nftflicks-upload.jks"
  "storePassword=$pass"
  "keyAlias=$alias"
  "keyPassword=$pass"
) | Set-Content -Path $props -Encoding ASCII

Write-Host ""
Write-Host "Created:"
Write-Host "  $jks"
Write-Host "  $props"
Write-Host ""
Write-Host "BACK UP both files offline. If you lose them you cannot update the Play listing with the same upload key."
Write-Host "Build Play bundle:  gradlew.bat bundleRelease"
