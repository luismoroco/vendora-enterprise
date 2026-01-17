"use client"

import { useState, useEffect } from "react"
import Image from "next/image"
import { PlusCircle, Eye } from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { toast } from "sonner"
import { productService } from "@/lib/services/productService"
import type { Product, CartItem } from "@/lib/types"
import ProductDetailModal from "./product-detail-modal"

interface ProductGridNewProps {
  categoryIds?: number[]
  searchQuery: string
  onAddToCart: (product: Product) => void
}

export default function ProductGridNew({ categoryIds, searchQuery, onAddToCart }: ProductGridNewProps) {
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const [modalOpen, setModalOpen] = useState(false)
  const [page, setPage] = useState(1)
  const [totalPages, setTotalPages] = useState(1)

  const fetchProducts = async () => {
    try {
      setLoading(true)
      const response = await productService.getProducts({
        name: searchQuery || undefined,
        categoryIds: categoryIds && categoryIds.length > 0 ? categoryIds : undefined,
        page,
        size: 20,
      })
      setProducts(response.content)
      setTotalPages(response.totalPages)
    } catch (error) {
      toast.error("Failed to load products")
      console.error("Error fetching products:", error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchProducts()
  }, [page, categoryIds, searchQuery])

  const handleProductClick = (product: Product, event: React.MouseEvent) => {
    // Check if the click was on the quick add button
    const target = event.target as HTMLElement
    if (target.closest('.quick-add-btn')) {
      handleQuickAdd(product)
    } else {
      setSelectedProduct(product)
      setModalOpen(true)
    }
  }

  const handleQuickAdd = (product: Product) => {
    if (product.stock > 0 && product.productStatusType === "ENABLED") {
      onAddToCart(product)
      toast.success(`${product.name} added to cart`)
    } else {
      toast.error("Product not available")
    }
  }

  if (loading && products.length === 0) {
    return (
      <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 xl:grid-cols-5">
        {Array.from({ length: 10 }).map((_, i) => (
          <Card key={i} className="overflow-hidden animate-pulse">
            <div className="aspect-square bg-muted" />
            <CardContent className="p-3">
              <div className="h-4 bg-muted rounded mb-2" />
              <div className="h-3 bg-muted rounded w-1/2" />
            </CardContent>
          </Card>
        ))}
      </div>
    )
  }

  return (
    <>
      <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 xl:grid-cols-5">
        {products.map((product) => {
          const profit = product.price - (product.cost || 0);
          const profitPercentage = product.cost ? ((profit / product.cost) * 100) : 0;
          
          return (
            <Card
              key={product.productId}
              className="overflow-hidden transition-all duration-200 hover:shadow-md cursor-pointer group relative bg-gray-50"
              onClick={(e) => handleProductClick(product, e)}
            >
              <div className="relative aspect-square">
                <div className="absolute inset-0 flex items-center justify-center bg-black/50 opacity-0 transition-opacity group-hover:opacity-100 z-10">
                  <div className="flex gap-2">
                    <Button
                      size="icon"
                      variant="secondary"
                      className="quick-add-btn h-10 w-10"
                      onClick={(e) => {
                        e.stopPropagation()
                        handleQuickAdd(product)
                      }}
                    >
                      <PlusCircle className="h-5 w-5" />
                    </Button>
                    <Button
                      size="icon"
                      variant="secondary"
                      className="h-10 w-10"
                    >
                      <Eye className="h-5 w-5" />
                    </Button>
                  </div>
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
              </div>
              <CardContent className="p-3">
                <div>
                  <h3 className="font-medium line-clamp-1">{product.name}</h3>
                  <p className="text-sm font-semibold text-green-600">${product.price.toFixed(2)}</p>
                  {product.cost > 0 && (
                    <p className="text-xs text-muted-foreground">
                      Cost: ${product.cost.toFixed(2)} | Profit: ${profit.toFixed(2)}
                    </p>
                  )}
                  <p className="text-xs text-muted-foreground mt-1">Stock: {product.stock}</p>
                </div>
              </CardContent>
            </Card>
          );
        })}

        {products.length === 0 && !loading && (
          <div className="col-span-full py-12 text-center">
            <p className="text-muted-foreground">No products found</p>
          </div>
        )}
      </div>

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-6">
          <Button
            variant="outline"
            onClick={() => setPage((p) => Math.max(1, p - 1))}
            disabled={page === 1}
          >
            Previous
          </Button>
          <span className="flex items-center px-4">
            Page {page} of {totalPages}
          </span>
          <Button
            variant="outline"
            onClick={() => setPage((p) => Math.min(totalPages, p + 1))}
            disabled={page === totalPages}
          >
            Next
          </Button>
        </div>
      )}

      <ProductDetailModal
        product={selectedProduct}
        open={modalOpen}
        onOpenChange={setModalOpen}
        onAddToCart={(product) => {
          onAddToCart(product)
          toast.success(`${product.name} added to cart`)
        }}
      />
    </>
  )
}

