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
    [:h3 (str "Here you can find prices for several rent a car agencies and choose the cheapest one.")]]))

(def columns
  [:id :name  ]) ;:price :url_for_reservation

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



(defn table-with-cars
  [cars columns]
  (for [car cars
        c columns]
    ;[:td [:input {:type "text" :name "name" :value (car c) :readonly true}]]
    ;[:td [:a {:href "https://rentacargrandmobile.com"}[:button (car c)]]]
    ;(if (= (car c) 0) [:td [:input {:type "text" :name "name" :value "/" :readonly true}]] [:td [:a {:href "https://rentacargrandmobile.com"} [:button (car c)]]])
    (if (= (str c) ":id") [:td [:input {:type "text" :name "name" :value (:id car) :readonly true}]] (if (= (str c) ":name") [:td [:input {:type "text" :name "name" :value (:name car) :readonly true}]] (if (= (car c) 0) [:td [:input {:type "text" :name "name" :value "/" :readonly true}]] [:td [:a {:href "https://rentacargrandmobile.com"} [:button (car c)]]])))
))


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
     [:hr]
     (if (empty? cars) [:h2 "No results found"]
         [:table
          (all-columns columns)
          (for [id ids]
            (let [prices (filter #(= id (:id %)) cars)
                  p (first prices)
                  grand (return-price "Rent a car Beograd GRAND MOBILE" prices)
                  agape (return-price "Rent a car Beograd AGAPE" prices)
                  max (return-price "Rent a car Beograd MAX" prices)
                  show (assoc p :grand grand :agape agape :max max)]
              (table-with-cars (vector show) all-columns-all)))]))))