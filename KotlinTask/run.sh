#!/bin/bash

./gradlew build shadowJar && mv ./build/libs/KotlinTask-1.0-SNAPSHOT-all.jar ./

# Loop through the values 100, 200, 300, 400, and 500
for value in 100 200 300 400 500
do
  # Remove any CSV files
  rm -rf *.csv

  # Run the Java command with the current value
  java -jar KotlinTask-1.0-SNAPSHOT-all.jar list "$value" 20 ./data3.csv

done