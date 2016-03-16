import matplotlib.pyplot as plt
from PIL import Image, ImageDraw
import math

# def EliminateArrayNoise(Array, SegmentSize):
# 	NumberOfSegments = int(math.floor(len(Array) / float(SegmentSize)))
# 	#SegmentSize = int(math.floor(len(Array) / float(NumberOfSegments)))
# 	# makes an array with all of the points in it
# 	Points = [[SegmentSize * SegmentNumber, Array[SegmentSize * SegmentNumber]] for SegmentNumber in range(0, NumberOfSegments)]
# 	# adds the last element manually because of rounding
# 	Points.append([len(Array) - 1, Array[len(Array) - 1]])

# 	ReturnArray = []

# 	for i in range(0, len(Points) - 1):
# 		Slope = float(Points[i][1] - Points[i + 1][1]) / float(Points[i][0] - Points[i + 1][0])

# 		y = Points[i][1]
# 		for x in range(Points[i][0], Points[i + 1][0]):
# 			# multiply by slope becasue we always change x by 1
# 			y += Slope
# 			ReturnArray.append(y)


# 	# add one last item beacuse it wasn't covered
# 	ReturnArray.append(Points[len(Points) - 1][1])

# 	return ReturnArray

def FindGreatestPoint(Array):
	GreatestValue = 0
	GreatestValueIndex = 0

	for i in range(0, len(Array)):
		if Array[i] > GreatestValue:
			GreatestValueIndex = i
			GreatestValue = Array[i]

	return GreatestValueIndex

def SmoothArray(Array, SampleSize):
	ReturnArray = []

	for i in range(0, len(Array) - SampleSize + 1):
		ReturnArray.append(0)
		for p in range(0, SampleSize):
			ReturnArray[i] += float(Array[i + p]) / float(SampleSize)
	return ReturnArray

# open the text image
TextImage = Image.open("proc.jpg")
# convert it to a grayscale image and remove alpha
TextImage = TextImage.convert('RGB')
# load the pixels
TextPixels = TextImage.load()

BlueArray = []
RedArray = []

BandSize = .5

for x in range (0, TextImage.size[0]):
	BlueSum = 0
	RedSum = 0
	for y in range(0, int(TextImage.size[1] * BandSize)):
		BlueSum += TextPixels[x, y][2] / float(TextImage.size[1])
		RedSum += TextPixels[x, y][0] / float(TextImage.size[1])

	BlueArray.append(BlueSum)
	RedArray.append(RedSum)

ProcessedRedArray = SmoothArray(BlueArray, 15)
ProcessedBlueArray = SmoothArray(RedArray, 15)

GreatestBluePoint = FindGreatestPoint(ProcessedBlueArray)
GreatestRedPoint = FindGreatestPoint(ProcessedRedArray)

Red = plt.plot(ProcessedRedArray)
Blue = plt.plot(ProcessedBlueArray)

print "greatest blue", GreatestBluePoint, "greatest red", GreatestRedPoint

plt.setp(Blue, color='b', linewidth=2.0)
plt.setp(Red, color='r', linewidth=2.0)

plt.ylabel('pixel value at height')
plt.show()