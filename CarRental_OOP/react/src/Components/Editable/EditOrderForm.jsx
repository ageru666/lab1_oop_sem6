import React, { useState} from 'react';
import { useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import CarDamage from '../CarDamage';
import CarReturn from '../CarReturn';

function EditOrderForm({ order, closeModal }) {
    const [clientId, setClientId] = useState(order.clientId);
    const [carId, setCarId] = useState(order.carId);
    const [rentalStart, setRentalStart] = useState(order.rentalStart.split('T')[0]);
    const [rentalEnd, setRentalEnd] = useState(order.rentalEnd.split('T')[0]);
    const [status, setStatus] = useState(order.status);
    const [totalPrice, setTotalPrice] = useState(order.totalPrice);
    const navigate = useNavigate();
    const today = new Date().toISOString().split('T')[0];

    const handleDelete = async () => {
        if (window.confirm(`Are you sure you want to delete order ${order.id}?`)) {
            try {
                const delOrder = {
                    id: order.id,
                    clientId: parseInt(clientId, 10),
                    carId: parseInt(carId, 10),
                    rentalStart: new Date(rentalStart).toISOString(),
                    rentalEnd: new Date(rentalEnd).toISOString(),
                    status: status,
                    totalPrice: parseFloat(totalPrice)
                };
                const response = await fetch(`http://localhost:8080/api/orders`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                    },
                    body: JSON.stringify(delOrder)
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                closeModal();
                navigate('/adm');
            } catch (error) {
                console.error('Car delete failed:', error);
            }
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const updatedOrder = {
            id: order.id,
            clientId: parseInt(clientId, 10),
            carId: parseInt(carId, 10),
            rentalStart: new Date(rentalStart).toISOString(),
            rentalEnd: new Date(rentalEnd).toISOString(),
            status: status,
            totalPrice: parseFloat(totalPrice)
        };

        try {
            const responseOrder = await fetch(`http://localhost:8080/api/orders`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                },
                body: JSON.stringify(updatedOrder)
            });

            if (!responseOrder.ok) {
                throw new Error('Network response was not ok');
            }

            const responseCar = await fetch(`http://localhost:8080/api/cars?carId=${carId}&updAvailable=true`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                },
                body: JSON.stringify({ id: carId, status: status })
            });

            if (!responseCar.ok) {
                throw new Error('Car update failed');
            }

            console.log('Order update success');
            closeModal();
            navigate('/adm');
        } catch (error) {
            console.error('Order update failed:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h3>Edit Order #{order.id}</h3>
            <div>
                <label>Client ID:</label>
                <input type="number" value={clientId} onChange={(e) => setClientId(e.target.value)} required/>
            </div>
            <div>
                <label>Car ID:</label>
                <input type="number" value={carId} onChange={(e) => setCarId(e.target.value)} required/>
            </div>
            <div style={{display: 'flex', alignItems: 'center', marginBottom: '10px'}}>
                <label style={{marginRight: '10px'}}>Rental Start:</label>
                <input type="date" value={rentalStart} onChange={(e) => setRentalStart(e.target.value)} min={today}
                       required/>
            </div>
            <div style={{display: 'flex', alignItems: 'center', marginBottom: '10px'}}>
                <label style={{marginRight: '10px'}}>Rental End:</label>
                <input type="date" value={rentalEnd} onChange={(e) => setRentalEnd(e.target.value)} required/>
            </div>
            <div>
                <label>Status:</label>
                <select value={status} onChange={(e) => setStatus(e.target.value)} required>
                    <option value="pending">Pending</option>
                    <option value="confirmed">Confirmed</option>
                    <option value="completed">Completed</option>
                    <option value="cancelled">Cancelled</option>
                </select>
            </div>
            <div>
                <label>Total Price:</label>
                <input type="number" value={totalPrice} onChange={(e) => setTotalPrice(e.target.value)} required/>
            </div>
            <button type="submit">Update Order</button>
            <button type="button" onClick={handleDelete} className="delete-button">Delete Order</button>
        </form>
    );
}

EditOrderForm.propTypes = {
    order: PropTypes.shape({
        id: PropTypes.number.isRequired,
        clientId: PropTypes.number.isRequired,
        carId: PropTypes.number.isRequired,
        rentalStart: PropTypes.string.isRequired,
        rentalEnd: PropTypes.string.isRequired,
        status: PropTypes.string.isRequired,
        totalPrice: PropTypes.number.isRequired
    }).isRequired,
    closeModal: PropTypes.func.isRequired
};

export default EditOrderForm;
