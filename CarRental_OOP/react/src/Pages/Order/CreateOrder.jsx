import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import PropTypes from "prop-types";
import SimpleModal from '../../Components/SimpleModal.jsx';
import LoginForm from '../Auth/Login.jsx';
import { useAuth } from '../../Components/AuthContext.jsx';

function CreateOrderForm({ carId, pricePerDay, imageURL, brand, model }) {
    const clientId = localStorage.getItem('clientId');
    const [rentalStart, setRentalStart] = useState('');
    const [rentalEnd, setRentalEnd] = useState('');
    const status = useState('pending');
    const [totalPrice, setTotalPrice] = useState('');
    const navigate = useNavigate();
    const today = new Date().toISOString().split('T')[0];
    const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);
    const { user, token } = useAuth();

    const handleLoginSuccess = () => {
        setIsLoginModalOpen(false);
    };

    const handleModalClose = () => {
        setIsLoginModalOpen(false);
        navigate('/');
    };

    const handleDateChange = () => {
        let newTotalPrice = 0;
        if (rentalStart && rentalEnd) {
            const startDate = new Date(rentalStart);
            const endDate = new Date(rentalEnd);
            const diffTime = Math.abs(endDate - startDate);
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) || 1; // Default to 1 if same day
            newTotalPrice = diffDays * pricePerDay;
        }
        setTotalPrice(newTotalPrice);
    };

    useEffect(() => {
        handleDateChange();
    }, [rentalStart, rentalEnd, pricePerDay]);

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!clientId || !token) {
            console.log('No token found, showing login modal...');
            setIsLoginModalOpen(true);
            return;
        }

        const orderData = {
            clientId: parseInt(clientId, 10),
            carId: parseInt(carId, 10),
            rentalStart: new Date(rentalStart).toISOString(),
            rentalEnd: new Date(rentalEnd).toISOString(),
            status: status[0],
            totalPrice: parseFloat(totalPrice)
        };
        try {
            const response = await fetch('http://localhost:8080/api/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                },
                body: JSON.stringify(orderData)
            });

            if (!response.ok) {
                const errorDetails = response.statusText || 'No error details';
                throw new Error(`Network response was not ok: ${response.status} ${errorDetails}`);
            }

            if (response.status !== 204) {
                const data = await response.json();
                console.log('Order creation success:', data);
            } else {
                console.log('Order created successfully, but no data was returned.');
            }
            navigate('/client');
        } catch (error) {
            console.error('Order creation failed:', error.message || error);
        }
    };

    return (
        <>
        <form onSubmit={handleSubmit}>
            <h2>Order {brand} {model}</h2>
            <div style={{width: '100%', height: '40%'}}>
                <img src={imageURL} alt={`${brand} ${model}`}/>
            </div>
            <div style={{display: 'flex', alignItems: 'center', marginBottom: '10px'}}>
                <label style={{marginRight: '10px'}}>Rental Start:</label>
                <input
                    type="date"
                    value={rentalStart}
                    onChange={(e) => setRentalStart(e.target.value)}
                    min={today}
                    required
                />
            </div>
            <div style={{display: 'flex', alignItems: 'center', marginBottom: '10px'}}>
                <label style={{marginRight: '10px'}}>Rental End:</label>
                <input type="date" value={rentalEnd} onChange={(e) => setRentalEnd(e.target.value)} required/>
            </div>

            <div>
                <label>Total Price (Day: {pricePerDay}$)</label>
                <input type="number" value={Number(totalPrice).toFixed(2)} readOnly/>
            </div>
            <button type="submit">Create Order</button>
        </form>
            <SimpleModal isOpen={isLoginModalOpen} handleClose={handleModalClose}>
                <LoginForm onSuccess={handleLoginSuccess} />
            </SimpleModal>
        </>
    );
}

CreateOrderForm.propTypes = {
    carId: PropTypes.number.isRequired,
    pricePerDay: PropTypes.number.isRequired
};

export default CreateOrderForm;
