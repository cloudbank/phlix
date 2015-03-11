# My Friend Flickr

An android client for logging in to Flickr which allows you to view photos from:

* Your own and your friends' photostreams
* The 'interestingness for today' collection
* Custom search result. Search includes OR, AND, and TEXT options
* 
![flickr](/../screenshots/screenshots/flickr_app.png?raw=true "")

In addition you can:

* View and add photo comments
* Take your own pictures with the camera! 
* Pull the action bar down to refresh your data






##TECHNICAL DETAILS

Makes use of a viewpager which swaps out fragments for the pages.  Implements oauth authentication for login to Flickr.  Caches photos to db with activeandroid.  Implements endless scrolling for listviews and pull to refresh library for up-to-date data.  REST networking calls run asynchronously in bg thread a la asynchttpclient.  *My final  project at Codepath bootcamp*.  A work ever in progress.

Libraries included:

* activeandroid 
* simple-crop-image
* scribe
* asynchttpclient
* universal image loader
