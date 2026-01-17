# Rune Blade – Open World RPG

![Rune Blade Banner](https://github.com/Enzosakollari/RPG-Game/blob/main/runeblade/banner.png?raw=true)

## 💬 Overview

**Rune Blade** is a **2D open-world RPG game** developed in **pure Java**, where players explore a vast map, fight enemies, solve labyrinths, interact with NPCs, and progressively upgrade their weapons and abilities.

The game features a **persistent progression system** using **SQLite**, allowing **multiple player accounts** and save slots. Players can collect loot, craft or buy different potions, defeat increasingly difficult enemies, and uncover hidden areas across the world.

The game was packaged as a **native Windows `.exe`** using **`jpackage`** and is available to play on **itch.io**.

---

## 🛠️ Technologies Used

<div style="display:flex; gap:10px; flex-wrap:wrap;">
  <img src="https://img.shields.io/badge/Swing-GUI-4E8B3A?style=flat-square&logo=java&logoColor=white" width="100px"/>
  <img src="https://img.shields.io/badge/AWT-Graphics-707070?style=flat-square&logo=java&logoColor=white" width="100px"/>
  <img src="https://img.shields.io/badge/SQLite-003B57?style=flat-square&logo=sqlite&logoColor=white" width="100px"/>
  <img src="https://img.shields.io/badge/jpackage-EXE-FF6F00?style=flat-square&logo=java&logoColor=white" width="100px"/>
  <img src="https://img.shields.io/badge/Pixel_Art-RPG-8E44AD?style=flat-square" width="100px"/>
</div>

---

## ⚔️ Gameplay Features

- **Open World Map** – Freely explore different regions with enemies, NPCs, and hidden areas  
- **Combat System** – Fight monsters using weapons, abilities, and strategy  
- **Labyrinths & Challenges** – Solve maze-like dungeons to unlock rewards  
- **Weapon Progression** – Obtain and upgrade weapons as you progress  
- **Potions & Items** – Health, power, and special effect potions  
- **NPC Interaction** – Talk to NPCs for quests, lore, and progression  
- **Multiple Accounts** – Create and manage multiple player profiles  
- **Persistent Saves** – Progress is stored using **SQLite**

---

## 📷 Screenshots

### 🏰 Main Menu / Banner
![Banner](https://github.com/Enzosakollari/RPG-Game/blob/main/runeblade/banner.png?raw=true)

### 🗺️ World Exploration
![Explore](https://github.com/Enzosakollari/RPG-Game/blob/main/runeblade/explore.png?raw=true)

### ⚔️ Combat System
![Attack](https://github.com/Enzosakollari/RPG-Game/blob/main/runeblade/attack.png?raw=true)

### 🧙 NPC Interaction
![NPC](https://github.com/Enzosakollari/RPG-Game/blob/main/runeblade/npc.png?raw=true)

### 🔐 Login & Accounts
![Login](https://github.com/Enzosakollari/RPG-Game/blob/main/runeblade/login.png?raw=true)

### ☠️ Game Over Screen
![Game Over](https://github.com/Enzosakollari/RPG-Game/blob/main/runeblade/gameover.png?raw=true)

---

## 💾 Save System

Rune Blade uses **SQLite** to store:
- Player accounts
- Character stats
- Inventory & potions
- Game progress

This allows players to safely close the game and continue exactly where they left off.

---

## 📦 Distribution

- The game is packaged as a **native Windows `.exe`** using **`jpackage`**
- No JVM installation required for players
- Available on **itch.io** for easy download and play

---

## 🔧 Build & Run (Developer)

### Requirements
- **JDK 17+**
- Java installed and configured in PATH

### Run locally
```bash
javac Main.java
java Main
