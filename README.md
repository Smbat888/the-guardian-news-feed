# News Feed

[![N|Solid](http://www.contactmagazine.net/wp-content/uploads/2018/01/guardian-logo-300x207.jpg)](https://open-platform.theguardian.com/)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

The simple Android News Feed application using the Guardian API. The following sections for this app are done:

  - Home Screen news feed list
  - News detailed info screen
  - Ability to pin news items to Home screen
  - Offline support

# Used technologies

  - Retrofit2 - for API requests
  - GSON/Retrofit2 GSON converter - for converting call resposne json to java models. 
  - Room persistance - for storing data in database
  - Andrid support library, design support, recycler view and card view - for UI design

### Installation/Running

For building and running application need to install the following components.
- Java 7 +
- Android Studio 3.0+

Open terminal and run the following commands to build application:

```sh
$ cd NewsFeed/
$ ./gradlew clean assembleRelease
```
or open application in Android Studio, sync gradle and run application on emulator or device you want.

### Todos

The following sections aren't done yet, so need to be done:

 - UI automation tests
 - Unit tests
 - Write MORE Tests

License
----

MIT
