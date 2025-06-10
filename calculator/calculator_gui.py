import tkinter as tk
from os.path import expanduser


def click(symbol):
    current = entry.get()
    entry.delete(0, tk.END)
    entry.insert(0, current + symbol)

def clear():
    entry.delete(0, tk.END)

def evaluate():
    try:
        result = eval(entry.get())
        entry.delete(0, tk.END)
        entry.insert(0, str(result))
    except:
        entry.delete(0, tk.END)
        entry.insert(0, "Error")

root = tk.Tk()
root.title("Python Calculator")
root.geometry("300x400")

entry = tk.Entry(root, width=16, font=("Arial", 24), borderwidth=2, relief="solid", justify="right")
entry.pack(pady=10)

buttons = [
    ('7', '8', '9', '/'),
    ('4', '5', '6', '*'),
    ('1', '2', '3', '-'),
    ('0', '.', '=', '+')
]

for row in buttons:
    frame = tk.Frame(root)
    frame.pack(expand = True, fill = "both")
    for symbol in row:
        action = evaluate if symbol == '=' else lambda s = symbol: click(s)
        btn = tk.Button(frame, text = symbol, font = ('Arial', 18), command=action
                        if symbol != '='
                        else evaluate)
        btn.pack(side="left", expand=True, fill="both", padx=2, pady=2)

clear_btn = tk.Button(root, text="Clear", font=("Arial", 18), command=clear)
clear_btn.pack(fill="both", padx=10, pady=10)

root.mainloop()