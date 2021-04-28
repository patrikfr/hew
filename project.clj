(defproject hew "0.1.0-SNAPSHOT"
  :description "Sets a Hue light to indicate temperature from Hue or 1-wire temperature sensor."
  :url "https://github.com/patrikfr/hew"
  :license {:name "The MIT License (MIT)"
            :url  "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.owfs/jowfsclient "1.2.6"]
                 [org.slf4j/slf4j-jdk14 "1.7.30"]
                 [clj-http "3.12.1"]
                 [cheshire "5.10.0"]
                 [jarohen/chime "0.3.2"]
                 [environ "1.2.0"]]
  :main ^:skip-aot hew.core
  :target-path "target/%s"
  :profiles {:uberjar      {:aot :all}
             :dev          [:project/dev :profiles/dev]
             :profiles/dev {}
             :project/dev  {:dependencies [[midje "1.9.10"]]
                            :plugins      [[lein-midje "3.2.1"]
                                           [lein-environ "1.2.0"]
                                           [lein-pprint "1.3.2"]]}}

  :uberjar-name "hew.jar")
