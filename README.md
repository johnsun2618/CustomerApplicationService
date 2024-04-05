# CustomerApplicationService (SunBase Assignment) Backend Project
This repository hosts a backend API designed to manage customer information. The project's primary objective is to implement various CRUD (Create, Read, Update, Delete) operations in its initial phase and extend its functionality in the second phase to synchronize customer data from a remote API.

## Assignment Completion:

The following API endpoints have been successfully implemented in the initial phase:
- To access the controller the endpoint would be http://localhost:8080/home

1. **Create a Customer**
      - Path: http://localhost:8080/home/api/create
      - Method: POST
      - Description: Creates a new customer in the system.

2. Get a List of Customers
     - Path: http://localhost:8080/home/api/getlist
     - Method: GET
     - Description: Retrieves a paginated, sorted, and searchable list of customers.
   
3. Get a Single Customer Based on ID
     - Path: http://localhost:8080/home/api/CustomerById/1 or 2 or 3 etc
     - Method: GET
     - Description: Retrieves details of a specific customer based on their ID.
   
4. Search a Customer by Name, Email, State, etc.
     - Path: http://localhost:8080/home/api/customers/search
     - Method: GET
     - Description: Searches for a customer by Name, Email, State, City.
   
5. Update a Customer
     - Path: http://localhost:8080/home/api/updateDetails/1 or 2 or 3 etc
     - Method: PUT
     - Description: Updates an existing customer's information.
   
6. Delete a Customer
      - Path: http://localhost:8080/home/api/deleteById/1 or 2 or 3 etc
      - Method: DELETE
      - Description: Deletes a customer from the system.
  

### DataBase
 - I am using MySQL for the database, with the database named "customerappservicedb".
 - ![Screenshot 2024-04-05 105520](https://github.com/johnsun2618/CustomerApplicationService/assets/101565759/7d7b6ed4-3452-46f9-8fc4-b97084492add)

### Authentication
  - JWT Authentication has been integrated for enhanced security and token retrieval.


## Second Phase
     - In the second phase of development, the following functionality has been added:

 - ### Synchronize Customer List
     - Description: A "Sync" button has been introduced on the customer list screen. Clicking this button triggers a call to a remote API to fetch the customer list. The retrieved customers are then saved in the local database. If a customer already exists in the database, their information is updated instead of inserting a new record. For manual interaction with this feature, either download the frontend repository or utilize Postman (http://localhost:8080/home/api/customers/search/sunbase).
     
## Frontend Repository Link
  - The frontend code associated with this project can be found at the following repository:
  - link : https://github.com/johnsun2618/CustomerApplicationService_Frontend

## Setup and Usage
To set up and utilize this project, follow these steps:

- Clone this repository.
- Configure the backend environment.
- Run the backend server.
- Clone the frontend code.
- Run the frontend code.
