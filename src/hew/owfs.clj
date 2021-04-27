(ns hew.owfs
  (:require [clojure.edn :as edn])
  (:import [org.owfs.jowfsclient OwfsConnectionFactory]))

(defn connect
  "Create a new thread-safe connection to OW Server using supplied connection spec:  e.g. (connect {:host \"localhost\", :port 2048}"
  [host port]
  (.createNewConnection (OwfsConnectionFactory. host (int port))))

(defn read-temp [ow-conn sensor-id]
  (let [path (str "/" sensor-id "/temperature")
        raw-temp (.read ow-conn path)]
    (edn/read-string raw-temp)))



