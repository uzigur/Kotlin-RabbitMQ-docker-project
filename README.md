# Kotlin over RabbitMQ Project

A containerized Kotlin application utilizing RabbitMQ for message brokering and Docker Compose for orchestration.

## Overview
This project demonstrates a **Producer/Consumer** messaging system written in **Kotlin**. It is designed to run in a fully containerized environment, ensuring that the application and the RabbitMQ server can communicate securely within a private virtual network.



## Features
* **Topic Exchange**: Implements flexible message routing using routing keys.
* **Interactive Producer**: Allows real-time message sending via the console.
* **Asynchronous Consumers**: Parallel processing of messages using Kotlin Coroutines.
* **Dockerized Environment**: Automated setup of RabbitMQ and the Kotlin runtime.

## Project Structure
* `*.kt`: Kotlin source files (Main, Producer, and Consumer logic).
* `build.gradle.kts`: Configured with the **ShadowJar** plugin to bundle all dependencies (Kotlin, RabbitMQ, Coroutines).
* `Dockerfile`: A multi-stage build using `eclipse-temurin:21-jre-jammy` for a lightweight runtime.
* `docker-compose.yml`: Orchestrates the network and handles service health checks to ensure RabbitMQ is ready before the app starts.

## How to Run

### Prerequisites
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running on Windows.

### Execution Steps
To run the system, execute the following commands in your terminal:

1. **Start RabbitMQ** (Background mode):
   ```bash
   docker-compose up -d rabbitmq
2. **Start The APP** (Intreactive mode):
   ```bash
   docker-compose run --rm --build app
3. **Take Down The App:**
   ```bash
   docker-compose down
