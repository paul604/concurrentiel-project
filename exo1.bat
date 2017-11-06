@echo off
cls

set classDir="class"
set exoDir="%classDir%/exo1"

if  not exist "%classDir%" (
    mkdir "%classDir%"
)

if  not exist "%exoDir%" (
    mkdir "%exoDir%"
)

javac src/prog2/concur/exercice1/*.java -d %exoDir%

java -classpath %exoDir% prog2/concur/exercice1/Philosophe

pause
