(ns tools-project.formattingdata
  (:require [clojure.string :as str]))

(defn trim-name [x]
  (let [new-name (-> (:name x)
                     (first))]
    {:name new-name :url (:url x) :price (:price x)}))



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






(defn new-url [cars]
  (loop [cars cars
         new-cars []]
    (if (empty? cars)
      new-cars
      (let [x (first cars)]
        (let [new-url (-> (:url x)
                          (str/replace (:url x) (clojure.core/str (:part-link x) (:url x))))]
          (recur (rest cars) (conj new-cars {:name (:name x) :url new-url :price (:price x)})))))))

(defn fix-car
  [car]
  (let [name (:name car)]
    {:name name}))

(defn fix-price
  [price]
  (if (str/includes? price "/") (subs (first (str/split price #" / ")) 4) price))