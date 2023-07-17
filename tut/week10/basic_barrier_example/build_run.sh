#!/bin/bash

#
# Wojciech Golab, 2016
#

source settings.sh

JAVA_CC=$JAVA_HOME/bin/javac

export CLASSPATH=".:lib/*"

echo --- Cleaning
rm -f *.class

echo --- Compiling Java
$JAVA_CC -version
$JAVA_CC *.java


$JAVA_HOME/bin/java BarrierExample $ZKSTRING /$USER
