(ns tools-project.db
  (:require [clojure.java.jdbc :refer [db-do-commands create-table-ddl insert! insert-multi! find-by-keys query drop-table-ddl]]));navedi sta treba

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "agencies.db"})

(defn create-db
  [db]
  (try (db-do-commands db 
                       [(create-table-ddl :agency
                                          [[:id :integer "PRIMARY KEY AUTOINCREMENT"]
                                           [:name :text]
                                           [:link :text]])
                        (create-table-ddl :car ;details???
                                          [[:id :integer "PRIMARY KEY AUTOINCREMENT"]
                                           [:name :text]])
                        (create-table-ddl :price
                                          [[:id :integer "PRIMARY KEY AUTOINCREMENT"]
                                           [:agency_id :integer]
                                           [:car_id :integer]
                                           [:url_for_reservation :text]
                                           [:price :double]
                                           ["FOREIGN KEY(agency_id) REFERENCES agency(id)"]
                                           ["FOREIGN KEY(car_id) REFERENCES car(id)"]])])
       (catch Exception e
         (println (.getMessage e))))) 

(defn drop-all-tables
  [db tables]
  (doseq [table tables]
    (try
      (db-do-commands db (drop-table-ddl table))
      (catch java.sql.SQLException _))))

(defn insert-one-in-db
  [db table item]
  (insert! db table item {:return-keys true}))


(defn insert-multi-in-db
  [db table items]
  (insert-multi! db table items))

(defn find
  [db table item]
  (find-by-keys db table item))

(defn select
  [db]
  (query db [(str "select ...")]))

