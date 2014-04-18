import sys 
import porter2
import re
import enchant
import pytrie
import os
import operator
from collections import OrderedDict
global stop_list
global positive_list
global negative_list
global entity_list


def readFile(fileName):
        """Reads file line by line
           stores in a list and returns"""

        ret = []
        f = open(fileName, 'r')
        f = f.readlines()
        for lines in f :
                ret.append(lines.split('\n')[0])
        return ret

def getStopWords():
	f=open("/home/devilo/workspace/Articulate/scripts/sentiment_analysis/finalstoplist","r")
	global stop_list
	stop_list=pytrie.StringTrie()
	l=re.split('[\s+]',f.read())
	for i in l:
		stop_list[i]=0
	f.close() 

def getPositiveWords():
	f=open("/home/devilo/workspace/Articulate/scripts/sentiment_analysis/positive.txt","r")
	global positive_list
	positive_list=pytrie.StringTrie()
	l=re.split('[\s+]',f.read())
	for i in l:
		positive_list[i]=0
	f.close() 

def getNegativeWords():
	f=open("/home/devilo/workspace/Articulate/scripts/sentiment_analysis/negative.txt","r")
	global negative_list
	negative_list=pytrie.StringTrie()
	l=re.split('[\s+]',f.read())
	for i in l:
		negative_list[i]=0
	f.close() 
def getEntities():
	f=open("articles/entity.txt","r")
	global entity_list
	entity_list=pytrie.StringTrie()
	l=re.split('[\s+]',f.read())
	for i in l:
		entity_list[i]=0
	f.close() 

global count_pos
global count_neg


#positiveness or negativeness calculation for individual word done// can change to array of words by for word in array
getStopWords()
getPositiveWords()	
getNegativeWords()
folderPath="/home/devilo/workspace/Articulate/files/articles/"
fileNames = []
articles = []

for files in os.listdir(folderPath):
         fileNames.append(files)
         ret = readFile(folderPath + files)
            
         articles.append([])
         for entity in ret:
			for ett in entity.split():
				re.sub('[^A-Za-z]+','',ett)
				ett.replace("#","")
				ett=ett.lower()
				articles[-1].append(ett)
#print articles
#getEntities()
final_articles_score=[]
fd={}
it=0
for entity_list in articles:
	it+=1
	ent={}
	with open('/home/devilo/workspace/Articulate/files/x','r') as f:
	    for line in f:
	        for word in line.split():
			re.sub('[^A-Za-z]+', '',word)
			word=word.lower()
			#print word
			if word in entity_list:
				if word in ent:
					ent[word].append(line)
				else:
					ent[word]=[line]
				break

	#print ent

	num=0
	score=0
	pos_entity=0
	neg_entity=0
	article_tweets=0
	for i in ent:
	    num=num+1
	    pos=0
	    neg=0
	    tot=0
	    for line in ent[i]:
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
	    if tot==0:
		s=0
	    else:
  	    	s=(1.0*(pos-neg))/(1.0*tot)
	    if s>0:
		pos_entity+=1
	    else:
		neg_entity+=1
	    article_tweets+=tot
  	    #minfactor=(0.5*min(pos,neg))
  	    score=score+abs(s)
	    #print "NOTHING----",line
	score+=(min(pos_entity,neg_entity)*0.5)
	if num==0:
		print "NUM IS 0"
		final_score=0
	else:
		final_score=(1.0*score)/(1.0*num)
	final_score*=article_tweets
	fd[it]=final_score
	#final_articles_score.append(final_score)
	#print "Final is",final_score

sorted_x = sorted(fd.iteritems(), key=operator.itemgetter(1))
fffinal=reversed(sorted_x)
for i in fffinal:
	print fileNames[i[0]-1],i[1]
'''ls=list(reversed(sorted(fd.keys())))
for i in ls:
	print fileNames[i-1],fd[i]'''
#print fd
'''print "Pos is",pos
print "Neg is",neg
score=(1.0*(pos-neg))/(1.0*tot)
minfactor=min(pos,neg)
print "Small added is",minfactor
print score
#print summation of (scores + 0.5*minfactor) divided by number of entities 
'''
