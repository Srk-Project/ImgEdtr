import numpy as np
import cv2 as cv

first_value=None
last_value=None
working_value=None

def getImg(data):
    global first_value,last_value
    np_data=np.frombuffer(data,np.uint8)
    img=cv.imdecode(np_data,cv.IMREAD_UNCHANGED)
    first_value=img
    last_value=img

def showOriginalImage():
    global last_value, working_value
    last_value=first_value
    working_value=None
    _,encoded_image=cv.imencode('.png',first_value)

    return encoded_image.tobytes()


def grayScale(progess,is_work_start):
    global working_value,last_value
    if is_work_start:
        working_value=last_value
    gray = np.empty(working_value.shape, dtype=np.uint8)
    gray[:, :, 0] = cv.cvtColor(working_value, cv.COLOR_BGR2GRAY)
    gray[:, :, 1] = gray[:, :, 0]
    gray[:, :, 2] = gray[:, :, 0]

    img_gray = cv.addWeighted(working_value, float(progess/10), gray, float(1-float(progess/10)), 0)
    #img_gray=cv.cvtColor(img,cv.COLOR_BGR2GRAY)
    last_value=img_gray
    _,encoded_image=cv.imencode('.png',img_gray)
    return encoded_image.tobytes()

def applyFilter(progress,is_work_start):
    global working_value,last_value
    if is_work_start:
        working_value=last_value
    """global last_value
    img=last_value
    if last_value is None:
        img=first_value"""
    kernel=np.ones((progress,progress),np.float32)/(progress*progress)
    filter_image=cv.filter2D(working_value,-1,kernel)
    last_value=filter_image
    _,encoded_value=cv.imencode('.png',filter_image)
    return encoded_value.tobytes()


def brightness(progress,is_work_start):
    global working_value, last_value
    if is_work_start:
        working_value=last_value

    hsv = cv.cvtColor(working_value, cv.COLOR_BGR2HSV)
    hsv[:, :, 2] = cv.multiply(hsv[:, :, 2], float(progress/10))
    brighter_img = cv.cvtColor(hsv, cv.COLOR_HSV2BGR)
    last_value=brighter_img
    _,encoded_img=cv.imencode('.png',brighter_img)
    return encoded_img.tobytes()

def hue(progress,is_work_start):
    global working_value, last_value
    if is_work_start:
        working_value=last_value
    hsv = cv.cvtColor(working_value, cv.COLOR_BGR2HSV)
    hsv[:, :, 0] = (hsv[:, :, 0] + progress)
    hued_img = cv.cvtColor(hsv, cv.COLOR_HSV2BGR)
    last_value=hued_img
    _,encoded_img=cv.imencode('.png',hued_img)
    return encoded_img.tobytes()

def saturation(progress,is_work_start):
    global working_value, last_value
    if is_work_start:
        working_value=last_value
    hsv = cv.cvtColor(working_value, cv.COLOR_BGR2HSV)
    hsv[:, :, 1] = np.minimum(hsv[:, :, 1]*(float(progress/10)), 255)
    saturated_img = cv.cvtColor(hsv, cv.COLOR_HSV2BGR)
    last_value=saturated_img
    _,encoded_img=cv.imencode('.png',saturated_img)
    return encoded_img.tobytes()


