import pandas as pd

class Word:
    def __init__(self, word, count):
        self.word = word
        self.count = count
    
def linearSearch(arr, word):
    for i in range(0, len(arr)):
        if arr[i].word == word:
            return int(i)
    return -1
    
usbDir = "/media/thomas/ESD-USB/"

pdData = pd.read_csv(usbDir +'CSV/adjectiveWords.csv', header=0)

wordData = pdData.Word

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
         
words = list()
totalcommentCounter = 0

commentCounts = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

for i in range(0, len(files)):
    currentFile = open(files[i], 'r')
    for line in currentFile:
        totalcommentCounter += 1
        commentCounts[i] += 1
        for word in wordData:
            if word in line:
                index = linearSearch(words, word)
                if index != -1:
                    words[index].count = words[index].count + 1
                    #print("Old Word "+ word +" at index "+ str(index) +" which is "+ words[index].word)
                else:
                    words.append(Word(word, 1))
                    #print("New Word "+ word)

output = open(usbDir +"BigHappyFiles.txt", 'w')
otherOut = open(usbDir + "BigUnHappyFile.txt", 'w')
output.write("Total Comments: "+ str(totalcommentCounter) +"\n")
for i in range(0, len(files)):
    output.write(str(files[i]) +" "+ str(commentCounts[i]))
    
for word in words:
    if word.count >= totalcommentCounter/2:
        output.write(str(word.word) +" "+ str(word.count)+"\n")
    else:
        otherOut.write(str(word.word) +" "+ str(word.count)+"\n")             

output.close()
otherOut.close()