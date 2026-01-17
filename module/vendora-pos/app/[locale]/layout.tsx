import type React from "react"
import { NextIntlClientProvider } from 'next-intl'
import { getMessages } from 'next-intl/server'
import { setRequestLocale } from 'next-intl/server'
import { routing } from '@/i18n/routing'
import { CartProvider } from "../context/cart-context"
import { Providers } from "@/lib/providers"

export default async function LocaleLayout({
  children,
  params
}: Readonly<{
  children: React.ReactNode
  params: Promise<{ locale: string }>
}>) {
  const { locale } = await params
  
  // Validate that the incoming `locale` parameter is valid
  if (!routing.locales.includes(locale as any)) {
    // This will be handled by the middleware, but just in case
    return null
  }

  // Enable static rendering
  setRequestLocale(locale)
  
  const messages = await getMessages()

  return (
    <NextIntlClientProvider messages={messages}>
      <Providers>
        <CartProvider>{children}</CartProvider>
      </Providers>
    </NextIntlClientProvider>
  )
}

