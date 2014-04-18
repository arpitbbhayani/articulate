from twython import TwythonStreamer
import time
import os

streamTime = 0
tweetList = []
startTime = 0

#class objects for tweet-feeding
class MyStreamer(TwythonStreamer):
	def on_success(self, data):
		global tweetList
		global startTime
		if 'text' in data:
			print data['text'].encode('utf-8')
			tweetList.append(data['text'].encode('utf-8'))
		else :
			print 'no-data'
		if time.time() - startTime > streamTime :
			self.disconnect()

	def on_error(self, status_code, data):
		print status_code

	def on_timeout():
		self.disconnect()

def readFile(fileName):
	"""Reads file line by line
	   stores in a list and returns"""

	ret = []
	f = open(fileName, 'r')
	f = f.readlines()
	for lines in f :
		ret.append(lines.split('\n')[0])
	return ret

def main():

	#initializing tokens and keys
	ret = readFile('config.txt')
	global streamTime
	global tweetList
	global startTime

	consumer_key = ret[0]
	consumer_secret = ret[1]
	access_token = ret[2]
	access_token_secret = ret[3]
	folderPath = ret[4]
	streamTime = int(ret[5])
	fileNames = []
	articles = []

	if folderPath[-1] != '/':
		folderPath += '/'
	

	stream = MyStreamer(consumer_key, 
				consumer_secret, access_token,
				access_token_secret)
	

	filterStr = ""
	for files in os.listdir(folderPath):
		fileNames.append(files)
		ret = readFile(folderPath + files)
	
		articles.append([])
		for entity in ret:
			filterStr = filterStr + entity + ','
			articles[-1].append(entity)
	#print "Article is",articles
	#print "File is",fileNames
	#print "FilterStr is",filterStr
	
	""" Streaming is done for 
	    'streamTime' duration """

	startTime = time.time()
	stream.statuses.filter(track=filterStr)

	print tweetList


if __name__ == "__main__" : 
	main()
