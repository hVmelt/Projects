import random
from operator import truediv
from datetime import datetime

def log_score(result, attempts, number):
    with open("game_score.txt", "a") as file:
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        file.write(f"{timestamp} | {result} | Attempts: {attempts} | Number: {number}\n")


def play_game():
    print("Welcome to the number guessing game!")
    print("I'm thinking of a number between 1 to 100")
    print("You have 7 tries to guess it correctly")

    secret_number = random.randint(1, 100)
    max_attempts = 7
    attempts = 0

    while attempts < max_attempts:
        try:
            guess = int(input(f"\nTry {attempts + 1}: Enter your guess: "))
            attempts += 1

            if guess < 1 or guess > 100:
                print("Guess must me between 1 to 100")
            elif guess < secret_number:
                print("Too low. Try again.")
            elif guess > secret_number:
                print("Too high. Try again.")
            else:
                print(f"Correct! The number was {secret_number}")
                print(f"You guessed in {attempts} attempts")
                log_score("Win", attempts, secret_number)
                break
        except ValueError:
            print("Please enter a valid number.")
    else:
        print(f"\nYou've used all {max_attempts} tries.")
        print(f"The correct number was {secret_number}.")
        log_score("Lose", attempts,secret_number)

def number_guessing_game():
    while True:
        play_game()
        again = input("\nPlay again? (y/n): ").lower()
        if again != "y":
            print("\nThanks for playing! See you next time.")
            break

if __name__ == "__main__":
    number_guessing_game()
