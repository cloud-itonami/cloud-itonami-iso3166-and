(ns culture.facts
  "Country-level regional-culture catalog for Andorra (AND) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"AND"
   [{:culture/id "and.dish.trinxat"
     :culture/name "Trinxat"
     :culture/country "AND"
     :culture/kind :dish
     :culture/summary "Dish of mashed potatoes, cabbage and pork from the Pyrenees, principally Andorra and the Catalan comarques of Cerdanya and Alt Urgell."
     :culture/url "https://en.wikipedia.org/wiki/Trinxat"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "and.dish.coca"
     :culture/name "Coca"
     :culture/country "AND"
     :culture/kind :dish
     :culture/summary "Sweet or savory pastry typically made and consumed in Catalonia, most of Valencia, the Balearic Islands, Andorra and French Catalonia."
     :culture/url "https://en.wikipedia.org/wiki/Coca_(pastry)"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "and.festival.our-lady-of-meritxell"
     :culture/name "Our Lady of Meritxell Day"
     :culture/name-local "Mare de Déu de Meritxell"
     :culture/country "AND"
     :culture/kind :festival
     :culture/summary "Feast day of Our Lady of Meritxell, patron saint of Andorra, celebrated on 8 September as the Andorran National Day."
     :culture/url "https://en.wikipedia.org/wiki/Our_Lady_of_Meritxell"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "and.heritage.madriu-perafita-claror"
     :culture/name "Madriu-Perafita-Claror Valley"
     :culture/country "AND"
     :culture/kind :heritage
     :culture/summary "Glacial valley in southeastern Andorra covering about 9% of the country, Andorra's only UNESCO World Heritage Site, inscribed in 2004."
     :culture/url "https://en.wikipedia.org/wiki/Madriu-Perafita-Claror_Valley"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "and.heritage.casa-de-la-vall"
     :culture/name "Casa de la Vall"
     :culture/country "AND"
     :culture/kind :heritage
     :culture/summary "Historic 16th-century house in Andorra la Vella that served as the seat of Andorra's General Council until 2011, registered in the Cultural Heritage of Andorra."
     :culture/url "https://en.wikipedia.org/wiki/Casa_de_la_Vall"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-and culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "AND"))
                 " AND entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
