(ns tools-project.db-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [tools-project.db :refer [create-db insert-one-in-db insert-multi-in-db findd]]
            [clojure.java.jdbc  :refer [db-do-commands drop-table-ddl query] ]))

(def db_test
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "agencies-test.db"})

(defn- clean-up
  [t]
  (doseq [table [:price :car :agency]]
    (try
      (db-do-commands db_test (drop-table-ddl table))
      (catch java.sql.SQLException _)))
          

  (t))

(use-fixtures
  :each clean-up)

(deftest test-create-db-and-tables
  (create-db db_test)
  (testing "creating table car" (is (= 0 (query db_test ["SELECT * FROM car"] {:result-set-fn count}))))
  (testing "creating table agency" (is (= 0 (query db_test ["SELECT * FROM agency"] {:result-set-fn count}))))
  (testing "creating table price" (is (= 0 (query db_test ["SELECT * FROM price"] {:result-set-fn count})))))

(deftest test-insert-db
  (create-db db_test)
  (testing "inserting one row to table agency"
    (let [r (get (first (insert-one-in-db db_test :agency {:name "GRAND MOBILE" :link "https://rentacargrandmobile.com"})) (keyword "last_insert_rowid()"))]
      (is (= r 1))))
  (testing "inserting one row to table car"
    (let [r (get (first (insert-one-in-db db_test :car {:name "Škoda Praktik"})) (keyword "last_insert_rowid()"))]
      (is (= r 1))))
  (testing "inserting one row to table price"
    (let [r (get (first (insert-one-in-db db_test :price {:agency_id 1 :car_id 1 :url_for_reservation "https://rentacargrandmobile.com/rent-a-car-beograd-izaberi/skoda-rapid-dsg" :price "od 28€"})) (keyword "last_insert_rowid()"))]
      (is (= r 1))))
  (testing "checking if right values are inserted to table agency"
    (let [r (first (findd db_test :agency {:id 1}))]
      (is (= r {:id 1, :name "GRAND MOBILE" :link "https://rentacargrandmobile.com"}))))
  (testing "checking if right values are inserted to table car"
    (let [r (first (findd db_test :car {:id 1}))]
      (is (= r {:id 1 :name "Škoda Praktik"}))))
  (testing "checking if right values are inserted to table price"
    (let [r (first (findd db_test :price {:id 1}))]
      (is (= r {:id 1 :agency_id 1 :car_id 1 :url_for_reservation "https://rentacargrandmobile.com/rent-a-car-beograd-izaberi/skoda-rapid-dsg" :price "od 28€" })))))