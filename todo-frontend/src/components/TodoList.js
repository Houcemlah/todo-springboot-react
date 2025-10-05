import React, { useState, useEffect } from "react";
import api from "../api/axiosConfig";

const TodoList = () => {
  const [todos, setTodos] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [users, setUsers] = useState([]);
  const [newTodo, setNewTodo] = useState({
    title: "",
    description: "",
    assignedUserId: "",
    status: "TO_DO",
  });

  const rawRoles = JSON.parse(localStorage.getItem("roles")) || [];
  const roles = rawRoles.map((r) => (typeof r === "string" ? r : r.name));
  const isAdmin = roles.includes("ADMIN");

  useEffect(() => {
    const fetchTodos = async () => {
      try {
        const res = await api.get("/todos");
        setTodos(res.data);
      } catch (err) {
        console.error("Failed to fetch todos", err);
      }
    };
    fetchTodos();
  }, []);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await api.get("/users");
        setUsers(res.data);
      } catch (err) {
        console.error("Failed to load users", err);
      }
    };
    fetchUsers();
  }, []);

  const updateStatus = async (id, status) => {
    try {
      const res = await api.put(`/todos/status?id=${id}&status=${status}`);
      const updatedTodo = res.data;
      setTodos((prev) =>
        prev.map((t) => (t.id === updatedTodo.id ? updatedTodo : t))
      );
    } catch (err) {
      console.error("Failed to update status", err);
      alert("Failed to update status");
    }
  };

  const handleAddTodo = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/todos", newTodo);
      setTodos([...todos, res.data]);
      setShowForm(false);
      setNewTodo({ title: "", description: "", assignedUserId: "", status: "TO_DO" });
    } catch (err) {
      console.error("Failed to add Todo", err);
      alert("Failed to add todo. Only admins can add tasks.");
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4 text-primary">My Todos</h2>

      {isAdmin && (
        <>
          <button
            className="btn btn-success mb-3"
            onClick={() => setShowForm(!showForm)}
          >
            {showForm ? "Cancel" : "➕ Add Task"}
          </button>

          {showForm && (
            <form className="card card-body mb-3 shadow-sm" onSubmit={handleAddTodo}>
              <div className="mb-3">
                <label className="form-label">Title</label>
                <input
                  type="text"
                  className="form-control"
                  placeholder="Enter task title"
                  value={newTodo.title}
                  onChange={(e) => setNewTodo({ ...newTodo, title: e.target.value })}
                  required
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Description</label>
                <input
                  type="text"
                  className="form-control"
                  placeholder="Enter description"
                  value={newTodo.description}
                  onChange={(e) => setNewTodo({ ...newTodo, description: e.target.value })}
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Assign to User</label>
                <select
                  className="form-select"
                  value={newTodo.assignedUserId}
                  onChange={(e) => setNewTodo({ ...newTodo, assignedUserId: e.target.value })}
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
                  value={newTodo.status}
                  onChange={(e) => setNewTodo({ ...newTodo, status: e.target.value })}
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
        </>
      )}

      <ul className="list-group">
        {todos.length === 0 ? (
          <li className="list-group-item text-muted">No todos yet</li>
        ) : (
          todos.map((t) => (
            <li key={t.id} className="list-group-item d-flex justify-content-between align-items-center">
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

export default TodoList;
