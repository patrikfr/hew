(ns hew.core
  (:require [settings :as s]
            [hew.owfs :as owfs]
            [hew.hue :as hue]
            [hew.tempmap :as tempmap]
            [hew.hue-color :as color]
            [chime :as chime]
            [clj-time.core :as t]
            [clj-time.periodic :as tp])
  (:gen-class))

(defn owfs-ops []
  (let [owfs (:owfs s/default)
        conn (owfs/connect owfs)
        temp (owfs/read-temp conn (:sensor-id owfs))]
    (println "Temp: " temp)
    (println "Type: " (class temp))))

(defn hue-ops []
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
  "I don't do a whole lot ... yet."
  [& args]
  (println "Init")
  (let [x (promise)]

    (chime/chime-at
      (rest                                                 ; excludes *right now*
        (tp/periodic-seq (t/now) (-> 5 t/seconds)))
      (fn [time] (read-and-update (owfs/connect (:owfs s/default))))
      {:on-finished #(deliver x "Done!")})

    (deref x)))

(-main)