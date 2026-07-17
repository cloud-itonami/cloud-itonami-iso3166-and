(ns culture.facts-test
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [culture.facts :as facts]))

(deftest and-has-culture-basis
  (let [sb (facts/spec-basis "AND")]
    (is (= 5 (count sb)))
    (is (= (count sb) (count (set (map :culture/id sb)))))
    (is (every? #(str/starts-with? (:culture/url %) "https://") sb))
    (is (every? #(= "AND" (:culture/country %)) sb))
    (is (every? #(nil? (:culture/municipality %)) sb))
    (is (every? #(seq (:culture/summary %)) sb))
    (is (every? #(string? (:culture/retrieved-at %)) sb))))

(deftest unknown-jurisdiction-has-no-basis
  (is (nil? (facts/spec-basis "ESP")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["AND" "ESP"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ESP"] (:missing-jurisdictions c)))))

(deftest by-kind-filters
  (is (= 2 (count (facts/by-kind "AND" :dish))))
  (is (= ["and.festival.our-lady-of-meritxell"]
         (mapv :culture/id (facts/by-kind "AND" :festival))))
  (is (empty? (facts/by-kind "AND" :other)))
  (is (empty? (facts/by-kind "ESP" :dish))))

(deftest tx-file-matches-catalog
  (let [tx (edn/read-string (slurp "data/culture-tx.edn"))
        flat (mapcat val (sort-by key facts/catalog))]
    (is (= (vec flat) (vec tx)))))
