"use client"

import { useState, useEffect, useRef, useCallback } from "react"
import { Plus, Eye, Search } from "lucide-react"
import Image from "next/image"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent } from "@/components/ui/card"
import { toast } from "sonner"
import { productService } from "@/lib/services/productService"
import type { Product } from "@/lib/types"
import MainSidebar from "../components/main-sidebar"
import ProductFormDialog from "../components/product-form-dialog"
import ProductDetailEditModal from "../components/product-detail-edit-modal"

export default function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [loadingMore, setLoadingMore] = useState(false)
  const [searchQuery, setSearchQuery] = useState("")
  const [page, setPage] = useState(1)
  const [hasMore, setHasMore] = useState(true)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [detailModalOpen, setDetailModalOpen] = useState(false)
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const observerTarget = useRef<HTMLDivElement>(null)

  const fetchProducts = async (pageNum: number, isNewSearch = false) => {
    try {
      if (pageNum === 1) {
        setLoading(true)
      } else {
        setLoadingMore(true)
      }
      
      const response = await productService.getProducts({
        name: searchQuery || undefined,
        page: pageNum,
        size: 20,
      })
      
      if (isNewSearch) {
        setProducts(response.content)
      } else {
        setProducts(prev => [...prev, ...response.content])
      }
      
      setHasMore(pageNum < response.totalPages)
    } catch (error) {
      toast.error("Failed to load products")
      console.error("Error fetching products:", error)
    } finally {
      setLoading(false)
      setLoadingMore(false)
    }
  }

  useEffect(() => {
    setPage(1)
    setProducts([])
    fetchProducts(1, true)
  }, [searchQuery])

  useEffect(() => {
    if (page > 1) {
      fetchProducts(page)
    }
  }, [page])

  // Infinite scroll observer
  useEffect(() => {
    const observer = new IntersectionObserver(
      entries => {
        if (entries[0].isIntersecting && hasMore && !loading && !loadingMore) {
          setPage(prev => prev + 1)
        }
      },
      { threshold: 0.1 }
    )

    if (observerTarget.current) {
      observer.observe(observerTarget.current)
    }

    return () => {
      if (observerTarget.current) {
        observer.unobserve(observerTarget.current)
      }
    }
  }, [hasMore, loading, loadingMore])

  const handleProductClick = (product: Product) => {
    setSelectedProduct(product)
    setDetailModalOpen(true)
  }

  const handleAdd = () => {
    setSelectedProduct(null)
    setDialogOpen(true)
  }

  const handleSuccess = () => {
    fetchProducts()
    setDialogOpen(false)
    setDetailModalOpen(false)
  }

  return (
    <div className="flex h-screen bg-background">
      <MainSidebar />

      <main className="flex-1 flex flex-col h-screen overflow-hidden">
        <div className="sticky top-0 z-10 bg-background p-6 border-b">
          <div className="flex items-center justify-between mb-4">
            <h1 className="text-3xl font-bold">Products</h1>
            <Button onClick={handleAdd}>
              <Plus className="mr-2 h-4 w-4" />
              Add Product
            </Button>
          </div>

          <div className="relative w-full max-w-sm">
            <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Search products..."
              className="pl-8"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
        </div>

        <div className="flex-1 overflow-auto p-6">
          {loading && products.length === 0 ? (
            <div className="grid grid-cols-3 gap-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 xl:grid-cols-7">
              {Array.from({ length: 14 }).map((_, i) => (
                <Card key={i} className="overflow-hidden animate-pulse">
                  <div className="aspect-square bg-muted" />
                  <CardContent className="p-2">
                    <div className="h-3 bg-muted rounded mb-1" />
                    <div className="h-2 bg-muted rounded w-1/2" />
                  </CardContent>
                </Card>
              ))}
            </div>
          ) : products.length === 0 ? (
            <div className="py-12 text-center">
              <p className="text-muted-foreground">No products found</p>
            </div>
          ) : (
            <>
              <div className="grid grid-cols-3 gap-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 xl:grid-cols-7">
                {products.map((product) => (
                  <Card
                    key={product.productId}
                    className={`overflow-hidden transition-all duration-200 hover:shadow-md cursor-pointer group relative ${
                      product.productStatusType === "DISABLED" ? "bg-gray-300 opacity-60" : "bg-gray-50"
                    }`}
                    onClick={() => handleProductClick(product)}
                  >
                    <div className="relative aspect-square">
                      <div className="absolute inset-0 flex items-center justify-center bg-black/50 opacity-0 transition-opacity group-hover:opacity-100 z-10">
                        <Button
                          size="icon"
                          variant="secondary"
                          className="h-10 w-10"
                        >
                          <Eye className="h-5 w-5" />
                        </Button>
                      </div>
                      <Image
                        src={product.imageUrl || "/placeholder.svg"}
                        alt={product.name}
                        fill
                        className="object-cover"
                      />
                    {product.stock <= 0 && (
                      <div className="absolute top-2 right-2 bg-destructive text-destructive-foreground px-2 py-1 rounded text-xs font-semibold">
                        Out of Stock
                      </div>
                    )}
                    {product.productStatusType === "DISABLED" && (
                      <div className="absolute top-2 left-2 bg-orange-500 text-white px-2 py-1 rounded text-xs font-semibold">
                        DISABLED
                      </div>
                    )}
                  </div>
                    <CardContent className="p-2">
                      <div>
                        <h3 className="text-sm font-medium line-clamp-2 mb-1 h-10">{product.name}</h3>
                        <p className="text-sm font-bold text-green-600">${product.price.toFixed(2)}</p>
                        <p className="text-[10px] text-muted-foreground">Stock: {product.stock}</p>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>

              {/* Infinite scroll trigger */}
              {hasMore && (
                <div ref={observerTarget} className="flex justify-center py-6">
                  {loadingMore && (
                    <div className="grid grid-cols-3 gap-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 xl:grid-cols-7">
                      {Array.from({ length: 7 }).map((_, i) => (
                        <Card key={i} className="overflow-hidden animate-pulse">
                          <div className="aspect-square bg-muted" />
                          <CardContent className="p-2">
                            <div className="h-3 bg-muted rounded mb-1" />
                            <div className="h-2 bg-muted rounded w-1/2" />
                          </CardContent>
                        </Card>
                      ))}
                    </div>
                  )}
                </div>
              )}
            </>
          )}
        </div>
      </main>

      <ProductFormDialog
        open={dialogOpen}
        onOpenChange={setDialogOpen}
        product={selectedProduct}
        onSuccess={handleSuccess}
      />

      <ProductDetailEditModal
        product={selectedProduct}
        open={detailModalOpen}
        onOpenChange={setDetailModalOpen}
        onSuccess={handleSuccess}
      />
    </div>
  )
}


