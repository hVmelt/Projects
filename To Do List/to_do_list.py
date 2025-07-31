import tkinter as tk
from tkinter import messagebox
import os

TODO_FILE = "todo_gui.txt"

def load_tasks():
    if not os.path.exists(TODO_FILE):
        return []
    with open(TODO_FILE, "r") as file:
        return [line.strip() for line in file.readlines()]

def save_tasks(tasks):
    with open(TODO_FILE, "w") as file:
        for task in tasks:
            file.write(task + "\n")

class TodoApp:
    def __init__(self, root):
        self.root = root
        self.root.title("📝 To-Do List")
        self.root.geometry("420x550")
        self.root.configure(bg="#2c3e50")

        self.tasks = load_tasks()

        # Title
        title = tk.Label(root, text="TO-DO LIST ✅", font=("Helvetica", 20, "bold"), bg="#2c3e50", fg="white")
        title.pack(pady=15)

        # Entry field
        self.task_entry = tk.Entry(root, width=28, font=("Segoe UI", 14), bd=2, relief="groove")
        self.task_entry.pack(pady=10)

        # Buttons
        btn_style = {"font": ("Segoe UI", 12), "width": 18, "padx": 5, "pady": 5, "bd": 0}

        tk.Button(root, text="➕ Add Task", command=self.add_task, bg="#27ae60", fg="black", **btn_style).pack(pady=5)
        tk.Button(root, text="✔️ Mark as Done", command=self.mark_done, bg="#2980b9", fg="black", **btn_style).pack(pady=5)
        tk.Button(root, text="🗑️ Delete Task", command=self.delete_task, bg="#c0392b", fg="black", **btn_style).pack(pady=5)

        # Task list
        frame = tk.Frame(root, bg="#ecf0f1", bd=2, relief="sunken")
        frame.pack(pady=15)

        self.task_listbox = tk.Listbox(
            frame, selectmode=tk.SINGLE, width=40, height=15,
            font=("Consolas", 12), bg="#ecf0f1", fg="#2c3e50", selectbackground="#d1d8e0"
        )
        self.task_listbox.pack(padx=10, pady=10)
        self.load_to_listbox()

        # Save on close
        self.root.protocol("WM_DELETE_WINDOW", self.on_closing)

    def load_to_listbox(self):
        self.task_listbox.delete(0, tk.END)
        for task in self.tasks:
            display_task = task
            if task.startswith("[✓]"):
                display_task = "✔️ " + task[3:]
            self.task_listbox.insert(tk.END, display_task)

    def add_task(self):
        task = self.task_entry.get().strip()
        if task:
            self.tasks.append("[ ] " + task)
            self.task_entry.delete(0, tk.END)
            self.load_to_listbox()
        else:
            messagebox.showwarning("Input Error", "Task cannot be empty!")

    def mark_done(self):
        selected = self.task_listbox.curselection()
        if selected:
            index = selected[0]
            if self.tasks[index].startswith("[✓]"):
                messagebox.showinfo("Already Done", "Task already marked as done.")
            else:
                self.tasks[index] = self.tasks[index].replace("[ ]", "[✓]", 1)
                self.load_to_listbox()
        else:
            messagebox.showwarning("No Selection", "Select a task to mark as done.")

    def delete_task(self):
        selected = self.task_listbox.curselection()
        if selected:
            index = selected[0]
            del self.tasks[index]
            self.load_to_listbox()
        else:
            messagebox.showwarning("No Selection", "Select a task to delete.")

    def on_closing(self):
        save_tasks(self.tasks)
        self.root.destroy()

if __name__ == "__main__":
    root = tk.Tk()
    app = TodoApp(root)
    root.mainloop()
