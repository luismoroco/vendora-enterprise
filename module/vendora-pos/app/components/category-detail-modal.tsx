"use client"

import { useState, useEffect } from "react"
import Image from "next/image"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Checkbox } from "@/components/ui/checkbox"
import { Badge } from "@/components/ui/badge"
import { Layers, Star } from "lucide-react"
import { toast } from "sonner"
import { categoryService } from "@/lib/services/categoryService"
import type { ProductCategory } from "@/lib/types"
import ImageUpload from "./image-upload"

interface CategoryDetailModalProps {
  category: ProductCategory | null
  open: boolean
  onOpenChange: (open: boolean) => void
  onSuccess: () => void
}

export default function CategoryDetailModal({
  category,
  open,
  onOpenChange,
  onSuccess,
}: CategoryDetailModalProps) {
  const [isEditing, setIsEditing] = useState(false)
  const [name, setName] = useState("")
  const [imageUrl, setImageUrl] = useState("")
  const [featured, setFeatured] = useState(false)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (category) {
      setName(category.name)
      setImageUrl(category.imageUrl || "")
      setFeatured(category.featured)
      setIsEditing(false)
    }
  }, [category])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!name.trim()) {
      toast.error("Name is required")
      return
    }

    if (!category) return

    try {
      setLoading(true)
      await categoryService.updateCategory(category.productCategoryId, {
        name: name.trim(),
        imageUrl: imageUrl.trim() || undefined,
        featured,
      })
      toast.success("Category updated successfully")
      setIsEditing(false)
      onSuccess()
    } catch (error: any) {
      toast.error(error.response?.data?.message || "Failed to update category")
      console.error("Error updating category:", error)
    } finally {
      setLoading(false)
    }
  }

  if (!category) return null

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-3xl">
        <DialogHeader>
          <DialogTitle className="text-2xl flex items-center gap-2">
            <Layers className="h-6 w-6" />
            {isEditing ? "Edit Category" : category.name}
          </DialogTitle>
        </DialogHeader>

        {!isEditing ? (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Category Image */}
            <div className="relative aspect-square rounded-lg overflow-hidden bg-muted">
              <Image
                src={category.imageUrl || "/placeholder.svg"}
                alt={category.name}
                fill
                className="object-cover"
              />
            </div>

            {/* Category Details */}
            <div className="flex flex-col gap-4">
              <div>
                <Label className="text-muted-foreground">Category ID</Label>
                <p className="text-lg font-semibold">{category.productCategoryId}</p>
              </div>

              <div>
                <Label className="text-muted-foreground">Name</Label>
                <p className="text-lg font-semibold">{category.name}</p>
              </div>

              <div>
                <Label className="text-muted-foreground">Featured</Label>
                <div className="mt-1">
                  <Badge variant={category.featured ? "default" : "secondary"}>
                    {category.featured ? (
                      <>
                        <Star className="h-3 w-3 mr-1" />
                        Featured
                      </>
                    ) : (
                      "Not Featured"
                    )}
                  </Badge>
                </div>
              </div>

              {category.imageUrl && (
                <div>
                  <Label className="text-muted-foreground">Image URL</Label>
                  <p className="text-sm truncate">{category.imageUrl}</p>
                </div>
              )}

              <Button
                className="w-full mt-4"
                size="lg"
                onClick={() => setIsEditing(true)}
              >
                Edit Category
              </Button>
            </div>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="name">Name *</Label>
              <Input
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Enter category name"
                required
              />
            </div>

            <ImageUpload
              label="Category Image"
              value={imageUrl}
              onChange={setImageUrl}
              disabled={loading}
            />

            <div className="flex items-center space-x-2">
              <Checkbox
                id="featured"
                checked={featured}
                onCheckedChange={(checked) => setFeatured(checked as boolean)}
              />
              <Label htmlFor="featured" className="cursor-pointer">
                Featured
              </Label>
            </div>

            <div className="flex justify-end gap-2 pt-4">
              <Button
                type="button"
                variant="outline"
                onClick={() => {
                  setIsEditing(false)
                  if (category) {
                    setName(category.name)
                    setImageUrl(category.imageUrl || "")
                    setFeatured(category.featured)
                  }
                }}
                disabled={loading}
              >
                Cancel
              </Button>
              <Button type="submit" disabled={loading}>
                {loading ? "Saving..." : "Save Changes"}
              </Button>
            </div>
          </form>
        )}
      </DialogContent>
    </Dialog>
  )
}

