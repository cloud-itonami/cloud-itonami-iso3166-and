(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "AND" 0)
        s (registry/register-submit "eng-1" "AND" 0)]
    (is (= "AND-DFT-000000" (get d "draft_number")))
    (is (= "AND-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "AND" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest direct-contracting-eligible-works-minor-and-urgent-tiers
  (testing "works: below EUR 24,000 -> always eligible (contracte menor), regardless of urgency"
    (is (true? (registry/direct-contracting-eligible?
                {:contract-type :works :contract-value 20000 :urgent? false})))
    (is (true? (registry/direct-contracting-eligible?
                {:contract-type :works :contract-value 20000 :urgent? true}))))
  (testing "works: EUR 24,000-40,000 -> eligible only when urgent"
    (is (false? (registry/direct-contracting-eligible?
                 {:contract-type :works :contract-value 30000 :urgent? false})))
    (is (true? (registry/direct-contracting-eligible?
                {:contract-type :works :contract-value 30000 :urgent? true}))))
  (testing "works: above EUR 40,000 -> never eligible on threshold grounds, even if urgent"
    (is (false? (registry/direct-contracting-eligible?
                 {:contract-type :works :contract-value 50000 :urgent? true})))))

(deftest direct-contracting-eligible-services-supplies-tiers
  (testing "services/supplies: below EUR 15,000 -> always eligible (contracte menor)"
    (is (true? (registry/direct-contracting-eligible?
                {:contract-type :services-supplies :contract-value 12000 :urgent? false}))))
  (testing "services/supplies: EUR 15,000-25,000 -> eligible only when urgent"
    (is (false? (registry/direct-contracting-eligible?
                 {:contract-type :services-supplies :contract-value 20000 :urgent? false})))
    (is (true? (registry/direct-contracting-eligible?
                {:contract-type :services-supplies :contract-value 20000 :urgent? true}))))
  (testing "services/supplies: above EUR 25,000 -> never eligible on threshold grounds"
    (is (false? (registry/direct-contracting-eligible?
                 {:contract-type :services-supplies :contract-value 30000 :urgent? true})))))

(deftest direct-contracting-eligible-unknown-contract-type-is-never-eligible
  (is (false? (registry/direct-contracting-eligible?
               {:contract-type :unknown-type :contract-value 100 :urgent? false}))))

(deftest direct-contracting-claim-mismatch
  (testing "claimed eligibility matches the recompute -> no mismatch"
    (is (false? (registry/direct-contracting-claim-mismatches?
                 {:contract-type :works :contract-value 20000 :urgent? false
                  :direct-contracting-claim? true}))))
  (testing "claiming eligibility a non-urgent above-threshold contract does not support -> mismatch"
    (is (true? (registry/direct-contracting-claim-mismatches?
                {:contract-type :works :contract-value 30000 :urgent? false
                 :direct-contracting-claim? true}))))
  (testing "an eligible engagement declared ineligible -> mismatch (catches both directions)"
    (is (true? (registry/direct-contracting-claim-mismatches?
                {:contract-type :works :contract-value 20000 :urgent? false
                 :direct-contracting-claim? false})))))
