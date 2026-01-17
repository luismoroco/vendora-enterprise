"use client"

import { useState, useEffect } from "react"
import Image from "next/image"
import { LayoutGrid } from "lucide-react"
import { Button } from "@/components/ui/button"
import { cn } from "@/lib/utils"
import { categoryService } from "@/lib/services/categoryService"
import type { ProductCategory } from "@/lib/types"

interface CategorySidebarNewProps {
  selectedCategoryIds: number[]
  onSelectCategory: (categoryIds: number[]) => void
}

export default function CategorySidebarNew({ selectedCategoryIds, onSelectCategory }: CategorySidebarNewProps) {
  const [categories, setCategories] = useState<ProductCategory[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        setLoading(true)
        const response = await categoryService.getCategories({
          page: 1,
          size: 100, // Get all categories
        })
        setCategories(response.content)
      } catch (error) {
        console.error("Error fetching categories:", error)
      } finally {
        setLoading(false)
      }
    }

    fetchCategories()
  }, [])

  const isAllSelected = selectedCategoryIds.length === 0

  return (
    <div className="w-56 border-r bg-background p-4">
      <h2 className="mb-4 text-lg font-semibold">Categories</h2>
      <div className="grid gap-3">
        {/* All Products Button */}
        <Button
          variant="ghost"
          className={cn(
            "flex h-auto flex-col items-center justify-center py-4 border bg-transparent",
            isAllSelected
              ? "border-2 border-primary text-foreground font-medium"
              : "border-muted text-muted-foreground hover:border-muted-foreground hover:text-foreground",
            "hover:bg-transparent"
          )}
          onClick={() => onSelectCategory([])}
        >
          <LayoutGrid className="mb-2 h-6 w-6" />
          <span className="text-sm">All Products</span>
        </Button>

        {/* Category Buttons */}
        {loading ? (
          Array.from({ length: 4 }).map((_, i) => (
            <div key={i} className="h-24 bg-muted rounded animate-pulse" />
          ))
        ) : (
          categories.map((category) => {
            const isSelected = selectedCategoryIds.includes(category.productCategoryId)
            return (
              <Button
                key={category.productCategoryId}
                variant="ghost"
                className={cn(
                  "flex h-auto flex-col items-center justify-center py-3 border bg-transparent overflow-hidden",
                  isSelected
                    ? "border-2 border-primary text-foreground font-medium"
                    : "border-muted text-muted-foreground hover:border-muted-foreground hover:text-foreground",
                  "hover:bg-transparent"
                )}
                onClick={() => onSelectCategory([category.productCategoryId])}
              >
                {category.imageUrl ? (
                  <div className="mb-2 h-12 w-12 relative rounded overflow-hidden">
                    <Image
                      src={category.imageUrl}
                      alt={category.name}
                      fill
                      className="object-cover"
                    />
                  </div>
                ) : (
                  <LayoutGrid className="mb-2 h-12 w-12 p-2 bg-muted rounded" />
                )}
                <span className="text-xs text-center line-clamp-2">{category.name}</span>
              </Button>
            )
          })
        )}
      </div>
    </div>
  )
}

