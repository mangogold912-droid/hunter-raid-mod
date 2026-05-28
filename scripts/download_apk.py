#!/usr/bin/env python3
"""Download latest Hunter Raid APK"""
import urllib.request
import json
import zipfile
import os

os.makedirs("output", exist_ok=True)
os.makedirs("temp", exist_ok=True)

# Download XAPK from APKPure
print("Downloading Hunter Raid v2.7.1 XAPK...")
urllib.request.urlretrieve(
    "https://d.apkpure.net/b/APK/studio.gameberry.idlehunter?version=latest",
    "temp/hunter.xapk"
)

# Extract APKs from XAPK
print("Extracting APKs from XAPK...")
with zipfile.ZipFile("temp/hunter.xapk", 'r') as z:
    z.extract("studio.gameberry.idlehunter.apk", "temp/")
    z.extract("config.arm64_v8a.apk", "temp/")
    z.extract("manifest.json", "temp/")

print("Download complete!")
print(f"Base APK: {os.path.getsize('temp/studio.gameberry.idlehunter.apk')//1024//1024}MB")
print(f"Config APK: {os.path.getsize('temp/config.arm64_v8a.apk')//1024//1024}MB")
