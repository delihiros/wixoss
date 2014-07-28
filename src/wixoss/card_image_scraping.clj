(ns wixoss.card-image-scraping
  (:use [clojure.pprint])
  (:require [net.cgrand.enlive-html :as html]
            [cemerick.url :as url]
            [clj-http.lite.client :as client]))


(defn- id->picture
  [id]
  (let [card
        (-> (clojure.java.io/reader
              (str "http://www.takaratomy.co.jp/products/wixoss/card/card_detail.php?id=" id))
            html/html-resource)]
    {:card-name
     (clojure.string/replace
       (first (clojure.string/split
                (let [card-name
                      (first (:content (first (html/select card [:h3]))))]
                  (if (string? card-name)
                    card-name ""))
                #"\n"))
       #"　" " ")
     :url
     (:src (:attrs (first (html/select card [:img]))))}))



(def all-card-pictures
  (filter #(not= "" (:card-name %))
          (pmap id->picture
                (range 1 500))))

(defn- scrape-picture
  [card]
  (let [res (client/get 
              (str "http://www.takaratomy.co.jp" (:url card))
              {:as :byte-array})
        data (:body res)
        filename (str "resources/public/img/"
                      (:card-name card) ".jpg")]
    (with-open [w (clojure.java.io/make-output-stream
                    filename {})]
      (.write w data))))

(defn scrape-all-pictures
  []
  (pmap scrape-picture
        all-card-pictures))
