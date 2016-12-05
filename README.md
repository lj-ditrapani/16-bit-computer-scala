LJD 16-bit Computer in Scala & JavaFX
-------------------------------------

Based on the LJD 16-bit Computer
[Specification](https://github.com/lj-ditrapani/16-bit-computer-specification).


Download
--------

<http://ditrapani.info/resources/ljd-16-bit-computer-1.0.0.jar>


Usage
-----

Start this program from the command line.

See the help text for usage [src/main/resources/help.txt](src/main/resources/help.txt)


Development
-----------

Style check

    sbt sacalstyle
    sbt test:sacalstyle

Run tests

    sbt test

Generate jar from source

    sbt assembly

jar can be found in target/scala-x.xx/ljd-16-bit-computer-x.x.x.jar
