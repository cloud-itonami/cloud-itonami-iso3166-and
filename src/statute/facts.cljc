(ns statute.facts
  "General-law compliance catalog for Andorra (AND) -- extends this repo's
  existing `marketentry.facts` (public-procurement market-entry only,
  narrow scope) with a second, orthogonal catalog of statutes a company
  operating in this jurisdiction must generally track for compliance.
  Mirrors cloud-itonami-iso3166-jpn/-deu/-bgr/-aze/-alb/-arm/-atg/-ben's
  `statute.facts` (ADR-2607141700, cloud-itonami-compliance-fact-
  federation).

  Every entry cites an OFFICIAL government-hosted URL -- never
  fabricated. Andorra's official legal-acts portal is
  portaljuridicandorra.ad (Portal Jurídic del Principat d'Andorra, a
  JOINT service of the Consell General -- Parliament -- and the Govern
  d'Andorra, WebSearch-confirmed via consellgeneral.ad's and govern.ad's
  own announcement pages), whose own `/L<year><number>` and `/R<date>`
  pages rendered directly on the FIRST curl attempt (plain HTML,
  `pandoc -f html -t plain` extraction) for every law in this catalog
  except the labor code, which was instead fetched as the Consell
  General's own directly-hosted BOPA-published PDF (`pdftotext -layout`)
  -- no JS-SPA or TLS problems for any entry below, so every citation is
  HIGH confidence, read directly, not a secondary-source fallback, with
  one noted caveat:

  - portaljuridicandorra.ad's own pages label each consolidated text
    '(Text refós sense caràcter oficial)' -- i.e. the CONSOLIDATED
    reading-copy hosted there is explicitly NOT itself the legally
    authoritative text (BOPA, the Butlletí Oficial, remains sole
    authority for that); this is the same caveat a reader would face
    consulting e.g. a consolidated-code service for any jurisdiction.
    Confidence is still HIGH because (a) the portal is jointly operated
    by the Government and Parliament themselves (not a private
    third-party summarizer), and (b) for the labor code this catalog
    additionally cross-checked the SAME article text against the
    Consell General's own directly-hosted BOPA-published PDF, which
    matched verbatim.
  - Companies/commercial-entity law: Llei 20/2007, del 18 d'octubre, de
    societats anònimes i de responsabilitat limitada -- curl-fetched
    directly from portaljuridicandorra.ad/L2007020_1 and read (Article 1
    'Concepte': 'La societat constituïda d'acord amb aquesta Llei és un
    negoci jurídic pel qual una o més persones físiques o jurídiques
    realitzen aportacions a títol de capital...'). Unlike Benin (an
    OHADA member state whose company law is a DIRECTLY-applicable
    SUPRANATIONAL instrument, no domestic transposition act), this
    iteration specifically checked and found Andorra is NOT part of
    OHADA or any comparable supranational company-law instrument --
    Llei 20/2007 is Andorra's own domestic statute, full stop. HIGH
    confidence.
  - Llei de relacions laborals (Labour Code): Llei 31/2018, del 6 de
    desembre, de relacions laborals -- fetched directly as a PDF from
    the Consell General's own hosting
    (consellgeneral.ad/fitxers/documents/lleis-2018/llei-31-2018-de-
    relacions-laborals, the BOPA-published text itself, 'Núm. 3 ...
    www.bopa.ad ... 10 de gener del 2019' visible in the PDF header) and
    read via `pdftotext -layout` (Article 1, 'Àmbit d'aplicació':
    'Aquesta Llei és aplicable a totes les relacions laborals que es
    desenvolupen al Principat d'Andorra...'). This iteration
    specifically investigated whether Andorra's labor statute is
    literally titled a 'Codi de relacions laborals' (the shape this
    catalog's own scaffold blueprint guessed) and found a genuinely
    different, dated answer: the PREDECESSOR statute, Llei 35/2008, del
    18 de desembre, DEL CODI DE RELACIONS LABORALS, WAS literally so
    titled, but Llei 31/2018 REPLACED it in 2018 and dropped the 'Codi'
    framing from its own title (while keeping a similar seven-title
    structure) -- so a citation naming a still-current 'Codi de
    relacions laborals' would be citing a superseded law. This catalog
    cites the current Llei 31/2018 by its own actual title. HIGH
    confidence.
  - Llei qualificada de protecció de dades personals (LQPD, data
    protection): Llei 29/2021, del 28 d'octubre, qualificada de
    protecció de dades personals -- curl-fetched directly from
    portaljuridicandorra.ad/L2021029 and read (Article 1 'Objecte';
    Article 46 'Naturalesa jurídica i estructura orgànica de l'Agència
    Andorrana de Protecció de Dades': 'L'Agència Andorrana de Protecció
    de Dades (APDA), va ser creada l'any 2003 com organisme públic amb
    personalitat jurídica pròpia, independent de les administracions
    públiques i amb plena capacitat d'obrar... es configura com una
    autoritat independent que actua amb objectivitat i plena
    independència'). APDA's OWN about page (apda.ad/agencia,
    WebFetch-read) independently corroborates APDA as an independent
    public body but states a DIFFERENT creation year ('creada l'any
    2005') than the current law's own Art. 46.1 text ('creada l'any
    2003', which matches the predecessor Llei 15/2003's own enactment
    year) -- this catalog cites the law's own text as authoritative for
    the year and notes the minor discrepancy with the agency's
    self-description honestly rather than silently picking one; this
    does not affect confidence in the substantive fact that APDA is a
    real, independent, legally-established authority, still HIGH. Llei
    29/2021 (in force since May 2022) is the CURRENT LQPD that governs
    APDA today, superseding the original Llei 15/2003, del 18 de
    desembre, qualificada de protecció de dades personals (the law that
    first created APDA) -- this catalog cites the current, governing law.

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"AND"
   [{:statute/id "and.llei-societats-anonimes-responsabilitat-limitada"
     :statute/title "Llei 20/2007, del 18 d'octubre, de societats anònimes i de responsabilitat limitada"
     :statute/jurisdiction "AND"
     :statute/kind :law
     :statute/law-number "Llei 20/2007, del 18 d'octubre"
     :statute/url "https://www.portaljuridicandorra.ad/L2007020_1"
     :statute/url-provenance :official-portaljuridicandorra-ad
     :statute/enacted-date "2007-10-18"
     :statute/retrieved-at "2026-07-22"
     :statute/topic #{:corporate-governance :incorporation}}
    {:statute/id "and.llei-relacions-laborals"
     :statute/title "Llei 31/2018, del 6 de desembre, de relacions laborals"
     :statute/jurisdiction "AND"
     :statute/kind :law
     :statute/law-number "Llei 31/2018, del 6 de desembre"
     :statute/url "https://www.consellgeneral.ad/fitxers/documents/lleis-2018/llei-31-2018-de-relacions-laborals"
     :statute/url-provenance :official-consellgeneral-ad
     :statute/enacted-date "2018-12-06"
     :statute/retrieved-at "2026-07-22"
     :statute/topic #{:labor :employment}}
    {:statute/id "and.llei-qualificada-proteccio-dades-personals"
     :statute/title "Llei 29/2021, del 28 d'octubre, qualificada de protecció de dades personals"
     :statute/jurisdiction "AND"
     :statute/kind :law
     :statute/law-number "Llei 29/2021, del 28 d'octubre"
     :statute/url "https://www.portaljuridicandorra.ad/L2021029"
     :statute/url-provenance :official-portaljuridicandorra-ad
     :statute/enacted-date "2021-10-28"
     :statute/retrieved-at "2026-07-22"
     :statute/topic #{:data-protection :privacy}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-and statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "AND")) " AND statutes seeded with an "
                 "official citation. Extend "
                 "`statute.facts/catalog`, never fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :data-protection)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
