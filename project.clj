(defproject btc-trading "0.1.0-SNAPSHOT"
  :description "A simple btc-trading application."
  :url "https://github.com/gilmaso/btc-trading"
  :license {:name "GNU GPL V3"
            :url "http://www.gnu.org/licenses/gpl.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [cheshire "5.2.0"]
                 [http-kit "2.1.13"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.clojure/math.numeric-tower "0.0.2"]]
  :main ^:skip-aot btc-trading.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
