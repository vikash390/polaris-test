FROM openjdk:21-slim

WORKDIR /app

# Copy your Java files and libs
COPY SimplePost.java .  
COPY lib/ ./lib/

# Compile the Java file
RUN javac -cp ".:lib/*" SimplePost.java

# Run your Java class
CMD ["java", "-cp", ".:lib/*", "SimplePost"]
