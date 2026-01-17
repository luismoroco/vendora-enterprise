"use client"

import Image from "next/image"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { ShoppingCart, Package, DollarSign, Tag, Building2 } from "lucide-react"
import type { Product } from "@/lib/types"

interface ProductDetailModalProps {
  product: Product | null
  open: boolean
  onOpenChange: (open: boolean) => void
  onAddToCart: (product: Product) => void
}

export default function ProductDetailModal({
  product,
  open,
  onOpenChange,
  onAddToCart,
}: ProductDetailModalProps) {
  if (!product) return null

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-3xl">
        <DialogHeader>
          <DialogTitle className="text-2xl">{product.name}</DialogTitle>
        </DialogHeader>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Product Image */}
          <div className="relative aspect-square rounded-lg overflow-hidden bg-muted">
            <Image
              src={product.imageUrl || "/placeholder.svg"}
              alt={product.name}
              fill
              className="object-cover"
            />
          </div>

          {/* Product Details */}
          <div className="flex flex-col gap-4">
            {/* Price */}
            <div className="flex items-center gap-2">
              <DollarSign className="h-5 w-5 text-muted-foreground" />
              <span className="text-3xl font-bold">${product.price.toFixed(2)}</span>
            </div>

            {/* Stock */}
            <div className="flex items-center gap-2">
              <Package className="h-5 w-5 text-muted-foreground" />
              <span className="text-sm">
                Stock: <span className="font-semibold">{product.stock} units</span>
              </span>
              <Badge variant={product.stock > 0 ? "default" : "destructive"}>
                {product.stock > 0 ? "In Stock" : "Out of Stock"}
              </Badge>
            </div>

            {/* Status */}
            <div className="flex items-center gap-2">
              <Badge variant={product.productStatusType === "ENABLED" ? "default" : "secondary"}>
                {product.productStatusType}
              </Badge>
            </div>

            {/* Barcode */}
            <div className="flex items-center gap-2">
              <Tag className="h-5 w-5 text-muted-foreground" />
              <span className="text-sm text-muted-foreground">
                Barcode: <span className="font-mono">{product.barCode}</span>
              </span>
            </div>

            {/* Brand */}
            <div className="flex items-center gap-2">
              <Building2 className="h-5 w-5 text-muted-foreground" />
              <span className="text-sm">
                Brand: <span className="font-semibold">{product.brand.name}</span>
              </span>
            </div>

            {/* Provider */}
            <div className="flex items-center gap-2">
              <Building2 className="h-5 w-5 text-muted-foreground" />
              <span className="text-sm">
                Provider: <span className="font-semibold">{product.provider.name}</span>
              </span>
            </div>

            {/* Categories */}
            {product.categories && product.categories.length > 0 && (
              <div className="space-y-2">
                <span className="text-sm text-muted-foreground">Categories:</span>
                <div className="flex flex-wrap gap-2">
                  {product.categories.map((category) => (
                    <Badge key={category.productCategoryId} variant="outline">
                      {category.name}
                    </Badge>
                  ))}
                </div>
              </div>
            )}

            {/* Add to Cart Button */}
            <Button
              className="w-full mt-4"
              size="lg"
              onClick={() => {
                onAddToCart(product)
                onOpenChange(false)
              }}
              disabled={product.stock <= 0 || product.productStatusType === "DISABLED"}
            >
              <ShoppingCart className="mr-2 h-5 w-5" />
              Add to Cart
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  )
}

