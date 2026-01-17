# Vendora POS Setup Guide

## Environment Configuration

Create a `.env.local` file in the root of the `vendora-pos` directory with the following content:

```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
```

## Running the Application

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

## Backend Requirements

Make sure the backend server is running on `http://localhost:8080` before using the frontend.

## Features

- **POS Interface**: Main point of sale with product browsing and cart management
- **Products Management**: Full CRUD operations for products with brand and category assignment
- **Categories Management**: Manage product categories with featured flag
- **Brands Management**: Manage product brands
- **Providers Management**: Manage product providers

## Navigation

- **POS**: Main point of sale interface
- **Products**: Product catalog management
- **Categories**: Category management
- **Brands**: Brand management
- **Providers**: Provider management

All modules are accessible via the left sidebar.

