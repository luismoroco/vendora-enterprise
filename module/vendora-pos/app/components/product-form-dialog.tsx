"use client"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Checkbox } from "@/components/ui/checkbox"
import { toast } from "sonner"
import { productService } from "@/lib/services/productService"
import { brandService } from "@/lib/services/brandService"
import { categoryService } from "@/lib/services/categoryService"
import { providerService } from "@/lib/services/providerService"
import type { Product, Brand, ProductCategory, Provider } from "@/lib/types"
import { ScrollArea } from "@/components/ui/scroll-area"

interface ProductFormDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  product: Product | null
  onSuccess: () => void
}

export default function ProductFormDialog({
  open,
  onOpenChange,
  product,
  onSuccess,
}: ProductFormDialogProps) {
  const [name, setName] = useState("")
  const [barCode, setBarCode] = useState("")
  const [price, setPrice] = useState("")
  const [stock, setStock] = useState("")
  const [imageUrl, setImageUrl] = useState("")
  const [brandId, setBrandId] = useState<number | null>(null)
  const [providerId, setProviderId] = useState<number | null>(null)
  const [selectedCategoryIds, setSelectedCategoryIds] = useState<number[]>([])
  const [productStatusType, setProductStatusType] = useState<"ENABLED" | "DISABLED">("ENABLED")
  
  const [brands, setBrands] = useState<Brand[]>([])
  const [categories, setCategories] = useState<ProductCategory[]>([])
  const [providers, setProviders] = useState<Provider[]>([])
  
  const [loading, setLoading] = useState(false)
  const [loadingData, setLoadingData] = useState(true)

  // Load brands, categories, and providers
  useEffect(() => {
    const loadData = async () => {
      try {
        setLoadingData(true)
        const [brandsRes, categoriesRes, providersRes] = await Promise.all([
          brandService.getBrands({ page: 1, size: 100 }),
          categoryService.getCategories({ page: 1, size: 100 }),
          providerService.getProviders({ page: 1, size: 100 }),
        ])
        setBrands(brandsRes.content)
        setCategories(categoriesRes.content)
        setProviders(providersRes.content)
      } catch (error) {
        toast.error("Failed to load form data")
        console.error("Error loading form data:", error)
      } finally {
        setLoadingData(false)
      }
    }

    if (open) {
      loadData()
    }
  }, [open])

  // Populate form when editing
  useEffect(() => {
    if (product) {
      setName(product.name)
      setBarCode(product.barCode)
      setPrice(product.price.toString())
      setStock(product.stock.toString())
      setImageUrl(product.imageUrl || "")
      setBrandId(product.brand.brandId)
      setProviderId(product.provider.providerId)
      setSelectedCategoryIds(product.categories.map((c) => c.productCategoryId))
      setProductStatusType(product.productStatusType)
    } else {
      setName("")
      setBarCode("")
      setPrice("")
      setStock("")
      setImageUrl("")
      setBrandId(null)
      setProviderId(null)
      setSelectedCategoryIds([])
      setProductStatusType("ENABLED")
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

    if (!name.trim() || !barCode.trim() || !price || !stock || !brandId || !providerId) {
      toast.error("Please fill in all required fields")
      return
    }

    if (selectedCategoryIds.length === 0) {
      toast.error("Please select at least one category")
      return
    }

    try {
      setLoading(true)

      if (product) {
        // Update
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
      } else {
        // Create
        await productService.createProduct({
          name: name.trim(),
          barCode: barCode.trim(),
          price: parseFloat(price),
          stock: parseInt(stock),
          imageUrl: imageUrl.trim(),
          brandId,
          providerId,
          productCategoryIds: selectedCategoryIds,
        })
        toast.success("Product created successfully")
      }

      onSuccess()
    } catch (error: any) {
      toast.error(error.response?.data?.message || "Failed to save product")
      console.error("Error saving product:", error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl max-h-[90vh]">
        <DialogHeader>
          <DialogTitle>{product ? "Edit Product" : "Add Product"}</DialogTitle>
        </DialogHeader>

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

            <div className="space-y-2">
              <Label htmlFor="imageUrl">Image URL</Label>
              <Input
                id="imageUrl"
                value={imageUrl}
                onChange={(e) => setImageUrl(e.target.value)}
                placeholder="Enter image URL"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="brand">Brand *</Label>
                <Select
                  value={brandId?.toString()}
                  onValueChange={(value) => setBrandId(parseInt(value))}
                  disabled={loadingData || (product !== null)}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Select brand" />
                  </SelectTrigger>
                  <SelectContent>
                    {brands.map((brand) => (
                      <SelectItem key={brand.brandId} value={brand.brandId.toString()}>
                        {brand.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-2">
                <Label htmlFor="provider">Provider *</Label>
                <Select
                  value={providerId?.toString()}
                  onValueChange={(value) => setProviderId(parseInt(value))}
                  disabled={loadingData || (product !== null)}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Select provider" />
                  </SelectTrigger>
                  <SelectContent>
                    {providers.map((provider) => (
                      <SelectItem key={provider.providerId} value={provider.providerId.toString()}>
                        {provider.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            </div>

            {product && (
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
            )}

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

            <div className="flex justify-end gap-2 pt-4">
              <Button
                type="button"
                variant="outline"
                onClick={() => onOpenChange(false)}
                disabled={loading}
              >
                Cancel
              </Button>
              <Button type="submit" disabled={loading || loadingData}>
                {loading ? "Saving..." : product ? "Update" : "Create"}
              </Button>
            </div>
          </form>
        </ScrollArea>
      </DialogContent>
    </Dialog>
  )
}

