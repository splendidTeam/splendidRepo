#!/bin/bash

if [ $# -lt 1 ] ; then
  #echo "Configuration value is missing!" > /dev/null 1>&2
  echo "配置值丢失！" > /dev/null 1>&2
  exit 1
fi

baseDirForScriptSelf=$(cd "$(dirname "$0")"; pwd)
source $baseDirForScriptSelf/config

new_val=${1// /}

match=`echo "$new_val" | egrep -i "^#[0-9a-f]+$" || echo "$new_val" | egrep -i "^[a-z]+$" || echo "$new_val" | egrep -i "rgb\([0-9]+,[0-9]+,[0-9]+\)"`
if [ -z "$match" ]; then
  #echo "Invalid configuration value! Valid configuration value Example: \"RGB(100,100,100)\" or \"#aaaaaa\" or \"red\"" > /dev/null 1>&2
  echo "无效的配置值！有效的配置值示例：\"RGB(100,100,100)\" 或 \"#aaaaaa\" 或 \"red\"" > /dev/null 1>&2
  exit 1
fi

if [ ! -f "$config_file" ]; then
  #echo "File \"$config_file\" is not found!" > /dev/null 1>&2
  echo "文件 \"$config_file\" 未找到！" > /dev/null 1>&2
  exit 1
fi

if [ ! -w "$config_file" ]; then
  #echo "File \"$config_file\" can not be written!" > /dev/null 1>&2
  echo "文件 \"$config_file\" 不可写！" > /dev/null 1>&2
  exit 1
fi

regex="^background[[:space:]]*=[[:space:]]*".*"$"
current_val=`grep -i $regex $config_file`
if [ -z "$current_val" ]; then
  #echo "Configuration items \"background\" does not exist!" > /dev/null 1>&2
  echo "配置项 \"background\" 不存在!" > /dev/null 1>&2
  exit 1
else
  sed -i "s/$current_val/background = \"$new_val\";/g" $config_file
fi

# 对比新旧图片背景色，将老的背景色对应的缩略图删除 
declare -l str
declare -l new_val
str=`echo $current_val | awk -F= '{print $2}'` 
new_val=`echo $new_val | xargs`
if [ $str != $new_val ]; then
  find $image_root -type f -iregex ".*_[0-9]*x[0-9]*_5\..*" -exec rm {} \;
  #find $image_root -type f -iregex ".*_[0-9]*x[0-9]*_5\..*" | xargs rm
  $baseDirForScriptSelf/callback.sh
fi

echo "OK" 
exit 0
