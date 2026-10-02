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

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AppShell />}>
          <Route index element={<DashboardPage />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="operations/stations" element={<StationsPage />} />
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

          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
