(ns hew.hue
  (:require [clj-http.client :as c]
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

(defn light-state [host user-id light-id]
  (:body (c/get (str (url host user-id) "/lights/" light-id) {:as :json})))

(defn update-light! [host user-id light-id new-state]
  (c/put
    (str (url host user-id) "/lights/" light-id "/state")
    {:body (json/generate-string new-state)}))

(defn update-light-color! [host user-id light-id xy]
  (update-light! host user-id light-id {:xy xy}))