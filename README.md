 Run Project  
 Use for each project:  
`mvn spring-boot:run`

1. First Run the Eureka Proyect
2. Second, Run the server proyect inside Principal-server.
3. Then Run the others Projects.

User:  
**All** users can use `/login` AND `/register`  
To get a valid **Token** to use enpoints, you need to add a **ADMIN** user with `/login` then use the `/register` endpoint to get the **Token**, on each request using PostMan use the **Token** of **ADMIN** user.
You can add **ADMIN** and **Customer** users, **ADMIN** user can use all the endpoints from other microservices.    
  
### Example to add new user  
1. Use `http://localhost:8084/all/register` to register a new user  
`{
  "username": "admin2",
  "password": "admin2",
  "role": "ADMIN"
}`
2. Use `http://localhost:8084/all/login` to get the **Token** with valid *Username* and *Password*  
   `{
  "username": "admin2",
  "password": "admin2"

}`

 

Product:  

Order:  

Payment:  
