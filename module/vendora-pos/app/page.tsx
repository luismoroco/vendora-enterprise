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
        <div className="sticky top-0 z-10 bg-background p-4 border-b">

          <div className="sticky top-0 z-10 bg-background p-4 border-b">
            <h1 className="text-2xl font-bold mb-4">Point of Sale</h1>

            <div className="flex items-center gap-2">
              {/* Name Search */}
              <div className="relative w-64">
                <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Search products..."
                  className="pl-8"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
              </div>

              {/* Barcode Search */}
              <div className="relative w-64">
                <Barcode className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Scan or enter barcode..."
                  className="pl-8"
                  value={barCodeQuery}
                  onChange={(e) => setBarCodeQuery(e.target.value)}
                  onKeyPress={handleKeyPress}
                  disabled={searchingBarCode}
                />
              </div>

              <Button
                onClick={() => handleBarCodeSearch()}
                disabled={searchingBarCode || !barCodeQuery.trim()}
                className="whitespace-nowrap"
              >
                {searchingBarCode ? "Searching..." : "Search"}
              </Button>
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
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Product Not Found</DialogTitle>
            <DialogDescription>
              No product was found with the barcode: <span className="font-mono font-semibold">{barCodeQuery}</span>
            </DialogDescription>
          </DialogHeader>
          <div className="flex justify-end gap-2 pt-4">
            <Button
              onClick={() => {
                setNotFoundModalOpen(false)
                setBarCodeQuery("")
              }}
            >
              OK
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}
