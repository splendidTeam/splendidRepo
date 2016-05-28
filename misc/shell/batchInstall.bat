::--by feilong 2016-05-07
mode con cols=150 lines=5600

set projects=%1%
echo input params projects:[%projects%]

:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
set mavenProject.current=%~dp0

::%~d0
set DISK_NAME=%mavenProject.current:~0,2%
%DISK_NAME%

cd %mavenProject.current%
cd ..
cd ..
set mavenProject.root=%cd%

echo ==================begin========================

set MAVEN_BATCH_ECHO=off
set MAVEN_BATCH_PAUSE=off

cd "%mavenProject.root%"
call mvn clean install -N

echo.
echo call mvn clean install -pl %projects%
echo.
call mvn clean install -pl %projects%