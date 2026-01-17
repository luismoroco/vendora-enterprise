"use client"

import { useState, useEffect } from "react"
import Image from "next/image"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Tag } from "lucide-react"
import { toast } from "sonner"
import { brandService } from "@/lib/services/brandService"
import type { Brand } from "@/lib/types"
import ImageUpload from "./image-upload"

interface BrandDetailModalProps {
  brand: Brand | null
  open: boolean
  onOpenChange: (open: boolean) => void
  onSuccess: () => void
}

export default function BrandDetailModal({
  brand,
  open,
  onOpenChange,
  onSuccess,
}: BrandDetailModalProps) {
  const [isEditing, setIsEditing] = useState(false)
  const [name, setName] = useState("")
  const [imageUrl, setImageUrl] = useState("")
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (brand) {
      setName(brand.name)
      setImageUrl(brand.imageUrl || "")
      setIsEditing(false)
    }
  }, [brand])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!name.trim()) {
      toast.error("Name is required")
      return
    }

    if (!brand) return

    try {
      setLoading(true)
      await brandService.updateBrand(brand.brandId, {
        name: name.trim(),
        imageUrl: imageUrl.trim() || undefined,
      })
      toast.success("Brand updated successfully")
      setIsEditing(false)
      onSuccess()
    } catch (error: any) {
      toast.error(error.response?.data?.message || "Failed to update brand")
      console.error("Error updating brand:", error)
    } finally {
      setLoading(false)
    }
  }

  if (!brand) return null

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-3xl">
        <DialogHeader>
          <DialogTitle className="text-2xl flex items-center gap-2">
            <Tag className="h-6 w-6" />
            {isEditing ? "Edit Brand" : brand.name}
          </DialogTitle>
        </DialogHeader>

        {!isEditing ? (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Brand Image */}
            <div className="relative aspect-square rounded-lg overflow-hidden bg-muted">
              <Image
                src={brand.imageUrl || "/placeholder.svg"}
                alt={brand.name}
                fill
                className="object-cover"
              />
            </div>

            {/* Brand Details */}
            <div className="flex flex-col gap-4">
              <div>
                <Label className="text-muted-foreground">Brand ID</Label>
                <p className="text-lg font-semibold">{brand.brandId}</p>
              </div>

              <div>
                <Label className="text-muted-foreground">Name</Label>
                <p className="text-lg font-semibold">{brand.name}</p>
              </div>

              {brand.imageUrl && (
                <div>
                  <Label className="text-muted-foreground">Image URL</Label>
                  <p className="text-sm truncate">{brand.imageUrl}</p>
                </div>
              )}

              <Button
                className="w-full mt-4"
                size="lg"
                onClick={() => setIsEditing(true)}
              >
                Edit Brand
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
                placeholder="Enter brand name"
                required
              />
            </div>

            <ImageUpload
              label="Brand Image"
              value={imageUrl}
              onChange={setImageUrl}
              disabled={loading}
            />

            <div className="flex justify-end gap-2 pt-4">
              <Button
                type="button"
                variant="outline"
                onClick={() => {
                  setIsEditing(false)
                  if (brand) {
                    setName(brand.name)
                    setImageUrl(brand.imageUrl || "")
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

