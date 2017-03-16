#--by feilong 2016-05-07

projects=$1
echo input params projects:[$projects]

directory=`pwd`

echo $pwd

echo $directory

#####################################:
cd ..

echo $pwd
cd ..

echo $pwd

echo ==================begin========================

MAVEN_BATCH_ECHO=off
MAVEN_BATCH_PAUSE=off

echo mvn clean install -N
mvn clean install -N

echo
echo mvn clean install -pl $projects
echo

mvn clean install -pl $projects
