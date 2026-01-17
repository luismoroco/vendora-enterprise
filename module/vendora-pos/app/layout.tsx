import type React from "react"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"
import { routing } from '../i18n/routing'

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "Vendora POS",
  description: "Modern Point of Sale System for Restaurants & Retail",
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang={routing.defaultLocale}>
      <body className={`${inter.className} bg-gray-100`}>
        {children}
      </body>
    </html>
  )
}
