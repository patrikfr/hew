(ns hew.hue-color
  (:require [clojure.math.numeric-tower :as m]))

(defn normalize-to-one [color]
  (map #(/ % 255) color))

(defn gamma-correct [nrgb-color]
  (map
    #(if (> % 0.04045)
      (m/expt (/ (+ % 0.055) 1.055) 2.4)
      (/ % 12.92))
    nrgb-color))

(defn nrgb-to-xyz [[r g b]]
  ;Wide gamut conversion D65
  [(+ (* r 0.664511) (* g 0.154324) (* b 0.162028))
   (+ (* r 0.283881) (* g 0.668433) (* b 0.047685))
   (+ (* r 0.000088) (* g 0.072310) (* b 0.986039))])

(defn xyz-to-xy [[x y z]]
  (let [sum (+ x y z)]
    (if (> sum 0)
      [(/ x sum) (/ y sum)]
      [0 0])))

(defn rgb-to-xy
  "Convert RGB (0-255) value to Hue xy-space."
  [color]
  (-> color
      normalize-to-one
      gamma-correct
      nrgb-to-xyz
      xyz-to-xy))

;http://www.developers.meethue.com/documentation/color-conversions-rgb-xy