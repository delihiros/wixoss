(ns wixoss.scraping
  (:use [hiccup.core]
        [clojure.pprint])
  (:require [net.cgrand.enlive-html :as html]
            [cemerick.url :as url]
            [clj-http.lite.client :as client]))

(def home
  (-> (clojure.java.io/reader "http://wixoss.81.la" :encoding "EUC-JP")
      html/html-resource))

(def card-list
  (-> (clojure.java.io/reader "http://wixoss.81.la/?%A5%AB%A1%BC%A5%C9%A5%EA%A5%B9%A5%C8" :encoding "EUC-JP")
      html/html-resource))

(def pack-list
  (html/select card-list
               [:html :body :tr :td :div#body :ul :li :a]))

(def pack-list-simplified
  (map
    (fn [pack]
      {:pack-name (-> pack :content)
       :url (-> pack :attrs :href)})
    pack-list))

(defn simplify-page
  [resource]
  (html/select resource
               [:html :body :table :tbody]))

(def all-cards
  (distinct
    (apply concat
           (map (fn [pack]
                  (re-seq #"《[^》]*》" 
                          (slurp (:url pack) :encoding "EUC-JP")))
                pack-list-simplified))))

(defn card->page
  [card-name]
  (clojure.java.io/reader
    (str "http://wixoss.81.la/?"
         (clojure.string/replace (java.net.URLEncoder/encode card-name "EUC-JP") #"\+" "%20"))
    :encoding "EUC-JP"))

(defn get-grow
  [card-name]
  (let [links
        (-> (card->page card-name)
            html/html-resource
            (html/select [:div#body :table :a]))]
    (map #(-> % :content first) links)))

(defn get-links
  [card-name]
  (let [links
        (-> (card->page card-name)
            html/html-resource
            (html/select [:div#body :ul :li :a]))]
    (filter
      (fn [link]
        (re-seq #"《[^》]*》" link))
    (map #(-> % :content first) links))))

(def all-cards-with-info
  (pmap (fn [card-name]
          {:card-name card-name
           :grow (get-grow card-name)
           :links (get-links card-name)})
        all-cards))

