# 🎯 Number Guessing Game (Python)

Welcome to the **Number Guessing Game** — a fun and beginner-friendly console game built in Python!  
Try to guess the number I'm thinking of (between 1 and 100) in 7 attempts or less.

---

## 🧠 How It Works

- The program randomly picks a number from 1 to 100.
- You get **7 tries** to guess it correctly.
- After each guess, you'll get feedback:
  - "Too low" if your guess is less than the secret number.
  - "Too high" if your guess is greater.
  - "Correct!" if you guessed it right 🎉
- After each round, you're asked if you want to play again.
- All game results are **logged to a file** called `game_score.txt`.

---

## 🗃 Example Log (`game_score.txt`)

## 🛠 How to Run

Make sure you have Python installed.  
Then run the script from your terminal or IDE:

```bash
python NumGuess.py
