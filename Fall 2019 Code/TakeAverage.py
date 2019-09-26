# -*- coding: utf-8 -*-
"""
Created on Mon Sep 16 13:10:49 2019

@author: thomas
"""

import pandas as pd
usbDir = "/media/thomas/ESD-USB/"
pdData = pd.read_csv(usbDir +'WordCount.csv', header=0)
wordData = pdData.Count

total = 0
for num in wordData:
    total += int(num)
    
mean = total/len(wordData)
print(str(mean))