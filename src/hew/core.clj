(ns hew.core
  (:require
    [environ.core :refer [env]]
    [clojure.edn :as edn]
    [hew.owfs :as owfs]
    [hew.hue :as hue]
    [hew.tempmap :as tempmap]
    [hew.hue-color :as color]
    [chime.core :as chime])
  (:import (java.time Instant Duration))
  (:gen-class))

(defn update-light [temp hue-host hue-user-id hue-light-name]
  (let [rgb (tempmap/to-color temp)
        xy (color/rgb-to-xy rgb)
        light-id (hue/light-by-name hue-host hue-user-id hue-light-name)]
    (println "Updating light" light-id "with xy" xy "for temp:" temp)
    (hue/update-light-color! hue-host hue-user-id light-id xy)))

(defn read-temp
  "Read temp and update the light"
  ([hue-host hue-user-id hue-sensor-id]
   (:temperature (hue/read-temperature hue-host hue-user-id hue-sensor-id)))
  ([conn owfs-sensor-id]
   (owfs/read-temp conn owfs-sensor-id)))

(defn -main
  "Main function, regularly polls the temperature and update the light"
  [& args]
  (println "Init")
  (println "Use OWFS? " (env :use-owfs))

  (let [use-owfs (edn/read-string (env :use-owfs))
        owfs-host (env :owfs-host)
        owfs-port (edn/read-string (env :owfs-port))
        owfs-sensor-id (env :owfs-sensor-id)
        hue-host (env :hue-host)
        hue-user-id (env :hue-user-id)
        hue-light-name (env :hue-light-name)
        hue-sensor-id (env :hue-sensor-id)]

    (chime/chime-at
      (chime/periodic-seq (Instant/now) (Duration/ofMinutes 5))
      (fn [_] (let [temp (if use-owfs
                           (read-temp (owfs/connect owfs-host owfs-port) owfs-sensor-id)
                           (read-temp hue-host hue-user-id hue-sensor-id))]
                (update-light temp hue-host hue-user-id hue-light-name))))))
;(-main)




;;-- Utility functions for REPL and dev use

(defn owfs-ops []
  "OWFS ops for REPL use and development"
  (let [conn (owfs/connect (env :owfs-host) (edn/read-string (env :owfs-port)))
        temp (owfs/read-temp conn (env :owfs-sensor-id))]
    (println "Temp: " temp)))

(defn hue-ops []
  "Hue ops for REPL use and development"
  (let [host (env :hue-host)
        user-id (env :hue-user-id)
        sensor-id (env :hue-sensor-id)]
    (println "Lights: " (hue/lights host user-id))
    (if sensor-id (println "Temp: " (:temperature (hue/read-temperature host user-id sensor-id))))
    ;(println (hue/light-by-name host user-id "Bloom!"))
    ))