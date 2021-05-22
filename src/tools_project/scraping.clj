(ns tools-project.scraping
  (:require [reaver :refer [parse extract-from text attr]]
            [tools-project.formattingdata :as format-data]
            [tools-project.db :refer [db insert-one-in-db insert-multi-in-db findd drop-all-tables create-db]]))

;trim needed in some agencies!!!!!
(defn parse-slurping
  [element]
  (parse(slurp (:url-for-scraping element))))

(defn extraction
  "extracting cars"
  [element]
  (map format-data/formatting (sort-by str (extract-from (parse-slurping element) (:element element)
                                                          [:name :url :price] ;:details
                                                          (:find-name element) text
                                                          (:find-url element) (attr :href)
                                                          (:find-price element) text)))) ;(:find-details element) text

(defn extraction-only
  [element]
  (extract-from (parse-slurping element) (:element element)
                [:name :url :price] ;:details
                (:find-name element) text
                (:find-url element) (attr :href)
                (:find-price element) text))

(defn all-agencies
  [rent-agencies]
  (loop [rent-agencies rent-agencies agencies []]
    (if (empty? rent-agencies) agencies
        (let [[agency & remain] rent-agencies]
          (recur remain (conj agencies {:name (agency :agency) :link (agency :part-link)})))))) ;link?


(defn insert-cars-and-prices
  [cars agency]
  (loop [cars cars prices []]
    (if (empty? cars) (insert-multi-in-db db "price" (set prices))
        (let [[car & remain] cars
              url (str (:link agency) (:url car))
              price (:price car)
              car-for-db (format-data/fix-car car)
              id (or (:id (first (findd db "car" car-for-db)))
                     (get (first (insert-one-in-db db "car" car-for-db)) (keyword "last_insert_rowid()")))]
          (recur remain (conj prices {:agency_id (:id agency) :car_id id :price price :url_for_reservation url}))))))




(defn cars-per-agency
  [agency]
  (let [a (first (findd db "agency" {:name (:agency agency)})) ;
        cars (set (extraction  agency))] ;(:element agency)
    (insert-cars-and-prices cars a))) 

(defn scraping-all-agencies
  [rent-agencies]
  (loop [rent-agencies rent-agencies agencies []]
    (if (empty? rent-agencies) agencies
        (let [[agency & remain] rent-agencies]
          (recur remain (into agencies (cars-per-agency agency)))))))

;url for ruting!!!!
(def rent-agencies [{:agency "GRAND MOBILE"
                     :url-for-scraping "https://rentacargrandmobile.com/iznajmljivanje-automobila-beograd"
                     :part-link "https://rentacargrandmobile.com"
                     :element "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                     :find-name ".text > a"
                     :find-url ".text > a"
                     :find-price ".price"} ;:find-details ".car-details .detail"
                    {:agency "AGAPE"
                     :url-for-scraping "https://rentacarbeograd.info/vozila/"
                     :part-link ""
                     :element "div.caption"
                     :find-name "h4.caption-title > a"
                     :find-url "td.buttons > a"
                     :find-price "h5.caption-title-sub"};:find-details "table.table"
                    {:agency "MAX"
                     :url-for-scraping "https://renta-car-beograd.rs/iznajmljivanje-automobila-beograd"
                     :part-link "https://renta-car-beograd.rs"
                     :element "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                     :find-name ".text > a"
                     :find-url "a.btn.detaljno"
                     :find-price ".price"}]) ;:find-details ".car-details .detail"

(defn start
  []
  (drop-all-tables db [:price :car :agency])
  (create-db db)
  (insert-multi-in-db db "agency" (all-agencies rent-agencies))
  (scraping-all-agencies rent-agencies))

(start)