ProGuard, please
================
Manual ProGuard configuration is not a pleasant task. It requires a lot of time, patience and strong Google-fu to search dark corners of Internet for magic incantations that make ProGuard happy and don't screw up your build. A bottle of alcoholic beverage also comes in handy.

I just want to say "ProGuard this for me, please". And that's what this plugin strives to do.

Usage
=====
This plugin is not available yet in on Maven Central, so right now you have to build it yourself and install it into your local Maven repo:

```
git clone https://github.com/chalup/proguard-please.git
cd proguard-please
gradle install
```

And add the following configuration to your `build.gradle`:

```groovy
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath 'org.chalup:proguard-please:0.1'
    }
}

apply plugin: 'com.android.application'
apply plugin: 'proguard-please'
```

How Does It Work?
=================
On the first run (or after calling `gradle proguardPleaseInit`) the plugin clones the [proguard-please-repo](https://github.com/chalup/proguard-please-repo) and adds `proguard-please.properties` file to your app sources. You should add this file to your version control system to ensure reproducible builds.

Before ProGuard task in build process, the plugin scans your compile dependencies and searches proguard-please-repo for ProGuard configuration for each declared and transitive dependency.

To fetch the latest set of ProGuard configs and bump your `proguard-please.properties` call:

```
gradle proguardPleaseUpdate
```

"ProGuard configuration for foo:bar:1.0 is missing"
===================================================
Sadly, the plugin doesn't work by pure magic. I went for the next best thing, i.e. crowdsourcing. The plugin relies on configuration from [proguard-please-repo repository](https://github.com/chalup/proguard-please-repo). If you use some library that wasn't added yet, and you have a working ProGuard configuration, be a good citizen and send the Pull Request.

Credits
=======
I used [android-apt](https://bitbucket.org/hvisser/android-apt) plugin by [Hugo Visser](https://twitter.com/botteaap) as a base for development.

License
=======

    Copyright (C) 2014 Jerzy Chalupski

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.