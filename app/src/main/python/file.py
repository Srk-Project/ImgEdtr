import numpy as np
import cv2 as cv


first_value=None
last_value=None
working_value=None
fore=None
back=None


def getImg(data):
    global first_value,last_value
    np_data=np.frombuffer(data,np.uint8)
    img=cv.imdecode(np_data,cv.IMREAD_UNCHANGED)
    first_value=img
    last_value=img
    fore=None
    back=None

def showOriginalImage():
    global last_value, working_value
    last_value=first_value
    working_value=None
    _,encoded_image=cv.imencode('.png',first_value)

    return encoded_image.tobytes()


def grayScale(progess,is_work_start):
    global working_value,last_value,fore,back
    if is_work_start:
        working_value=last_value
    gray = np.empty(working_value.shape, dtype=np.uint8)
    gray[:, :, 0] = cv.cvtColor(working_value, cv.COLOR_BGR2GRAY)
    gray[:, :, 1] = gray[:, :, 0]
    gray[:, :, 2] = gray[:, :, 0]

    img_gray = cv.addWeighted(working_value, float(progess/10), gray, float(1-float(progess/10)), 0)
    #img_gray=cv.cvtColor(img,cv.COLOR_BGR2GRAY)
    last_value=img_gray
    fore=None
    back=None
    _,encoded_image=cv.imencode('.png',img_gray)
    return encoded_image.tobytes()

def applyFilter(progress,is_work_start):
    global working_value,last_value,fore,back
    if is_work_start:
        working_value=last_value
    """global last_value
    img=last_value
    if last_value is None:
        img=first_value"""
    kernel=np.ones((progress,progress),np.float32)/(progress*progress)
    filter_image=cv.filter2D(working_value,-1,kernel)
    last_value=filter_image
    fore=None
    back=None
    _,encoded_value=cv.imencode('.png',filter_image)
    return encoded_value.tobytes()


def brightness(progress,is_work_start):
    global working_value, last_value,fore,back
    if is_work_start:
        working_value=last_value

    hsv = cv.cvtColor(working_value, cv.COLOR_BGR2HSV)
    hsv[:, :, 2] = cv.multiply(hsv[:, :, 2], float(progress/10))
    brighter_img = cv.cvtColor(hsv, cv.COLOR_HSV2BGR)
    last_value=brighter_img
    fore=None
    back=None

    _,encoded_img=cv.imencode('.png',brighter_img)
    return encoded_img.tobytes()

def hue(progress,is_work_start):
    global working_value, last_value,fore,back
    if is_work_start:
        working_value=last_value
    hsv = cv.cvtColor(working_value, cv.COLOR_BGR2HSV)
    hsv[:, :, 0] = (hsv[:, :, 0] + progress)
    hued_img = cv.cvtColor(hsv, cv.COLOR_HSV2BGR)
    last_value=hued_img
    fore=None
    back=None
    _,encoded_img=cv.imencode('.png',hued_img)
    return encoded_img.tobytes()

def saturation(progress,is_work_start):
    global working_value, last_value,fore,back
    if is_work_start:
        working_value=last_value
    hsv = cv.cvtColor(working_value, cv.COLOR_BGR2HSV)
    hsv[:, :, 1] = np.minimum(hsv[:, :, 1]*(float(progress/10)), 255)
    saturated_img = cv.cvtColor(hsv, cv.COLOR_HSV2BGR)
    last_value=saturated_img
    fore=None
    back=None
    _,encoded_img=cv.imencode('.png',saturated_img)
    return encoded_img.tobytes()





#eye



def detectWhite(ROIhsv):
    kernel = np.ones((1 + ROIhsv.shape[1] // 50, 1 + ROIhsv.shape[1] // 50), np.uint8)
    v, s = 130, 40
    min_s = np.min(ROIhsv[:, :, 1])
    max_s = np.max(ROIhsv[:, :, 1])
    min_v = np.min(ROIhsv[:, :, 2])
    max_v = np.max(ROIhsv[:, :, 2])
    v = min_v + v * (255 - min_v) / (max_v - min_v)
    s = min_s + s * (255 - min_s) / (max_s - min_s)
    mask = cv.inRange(ROIhsv[:, :, 2], v, 255)
    mask2 = cv.inRange(ROIhsv[:, :, 1], 0, s)
    mask = cv.bitwise_and(mask, mask2)
    mask = cv.morphologyEx(mask, cv.MORPH_OPEN, kernel)
    return cv.morphologyEx(mask, cv.MORPH_CLOSE, kernel)



def findEyes(ROIhsv, white):
    contours, hierarchy = cv.findContours(white, cv.RETR_TREE, cv.CHAIN_APPROX_SIMPLE)
    cnt_img = cv.cvtColor(white, cv.COLOR_GRAY2BGR)
    mask = np.zeros(ROIhsv.shape, dtype=np.uint8)
    if len(contours) < 2:
        return cv.cvtColor(mask, cv.COLOR_BGR2GRAY)
    a = np.empty(len(contours), dtype=np.ushort)
    for i in range(len(contours)):
        x, y, w, h = cv.boundingRect(contours[i])
        a[i] = w * h
    m1 = np.argmax(a)
    a[m1] = 0
    m2 = np.argmax(a)
    for i in range(len(contours)):
        x, y, w, h = cv.boundingRect(contours[i])
        color = (255, 0, 0)
        if i == m1 or i == m2:
            color = (0, 0, 255)
        cv.rectangle(cnt_img, (x, y), (x + w, y + h), color, 1)
    r1 = cv.boundingRect(contours[m1])
    r2 = cv.boundingRect(contours[m2])
    if r1[0] > r2[0]:
        r2, r1 = r1, r2
    pts = np.array(
        [
            [r1[0] + r1[2], r1[1]],
            [r2[0], r2[1]],
            [r2[0] + r2[2] // 4, r2[1] + r2[3] // 2],
            [r2[0], r2[1] + r2[3]],
            [r1[0] + r1[2], r1[1] + r1[3]],
            [r1[0] + 3 * r1[2] // 4, r1[1] + r1[3] // 2],
        ],
        np.int32,
    )
    cv.fillConvexPoly(mask, pts, (0, 255, 0))
    mask = cv.cvtColor(mask, cv.COLOR_BGR2GRAY)
    mask = cv.bitwise_and(mask, cv.bitwise_not(white))
    cv.polylines(cnt_img, [pts], True, (0, 255, 255))
    return mask

def eyeColor(progress,is_work_start):
    global last_value,working_value,fore,back
    if is_work_start:
        working_value=last_value
    img=working_value
    eyes_cascade = cv.CascadeClassifier(cv.data.haarcascades + 'haarcascade_eye_tree_eyeglasses.xml')
    face_cascade = cv.CascadeClassifier(cv.data.haarcascades + 'haarcascade_frontalface_alt.xml')
    frame_gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    frame_gray = cv.equalizeHist(frame_gray)
    faces = face_cascade.detectMultiScale(frame_gray)
    eye_mask = np.zeros(img.shape[:2], dtype=np.uint8)
    fg=None
    bg=None
    if fore is None or back is None:
        assert len(faces) != 0
        for x, y, w, h in faces:
            faceROI = frame_gray[y : y + h, x : x + w]
            #cv.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2)
            #_,encoded_value=cv.imencode('.png',img)
            #return encoded_value.tobytes()
            eyes = eyes_cascade.detectMultiScale(faceROI)
            for x2, y2, w2, h2 in eyes:
                eyesROI = img[y + y2 + h2 // 4 : y + y2 + 3 * h2 // 4, x + x2 : x + x2 + w2]
                ROIhsv = cv.cvtColor(eyesROI, cv.COLOR_BGR2HSV)
                white = detectWhite(ROIhsv)
                eye = findEyes(ROIhsv, white)

                eye_mask[
                y + y2 + h2 // 4 : y + y2 + 3 * h2 // 4, x + x2 : x + x2 + w2
                ] = cv.bitwise_or(
                    eye_mask[y + y2 + h2 // 4 : y + y2 + 3 * h2 // 4, x + x2 : x + x2 + w2],
                    eye,
                )

        ret, eye_mask = cv.threshold(eye_mask, 10, 255, cv.THRESH_BINARY)

        inv_mask = cv.bitwise_not(eye_mask)
        bg = cv.bitwise_and(img, img, mask=inv_mask)
        fg = cv.bitwise_and(img, img, mask=eye_mask)
        fore=fg
        back=bg
    else:
        fg=fore
        bg=back


    fg = cv.cvtColor(fg, cv.COLOR_BGR2HSV)
    fg[:, :, 0] = progress
    fg[:, :, 1] = fg[:, :, 1] * 1.1

    fg = cv.add(fg, 0)

    fg = cv.cvtColor(fg, cv.COLOR_HSV2BGR)
    dst = cv.add(bg, fg)
    last_value=dst
    _,encoded_value=cv.imencode('.png',dst)
    return encoded_value.tobytes()


