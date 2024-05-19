import React, { useState, useEffect } from 'react';
import OrderCard from "../../Components/OrderCard.jsx";
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../Components/AuthContext.jsx';

function ClientAccountPage() {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [client, setClient] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('jwtToken');
    const clientId = localStorage.getItem('clientId');

    if (!clientId || !token) {
      console.log('No token found, showing login...');
      navigate('/login')
      return;
    }

    const fetchOrders = async () => {
      setIsLoading(true);
      try {
        const response = await fetch(`http://localhost:8080/api/orders?clientId=${clientId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          setOrders(data);
        } else {
          console.error('Failed to fetch orders');
          throw new Error('Network response was not ok');
        }
      } catch (error) {
        console.error('Error fetching orders:', error);
      } finally {
        setIsLoading(false);
      }
    };

    const fetchClient = async () => {
      setIsLoading(true);
      try {
        const response = await fetch(`http://localhost:8080/api/clients?clientId=${clientId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          setClient(data);
        } else {
          console.error('Failed to fetch client');
          throw new Error('Network response was not ok');
        }
      } catch (error) {
        console.error('Error fetching orders:', error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchOrders();
    fetchClient();

  }, [navigate, user]);

  if (isLoading) {
    return <div>Loading orders...</div>;
  }

  if (orders.length === 0) {
    return <div>No orders available.</div>;
  }

  return (
      <div>
        <h1>Client Information</h1>
        {client ? (
            <div>
              <p>Name: {client.fullName}</p>
              <p>Email: {client.email}</p>
              <p>Phone Number: {client.phoneNumber}</p>
              <p>Passport Number: {client.passportNumber}</p>
              <p>Username: {client.username}</p>
            </div>
        ) : (
            <p>No client data available.</p>
        )}
        <div className="card-container">
          {orders.map(order => <OrderCard key={order.id} order={order} />)}
        </div>
      </div>
  );
}

export default ClientAccountPage;
