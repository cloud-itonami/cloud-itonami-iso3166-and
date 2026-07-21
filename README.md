# cloud-itonami-iso3166-and

**AND**: Andorra.

- Ministeri de Finances e-procurement (Plataforma de contractació del sector públic, contractacio.govern.ad)
- Registre de Societats Mercantils + NRT

AGPL-3.0-or-later.

## Market-entry / statute catalogs

Governed public-sector market-entry compliance actor, same architecture
as `cloud-itonami-iso3166-jpn`/`-deu`/`-ben`/`-atg` (minus the JPN-specific
`goyoukiki` bridge):

- `src/marketentry/{facts,governor,phase,sim,operation,registry,store,
  marketentryllm}.cljc` -- the actor. `facts.cljc` cites the Junta de
  Contractació Administrativa, the Registre Oficial de Licitadors i
  Empreses Classificades and the Plataforma de contractació del sector
  públic (Llei 14/2022, del 12 de maig, de contractació pública -- all
  three housed within the ministeri encarregat de les finances, a
  genuinely different shape than Benin's ARMP/DNCMP split), the Registre
  de Societats Mercantils (Reglament del 20 de febrer del 2008 + Llei
  20/2007) and the NRT (Départament de Tributs i Fronteres, Llei 21/2014
  Art. 26.2.b). `governor.cljc`'s flagship check independently
  recomputes Llei 14/2022 Art. 30.1's direct-contracting EUR thresholds
  (per contract type and urgency).
- `src/statute/facts.cljc` -- general-law catalog: Llei 20/2007
  (societats anònimes i de responsabilitat limitada), Llei 31/2018 (de
  relacions laborals) and Llei 29/2021 (qualificada de protecció de
  dades personals, which governs the Agència Andorrana de Protecció de
  Dades, APDA).

Every citation is curl/pandoc-verified against an official source
(portaljuridicandorra.ad, consellgeneral.ad, transparencia.ad); see each
namespace's docstring for the full research trail and any
honestly-narrowed scope.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Andorra:

- `src/culture/facts.cljc` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
