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


def grayScale():
    global last_value
    img=last_value
    if last_value is None:
        img=first_value
    if len(img.shape)== 2:
        _,encoded_image=cv.imencode('.png',img)
        return encoded_image.tobytes()
    img_gray=cv.cvtColor(img,cv.COLOR_BGR2GRAY)
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