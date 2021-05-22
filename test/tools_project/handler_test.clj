(ns tools-project.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [tools-project.handler :refer [app]]
            [ring.mock.request :as mock]))

(deftest test-app
  (testing "welcome"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))))
  
  (testing "searching-form"
    (let [response (app (mock/request :get "/search"))]
      (is (= (:status response) 200))))
  
  (testing "searching"
    (let [response (app (mock/request :get "/search-submit?name=VolksWagen"))]
      (is (= (:status response) 200))))
  
  (testing "not-found"
    (let [response (app (mock/request :get "/dniuafhui"))]
      (is (= (:status response) 404))))
  )