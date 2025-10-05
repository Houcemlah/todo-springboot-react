import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Login from "./components/Login";
import TodoList from "./components/TodoList";
import Register from "./components/Register";
import AdminTodos from "./components/AdminTodos";

function App() {
  return (
    <Router>
      <Navbar />
      <div className="container" style={{ marginTop: "20px" }}>
        <Routes>
          <Route path="/" element={<h2>Welcome to Todo App</h2>} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/todos" element={<TodoList />} />
          <Route path="/admin/todos" element={<AdminTodos />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
