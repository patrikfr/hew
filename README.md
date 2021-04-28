# hew

Hew is small Clojure program to make a Philips Hue light emit a color based on (or hew to, if you wish) a temperature reading.

In the current version it can acquire temperature readings from either:
* An [OWFS server](https://owfs.org/) with a [1-wire](https://en.wikipedia.org/wiki/1-Wire) temperature sensor, *or*
* A Philips Hue Motion sensor, such as [Outdoor sensor](https://www.philips-hue.com/en-us/p/hue-outdoor-sensor/046677541736), as these are equipped with a temperature sensor in addition to the motion and ambient light sensors.

The temperature is read, and the light is updated, every five minutes. The Hue temperature sensor seems to be sending updates to the bridge every five minute while OWFS readings are generally instant.

Here is short writeup: https://medium.com/@weakreference/shorts-or-jeans-f2f1783dc7a4

![lein workflow](https://github.com/patrikfr/hew/actions/workflows/lein.yml/badge.svg)
 
## What you need

### A Philips Hue lighting system
The [Philips Hue](https://www.philips-hue.com/) lighting system connects LED bulbs and other light sources and accessories using ZigBee to your home network using a Hue bridge. The bridge connects to your network using Ethernet and exposes a RESTful HTTP interface which is used by _hew_ to control the lights. Note that not all Hue products need a Hue bridge, but a bridge is required for _hew_ to work. _hew_ needs to be able to access the bridge over the local network, remote access is not supported.

You need one bulb/lamp/lightstrip capable of emitting multi-colored light, and, unless you use 1-Wire, a Hue motion sensor for measuring the temperature.
 
### Optionally: A 1-Wire temperature sensor network
[1-Wire](https://en.wikipedia.org/wiki/1-Wire) is a device communications bus system which can be used to connect temperature sensors among other things. _Hew_ expects temperature readings to be sourced from a 1-Wire sensor net by use of [1-Wire File System, OWFS](http://www.owfs.org). Specifically, _hew_ expects an OWFS server available to which it can connect and obtain temperature readings. 

### A JDK and Leiningen
Hew is written in Clojure. To build and run it you'll need a [current JDK](https://adoptopenjdk.net/) and [Leiningen](https://leiningen.org/).

## Configuration
Configuration is provided to the application using environment variables:

### Using a Hue motion sensor for obtaining temperature readings:
```
USE_OWFS=false 
HUE_HOST=<ip of Hue bridge> 
HUE_USER_ID=<Hue bridge user id, see below> 
HUE_LIGHT_NAME=<Name of Hue light to control, as seen in the Hue App> 
HUE_SENSOR_ID=<ID of the Hue temperature sensor, see below>
```

### Using an OWFS server for obtaining temperature readings:
```
USE_OWFS=true
OWFS_HOST=<owfs host/ip>
OWFS_PORT=<owfs port> 
HUE_HOST=<ip of Hue bridge> 
HUE_USER_ID=<Hue bridge user id, see below> 
HUE_LIGHT_NAME=<Name of Hue light to control, as seen in the Hue App> 
```

### Obtaining a Hue Bridge User ID
If you have not done so, you need to create a user for the Hue bridge by following the instructions on the [Philips Hue developer site](https://developers.meethue.com/develop/get-started-2/).

### Obtaining the ID of the Hue temperature sensor
The temperature sensing capability of the Hue motion sensors is somewhat of a hidden feature. It doesn't show up in the Hue app (but is supported by some third-party systems, such as Apple's HomeKit), and in the Hue API it is exposed as a separate sensor that requires a bit of digging to find: 

Issue an HTTP GET request to `http://<hue-bridge-address>/api/<hue-bridge-user-id>/sensors` and look for sensors of _type_ `ZLLTemperature`. The ID to look for is the associated int, i.e. `79` in this example response fragment:

```json
[...]
 }
  },
  "79": {
    "state": {
      "temperature": 832,
      "lastupdated": "2021-04-28T08:07:45"
    },
    "swupdate": {
      "state": "noupdates",
      "lastinstall": "2020-11-08T13:45:47"
    },
    "config": {
      "on": true,
      "battery": 100,
      "reachable": true,
      "alert": "none",
      "ledindication": false,
      "usertest": false,
      "pending": []
    },
    "name": "Hue outdoor temp. sensor 1",
    "type": "ZLLTemperature",
    "modelid": "SML002",
    "manufacturername": "Signify Netherlands B.V.",
[...]
```

The following [jq](https://stedolan.github.io/jq/) expression lists all temperature sensor IDs known to the bridge, along with the latest captured temperature:  
```shell
curl http://<hue-bridge-address>/api/<hue-bridge-user-id>/sensors \
| jq '[. |  to_entries[] | select ( .value.type | contains("ZLLTemperature")) | {id: .key, temperature: (.value.state.temperature / 100), lastupdated: .value.state.lastupdated}]'
```

## Clone, build and run

Clone the Git repository and build using [Leiningen](http://leiningen.org):

```shell
lein uberjar
```

    
Set required environment variables and run:
```shell
USE_OWFS=false HUE_HOST=192.168.0.1 HUE_USER_ID=hue-user-id HUE_LIGHT_NAME=Bloom HUE_SENSOR_ID=79 java -jar target/uberjar/hew.jar 
```    

Copyright © 2016-2021 Patrik Fredriksson

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
