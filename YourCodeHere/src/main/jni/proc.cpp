//
// Created by Matthew Kelsey on 3/5/2016.
//

#include <C:/opencv/build/include/opencv2/core/core.hpp>
#include <C:/opencv/build/include/opencv2/highgui/highgui.hpp>
#include <iostream>

using namespace cv;


int main(int argc, char** argv)
{

	Mat image;
	image = imread("C://Users/SAAS Student/Pictures/Odyssey Pics/DSC_0074 - Copy.JPG", CV_LOAD_IMAGE_COLOR);   // Read the file

	if (!image.data)                              // Check for invalid input
	{
		std::cout << "Could not open or find the image" << std::endl;
		return -1;
	}

	namedWindow("Display window", WINDOW_AUTOSIZE);// Create a window for display.
	imshow("Display window", image);                   // Show our image inside it.

	waitKey(0);                                          // Wait for a keystroke in the window
	return 0;
}
