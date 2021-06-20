docker build -t note-app .
docker run -d --name note-app -p 8080:8080 note-app