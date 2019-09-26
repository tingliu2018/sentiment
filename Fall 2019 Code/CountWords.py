# -*- coding: utf-8 -*-
"""
@author Thomas Talasco, Yuwei Chen
"""

class Word:
    def __init__(self, word, count):
        self.word = word
        self.count = count
        
    def __eq__(self, word):
        return self.word == word
    
    def __lt__(self, word):
        return self.word < word
    
    def __gt__(self,word):
        return self.word > word
    
    def __ne__(self, word):
        return self.word != word
    
    def __le__(self, word):
        return self.word <= word
    
    def __ge__(self, word):
        return self.word >= word
        
    
def linearSearch(arr, word):
    for i in range(0, len(arr)):
        if arr[i].word == word:
            return int(i)
    return -1
        

# Read in data set
usbDir = "D:\\"
files = [usbDir +"FemaleAwesomeTags.txt",
         usbDir +"FemaleGoodTags.txt",
         usbDir +"FemaleAverageTags.txt",
         usbDir +"FemalePoorTags.txt",
         usbDir +"FemaleAwfulTags.txt",
         usbDir +"MaleAwesomeTags.txt",
         usbDir +"MaleGoodTags.txt",
         usbDir +"MaleAverageTags.txt",
         usbDir +"MalePoorTags.txt",
         usbDir +"MaleAwfulTags.txt"]
         

wordList = list() #creates list 

for file in files:
    inputFile = open(file, 'r', encoding="utf8")
    for line in inputFile:
        words = line.split()
        for word in words:
            index = linearSearch(wordList, word)
            if index == -1:
                wordList.append(Word(word, 1))
            else:
                wordList[index].count += 1

wordList.sort()

output = open(usbDir + "WordCount.csv", "w")
output.write("Word\tTotal Count")
for word in wordList:
    output.write(str(word.word) +"\t"+ str(word.count) +"\n")    
    
output.close()