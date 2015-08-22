Building Aluminum consists of two steps: obtaining the source code and building a distribution archive.

## Obtaining the source code ##

The source code is hosted in a [Mercurial](http://mercurial.selenic.com/) repository. This means that you'll need a Mercurial client to get the sources. You can use the following command to get the most recent sources:

> _hg clone http://aluminumproject.googlecode.com/hg aluminum_

If you would rather not rely on the tip, you can get a specific version with the following command (please replace TAG with the version number you're interested in):

> _hg clone -r TAG http://aluminumproject.googlecode.com/hg aluminum_

## Building a distribution archive ##

The build tool of our choice is [Gradle](http://www.gradle.org/). To build a distribution archive, first go to the `aluminum` directory that contains the sources. Then use the following command:

> _gradlew createDistribution_

(On a Unix-based system, you might have to use _./gradlew_ or _sh gradlew_.)

After Gradle is done, you can find the distribution archive in the `build/distributions` directory.