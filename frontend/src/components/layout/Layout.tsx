import { ReactNode, useEffect } from "react";
import Sidebar from "./Sidebar";
import { clearNotification } from "../../store/slices/uiSlice";
import { CheckCircle, XCircle, X } from "lucide-react";
import { useAppDispatch, useAppSelector } from "../../hooks/useRedux";

interface LayoutProps {
  children: ReactNode;
}

const Layout = ({ children }: LayoutProps) => {
  const { notification } = useAppSelector((state) => state.ui);
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (notification) {
      const timer = setTimeout(() => {
        dispatch(clearNotification());
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [notification, dispatch]);

  return (
    <div className="flex min-h-screen bg-gray-50">
      <Sidebar />
      <main className="flex-1 w-full lg:w-auto overflow-x-hidden">
        <div className="p-4 md:p-6 lg:p-8 pt-16 lg:pt-8">{children}</div>
      </main>

      {/* Notification Toast */}
      {notification && (
        <div
          className={`fixed bottom-4 right-4 z-50 flex items-center gap-3 px-4 py-3 rounded-xl shadow-lg ${
            notification.type === "success"
              ? "bg-green-50 border border-green-200 text-green-800"
              : "bg-red-50 border border-red-200 text-red-800"
          }`}
        >
          {notification.type === "success" ? (
            <CheckCircle className="w-5 h-5 text-green-500" />
          ) : (
            <XCircle className="w-5 h-5 text-red-500" />
          )}
          <span className="text-sm font-medium">{notification.message}</span>
          <button
            onClick={() => dispatch(clearNotification())}
            className="p-1 rounded-full hover:bg-white/50"
          >
            <X className="w-4 h-4" />
          </button>
        </div>
      )}
    </div>
  );
};

export default Layout;
