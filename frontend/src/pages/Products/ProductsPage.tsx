import { useEffect, useState, FormEvent } from "react";
import { useAppDispatch, useAppSelector } from "../../hooks/useRedux";
import {
  Package,
  Plus,
  Pencil,
  Trash2,
  Search,
  RefreshCw,
  Boxes,
  DollarSign,
  X,
} from "lucide-react";
import {
  Card,
  Button,
  Table,
  Modal,
  Input,
  Select,
  Badge,
} from "../../components";
import {
  fetchProducts,
  createProduct,
  updateProduct,
  deleteProduct,
  addMaterialToProduct,
  removeMaterialFromProduct,
} from "../../store/slices/productsSlice";
import { fetchRawMaterials } from "../../store/slices/rawMaterialsSlice";
import { showNotification } from "../../store/slices/uiSlice";
import type { Product } from "../../types";

interface FormData {
  name: string;
  price: string;
}

interface MaterialForm {
  materialId: string;
  requiredQuantity: string;
}

interface FormErrors {
  name?: string;
  price?: string;
}

interface SelectOption {
  value: string;
  label: string;
}

const ProductsPage = () => {
  const dispatch = useAppDispatch();
  const { items: products, loading } = useAppSelector(
    (state) => state.products,
  );
  const { items: rawMaterials } = useAppSelector((state) => state.rawMaterials);

  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState<boolean>(false);
  const [isMaterialsModalOpen, setIsMaterialsModalOpen] =
    useState<boolean>(false);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [formData, setFormData] = useState<FormData>({
    name: "",
    price: "",
  });
  const [materialForm, setMaterialForm] = useState<MaterialForm>({
    materialId: "",
    requiredQuantity: "",
  });
  const [errors, setErrors] = useState<FormErrors>({});

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchRawMaterials());
  }, [dispatch]);

  const filteredProducts = products.filter((product) =>
    product.name.toLowerCase().includes(searchTerm.toLowerCase()),
  );

  const handleOpenModal = (product: Product | null = null) => {
    if (product) {
      setSelectedProduct(product);
      setFormData({
        name: product.name,
        price: product.price.toString(),
      });
    } else {
      setSelectedProduct(null);
      setFormData({ name: "", price: "" });
    }
    setErrors({});
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedProduct(null);
    setFormData({ name: "", price: "" });
    setErrors({});
  };

  const handleOpenMaterialsModal = (product: Product) => {
    setSelectedProduct(product);
    setMaterialForm({ materialId: "", requiredQuantity: "" });
    setIsMaterialsModalOpen(true);
  };

  const handleCloseMaterialsModal = () => {
    setIsMaterialsModalOpen(false);
    setSelectedProduct(null);
    setMaterialForm({ materialId: "", requiredQuantity: "" });
  };

  const validateForm = (): boolean => {
    const newErrors: FormErrors = {};
    if (!formData.name.trim()) {
      newErrors.name = "Name is required";
    }
    if (!formData.price || parseFloat(formData.price) <= 0) {
      newErrors.price = "Price must be a positive number";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!validateForm()) return;

    const data = {
      name: formData.name.trim(),
      price: parseFloat(formData.price),
    };

    try {
      if (selectedProduct) {
        await dispatch(
          updateProduct({ id: selectedProduct.id, data }),
        ).unwrap();
        dispatch(
          showNotification({
            type: "success",
            message: "Product updated successfully!",
          }),
        );
      } else {
        await dispatch(createProduct(data)).unwrap();
        dispatch(
          showNotification({
            type: "success",
            message: "Product created successfully!",
          }),
        );
      }
      handleCloseModal();
      dispatch(fetchProducts());
    } catch (error) {
      dispatch(
        showNotification({
          type: "error",
          message: (error as string) || "An error occurred",
        }),
      );
    }
  };

  const handleDeleteClick = (product: Product) => {
    setSelectedProduct(product);
    setIsDeleteModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!selectedProduct) return;

    try {
      await dispatch(deleteProduct(selectedProduct.id)).unwrap();
      dispatch(
        showNotification({
          type: "success",
          message: "Product deleted successfully!",
        }),
      );
      setIsDeleteModalOpen(false);
      setSelectedProduct(null);
    } catch (error) {
      dispatch(
        showNotification({
          type: "error",
          message: (error as string) || "Error deleting product",
        }),
      );
    }
  };

  const handleAddMaterial = async () => {
    if (
      !materialForm.materialId ||
      !materialForm.requiredQuantity ||
      !selectedProduct
    ) {
      dispatch(
        showNotification({ type: "error", message: "Please fill all fields" }),
      );
      return;
    }

    try {
      await dispatch(
        addMaterialToProduct({
          productId: selectedProduct.id,
          materialId: parseInt(materialForm.materialId),
          requiredQuantity: parseInt(materialForm.requiredQuantity),
        }),
      ).unwrap();
      dispatch(
        showNotification({
          type: "success",
          message: "Material added successfully!",
        }),
      );
      setMaterialForm({ materialId: "", requiredQuantity: "" });
      dispatch(fetchProducts());
    } catch (error) {
      dispatch(
        showNotification({
          type: "error",
          message: (error as string) || "Error adding material",
        }),
      );
    }
  };

  const handleRemoveMaterial = async (materialId: number) => {
    if (!selectedProduct) return;

    try {
      await dispatch(
        removeMaterialFromProduct({
          productId: selectedProduct.id,
          materialId,
        }),
      ).unwrap();
      dispatch(
        showNotification({
          type: "success",
          message: "Material removed successfully!",
        }),
      );
      dispatch(fetchProducts());
    } catch (error) {
      dispatch(
        showNotification({
          type: "error",
          message: (error as string) || "Error removing material",
        }),
      );
    }
  };

  const formatCurrency = (value: number): string => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
    }).format(value);
  };

  const getMaterialOptions = (): SelectOption[] => {
    const existingMaterialIds =
      currentProduct && currentProduct.materials
        ? currentProduct.materials.map((m) => m.materialId)
        : [];
    return rawMaterials
      .filter((m) => !existingMaterialIds.includes(m.id))
      .map((material) => ({
        value: material.id.toString(),
        label: material.name,
      }));
  };

  const getMaterialName = (materialId: number): string => {
    const material = rawMaterials.find((m) => m.id === materialId);
    return material?.name || "Unknown Material";
  };

  const columns = [
    {
      header: "Product",
      accessor: "name" as const,
      render: (value: string) => (
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-purple-100 to-pink-100 flex items-center justify-center">
            <Package className="w-5 h-5 text-purple-600" />
          </div>
          <span className="font-medium text-gray-900">{value}</span>
        </div>
      ),
    },
    {
      header: "Price",
      accessor: "price" as const,
      render: (value: number) => (
        <span className="font-semibold text-green-600">
          {formatCurrency(value)}
        </span>
      ),
    },
    {
      header: "Materials",
      accessor: "materials" as const,
      render: (value: Product["materials"], row: Product) => (
        <div className="flex items-center gap-2">
          <Badge variant={value?.length > 0 ? "info" : "default"}>
            {value?.length || 0} materials
          </Badge>
          <Button
            variant="ghost"
            size="sm"
            onClick={() => handleOpenMaterialsModal(row)}
            className="text-blue-600"
            icon={Boxes}
          />
        </div>
      ),
    },
    {
      header: "Actions",
      accessor: "id" as const,
      className: "text-right",
      render: (_: number, row: Product) => (
        <div className="flex items-center justify-end gap-2">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => handleOpenModal(row)}
            icon={Pencil}
          />
          <Button
            variant="ghost"
            size="sm"
            onClick={() => handleDeleteClick(row)}
            className="text-red-600 hover:bg-red-50"
            icon={Trash2}
          />
        </div>
      ),
    },
  ];

  // Get the current product with updated materials
  const currentProduct = selectedProduct
    ? products.find((p) => p.id === selectedProduct.id)
    : null;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Products</h1>
          <p className="text-gray-500 mt-1">Manage your product catalog</p>
        </div>
        <div className="flex items-center gap-3">
          <Button
            variant="secondary"
            onClick={() => dispatch(fetchProducts())}
            icon={RefreshCw}
            disabled={loading}
          >
            Refresh
          </Button>
          <Button onClick={() => handleOpenModal()} icon={Plus}>
            New Product
          </Button>
        </div>
      </div>

      {/* Search */}
      <Card>
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <Input
              placeholder="Search products..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              icon={Search}
            />
          </div>
        </div>
      </Card>

      {/* Table */}
      <Card padding={false}>
        <div className="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
          <h2 className="font-semibold text-gray-800">Products Catalog</h2>
          <Badge variant="purple">{filteredProducts.length} products</Badge>
        </div>
        <Table
          columns={columns}
          data={filteredProducts}
          loading={loading}
          emptyMessage="No products found"
        />
      </Card>

      {/* Create/Edit Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={selectedProduct ? "Edit Product" : "Create New Product"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Product Name"
            placeholder="Enter product name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            error={errors.name}
            required
          />
          <Input
            label="Price"
            type="number"
            step="0.01"
            placeholder="0.00"
            value={formData.price}
            onChange={(e) =>
              setFormData({ ...formData, price: e.target.value })
            }
            error={errors.price}
            icon={DollarSign}
            min="0.01"
            required
          />
          <div className="flex justify-end gap-3 pt-4">
            <Button
              type="button"
              variant="secondary"
              onClick={handleCloseModal}
            >
              Cancel
            </Button>
            <Button type="submit" loading={loading}>
              {selectedProduct ? "Update" : "Create"}
            </Button>
          </div>
        </form>
      </Modal>

      {/* Materials Modal */}
      <Modal
        isOpen={isMaterialsModalOpen}
        onClose={handleCloseMaterialsModal}
        title={`Materials for ${currentProduct?.name || selectedProduct?.name}`}
        size="lg"
      >
        <div className="space-y-6">
          {/* Add Material Form */}
          <div className="bg-gray-50 rounded-xl p-4">
            <h3 className="text-sm font-semibold text-gray-700 mb-3">
              Add Material
            </h3>
            <div className="flex flex-col sm:flex-row gap-3">
              <div className="flex-1">
                <Select
                  placeholder="Select material"
                  value={materialForm.materialId}
                  onChange={(e) =>
                    setMaterialForm({
                      ...materialForm,
                      materialId: e.target.value,
                    })
                  }
                  options={getMaterialOptions()}
                />
              </div>
              <div className="w-full sm:w-32">
                <Input
                  type="number"
                  placeholder="Quantity"
                  value={materialForm.requiredQuantity}
                  onChange={(e) =>
                    setMaterialForm({
                      ...materialForm,
                      requiredQuantity: e.target.value,
                    })
                  }
                  min="1"
                />
              </div>
              <Button onClick={handleAddMaterial} icon={Plus}>
                Add
              </Button>
            </div>
          </div>

          {/* Materials List */}
          <div>
            <h3 className="text-sm font-semibold text-gray-700 mb-3">
              Required Materials
            </h3>
            {currentProduct &&
            currentProduct.materials &&
            currentProduct.materials.length > 0 ? (
              <div className="space-y-2">
                {currentProduct.materials.map((material) => (
                  <div
                    key={material.materialId}
                    className="flex items-center justify-between p-3 bg-white rounded-xl border border-gray-100"
                  >
                    <div className="flex items-center gap-3">
                      <div className="w-8 h-8 rounded-lg bg-blue-100 flex items-center justify-center">
                        <Boxes className="w-4 h-4 text-blue-600" />
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-900">
                          {getMaterialName(material.materialId)}
                        </p>
                        <p className="text-xs text-gray-500">
                          Required: {material.requiredQuantity} units
                        </p>
                      </div>
                    </div>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleRemoveMaterial(material.materialId)}
                      className="text-red-600 hover:bg-red-50"
                      icon={X}
                    />
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-8 text-gray-500">
                <Boxes className="w-12 h-12 mx-auto text-gray-300 mb-2" />
                <p className="text-sm">No materials added yet</p>
              </div>
            )}
          </div>

          <div className="flex justify-end pt-4 border-t border-gray-100">
            <Button variant="secondary" onClick={handleCloseMaterialsModal}>
              Close
            </Button>
          </div>
        </div>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={isDeleteModalOpen}
        onClose={() => setIsDeleteModalOpen(false)}
        title="Confirm Delete"
        size="sm"
      >
        <div className="space-y-4">
          <p className="text-gray-600">
            Are you sure you want to delete{" "}
            <span className="font-semibold text-gray-900">
              {selectedProduct?.name}
            </span>
            ? This action cannot be undone.
          </p>
          <div className="flex justify-end gap-3">
            <Button
              variant="secondary"
              onClick={() => setIsDeleteModalOpen(false)}
            >
              Cancel
            </Button>
            <Button
              variant="danger"
              onClick={handleConfirmDelete}
              loading={loading}
            >
              Delete
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default ProductsPage;
