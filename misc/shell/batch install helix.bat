::		one key clean install nebula-helix
::
::
::  helix is nebula child project,dependency on nebula-repo,and nebula-repo dependency nebula-utilities, 
::	if nebula-utilities or nebula-repo,has any code change,we need first step install nebula-utilities,second step install nebula-repo
::	At this time is more complicated, so we provide one key clean install nebula-helix function
::
::step1 base on nebula parent git update
::
::step2 double clike this bat file,will auto install parent project, 
::		and nebula-utilities,nebula-repo,nebula-helix
::		don't need youself install project-by-project
::
::--by feilong 2016-05-07

mode con cols=150 lines=5600

@echo off
rem batch install nebula-helix
title install nebula-helix

echo 
echo create by feilong
echo 2016-05-07

call "%~dp0/batchInstall.bat" "nebula-utilities,nebula-repo,nebula-helix"

pause