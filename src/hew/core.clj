(ns hew.core
  (:require [settings :as s]
            [hew.owfs :as owfs])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [conn (owfs/connect s/default)
        temp (owfs/read-temp conn (:sensor-id s/default))]
    (println "Temp: " temp)
    (println "Type: " (class temp))))
(-main)