#!/usr/bin/env python3
"""Fallback: Direct merge without apktool"""
import zipfile, os

print("Using direct merge approach...")

BASE = "temp/studio.gameberry.idlehunter.apk"
CONFIG = "temp/config.arm64_v8a.apk"
MOD = "libDeathLantern.so"
OUT = "output/unsigned.apk"

os.makedirs("output", exist_ok=True)

# Read config libs
config_libs = {}
with zipfile.ZipFile(CONFIG, 'r') as z:
    for name in z.namelist():
        if name.startswith('lib/arm64-v8a/'):
            config_libs[name] = z.read(name)

with open(MOD, 'rb') as f:
    config_libs['lib/arm64-v8a/libDeathLantern.so'] = f.read()

# Create merged APK (DEX not patched, but lib loaded by existing code)
with zipfile.ZipFile(BASE, 'r') as zin:
    with zipfile.ZipFile(OUT, 'w', zipfile.ZIP_DEFLATED) as zout:
        for item in zin.namelist():
            zout.writestr(item, zin.read(item))
        for name, data in config_libs.items():
            zout.writestr(name, data)

print(f"Created: {OUT} ({os.path.getsize(OUT)//1024//1024}MB)")
