# -*- coding: utf-8 -*-
"""
Created on Fri Jul 12 12:05:59 2019

@author: Code Monkey A
"""

#Generate STree and SOStr based off of the current masterFile and parents.txt
#THIS CODE IS CALLED BY DatasetBuilder.java

directory = "/media/thomas/ESD-USB/dataset/" #Change this to where the dataset is stored

SOStr = open(directory + "SOStr.txt", "w")
STree = open(directory + "SOStr.txt", "w")
masterFile = open(directory + "masterFile.txt", "r")
parents = open(directory + "parents.txt", "r")

sentences = masterFile.readlines()

for sentence in sentences:
    line = sentence.replace(" ", "|")
    SOStr.write(line)
    
SOStr.close()

nums = parents.readlines()
for line in nums:
    newline = line.replace(" ", "|")
    while "||" in newline:
        newline = newline.replace("||", "|")
    STree.write(newline)
    
STree.close()
masterFile.close()
parents.close()
