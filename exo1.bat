@echo off
cls

set dir="class/exo1"

if  not exist "%dir%" (
        mkdir "%dir%"
)
javac src/prog2/concur/exercice1/*.java -d %dir%

java -classpath %dir% prog2/concur/exercice1/Philosophe

pause