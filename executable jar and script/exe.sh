#!/bin/bash

echo " Executing bash script for java jar...."
echo " Finding path..."
DIR="$( cd "$( dirname "$0" )" && pwd)"

echo
echo " Current Path is "$DIR
echo " Excuting project .jar"
echo
java -jar $DIR"/MainThread.jar"
