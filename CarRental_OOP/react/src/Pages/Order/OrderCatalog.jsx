import React, { useState, useEffect } from 'react';
import OrderCard from '../../Components/OrderCard.jsx';

function OrderCatalog() {
    const [orders, setOrders] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchOrders = async () => {
            setIsLoading(true);
            try {
                const response = await fetch('http://localhost:8080/api/orders');
                if (response.ok) {
                    const data = await response.json();
                    setOrders(data);
                } else {
                    throw new Error('Network response was not ok');
                }
            } catch (error) {
                console.error('Error fetching orders:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchOrders();
    }, []);

    if (isLoading) {
        return <div>Loading orders...</div>;
    }

    if (orders.length === 0) {
        return <div>No orders available.</div>;
    }

    return (
        <div className="card-container">
            {orders.map(order => <OrderCard key={order.id} order={order} />)}
        </div>
    );
}

export default OrderCatalog;