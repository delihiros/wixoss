(ns wixoss.core
  (:use [compojure.core]
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as response]))

(defroutes main-routes
  (GET "/" []
       (response/resource-response "public/index.html"))
  (route/resources "/")
  (route/not-found "404 not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)))
