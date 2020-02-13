#TEST PROJECT
##GATEWAYS MANAGEMENT

###REQUIREMENTS

- **MySQL 8.0.19 MySQL Community Server - GPL**
    
        https://cdn.mysql.com/archives/mysql-installer/mysql-installer-web-community-8.0.18.0.msi
    
- **Java version "1.8.0_241"**
- **Apache Maven 3.6.3**

        https://www-us.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.zip

- **Database Schema created**: (the default schema name is **gateways_manage**)
        
        CREATE DATABASE `gateways_manage`
         /*!40100 DEFAULT CHARACTER SET utf8 */ 
         /*!80016 DEFAULT ENCRYPTION=''N'' */

###DEVELOPMENT TOOLS USED

- IntelliJ IDEA 2019.3.2
- Postman 7.17.0
- MySQL Workbench 8.0.19
- GitKraken 4.0.4
- Git Bash
- Github

### HOW TO DOWNLOAD OR CLONE THE REPOSITORY

    git clone https://github.com/Acronus2008/api-rest-gateway.git

###REGARDING THE PROJECT
 This project has Flyway configured as a tool for database migration.
 - Creation of tables automatically
 - Tables are populated with data
 
 ####Location of migration files
 
    resources/db.migration
 
 ###UNIT AND INTEGRATION TESTS
 This project uses the Spock framework to perform unit and integration tests.
 
 ####How to run the tests
 This step is also achieved when we execute the project through maven
    
    mvn clean test
    
###RUNNING THE PROJECT
1. Using java
    -
    - First execute the following command
    
            mvn install 
            
      This command will create an executable java file in the following project path.
      
            target\restgateways-0.0.1-SNAPSHOT.jar
            
    - After executing the previous command, we can execute the application.
    
            java -jar target\restgateways-0.0.1-SNAPSHOT.jar
            
      When executing this command the api's are exposed and we can consume the functionalities described for the gateways
      
###WHERE THE API FUNCTIONALITIES ARE DESCRIBED
  This project uses **Swagger** to describe the api's functionalities, therefore, the description of the functionalities of the exposed api's are described in:
  
    http://localhost:8080/v2/api-docs
    
   or
   
    http://localhost:8080/swagger-ui.html
    
###HOW TO CONSUME THE FUNCTIONALITIES OF THE API's
There is a file in the root of the project that contains a Postman collection and that contains all the functionalities of the Api's.

    GATEWAY.postman_collection.json

In this collection are the functionalities of:

Functionality | Method | Parameters | Parameter type | Descripcion
--- | --- | --- | --- | --- |
add|POST|GatewayDto|Body| Add a gateway
all|GET|GatewayDto|Body| Get all gateway
detail/{id}|GET|Id|PathVariable|Get detail of gateway
update/{id}|PUT|Id, PeripheralDeviceDto|PathVariable, Body|Add device to gateway
delete/{idGateway}/{idPeripheral}|DELETE|Id, Id, PeripheralDeviceDto|PathVariable, Body|Delete device to gateway

###ANOTHER MYSQL SERVER ALTERNATIVE

If there are inconveniences when installing MySQL, you can use an online service that hosts a MySQL server.

    Server: remotemysql.com
    Port: 3306
    User: 0HvBSSKVvS
    Password: SqAwXvFE0S
    Schema: 0HvBSSKVvS
    
####Configuration variables to change if using this MySQL alternative

   - Configuration of spring database connection 
        
    spring.datasource.hikari.jdbc-url=jdbc:mysql://remotemysql.com:3306/0HvBSSKVvS?useSSL=false&serverTimezone=UTC
    spring.datasource.jdbc-url=jdbc:mysql://remotemysql.com:3306/0HvBSSKVvS?useSSL=false&serverTimezone=UTC
    spring.datasource.username=0HvBSSKVvS
    spring.datasource.password=SqAwXvFE0S

   - Configuration for flyway
    
    spring.flyway.url=jdbc:mysql://remotemysql.com:3306/0HvBSSKVvS?useSSL=false&serverTimezone=UTC
    spring.flyway.locations=classpath:/db/migration
    spring.flyway.user=0HvBSSKVvS
    spring.flyway.password=SqAwXvFE0S
    spring.flyway.schemas=0HvBSSKVvS