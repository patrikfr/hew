(ns hew.core
  (:require [settings :as s]
            [hew.owfs :as owfs]
            [hew.hue :as hue]
            [hew.tempmap :as tempmap]
            [hew.hue-color :as color]
            [chime.core :as chime])
  (:import (java.time Instant Duration))
  (:gen-class))

(defn owfs-ops []
  "OWFS ops for REPL use and development"
  (let [owfs (:owfs s/default)
        conn (owfs/connect owfs)
        temp (owfs/read-temp conn (:sensor-id owfs))]
    (println "Temp: " temp)
    (println "Type: " (class temp))))

(defn hue-ops []
  "Hue ops for REPL use and development"
  (let [host (:host (:hue s/default))
        user-id (:user-id (:hue s/default))]
    (println (hue/lights host user-id))
    ;(println (hue/light-by-name host user-id "Bloom!"))
    ))

(defn read-and-update [conn]
  "Read temp and update the light"
  (let [temp (owfs/read-temp conn (get-in s/default [:owfs :sensor-id]))
        rgb (tempmap/to-color temp)
        xy (color/rgb-to-xy rgb)
        hue (:hue s/default)
        light-id (hue/light-by-name (:host hue) (:user-id hue) (:light-name hue))]
    (println "Updating light" light-id "with xy" xy "for temp:" temp)
    (hue/update-light-color! (:host hue) (:user-id hue) light-id xy)))

(defn -main
  "Main function, regularly polls the temperature and upate the light"
  [& args]
  (println "Init")

  (chime/chime-at
    (chime/periodic-seq (Instant/now) (Duration/ofMinutes 5))
    (fn [_] (read-and-update (owfs/connect (:owfs s/default))))))

;(-main)