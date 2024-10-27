#!/bin/bash

./gradlew build shadowJar && mv ./build/libs/KotlinTask-1.0-SNAPSHOT-all.jar ./

for value in 100 200 300 400 500
do
  rm -rf *.csv

  java -jar KotlinTask-1.0-SNAPSHOT-all.jar list "$value" 20 ./data3.csv

done