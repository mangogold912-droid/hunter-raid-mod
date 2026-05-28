#!/usr/bin/env python3
"""
Hunter Raid Mod Injector v2 - DeathLantern
Merges split APKs, injects mod library, patches DEX
"""
import zipfile
import os
import sys
import shutil
import struct

print("=" * 60)
print("Hunter Raid Mod Injector - DeathLantern Edition")
print("=" * 60)

BASE_APK = "temp/studio.gameberry.idlehunter.apk"
CONFIG_APK = "temp/config.arm64_v8a.apk"
MOD_LIB = "libDeathLantern.so"
DECOMPILED = "temp/decompiled"
OUTPUT = "output/unsigned.apk"

# Step 1: Merge APKs
print("\n[1/5] Merging split APKs...")
config_libs = {}
with zipfile.ZipFile(CONFIG_APK, 'r') as z:
    for name in z.namelist():
        if name.startswith('lib/arm64-v8a/'):
            config_libs[name] = z.read(name)
            print(f"  + {name} ({len(config_libs[name])//1024}KB)")

# Add mod library
with open(MOD_LIB, 'rb') as f:
    mod_data = f.read()
config_libs['lib/arm64-v8a/libDeathLantern.so'] = mod_data
print(f"  + lib/arm64-v8a/libDeathLantern.so ({len(mod_data)//1024}KB) [MOD]")

# Create merged APK
merged_apk = "temp/merged.apk"
with zipfile.ZipFile(BASE_APK, 'r') as zin:
    with zipfile.ZipFile(merged_apk, 'w', zipfile.ZIP_DEFLATED) as zout:
        for item in zin.namelist():
            zout.writestr(item, zin.read(item))
        for name, data in config_libs.items():
            zout.writestr(name, data)

print(f"  Merged APK: {os.path.getsize(merged_apk)//1024//1024}MB")

# Step 2: Decompile with apktool
print("\n[2/5] Decompiling with apktool...")
os.system(f"java -jar /usr/local/bin/apktool.jar d {merged_apk} -o {DECOMPILED} -f")

if not os.path.exists(DECOMPILED):
    print("  ERROR: Decompilation failed!")
    print("  Falling back to direct merge approach...")
    shutil.copy(merged_apk, OUTPUT)
    sys.exit(0)

# Step 3: Create mod loader smali
print("\n[3/5] Creating mod loader smali...")

# Find smali directories
smali_dirs = sorted([d for d in os.listdir(DECOMPILED) if d.startswith('smali')])
print(f"  Found smali dirs: {smali_dirs}")

# Create ModLoader class in the first smali directory
mod_dir = os.path.join(DECOMPILED, smali_dirs[-1], 'com', 'deathlantern')
os.makedirs(mod_dir, exist_ok=True)

mod_smali = """.class public Lcom/deathlantern/ModLoader;
.super Ljava/lang/Object;

.method public constructor <init>()V
    .registers 1
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V
    return-void
.end method

.method public static loadMod()V
    .registers 1
    const-string v0, "DeathLantern"
    invoke-static {v0}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V
    return-void
.end method
"""

with open(os.path.join(mod_dir, 'ModLoader.smali'), 'w') as f:
    f.write(mod_smali)
print(f"  Created: {os.path.join(mod_dir, 'ModLoader.smali')}")

# Step 4: Patch Activity classes to load mod
print("\n[4/5] Patching Activity classes...")

# Find and patch MessagingUnityPlayerActivity and UnityPlayerActivity
targets = [
    ('com/google/firebase/MessagingUnityPlayerActivity.smali', 'MessagingUnityPlayerActivity'),
    ('com/unity3d/player/UnityPlayerActivity.smali', 'UnityPlayerActivity'),
]

for smali_dir_name in smali_dirs:
    smali_dir = os.path.join(DECOMPILED, smali_dir_name)
    for rel_path, class_name in targets:
        smali_path = os.path.join(smali_dir, rel_path)
        if os.path.exists(smali_path):
            with open(smali_path, 'r') as f:
                content = f.read()
            
            # Find onCreate method and inject loadMod call
            if 'onCreate' in content and 'loadMod' not in content:
                # Replace .locals in onCreate with .locals + 1 and add loadMod call
                old_oncreate = '.method public onCreate(Landroid/os/Bundle;)V\n    .locals'
                new_oncreate = '.method public onCreate(Landroid/os/Bundle;)V\n    .locals 2\n\n    invoke-static {}, Lcom/deathlantern/ModLoader;->loadMod()V\n'
                
                if old_oncreate in content:
                    content = content.replace(old_oncreate, new_oncreate, 1)
                    with open(smali_path, 'w') as f:
                        f.write(content)
                    print(f"  Patched: {smali_dir_name}/{rel_path}")
                else:
                    # Try alternative pattern
                    lines = content.split('\n')
                    new_lines = []
                    in_oncreate = False
                    for line in lines:
                        new_lines.append(line)
                        if '.method public onCreate(Landroid/os/Bundle;)V' in line and not in_oncreate:
                            in_oncreate = True
                            new_lines.append('    .registers 2')
                            new_lines.append('')
                            new_lines.append('    invoke-static {}, Lcom/deathlantern/ModLoader;->loadMod()V')
                            # Skip the original .registers line if it follows
                            continue
                    content = '\n'.join(new_lines)
                    with open(smali_path, 'w') as f:
                        f.write(content)
                    print(f"  Patched (alt): {smali_dir_name}/{rel_path}")

# Step 5: Done
print("\n[5/5] Injection complete!")
print(f"\nDecompiled directory: {DECOMPILED}")
print("Ready for recompilation with apktool")
