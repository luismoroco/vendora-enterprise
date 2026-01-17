"use client"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { toast } from "sonner"
import { providerService } from "@/lib/services/providerService"
import type { Provider } from "@/lib/types"
import ImageUpload from "./image-upload"

interface ProviderFormDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  provider: Provider | null
  onSuccess: () => void
}

export default function ProviderFormDialog({
  open,
  onOpenChange,
  provider,
  onSuccess,
}: ProviderFormDialogProps) {
  const [name, setName] = useState("")
  const [ruc, setRuc] = useState("")
  const [phone, setPhone] = useState("")
  const [email, setEmail] = useState("")
  const [imageUrl, setImageUrl] = useState("")
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (provider) {
      setName(provider.name)
      setRuc(provider.ruc)
      setPhone(provider.phone || "")
      setEmail(provider.email || "")
      setImageUrl(provider.imageUrl || "")
    } else {
      setName("")
      setRuc("")
      setPhone("")
      setEmail("")
      setImageUrl("")
    }
  }, [provider])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!name.trim() || !ruc.trim()) {
      toast.error("Name and RUC are required")
      return
    }

    try {
      setLoading(true)

      if (provider) {
        // Update
        await providerService.updateProvider(provider.providerId, {
          name: name.trim(),
          ruc: ruc.trim(),
          phone: phone.trim() || undefined,
          email: email.trim() || undefined,
          imageUrl: imageUrl.trim() || undefined,
        })
        toast.success("Provider updated successfully")
      } else {
        // Create
        await providerService.createProvider({
          name: name.trim(),
          ruc: ruc.trim(),
          phone: phone.trim(),
          email: email.trim(),
          imageUrl: imageUrl.trim(),
        })
        toast.success("Provider created successfully")
      }

      onSuccess()
    } catch (error: any) {
      toast.error(error.response?.data?.message || "Failed to save provider")
      console.error("Error saving provider:", error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{provider ? "Edit Provider" : "Add Provider"}</DialogTitle>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="name">Name *</Label>
            <Input
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter provider name"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="ruc">RUC *</Label>
            <Input
              id="ruc"
              value={ruc}
              onChange={(e) => setRuc(e.target.value)}
              placeholder="Enter RUC"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="phone">Phone</Label>
            <Input
              id="phone"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              placeholder="Enter phone number"
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="email">Email</Label>
            <Input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter email"
            />
          </div>

          <ImageUpload
            label="Provider Image"
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
              {loading ? "Saving..." : provider ? "Update" : "Create"}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  )
}

