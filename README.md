# Popular Movies
An app that displays details about popular playing movies.<br />
Created as a part of Android Developer Nanodegree provided by Udacity.<br /><b>Note</b>: In order to run the app, add your own API key obtained from themoviedb.org to ```build.gradle:Module``` as
```
buildTypes.each{
        it.buildConfigField 'String', 'MOBDB_API_KEY',"\"API_KEY\""
    }
```
Replace API_KEY with your own API KEY.