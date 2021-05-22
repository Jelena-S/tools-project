(ns tools-project.views
  (:require [hiccup.page :refer [html5 include-css]]
            [tools-project.scraping :refer [rent-agencies]]
            [tools-project.db :as db]))

(def options
  (list
   [:head
    (include-css "../css/style.css")]
   [:ul 
    [:li [:a {:href "/"} "Home"]]
    [:li [:a {:href "/search"} "Search"]]]))

(defn welcome
  []
  (html5
   options
   [:div
    [:h1 (str "Welcome to the price comparating site!")]
    [:br]
    [:h3 (str "Here you can find prices for several rent a car agencies and choose the cheapest one.")]
    [:p (str "Go to search page and enter car name. When you click on search button, results will be shown. If you leave car name empty, all cars will be shown with their prices.")]]))

(def columns
  [:id :name  ]) 

(def all-columns-all
  [:id :name :grand :agape :max])

(defn columns-for-agencies
  []
  (let [names (map :agency rent-agencies)]
    (for [agency-name names]
      [:th agency-name])))

(defn all-columns
  [columns]
  [:tr
   (for [column columns]
     [:th column])
   (columns-for-agencies)])

(defn return-price
  [agency cars]
  (let [p (:price (first (filter #(= agency (:agency_name %)) cars)))]
    (if (nil? p) 0 p)))

(defn return-url
  [agency cars]
(let [p (:url (first (filter #(= agency (:agency_name %)) cars)))]
  (if (nil? p) 0 p)))

(defn table-with-cars
  [cars columns]
  [:tr (for [car cars
        c columns]
    ;[:td [:input {:type "text" :name "name" :value (car c) :readonly true}]]
    ;[:td [:a {:href "https://rentacargrandmobile.com"}[:button (car c)]]]
    ;(if (= (car c) 0) [:td [:input {:type "text" :name "name" :value "/" :readonly true}]] [:td [:a {:href "https://rentacargrandmobile.com"} [:button (car c)]]])
    (if (= (str c) ":id") 
      [:td [:input {:type "text" :name "name" :value (:id car) :readonly true}]] 
      (if (= (str c) ":name") 
        [:td [:input {:type "text" :name "name" :value (:name car) :readonly true}]] 
        (if (= (car c) 0) 
          [:td [:input {:type "text" :name "name" :value "/" :readonly true}]] 
          (if (= (str c) ":max")
            [:td [:a {:href (car :max-url)} [:button (car c)]]]
            (if (= (str c) ":agape")
              [:td [:a {:href (car :agape-url)} [:button (car c)]]]
              [:td [:a {:href (car :grand-url)} [:button (car c)]]]))))))])


;[:td [:input {:type "text" :name "name" :value (:name car) :readonly true}]]
;[:td [:input {:type "text" :name "grand" :value (:grand car) :readonly true}]]
;[:td [:input {:type "text" :name "agape" :value (:agape car) :readonly true}]]
;[:td [:input {:type "text" :name "max" :value (:max car) :readonly true}]]

(defn searching-form
  []
  (html5
   options
   [:div
    [:h1 (str "Here you can search for desired car")]
    [:hr]
    [:form {:method "get" :action "search-submit"}
     [:label "Car name: "]
     [:input {:type "text" :name "name"}]
     [:br]
     [:br]
     [:input {:type "submit" :value "Search!"}]]
    ;table with all cars
    ]))

(defn searching
  [name]
  (let [cars (if (empty? name) (db/selectt db/db) (db/selectt db/db name))
        ids (set (map :id cars))]
    (html5
     [:a {:href "/search"} [:button  "<- Back to searching"]]
     [:h2 "Results for searching: " name]
     [:h4 "Click on price button for details..."]
     [:hr]
     (if (empty? cars) [:h2 "No results found"]
         [:table
          (include-css "../css/style.css")
          (all-columns columns)
          (for [id ids]
            (let [prices (filter #(= id (:id %)) cars)
                  p (first prices)
                  grand (return-price "GRAND MOBILE" prices)
                  agape (return-price "AGAPE" prices)
                  max (return-price "MAX" prices)
                  grand-url (return-url "GRAND MOBILE" prices)
                  agape-url (return-url "AGAPE" prices)
                  max-url (return-url "MAX" prices)
                  show (assoc p :grand grand :agape agape :max max :grand-url grand-url :agape-url agape-url :max-url max-url)]
              (table-with-cars (vector show) all-columns-all)))]))))