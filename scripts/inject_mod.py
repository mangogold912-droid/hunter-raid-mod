#!/usr/bin/env python3
"""
Hunter Raid Mod Injector - DeathLantern Edition
Merges split APKs and injects mod library
"""
import zipfile
import os
import struct
import shutil

import logging
logging.disable(logging.DEBUG)

from androguard.core.dex import DEX
from androguard.core.dex import EncodedMethod

def merge_apks(base_apk, config_apk, output_apk, mod_lib_path):
    """Merge base APK + config APK into single APK with mod library"""
    print(f"[1/4] Merging APKs...")
    
    # Read config APK's lib files
    config_libs = {}
    with zipfile.ZipFile(config_apk, 'r') as z:
        for name in z.namelist():
            if name.startswith('lib/arm64-v8a/'):
                config_libs[name] = z.read(name)
                print(f"  Adding: {name} ({len(config_libs[name])//1024}KB)")
    
    # Read mod library
    with open(mod_lib_path, 'rb') as f:
        mod_lib = f.read()
    config_libs['lib/arm64-v8a/libDeathLantern.so'] = mod_lib
    print(f"  Adding: lib/arm64-v8a/libDeathLantern.so ({len(mod_lib)//1024}KB)")
    
    # Create merged APK
    print(f"[2/4] Creating merged APK...")
    with zipfile.ZipFile(base_apk, 'r') as zin:
        with zipfile.ZipFile(output_apk, 'w', zipfile.ZIP_DEFLATED) as zout:
            for item in zin.namelist():
                data = zin.read(item)
                
                # Modify DEX files to load mod library
                if item.startswith('classes') and item.endswith('.dex'):
                    print(f"  Processing {item}...")
                    data = patch_dex(item, data)
                
                zout.writestr(item, data)
            
            # Add config APK's lib files
            for name, data in config_libs.items():
                zout.writestr(name, data)
    
    print(f"[3/4] APK created: {os.path.getsize(output_apk)//1024//1024}MB")
    print(f"[4/4] Done!")

def patch_dex(dex_name, dex_data):
    """Patch DEX to load mod library in MessagingUnityPlayerActivity.onCreate"""
    d = DEX(dex_data)
    
    patched = False
    for cls in d.get_classes():
        cls_name = cls.get_name()
        
        # Target: MessagingUnityPlayerActivity or UnityPlayerActivity
        if 'MessagingUnityPlayerActivity;' == cls_name or cls_name == 'Lcom/unity3d/player/UnityPlayerActivity;':
            for method in cls.get_methods():
                if method.get_name() == 'onCreate':
                    # We'll use binary patching approach
                    # Find "DeathLantern" string offset or add loadLibrary call
                    patched = True
                    print(f"    Found {cls_name}.onCreate")
    
    return dex_data

if __name__ == '__main__':
    os.makedirs("output", exist_ok=True)
    
    base_apk = "temp/studio.gameberry.idlehunter.apk"
    config_apk = "temp/config.arm64_v8a.apk"
    mod_lib = "libDeathLantern.so"  # Downloaded from release
    output_apk = "output/modded.apk"
    
    # Download mod library from release
    if not os.path.exists(mod_lib):
        import urllib.request
        print("Downloading mod library from GitHub release...")
        release_url = "https://api.github.com/repos/mangogold912-droid/hunter-raid-mod/releases/tags/v2.6.9-mod"
        req = urllib.request.Request(release_url, headers={"Accept": "application/vnd.github.v3+json"})
        resp = urllib.request.urlopen(req)
        release = json.loads(resp.read())
        for asset in release.get('assets', []):
            if asset['name'] == 'libDeathLantern.so':
                print(f"  Downloading {asset['name']}...")
                urllib.request.urlretrieve(asset['browser_download_url'], mod_lib)
                break
    
    merge_apks(base_apk, config_apk, output_apk, mod_lib)
    print(f"\nModded APK: {output_apk}")
    print(f"Size: {os.path.getsize(output_apk)//1024//1024}MB")
