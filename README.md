# Youtubey Apis

Back End apis for the youtubey platform

## Technologies used

- [Node](https://nodejs.org/docs/latest/api/) JavaScript Runtime environment 
- API done with [express.js](https://expressjs.com)
- [Java](https://spring.io) RestAPI
- [Python](https://fastapi.tiangolo.com) Api, made with FastAPI
- [Database](https://www.mongodb.com)
- [Database2](https://firebase.google.com)


## Setup
You can pull the images from dockerhub.
```
docker pull bnn16/youtubeyy:<spring-app, node-app, python-app>
```
After you download the 3 images you have to build and run the images via 
```
docker-compose up -d
```

## Testing
Testing is done with JUnit and Mockito.


I used sonarqube to check code quality and test coverage. You can see that it has over 80% coverage.

![alt text](https://i.imgur.com/fgEsxuC.png)

## Routes
### All endpoints with the exception of /login and /register are protected via JWT.

# Endpoints for the User Controller

## Register a user
```
curl -X POST -H "Content-Type: application/json" -d '{
  username: "foo",
  password: "foo",
  firstName: "Bogdan",
  email: 'bogdan.nikolov4@outlook.com',
}' http://example.com/rest/auth/register
```
You get the user info returned.

## Login a user
```
curl -X POST -H "Content-Type: application/json" -d '{
  email: "bogdan.nikolov4@outlook.com",
  password: "foo",
}' http://example.com/rest/auth/login
```
You get a JWT token in the following json
```
    {
       token: '...',
    }
``` 

## Get a user via ID 

```
curl -X GET http://example.com/rest/auth/profile/:id
```
You get a json with the given users information in the following format:
```
{
  username: '',
  userID: '',
  role: '',
  description: '',
  image: Binary data,
  ytLink: '',
  location: ''
}
```

## Add user information
You can create the user information for your profile with this endpoint

```
curl -X POST -H "Content-Type: application/json" -d '{
  "username": "JohnDoe",
  "userID": "123456",
  "role": "youtuber",
  "description": "Passionate about coding and technology.",
  "image": "SGVsbG8gd29ybGQh",  // Binary data encoded in base64
  "ytLink": "https://www.youtube.com/user/JohnDoe",
  "location": "Cityville, USA"
}' http://example.com/rest/auth/profile/:id
```

## Update user information
You can update the user information for your profile with this endpoint

```
curl -X PATCH -H "Content-Type: application/json" -d '{
  "ytLink": "https://www.youtube.com/user/JohnDoe123",
  "location": "Cityville, USA"
}' http://example.com/rest/auth/profile/:id
```

## Delete user information
You can delete the user information with the following endpoint

```
curl -X DELETE http://example.com/rest/auth/profile/:id
```

# Endpoints for the Post Controller

## Create a  Post
A user can create a post with the endpoint.
You get the public_url from the python API, that is hit first with the video itself that is uploaded to firebase.

```
curl -X POST -H "Content-Type: application/json" -d '{
  "id": "1",
  "title": "Sample Title",
  "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
  "userId": "user123",
  "status": "Published",
  "link": "https://www.example.com/post/1",
  "public_url": "https://www.example.com/public/1",
}' http://example.com/rest/requests/edits/posts
```
## Get a Post
To retrieve posts by a specific user ID, you can use the following endpoint:

```
curl http://example.com/rest/requests/edits/posts/user/user123
```
This will return a list of posts associated with the user with ID "user123" or a 404 status if no posts are found.

## Get Posts by Editor ID
Retrieve posts associated with a specific editor ID by sending a GET request to the following endpoint:
```
curl http://example.com/rest/requests/edits/posts/editor/editor456
```
This will return a list of posts associated with the editor with ID "editor456" or a 404 status if no posts are found.
```
[
  {
    "id": "1",
    "title": "Sample Title 1",
    "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    "userId": "user123",
    "status": "Published",
    "link": "https://www.example.com/post/1",
    "public_url": "https://www.example.com/public/1",
    "editorId": "editor456",
    "editedVideo": "https://www.example.com/edited-video/1"
  },
  {
    "id": "2",
    "title": "Sample Title 2",
    "description": "Another post description goes here.",
    "userId": "user456",
    "status": "Draft",
    "link": "https://www.example.com/post/2",
    "public_url": "https://www.example.com/public/2",
    "editorId": "editor456",
    "editedVideo": "https://www.example.com/edited-video/2"
  }
]
```
## Get All Posts with Status "Created"
Retrieve all posts with the status "Created" by sending a GET request to the following endpoint:
```
curl http://example.com/rest/requests/edits/posts
```
The response will include the list of posts with status "Created" or an error message if no such posts are found.
```
[
  {
    "id": "1",
    "title": "Sample Title 1",
    "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    "userId": "user123",
    "status": "Created",
    "link": "https://www.example.com/post/1",
    "public_url": "https://www.example.com/public/1",
    "editorId": "editor456",
    "editedVideo": "https://www.example.com/edited-video/1"
  },
  {
    "id": "2",
    "title": "Sample Title 2",
    "description": "Another post description goes here.",
    "userId": "user456",
    "status": "Created",
    "link": "https://www.example.com/post/2",
    "public_url": "https://www.example.com/public/2",
    "editorId": "editor789",
    "editedVideo": "https://www.example.com/edited-video/2"
  }
]
```

## Get Post by ID
Retrieve a specific post by its ID by sending a GET request to the following endpoint:
```
curl http://example.com/rest/requests/edits/posts/:id
```
The response will include the details of the post with the specified ID or an error message if the post is not found or an exception occurs.

Example Response (Successful)
```
{
  "id": "1",
  "title": "Sample Title",
  "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
  "userId": "user123",
  "status": "Published",
  "link": "https://www.example.com/post/1",
  "public_url": "https://www.example.com/public/1",
  "editorId": "editor456",
  "editedVideo": "https://www.example.com/edited-video/1"
}
```

Example Response (Error)
```
{
  "message": "Post not found with ID: 1"
}
```

## Delete Post
You can delete a post with the following endpoint

```
curl -X DELETE http://example.com/rest/requests/edits/posts/:id
```
The response will include a status code, if the deletion was successful or not.

## Update Post by ID
Update a specific post by its ID using a PATCH request to the following endpoint:
```
curl -X PATCH -H "Content-Type: application/json" -d '{
  "title": "Updated Title",
  "description": "Updated description."
}' http://example.com/rest/requests/edits/posts/:id
```
The response will include a status code, if the change was successful or not.

# CI/CD pipeline

This GitHub Actions workflow defines a CI/CD pipeline that is triggered on every push to the `main` branch.

```yaml
name: CI/CD pipeline

on:
  push:
    branches:
      - main

jobs:
  build:
    name: Build, Test, and Analyze Code Quality, Docker, Deploy
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v2

      - name: Set up Java 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'adopt'
          java-package: 'jdk'
          architecture: 'x64'

      - name: List directory contents
        run: ls -l

      - name: Checkout project sources
        uses: actions/checkout@v3
      - name: Setup Gradle
        uses: gradle/gradle-build-action@v2
      - name: Run build with Gradle Wrapper
        run: ./gradlew build
        working-directory: ./spring

      - name: List directory contents bin
        run: ls spring/build/libs/

      # commenting this out, due to not having a sonarqube host instance, to send the answer http://localhost:9000 doesn't work,
      # student desk refused to pay to host the sonarqube
      # - name: Build and Analyze with SonarQube
      #   run: ./gradlew build sonar -Dsonar.projectKey=youtubey -Dsonar.host.url=http://localhost:9000 -Dsonar.login=$SONAR_TOKEN
      #   working-directory: ./spring

      # env:
      #   SONAR_TOKEN: sqp_cd96491f1f2c42fc293375f20b2f805caf30ddfb
      - name: Set up Docker
        uses: docker/setup-buildx-action@v1

      - name: Docker Login
        run: docker login -u bnn16 -p ${{ secrets.DOCKER_PASSWORD }}
        env:
          DOCKER_PASSWORD: ${{ secrets.DOCKER_PASSWORD }}

      - name: Build Docker Images
        run: docker-compose build
        working-directory: ./

      - name: Push Docker Images
        run: docker-compose push
        working-directory: ./

      # final step is to push to azure/aws or other hosting service. Here is how it works.
      # - name: Deploy to Azure App Service
      #   uses: azure/webapps-deploy@v2
      #   with:
      #     app-name: <AZURE_APP_NAME>
      #     images: '<ACR_NAME>.azurecr.io/<REPOSITORY>:<TAG>'
      #   env:
      #     AZURE_LOGIN: ${{ secrets.AZURE_CREDENTIALS }}


```

# Suggestion

I would recommend that this api is split into multiple microservices, probably with RPC, so it's faster and safer.
Have only 1 entrypoint with a restapi, that uses a queue to queue a request to an RPC microservice architecture.
