
 What was done:
 -
 1. Note-app - REST application based on Spring boot framework(Spring mvc) ,JDBC.
   
 1. Database - H2 
 
 1. App functionality 
 
    Notes - Add/Update/DELETE

    - getting filtered notes by sub string.
    - getting filtered notes by hash tag.
    - getting all notes.
    
    
API documentation by Swagger 
-

Lunch application. API documentation will be on http://localhost:8080/swagger-ui/index.html

Compile and run instruction
-

- Run from Intellij IDEA.
    - In Intellij IDEA click to project folder->add framework support->Maven.
    - Lunch src\main\java\com\berdibekov\NoteApplication.java .

- From Console 
    - Windows
        - run make.bat
        - run scripts\note-app.bat
    - UNIX (Linux,Mac OS)     
        - run chmod ugo+x make.sh 
        - run make.sh (UNIX)
        - run chmod ugo+x scripts\note-app.sh
        - run scripts\note-app.sh (UNIX)
- Run in Docker
    - run buildDocker.bat (Windows) / buildDocker.sh (UNIX) to build /Dockerfile.
    - run stopDocker.bat (Windows) / stopDocker.sh (UNIX) to stop container.
    - run startDocker.bat (Windows) / startDocker.sh (UNIX) to restart container.    
#Use instructions
1. to filter by subString specify subString param in GET request.
1. to filter by hash tag specify hashTag param in GET request.

- requests by swagger.
    http://localhost:8080/swagger-ui/index.html
- requests by postman.
    At /docs you can find postman collection and json example of request body.

# Create note

   ![Alt text](docs/create.png?raw=true)
   #Get filtered notes
   ![alt text](docs/getFiltered.png?raw=true)
    
    
    