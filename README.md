# hew

Hew is a project making a Philips Hue light emit a color based on (or hew to, if you wish) a temperature reading.

In the current version it requires a running OWFS server providing temp info, but this could easily be replace by anouther source or web feed of temperature readings.

Here is short writup: https://medium.com/@weakreference/shorts-or-jeans-f2f1783dc7a4
 
## What you need:

### A Philips Hue lighting system
The [Philips Hue](http://www.meethue.com) lighting system connects a number of LED bulbs and other light sources using ZigBee and a control bridge connected to your home network using Ethernet. The bridge exposes a RESTful HTTP interface which is used by hew to control the lights.
 
### A 1-Wire temperature sensor network
[1-Wire](https://en.wikipedia.org/wiki/1-Wire) is a device communications bus system which can be used to connect temperature sensors among other things. Hew expects temperature readings to be sourced from a 1-Wire sensor net by use of [1-Wire File System, OWFS](http://www.owfs.org). Specifically, hew expects an OWFS server available to which it can connect and obtain temperature readings. 

### A JDK
Hew is written in Clojure. To build and run it you'll need a current JDK.

## Settings

Edit `settings.clj` to include the host and port info of the OWFS server, as well as the ID of the sensor to read. Also add the user-id and host for the Hue bridge, and the name of the light to control (the name is the one set in the Hue mobile app).

If you have not done so, you need to create a user for the Hue bridge by following the instructions on the [Philips Hue developer site](https://developers.meethue.com/documentation/getting-started).

## Clone, build and run

Clone the Git repository and build using [Leiningen](http://leiningen.org):

    $ lein uberjar
    
In `target/uberjar` run:
    
    $ java -jar hew-0.1.0-SNAPSHOT-standalone.jar

Copyright © 2016-2017 Patrik Fredriksson

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
