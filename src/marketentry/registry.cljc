(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `direct-contracting-eligible?` / `direct-contracting-claim-mismatches?`
  are the SAME discipline applied to a genuinely Andorra-specific
  mechanism: Llei 14/2022, del 12 de maig, de contractació pública, Art.
  30.1 (curl-verified 2026-07-22 against portaljuridicandorra.ad's own
  hosting), which permits `contractació directa` (direct contracting,
  bypassing the ordinary competitive concurs procedure) on quantitative
  grounds in exactly two lletres:

    d) contractes menors -- ALWAYS permitted, no urgency needed: works
       under EUR 24,000, services/supplies under EUR 15,000.
    c) urgent direct contracting -- permitted ONLY when urgency
       circumstances apply: works up to EUR 40,000, services/supplies up
       to EUR 25,000.

  This is a GENUINELY DIFFERENT check SHAPE than every prior iso3166
  sibling this repo mirrors: Bulgaria's ЗОП Art. 54(5) de-minimis is a
  PERCENTAGE-OF-TURNOVER ELIGIBILITY formula, Albania's Neni 76(2)(c)
  carve-out is a FLAT-CONSTANT ELIGIBILITY threshold, Azerbaijan's/
  Armenia's flagship checks are BOOLEAN registry-membership ELIGIBILITY
  reads, Antigua and Barbuda's vendor-class check is a THREE-TIER
  ELIGIBILITY-THRESHOLD classification over a SINGLE numeric axis
  (contract value only), and Benin's Art. 77 mechanism is a BID-
  EVALUATION PRICE ADJUSTMENT (never an eligibility gate at all).
  Andorra's Art. 30.1 mechanism is an ELIGIBILITY-THRESHOLD
  classification like Antigua and Barbuda's, but over TWO independent
  axes at once -- contract TYPE (works vs. services/supplies, each with
  its OWN threshold pair) AND an urgency flag (which threshold pair
  applies) -- so the 'recompute' here derives eligibility from a
  two-dimensional lookup, not a single ordered tier ladder. This is
  reported honestly as a sixth distinct check shape for the family, not
  treated as a lesser version of any prior shape.

  Only Art. 30.1's TWO QUANTITATIVE lletres (c and d) are modeled.
  Art. 30.1's other lletres (a, b, e, f, g, h) are deliberately NOT
  modeled: each turns on a qualitative, case-by-case judgment call (lack
  of competing bids, emergency processing, artistic character, national-
  security sensitivity, etc.) that this governor cannot independently
  recompute from an engagement's own declared numeric fields without
  inventing a discretionary judgment the contracting body itself has not
  made. Modeling only the two unambiguous quantitative thresholds is an
  honest scope-narrowing, the same discipline this family's other
  honest-narrowing decisions already established.

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real procurement portal. It builds the RECORD an
  operator would keep, not the act of submitting a portal registration
  itself (that is `marketentry.operation`'s `:filing/submit`, always
  human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  (+ (double base-fee)
     (* (double monthly-rate) (double monitoring-months))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (== (double claimed-fee) (compute-engagement-fee engagement)))

(def direct-contracting-thresholds-eur
  "Llei 14/2022, del 12 de maig, de contractació pública, Art. 30.1
  (curl/pandoc-verified 2026-07-22 against portaljuridicandorra.ad's
  official hosting): the EUR upper bound for `contractes menors` (:minor
  -- always permitted) and for urgent direct contracting (:urgent --
  permitted only when urgency circumstances apply), per contract type."
  {:works             {:minor 24000.0 :urgent 40000.0}
   :services-supplies {:minor 15000.0 :urgent 25000.0}})

(defn direct-contracting-eligible?
  "Does `engagement`'s own declared `:contract-type` (`:works` or
  `:services-supplies`), `:contract-value` (EUR) and `:urgent?` flag
  qualify for `contractació directa` under Art. 30.1 lletra d) (minor
  contracts, always) or lletra c) (urgent contracts, only when
  `:urgent?` is true)? Missing/unknown `:contract-type` is never
  eligible -- this governor does not guess a contract's own category."
  [{:keys [contract-type contract-value urgent?]}]
  (boolean
   (when-let [{:keys [minor urgent]} (get direct-contracting-thresholds-eur contract-type)]
     (let [v (double (or contract-value 0))]
       (or (< v minor)
           (and (true? urgent?) (<= v urgent)))))))

(defn direct-contracting-claim-mismatches?
  "Does `engagement`'s own declared `:direct-contracting-claim?` differ
  from the INDEPENDENTLY recomputed Art. 30.1 eligibility? Catches BOTH
  directions honestly: claiming direct-contracting eligibility the
  engagement's own declared value/type/urgency does not actually
  support, and an eligible engagement being declared ineligible."
  [{:keys [direct-contracting-claim?] :as engagement}]
  (not= (boolean direct-contracting-claim?)
        (direct-contracting-eligible? engagement)))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a portal registration
  package. Pure function -- does not touch any real procurement
  portal."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a portal
  registration (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
