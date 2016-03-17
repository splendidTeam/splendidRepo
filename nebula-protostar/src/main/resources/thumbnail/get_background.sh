#!/bin/bash

baseDirForScriptSelf=$(cd "$(dirname "$0")"; pwd)
source $baseDirForScriptSelf/config

if [ ! -f "$config_file" ]; then
  #echo "File \"$config_file\" is not found!" > /dev/null 1>&2
  echo "文件 \"$config_file\" 未找到！" > /dev/null 1>&2
  exit 1
fi

regex="^background[[:space:]]*=[[:space:]]*.*$"
config_value=`grep -i $regex $config_file`
if [ -z "$config_value" ]; then
  #echo "Configuration items \"background\" does not exist!" > /dev/null 1>&2
  echo "配置项 \"background\" 不存在！" > /dev/null 1>&2
  exit 1
else 
  echo "$config_value" | awk -F"\"" '{print $2}'
  exit 0
fi
