import { NavLink } from "react-router-dom";
import {
  LayoutDashboard,
  Package,
  Boxes,
  Menu,
  X,
  LucideIcon,
} from "lucide-react";
import { toggleSidebar } from "../../store/slices/uiSlice";
import { useAppDispatch, useAppSelector } from "../../hooks/useRedux";

interface MenuItem {
  path: string;
  label: string;
  icon: LucideIcon;
}

const Sidebar = () => {
  const { sidebarOpen } = useAppSelector((state) => state.ui);
  const dispatch = useAppDispatch();

  const menuItems: MenuItem[] = [
    { path: "/", label: "Dashboard", icon: LayoutDashboard },
    { path: "/products", label: "Products", icon: Package },
    { path: "/raw-materials", label: "Raw Materials", icon: Boxes },
  ];

  return (
    <>
      {/* Mobile overlay */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-20 lg:hidden"
          onClick={() => dispatch(toggleSidebar())}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`fixed top-0 left-0 h-screen bg-white shadow-lg z-30 transition-transform duration-300 ease-in-out w-64 flex-shrink-0 ${
          sidebarOpen ? "translate-x-0" : "-translate-x-full"
        } lg:translate-x-0 lg:sticky lg:top-0`}
      >
        {/* Logo */}
        <div className="flex items-center justify-between px-6 py-5 border-b border-gray-100">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl flex items-center justify-center shadow-lg shadow-blue-500/30">
              <Boxes className="w-5 h-5 text-white" />
            </div>
            <div>
              <h1 className="font-bold text-gray-800 text-lg">Inventory</h1>
              <p className="text-xs text-gray-400">Management</p>
            </div>
          </div>
          <button
            onClick={() => dispatch(toggleSidebar())}
            className="lg:hidden p-2 rounded-lg hover:bg-gray-100 text-gray-500"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Navigation */}
        <nav className="px-4 py-6">
          <ul className="space-y-2">
            {menuItems.map((item) => (
              <li key={item.path}>
                <NavLink
                  to={item.path}
                  className={({ isActive }) =>
                    `flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                      isActive
                        ? "bg-blue-50 text-blue-600 font-medium shadow-sm"
                        : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                    }`
                  }
                >
                  <item.icon className="w-5 h-5" />
                  <span>{item.label}</span>
                </NavLink>
              </li>
            ))}
          </ul>
        </nav>

        {/* Footer */}
        <div className="absolute bottom-0 left-0 right-0 p-4 border-t border-gray-100">
          <div className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl p-4">
            <p className="text-xs text-gray-500 mb-1">System Status</p>
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
              <span className="text-sm font-medium text-gray-700">Online</span>
            </div>
          </div>
        </div>
      </aside>

      {/* Mobile menu button */}
      <button
        onClick={() => dispatch(toggleSidebar())}
        className={`fixed top-4 left-4 z-40 lg:hidden p-2 bg-white rounded-lg shadow-md text-gray-600 hover:bg-gray-50 transition-opacity ${
          sidebarOpen ? "opacity-0 pointer-events-none" : "opacity-100"
        }`}
      >
        <Menu className="w-6 h-6" />
      </button>
    </>
  );
};

export default Sidebar;
