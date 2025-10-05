import React, { useContext, useState } from "react";
import api from "../api/axiosConfig";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";
import TodoListLogo from "../assets/TodoListLogo.png"; 

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { login } = useContext(AuthContext);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/auth/login", { email, password });
      const token = res.data.token;

      const payload = JSON.parse(atob(token.split(".")[1]));
      const rawRoles = payload.roles || [];

      const roles = rawRoles.map(r => (typeof r === "string" ? r : r?.name)).filter(Boolean);

      login({ token, roles, email: payload.sub });

      if (roles.includes("ADMIN")) {
        navigate("/admin/todos");
      } else {
        navigate("/todos");
      }
    } catch (error) {
      console.error("Login error:", error);
      setError("Invalid credentials");
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
      <div className="card shadow-sm p-4" style={{ width: "350px", borderRadius: "10px" }}>
        
        <div className="text-center mb-3">
          <img src={TodoListLogo} alt="Logo" style={{ width: "150px", height: "auto" }} />
        </div>

        <h2 className="text-center mb-4" style={{ color: "#49b4d5" }}>Login</h2>
        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <input
              type="email"
              className="form-control"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="mb-3">
            <input
              type="password"
              className="form-control"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary w-100">
            Login
          </button>
        </form>
        {error && <p className="text-danger text-center mt-3">{error}</p>}
      </div>
    </div>
  );
};

export default Login;
