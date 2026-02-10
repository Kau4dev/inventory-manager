import { useEffect } from "react";
import {
  Package,
  Boxes,
  DollarSign,
  TrendingUp,
  RefreshCw,
  Factory,
  Sparkles,
} from "lucide-react";
import { Card, Button, Table, Badge, StatCard } from "../../components";
import { TableColumn } from "../../components/ui/Table";
import {
  fetchProducts,
  fetchProductionSuggestions,
} from "../../store/slices/productsSlice";
import { fetchRawMaterials } from "../../store/slices/rawMaterialsSlice";
import { useAppDispatch, useAppSelector } from "../../hooks/useRedux";
import { ProductionSuggestion, RawMaterial } from "../../types";

const DashboardPage = () => {
  const dispatch = useAppDispatch();
  const {
    items: products,
    productionSuggestions,
    loading,
  } = useAppSelector((state) => state.products);
  const { items: rawMaterials } = useAppSelector((state) => state.rawMaterials);

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchRawMaterials());
    dispatch(fetchProductionSuggestions());
  }, [dispatch]);

  const formatCurrency = (value: number): string => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
    }).format(value || 0);
  };

  const totalInventoryValue = rawMaterials.reduce(
    (acc: number, material: RawMaterial) => acc + material.quantity,
    0,
  );

  const lowStockMaterials = rawMaterials.filter(
    (m: RawMaterial) => m.quantity <= m.minimumStock,
  );

  const columns: TableColumn<ProductionSuggestion>[] = [
    {
      header: "Product",
      accessor: "productName",
      render: (value: string) => (
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-100 to-purple-100 flex items-center justify-center">
            <Package className="w-5 h-5 text-indigo-600" />
          </div>
          <span className="font-medium text-gray-900">{value}</span>
        </div>
      ),
    },
    {
      header: "Suggested Qty",
      accessor: "suggestedQuantity",
      render: (value: number) => (
        <div className="flex items-center gap-2">
          <span className="font-semibold text-blue-600">{value}</span>
          <span className="text-gray-500 text-sm">units</span>
        </div>
      ),
    },
    {
      header: "Unit Profit",
      accessor: "profitPerUnit",
      render: (value: number) => (
        <span className="text-gray-700">{formatCurrency(value)}</span>
      ),
    },
    {
      header: "Total Profit",
      accessor: "totalProfit",
      render: (value: number) => (
        <span className="font-semibold text-green-600">
          {formatCurrency(value)}
        </span>
      ),
    },
  ];

  const handleRefresh = () => {
    dispatch(fetchProducts());
    dispatch(fetchRawMaterials());
    dispatch(fetchProductionSuggestions());
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
          <p className="text-gray-500 mt-1">
            Production overview and optimization
          </p>
        </div>
        <Button
          variant="secondary"
          onClick={handleRefresh}
          icon={RefreshCw}
          disabled={loading}
        >
          Refresh Data
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          title="Estimated Revenue"
          value={formatCurrency(productionSuggestions.totalValue)}
          icon={DollarSign}
          color="green"
        />
        <StatCard
          title="Products in Catalog"
          value={products.length}
          icon={Package}
          color="purple"
        />
        <StatCard
          title="Raw Materials"
          value={rawMaterials.length}
          icon={Boxes}
          color="blue"
        />
        <StatCard
          title="Total Stock Units"
          value={totalInventoryValue.toLocaleString()}
          icon={TrendingUp}
          color="indigo"
        />
      </div>

      {/* Low Stock Alert */}
      {lowStockMaterials.length > 0 && (
        <Card className="border-l-4 border-l-yellow-500 bg-yellow-50">
          <div className="flex items-start gap-4">
            <div className="w-10 h-10 rounded-xl bg-yellow-100 flex items-center justify-center flex-shrink-0">
              <Boxes className="w-5 h-5 text-yellow-600" />
            </div>
            <div>
              <h3 className="font-semibold text-yellow-800">Low Stock Alert</h3>
              <p className="text-yellow-700 text-sm mt-1">
                {lowStockMaterials.length} material(s) with low stock:{" "}
                {lowStockMaterials.map((m: RawMaterial) => m.name).join(", ")}
              </p>
            </div>
          </div>
        </Card>
      )}

      {/* Production Suggestions */}
      <Card padding={false}>
        <div className="px-6 py-4 border-b border-gray-100">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-500 flex items-center justify-center shadow-lg shadow-blue-500/30">
                <Factory className="w-5 h-5 text-white" />
              </div>
              <div>
                <h2 className="font-semibold text-gray-800">
                  Optimal Production Plan
                </h2>
                <p className="text-sm text-gray-500">
                  Products prioritized by highest value
                </p>
              </div>
            </div>
            <div className="flex items-center gap-2">
              <Sparkles className="w-5 h-5 text-yellow-500" />
              <span className="text-sm font-medium text-gray-600">
                Based on available stock
              </span>
            </div>
          </div>
        </div>

        {productionSuggestions.suggestions?.length > 0 ? (
          <>
            <Table
              columns={columns}
              data={productionSuggestions.suggestions}
              loading={loading}
            />
            <div className="px-6 py-4 border-t border-gray-100 bg-gradient-to-r from-green-50 to-emerald-50">
              <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                <div className="flex items-center gap-3">
                  <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-green-500 to-emerald-500 flex items-center justify-center shadow-lg shadow-green-500/30">
                    <DollarSign className="w-6 h-6 text-white" />
                  </div>
                  <div>
                    <p className="text-sm font-medium text-gray-600">
                      Total Estimated Revenue
                    </p>
                    <p className="text-2xl font-bold text-green-600">
                      {formatCurrency(productionSuggestions.totalValue)}
                    </p>
                  </div>
                </div>
                <Badge variant="success" size="lg">
                  {productionSuggestions.suggestions.length} producible products
                </Badge>
              </div>
            </div>
          </>
        ) : (
          <div className="p-12 text-center">
            <div className="w-16 h-16 rounded-2xl bg-gray-100 flex items-center justify-center mx-auto mb-4">
              <Factory className="w-8 h-8 text-gray-400" />
            </div>
            <h3 className="text-lg font-semibold text-gray-700 mb-2">
              No Production Suggestions
            </h3>
            <p className="text-gray-500 max-w-md mx-auto">
              {rawMaterials.length === 0
                ? "Add raw materials to get started with production planning."
                : products.length === 0
                  ? "Add products and associate materials to see production suggestions."
                  : "Not enough raw materials in stock to produce any products. Restock your materials."}
            </p>
          </div>
        )}
      </Card>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <Card
          hover
          className="cursor-pointer group"
          onClick={() => (window.location.href = "/raw-materials")}
        >
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-500 to-blue-600 flex items-center justify-center shadow-lg shadow-blue-500/30 group-hover:scale-105 transition-transform">
              <Boxes className="w-6 h-6 text-white" />
            </div>
            <div>
              <h3 className="font-semibold text-gray-800 group-hover:text-blue-600 transition-colors">
                Manage Raw Materials
              </h3>
              <p className="text-sm text-gray-500">
                Add, edit or remove materials from stock
              </p>
            </div>
          </div>
        </Card>

        <Card
          hover
          className="cursor-pointer group"
          onClick={() => (window.location.href = "/products")}
        >
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-purple-500 to-purple-600 flex items-center justify-center shadow-lg shadow-purple-500/30 group-hover:scale-105 transition-transform">
              <Package className="w-6 h-6 text-white" />
            </div>
            <div>
              <h3 className="font-semibold text-gray-800 group-hover:text-purple-600 transition-colors">
                Manage Products
              </h3>
              <p className="text-sm text-gray-500">
                Create products and assign materials
              </p>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default DashboardPage;
