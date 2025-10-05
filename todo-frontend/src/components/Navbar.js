import React, { useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";
import logo from "../assets/TodoListLogo.png"; // <-- import your logo

const Navbar = () => {
    const { user, logout } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    const navStyle = {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "10px 20px",
        backgroundColor: "#343a40",
        color: "#fff",
        boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
    };

    const linkStyle = {
        color: "#fff",
        textDecoration: "none",
        marginRight: "15px",
        fontWeight: "500",
        transition: "color 0.2s",
    };

    const rightSectionStyle = {
        display: "flex",
        alignItems: "center",
    };

    const buttonStyle = {
        backgroundColor: "#ffc107",
        border: "none",
        padding: "6px 12px",
        borderRadius: "4px",
        cursor: "pointer",
        fontWeight: "500",
    };

    return (
        <nav style={navStyle}>
            {/* Logo */}
            <div style={{ display: "flex", alignItems: "center" }}>
                <img src={logo} alt="Logo" style={{ height: "40px", marginRight: "15px" }} />
                {user && user.roles?.includes("USER") && (
                    <Link to="/todos" style={linkStyle}>Todos</Link>
                )}
                {user && user.roles?.includes("ADMIN") && (
                    <Link to="/admin/todos" style={linkStyle}>Todos</Link>
                )}
            </div>

            <div style={rightSectionStyle}>
                {!user ? (
                    <>
                        <Link to="/login" style={linkStyle}>Login</Link>
                        <Link to="/register" style={linkStyle}>Register</Link>
                    </>
                ) : (
                    <button onClick={handleLogout} style={buttonStyle}>Logout</button>
                )}
            </div>
        </nav>
    );
};

export default Navbar;
