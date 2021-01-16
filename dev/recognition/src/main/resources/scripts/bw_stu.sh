#!/bin/bash

set -e

ASR_HOME=/opt/asr

if [ ! -e "${JAVA_HOME}/bin/java" ]; then
    echo "JAVA_HOME not properly set!"
    exit 1
fi

if [ ! -e "${ASR_HOME}/bin/an4_learn.sh" ]; then
    echo "ASR_HOME not properly set!"
    exit 1
fi


JAVA_EXE=$JAVA_HOME/bin/java
CMD_LINE_ARGS=an4-bw-stu-context.xml
JAR=$ASR_HOME/lib/recognition-1.0-SNAPSHOT.jar

cd $ASR_HOME
$JAVA_EXE -ea -d64 -Xmx6200M -Xms3024M -jar $JAR $CMD_LINE_ARGS