# Galaxy Trip

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

## 📖 Project Overview

Galaxy Trip is a Java-based implementation of the popular board game, developed as part of the Software Engineering course at Politecnico di Milano (Academic Year 2024–2025). Players construct their spaceships in real-time using pipes, engines, and cabins, while navigating meteoroids, space pirates, and other cosmic challenges.

## 🛠️ Project Structure

* **IS25-AM05-1.0-SNAPSHOT-server-<architecture>.jar**: Server component handling lobby management, game sessions, and communication protocols.

    * **Socket port**: `25565`
    * **RMI port**: `25566`
* **IS25-AM05-1.0-SNAPSHOT-client-<architecture>.jar**: Client application supporting GUI (JavaFX) or TUI (console) interfaces.

## 👥 Team Members

* **Giacomo Di Clerico** ([MishowHD](https://github.com/MishowHD))
* **Loreto Del Vecchio** ([lerry04](https://github.com/lerry04))
* **Giuseppe D’Ambrosi** ([giusedmb](https://github.com/giusedmb))
* **Lorenzo D’Ortona** ([LorenzoDOrtona](https://github.com/LorenzoDOrtona))

## 📦 Technologies & Dependencies

| Library / Plugin       | Version       | Purpose                                      |
|------------------------|---------------|----------------------------------------------|
| Maven                  | 3.6+          | Build automation                             |
| Java (JDK)             | 23            | Core programming language (source/target 23) |
| JUnit                  | 5.10.2 (test) | Unit testing framework                       |
| JavaFX Controls & FXML | 24            | Graphical user interface                     |
| JavaFX Media           | 24            | Multimedia support                           |
| Jackson Databind       | 2.17.2        | JSON serialization/deserialization           |

## ✅ Implemented Features

**Core Features**

| Feature                       | Status |
|:------------------------------|:------:|
| Ship building rules           |   ✅    |
| Full game rules               |   ✅    |
| Socket communication          |   ✅    |
| RMI communication             |   ✅    |
| Console-based interface (TUI) |   ✅    |
| Graphical interface (GUI)     |   ✅    |

**Advanced Features**

| Feature                      | Status |
|:-----------------------------|:------:|
| Test flight simulation       |   ✅    |
| Multiple concurrent games    |   ✅    |
| Persistence (data saving)    |   ⛔️   |
| Resilience to disconnections |   ⛔️   |

> **Legend:** ✅ Implemented • ⛔ Not implemented • ⚠️ In progress

---

## 📥 Installation & Execution

### Prerequisites

* Java 23 (or higher) installed and on your `PATH`.

  ```bash
  java -version
  ```
* Maven (for building from source).
* JAR files located in `deliverables/out/artifacts/` directory.

### Running the Server

The server listens on port **25565** for socket connections and **25566** for RMI.

```bash
cd deliverables/out/artifacts
java -jar IS25-AM05-1.0-SNAPSHOT-server-<architecture>.jar
```

### Running the Client

The client requires three arguments:

1. **Server IP** (e.g., `127.0.0.1`)
2. **Communication method**: `--socket` or `--rmi`
3. **Interface mode**: `--GUI` or `--TUI`

```bash
java -jar IS25-AM05-1.0-SNAPSHOT-client-<architecture>.jar <IP> <method> <interface>
```

#### Examples

* **GUI via socket (port 25565)**

  ```bash
  java -jar IS25-AM05-1.0-SNAPSHOT-client-<architecture>.jar 192.168.1.10 --socket --GUI
  ```
* **TUI via RMI (port 25566)**

  ```bash
  java -jar IS25-AM05-1.0-SNAPSHOT-client-<architecture>.jar 192.168.1.10 --rmi --TUI
  ```

## 🐞 Debug & Tips

* Check server console logs for binding or connection errors.
* Ensure ports 25565 (socket) and 25566 (RMI) are open and not blocked by firewall.
* Verify that both server and client run on the same Java version.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
