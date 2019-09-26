import pandas as pd

class Word:
    def __init__(self, word, count):
        self.word = word
        self.count = count
        self.ratingCounts = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        self.combinedCounts = list()
        
    def updateCount(self,index):
        self.ratingCounts[index] += 1
    
    def combineCounts(self):
        for i in range(0,5):
            self.combinedCounts.append(self.ratingCounts[i] + self.ratingCounts[i+5])
        print(str(len(self.combinedCounts)))
    
def linearSearch(arr, word):
    for i in range(0, len(arr)):
        if arr[i].word == word:
            return int(i)
    return -1
    
usbDir = "/media/thomas/ESD-USB/"

pdData = pd.read_csv(usbDir +'testLexicon.csv', header=0)

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
        commentCounts[i] += 1
        for word in wordData:
            if word in line:
                index = linearSearch(words, word)
                if index != -1:
                    words[index].count = words[index].count + 1
                    words[index].updateCount(i)
                    #print("Old Word "+ word +" at index "+ str(index) +" which is "+ words[index].word)
                else:
                    words.append(Word(word, 1))
                    words[index].updateCount(i)
                    #print("New Word "+ word)
                    
output = open(usbDir + "ratioCounts.txt", 'w')

output.write("Word\tTotalCount\tAwesomeCount\tGoodCount\tAverageCount\tPoorCount\tAwfulCount\n")
for word in words:
    output.write(str(word.word) +"\t"+ str(word.count)+" ") 
    word.combineCounts()
    for count in word.combinedCounts:
        output.write(str(count)+"\t")
    output.write("\n")
output.close()
