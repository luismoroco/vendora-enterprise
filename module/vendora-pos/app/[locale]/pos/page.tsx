"use client"

import { useState, useEffect } from "react"
import { Search, Barcode } from "lucide-react"
import { useTranslations } from 'next-intl'
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { toast } from "sonner"
import ProductGridNew from "../../components/product-grid-new"
import CartSidebar from "../../components/cart-sidebar"
import CategorySidebarNew from "../../components/category-sidebar-new"
import MainSidebar from "../../components/main-sidebar"
import ProductDetailModal from "../../components/product-detail-modal"
import { useCart } from "../../context/cart-context"
import { productService } from "@/lib/services/productService"
import { brandService } from "@/lib/services/brandService"
import type { Product, Brand } from "@/lib/types"

export default function POSPage() {
  const t = useTranslations()
  const [searchQuery, setSearchQuery] = useState("")
  const [barCodeQuery, setBarCodeQuery] = useState("")
  const [selectedCategoryIds, setSelectedCategoryIds] = useState<number[]>([])
  const [selectedBrandId, setSelectedBrandId] = useState<number | undefined>(undefined)
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const [productModalOpen, setProductModalOpen] = useState(false)
  const [notFoundModalOpen, setNotFoundModalOpen] = useState(false)
  const [searchingBarCode, setSearchingBarCode] = useState(false)
  const [brands, setBrands] = useState<Brand[]>([])
  const [loadingBrands, setLoadingBrands] = useState(true)
  const { addToCart } = useCart()

  useEffect(() => {
    const fetchBrands = async () => {
      try {
        setLoadingBrands(true)
        const response = await brandService.getBrands({ page: 1, size: 100 })
        setBrands(response.content)
      } catch (error) {
        console.error("Error fetching brands:", error)
        toast.error(t('brands.noBrands'))
      } finally {
        setLoadingBrands(false)
      }
    }

    fetchBrands()
  }, [t])

  const handleAddToCart = (product: Product) => {
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
      toast.error(t('pos.barcode'))
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
        setBarCodeQuery("")
      } else {
        setNotFoundModalOpen(true)
      }
    } catch (error) {
      console.error("Error searching by barcode:", error)
      toast.error(t('products.loadError'))
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
          <div className="flex items-center justify-between gap-4">
            <h1 className="text-2xl font-bold whitespace-nowrap">{t('pos.title')}</h1>
            
            <div className="flex items-center gap-2 flex-1">
              {/* Name Search */}
              <div className="relative flex-1">
                <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t('pos.searchProducts')}
                  className="pl-8"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
              </div>
              
              {/* Barcode Search */}
              <div className="relative flex-1">
                <Barcode className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t('pos.scanBarcode')}
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
                {searchingBarCode ? t('pos.searching') : t('pos.scan')}
              </Button>
            </div>
          </div>

          {/* Brands Filter Bar */}
          <div className="mt-4">
            <div className="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-thin scrollbar-thumb-gray-300 scrollbar-track-gray-100">
              <Badge
                variant={selectedBrandId === undefined ? "default" : "outline"}
                className="cursor-pointer whitespace-nowrap hover:bg-primary/90 px-4 py-2 text-sm"
                onClick={() => setSelectedBrandId(undefined)}
              >
                {t('pos.allBrands')}
              </Badge>
              {loadingBrands ? (
                <div className="flex gap-2">
                  {Array.from({ length: 5 }).map((_, i) => (
                    <div key={i} className="h-8 w-20 bg-muted animate-pulse rounded-full" />
                  ))}
                </div>
              ) : (
                brands.map((brand) => (
                  <Badge
                    key={brand.brandId}
                    variant={selectedBrandId === brand.brandId ? "default" : "outline"}
                    className="cursor-pointer whitespace-nowrap hover:bg-primary/90 px-4 py-2 text-sm"
                    onClick={() => setSelectedBrandId(brand.brandId)}
                  >
                    {brand.name}
                  </Badge>
                ))
              )}
            </div>
          </div>
        </div>

        <div className="flex-1 overflow-auto p-4">
          <ProductGridNew
            categoryIds={selectedCategoryIds}
            brandId={selectedBrandId}
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
          toast.success(`${product.name} ${t('products.addedToCart')}`)
        }}
      />

      {/* Not Found Modal */}
      <Dialog open={notFoundModalOpen} onOpenChange={setNotFoundModalOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{t('pos.productNotFound')}</DialogTitle>
            <DialogDescription>
              {t('pos.noProductWithBarcode')}: <span className="font-mono font-semibold">{barCodeQuery}</span>
            </DialogDescription>
          </DialogHeader>
          <div className="flex justify-end gap-2 pt-4">
            <Button
              onClick={() => {
                setNotFoundModalOpen(false)
                setBarCodeQuery("")
              }}
            >
              {t('common.ok')}
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}

