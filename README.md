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
  
# Example to add new user  
## 1. User `http://localhost:8084/all/register` to register a new user  
`{
  "username": "admin2",
  "password": "admin2",
  "role": "ADMIN"
}`
### 2. Use `http://localhost:8084/all/login` to get the **Token** with valid *Username* and *Password*  
   `{
  "username": "admin2",
  "password": "admin2"
   }`

##Product:  
### 1. You can manipulate enpoints with the root  `"/products"` ,then , next for the enpoints you can use:
   -  `"/GetProducts"`
   -  `/GetProduct/{productName}`
   -  `/UpdateProduct/{productName}`
   -  `/DeleteProduct/{productName}`
  
### 2. Create a product
  {
  "productName": "Mouse inalámbrico",
  "productPrice": 300,
  "productStock": 100
   }

## Order:
### 1. Use the root `/Order` to use order endpoints:
   -`/GetAllOrders`
   - `/GetOrder/{id}`
   - `/AddOrder`

### 2. Create order:  
`
    {
  "orderUserId": 2,
  "orderItems": [
    {
      "productName": "Talet Pro",
      "quantity": 1
    }
  ]
}`
##Payment:  
### 1. To pay a order use `/Payments` to use endpoints:  
   -`/pay`  
   -`/order/{orderId}`  

 ### 2. Pay:
 `
      {
    "orderId" : 1,
    "amount" : 3500.0
    }
  `    
