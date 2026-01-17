"use client"

import { useState, useEffect } from "react"
import Image from "next/image"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Building2, Mail, Phone, FileText } from "lucide-react"
import { toast } from "sonner"
import { providerService } from "@/lib/services/providerService"
import type { Provider } from "@/lib/types"
import ImageUpload from "./image-upload"

interface ProviderDetailModalProps {
  provider: Provider | null
  open: boolean
  onOpenChange: (open: boolean) => void
  onSuccess: () => void
}

export default function ProviderDetailModal({
  provider,
  open,
  onOpenChange,
  onSuccess,
}: ProviderDetailModalProps) {
  const [isEditing, setIsEditing] = useState(false)
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
      setIsEditing(false)
    }
  }, [provider])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!name.trim() || !ruc.trim()) {
      toast.error("Name and RUC are required")
      return
    }

    if (!provider) return

    try {
      setLoading(true)
      await providerService.updateProvider(provider.providerId, {
        name: name.trim(),
        ruc: ruc.trim(),
        phone: phone.trim() || undefined,
        email: email.trim() || undefined,
        imageUrl: imageUrl.trim() || undefined,
      })
      toast.success("Provider updated successfully")
      setIsEditing(false)
      onSuccess()
    } catch (error: any) {
      toast.error(error.response?.data?.message || "Failed to update provider")
      console.error("Error updating provider:", error)
    } finally {
      setLoading(false)
    }
  }

  if (!provider) return null

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-3xl">
        <DialogHeader>
          <DialogTitle className="text-2xl flex items-center gap-2">
            <Building2 className="h-6 w-6" />
            {isEditing ? "Edit Provider" : provider.name}
          </DialogTitle>
        </DialogHeader>

        {!isEditing ? (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Provider Image */}
            <div className="relative aspect-square rounded-lg overflow-hidden bg-muted flex items-center justify-center">
              {provider.imageUrl ? (
                <Image
                  src={provider.imageUrl}
                  alt={provider.name}
                  fill
                  className="object-cover"
                />
              ) : (
                <div className="flex flex-col items-center justify-center text-muted-foreground p-8 text-center">
                  <Building2 className="h-24 w-24 mb-4" />
                  <p className="text-sm">No image available yet</p>
                </div>
              )}
            </div>

            {/* Provider Details */}
            <div className="flex flex-col gap-4">
              <div>
                <Label className="text-muted-foreground">Provider ID</Label>
                <p className="text-lg font-semibold">{provider.providerId}</p>
              </div>

              <div>
                <Label className="text-muted-foreground">Name</Label>
                <p className="text-lg font-semibold">{provider.name}</p>
              </div>

              <div className="flex items-center gap-2">
                <FileText className="h-5 w-5 text-muted-foreground" />
                <div>
                  <Label className="text-muted-foreground text-xs">RUC</Label>
                  <p className="font-semibold">{provider.ruc}</p>
                </div>
              </div>

              {provider.phone && (
                <div className="flex items-center gap-2">
                  <Phone className="h-5 w-5 text-muted-foreground" />
                  <div>
                    <Label className="text-muted-foreground text-xs">Phone</Label>
                    <p className="font-semibold">{provider.phone}</p>
                  </div>
                </div>
              )}

              {provider.email && (
                <div className="flex items-center gap-2">
                  <Mail className="h-5 w-5 text-muted-foreground" />
                  <div>
                    <Label className="text-muted-foreground text-xs">Email</Label>
                    <p className="font-semibold">{provider.email}</p>
                  </div>
                </div>
              )}

              <Button
                className="w-full mt-4"
                size="lg"
                onClick={() => setIsEditing(true)}
              >
                Edit Provider
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
                onClick={() => {
                  setIsEditing(false)
                  if (provider) {
                    setName(provider.name)
                    setRuc(provider.ruc)
                    setPhone(provider.phone || "")
                    setEmail(provider.email || "")
                    setImageUrl(provider.imageUrl || "")
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

