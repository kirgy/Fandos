# Fandos Android Wear App
![fandos-mockup](https://cloud.githubusercontent.com/assets/3931356/7699513/546a7be4-fe10-11e4-984f-cd5ba60fa505.jpg)

## Introduction
>A Nandos diner makes a purchase against their registered loyalty card, the user gets notified of their updated loyalty reward status instantly on their watch.

Fandos is a Nandos fan-app, demonstrating the engagement potential for restaurant and retail businesses ultilising wearable tech in addition to their traditional marketing means.

## Getting Started
This process does require some patience, determination and a lot of coffee. Once you have the hang of it, it will be easy. Tweet @ChrisMcKirgan for assitance.
>1. Clone the repo
2. In android studio, go to "File->Import Project", navigate to the imported repo, select the Android Project directory and click "Import"
3. Once imported open "Mobile/Java/com.nybblemouse.fandos/MainActivity.java", and edit the value of the String "PROJECT_NUMBER" to your generated Google API project number. You can obtain his by creating a project (with Google Cloud Messenger enabled) on Google Console: https://console.developers.google.com
4. Run the project as normal. You will need a real cabled mobile device as well as the Wear emulator, and you will need to forward port 5601 between devices using the Android Debug Bridge: *adb -d forward tcp:5601 tcp:5601*
5. After running the mobile application, checking the Android Studio console, take the registered GCM value and add this to the server codes' GCM id list. This allows the server-side code to broadcast to the mobile devices.
6. Run the server-side code. Your wearable app should launch.

##Project Files
The project consists of documents, designs, server code and the Android Studio project. 

### Designs
The designs contain various PSDs of the design used in the project.
### Android Project
The Android Studio project consists of a typical Android Wear project, this contains all the code for the application, *except* the API settings which need to be added to the project.
### Server Code
The server code is some shot PHP code which needs to be placed on the processing server. This server communicates with the Google Cloud Messaging server, which in turn broadcasts the message to the mobile application.
### Documents
The documents contain some DFD's of the process as well as a PDF presentation which presented the commuinication process to the South West Mobile user group in Bristol, UK on 2015/05/19.

## Credits
Both Android and server-side code by @kirgy using various open source code.
Design by @netotaku
Server Code adapted from work by Matt Grundy

## Copyright
Nandos is a registered copyright of Nandos. Some of the visual assets within the Android Studio project as well as the PSD files may share similar likeness to that of Nandos material and Nandos may hold copyright ownership of such material. This repo is intended for education purposes only, and should not be used for any commerical gain. All the code in this repo has been created by Christopher McKirgan and is open source under the MIT license - please do share and modify remeber to reference the original author.