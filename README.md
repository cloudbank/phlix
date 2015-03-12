# My Friend Flickr v 1.0

An android client for logging in to Flickr which allows you to view photos from:

* Your own and your friends' photostreams
* The 'interestingness for today' collection
* Custom search result. Search includes OR, AND, and TEXT options
* 
![flickr](/../screenshots/screenshots/flickr_app.png?raw=true "")

In addition you can:

* View and add photo comments
* Take your own pictures with the camera! 



Built tumblrsnap as a part of the [Android CodePath Bootcamp](http://thecodepath.com/androidbootcamp). Released on the [Google Play Store]

<img src="http://i.imgur.com/zeFmmYm.png" height="545" />
&nbsp;&nbsp;
<img src="http://i.imgur.com/GlXlQ57.png" height="545" />

## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

## Building

The build requires [Gradle](http://www.gradleware.com/)
v1.6 and the [Android SDK](http://developer.android.com/sdk/index.html)
to be installed in your development environment. In addition you'll need to set
the `ANDROID_HOME` environment variable to the location of your SDK:

    export ANDROID_HOME=/opt/tools/android-sdk

After satisfying those requirements, the build is pretty simple:

* Run `gradle assemble` from the root directory to build the APK only
* Run `gradle build` from the root directory to build the app and also run
  the integration tests, this requires a connected Android device or running
  emulator.

## Acknowledgements

This project uses the [Flickr API] [https://www.flickr.com/services/api/]

It also uses many other open source libraries such as:

 * [CodePath Rest-Client-Template](https://github.com/thecodepath/android-rest-client-template)
 * [scribe-java](https://github.com/fernandezpablo85/scribe-java)
 * [Android Async HTTP](https://github.com/loopj/android-async-http)
 * [codepath-oauth](https://github.com/thecodepath/android-oauth-handler)
 * [UniversalImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader)
 * [ActiveAndroid](https://github.com/pardom/ActiveAndroid)
 * [simple-crop-image]
 * [Gradle](https://github.com/gradle/gradle)




