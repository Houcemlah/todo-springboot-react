import React, { useState, useEffect } from "react";
import api from "../api/axiosConfig";

const AdminTodos = () => {
  const [todos, setTodos] = useState([]);
  const [showTaskForm, setShowTaskForm] = useState(false);
  const [showAdminForm, setShowAdminForm] = useState(false);

  const [newTask, setNewTask] = useState({
    title: "",
    description: "",
    assignedUserId: "",
    status: "TO_DO",
  });
  const [newAdmin, setNewAdmin] = useState({ name: "", email: "", password: "" });
  const [users, setUsers] = useState([]);

  const rawRoles = JSON.parse(localStorage.getItem("roles")) || [];
  const roles = rawRoles
    .map((r) => (typeof r === "string" ? r : r?.name))
    .filter(Boolean)
    .map((r) => r.toUpperCase());
  const isAdmin = roles.includes("ADMIN");

  useEffect(() => {
    const fetchTodos = async () => {
      try {
        const res = await api.get("/todos");
        setTodos(res.data);
      } catch (err) {
        console.error("Error fetching todos:", err);
      }
    };

    const fetchUsers = async () => {
      try {
        const res = await api.get("/users");
        setUsers(res.data);
      } catch (err) {
        console.error("Error fetching users:", err);
      }
    };

    fetchTodos();
    fetchUsers();
  }, []);

  const addTask = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/todos", newTask);
      setTodos([...todos, res.data]);
      setNewTask({ title: "", description: "", assignedUserId: "", status: "TO_DO" });
      setShowTaskForm(false);
    } catch (err) {
      console.error("Error adding task:", err.response?.data || err);
      alert("Failed to add task. Check your data.");
    }
  };

  const addAdmin = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token");
      await api.post("/users/admin", newAdmin, {
        headers: { Authorization: `Bearer ${token}` },
      });
      alert("✅ Admin added successfully!");
      setNewAdmin({ name: "", email: "", password: "" });
      setShowAdminForm(false);
    } catch (err) {
      console.error("Error adding admin:", err.response?.data || err);
      alert("❌ Failed to add admin.");
    }
  };

  const updateStatus = async (id, status) => {
    try {
      const res = await api.put(`/todos/status?id=${id}&status=${status}`);
      const updatedTodo = res.data;
      setTodos((prev) =>
        prev.map((t) => (t.id === updatedTodo.id ? updatedTodo : t))
      );
    } catch (err) {
      console.error("Failed to update status", err.response?.data || err);
      alert("Failed to update status");
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4 text-primary">Admin Panel</h2>

      {isAdmin && (
        <div className="mb-3">
          <button className="btn btn-success me-2" onClick={() => setShowTaskForm(!showTaskForm)}>
            {showTaskForm ? "Cancel" : "➕ Add Task"}
          </button>
          <button className="btn btn-warning" onClick={() => setShowAdminForm(!showAdminForm)}>
            {showAdminForm ? "Cancel" : "➕ Add Admin"}
          </button>
        </div>
      )}

      {/* Add Task Form */}
      {isAdmin && showTaskForm && (
        <form className="card card-body mb-3 shadow-sm" onSubmit={addTask}>
          <div className="mb-3">
            <label className="form-label">Task Title</label>
            <input
              type="text"
              className="form-control"
              placeholder="Enter task title"
              value={newTask.title}
              onChange={(e) => setNewTask({ ...newTask, title: e.target.value })}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Task Description</label>
            <input
              type="text"
              className="form-control"
              placeholder="Enter task description"
              value={newTask.description}
              onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Assign to User</label>
            <select
              className="form-select"
              value={newTask.assignedUserId}
              onChange={(e) => setNewTask({ ...newTask, assignedUserId: e.target.value })}
              required
            >
              <option value="">-- Select a user --</option>
              {users.map((u) => (
                <option key={u.id} value={u.id}>
                  {u.name} ({u.email})
                </option>
              ))}
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Status</label>
            <select
              className="form-select"
              value={newTask.status}
              onChange={(e) => setNewTask({ ...newTask, status: e.target.value })}
            >
              <option value="TO_DO">To Do</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="DONE">Done</option>
            </select>
          </div>

          <button type="submit" className="btn btn-primary">
            💾 Save Task
          </button>
        </form>
      )}

      {/* Add Admin Form */}
      {isAdmin && showAdminForm && (
        <form className="card card-body mb-3 shadow-sm" onSubmit={addAdmin}>
          <div className="mb-3">
            <label className="form-label">Name</label>
            <input
              type="text"
              className="form-control"
              placeholder="Enter name"
              value={newAdmin.name}
              onChange={(e) => setNewAdmin({ ...newAdmin, name: e.target.value })}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Email</label>
            <input
              type="email"
              className="form-control"
              placeholder="Enter email"
              value={newAdmin.email}
              onChange={(e) => setNewAdmin({ ...newAdmin, email: e.target.value })}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Password</label>
            <input
              type="password"
              className="form-control"
              placeholder="Enter password"
              value={newAdmin.password}
              onChange={(e) => setNewAdmin({ ...newAdmin, password: e.target.value })}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary">
            💾 Save Admin
          </button>
        </form>
      )}

      {/* Todo List */}
      <ul className="list-group mt-4">
        {todos.length === 0 ? (
          <li className="list-group-item text-muted">No todos yet</li>
        ) : (
          todos.map((t) => (
            <li
              key={t.id}
              className="list-group-item d-flex justify-content-between align-items-center"
            >
              <span>
                <b>{t.title}</b> – {t.description || "No description"}
              </span>
              <select
                className="form-select w-auto"
                value={t.status}
                onChange={(e) => updateStatus(t.id, e.target.value)}
              >
                <option value="TO_DO">To Do</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="DONE">Done</option>
              </select>
            </li>
          ))
        )}
      </ul>
    </div>
  );
};

export default AdminTodos;
