"use client"

import { Provider } from 'react-redux';
import { store } from './store';
import { Toaster } from '@/components/ui/sonner';

export function Providers({ children }: { children: React.ReactNode }) {
  return (
    <Provider store={store}>
      {children}
      <Toaster 
        position="top-center"
        toastOptions={{
          classNames: {
            error: 'bg-red-500 text-white border-red-600',
            success: 'bg-green-500 text-white border-green-600',
            warning: 'bg-yellow-500 text-white border-yellow-600',
            info: 'bg-blue-500 text-white border-blue-600',
          },
        }}
        richColors
      />
    </Provider>
  );
}

