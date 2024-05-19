import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

function AddDamage() {
    const { orderId } = useParams();
    const [description, setDescription] = useState('');
    const [repairCost, setRepairCost] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const payload = {
                orderId: parseInt(orderId),
                description: description,
                repairCost: parseFloat(repairCost)
            };
            const response = await fetch(`http://localhost:8080/api/car-damage`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                navigate(`/adm`);
            } else {
                throw new Error('Failed to add damage details');
            }
        } catch (error) {
            console.error('Error adding damage details:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h1>Add Damage for Order #{orderId}</h1>
            <label>
                Description:
                <input type="text" value={description} onChange={e => setDescription(e.target.value)} required />
            </label>
            <label>
                Repair Cost:
                <input type="number" value={repairCost} onChange={e => setRepairCost(e.target.value)} required />
            </label>
            <button type="submit">Submit</button>
        </form>
    );
}

export default AddDamage;
