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

## 🏗️ Project Architecture

