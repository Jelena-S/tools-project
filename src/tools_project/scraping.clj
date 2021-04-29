(ns tools-project.scraping
  (:require [reaver :refer [parse extract-from text attr select]]
            [clojure.string :as str]
            [clojure.set :as set]
            [tools-project.formattingdata :as format-data]
            [tools-project.db :refer [db insert-one-in-db insert-multi-in-db find]]))

;trim needed in some agencies!!!!!
(defn extraction
  "extracting cars"
  [url-for-scraping element]
  (map format-data/formatting (sort-by str (extract-from (parse (slurp url-for-scraping)) (:element element)
                                                         [:name :url :price :details]
                                                         (:find-name element) text
                                                         (:find-url element) (attr :href)
                                                         (:find-price element) text)))) ;(:find-details element) text

(defn all-agencies
  [rent-agencies]
  (loop [rent-agencies rent-agencies agencies []]
    (if (empty? rent-agencies) agencies
        (let [[agency & remain] rent-agencies]
          (recur remain (conj agencies {:name (agency :name)}))))))


(defn insert-cars-and-prices
  [cars agency]
  (loop [cars cars prices []]
    (if (empty? cars) (insert-multi-in-db db "price" (set prices))
        (let [[car & remain] cars
              url (:url car)
              price (:price car)
              car-for-db (format-data/fix-car car)
              id (or (:id (first (find db "car" car-for-db)))
                     (get (first (insert-one-in-db db "car" car-for-db)) (keyword "last_id()")))]
          (recur remain (conj prices {:agency_id agency :car_id id :price price :url_for_reservation url}))))))




(defn cars-per-agency
  [agency]
  (let [a (first (find db "agency" {:name (:name agency)}))
        cars (set (extraction (:url-for-scraping agency)(:element agency)))]
    (insert-cars-and-prices cars (:id a))))

(defn scraping-all-agencies
  [rent-agencies]
  (loop [rent-agencies rent-agencies agencies []]
    (if (empty? rent-agencies) agencies
        (let [[agency & remain] rent-agencies]
          (recur remain (into agencies (cars-per-agency agency)))))))

;url for ruting!!!!
(def rent-agencies [{:agency "Rent a car Beograd GRAND MOBILE"
                     :url-for-scraping "https://rentacargrandmobile.com/iznajmljivanje-automobila-beograd"
                     :part-link "https://rentacargrandmobile.com"
                     :element "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                     :find-name ".text > a"
                     :find-url ".text > a"
                     :find-price ".price"} ;:find-details ".car-details .detail"
                    {:agency "Rent a car Beograd AGAPE"
                     :url-for-scraping "https://rentacarbeograd.info/vozila/"
                     :part-link ""
                     :element "div.caption"
                     :find-name "h4.caption-title > a"
                     :find-url "td.buttons > a"
                     :find-price "h5.caption-title-sub"};:find-details "table.table"
                    {:agency "Rent a car Beograd MAX"
                     :url-for-scraping "https://renta-car-beograd.rs/iznajmljivanje-automobila-beograd"
                     :part-link "https://renta-car-beograd.rs"
                     :element "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                     :find-name ".text > a"
                     :find-url "a.btn.detaljno"
                     :find-price ".price"}]) ;:find-details ".car-details .detail"