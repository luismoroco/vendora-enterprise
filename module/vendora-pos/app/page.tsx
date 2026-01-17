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
    addToCart({
      id: product.productId,
      name: product.name,
      price: product.price,
      image: product.imageUrl,
      category: product.categories[0]?.name || "",
    })
  }

  const handleBarCodeSearch = async () => {
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

      if (response.content?.length) {
        setSelectedProduct(response.content[0])
        setProductModalOpen(true)
        setBarCodeQuery("")
      } else {
        setNotFoundModalOpen(true)
      }
    } catch (error) {
      console.error(error)
      toast.error("Error searching for product")
    } finally {
      setSearchingBarCode(false)
    }
  }

  return (
    <div className="flex h-screen bg-background">
      <MainSidebar />

      <CategorySidebarNew
        selectedCategoryIds={selectedCategoryIds}
        onSelectCategory={setSelectedCategoryIds}
      />

      <main className="flex-1 flex flex-col overflow-hidden">
        {/* Header */}
        <div className="sticky top-0 z-10 bg-background border-b">
          <div className="px-6 pt-6 pb-4">
            <h1 className="text-3xl font-bold text-gray-800">Point of Sale</h1>
          </div>

          {/* Search Row */}
          <div className="px-6 pb-4">
            <div className="flex items-center gap-3 jus">
              {/* Text search */}
              <div className="relative flex-1">
                <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Search products by name..."
                  className="pl-10 h-11"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
              </div>

              {/* Barcode input */}
              <div className="relative">
                <Barcode className="absolute left-3 top-3 h-4 w-4 text-blue-600" />
                <Input
                  placeholder="Barcode"
                  className="
                    pl-10
                    h-11
                    w-[170px]
                    border-2
                    border-blue-600
                    focus:border-blue-700
                    focus:ring-blue-600
                    font-mono
                  "
                  value={barCodeQuery}
                  onChange={(e) => setBarCodeQuery(e.target.value)}
                  onKeyDown={(e) => e.key === "Enter" && handleBarCodeSearch()}
                  disabled={searchingBarCode}
                  autoFocus
                />
              </div>

              {/* Scan button */}
              <Button
                onClick={handleBarCodeSearch}
                disabled={searchingBarCode || !barCodeQuery.trim()}
                className="h-11 bg-blue-600 hover:bg-blue-700"
              >
                Scan
              </Button>
            </div>
          </div>
        </div>

        {/* Products */}
        <div className="flex-1 overflow-auto p-4">
          <ProductGridNew
            categoryIds={selectedCategoryIds}
            searchQuery={searchQuery}
            onAddToCart={(product) => {}}
          />
        </div>
      </main>

      <CartSidebar />

      {/* Product Detail */}
      <ProductDetailModal
        product={selectedProduct}
        open={productModalOpen}
        onOpenChange={setProductModalOpen}
      />

      {/* Not Found Modal */}
      <Dialog open={notFoundModalOpen} onOpenChange={setNotFoundModalOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle className="text-center text-2xl">
              Product Not Found
            </DialogTitle>
          </DialogHeader>

          <div className="flex flex-col items-center py-6 space-y-4">
            <div className="text-8xl">😔</div>
            <DialogDescription className="text-center">
              No product found with this barcode
            </DialogDescription>

            <div className="bg-muted px-4 py-2 rounded-md font-mono text-lg">
              {barCodeQuery}
            </div>
          </div>

          <Button
            onClick={() => {
              setNotFoundModalOpen(false)
              setBarCodeQuery("")
            }}
          >
            Try Again
          </Button>
        </DialogContent>
      </Dialog>
    </div>
  )
}
