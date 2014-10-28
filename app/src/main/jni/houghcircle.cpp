// this is from http://docs.opencv.org/doc/tutorials/imgproc/imgtrans/hough_circle/hough_circle.html


#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <iostream>
#include <stdio.h>
#include <android/log.h>
#include <sstream>
#include <string>
#include "design_software_little_1practice_circledetection_NativeCircleDetector.h"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"houghcircle.cpp",__VA_ARGS__)

using namespace cv;

/** @function main */
int detectCircles(Mat& src, vector<Vec3f>& circles)
{
    Mat src_gray;

    if( !src.data ) {
        return -1;
    }

    /// Convert it to gray
    cvtColor( src, src_gray, CV_BGR2GRAY );

    /// Reduce the noise so we avoid false circle detection
    GaussianBlur( src_gray, src_gray, Size(9, 9), 2, 2 );

    /// Apply the Hough Transform to find the circles
    HoughCircles( src_gray, circles, CV_HOUGH_GRADIENT, 1, src_gray.rows/8, 20, 50, 0, 0 );

    LOGI("Circle number: %d", circles.size());
    /// Draw the circles detected
    for( size_t i = 0; i < circles.size(); i++ ) {
        Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
        int radius = cvRound(circles[i][2]);
        // circle center
        circle( src, center, 3, Scalar(0,255,0), -1, 8, 0 );
        // circle outline
        circle( src, center, radius, Scalar(0,0,255), 3, 8, 0 );
    }



}


JNIEXPORT void JNICALL Java_design_software_little_1practice_circledetection_NativeCircleDetector_nativeDetect
 (JNIEnv * env, jclass clazz, jlong imgsrc)
{
    Mat& img = *(Mat*) imgsrc;
    vector<Vec3f> circles;
    detectCircles(img, circles);
}