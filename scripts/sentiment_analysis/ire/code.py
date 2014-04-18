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

count_pos=0
count_neg=0

#positiveness or negativeness calculation for individual word done// can change to array of words by for word in array
f=open("x","r")
for line in f.readline():
	for word in line:
		print word
'''re.sub('[^A-Za-z]+', '',word)
d=enchant.Dict("en_US")
if d.check(word) is False:
	word=d.suggest(word)[0]
#word=porter2.stem(lower)
word=word.lower()
getStopWords()
if stop_list.has_key(word):
	print "NO"
else:
	getPositiveWords()	
	getNegativeWords()
	print word	
	if positive_list.has_key(word):
		count_pos=count_pos+1
	elif negative_list.has_key(word):
		count_neg=count_neg+1
print count_pos
print count_neg

#stem=porter2.stem(x)
'''
