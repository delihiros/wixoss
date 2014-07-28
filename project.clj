(defproject wixoss "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.5"]
                 [com.cemerick/url "0.1.1"]
                 [clj-http-lite "0.2.0"]
                 [ring "1.3.0"]
                 [compojure "1.1.8"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler wixoss.core/app})

