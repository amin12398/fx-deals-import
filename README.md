# FX Deals 

## 1. Project Overview

This project handles FX Deals through a REST API. It can:

- Import FX Deals
- Validate them
- Save valid deals in MySQL
- Skip duplicates
- Not perform rollback
- Return success count and failed rows


## 2. Tech Stack

The project uses:

- Java 17
- Spring Boot
- MySQL 8
- Docker and Docker Compose
- Maven
- JUnit 5
- JaCoCo (test coverage)
- K6 (performance testing)
- Postman collection

  
## Architecture

The project is organized in layers:

- **Controller**: handles API requests
- **Service**: contains business logic
- **Repository**: manages database interactions
- **Entity**: FX Deal data model

- **Tests**
  - **Unit tests**: `DealControllerTest`, `DealCsvReaderTest` , `DealServiceTest` 
  - **Jacoco**    :  Generates code coverage reports  , Reports are located under `target/site/jacoco`


 ## 4. Validation Rules

Deals are checked for:

- Missing fields
- Invalid timestamps
- Missing currency codes
- Missing amounts
- Duplicate deal IDs

## 5. Run the Project

### Build the project 
```bash
mvn clean install
mvn spring-boot:run
docker-compose up --build
API endpoint : POST /api/deals/import


# 6. Testing

## Unit Tests

- **DealControllerTest**: tests API endpoints  
- **DealCsvReaderTest**: tests CSV import and validation logic  
- **DealServiceTest**: tests service methods  

## Integration Tests

- **DealServiceIntegrationTest**: tests service and repository together  

## Test Coverage

- **JaCoCo report**: `target/site/jacoco/index.html`  
  Shows coverage for all tests in the project














