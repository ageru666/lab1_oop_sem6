import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import PropTypes from "prop-types";

function AddReturn() {
    const { orderId, carId, status } = useParams();
    const [returnDate, setReturnDate] = useState('');
    const [carCondition, setCarCondition] = useState('');
    const [notes, setNotes] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const payload = {
                orderId: parseInt(orderId),
                returnDate: new Date(returnDate).toISOString(),
                carCondition: carCondition,
                notes: notes
            };
            const response = await fetch(`http://localhost:8080/api/car-return`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                throw new Error('Failed to add return');
            }

            const responseCar = await fetch(`http://localhost:8080/api/cars?carId=${carId}&updAvailable=true`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                },
                body: JSON.stringify({ id: carId, status: "" })
            });

            if (!responseCar.ok) {
                throw new Error('Car update failed');
            }

            navigate(`/adm`);
        } catch (error) {
            console.error('Error adding return details:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h1>Add Return for Order #{orderId}</h1>
            <label>
                Return Date:
                <input type="date" value={returnDate} onChange={e => setReturnDate(e.target.value)} required />
            </label>
            <label>
                Car Condition:
                <input type="text" value={carCondition} onChange={e => setCarCondition(e.target.value)}  />
            </label>
            <label>
                Notes:
                <textarea value={notes} onChange={e => setNotes(e.target.value)} />
            </label>
            <button type="submit">Submit</button>
        </form>
    );
}


export default AddReturn;
