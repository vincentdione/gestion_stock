#!/bin/bash

# Attendre que l'API soit disponible
until curl -s http://stock-api:8082/api/v1/auth/register > /dev/null; do
  echo "Waiting for stock-api..."
  sleep 5
done

# Enregistrer l'utilisateur
curl -X POST http://stock-api:8082/api/v1/auth/register \
-H "Content-Type: application/json" \
-d '{
  "nom": "prince",
  "prenom": "prince",
  "username": "prince",
  "email": "prince@gmail.com",
  "password": "passer",
  "role": "ADMIN"
}'

echo "User registered successfully!"
