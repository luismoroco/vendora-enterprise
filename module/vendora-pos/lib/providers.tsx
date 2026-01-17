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
            toast: 'text-white font-medium',
            error: '!bg-red-500 !text-white border-0',
            success: '!bg-green-500 !text-white border-0',
            warning: '!bg-yellow-500 !text-white border-0',
            info: '!bg-blue-500 !text-white border-0',
            title: '!text-white',
            description: '!text-white/90',
          },
        }}
      />
    </Provider>
  );
}

