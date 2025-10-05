# 📝 Todo List - Spring Boot & React

A fullstack **Todo List application** built with **Spring Boot (backend)** and **React (frontend)**.  
This project demonstrates **authentication, role-based access (USER/ADMIN)**, and CRUD operations with a clean UI.

---

## 🚀 Features

- 🔐 **Authentication & Authorization**
  - Login & Register users
  - Role management: `USER` and `ADMIN`
- ✅ **Todos Management**
  - Add, update, delete todos
  - Mark todos as `TODO`, `IN_PROGRESS`, or `DONE`
- 👨‍💼 **Admin Panel**
  - Create new admins
  - Manage tasks and user roles
- 🎨 **Frontend (React)**
  - Responsive UI with Bootstrap
  - Navbar with login/logout/register buttons
  - Clean design with logo integration
- ⚙️ **Backend (Spring Boot)**
  - REST API with JWT authentication
  - MySQL database integration
  - Spring Data JPA for persistence

---

## 🏗️ Tech Stack

**Backend:**
- Java 17
- Spring Boot
- Spring Security (JWT)
- Hibernate / JPA
- MySQL

**Frontend:**
- React (CRA)
- Axios
- Bootstrap 5

---

## 📂 Project Structure
│
├── todo-backend/ # Spring Boot backend
│ ├── src/main/java # Java source files
│ ├── src/main/resources
│ └── pom.xml
│
├── todo-frontend/ # React frontend
│ ├── src/ # React components
│ ├── public/
│ └── package.json
│
└── README.md


## ⚡ Installation & Setup

### 1️⃣ Backend (Spring Boot)
```bash
cd todo-backend
# configure MySQL in src/main/resources/application.properties
mvn spring-boot:run

### 2️⃣ Frontend (React)
cd todo-frontend
npm install
npm start

The app will run at:

Backend API → http://localhost:8080
Frontend UI → http://localhost:3000

🔑 Login Credentials

Register as a new user at /register
Admins can add other admins from the Admin Panel

📸 Screenshots


