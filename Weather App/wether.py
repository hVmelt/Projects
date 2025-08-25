import tkinter as tk
from http.client import responses

import requests

API_KEY = "597ea77642119ab5aaf38da30031f321"
BASE_URL = "https://api.openweathermap.org/data/2.5/weather"

def get_weather():
    city = city_entry.get()
    if city.strip() == "":
        result_label.config(text = "Please enter a city name.")
        return

    params = {
        'q': city,
        'appid': API_KEY,
        'units': 'metric'
    }

    try:
        response = requests.get(BASE_URL, params=params)
        data = response.json()

        if data.get("cod") != 200:
            result_label.config(text=data.get("message", "Error fetching weather."))
            return

        city_name = data["name"]
        temp = data["main"]["temp"]
        description = data["weather"][0]["description"].capitalize()
        humidity = data["main"]["humidity"]

        result_label.config(
            text = f"Weather in {city_name}:\n"
                   f"Temperature: {temp}C\n"
                   f"Condition: {description}\n"
                   f"Humidity: {humidity}%"
        )

    except Exception as e:
        result_label.config(text="Error: Unable to connect to weather service.")

root = tk.Tk()
root.title("Simple Weather App")
root.geometry("300x250")
root.config(bg="#222")

title_label = tk.Label(root, text="Weather App", font=("Arial", 16, "bold"), fg="white", bg="#222")
title_label.pack(pady=10)

city_entry = tk.Entry(root, font=("Arial", 12))
city_entry.pack(pady=5)

get_button = tk.Button(root, text="Get Weather", command=get_weather, font=("Arial", 12), bg="#4CAF50", fg="gray")
get_button.pack(pady=10)

result_label = tk.Label(root, text="", font=("Arial", 12), fg="gray", bg="#222", justify="left")
result_label.pack(pady=10)

root.mainloop()
