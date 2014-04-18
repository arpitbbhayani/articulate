import sys 
import porter2
import re
import enchant
import pytrie
from collections import OrderedDict
global stop_list
global positive_list
global negative_list

def getStopWords():
	f=open("finalstoplist","r")
	global stop_list
	stop_list=pytrie.StringTrie()
	l=re.split('[\s+]',f.read())
	for i in l:
		stop_list[i]=0
	f.close() 

def getPositiveWords():
	f=open("positive.txt","r")
	global positive_list
	positive_list=pytrie.StringTrie()
	l=re.split('[\s+]',f.read())
	for i in l:
		positive_list[i]=0
	f.close() 

def getNegativeWords():
	f=open("negative.txt","r")
	global negative_list
	negative_list=pytrie.StringTrie()
	l=re.split('[\s+]',f.read())
	for i in l:
		negative_list[i]=0
	f.close() 


global count_pos
global count_neg


#positiveness or negativeness calculation for individual word done// can change to array of words by for word in array
getStopWords()
getPositiveWords()	
getNegativeWords()
with open('x','r') as f:
    pos=0
    neg=0
    tot=0
    for line in f:
    	tot=tot+1
    	count_pos=0
	count_neg=0
        for word in line.split():
		re.sub('[^A-Za-z]+', '',word)
		word=word.lower()
		d=enchant.Dict("en_US")
		if d.check(word) is False:
			l=d.suggest(word)
			if len(l)==0:
				continue
			word=l[0]
		if stop_list.has_key(word):
			continue
		else:
			#print word	
			if positive_list.has_key(word):
				count_pos=count_pos+1
			elif negative_list.has_key(word):
				count_neg=count_neg+1
	if count_pos>count_neg:
	   pos=pos+1
	   #print "POSITIVE---",line
	elif count_neg>count_pos:
	   neg=neg+1
	   #print "NEGATIVE---",line
	#else:
	   
	   #print "NOTHING----",line
print "Pos is",pos
print "Neg is",neg
score=(1.0*(pos-neg))/(1.0*tot)
minfactor=min(pos,neg)
print "Small added is",minfactor
print score
#print summation of (scores + 0.5*minfactor) divided by number of entities 

