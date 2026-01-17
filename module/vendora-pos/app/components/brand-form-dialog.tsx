"use client"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { toast } from "sonner"
import { brandService } from "@/lib/services/brandService"
import type { Brand } from "@/lib/types"
import ImageUpload from "./image-upload"

interface BrandFormDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  brand: Brand | null
  onSuccess: () => void
}

export default function BrandFormDialog({ open, onOpenChange, brand, onSuccess }: BrandFormDialogProps) {
  const [name, setName] = useState("")
  const [imageUrl, setImageUrl] = useState("")
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (brand) {
      setName(brand.name)
      setImageUrl(brand.imageUrl || "")
    } else {
      setName("")
      setImageUrl("")
    }
  }, [brand])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!name.trim()) {
      toast.error("Name is required")
      return
    }

    try {
      setLoading(true)
      
      if (brand) {
        // Update
        await brandService.updateBrand(brand.brandId, {
          name: name.trim(),
          imageUrl: imageUrl.trim() || undefined,
        })
        toast.success("Brand updated successfully")
      } else {
        // Create
        await brandService.createBrand({
          name: name.trim(),
          imageUrl: imageUrl.trim(),
        })
        toast.success("Brand created successfully")
      }
      
      onSuccess()
    } catch (error: any) {
      toast.error(error.response?.data?.message || "Failed to save brand")
      console.error("Error saving brand:", error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{brand ? "Edit Brand" : "Add Brand"}</DialogTitle>
        </DialogHeader>

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
              onClick={() => onOpenChange(false)}
              disabled={loading}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={loading}>
              {loading ? "Saving..." : brand ? "Update" : "Create"}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  )
}

