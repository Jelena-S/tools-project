(ns tools-project.core
  (:require [reaver :refer [parse extract-from text attr select]]
            [clojure.string :as str]
            [clojure.set :as set]))

(def cars-max (slurp "https://renta-car-beograd.rs/iznajmljivanje-automobila-beograd"))
(def cars-max-rent-a-car (extract-from (parse cars-max) "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                                       [:name :url :price :details]
                                       ".text > a" text
                                       ".text > a" (attr :href)
                                       ".price" text
                                       ".car-details .detail" text))
