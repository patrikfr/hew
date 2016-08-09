(ns hew.core
  (:require [settings :as s]
            [hew.owfs :as owfs]
            [hew.hue :as hue]
            [chime :as chime]
            [clj-time.core :as t]
            [clj-time.periodic :as tp])
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


;(hue-ops)

(defn main-loop []
  (println "Init")
  (let [x (promise)]

    (chime/chime-at
      (rest                                                 ; excludes *right now*
        (tp/periodic-seq (t/now) (-> 5 t/seconds)))
      (fn [time] (println "Chiming at" time))
      {:on-finished #(deliver x "Done!")})

    (deref x)))
