(ns tools-project.core
  (:require [reaver :refer [parse extract-from text attr select]]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn trimuj [x]
  (let [new-name (-> (:name x)
                     (first))]
    {:name new-name :url (:url x) :price (:price x)}))


;namesti

(defn formatting [x]
  (if (str/includes? (:name x) "Skoda")
    (let [new-name (-> (:name x)
                       (str/replace "Skoda" "Škoda"))]
      {:name new-name :url (:url x) :price (:price x)})

    (if (str/includes? (:name x) "VW")
      (let [new-name (-> (:name x)
                         (str/replace "VW" "Volkswagen"))]
        {:name new-name :url (:url x) :price (:price x)})
      (if (str/includes? (:name x) "Golf")
        (let [new-name (-> (:name x)
                           (str/replace "Golf" "Volkswagen Golf"))]
          {:name new-name :url (:url x) :price (:price x)})
        (let [new-name (-> (:name x))]
          {:name new-name :url (:url x) :price (:price x)})))))

(defn agencija [ime-agencije x]
  {:agency ime-agencije :name (:name x) :url (:url x) :price (:price x)})




;max rent a car 37
(def cars-max (slurp "https://renta-car-beograd.rs/iznajmljivanje-automobila-beograd"))
(def cars-max-rent-a-car (map formatting (sort-by str (extract-from (parse cars-max) "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                                                                    [:name :url :price :details]
                                                                    ".text > a" text
                                                                    "a.btn.detaljno" (attr :href)
                                                                    ".price" text
                                                                    ".car-details .detail" text))))


;grand mobile rent a car 15
(def cars-grand-mobile (slurp "https://rentacargrandmobile.com/iznajmljivanje-automobila-beograd"))

(def cars-grand-mobile-rent-a-car (map formatting (sort-by str (extract-from (parse cars-grand-mobile) "div.listanje-vozila.flex.row div.col-sm-4.col-md-4.col-lg-3.vozilo.fadeIn"
                                                                             [:name :url :price :details]
                                                                             ".text > a" text
                                                                             ".text > a" (attr :href)
                                                                             ".price" text
                                                                             ".car-details .detail" text))))


;atos rent a car 26
(def cars-atos (slurp "https://www.rentacaratos.com/rent-a-car-beograd-automobili"))

(def cars-atos-rent-a-car (map formatting (map trimuj (sort-by str (extract-from (parse cars-atos) "a.book-car-box"
                                                                                 [:name :url :price :details]
                                                                                 ".col-md-3 p.dark" text
                                                                                 "a.book-car-box" (attr :href)
                                                                                 ".green" text
                                                                                 ".car-icons-holder .number-icon-1" text)))))

;Agape rent a car 11

(def cars-agape (slurp "https://rentacarbeograd.info/vozila/"))

(def cars-agape-rent-a-car (map formatting (sort-by str (extract-from (parse cars-agape) "div.caption"
                                                                      [:name :url :price :details]
                                                                      "h4.caption-title > a" text
                                                                      "td.buttons > a" (attr :href)
                                                                      "h5.caption-title-sub" text
                                                                      "table.table" text))))



;bel rent a car 37
(def cars-bel (slurp "https://beograd-renta-car.com/rent-a-car-beograd-automobili"))

(def cars-bel-rent-a-car (map formatting(map trimuj (sort-by str (extract-from (parse cars-bel) "a.book-car-box"
                                                                 [:name :url :price :details]
                                                                 ".col-md-3 p.dark" text
                                                                 "a.book-car-box" (attr :href)
                                                                 ".green" text
                                                                 ".car-icons-holder .number-icon-1" text)))))

;fali UP,Polo,7...
(def razliciti (distinct (concat (map :name cars-max-rent-a-car) (map :name cars-grand-mobile-rent-a-car) (map :name cars-atos-rent-a-car) (map :name cars-agape-rent-a-car) (map :name cars-bel-rent-a-car)))) ;104

(def svi (concat (map :name cars-max-rent-a-car) (map :name cars-grand-mobile-rent-a-car) (map :name cars-atos-rent-a-car) (map :name cars-agape-rent-a-car) (map :name cars-bel-rent-a-car)))
(count svi) ;126

(clojure.set/intersection (set (map :name cars-max-rent-a-car)) (set (map :name cars-atos-rent-a-car)) (set (map :name cars-agape-rent-a-car)) (set (map :name cars-bel-rent-a-car))) ;Skoda fabia

;dodaj agenciju, URL TREBA
(loop [cars cars-max-rent-a-car
       new-cars []]
  (if (empty? cars)
      new-cars
    (let [x (first cars)]
      (recur (rest cars) (conj new-cars {:agency "max" :name (:name x) :url (:url x) :price (:price x)})))))

(defn  dodaj-agenciju [ime-agencije cars]
  (loop [cars cars
         new-cars []]
    (if (empty? cars)
      new-cars
      (let [x (first cars)]
        (recur (rest cars) (conj new-cars {:agency ime-agencije :name (:name x) :url (:url x) :price (:price x)}))))))


(defn novi-url[x]
  (let [new-name (-> (:url x)
                     (str/replace (:url x) (clojure.core/str "part-link" (:url x))))]
    {:name (:name x) :url new-name :price (:price x)}))

(defn new-url[cars]
  (loop[cars cars
        new-cars []]
   (if (empty? cars)
     new-cars
     (let [x (first cars)]
       (let[ new-url (-> (:url x)
                       (str/replace (:url x) (clojure.core/str (:part-link x) (:url x))))]
        (recur (rest cars)(conj new-cars {:name (:name x) :url new-url :price (:price x)})))))))

(defn fix-price
  [price]
  (if (str/includes? price "/") (subs (first (str/split price #" / ")) 4) price))
