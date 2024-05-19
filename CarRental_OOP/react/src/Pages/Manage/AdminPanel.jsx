import React, { useState, useEffect } from 'react';
import EditableCarCard from '../../Components/Editable/EditableCarCard.jsx';
import EditableOrderCard from '../../Components/Editable/EditableOrderCard.jsx';
import { useNavigate } from 'react-router-dom';



const AdminPage = () => {
    const [cars, setCars] = useState([]);
    const [orders, setOrders] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const role = localStorage.getItem('role');

    useEffect(() => {
        if (role !== "ADMIN")
        {
            navigate('/');
            return;
        }
        const fetchCars = async () => {
            setIsLoading(true);
            try {
                const response = await fetch('http://localhost:8080/api/cars');
                if (response.ok) {
                    const data = await response.json();
                    setCars(data);
                } else {
                    throw new Error('Network response was not ok');
                }
            } catch (error) {
                console.error('Error fetching cars:', error);
            } finally {
                setIsLoading(false);
            }
        };
        const fetchOrders = async () => {
            setIsLoading(true);
            try {
                const response = await fetch('http://localhost:8080/api/orders', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                    }
                });
                if (response.ok) {
                    const data = await response.json();
                    setOrders(data);
                } else {
                    throw new Error('Network response was not ok');
                }
            } catch (error) {
                console.error('Error fetching cars:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchCars();
        fetchOrders();
    }, []);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (cars.length === 0) {
        return <div>No data available.</div>;
    }

    return (
        <div>
            <h1>Admin Dashboard</h1>
            <h2>Cars </h2>
            <EditableCarCard cars={cars}/>
            <h2>Orders </h2>
            <EditableOrderCard orders={orders}/>
        </div>
    );
};

export default AdminPage;
