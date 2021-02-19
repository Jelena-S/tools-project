(ns tools-project.core
  (:require [reaver :refer [parse extract-from text attr select]]
            [clojure.string :as str]
            [clojure.set :as set]))

(def cars-max (slurp "https://renta-car-beograd.rs/iznajmljivanje-automobila-beograd"))
(def cars-max-rent-a-car (sort-by str (extract-from (parse cars-max) "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                                       [:name :url :price :details]
                                       ".text > a" text
                                       ".text > a" (attr :href)
                                       ".price" text
                                       ".car-details .detail" text)))

;drugi rent a car
(def cars-grand-mobile (slurp "https://rentacargrandmobile.com/iznajmljivanje-automobila-beograd"))

(def cars-grand-mobile-rent-a-car (sort-by str(extract-from (parse cars-grand-mobile) "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                                                [:name :url :price :details]
                                                ".text > a" text
                                                ".text > a" (attr :href)
                                                ".price" text
                                                ".car-details .detail" text)))


;treci Atos treba trim
(def cars-atos (slurp "https://www.rentacaratos.com/rent-a-car-beograd-automobili"))

(def cars-atos-rent-a-car (sort-by str(extract-from (parse cars-atos) "a.book-car-box"
                                        [:name :url :price :details]
                                        ".col-md-3 p.dark" text
                                        "a.book-car-box" (attr :href)
                                        ".green" text
                                        ".car-icons-holder .number-icon-1" text)))

;Agape

(def cars-agape (slurp "https://rentacarbeograd.info/vozila/"))

(def cars-agape-rent-a-car (sort-by str(extract-from (parse cars-agape) "div.caption"
                                         [:name :url :price :details]
                                         "h4.caption-title > a" text
                                         "td.buttons > a" (attr :href)
                                         "h5.caption-title-sub" text
                                         "table.table" text)))

;bel trim treba
(def cars-bel (slurp "https://beograd-renta-car.com/rent-a-car-beograd-automobili"))

(def cars-bel-rent-a-car (sort-by str(extract-from (parse cars-bel) "a.book-car-box"
                                       [:name :url :price :details]
                                       ".col-md-3 p.dark" text
                                       "a.book-car-box" (attr :href)
                                       ".green" text
                                       ".car-icons-holder .number-icon-1" text)))

