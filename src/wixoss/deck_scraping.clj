(ns wixoss.deck-scraping
(:use [hiccup.core]
        [clojure.pprint])
  (:require [net.cgrand.enlive-html :as html]
            [cemerick.url :as url]
            [clj-http.lite.client :as client]))


(defn get-deck
  [id]
  (let [resource
        (-> (clojure.java.io/reader
              (str "http://komekkun.com/wixoss-linkage/decks/" id))
            html/html-resource)]
    (map (fn [link]
           (let [l (-> link :content first)
                 parsed (first (re-seq #"([1-9])×(.+)" l))]
             (when parsed
               {:card-name (clojure.string/replace (nth parsed 2) #"　" " ")
                :card-count (Integer. (nth parsed 1))})))
         (html/select resource [:div#paste_deck_slidepanel :a]))))

(def all-deck
  (map get-deck
       (range 1 1108)))

(pprint all-deck)
