import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../Components/AuthContext.jsx';
import CarCatalog from './Car/CarCatalog.jsx';

function HomePage() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();


    const handleLogout = () => {
        logout();
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('clientId');
        navigate('/');
    };

    return (
        <div>
            <h1>Welcome to the Car Rental System</h1>
            <div>
                {user ? (
                    <>
                        <button onClick={handleLogout} className="button">Logout</button>
                        {user.role === "ADMIN" && (
                            <div>
                                <Link to="/adm" className="button">Admin Panel</Link>
                                <Link to="/createCar" className="button">Add new car</Link>
                            </div>
                        )}
                        {user.role === "USER" && (
                            <Link to="/client" className="button">Client Cabinet</Link>
                        )}
                    </>
                ) : (
                    <>
                        <Link to="/login" className="button">Login</Link>
                        <Link to="/register" className="button">Register</Link>
                    </>
                )}
                <CarCatalog />
            </div>
        </div>
    );
}

export default HomePage;
