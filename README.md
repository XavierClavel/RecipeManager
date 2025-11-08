# Cooknco
Cooknco is an web application that allows you to write cooking recipes and share them with friends.

## Features
- Write recipes and add an image to them
- Like recipes from your friends
- Follow your friends to see their recipes appear on your feed
- Create cookbooks to group recipes, and add friends to your cookbooks to allow them to add their own recipes

## Infrastructure
Cooknco runs on a Kubernetes cluster. Here are the pods used:
- cooknco-backend: most of the application as a monolith for now
  - cooknco-database: the main database
  - cooknco-redis: used for sessions
- cooknco-mail-service: microservice used for sending mails
  - cooknco-mail-database: its database
- cooknco-frontend: the frontend, powered by nginx

## Stack
- Kotlin
- Ktor
- Ebean
- Vue.js
- Apache Kafka
