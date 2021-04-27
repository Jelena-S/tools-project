(ns tools-project.scraping
  (:require [reaver :refer [parse extract-from text attr select]]
            [clojure.string :as str]
            [clojure.set :as set]
            [tools-project.formattingdata :as format-data]))

;trim needed in some agencies!!!!!
(defn extraction
  "extracting cars"
  [url-for-scraping element]
  (map format-data/formatting(sort-by str(extract-from (parse (slurp url-for-scraping)) (:element element)
                                                       [:name :url :price :details]
                                                       (:find-name element) text
                                                       (:find-url element) (attr :href)
                                                       (:find-price element) text
                                                       (:find-details element) text))))
;url for ruting!!!!
(def rent-agencies [{:agency "cars-grand-mobile"
                :url-for-scraping "https://rentacargrandmobile.com/iznajmljivanje-automobila-beograd"
                :element "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                :find-name ".text > a"
                :find-url ".text > a"
                :find-price ".price"
                :find-details ".car-details .detail"}
               {:agency "cars-agape"
                :url-for-scraping "https://rentacarbeograd.info/vozila/"
                :element "div.caption"
                :find-name "h4.caption-title > a"
                :find-url "td.buttons > a"
                :find-price "h5.caption-title-sub"
                :find-details "table.table"}
                {:agency "cars-max"
                 :url-for-scraping "https://renta-car-beograd.rs/iznajmljivanje-automobila-beograd"
                 :element "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                 :find-name ".text > a"
                 :find-url "a.btn.detaljno"
                 :find-price ".price"
                 :find-details ".car-details .detail"}])