#!/bin/bash

#
# Argument is any number of files which contains words.
# Each word is on the new line
#

if [ -f "noun.dict" ]
then
	rm noun.dict
fi

if [ -f "other.dict" ]
then
	rm other.dict
fi

while test ${#} -gt 0
do
	file_name=$1
	for word in $(cat $file_name)
	do

		count=`wn $word | grep "Information available for" | cut -d " " -f 4 | wc -l`
		if [ $count -eq 1 ]
		then
			pos=`wn $word | grep "Information available for" | cut -d " " -f 4`
			if [ $pos == "noun" ]
			then
				echo $word >> noun.dict
			else
				echo $word >> other.dict
			fi
		else
			echo $word >> other.dict
		fi
	done

	shift
done
