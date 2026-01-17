"use client"

import { useState, useEffect } from "react"
import { Plus, Eye, Search, Building2 } from "lucide-react"
import Image from "next/image"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent } from "@/components/ui/card"
import { toast } from "sonner"
import { providerService } from "@/lib/services/providerService"
import type { Provider } from "@/lib/types"
import MainSidebar from "../components/main-sidebar"
import ProviderFormDialog from "../components/provider-form-dialog"
import ProviderDetailModal from "../components/provider-detail-modal"

export default function ProvidersPage() {
  const [providers, setProviders] = useState<Provider[]>([])
  const [loading, setLoading] = useState(true)
  const [searchQuery, setSearchQuery] = useState("")
  const [page, setPage] = useState(1)
  const [totalPages, setTotalPages] = useState(1)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [detailModalOpen, setDetailModalOpen] = useState(false)
  const [selectedProvider, setSelectedProvider] = useState<Provider | null>(null)

  const fetchProviders = async () => {
    try {
      setLoading(true)
      const response = await providerService.getProviders({
        name: searchQuery || undefined,
        page,
        size: 20,
      })
      setProviders(response.content)
      setTotalPages(response.totalPages)
    } catch (error) {
      toast.error("Failed to load providers")
      console.error("Error fetching providers:", error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchProviders()
  }, [page, searchQuery])

  const handleProviderClick = (provider: Provider) => {
    setSelectedProvider(provider)
    setDetailModalOpen(true)
  }

  const handleAdd = () => {
    setSelectedProvider(null)
    setDialogOpen(true)
  }

  const handleSuccess = () => {
    fetchProviders()
    setDialogOpen(false)
    setDetailModalOpen(false)
  }

  return (
    <div className="flex h-screen bg-background">
      <MainSidebar />

      <main className="flex-1 flex flex-col h-screen overflow-hidden">
        <div className="sticky top-0 z-10 bg-background p-6 border-b">
          <div className="flex items-center justify-between mb-4">
            <h1 className="text-3xl font-bold">Providers</h1>
            <Button onClick={handleAdd}>
              <Plus className="mr-2 h-4 w-4" />
              Add Provider
            </Button>
          </div>

          <div className="relative w-full max-w-sm">
            <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Search providers..."
              className="pl-8"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
        </div>

        <div className="flex-1 overflow-auto p-6">
          {loading && providers.length === 0 ? (
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
          ) : providers.length === 0 ? (
            <div className="py-12 text-center">
              <p className="text-muted-foreground">No providers found</p>
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 xl:grid-cols-5">
              {providers.map((provider) => (
                <Card
                  key={provider.providerId}
                  className="overflow-hidden transition-all duration-200 hover:shadow-md cursor-pointer group relative"
                  onClick={() => handleProviderClick(provider)}
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
                    {provider.imageUrl ? (
                      <Image
                        src={provider.imageUrl}
                        alt={provider.name}
                        fill
                        className="object-cover"
                      />
                    ) : (
                      <div className="absolute inset-0 flex flex-col items-center justify-center bg-muted text-muted-foreground">
                        <Building2 className="h-16 w-16 mb-2" />
                        <p className="text-xs">No image yet</p>
                      </div>
                    )}
                  </div>
                  <CardContent className="p-3">
                    <div>
                      <h3 className="font-medium line-clamp-1">{provider.name}</h3>
                      <p className="text-xs text-muted-foreground">RUC: {provider.ruc}</p>
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

      <ProviderFormDialog
        open={dialogOpen}
        onOpenChange={setDialogOpen}
        provider={selectedProvider}
        onSuccess={handleSuccess}
      />

      <ProviderDetailModal
        provider={selectedProvider}
        open={detailModalOpen}
        onOpenChange={setDetailModalOpen}
        onSuccess={handleSuccess}
      />
    </div>
  )
}

