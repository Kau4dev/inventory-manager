import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Layout } from "./components";
import { DashboardPage, ProductsPage, RawMaterialsPage } from "./pages";

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/products" element={<ProductsPage />} />
          <Route path="/raw-materials" element={<RawMaterialsPage />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
