#!/usr/bin/env bash

cd $(dirname $0)
cd ..

if [ -n "$JAVA_HOME" ] ; then
	JAVACMD="$JAVA_HOME/bin/java"
else
	JAVACMD="java"
fi


find src/ -name "*.java" | xargs $JAVACMD -jar tools/google-java-format-1.7-all-deps.jar -i

cd src/web
yarn lint
