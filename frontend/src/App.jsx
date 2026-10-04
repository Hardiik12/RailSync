import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AppShell } from './components/layout/AppShell';
import { DashboardPage } from './pages/DashboardPage';
import { StationsPage } from './pages/StationsPage';
import { DocumentsPage } from './pages/DocumentsPage';
import { M1LandingPage } from './pages/M1LandingPage';
import { KmpPage } from './pages/KmpPage';
import { ZFunctionPage } from './pages/ZFunctionPage';
import { RabinKarpPage } from './pages/RabinKarpPage';
import { AhoCorasickPage } from './pages/AhoCorasickPage';
import { M2LandingPage } from './pages/M2LandingPage';
import { SuffixArrayPage } from './pages/SuffixArrayPage';
import { SAISPage } from './pages/SAISPage';
import { KasaiPage } from './pages/KasaiPage';
import { LcpPage } from './pages/LcpPage';
import { SuffixAutomatonPage } from './pages/SuffixAutomatonPage';
import { M3LandingPage } from './pages/M3LandingPage';
import { LevenshteinPage } from './pages/LevenshteinPage';
import { DamerauPage } from './pages/DamerauPage';
import { BitmaskPage } from './pages/BitmaskPage';
import { MatrixChainPage } from './pages/MatrixChainPage';
import { OptimalBSTPage } from './pages/OptimalBSTPage';
import { M4LandingPage } from './pages/M4LandingPage';
import { FordFulkersonPage } from './pages/FordFulkersonPage';
import { EdmondsKarpPage } from './pages/EdmondsKarpPage';
import { DinicPage } from './pages/DinicPage';
import { BipartiteMatchingPage } from './pages/BipartiteMatchingPage';
import { KonigPage } from './pages/KonigPage';
import { MaxFlowMinCutPage } from './pages/MaxFlowMinCutPage';
import { M5LandingPage } from './pages/M5LandingPage';
import { SatPage } from './pages/SatPage';
import { ThreeSatPage } from './pages/ThreeSatPage';
import { ThreeSatToCliquePage } from './pages/ThreeSatToCliquePage';
import { CliqueToISPage } from './pages/CliqueToISPage';
import { ISToVCPage } from './pages/ISToVCPage';
import { VertexCoverApproxPage } from './pages/VertexCoverApproxPage';

import { NetworkPage } from './pages/NetworkPage';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AppShell />}>
          <Route index element={<DashboardPage />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="operations/stations" element={<StationsPage />} />
          <Route path="operations/network" element={<NetworkPage />} />
          <Route path="operations/documents" element={<DocumentsPage />} />
          
          {/* Module 1 */}
          <Route path="dsa/m1" element={<M1LandingPage />} />
          <Route path="dsa/m1/kmp" element={<KmpPage />} />
          <Route path="dsa/m1/z-function" element={<ZFunctionPage />} />
          <Route path="dsa/m1/rabin-karp" element={<RabinKarpPage />} />
          <Route path="dsa/m1/aho-corasick" element={<AhoCorasickPage />} />

          {/* Module 2 */}
          <Route path="dsa/m2" element={<M2LandingPage />} />
          <Route path="dsa/m2/suffix-array" element={<SuffixArrayPage />} />
          <Route path="dsa/m2/sa-is" element={<SAISPage />} />
          <Route path="dsa/m2/kasai" element={<KasaiPage />} />
          <Route path="dsa/m2/lcp" element={<LcpPage />} />
          <Route path="dsa/m2/suffix-automaton" element={<SuffixAutomatonPage />} />

          {/* Module 3 */}
          <Route path="dsa/m3" element={<M3LandingPage />} />
          <Route path="dsa/m3/levenshtein" element={<LevenshteinPage />} />
          <Route path="dsa/m3/damerau" element={<DamerauPage />} />
          <Route path="dsa/m3/bitmask" element={<BitmaskPage />} />
          <Route path="dsa/m3/matrix-chain" element={<MatrixChainPage />} />
          <Route path="dsa/m3/optimal-bst" element={<OptimalBSTPage />} />

          {/* Module 4 */}
          <Route path="dsa/m4" element={<M4LandingPage />} />
          <Route path="dsa/m4/ford-fulkerson" element={<FordFulkersonPage />} />
          <Route path="dsa/m4/edmonds-karp" element={<EdmondsKarpPage />} />
          <Route path="dsa/m4/dinic" element={<DinicPage />} />
          <Route path="dsa/m4/bipartite-matching" element={<BipartiteMatchingPage />} />
          <Route path="dsa/m4/konig" element={<KonigPage />} />
          <Route path="dsa/m4/max-flow-min-cut" element={<MaxFlowMinCutPage />} />

          {/* Module 5 */}
          <Route path="dsa/m5" element={<M5LandingPage />} />
          <Route path="dsa/m5/sat" element={<SatPage />} />
          <Route path="dsa/m5/3sat" element={<ThreeSatPage />} />
          <Route path="dsa/m5/3sat-to-clique" element={<ThreeSatToCliquePage />} />
          <Route path="dsa/m5/clique-to-independent-set" element={<CliqueToISPage />} />
          <Route path="dsa/m5/independent-set-to-vertex-cover" element={<ISToVCPage />} />
          <Route path="dsa/m5/vertex-cover-2approx" element={<VertexCoverApproxPage />} />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
