# Mowing Simulation

A program simulating predefined mower movements in a rectangular surface according to the [program specification](specification.pdf)

## Set up

### Requirements
* Java 17

### 1. Build

./mvnw clean install

### 2. Run

java -jar api/target/api-1.0-SNAPSHOT-jar-with-dependencies.jar api/mowerProgram.input

#### Usage

Input file is called 'mowerProgram.input' located at api module root
Output file will be named 'mowerProgram.output' located at the same directory once program finishes its execution

