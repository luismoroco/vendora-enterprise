"use client"

import { useState, useEffect } from "react"
import { Plus, Pencil, Search } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { toast } from "sonner"
import { providerService } from "@/lib/services/providerService"
import type { Provider } from "@/lib/types"
import MainSidebar from "../components/main-sidebar"
import ProviderFormDialog from "../components/provider-form-dialog"

export default function ProvidersPage() {
  const [providers, setProviders] = useState<Provider[]>([])
  const [loading, setLoading] = useState(true)
  const [searchQuery, setSearchQuery] = useState("")
  const [page, setPage] = useState(1)
  const [totalPages, setTotalPages] = useState(1)
  const [dialogOpen, setDialogOpen] = useState(false)
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

  const handleEdit = (provider: Provider) => {
    setSelectedProvider(provider)
    setDialogOpen(true)
  }

  const handleAdd = () => {
    setSelectedProvider(null)
    setDialogOpen(true)
  }

  const handleSuccess = () => {
    fetchProviders()
    setDialogOpen(false)
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
          <div className="border rounded-lg">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-20">ID</TableHead>
                  <TableHead>Name</TableHead>
                  <TableHead>RUC</TableHead>
                  <TableHead>Phone</TableHead>
                  <TableHead>Email</TableHead>
                  <TableHead className="w-32 text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {loading && providers.length === 0 ? (
                  Array.from({ length: 5 }).map((_, i) => (
                    <TableRow key={i}>
                      <TableCell><div className="h-4 bg-muted rounded animate-pulse" /></TableCell>
                      <TableCell><div className="h-4 bg-muted rounded animate-pulse" /></TableCell>
                      <TableCell><div className="h-4 bg-muted rounded animate-pulse" /></TableCell>
                      <TableCell><div className="h-4 bg-muted rounded animate-pulse" /></TableCell>
                      <TableCell><div className="h-4 bg-muted rounded animate-pulse" /></TableCell>
                      <TableCell><div className="h-4 bg-muted rounded animate-pulse" /></TableCell>
                    </TableRow>
                  ))
                ) : providers.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={6} className="text-center py-8 text-muted-foreground">
                      No providers found
                    </TableCell>
                  </TableRow>
                ) : (
                  providers.map((provider) => (
                    <TableRow key={provider.providerId}>
                      <TableCell className="font-medium">{provider.providerId}</TableCell>
                      <TableCell>{provider.name}</TableCell>
                      <TableCell>{provider.ruc}</TableCell>
                      <TableCell>{provider.phone || "-"}</TableCell>
                      <TableCell>{provider.email || "-"}</TableCell>
                      <TableCell className="text-right">
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => handleEdit(provider)}
                        >
                          <Pencil className="h-4 w-4" />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </div>

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
    </div>
  )
}

