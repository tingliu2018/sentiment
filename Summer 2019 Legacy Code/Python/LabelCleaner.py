# -*- coding: utf-8 -*-
"""
Created on Fri Jul 12 10:38:09 2019

@author: Code Monkey A
"""

directory = "/home/thomas/Desktop/Research/treelstm-master/" #Change this to be the root of the treelstm repo

labels = [open(directory + "data/sst/test/labels.txt", 'r+'), open(directory + "data/sst/train/labels.txt", 'r+'), open(directory + "data/sst/dev/labels.txt", 'r+')]

lines = list()
for file in labels:
    string = file.readlines()
    for line in string:
        line = line.replace("None", "1")
        lines.append(line)
    file.seek(0)
    file.truncate()
    for line in lines:
        file.write(line)
    file.close()
    lines.clear()

labels = [open(directory + "data/sst/test/dlabels.txt", 'r+'), open(directory + "data/sst/train/dlabels.txt", 'r+'), open(directory + "data/sst/dev/dlabels.txt", 'r+')]

lines = list()
for file in labels:
    string = file.readlines()
    for line in string:
        line = line.replace("#", "-1")
        #line = line.replace("0", "-1")
        lines.append(line)
    file.seek(0)
    file.truncate()
    for line in lines:
        file.write(line)
    file.close()
    lines.clear()


