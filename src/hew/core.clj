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

(defn owfs-ops []
  "OWFS ops for REPL use and development"
  (let [conn (owfs/connect (env :owfs-host) (env :owfs-port))
        temp (owfs/read-temp conn (env :owfs-sensor-id))]
    (println "Temp: " temp)))

(defn hue-ops []
  "Hue ops for REPL use and development"
  (let [host (env :hue-host)
        user-id (env :hue-user-id)
        sensor-id (env :hue-sensor-id)]
    (println "Lights: " (hue/lights host user-id))
    (if (sensor-id) (println "Temp: " (:temperature (hue/read-temperature host user-id sensor-id))))
    ;(println (hue/light-by-name host user-id "Bloom!"))
    ))

(defn read-and-update [conn]
  "Read temp and update the light"
  (let [hue-host (env :hue-host)
        hue-user-id (env :hue-user-id)
        temp (if (edn/read-string (env :use-owfs))
               (owfs/read-temp conn (env :owfs-sensor-id))
               (:temperature (hue/read-temperature hue-host hue-user-id (env :hue-sensor-id))))
        rgb (tempmap/to-color temp)
        xy (color/rgb-to-xy rgb)
        light-id (hue/light-by-name hue-host hue-user-id (env :hue-light-name))]
    (println "Updating light" light-id "with xy" xy "for temp:" temp)
    (hue/update-light-color! hue-host hue-user-id light-id xy)))

(defn -main
  "Main function, regularly polls the temperature and upate the light"
  [& args]
  (println "Init")
  (println "Use OWFS? " (env :use-owfs))

  (chime/chime-at
    (chime/periodic-seq (Instant/now) (Duration/ofMinutes 5))
    (fn [_] (read-and-update (owfs/connect (env :owfs-host) (edn/read-string (env :owfs-port))))))) ;TODO: Figure out which temp sensor to use here (OWFS vs Hue)

;(-main)