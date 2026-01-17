"use client"

import { useState, useEffect } from "react"
import Image from "next/image"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Badge } from "@/components/ui/badge"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Checkbox } from "@/components/ui/checkbox"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Package, DollarSign, Tag, Building2 } from "lucide-react"
import { toast } from "sonner"
import { productService } from "@/lib/services/productService"
import { categoryService } from "@/lib/services/categoryService"
import type { Product, ProductCategory } from "@/lib/types"
import ImageUpload from "./image-upload"

interface ProductDetailEditModalProps {
  product: Product | null
  open: boolean
  onOpenChange: (open: boolean) => void
  onSuccess: () => void
}

export default function ProductDetailEditModal({
  product,
  open,
  onOpenChange,
  onSuccess,
}: ProductDetailEditModalProps) {
  const [isEditing, setIsEditing] = useState(false)
  const [name, setName] = useState("")
  const [barCode, setBarCode] = useState("")
  const [price, setPrice] = useState("")
  const [stock, setStock] = useState("")
  const [imageUrl, setImageUrl] = useState("")
  const [productStatusType, setProductStatusType] = useState<"ENABLED" | "DISABLED">("ENABLED")
  const [selectedCategoryIds, setSelectedCategoryIds] = useState<number[]>([])
  const [categories, setCategories] = useState<ProductCategory[]>([])
  const [loading, setLoading] = useState(false)
  const [loadingData, setLoadingData] = useState(true)

  useEffect(() => {
    const loadCategories = async () => {
      try {
        setLoadingData(true)
        const categoriesRes = await categoryService.getCategories({ page: 1, size: 100 })
        setCategories(categoriesRes.content)
      } catch (error) {
        toast.error("Failed to load categories")
        console.error("Error loading categories:", error)
      } finally {
        setLoadingData(false)
      }
    }

    if (open) {
      loadCategories()
    }
  }, [open])

  useEffect(() => {
    if (product) {
      setName(product.name)
      setBarCode(product.barCode)
      setPrice(product.price.toString())
      setStock(product.stock.toString())
      setImageUrl(product.imageUrl || "")
      setProductStatusType(product.productStatusType)
      setSelectedCategoryIds(product.categories.map((c) => c.productCategoryId))
      setIsEditing(false)
    }
  }, [product])

  const toggleCategory = (categoryId: number) => {
    setSelectedCategoryIds((prev) =>
      prev.includes(categoryId)
        ? prev.filter((id) => id !== categoryId)
        : [...prev, categoryId]
    )
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!name.trim() || !barCode.trim() || !price || !stock) {
      toast.error("Please fill in all required fields")
      return
    }

    if (selectedCategoryIds.length === 0) {
      toast.error("Please select at least one category")
      return
    }

    if (!product) return

    try {
      setLoading(true)
      await productService.updateProduct(product.productId, {
        name: name.trim(),
        barCode: barCode.trim(),
        price: parseFloat(price),
        stock: parseInt(stock),
        imageUrl: imageUrl.trim() || undefined,
        productCategoryIds: selectedCategoryIds,
        productStatusType,
      })
      toast.success("Product updated successfully")
      setIsEditing(false)
      onSuccess()
    } catch (error: any) {
      toast.error(error.response?.data?.message || "Failed to update product")
      console.error("Error updating product:", error)
    } finally {
      setLoading(false)
    }
  }

  if (!product) return null

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-4xl max-h-[90vh]">
        <DialogHeader>
          <DialogTitle className="text-2xl flex items-center gap-2">
            <Package className="h-6 w-6" />
            {isEditing ? "Edit Product" : product.name}
          </DialogTitle>
        </DialogHeader>

        {!isEditing ? (
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
              <div className="flex items-center gap-2">
                <DollarSign className="h-5 w-5 text-muted-foreground" />
                <span className="text-3xl font-bold">${product.price.toFixed(2)}</span>
              </div>

              <div>
                <Label className="text-muted-foreground">Product ID</Label>
                <p className="text-lg font-semibold">{product.productId}</p>
              </div>

              <div className="flex items-center gap-2">
                <Package className="h-5 w-5 text-muted-foreground" />
                <span className="text-sm">
                  Stock: <span className="font-semibold">{product.stock} units</span>
                </span>
                <Badge variant={product.stock > 0 ? "default" : "destructive"}>
                  {product.stock > 0 ? "In Stock" : "Out of Stock"}
                </Badge>
              </div>

              <div>
                <Label className="text-muted-foreground">Status</Label>
                <div className="mt-1">
                  <Badge variant={product.productStatusType === "ENABLED" ? "default" : "secondary"}>
                    {product.productStatusType}
                  </Badge>
                </div>
              </div>

              <div className="flex items-center gap-2">
                <Tag className="h-5 w-5 text-muted-foreground" />
                <span className="text-sm text-muted-foreground">
                  Barcode: <span className="font-mono">{product.barCode}</span>
                </span>
              </div>

              <div className="flex items-center gap-2">
                <Building2 className="h-5 w-5 text-muted-foreground" />
                <span className="text-sm">
                  Brand: <span className="font-semibold">{product.brand.name}</span>
                </span>
              </div>

              <div className="flex items-center gap-2">
                <Building2 className="h-5 w-5 text-muted-foreground" />
                <span className="text-sm">
                  Provider: <span className="font-semibold">{product.provider.name}</span>
                </span>
              </div>

              {product.categories && product.categories.length > 0 && (
                <div className="space-y-2">
                  <Label className="text-muted-foreground">Categories</Label>
                  <div className="flex flex-wrap gap-2">
                    {product.categories.map((category) => (
                      <Badge key={category.productCategoryId} variant="outline">
                        {category.name}
                      </Badge>
                    ))}
                  </div>
                </div>
              )}

              <Button
                className="w-full mt-4"
                size="lg"
                onClick={() => setIsEditing(true)}
              >
                Edit Product
              </Button>
            </div>
          </div>
        ) : (
          <ScrollArea className="max-h-[calc(90vh-120px)] pr-4">
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="name">Name *</Label>
                  <Input
                    id="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Enter product name"
                    required
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="barCode">Barcode *</Label>
                  <Input
                    id="barCode"
                    value={barCode}
                    onChange={(e) => setBarCode(e.target.value)}
                    placeholder="Enter barcode"
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="price">Price *</Label>
                  <Input
                    id="price"
                    type="number"
                    step="0.01"
                    min="0"
                    value={price}
                    onChange={(e) => setPrice(e.target.value)}
                    placeholder="0.00"
                    required
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="stock">Stock *</Label>
                  <Input
                    id="stock"
                    type="number"
                    min="0"
                    value={stock}
                    onChange={(e) => setStock(e.target.value)}
                    placeholder="0"
                    required
                  />
                </div>
              </div>

              <ImageUpload
                label="Product Image"
                value={imageUrl}
                onChange={setImageUrl}
                disabled={loading || loadingData}
              />

              <div className="space-y-2">
                <Label htmlFor="status">Status *</Label>
                <Select
                  value={productStatusType}
                  onValueChange={(value) => setProductStatusType(value as "ENABLED" | "DISABLED")}
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="ENABLED">Enabled</SelectItem>
                    <SelectItem value="DISABLED">Disabled</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-2">
                <Label>Categories *</Label>
                <div className="border rounded-lg p-4 space-y-2 max-h-48 overflow-y-auto">
                  {categories.map((category) => (
                    <div key={category.productCategoryId} className="flex items-center space-x-2">
                      <Checkbox
                        id={`category-${category.productCategoryId}`}
                        checked={selectedCategoryIds.includes(category.productCategoryId)}
                        onCheckedChange={() => toggleCategory(category.productCategoryId)}
                      />
                      <Label
                        htmlFor={`category-${category.productCategoryId}`}
                        className="cursor-pointer flex-1"
                      >
                        {category.name}
                      </Label>
                    </div>
                  ))}
                </div>
              </div>

              <div>
                <Label className="text-muted-foreground">Brand (Cannot be changed)</Label>
                <p className="text-lg font-semibold">{product.brand.name}</p>
              </div>

              <div>
                <Label className="text-muted-foreground">Provider (Cannot be changed)</Label>
                <p className="text-lg font-semibold">{product.provider.name}</p>
              </div>

              <div className="flex justify-end gap-2 pt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => {
                    setIsEditing(false)
                    if (product) {
                      setName(product.name)
                      setBarCode(product.barCode)
                      setPrice(product.price.toString())
                      setStock(product.stock.toString())
                      setImageUrl(product.imageUrl || "")
                      setProductStatusType(product.productStatusType)
                      setSelectedCategoryIds(product.categories.map((c) => c.productCategoryId))
                    }
                  }}
                  disabled={loading}
                >
                  Cancel
                </Button>
                <Button type="submit" disabled={loading || loadingData}>
                  {loading ? "Saving..." : "Save Changes"}
                </Button>
              </div>
            </form>
          </ScrollArea>
        )}
      </DialogContent>
    </Dialog>
  )
}

