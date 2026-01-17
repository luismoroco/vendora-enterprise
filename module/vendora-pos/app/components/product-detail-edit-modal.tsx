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
import { brandService } from "@/lib/services/brandService"
import { providerService } from "@/lib/services/providerService"
import type { Product, ProductCategory, Brand, Provider } from "@/lib/types"
import ImageUpload from "./image-upload"
import { Textarea } from "@/components/ui/textarea"

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
  const [cost, setCost] = useState("")
  const [stock, setStock] = useState("")
  const [imageUrl, setImageUrl] = useState("")
  const [description, setDescription] = useState("")
  const [productStatusType, setProductStatusType] = useState<"ENABLED" | "DISABLED">("ENABLED")
  const [brandId, setBrandId] = useState<number | null>(null)
  const [providerId, setProviderId] = useState<number | null>(null)
  const [selectedCategoryIds, setSelectedCategoryIds] = useState<number[]>([])
  const [categories, setCategories] = useState<ProductCategory[]>([])
  const [brands, setBrands] = useState<Brand[]>([])
  const [providers, setProviders] = useState<Provider[]>([])
  const [loading, setLoading] = useState(false)
  const [loadingData, setLoadingData] = useState(true)

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoadingData(true)
        const [categoriesRes, brandsRes, providersRes] = await Promise.all([
          categoryService.getCategories({ page: 1, size: 100 }),
          brandService.getBrands({ page: 1, size: 100 }),
          providerService.getProviders({ page: 1, size: 100 }),
        ])
        setCategories(categoriesRes.content)
        setBrands(brandsRes.content)
        setProviders(providersRes.content)
      } catch (error) {
        toast.error("Failed to load data")
        console.error("Error loading data:", error)
      } finally {
        setLoadingData(false)
      }
    }

    if (open) {
      loadData()
    }
  }, [open])

  useEffect(() => {
    if (product) {
      setName(product.name)
      setBarCode(product.barCode)
      setPrice(product.price.toString())
      setCost(product.cost?.toString() || "0")
      setStock(product.stock.toString())
      setImageUrl(product.imageUrl || "")
      setDescription(product.description || "")
      setProductStatusType(product.productStatusType)
      setBrandId(product.brand.brandId)
      setProviderId(product.provider.providerId)
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

    if (!name.trim() || !barCode.trim() || !price || !stock || !cost) {
      toast.error("Please fill in all required fields")
      return
    }

    if (selectedCategoryIds.length === 0) {
      toast.error("Please select at least one category")
      return
    }

    if (!product || !brandId || !providerId) return

    try {
      setLoading(true)
      await productService.updateProduct(product.productId, {
        name: name.trim(),
        barCode: barCode.trim(),
        price: parseFloat(price),
        cost: parseFloat(cost),
        stock: parseInt(stock),
        imageUrl: imageUrl.trim() || undefined,
        description: description.trim() || undefined,
        brandId,
        providerId,
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
              <div>
                <Label className="text-muted-foreground">Product ID</Label>
                <p className="text-lg font-semibold">{product.productId}</p>
              </div>

              <div className="border-l-4 border-green-500 pl-4 bg-green-50 p-3 rounded">
                <div className="flex items-center gap-2 mb-2">
                  <DollarSign className="h-5 w-5 text-green-600" />
                  <span className="text-sm text-muted-foreground">Sale Price</span>
                </div>
                <span className="text-3xl font-bold text-green-600">${product.price.toFixed(2)}</span>
                
                {product.cost > 0 && (
                  <>
                    <div className="mt-3 space-y-1">
                      <p className="text-sm text-muted-foreground">
                        Cost: <span className="font-semibold text-gray-700">${product.cost.toFixed(2)}</span>
                      </p>
                      <p className="text-sm font-semibold text-green-700">
                        Profit: ${(product.price - product.cost).toFixed(2)} 
                        <span className="text-xs ml-1">
                          ({(((product.price - product.cost) / product.cost) * 100).toFixed(1)}%)
                        </span>
                      </p>
                    </div>
                  </>
                )}
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
                  <Badge className={product.productStatusType === "ENABLED" ? "bg-green-500 hover:bg-green-600" : "bg-orange-500 hover:bg-orange-600"}>
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

              {product.description && (
                <div>
                  <Label className="text-muted-foreground">Description</Label>
                  <p className="text-sm mt-1">{product.description}</p>
                </div>
              )}

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
                  <Label htmlFor="price">Sale Price *</Label>
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
                  <Label htmlFor="cost">Cost *</Label>
                  <Input
                    id="cost"
                    type="number"
                    step="0.01"
                    min="0"
                    value={cost}
                    onChange={(e) => setCost(e.target.value)}
                    placeholder="0.00"
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
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

                <div className="space-y-2">
                  <Label className="text-muted-foreground">Profit (Calculated)</Label>
                  <div className="flex items-center h-10 px-3 rounded-md border bg-muted">
                    <span className="font-semibold text-green-600">
                      ${(parseFloat(price || "0") - parseFloat(cost || "0")).toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>

              <ImageUpload
                label="Product Image"
                value={imageUrl}
                onChange={setImageUrl}
                disabled={loading || loadingData}
              />

              <div className="space-y-2">
                <Label htmlFor="description">Description</Label>
                <Textarea
                  id="description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  placeholder="Enter product description (optional)"
                  rows={3}
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="brand">Brand *</Label>
                  <Select
                    value={brandId?.toString()}
                    onValueChange={(value) => setBrandId(parseInt(value))}
                    disabled={loadingData}
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
                    disabled={loadingData}
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
                    setCost(product.cost?.toString() || "0")
                    setStock(product.stock.toString())
                    setImageUrl(product.imageUrl || "")
                    setDescription(product.description || "")
                    setProductStatusType(product.productStatusType)
                    setBrandId(product.brand.brandId)
                    setProviderId(product.provider.providerId)
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

