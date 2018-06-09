# Cosmic Rays Detector App
An app that detects cosmic rays using Camera's CMOS sensor

## What are Cosmic Rays? 
Cosmic rays are energetic, subatomic particles that arrive from outside the Earth's atmosphere. 

## How does the app detect Cosmic Rays?
-> When a cosmic ray enters the CMOS sensor, the cosmic ray deposits a charge onto the sensor itself. If the sensor is not exposed to any sources of light, the cosmic ray will leave a track behind in the resulting image.  
[Source: More Info](http://via.library.depaul.edu/cgi/viewcontent.cgi?article=1021&context=ahac "American Institute of Aeronautics and Astronautics Report")

## The app
-> Runs the camera display and everytime a hit is found, saves the frame and uploads to Firebase Storage and Firestore which is then used to solve the mystery of Cosmic Rays/Particles
