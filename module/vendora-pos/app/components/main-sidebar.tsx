"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { ShoppingCart, Package, Tag, Building2, Layers } from "lucide-react"
import { Button } from "@/components/ui/button"
import { cn } from "@/lib/utils"

interface NavItem {
  label: string
  href: string
  icon: React.ElementType
}

const navItems: NavItem[] = [
  {
    label: "POS",
    href: "/",
    icon: ShoppingCart,
  },
  {
    label: "Products",
    href: "/products",
    icon: Package,
  },
  {
    label: "Categories",
    href: "/categories",
    icon: Layers,
  },
  {
    label: "Brands",
    href: "/brands",
    icon: Tag,
  },
  {
    label: "Providers",
    href: "/providers",
    icon: Building2,
  },
]

export default function MainSidebar() {
  const pathname = usePathname()

  return (
    <div className="w-20 border-r bg-background flex flex-col items-center py-4 gap-4">
      <div className="font-bold text-xl mb-4">V</div>
      
      {navItems.map((item) => {
        const Icon = item.icon
        const isActive = pathname === item.href
        
        return (
          <Link key={item.href} href={item.href}>
            <Button
              variant="ghost"
              size="icon"
              className={cn(
                "h-12 w-12",
                isActive
                  ? "bg-primary text-primary-foreground hover:bg-primary hover:text-primary-foreground"
                  : "hover:bg-muted"
              )}
              title={item.label}
            >
              <Icon className="h-6 w-6" />
            </Button>
          </Link>
        )
      })}
    </div>
  )
}

