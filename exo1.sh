#!/bin/bash

dir="class/exo1";

if [ ! -f $dir ]; then
        mkdir -p $dir
fi
javac src/prog2/concur/exercice1/*.java -d $dir

java -classpath $dir prog2/concur/exercice1/Philosophe
