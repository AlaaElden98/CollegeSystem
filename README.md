# Runtime Code (Kfs Arts)

## Problem description

1 - Recording desires

* Our project will solve a huge problem facing most students specially who joined faculty of arts.

* concerning selecting desired department process which takes about not less than month at the beginning of each year to distribute students using regular ways and announce the departments results, so it will certainly help reducing effort , cost and physical resources used nowadays .

* As well as Attendance recording feature which will help professors following up students presence at lectures in easier and faster way , which in regular may take hours through each term for every single course .  

2 - E-Exam practice

* sometimes a student needs to improve or test his knowledge in specific subject .

* but he can't find questions for testing his information. the app solved this problem by offering small exam for any subject on any chapter .

* and at the end of the exam student will receive score instantly.

3 - Recording attendance

* In any lecture student and doctor usually waste alot of time to record attendance.

* The app solved this problem by making attendance system using Machine learning kit from Firebase .

* Making QR for each single lecture, doctor can record and count number of students in a single minute.

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Project scope

* Our mobile application provides a lot of features helping students to get in touch with many aspects related to their faculty so it will be dispensable especially for  students who are organized to be enrolled in far faculty , where despite of using papers to enroll in certain department , student can only use their national id and seat number  pre-existed from the ministry of education system, then they can easily be confirmed to catch up the department they want .

* And through each term they will be regularly informed through that app ( from professor posts) with essential issues like any change in a specific schedule ,  grades reports , any warning for any student and so on .

* Concerning professors  in addition that it will help them provide essential student related news to them and follow up absence in a way that less prone to error by reducing human intervention , professors can also use it to test students by creating quiz on the app and on the other hand students could easily submit answers and get their grades.
And so for Student affairs office Administrators they can easily add,edit departments or students show all of them , and do so on .

* So as we mentioned our app  works on gathering all these college related work together in one consistent high performance system so that  the scope of the system will be bounded by certain users who include , students , professors and student's affair office administrators .

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------
## Presentation

[Download Presentation from here](https://drive.google.com/open?id=1Vh_A7dkR1FXMt1ksRsSrdwTgk8Q_LXAv)

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------
## App on google play (issues)

we are waiting for approval on google play

<img alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74095378-4e0c5f80-4af8-11ea-88e8-4975b660b313.JPG">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Prerequisites

* First Download [Android studio](https://developer.android.com/studio) .
* Make sure you have [Google](https://myaccount.google.com/?tab=kk) account.
* Make project on firebase.

## Setup steps With firebase

1 -  First of all you need google-services.json. Create a Firebase project in the [Firebase console](https://console.firebase.google.com/u/0/), if you don't already have one. Go to your project and click ‘Add Firebase to your Android app’. Follow the setup steps. At the end, you'll download a google-services.json file which you should add to your project.

![32899277-30da3374-caf3-11e7-86e0-58cb1bfd59e2](https://user-images.githubusercontent.com/48976562/73993419-cc32ff80-495a-11ea-9b0a-c1de7c4a4a4d.png)

2 - Setup realtime database. In firebase console go to DEVELOP->Database-> Get Started -> choose tab ‘RULES’ and paste this:

{
  
  "rules":

    {
  
        ".read": "true",
  
        ".write": "true"
  
     }

}

3 - If you haven't yet specified your app's SHA-1 fingerprint, do so from the [Setting page](https://console.firebase.google.com/u/0/project/_/settings/general/) of the Firebase console. See [Authenticating](https://developers.google.com/android/guides/client-auth) Your Client for details on how to get your app's SHA-1 fingerprint.

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Database

To ensure you can use our app .. you must download standard data .

it's a data that given from Ministry of education . this data contains national ID and seating number for each student .

you can download this file and put on firebase runtime as a standard data .

[Download database from here](https://drive.google.com/drive/folders/1-ek67gnb6vaAB-a-PbLogiadsCZ11Yjy?usp=sharing) .. this is a json file you can import that in firebase directly.

![importDatabase](https://user-images.githubusercontent.com/48976562/74079765-89e5ed00-4a44-11ea-8709-833733ec81cd.JPG)

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

# Blog Application

## navigation component And ViewModel (Architecture competition ) (Stable)

* our app was built in one activity and alot of fragments.

* this is our graph

* using viewModel .

![Capture](https://user-images.githubusercontent.com/48976562/74080648-f1089f00-4a4e-11ea-9250-59cd5616bbeb.JPG)

### our app split into three parts (Admin , Doctors , Student) (Stable)

    - Before explain anything we will be shown how to Login the app as (Admin , doctor , student)
    - After importing the database you will find nationl ID and password for each of them 

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084913-94bc7400-4a7c-11ea-803c-404b959ebba5.jpg">|<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084922-a3a32680-4a7c-11ea-9b7c-47dcd50d6014.jpg">|<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084932-b289d900-4a7c-11ea-8c78-882f26854efe.jpg">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------
## Home Screen (Stable)

The Home Screen shows posts that doctors added and the latest news.

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74095240-bb1ef580-4af6-11ea-8a1c-a1ade4e966e2.jpg">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

# 1 - Authorities of the admin part (Stable)

After login as admin

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74080793-7ccefb00-4a50-11ea-9247-56f949791f81.jpg">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Adds, delete and edit departments (Stable)

### add department screen contain (Stable)

1 -  add department name .

2 - department capacity (number of student in this department).

3 - min total (minimum number of total degree) .

4 - min special (it's an optional field , the condition of degree in specific subject < the degree that student had) .

5 - description of this subject that student should know.

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084275-39878300-4a76-11ea-8570-2fd0008ef688.png">

### delete and edit department (Stable)

1 - show all departments

2 - admin can delete or edit a department

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084524-a3089100-4a78-11ea-8730-14659ea9db7f.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Adds, delete and edit subjects (Stable)

### add subject screen contain (Stable)

1 - choose level of this subject

2 - choose department

3 - choose doctor

4 - add subject name

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084287-56bc5180-4a76-11ea-920b-69ac63aa3ddf.png">

### edit or delete subject screen contain (Stable)

1 - in the first screen choose level and department

2 - in the second screen showing all subjects in this level and department , and admin can delete any subject

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084727-6a69b700-4a7a-11ea-96ed-92d93eb75f27.png">|<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084802-596d7580-4a7b-11ea-914f-ae9511217bfe.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Adds, delete and edit professors (Stable)

### add new doctor (Stable)

1 - doctor name .

2 - national ID .

3 - doctor password .

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084841-b1a47780-4a7b-11ea-9a95-465fee51fa1f.png">

### edit and  delete doctor (Stable)

-- in this section admin can show all doctor and has access to delete or edit information.

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084852-d39dfa00-4a7b-11ea-801a-75521146fed1.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

### Sees all students, and delete any student left the collage (Stable)

-- in this screen all students have been accepted in arts will be shown

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084864-0647f280-4a7c-11ea-88c7-dfc2786ecda9.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

# 2 - Authorities of the doctors part (Stable)

After login as a doctor

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084877-34c5cd80-4a7c-11ea-995f-6c828e01adab.jpg">

## add new post (Stable)

1 - doctor name .

2 - write subject name

3 - write what is the topic or information

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085173-fbdb2800-4a7e-11ea-924b-9bf0f13b106f.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Add new Question screen contain (Stable)

1 - in the first screen choose level , department , subject found in database , choose chapter

2 - in the second screen he can add question with all choices and the correct answer

all this information will be fetched from database when doctor make exam practice to student

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085226-950a3e80-4a7f-11ea-8bf4-30c297cb3e18.png">|<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085234-a5bab480-4a7f-11ea-914c-cb7df56e42c7.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Show Subjects (Stable)

1 - in the first screen choose level and department

2 - in the second screen showing all subjects in this level and department

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74084727-6a69b700-4a7a-11ea-96ed-92d93eb75f27.png">|<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085336-c3d4e480-4a80-11ea-879e-a6f37bd13e83.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Make Exam (Stable)

1 - in this screen doctor will choose level , department , and the subject he needed to create an exam for students

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085363-f7b00a00-4a80-11ea-885a-45e2b60bd078.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Student attendance (Stable)

-- in this screen doctor can show how many students attended his lecture , this is by barcode helper

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085437-98062e80-4a81-11ea-9e44-d8eaa0123ed5.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

# 3 - Authorities of the student part (Stable)

After login as a student

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085488-0f3bc280-4a82-11ea-805f-7d62c0097fe6.jpg">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Recording desires and choose department (Stable)

* this is the most important part of the application.

1 - using dynamic recyclerview that allows user to drag and drop , UP and Down to arrange his desires by modern way.

2 - Before clicking the button we fetch his degree at high school And choose the appropriate department.

3 - after this when he clicking the same button , we send him to fragment contain information about his department.

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085540-79546780-4a82-11ea-8421-c2c2620a0b4f.jpg">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## QR to Record attendance (Stable but need some improve)

1 - Every doctor possesses QR for his subject with a unique number.

2 - QR like pic in the following screen.

3 - this process can take just a second to finish , and saves a lot of time

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085652-fdf3b580-4a83-11ea-8320-1416107fc919.png">|<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085689-5e82f280-4a84-11ea-9d8d-7e796395697f.png">

-------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------

## Student can take small exam (Stable)

* This exam as a practice for each subject and chapter

* first of all a student chooses the subject , then our app fetches data from database and checks whether the exam is available by the doctor or not.

* then the questions will be shown in order , and after finishing he got his points .

<img width="250" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085774-5d05fa00-4a85-11ea-8552-71ac2c967537.jpg"> | <img width="230" alt="portfolio_view" src="https://user-images.githubusercontent.com/48976562/74085807-a0606880-4a85-11ea-9363-94bab9d50eb6.png">
