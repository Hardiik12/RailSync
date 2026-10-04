import React from 'react';

export const DataOriginBadge = ({ origin = 'SYNTHETIC', className = '' }) => {
  const isPublic = origin === 'PUBLIC_DATA';

  return (
    <span
      className={`inline-flex items-center px-2 py-0.5 rounded text-[10px] font-mono font-medium border ${
        isPublic
          ? 'bg-teal-500/10 text-teal-400 border-teal-500/20'
          : 'bg-[#172033] text-[#94A3B8] border-[#263449]'
      } ${className}`}
    >
      {isPublic ? 'PUBLIC DATA' : 'SYNTHETIC'}
    </span>
  );
};
