#!/bin/bash

if [ $# -lt 1 ] ; then
  #echo "Configuration value is missing!" > /dev/null 1>&2
  echo "配置值丢失！" > /dev/null 1>&2
  exit 1
fi

baseDirForScriptSelf=$(cd "$(dirname "$0")"; pwd)
source $baseDirForScriptSelf/config

new_val=${1// /}

match=`echo "$new_val" | egrep -i "^([a-z]+,)*[a-z]+$"`

if [[ -z "$match" ]]; then
  #echo "Invalid configuration value! Valid configuration value Example: \"jpg,png,bmp\"" > /dev/null 1>&2
  echo "无效的配置值！有效的配置值示例：\"jpg,png,bmp\"" > /dev/null 1>&2
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

new_arry=(${new_val//,/ })
replace_val=""
len=${#new_arry[@]}

for i in $(seq $len); do
  replace_val="${replace_val}\"${new_arry[i-1]}\""
  if [ $i != $len ]; then
    replace_val="${replace_val},"
  fi
done

regexp="^image_ext[[:space:]]*=[[:space:]]*.*$"
current_val=`grep -i $regexp $config_file`
if [ -z "$current_val" ]; then
  #echo "Configuration items \"background\" does not exist!" > /dev/null 1>&2
  echo "配置项 \"background\" 不存在!" > /dev/null 1>&2
  exit 1
else
  sed -i "s/$current_val/image_ext = {$replace_val};/g" $config_file
fi

# 对比新旧图片格式，将弃用的图片格式对应的缩略图删除 
str=`echo $current_val | awk -F"[}{]" '{print $2}'`
str=${str//\"/}
current_arry=(${str//,/ })
new_arry=(${new_val//,/ })
for ext in ${current_arry[@]}; do
  exists=`echo "${new_arry[@]}" | grep -wq "$ext" &&  echo "Yes" || echo "No"`
  if [ $exists = "No" ]; then
    ext=`echo $ext | xargs`
    find $image_root -type f -regex ".*\..*_[0-9]+x[0-9]+_[0-9]+\.$ext" -exec rm {} \;
    $baseDirForScriptSelf/callback.sh
  fi
done

echo "OK"
exit 0
