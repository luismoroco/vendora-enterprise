import type React from "react"
import type { Metadata } from "next"
import { NextIntlClientProvider } from 'next-intl'
import { getMessages } from 'next-intl/server'
import { Inter } from "next/font/google"
import "./globals.css"
import { CartProvider } from "./context/cart-context"
import { Providers } from "@/lib/providers"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "Vendora POS",
  description: "Modern Point of Sale System for Restaurants & Retail",
}

export default async function RootLayout({
  children,
  params
}: Readonly<{
  children: React.ReactNode
  params: Promise<{ locale: string }>
}>) {
  const { locale } = await params
  const messages = await getMessages()

  return (
    <html lang={locale}>
      <body className={`${inter.className} bg-gray-100`}>
        <NextIntlClientProvider messages={messages}>
          <Providers>
            <CartProvider>{children}</CartProvider>
          </Providers>
        </NextIntlClientProvider>
      </body>
    </html>
  )
}
