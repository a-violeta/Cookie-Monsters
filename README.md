# Cookie Monsters 🍪

A Reddit clone built with Java and Spring Boot, featuring authentication, posts, comments, and communities (subreddits).

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
    - [Storage Abstraction](#storage-abstraction)
    - [Business Logic Abstraction](#business-logic-abstraction)
    - [Authentication](#authentication)
    - [Logging](#logging)
    - [Soft Delete](#soft-delete)
    - [Voting](#voting)
- [Deployment](#deployment)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Running the App](#running-the-app)
- [API Documentation](#api-documentation)
- [Deprecated: Console Client](#deprecated-console-client)

---

## Overview

Cookie Monsters is a project implemented in the **Zero To Hero** Cognyte programme. It is a Reddit-style social platform supporting user accounts, communities (subreddits), posts, and comments. The backend is a Spring Boot REST API, containerized and deployed on AWS EC2 with a PostgreSQL database, plus a companion C# microservice for image filtering.

## Features

- **Login & Register** — JWT-based authentication
- **Posts** — create, read, update, delete within a community
- **Comments** — create, read, update, delete on a post
- **Communities (Subreddits)** — create, edit, list

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Framework | Spring / Spring Boot |
| IDE | IntelliJ IDEA |
| Database | PostgreSQL |
| Auth | JWT |
| Image filters | C# microservice |
| Containerization | Docker |
| Deployment | AWS EC2 |

*(exact versions — Java version, Spring Boot version, Postgres version, Docker base images.)*

## Architecture

*(high-level diagram or description of how FE, BE, DB, and the image-filter microservice talk to each other.)*

The frontend connects to the backend via the EC2-hosted URL and communicates entirely over HTTP, sending requests and receiving JSON responses.

### Storage Abstraction

Business logic never talks to the database directly. Every entity has a `*Repository` interface (e.g. `UserRepository`, `PostRepository`) sitting between the `*Service` classes and persistence. The current implementation uses Spring Data JPA (`Jpa*Repository` e.g. `JpaUserRepository`, `JpaPostRepository`), but the interface boundary means storage could be swapped for something JPA doesn't support (in-memory, file-based, or a NoSQL store) without touching business logic. The first storage type was in fact in memory using `InMemory*Repository` interfaces.

### Business Logic Abstraction

Similarly, the (deprecated) console never called `*Service` classes directly — it depended only on `*Abstract` interfaces (e.g. `UserAbstract`, `PostAbstract`). This meant the business logic implementation could change, or even move to a different process entirely (see [Deprecated: Console Client](#deprecated-console-client)), without the console needing to know.

### Authentication

JWT-based authentication protects write operations and user-specific reads. Tokens are issued on login/register and validated on each request via a security filter.

*(token expiry, which endpoints are public vs. protected, the classes that do these things)*

### Logging

A handmade logger runs on a separate thread, so logging doesn't block request handling.

*(what gets logged, where logs are written)*

### Soft Delete

Users, posts and comments are soft deleted using a `isDeleted` field in each model and are never actually deleted from the database.

*(how "deleted" records are filtered from normal queries)*

### Voting

Using 2 entities `PostVote` and `CommentVote` a user is able to upvote or downvote posts and comments.

## Deployment

- **Backend**: containerized (Docker) and deployed on AWS EC2
- **Database**: PostgreSQL, deployed in a separate container in the seme EC2
- **Image filters**: a dedicated C# microservice, called by the backend for post-image processing
- **Frontend**: connects to the backend via the EC2 instance's public URL

*(EC2 instance type/region, how the container is built and shipped, environment variable / secrets management, how the C# microservice is deployed and reached.)*

## Project Structure

*(package-by-package breakdown)*

## Installation

*(prerequisites — JDK version, Maven, Docker, profiles and setup required before first run.)*

## Running the App

*(local run + Docker Compose step vs. how the deployment happens after pushing to main)*

## API Documentation

[https://zth-cog-fe.netlify.app/api-docs](https://zth-cog-fe.netlify.app/api-docs)

## Deprecated: Console Client

This project originally shipped with a command-line console client as an alternative frontend. The console ran as a **separate process** from the backend and communicated with it over HTTP, using the same `*Abstract` interface abstraction described above — one implementation talked to a local database, the other made HTTP calls to the (now-remote) business logic. Because of further development prioritizing the frontend, the console is no longer maintained. For further explanations about how it worked, check out `src/main/java/com/app/explanations.md`. To revive it the `*command` classes from `src/main/java/com/app/console` would need to be modified.
