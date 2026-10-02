import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AppShell } from './components/layout/AppShell';
import { DashboardPage } from './pages/DashboardPage';
import { StationsPage } from './pages/StationsPage';
import { KmpPage } from './pages/KmpPage';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AppShell />}>
          <Route index element={<DashboardPage />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="operations/stations" element={<StationsPage />} />
          <Route path="dsa/m1/kmp" element={<KmpPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
