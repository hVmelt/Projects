import random
import string

length = int(input("Enter the desired password length: "))

include_upper = input("Include upper case letters?(y/n): ").lower() == 'y'
include_lower = input("Include lower case letters?(y/n): ").lower() == 'y'
include_digits = input("Include upper digits?(y/n): ").lower() == 'y'
include_symbols = input("Include upper symbols?(y/n): ").lower() == 'y'

characters = ""

if include_upper:
    characters += string.ascii_uppercase
if include_lower:
    characters += string.ascii_lowercase
if include_digits:
    characters += string.digits
if include_symbols:
    characters += string.punctuation

if not characters:
    print("No character type selected! Defaulting to all character types.")
    characters = string.ascii_letters + string.digits + string.punctuation

password = ''.join(random.choice(characters) for _ in range(length))
print("\nGenerated Password: ", password)
