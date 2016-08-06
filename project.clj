(defproject hew "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License (MIT)"
            :url  "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.owfs/jowfsclient "1.2.6"]
                 [clj-http "2.2.0"]
                 [cheshire "5.6.3"]]
  :main ^:skip-aot hew.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
