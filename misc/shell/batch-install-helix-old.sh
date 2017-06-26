#		one key clean install nebula-helix
#
#
#  helix is nebula child project,dependency on nebula-repo,and nebula-repo dependency nebula-utilities,
#	if nebula-utilities or nebula-repo,has any code change,we need first step install nebula-utilities,second step install nebula-repo
#	At this time is more complicated, so we provide one key clean install nebula-helix function
#
# step0 chmod +x  *.sh
#
# step1 base on nebula parent git update
#
# step2 double clike this bat file,will auto install parent project,
#		and nebula-utilities,nebula-repo,nebula-helix
#		don't need youself install project-by-project
#
#--by feilong 2017年03月10日

echo
echo create by feilong
echo 2017年03月10日

echo pwd:$pwd

#basepath=$(cd `dirname $0`; pwd)
basepath="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo ${basepath}/batchInstall.sh "nebula-utilities,nebula-repo,nebula-helix"
${basepath}/batchInstall.sh "nebula-utilities,nebula-repo,nebula-helix"
