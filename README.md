# 💬 Real-Time Chat Application

A full-stack real-time chat application built using **Spring Boot**, **WebSocket (STOMP)**, **JWT Authentication**, and **React.js**.

This application supports private messaging, room-based chat, image sharing, message status tracking, and user presence detection.

---

## 🚀 Features

### 🔐 Authentication & Security
- JWT-based Authentication
- Secure REST APIs
- Role-based access
- Password encryption (BCrypt)
- Protected WebSocket connection
- CORS configured for frontend-backend integration

### 💬 Messaging Features
- Real-time private messaging
- Real-time room-based chat
- WebSocket using STOMP protocol
- Instant message delivery
- Message status (Sent, Delivered, Read)
- Soft delete messages
- Message timestamps

### 🖼️ Image Sharing
- Upload images (Max 5MB)
- Instant preview
- Stored on server
- Secure image access
- Click to open full image

### 👤 User Presence
- Online/Offline status
- Last seen tracking
- Auto-refresh every 30 seconds

### 🗂️ Chat History
- Load previous messages
- Persistent storage using MySQL
- Messages sorted by timestamp

---

## 🛠️ Tech Stack

### 🔹 Backend
- Spring Boot
- Spring Security
- JWT
- WebSocket (STOMP)
- Hibernate / JPA
- MySQL
- Lombok

### 🔹 Frontend
- React.js
- Axios
- SockJS
- STOMP.js
- CSS

---
# 💬 Real-Time Chat Application

A full-stack, production-ready real-time messaging platform built with Spring Boot, React (Vite), WebSocket, and MySQL. Features include private messaging, group chat rooms, image sharing, last seen indicators, and secure authentication.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen)
![React](https://img.shields.io/badge/React-18.3-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-yellow)

## 🎯 Features

### 🔐 Authentication & Security
- **JWT-based Authentication** with Bearer tokens
- **BCrypt Password Hashing** (strength: 12)
- **OTP-based Password Reset** via email (6-digit, 10-minute expiry)
- **Rate Limiting** on OTP requests (3 per hour)
- **Session Management** with online/offline status

### 💬 Messaging
- **Real-time Private Chat** (one-on-one messaging)
- **Group Chat Rooms** (multi-user broadcast)
- **Message History** (persistent storage with auto-load)
- **Message Deletion** (soft delete with user confirmation)
- **Message Status Indicators** (sent, delivered, read)
- **Last Seen Tracking** (shows user's last active time)

### 📷 Media & Files
- **Image Sharing** (up to 5MB per image)
- **Image Preview** (click to view full size)
- **File Validation** (type and size checking)

### 🎨 User Experience
- **Responsive Design** (mobile, tablet, desktop)
- **Real-time Updates** (no page refresh needed)
- **Loading Indicators** (connection status display)
- **Empty State Messages** (helpful onboarding)
- **Smooth Scrolling** (auto-scroll to latest message)

---

## 🏗️ Architecture

### Backend (Spring Boot)
```
backend/
├── config/
│   ├── SecurityConfig.java         # JWT & CORS configuration
│   └── WebSocketConfig.java        # STOMP WebSocket setup
├── controller/
│   ├── AuthController.java         # Login, register, logout
│   ├── ChatController.java         # REST APIs for chat history
│   ├── WebSocketChatController.java # Real-time message handlers
│   ├── PasswordResetController.java # OTP flow
│   └── FileUploadController.java   # Image upload/retrieval
├── entity/
│   ├── User.java                   # User model (BCrypt password)
│   ├── ChatMessage.java            # Message model
│   ├── ChatRoom.java               # Room model
│   └── PasswordResetOtp.java       # OTP model
├── service/
│   ├── UserService.java            # User management
│   ├── ChatService.java            # Message CRUD
│   ├── OtpService.java             # OTP generation & validation
│   ├── EmailService.java           # SMTP email sender
│   └── JwtService.java             # Token generation & validation
└── security/
    └── JwtAuthenticationFilter.java # Request interceptor
```

### Frontend (React + Vite)
```
frontend/
├── components/
│   ├── auth/
│   │   ├── Login.jsx               # Login form
│   │   ├── Register.jsx            # Registration form
│   │   └── ForgotPassword.jsx      # 3-step password reset
│   ├── chat/
│   │   ├── ChatHome.jsx            # Chat mode selection
│   │   ├── RoomChat.jsx            # Group chat interface
│   │   ├── PrivateChat.jsx         # Private messaging
│   │   └── MessageItem.jsx         # Individual message bubble
│   └── common/
│       ├── Navbar.jsx              # Navigation bar
│       ├── ProtectedRoute.jsx      # Auth guard
│       └── Loading.jsx             # Loading spinner
├── services/
│   ├── api.js                      # Axios configuration
│   ├── authService.js              # Auth API calls
│   └── websocketService.js         # STOMP client wrapper
└── styles/
    ├── App.css                     # Global styles
    ├── Auth.css                    # Authentication pages
    └── Chat.css                    # Chat interface
```

---

## 🚀 Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.1.5**
  - Spring Security
  - Spring Web
  - Spring WebSocket
  - Spring Data JPA
  - Spring Mail
- **MySQL 8.0**
- **JWT (jjwt 0.11.5)**
- **BCrypt** (password hashing)
- **JavaMailSender** (Gmail SMTP)

### Frontend
- **React 18.3** (Functional Components + Hooks)
- **Vite 6.x** (Build tool)
- **React Router DOM 6** (Routing)
- **Axios** (HTTP client)
- **SockJS Client** (WebSocket fallback)
- **STOMP.js** (WebSocket protocol)

### Database
- **MySQL 8.0**
- **Hibernate ORM** (JPA)
- **HikariCP** (Connection pooling)

---

## 📦 Installation & Setup

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- MySQL 8.0 or higher
- Maven 3.6+

### 1️⃣ Clone Repository
```bash
git clone https://github.com/akas-coder/chat-application.git
cd chat-application
```

### 2️⃣ Database Setup
```sql
-- Create database
CREATE DATABASE chat_db;

-- Use database
USE chat_db;

-- Tables will be auto-created by Hibernate on first run
```

### 3️⃣ Backend Configuration

Navigate to `backend/src/main/resources/application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/chat_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expiration=86400000

# Email Configuration (Gmail)
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Generate Gmail App Password:**
1. Go to [Google Account Security](https://myaccount.google.com/security)
2. Enable 2-Factor Authentication
3. Generate App Password for "Mail"
4. Use this password in `application.properties`

### 4️⃣ Start Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

### 5️⃣ Start Frontend
```bash
cd frontend
npm install
npm run dev
```

Frontend will start on `http://localhost:3000`

---

## 🎮 Usage

### 1️⃣ Register Account
- Navigate to `http://localhost:3000`
- Click "Register here"
- Fill in: Full Name, Username, Email, Password
- Click "Register"

### 2️⃣ Login
- Enter username and password
- Receive JWT token (stored in localStorage)
- Redirected to Chat Home

### 3️⃣ Private Chat
- Enter recipient's username
- System validates username exists
- Start chatting in real-time
- Upload images via 📎 button
- Delete messages via 🗑️ icon (hover over your messages)

### 4️⃣ Group Chat
- Enter room ID (e.g., "GENERAL", "TECH")
- Join room (creates if doesn't exist)
- All members see messages in real-time
- System messages for join/leave events

### 5️⃣ Password Reset (OTP)
- Click "Forgot Password"
- Step 1: Enter email → Receive OTP
- Step 2: Enter 6-digit OTP → Verify
- Step 3: Set new password → Login

---

## 🔌 API Endpoints

### Authentication
```
POST   /api/auth/register              Register new user
POST   /api/auth/login                 Login (returns JWT)
POST   /api/auth/logout                Logout user
GET    /api/auth/user/exists           Check if username exists
```

### Password Reset
```
POST   /api/password-reset/request-otp Request OTP via email
POST   /api/password-reset/verify-otp  Verify OTP code
POST   /api/password-reset/reset-password Reset password with OTP
```

### Chat
```
GET    /api/chat/room/{roomName}/messages        Get room history
GET    /api/chat/private/{user1}/{user2}/messages Get private history
DELETE /api/chat/message/{id}                     Delete message
PUT    /api/chat/message/{id}/read                Mark as read
GET    /api/chat/user/{username}/last-seen        Get last seen
```

### File Upload
```
POST   /api/files/upload-image         Upload image (max 5MB)
GET    /api/files/images/{filename}    Retrieve uploaded image
```

### WebSocket Endpoints
```
/ws                                    SockJS connection
/app/chat.sendToRoom                   Send to room
/app/chat.sendToUser                   Send private message
/app/chat.joinRoom/{roomId}            Join room notification
/app/chat.leaveRoom/{roomId}           Leave room notification
/topic/room/{roomId}                   Room broadcast channel
/user/{username}/queue/messages        Private message channel
```

---

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    is_online BOOLEAN DEFAULT FALSE,
    last_seen TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Chat Messages Table
```sql
CREATE TABLE chat_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT,
    room_id BIGINT,
    message_type ENUM('CHAT', 'JOIN', 'LEAVE', 'TYPING', 'IMAGE'),
    image_url VARCHAR(500),
    is_image BOOLEAN DEFAULT FALSE,
    is_read BOOLEAN DEFAULT FALSE,
    is_delivered BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id),
    FOREIGN KEY (room_id) REFERENCES chat_rooms(id)
);
```

### Chat Rooms Table
```sql
CREATE TABLE chat_rooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Password Reset OTP Table
```sql
CREATE TABLE password_reset_otp (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    otp VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    attempt_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🔒 Security Features

### JWT Authentication
- HS256 algorithm
- 24-hour token expiration
- Bearer token in Authorization header
- Automatic logout on token expiration

### Password Security
- BCrypt hashing (strength: 12)
- Minimum 6 characters required
- Stored as irreversible hash

### OTP Security
- 6-digit numeric code
- 10-minute expiration
- One-time use only
- Max 3 verification attempts
- Rate limiting: 3 requests per hour per email

### CORS Protection
- Whitelisted origins only
- Credentials allowed
- Specific methods enabled

---

## 📊 Message Flow

### Private Message
```
1. User A types message
2. Frontend: websocketService.sendToUser(A, B, content)
3. Backend: Receives at /app/chat.sendToUser
4. Backend: Saves to chat_messages table
5. Backend: Sends to /user/B/queue/messages
6. Backend: Also sends to /user/A/queue/messages (echo)
7. Frontend B: Receives and displays
8. Frontend A: Sees own message appear
```

### Room Message
```
1. User A types in GENERAL room
2. Frontend: websocketService.sendToRoom(GENERAL, A, content)
3. Backend: Receives at /app/chat.sendToRoom
4. Backend: Saves to chat_messages table
5. Backend: Broadcasts to /topic/room/GENERAL
6. All subscribed users: Receive message in real-time
```

---

## 🐛 Troubleshooting

### Backend Issues

**Issue:** `401 Unauthorized` on protected endpoints
```bash
Solution: Check if JWT token is present in localStorage
- Login again to get fresh token
- Verify token format: "Bearer "
```

**Issue:** WebSocket connection fails
```bash
Solution: Check CORS configuration
- Ensure frontend URL is in allowedOrigins
- Restart backend after config changes
```

**Issue:** Email not sending
```bash
Solution: Verify Gmail app password
- Use app-specific password, not account password
- Enable "Less secure app access" if needed
```

### Frontend Issues

**Issue:** "global is not defined" error
```bash
Solution: Add to index.html (already included)
window.global = window;
```

**Issue:** Messages not appearing in real-time
```bash
Solution: Check WebSocket connection status
- Should show "● Connected" in chat header
- Check browser console for STOMP errors
```

### Database Issues

**Issue:** `Data truncated for column 'message_type'`
```sql
Solution: Update enum to include IMAGE
ALTER TABLE chat_messages 
MODIFY COLUMN message_type ENUM('CHAT', 'JOIN', 'LEAVE', 'TYPING', 'IMAGE');
```

