(ns hew.hue
  (:require [clj-http.client :as c]))

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
