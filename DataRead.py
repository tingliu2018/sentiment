# -*- coding: utf-8 -*-
import pandas as pd
import math
"""
@author Thomas Talasco
"""

def binarySearch(arr, size, word):
    L = 0
    R = size - 1
    while L <= R:
        mid = math.floor((L + R) / 2)
        if arr[mid] < word:
            L = mid + 1
        elif arr[mid] > word:
            R = mid - 1
        else:
            return mid
    return -1
    
def linearSearch(arr, word):
    for i in range(0, len(arr)):
        if arr[i] == word:
            return i
    return -1
        

# Read in data set
pdData = pd.read_csv('Scores.csv', header=0)
wordData = pdData.Word
valence = pdData.Valence
arousal = pdData.Arousal
dominance = pdData.Dominance

wordSetCount = [0 for i in range(20007)]# Count of words in pandas dataset
unknownWords = list()
unknownWordsCount = list()
file = open('testfile.txt', 'r') # open some file to read
outputKnown = open('outputKnown.txt', 'w') #output files for words
outputUnknown = open('outputUnknown.txt', 'w')

wordList = list() #creates list 
text = file.readlines()
for line in text:
    words = line.split()
    for word in words:
        wordList.append(word)
'''
 Carefully iterates over the list of words created from the data which was read 
 in from the csv file and then binary searches for the word in the data set
 if the word is in the data set then we use the index returned by binary
 search to increase the integer in our list of counts which correlates with the
 indexes of the words loaded in from the data set. If the word was not in the data
 set and therefore could not have its wordSetCount index updated then we have
 to do a linear search on the words that we've read so far which is not sorted
 to see if we have seen this unknown word before. If we have then we add the
 new unknown word to our ever expanding list of unknown words and then we also
 have to add 1 to the end of the count of unknown words which also has corresponding
 indices so that we keep both lists aligned correctly.
'''
for word in wordList:
    returnVal = binarySearch(wordData, len(wordData), word)
    if returnVal != -1:
        wordSetCount[returnVal] += 1
    else:
        index = linearSearch(unknownWords, word)
        if index != -1:
            unknownWordsCount[index] += 1
        else:
            unknownWords.append(word)
            unknownWordsCount.append(1)

i = 0
for word in wordData:
    outputKnown.write(word +" "+ str(wordSetCount[i]) +"\n")
    i += 1
    
i = 0
for word in unknownWords:
    outputUnknown.write(word + " "+ str(unknownWordsCount[i]) +"\n")
    i += 1
                
outputKnown.close()
outputUnknown.close()
            
        
    

    
    