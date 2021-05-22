(ns tools-project.formattingdata-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [tools-project.formattingdata :refer [formatting fix-price]]))

(deftest test-formatting
  (testing "formatting Skoda"
    (is (= (formatting "Skoda") "Škoda")))
  (testing "formatting nestoSkoda"
    (is (= (formatting "nestoSkoda") "Škoda")))
  (testing "formatting VW"
    (is (= (formatting "VW") "Volkswagen")))
  (testing "formatting nestoVW"
    (is (= (formatting "nestoVW") "Volkswagen")))
  (testing "formatting Golf"
    (is (= (formatting "Golf") "Volkswagen Golf")))
(testing "formatting nestoSkoda"
  (is (= (formatting "nestoGolf") "Volkswagen Golf"))))

(deftest test-fixing-price
  (testing "fixing od 28€"
    (is (= (fix-price "od 28€") "od 28€")))
  (testing "fixing Već od 28 € / dnevno"
    (is (= (fix-price "Već od 28 € / dnevno") "od 28 €"))))