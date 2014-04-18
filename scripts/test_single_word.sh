#!/bin/bash

#
# Argument is any number of files which contains words.
# Each word is on the new line
#

word=$1

count=`wn $word | grep "Information available for" | cut -d " " -f 4 | wc -l`
if [ $count -eq 1 ]
then
	pos=`wn $word | grep "Information available for" | cut -d " " -f 4`
	if [ $pos == "noun" ]
	then
		echo Noun
	else
		echo Other1
	fi
else
	echo Other2
fi
