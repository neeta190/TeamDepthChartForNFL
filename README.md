# TeamDepthChart

TeamDepthChart is designed to create a sample Trading Solution for NFL Depth Charts. This application provides a set of RESTful APIs to perform GET/POST/DELETE Operations for NFL Depth Charts

## Features
- Add a player to the team’s depth chart 
- Get the full team depth chart
- Get the backups of a player from team's depth chart
- Delete a player from team's the depth chart

## Technologies
This project uses the following technologies:
- Java 17+
- Spring Boot 3
- Spring Data JPA
- Hibernate
- H2 Database
- JUnit 5
- Maven

## Prerequisites
Make sure you have the following installed on your machine:
- Java 17+
- Maven
  
## Steps to Install
1.	Clone the repository

  	git clone https://github.com/yourusername/project-name.git
2.	Navigate to the project directory

  	cd teamDepthcharts
3. Build the project with Maven

   mvn clean install
4. Run the application

   mvn spring-boot:run

## Swagger URL
http://localhost:8080/swagger-ui/index.html

## API Enpoints and  Example API Requests

#### Adding a Player to Team's Depth Chart

**Endpoint:** `POST /depth/chart/addPlayerToDepthChart`

**Request Body:**

```json
{
  "position": "string",
  "player": {
    "number": 0,
    "name": "string"
  },
  "depth": 0
}
```
**Response:**  
HTTP Status 201 Created  
`Location: /depth/chart/getDepthChart`

#### View Full Depth Chart

**Endpoint:** `GET /depth/chart/getDepthChart`  
**Response:**

```json
{
  "RB": [],
  "QB": [
    {
      "number": 12,
      "name": "Tom Brady"
    },
    {
      "number": 2,
      "name": "Kyle Trask"
    },
    {
      "number": 11,
      "name": "Blaine Gabbert"
    }
  ]
}
```
#### Get the backup(s) of a player

**Endpoint:** `GET /depth/chart/getBackups` 

**Request Param:**
```
playerPosition=QB
player={"number":12,"name":"Tom Brady"}
```

**Response:**  
```
[(#2, name=Kyle Trask), (#11, name=Blaine Gabbert)]
```
 HTTP Status 200 OK  
`Location: /depth/chart/getDepthChart`

#### Delete a player 

**Endpoint:** `DELETE /depth/chart/removePlayerFromDepthChart` 

**Request Param:**
```
playerPosition=QB
player={"number":12,"name":"Tom Brady"}
```

**Response:**  
```
[(#12, name=Tom Brady)]
```
 HTTP Status 200 OK  
`Location: /depth/chart/getDepthChart`

