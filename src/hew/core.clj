(ns hew.core
  (:require [settings :as s]
            [hew.owfs :as owfs]
            [hew.hue :as hue])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [owfs (:owfs s/default)
        conn (owfs/connect owfs)
        temp (owfs/read-temp conn (:sensor-id owfs))]
    (println "Temp: " temp)
    (println "Type: " (class temp))))

;(-main)

(defn hue-ops []
  (let [host (:host (:hue s/default))
        user-id (:user-id (:hue s/default))]
    (println (hue/lights host user-id))
    ;(println (hue/light-by-name host user-id "Bloom!"))
    ))


(hue-ops)