import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AppShell } from './components/layout/AppShell';
import { DashboardPage } from './pages/DashboardPage';
import { StationsPage } from './pages/StationsPage';
import { M1LandingPage } from './pages/M1LandingPage';
import { KmpPage } from './pages/KmpPage';
import { ZFunctionPage } from './pages/ZFunctionPage';
import { RabinKarpPage } from './pages/RabinKarpPage';
import { AhoCorasickPage } from './pages/AhoCorasickPage';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AppShell />}>
          <Route index element={<DashboardPage />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="operations/stations" element={<StationsPage />} />
          <Route path="dsa/m1" element={<M1LandingPage />} />
          <Route path="dsa/m1/kmp" element={<KmpPage />} />
          <Route path="dsa/m1/z-function" element={<ZFunctionPage />} />
          <Route path="dsa/m1/rabin-karp" element={<RabinKarpPage />} />
          <Route path="dsa/m1/aho-corasick" element={<AhoCorasickPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
