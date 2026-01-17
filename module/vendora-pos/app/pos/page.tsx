"use client"

import { useState } from "react"
import { Search } from "lucide-react"
import { Input } from "@/components/ui/input"
import ProductGridNew from "../components/product-grid-new"
import CartSidebar from "../components/cart-sidebar"
import CategorySidebarNew from "../components/category-sidebar-new"
import MainSidebar from "../components/main-sidebar"
import { useCart } from "../context/cart-context"
import type { Product } from "@/lib/types"

export default function POSPage() {
  const [searchQuery, setSearchQuery] = useState("")
  const [selectedCategoryIds, setSelectedCategoryIds] = useState<number[]>([])
  const { addToCart } = useCart()

  const handleAddToCart = (product: Product) => {
    // Convert Product to the cart context format
    addToCart({
      id: product.productId,
      name: product.name,
      price: product.price,
      image: product.imageUrl,
      category: product.categories[0]?.name || "",
    })
  }

  return (
    <div className="flex h-screen bg-background">
      <MainSidebar />
      <CategorySidebarNew
        selectedCategoryIds={selectedCategoryIds}
        onSelectCategory={setSelectedCategoryIds}
      />

      <main className="flex-1 flex flex-col h-screen overflow-hidden">
        <div className="sticky top-0 z-10 bg-background p-4 border-b">
          <div className="flex items-center justify-between">
            <h1 className="text-2xl font-bold">Point of Sale</h1>
            <div className="relative w-64">
              <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder="Search products..."
                className="pl-8"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
          </div>
        </div>

        <div className="flex-1 overflow-auto p-4">
          <ProductGridNew
            categoryIds={selectedCategoryIds}
            searchQuery={searchQuery}
            onAddToCart={handleAddToCart}
          />
        </div>
      </main>

      <CartSidebar />
    </div>
  )
}

