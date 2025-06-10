# Connect Four (JavaFX)

A fully functional Connect Four game built in JavaFX with user authentication. This was developed as the final project for our Computer Science II (CS265) course at Southeast Missouri State University.

## 👥 Authors
- Galib Yasar Kabir  
- Swopnil Dahal  
- Sarwat Saadaat  
- Chrissy  
- Nidah  
- With logic support from ChatGPT

---

## 🧩 Features

### 🎮 Gameplay
- Classic Connect Four rules: drop discs into a 7x6 grid
- Win detection (horizontal, vertical, and diagonal)
- Smooth animations for disc drops using `TranslateTransition`

### 👤 User Authentication
- Login and account creation with hashed passwords (SHA-256)
- Credentials stored securely in `accounts.txt`

### 🌈 GUI
- Built entirely with **JavaFX**
- Interactive main menu
- Visual feedback for mouse hover on columns
- Styled game board and components
- Game-over screen with winner display and options to restart or exit

---

## 🛠️ Technologies Used
- Java 17+
- JavaFX
- SHA-256 hashing (`MessageDigest`)
- Scene control with `Stage`, `Scene`, `VBox`, `GridPane`
- File I/O for account persistence

---

## 📁 File Structure

