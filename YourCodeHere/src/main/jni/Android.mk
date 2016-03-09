LOCAL_PATH := $(call my_dir)

include $(CLEAR_VARS)

LOCAL_MODULE := "DistProc"
LOCAL_SRC_FILES := proc.cpp

include $(BUILD_SHARED_LIBRARY)
