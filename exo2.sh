#!/bin/bash

dir="class/exo2";

if [ ! -f $dir ]; then
        mkdir -p $dir
fi
javac src/prog2/concur/exercice2/*.java -d $dir

java -classpath $dir prog2/concur/exercice2/Exercice2
