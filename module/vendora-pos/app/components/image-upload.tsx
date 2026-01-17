"use client"

import { useState, useRef } from "react"
import { Upload, X, Loader2, Image as ImageIcon } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import { toast } from "sonner"
import { imageService } from "@/lib/services/imageService"
import Image from "next/image"

interface ImageUploadProps {
  label?: string
  value?: string
  onChange: (url: string) => void
  disabled?: boolean
}

export default function ImageUpload({ label, value, onChange, disabled }: ImageUploadProps) {
  const [uploading, setUploading] = useState(false)
  const [preview, setPreview] = useState<string | null>(value || null)
  const fileInputRef = useRef<HTMLInputElement>(null)

  const handleFileSelect = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    if (!file) return

    // Validate file type
    if (!file.type.startsWith('image/')) {
      toast.error('Please select an image file')
      return
    }

    // Validate file size (10MB)
    if (file.size > 10 * 1024 * 1024) {
      toast.error('Image size must be less than 10MB')
      return
    }

    try {
      setUploading(true)

      // Create preview
      const reader = new FileReader()
      reader.onloadend = () => {
        setPreview(reader.result as string)
      }
      reader.readAsDataURL(file)

      // Upload to server
      const response = await imageService.uploadImage(file)
      onChange(response.url)
      toast.success('Image uploaded successfully')

    } catch (error: any) {
      console.error('Error uploading image:', error)
      toast.error(error.response?.data?.error || 'Failed to upload image')
      setPreview(null)
    } finally {
      setUploading(false)
      if (fileInputRef.current) {
        fileInputRef.current.value = ''
      }
    }
  }

  const handleRemove = () => {
    setPreview(null)
    onChange('')
    if (fileInputRef.current) {
      fileInputRef.current.value = ''
    }
  }

  return (
    <div className="space-y-2">
      {label && <Label>{label}</Label>}
      
      <div className="flex items-start gap-4">
        {/* Preview */}
        {preview ? (
          <div className="relative w-32 h-32 border rounded-lg overflow-hidden bg-muted">
            <Image
              src={preview}
              alt="Preview"
              fill
              className="object-cover"
            />
            {!disabled && (
              <button
                type="button"
                onClick={handleRemove}
                className="absolute top-1 right-1 p-1 bg-destructive text-destructive-foreground rounded-full hover:bg-destructive/90 transition-colors"
                disabled={uploading}
              >
                <X className="h-3 w-3" />
              </button>
            )}
          </div>
        ) : (
          <div className="w-32 h-32 border-2 border-dashed rounded-lg flex items-center justify-center bg-muted">
            <ImageIcon className="h-8 w-8 text-muted-foreground" />
          </div>
        )}

        {/* Upload Button */}
        <div className="flex flex-col gap-2">
          <input
            ref={fileInputRef}
            type="file"
            accept="image/*"
            onChange={handleFileSelect}
            className="hidden"
            disabled={disabled || uploading}
          />
          
          <Button
            type="button"
            variant="outline"
            onClick={() => fileInputRef.current?.click()}
            disabled={disabled || uploading}
          >
            {uploading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Uploading...
              </>
            ) : (
              <>
                <Upload className="mr-2 h-4 w-4" />
                {preview ? 'Change Image' : 'Upload Image'}
              </>
            )}
          </Button>

          <p className="text-xs text-muted-foreground">
            PNG, JPG, GIF up to 10MB
          </p>
        </div>
      </div>
    </div>
  )
}

