<p align="center"><a href="https://github.com/btmyles/RunSmart/tree/master"><img src="/app/src/main/res/mipmap-xxxhdpi/panda_ic_launcher_round.png" width="150"></a></p>

<h2 align="center"><b>RunSmart</b></h2>
<h4 align="center">A privacy-respecting GPS tracker for outdoor fitness.</h4>

<p align="center"><a href="https://play.google.com/store/apps/details?id=com.cs2063.runsmart"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" alt="Download on Google Play" height="100"></a></p>

<p align="center">
<a href="https://github.com/btmyles/RunSmart/releases" alt="GitHub release"><img src="https://img.shields.io/github/release/btmyles/RunSmart.svg" ></a>
<a href="https://www.gnu.org/licenses/gpl-3.0" alt="License: GPLv3"><img src="https://img.shields.io/badge/License-GPL%20v3-blue.svg"></a>
</p>

## Table of Contents

  - [Screenshots](#screenshots)
  - [Description](#description)
  - [Features](#features)
  - [Battery](#battery)
  - [Privacy](#privacy)
  - [Installation](#installation)
  - [How it Works](#how-it-works)
  - [License](#license)

### Screenshots

<p><img src="/screenshots/StartRun.png" width="150">
   <img src="/screenshots/History.png" width="150">
   <img src="/screenshots/Detail.png" width="150">
   <img src="/screenshots/Map.png" width="150">
   <img src="/screenshots/Articles.png" width="150"></p>

### Description

RunSmart is a tool designed to help novice runners achieve their next goals. It provides users a simple, and effective way of tracking their runs and measuring their progress without the need for a wearable fitness tracker.

### Features

- GPS tracking even while the app is closed
- Timer recording the duration of the run
- Map to display running routes using MapBox
- Run log showing previous run data
- Notes section for details about a completed run
- Recommended articles for novice and experienced runners

### Battery

- GPS significantly reduces battery life
- If the battery is below 25% when starting a run, GPS location is accessed half as often

### Privacy

- RunSmart does not require internet permissions
- Network location is not used. GPS only
- All data is stored locally and is never uploaded online

### Installation
- RunSmart can be installed on any android phone or tablet with Android 8.0 Oreo and above.
- Install from the [Google Play Store](https://play.google.com/store/apps/details?id=com.cs2063.runsmart).
- Download and install from the GitHub [releases](https://github.com/btmyles/RunSmart/releases).

#### Permissions
- GPS fine location

### How it Works

#### Track your Run
- When you open the app, you will see a blue “Run” button. Pressing that button will begin tracking your location for a new run session.
- After you press the button, the app will access your current location. The button will turn yellow while the app communicates with your GPS. 
Note: you will need to enable GPS permissions for this feature to work.
- Once your GPS has been successfully accessed, the button will turn Red with the text “Finish”. While this button is displayed, the app will access your location in real time as you run.
- When you are finished running, press the red Finish button. The app will then display the statistics from your run. You will be able to see your run time, distance, average pace, and you will have the option to see a map of your route.
- Pressing the Open Route button will display a map with your running route highlighted in blue. The map is interactive, and you can zoom and pan across the map.
- There is space on your run summary for you to write custom notes if desired.

#### View your Running History
- After opening the app, select the History tab on the bottom navigation view. 
- A list will appear containing high level information of your past runs: date, start time, distance, and duration.
- To access more information about your run, click on the desired list item.
- The app will display your run time, distance, average pace, notes, and a button to view your route.
- There is space on this screen to write custom notes if desired.
- Pressing the Open Route button will display a map with your running route highlighted in blue. The map is interactive, and you can zoom and pan across the map.


#### Find Related Articles
- After opening the app, select Articles on the bottom navigation view.
- There are links to various fitness-related articles. Clicking a link will prompt you to choose a browser in which to view the article.
- After selecting a browser, that browser will launch and display the article.


### License
[![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)](http://www.gnu.org/licenses/gpl-3.0.en.html)  

RunSmart is Free Software: You can use, study, share, and improve it at your
will. Specifically you can redistribute and/or modify it under the terms of the
[GNU General Public License](https://www.gnu.org/licenses/gpl.html) as
published by the Free Software Foundation, version 3 of the License.
