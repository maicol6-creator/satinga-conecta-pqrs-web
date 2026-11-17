#!/usr/bin/env bash
# Linux / macOS: compila y arranca Tomcat 10 -> http://localhost:8080/satinga-pqrs/
cd "$(dirname "$0")"
mvn package -DskipTests cargo:run
