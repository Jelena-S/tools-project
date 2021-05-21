(ns tools-project.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [tools-project.views :as views]))

(defroutes app-routes
  (route/resources "/")
  (GET "/" [] (views/welcome))
  (GET "/search" [] (views/searching-form))
  (GET "/search-submit" [name] (views/searching name))
  (route/not-found "Not found"))

(def app
  (wrap-defaults app-routes site-defaults))