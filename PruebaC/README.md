# PruebaC - Ixchel Garcia

## Requirements
- Java 17
- Maven 3.6+
- Spring Boot 4.0.3

## How to run
1. Clone or download the project
2. Open project in Eclipse or IntelliJ
3. Run `PruebaCApplication.java`
4. API runs on `http://localhost:8080`

## API Documentation (Swagger)
Once the app is running, open:
```
http://localhost:8080/swagger-ui/index.html
```

## Postman Collection
Import the file `PruebaC_Ixchel.postman_collection.json` in Postman

## Endpoints
GET  /users | Get all users |
GET  /users?sortedBy=name | Get users sorted by field |
GET  /users?filter=name+co+user | Get users filtered |
POST  /users | Create new user |
PATCH  /users/{id} | Update user fields |
DELETE  /users/{id} | Delete user |
POST /login | User authentication |

## Notes
- Passwords are encrypted using AES-256 GCM
- created_at uses Madagascar timezone (Indian/Antananarivo)
- tax_id must have RFC format and must be unique
- phone must have 10 digits (country code optional)
