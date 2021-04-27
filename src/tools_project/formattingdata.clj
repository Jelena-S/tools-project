(ns tools-project.core
  (:require [clojure.string :as str]))

(defn trim-name [x]
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

(defn extract-agency-name [agency-name x]
  {:agency agency-name :name (:name x) :url (:url x) :price (:price x)})
