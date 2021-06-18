
 What has been done:
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
        - run scripts\poll-app.bat
    - UNIX (Linux,Mac OS)     
        - chmod ugo+x make.sh 
        - make.sh (UNIX)
        - chmod ugo+x scripts\poll-app.sh
        - scripts\poll-app.sh (UNIX)
 
    
    