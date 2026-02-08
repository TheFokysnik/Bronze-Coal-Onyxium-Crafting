# Bronze & Onyxium Crafting

![Hytale Server Mod](https://img.shields.io/badge/Hytale-Server%20Mod-0ea5e9?style=for-the-badge)
![Version](https://img.shields.io/badge/version-2.3.0-10b981?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-21+-f97316?style=for-the-badge&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-a855f7?style=for-the-badge)

A comprehensive crafting expansion mod for Hytale that introduces a complete **Bronze tier**, makes **Onyxium** gear fully craftable, adds **Coal** as a superior fuel, and features **auto-igniting furnaces**!

## 🔥 Features

### New Resources
- **Tin Ore** — Common ore that spawns throughout caves (Y20-100)
- **Tin Bar** — Smelt Tin Ore in a furnace
- **Bronze Bar** — Alloy crafted from **2 Copper Bars + 1 Tin Bar = 3 Bronze Bars**
- **Coal Ore** — New fuel ore found throughout the world (Y20-100)
- **Coal** — Superior fuel item, **5x more efficient than Charcoal**

### 🔥 Auto-Ignition
Furnaces now start automatically when fuel and items are loaded — **no need to press "Turn On"!**

### Bronze Equipment (Mid-tier between Iron and Cobalt)

**Weapons:**
- Bronze Sword
- Bronze Daggers
- Bronze Shortbow
- Bronze Spear

**Armor Set:**
- Bronze Helmet
- Bronze Chestplate
- Bronze Gauntlets
- Bronze Greaves

**Tools:**
- Bronze Pickaxe
- Bronze Hatchet
- Bronze Shovel

### Onyxium — Now Craftable!

**Onyxium Ore** now generates naturally deep underground (Y5-45). Smelt it into bars and craft a complete epic-tier arsenal:

**Weapons:**
- Onyxium Sword
- Onyxium Longsword
- Onyxium Daggers
- Onyxium Axe
- Onyxium Battleaxe
- Onyxium Club
- Onyxium Mace
- Onyxium Shortbow

**Armor Set:**
- Onyxium Helmet
- Onyxium Chestplate
- Onyxium Gauntlets
- Onyxium Greaves

**Tools:**
- Onyxium Pickaxe
- Onyxium Hatchet

## ⚔️ Balance

| Tier | Durability | Power Level |
|------|------------|-------------|
| Iron | 250 | Base |
| **Bronze** | **275** | **+10%** |
| Cobalt | 300 | +20% |
| **Onyxium** | **450** | **Epic** |

Bronze fills the gap between Iron and Cobalt, giving players a smoother progression path.

## 🌍 Ore Generation

| Ore | Y-Level | Veins/Chunk | Blocks/Vein |
|-----|---------|-------------|-------------|
| Tin | 20-100 | 12 | 8 |
| Coal | 20-100 | 16 | 10 |
| Onyxium | 5-45 | 6 | 5 |

All ores generate in every rock type: **Stone, Basalt, Sandstone, Shale, Volcanic, Slate**

## 📦 Installation

1. Download the latest release from the [Releases](https://github.com/TheFokysnik/Bronze-Coal-Onyxium-Crafting/releases) page
2. Place the JAR file in your Hytale mods directory:
   - **macOS:** `~/Library/Application Support/Hytale/UserData/Mods/`
   - **Windows:** `%APPDATA%/Hytale/UserData/Mods/`
   - **Linux:** `~/.hytale/UserData/Mods/`
3. Launch Hytale and enjoy!

## 🛠️ Building from Source

### Prerequisites
- Java 21 or higher
- Gradle (included via wrapper)
- Hytale Server JAR (place in `libs/HytaleServer.jar`)

### Build Steps
```bash
git clone https://github.com/TheFokysnik/Bronze-Coal-Onyxium-Crafting.git
cd Bronze-Coal-Onyxium-Crafting
./gradlew build
```

The compiled mod will be automatically placed in your Hytale mods directory.

## 🔧 Crafting Requirements

- **Bronze gear:** Workbench / Weapon Bench / Armor Bench
- **Onyxium gear:** Tier 2 Workbench / Weapon Bench / Armor Bench

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.
