"use client"

import { useState, useEffect } from "react"
import { Plus, Eye, Search } from "lucide-react"
import Image from "next/image"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent } from "@/components/ui/card"
import { toast } from "sonner"
import { brandService } from "@/lib/services/brandService"
import type { Brand } from "@/lib/types"
import MainSidebar from "../components/main-sidebar"
import BrandFormDialog from "../components/brand-form-dialog"
import BrandDetailModal from "../components/brand-detail-modal"

export default function BrandsPage() {
  const [brands, setBrands] = useState<Brand[]>([])
  const [loading, setLoading] = useState(true)
  const [searchQuery, setSearchQuery] = useState("")
  const [page, setPage] = useState(1)
  const [totalPages, setTotalPages] = useState(1)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [detailModalOpen, setDetailModalOpen] = useState(false)
  const [selectedBrand, setSelectedBrand] = useState<Brand | null>(null)

  const fetchBrands = async () => {
    try {
      setLoading(true)
      const response = await brandService.getBrands({
        name: searchQuery || undefined,
        page,
        size: 20,
      })
      setBrands(response.content)
      setTotalPages(response.totalPages)
    } catch (error) {
      toast.error("Failed to load brands")
      console.error("Error fetching brands:", error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchBrands()
  }, [page, searchQuery])

  const handleBrandClick = (brand: Brand) => {
    setSelectedBrand(brand)
    setDetailModalOpen(true)
  }

  const handleAdd = () => {
    setSelectedBrand(null)
    setDialogOpen(true)
  }

  const handleSuccess = () => {
    fetchBrands()
    setDialogOpen(false)
    setDetailModalOpen(false)
  }

  return (
    <div className="flex h-screen bg-background">
      <MainSidebar />

      <main className="flex-1 flex flex-col h-screen overflow-hidden">
        <div className="sticky top-0 z-10 bg-background p-6 border-b">
          <div className="flex items-center justify-between mb-4">
            <h1 className="text-3xl font-bold">Brands</h1>
            <Button onClick={handleAdd}>
              <Plus className="mr-2 h-4 w-4" />
              Add Brand
            </Button>
          </div>

          <div className="relative w-full max-w-sm">
            <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Search brands..."
              className="pl-8"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
        </div>

        <div className="flex-1 overflow-auto p-6">
          {loading && brands.length === 0 ? (
            <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 xl:grid-cols-5">
              {Array.from({ length: 10 }).map((_, i) => (
                <Card key={i} className="overflow-hidden animate-pulse">
                  <div className="aspect-square bg-muted" />
                  <CardContent className="p-3">
                    <div className="h-4 bg-muted rounded mb-2" />
                    <div className="h-3 bg-muted rounded w-1/2" />
                  </CardContent>
                </Card>
              ))}
            </div>
          ) : brands.length === 0 ? (
            <div className="py-12 text-center">
              <p className="text-muted-foreground">No brands found</p>
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 xl:grid-cols-5">
              {brands.map((brand) => (
                <Card
                  key={brand.brandId}
                  className="overflow-hidden transition-all duration-200 hover:shadow-md cursor-pointer group relative bg-gray-50"
                  onClick={() => handleBrandClick(brand)}
                >
                  <div className="relative aspect-square">
                    <div className="absolute inset-0 flex items-center justify-center bg-black/50 opacity-0 transition-opacity group-hover:opacity-100 z-10">
                      <Button
                        size="icon"
                        variant="secondary"
                        className="h-10 w-10"
                      >
                        <Eye className="h-5 w-5" />
                      </Button>
                    </div>
                    <Image
                      src={brand.imageUrl || "/placeholder.svg"}
                      alt={brand.name}
                      fill
                      className="object-cover"
                    />
                  </div>
                  <CardContent className="p-3">
                    <div>
                      <h3 className="font-medium line-clamp-1">{brand.name}</h3>
                      <p className="text-xs text-muted-foreground">ID: {brand.brandId}</p>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex justify-center gap-2 mt-6">
              <Button
                variant="outline"
                onClick={() => setPage((p) => Math.max(1, p - 1))}
                disabled={page === 1}
              >
                Previous
              </Button>
              <span className="flex items-center px-4">
                Page {page} of {totalPages}
              </span>
              <Button
                variant="outline"
                onClick={() => setPage((p) => Math.min(totalPages, p + 1))}
                disabled={page === totalPages}
              >
                Next
              </Button>
            </div>
          )}
        </div>
      </main>

      <BrandFormDialog
        open={dialogOpen}
        onOpenChange={setDialogOpen}
        brand={selectedBrand}
        onSuccess={handleSuccess}
      />

      <BrandDetailModal
        brand={selectedBrand}
        open={detailModalOpen}
        onOpenChange={setDetailModalOpen}
        onSuccess={handleSuccess}
      />
    </div>
  )
}

