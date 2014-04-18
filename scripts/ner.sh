#!/bin/bash

#
#	Arg1 : is the possible candidat file
#		This file will ocntain the list of possible candidates
#		with their frequencies.
#	Arg2 : is the output file which will be created
#		Class : word : frequency
#


DELIM='<'
I_FILE="wn_output"

c_file=$1
o_file=$2

#rm -f /home/devilo/workspace/Articulate/files/articles/*

if [ -f $o_file ]
then
	rm $o_file
fi


function check_entity() {

	word_to_check=$1

	count=`wn $word_to_check | grep "Information available for" | cut -d " " -f 4 | wc -l`
	if [ $count -eq 1 ]
	then
		pos=`wn $word_to_check | grep "Information available for" | cut -d " " -f 4`
		if [ $pos == "noun" ]
		then
			return 0
		else
			return 1
		fi
	else
		return 1
	fi

}

function get_class() {

	if [ $line_num -eq -1 ]
	then
		echo "entity"
		return
	fi

	cur_line_no=1

	while read line
	do
		if [ $cur_line_no -gt $line_num ]
		then
			if [ "$line" == "=> entity" ]
			then
				break
			fi
			
		fi

		cur_line_no=`expr $cur_line_no + 1`

	done < $I_FILE

	cur_line_no=`expr $cur_line_no - 3`

	if [ $cur_line_no -lt $line_num ]
	then
		cur_line_no=`expr $cur_line_no + 3`
	fi

	class=`head -$cur_line_no $I_FILE | tail -1`

	echo $class | grep -Eq "=> whole, unit"
	if [ $? -eq 0 ]
	then
		cur_line_no=`expr $cur_line_no - 1`
		class=`head -$cur_line_no $I_FILE | tail -1`
	fi

	class=`echo $class | cut -d " " -f 2-`
	class=`echo $class | cut -d "," -f -1`
	echo $class

}

while read line
do

	ne=`echo $line | cut -d "$DELIM" -f 1`
	freq=`echo $line | cut -d "$DELIM" -f 2`

	check_entity $ne
	if [ $? -eq 1 ]
	then
		continue
	fi

	echo $ne | grep -Eq "[ ]"
	if [ $? -eq 0 ]
	then
		wn "$ne" -hypen > $I_FILE
	else
		wn $ne -hypen > $I_FILE
	fi
		
	line_num=`cat $I_FILE | grep -n "Sense 1" | head -1 | cut -d ":" -f 1`

	if [ -z $line_num ]
	then
		line_num=-1
	fi

	echo $ne#`get_class`#$freq >> $o_file
	#echo $ne >> $o_file

done < $c_file

echo $ne#`get_class`#$freq >> $o_file

rm $I_FILE
