"use client"

import { useState, useEffect, useRef } from "react"
import Image from "next/image"
import { Eye, Plus, Minus, ShoppingCart } from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { toast } from "sonner"
import { productService } from "@/lib/services/productService"
import { useCart } from "../context/cart-context"
import type { Product, CartItem } from "@/lib/types"
import ProductDetailModal from "./product-detail-modal"

interface ProductGridNewProps {
  categoryIds?: number[]
  brandId?: number
  searchQuery: string
  onAddToCart: (product: Product) => void
}

export default function ProductGridNew({ categoryIds, brandId, searchQuery, onAddToCart }: ProductGridNewProps) {
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [loadingMore, setLoadingMore] = useState(false)
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const [modalOpen, setModalOpen] = useState(false)
  const [page, setPage] = useState(1)
  const [hasMore, setHasMore] = useState(true)
  const { cart, addToCart, updateQuantity } = useCart()
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
        categoryIds: categoryIds && categoryIds.length > 0 ? categoryIds : undefined,
        brandId: brandId || undefined,
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
  }, [categoryIds, brandId, searchQuery])

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

  const handleProductClick = (product: Product, event: React.MouseEvent) => {
    const target = event.target as HTMLElement
    // Don't open modal if clicking on cart controls
    if (target.closest('.cart-controls')) {
      return
    }
    setSelectedProduct(product)
    setModalOpen(true)
  }

  const handleAddToCart = (product: Product) => {
    if (product.stock > 0 && product.productStatusType === "ENABLED") {
      addToCart({
        id: product.productId,
        name: product.name,
        price: product.price,
        image: product.imageUrl,
        category: product.categories[0]?.name || "",
      })
      toast.success(`${product.name} added to cart`)
    } else {
      toast.error("Product not available")
    }
  }

  const handleQuantityChange = (productId: number, newQuantity: number) => {
    if (newQuantity < 0) return
    updateQuantity(productId, newQuantity)
  }

  const getCartQuantity = (productId: number) => {
    const cartItem = cart.find(item => item.id === productId)
    return cartItem?.quantity || 0
  }

  if (loading && products.length === 0) {
    return (
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
    )
  }

  return (
    <>
      <div className="grid grid-cols-3 gap-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 xl:grid-cols-7">
        {products.map((product) => {
          const profit = product.price - (product.cost || 0);
          const cartQuantity = getCartQuantity(product.productId);
          const isDisabled = product.productStatusType === "DISABLED" || product.stock <= 0;
          
          return (
            <Card
              key={product.productId}
              className={`overflow-hidden transition-all duration-200 hover:shadow-md cursor-pointer group relative ${
                product.productStatusType === "DISABLED" ? "bg-gray-300 opacity-60" : "bg-gray-50"
              }`}
              onClick={(e) => handleProductClick(product, e)}
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
                  {product.cost > 0 && (
                    <p className="text-[10px] text-muted-foreground leading-tight">
                      Profit: ${profit.toFixed(2)}
                    </p>
                  )}
                  <p className="text-[10px] text-muted-foreground">Stock: {product.stock}</p>
                </div>
              </CardContent>
              
              {/* Cart Controls Section */}
              <div className="cart-controls bg-gray-800 p-2 border-t border-gray-700" onClick={(e) => e.stopPropagation()}>
                {cartQuantity === 0 ? (
                  <Button
                    size="sm"
                    className="w-full h-8 bg-blue-600 hover:bg-blue-700 text-white"
                    onClick={() => handleAddToCart(product)}
                    disabled={isDisabled}
                  >
                    <ShoppingCart className="h-3 w-3 mr-1" />
                    Add
                  </Button>
                ) : (
                  <div className="flex items-center gap-1">
                    <Button
                      size="sm"
                      variant="secondary"
                      className="h-7 flex-1 bg-red-500 hover:bg-red-600 text-white px-2"
                      onClick={() => handleQuantityChange(product.productId, cartQuantity - 1)}
                    >
                      <Minus className="h-3 w-3" />
                    </Button>
                    <Input
                      type="number"
                      min="0"
                      value={cartQuantity}
                      onChange={(e) => handleQuantityChange(product.productId, parseInt(e.target.value) || 0)}
                      className="h-7 w-12 text-center text-xs font-semibold px-1 bg-white"
                    />
                    <Button
                      size="sm"
                      variant="secondary"
                      className="h-7 flex-1 bg-green-500 hover:bg-green-600 text-white px-2"
                      onClick={() => handleAddToCart(product)}
                      disabled={isDisabled}
                    >
                      <Plus className="h-3 w-3" />
                    </Button>
                  </div>
                )}
              </div>
            </Card>
          );
        })}

        {products.length === 0 && !loading && (
          <div className="col-span-full py-12 text-center">
            <p className="text-muted-foreground">No products found</p>
          </div>
        )}
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


