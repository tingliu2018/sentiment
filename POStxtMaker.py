# -*- coding: utf-8 -*-
"""
Created on Wed Jun 19 13:01:42 2019

@author: thomas
"""

# -*- coding: utf-8 -*-
"""
Created on Wed Jun 19 12:10:38 2019

@author: thomas
"""

import pandas as pd

def linearSearch(search, arr):
    for i in range(0, len(arr)):
        if arr[i] == search:
            return i
    return -1

pdData = pd.read_csv('betterbigbadfile.csv', header=0)
words = pdData.Word
ourscore = pdData.Our_Score
lexscore = pdData.Lex_Score
totalcount = pdData.Total_Count
awesomecount = pdData.Awesome_Count
goodcount = pdData.Good_Count
averagecount = pdData.Average_Count
poorcount = pdData.Poor_Count
awfulcount = pdData.Awful_Count

nounfile = open('/media/thomas/ESD-USB/Parts of Speech/Noun.txt', 'r')
verbfile = open('/media/thomas/ESD-USB/Parts of Speech/Verb.txt', 'r')
adjectivefile = open('/media/thomas/ESD-USB/Parts of Speech/Adjective.txt', 'r')
adverbfile = open('/media/thomas/ESD-USB/Parts of Speech/Adverb.txt', 'r')
nounchart = open('/media/thomas/ESD-USB/Parts of Speech/NounScores.txt', 'w')
verbchart = open('/media/thomas/ESD-USB/Parts of Speech/VerbScores.txt', 'w')
adjectivechart = open('/media/thomas/ESD-USB/Parts of Speech/AdjectiveScores.txt', 'w')
adverbchart = open('/media/thomas/ESD-USB/Parts of Speech/AdverbScores.txt', 'w')

nouns = nounfile.readlines()
verbs = verbfile.readlines()
adjectives = adjectivefile.readlines()
adverbs = adverbfile.readlines()

for i in range(0, len(nouns)):
    nouns[i] = nouns[i].rstrip()
    
for i in range(0, len(verbs)):
    verbs[i] = verbs[i].rstrip()
    
for i in range(0, len(adjectives)):
    adjectives[i] = adjectives[i].rstrip()
    
for i in range(0, len(adverbs)):
    adverbs[i] = adverbs[i].rstrip()

for i in range(0, len(words)):
    index = linearSearch(words[i], nouns)
    if index != -1:
        nounchart.write(str(words[i]) +" "+ str(ourscore[i]) +"\n")
    index = linearSearch(words[i], verbs)
    if index != -1:
        verbchart.write(str(words[i]) +" "+ str(ourscore[i]) +"\n")
    index = linearSearch(words[i], adjectives)
    if index != -1:
        adjectivechart.write(str(words[i]) +" "+ str(ourscore[i]) +"\n")
    index = linearSearch(words[i], adverbs)
    if index != -1:
        adverbchart.write(str(words[i]) +" "+ str(ourscore[i]) +"\n")
    
nounfile.close()
verbfile.close()
adjectivefile.close()
adverbfile.close()

nounchart.close()
verbchart.close()
adjectivechart.close()
adverbchart.close()