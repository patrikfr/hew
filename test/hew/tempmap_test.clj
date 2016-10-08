(ns hew.tempmap-test
  (:use midje.sweet)
  (:require [hew.tempmap :as t]))

(fact "Mapping temp (in °C) to RGB values"
      (t/to-color 10.5) => [255, 229, 0]
      (t/to-color 10.0) => [255, 229, 0]
      (t/to-color 9.0) => [222, 221, 38]
      (t/to-color 9.9) => [222, 221, 38]

      (t/to-color -7) => [161, 218, 248]
      (t/to-color -5) => [193, 235, 251]
      (t/to-color -9.9) => [161, 218, 248]
      (t/to-color -10) => [161, 218, 248]
      (t/to-color -10.1) => [122, 193, 227])

