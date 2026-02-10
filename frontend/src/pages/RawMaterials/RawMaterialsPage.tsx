import { useEffect, useState, ReactNode } from "react";
import { Boxes, Plus, Pencil, Trash2, Search, RefreshCw } from "lucide-react";
import { Card, Button, Table, Modal, Input, Badge } from "../../components";
import {
  fetchRawMaterials,
  createRawMaterial,
  updateRawMaterial,
  deleteRawMaterial,
} from "../../store/slices/rawMaterialsSlice";
import { showNotification } from "../../store/slices/uiSlice";
import { useAppDispatch, useAppSelector } from "../../hooks/useRedux";
import type { RawMaterial } from "../../types";

interface FormData {
  name: string;
  quantity: string;
  unitPrice: string;
  minimumStock: string;
}

interface FormErrors {
  name?: string;
  quantity?: string;
  unitPrice?: string;
  minimumStock?: string;
}

interface StockStatus {
  variant: "danger" | "warning" | "success";
  label: string;
}

interface TableColumn {
  header: string;
  accessor: keyof RawMaterial | "id";
  className?: string;
  render?: (value: any, row?: RawMaterial) => ReactNode;
}

const RawMaterialsPage = () => {
  const dispatch = useAppDispatch();
  const { items: rawMaterials, loading } = useAppSelector(
    (state) => state.rawMaterials,
  );

  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState<boolean>(false);
  const [selectedMaterial, setSelectedMaterial] = useState<RawMaterial | null>(
    null,
  );
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [formData, setFormData] = useState<FormData>({
    name: "",
    quantity: "",
    unitPrice: "",
    minimumStock: "",
  });
  const [errors, setErrors] = useState<FormErrors>({});

  useEffect(() => {
    dispatch(fetchRawMaterials());
  }, [dispatch]);

  const filteredMaterials = rawMaterials.filter((material) =>
    material.name.toLowerCase().includes(searchTerm.toLowerCase()),
  );

  const getStockStatus = (quantity: number): StockStatus => {
    if (quantity === 0) return { variant: "danger", label: "Out of Stock" };
    if (quantity <= 10) return { variant: "warning", label: "Low Stock" };
    return { variant: "success", label: "In Stock" };
  };

  const handleOpenModal = (material: RawMaterial | null = null): void => {
    if (material) {
      setSelectedMaterial(material);
      setFormData({
        name: material.name,
        quantity: material.quantity.toString(),
        unitPrice: material.unitPrice.toString(),
        minimumStock: material.minimumStock.toString(),
      });
    } else {
      setSelectedMaterial(null);
      setFormData({ name: "", quantity: "", unitPrice: "", minimumStock: "" });
    }
    setErrors({});
    setIsModalOpen(true);
  };

  const handleCloseModal = (): void => {
    setIsModalOpen(false);
    setSelectedMaterial(null);
    setFormData({ name: "", quantity: "", unitPrice: "", minimumStock: "" });
    setErrors({});
  };

  const validateForm = (): boolean => {
    const newErrors: FormErrors = {};
    if (!formData.name.trim()) {
      newErrors.name = "Name is required";
    }
    if (!formData.quantity || parseInt(formData.quantity) < 0) {
      newErrors.quantity = "Quantity must be a positive number";
    }
    if (!formData.unitPrice || parseFloat(formData.unitPrice) < 0) {
      newErrors.unitPrice = "Unit price must be a positive number";
    }
    if (!formData.minimumStock || parseInt(formData.minimumStock) < 0) {
      newErrors.minimumStock = "Minimum stock must be a positive number";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (
    e: React.FormEvent<HTMLFormElement>,
  ): Promise<void> => {
    e.preventDefault();
    if (!validateForm()) return;

    const data = {
      name: formData.name.trim(),
      quantity: parseInt(formData.quantity),
      unitPrice: parseFloat(formData.unitPrice),
      minimumStock: parseInt(formData.minimumStock),
    };

    try {
      if (selectedMaterial) {
        await dispatch(
          updateRawMaterial({ id: selectedMaterial.id, data }),
        ).unwrap();
        dispatch(
          showNotification({
            type: "success",
            message: "Material updated successfully!",
          }),
        );
      } else {
        await dispatch(createRawMaterial(data)).unwrap();
        dispatch(
          showNotification({
            type: "success",
            message: "Material created successfully!",
          }),
        );
      }
      handleCloseModal();
      dispatch(fetchRawMaterials());
    } catch (error) {
      dispatch(
        showNotification({
          type: "error",
          message: (error as string) || "An error occurred",
        }),
      );
    }
  };

  const handleDeleteClick = (material: RawMaterial): void => {
    setSelectedMaterial(material);
    setIsDeleteModalOpen(true);
  };

  const handleConfirmDelete = async (): Promise<void> => {
    if (!selectedMaterial) return;
    try {
      await dispatch(deleteRawMaterial(selectedMaterial.id)).unwrap();
      dispatch(
        showNotification({
          type: "success",
          message: "Material deleted successfully!",
        }),
      );
      setIsDeleteModalOpen(false);
      setSelectedMaterial(null);
    } catch (error) {
      dispatch(
        showNotification({
          type: "error",
          message: (error as string) || "Error deleting material",
        }),
      );
    }
  };

  const columns: TableColumn[] = [
    {
      header: "Name",
      accessor: "name",
      render: (value: string) => (
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-100 to-indigo-100 flex items-center justify-center">
            <Boxes className="w-5 h-5 text-blue-600" />
          </div>
          <span className="font-medium text-gray-900">{value}</span>
        </div>
      ),
    },
    {
      header: "Stock Quantity",
      accessor: "quantity",
      render: (value: number) => (
        <span className="font-semibold text-gray-700">{value} units</span>
      ),
    },
    {
      header: "Status",
      accessor: "quantity",
      render: (value: number) => {
        const status = getStockStatus(value);
        return <Badge variant={status.variant}>{status.label}</Badge>;
      },
    },
    {
      header: "Actions",
      accessor: "id",
      className: "text-right",
      render: (_: any, row?: RawMaterial) => (
        <div className="flex items-center justify-end gap-2">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => row && handleOpenModal(row)}
            icon={Pencil}
          />
          <Button
            variant="ghost"
            size="sm"
            onClick={() => row && handleDeleteClick(row)}
            className="text-red-600 hover:bg-red-50"
            icon={Trash2}
          />
        </div>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Raw Materials</h1>
          <p className="text-gray-500 mt-1">
            Manage your inventory of raw materials
          </p>
        </div>
        <div className="flex items-center gap-3">
          <Button
            variant="secondary"
            onClick={() => dispatch(fetchRawMaterials())}
            icon={RefreshCw}
            disabled={loading}
          >
            Refresh
          </Button>
          <Button onClick={() => handleOpenModal()} icon={Plus}>
            Add Material
          </Button>
        </div>
      </div>

      {/* Search */}
      <Card>
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <Input
              placeholder="Search materials..."
              value={searchTerm}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                setSearchTerm(e.target.value)
              }
              icon={Search}
            />
          </div>
        </div>
      </Card>

      {/* Table */}
      <Card padding={false}>
        <div className="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
          <h2 className="font-semibold text-gray-800">Materials Stock</h2>
          <Badge variant="info">{filteredMaterials.length} items</Badge>
        </div>
        <Table
          columns={columns}
          data={filteredMaterials}
          loading={loading}
          emptyMessage="No raw materials found"
        />
      </Card>

      {/* Create/Edit Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={selectedMaterial ? "Edit Material" : "Add New Material"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Material Name"
            placeholder="Enter material name"
            value={formData.name}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
              setFormData({ ...formData, name: e.target.value })
            }
            error={errors.name}
            required
          />
          <Input
            label="Quantity"
            type="number"
            placeholder="Enter quantity"
            value={formData.quantity}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
              setFormData({ ...formData, quantity: e.target.value })
            }
            error={errors.quantity}
            min="0"
            required
          />
          <Input
            label="Unit Price"
            type="number"
            step="0.01"
            placeholder="Enter unit price"
            value={formData.unitPrice}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
              setFormData({ ...formData, unitPrice: e.target.value })
            }
            error={errors.unitPrice}
            min="0"
            required
          />
          <Input
            label="Minimum Stock"
            type="number"
            placeholder="Enter minimum stock"
            value={formData.minimumStock}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
              setFormData({ ...formData, minimumStock: e.target.value })
            }
            error={errors.minimumStock}
            min="0"
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
              {selectedMaterial ? "Update" : "Create"}
            </Button>
          </div>
        </form>
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
              {selectedMaterial?.name}
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

export default RawMaterialsPage;
