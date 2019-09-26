inputFile = open("KnownScores.txt", 'r')
outputFile = open("CleanedScores.txt", 'w')
text = inputFile.readlines()
for line in text:
	score = float(line.split()[1])
	if score != 0.0:
		outputFile.write(line.split()[0] +" "+ str(score) +"\n")
inputFile.close()
outputFile.close()


