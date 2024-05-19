import './css/App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './Components/AuthContext.jsx';
import LoginForm from './Pages/Auth/Login.jsx';
import HomePage from './Pages/Home.jsx';
import RegistrationForm from './Pages/Auth/Registration.jsx';
import CreateCar from './Pages/Car/CreateCar.jsx';
import AdminPage from './Pages/Manage/AdminPanel.jsx';
import ClientAccount from './Pages/Manage/ClientAccount.jsx';
import AddDamage from "./Pages/Order/AddDamage.jsx";
import AddReturn from "./Pages/Order/AddReturn.jsx";

function App() {
    return (
        <AuthProvider> {/* Оборачиваем Router в AuthProvider */}
            <Router>
                <Routes>
                    <Route path="/login" element={<LoginForm />} />
                    <Route path="/register" element={<RegistrationForm />} />
                    <Route path="/createCar" element={<CreateCar />} />
                    <Route path="/adm" element={<AdminPage />} />
                    <Route path="/client" element={<ClientAccount />} />
                    <Route path="/" element={<HomePage />} />
                    <Route path="/add-damage/:orderId" element={<AddDamage />} />
                    <Route path="/add-return/:orderId/:carId/:status" element={<AddReturn />} />
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
