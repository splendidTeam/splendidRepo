# arr=($a)用于将字符串$a分割到数组$arr ${arr[0]} ${arr[1]} ... 分别存储分割后的数组第1 2 ... 项
# projects=($1)
# command=$2
projects="nebula-utilities,nebula-repo,nebula-helix"
command="clean install"

# echo
# echo
#
# echo cd ..
# cd ..
#
# echo cd ..
# cd ..

cd /Users/feilong/workspace/baozun/Nebula5.3.2


##-----------------------------------------------------------------------------------

#::MAVEN_BATCH_ECHO - 设为'on'使能批处理命令的反馈
MAVEN_BATCH_ECHO=off

#::设为'on',在结束前等待一个键的按下.
MAVEN_BATCH_PAUSE=off

##-----------------------------------------------------------------------------------
echo mvn clean install -N
mvn clean install -N

echo mvn clean install -pl $projects
mvn clean install -pl $projects
