import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';

function EditCarForm({ car, closeModal }) {
    const [brand, setBrand] = useState(car.brand);
    const [model, setModel] = useState(car.model);
    const [availability, setAvailability] = useState(car.availability);
    const [imageURL, setImageURL] = useState(car.imageURL);
    const [priceForDay, setPriceForDay] = useState(car.priceForDay);
    const navigate = useNavigate();


    const handleDelete = async () => {
        if (window.confirm(`Are you sure you want to delete ${car.brand} ${car.model}?`)) {
            try {
                const response = await fetch(`http://localhost:8080/api/cars?carId=${car.id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                    }
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
        const carData = { brand, model, availability, imageURL, priceForDay: parseFloat(priceForDay) };

        try {
            const response = await fetch(`http://localhost:8080/api/cars?carId=${car.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                },
                body: JSON.stringify(carData)
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            closeModal();
            navigate('/adm');
        } catch (error) {
            console.error('Car update failed:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Edit Car</h2>
            <div>
                <label>Brand:</label>
                <input type="text" value={brand} onChange={(e) => setBrand(e.target.value)} required/>
            </div>
            <div>
                <label>Model:</label>
                <input type="text" value={model} onChange={(e) => setModel(e.target.value)} required/>
            </div>
            <div>
                <label>Availability:</label>
                <select value={availability} onChange={(e) => setAvailability(e.target.value === 'true')} required>
                    <option value="true">Available</option>
                    <option value="false">Unavailable</option>
                </select>
            </div>
            <div>
                <label>Image URL:</label>
                <input type="text" value={imageURL} onChange={(e) => setImageURL(e.target.value)} required/>
            </div>
            <div>
                <label>Price Per Day:</label>
                <input type="number" value={priceForDay} onChange={(e) => setPriceForDay(e.target.value)} required/>
            </div>
            <button type="submit">Update Car</button>
            <button type="button" onClick={handleDelete} className="delete-button">Delete Car</button>
        </form>
    );
}

EditCarForm.propTypes = {
    car: PropTypes.object.isRequired,
    closeModal: PropTypes.func.isRequired
};

export default EditCarForm;
