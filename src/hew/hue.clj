(ns hew.hue
  (:require [clj-http.client :as c]
            [clojure.instant :as i]
            [cheshire.core :as json]))

(defn url [host user-id]
  (str "http://" host "/api/" user-id))

(defn light-by-name
  "Find a light's ID by name. Returns nil if no light with matching name is found."
  [host user-id light-name]
  (if-let [light (first
                   (filter
                     #(= light-name (:name (val %)))
                     (:body (c/get (str (url host user-id) "/lights/") {:as :json}))))]
    (name (key light))))

(defn lights
  "List all lights and their names"
  [host user-id]
  (let [response (:body (c/get (str (url host user-id) "/lights/") {:as :json}))]
    (map #(vector (name (key %)) (:name (val %))) response)))

(defn sensors
  "List all sensors and their names"
  [host user-id]
  (let [response (:body (c/get (str (url host user-id) "/sensors/") {:as :json}))]
    (map #(vector (name (key %)) (:name (val %))) response)))

(defn read-temperature
  "Retrieve state for a given temperature sensor"
  [host user-id sensor-id]
  (let [state (:state (:body (c/get (str (url host user-id) "/sensors/" sensor-id) {:as :json})))]
    (if-not (every? #(contains? state %) [:lastupdated, :temperature])
      (throw (Exception. (str "Id does not reference a temperature sensor: " sensor-id))))
    (->
      state
      (update :lastupdated i/read-instant-date)
      (update :temperature / 100.0))))

(defn light-state [host user-id light-id]
  "Retrieve state for a given light id"
  (:state (:body (c/get (str (url host user-id) "/lights/" light-id) {:as :json}))))

(defn update-light! [host user-id light-id new-state]
  "Update given light's state"
  (c/put
    (str (url host user-id) "/lights/" light-id "/state")
    {:body (json/generate-string new-state)}))

(defn update-light-color! [host user-id light-id xy]
  "Set given light's color using the supplied xy values"
  (update-light! host user-id light-id {:xy xy}))