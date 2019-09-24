#!/usr/bin/env bash

cd $(dirname $0)
cd ..

find src/ -name "*.java" | xargs java -jar tools/google-java-format-1.7-all-deps.jar -i
