@echo off
cls

set classDir="class"
set exoDir="%classDir%/exo2"

if  not exist "%classDir%" (
    mkdir "%classDir%"
)

if  not exist "%exoDir%" (
    mkdir "%exoDir%"
)

javac src/prog2/concur/exercice2/*.java -d %exoDir%

java -classpath %exoDir% prog2/concur/exercice2/Exercice2

pause
