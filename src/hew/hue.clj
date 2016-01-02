(ns hew.hue
  (:require [clj-http.client :as c]))

(defn light-by-name
  "Find a light's ID by name. Returns nil if no light with matching name is found."
  [host user-id light-name]
  (if-let [light (first
                   (filter
                     #(= light-name (:name (val %)))
                     (:body (c/get (str "http://" host "/api/" user-id "/lights/") {:as :json}))))]
    (name (key light))))