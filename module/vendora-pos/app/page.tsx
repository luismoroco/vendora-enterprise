"use client"

import { useState } from "react"
import { Search, Barcode } from "lucide-react"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { toast } from "sonner"
import ProductGridNew from "./components/product-grid-new"
import CartSidebar from "./components/cart-sidebar"
import CategorySidebarNew from "./components/category-sidebar-new"
import MainSidebar from "./components/main-sidebar"
import ProductDetailModal from "./components/product-detail-modal"
import { useCart } from "./context/cart-context"
import { productService } from "@/lib/services/productService"
import type { Product } from "@/lib/types"

export default function POSPage() {
  const [searchQuery, setSearchQuery] = useState("")
  const [barCodeQuery, setBarCodeQuery] = useState("")
  const [selectedCategoryIds, setSelectedCategoryIds] = useState<number[]>([])
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const [productModalOpen, setProductModalOpen] = useState(false)
  const [notFoundModalOpen, setNotFoundModalOpen] = useState(false)
  const [searchingBarCode, setSearchingBarCode] = useState(false)
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

  const handleBarCodeSearch = async (e?: React.FormEvent) => {
    e?.preventDefault()
    
    if (!barCodeQuery.trim()) {
      toast.error("Please enter a barcode")
      return
    }

    try {
      setSearchingBarCode(true)
      const response = await productService.getProducts({
        barCode: barCodeQuery.trim(),
        page: 1,
        size: 1,
      })

      if (response.content && response.content.length > 0) {
        setSelectedProduct(response.content[0])
        setProductModalOpen(true)
        setBarCodeQuery("") // Clear the barcode input
      } else {
        setNotFoundModalOpen(true)
      }
    } catch (error) {
      console.error("Error searching by barcode:", error)
      toast.error("Error searching for product")
    } finally {
      setSearchingBarCode(false)
    }
  }

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === "Enter") {
      handleBarCodeSearch()
    }
  }

  return (
    <div className="flex h-screen bg-background">
      <MainSidebar />
      <CategorySidebarNew
        selectedCategoryIds={selectedCategoryIds}
        onSelectCategory={setSelectedCategoryIds}
      />

      <main className="flex-1 flex flex-col h-screen overflow-hidden">
        <div className="sticky top-0 z-10 bg-background border-b">
          {/* Title */}
          <div className="px-6 pt-6 pb-4">
            <h1 className="text-3xl font-bold text-gray-800">Point of Sale</h1>
          </div>
 
          {/* Barcode Search - Prominent */}
          <div className="px-6 pb-4">
            <div className="bg-gradient-to-r from-blue-50 to-indigo-50 border-2 border-blue-200 rounded-lg p-4 shadow-sm">
              <div className="flex items-center gap-3">
                <div className="bg-blue-500 p-3 rounded-lg">
                  <Barcode className="h-6 w-6 text-white" />
                </div>
                <div className="flex-1">
                  <label className="text-sm font-semibold text-gray-700 mb-1 block">
                    Scan Barcode
                  </label>
                  <div className="flex items-center gap-2">
                    <Input
                      placeholder="Scan or type barcode here..."
                      className="text-lg h-12 border-2 border-blue-300 focus:border-blue-500"
                      value={barCodeQuery}
                      onChange={(e) => setBarCodeQuery(e.target.value)}
                      onKeyPress={handleKeyPress}
                      disabled={searchingBarCode}
                      autoFocus
                    />
                    <Button
                      onClick={() => handleBarCodeSearch()}
                      disabled={searchingBarCode || !barCodeQuery.trim()}
                      size="lg"
                      className="h-12 px-6 bg-blue-600 hover:bg-blue-700"
                    >
                      {searchingBarCode ? "Searching..." : "Search"}
                    </Button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Name Search - Secondary */}
          <div className="px-6 pb-4">
            <div className="relative max-w-md">
              <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder="Search products by name..."
                className="pl-10"
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

      {/* Product Detail Modal */}
      <ProductDetailModal
        product={selectedProduct}
        open={productModalOpen}
        onOpenChange={setProductModalOpen}
        onAddToCart={(product) => {
          handleAddToCart(product)
          toast.success(`${product.name} added to cart`)
        }}
      />

      {/* Not Found Modal */}
      <Dialog open={notFoundModalOpen} onOpenChange={setNotFoundModalOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle className="text-center text-2xl">Product Not Found</DialogTitle>
          </DialogHeader>
          <div className="flex flex-col items-center justify-center py-6 space-y-4">
            {/* Sad Icon/Image */}
            <div className="text-8xl">😔</div>
            <DialogDescription className="text-center text-base">
              We couldn't find any product with the barcode:
            </DialogDescription>
            <div className="bg-muted px-4 py-2 rounded-md">
              <span className="font-mono font-semibold text-lg">{barCodeQuery}</span>
            </div>
            <p className="text-sm text-muted-foreground text-center">
              Please check the barcode and try again
            </p>
          </div>
          <div className="flex justify-center gap-2 pt-2">
            <Button
              className="w-full"
              onClick={() => {
                setNotFoundModalOpen(false)
                setBarCodeQuery("")
              }}
            >
              Try Again
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}
