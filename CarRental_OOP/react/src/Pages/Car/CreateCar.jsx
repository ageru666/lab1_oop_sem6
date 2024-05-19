import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';

function CreateCarForm() {
    const [brand, setBrand] = useState('');
    const [model, setModel] = useState('');
    const [availability, setAvailability] = useState(false);
    const [imageURL, setImageURL] = useState('');
    const [priceForDay, setPriceForDay] = useState('');
    const navigate = useNavigate();
    const role = localStorage.getItem('role');


    useEffect(() => {
        if (role !== "ADMIN")
        {
            navigate('/');
            return;
        }
    })


    const handleSubmit = async (event) => {
        event.preventDefault();

        const carData = {
            brand,
            model,
            availability,
            imageURL,
            priceForDay: parseFloat(priceForDay)
        };

        try {
            const response = await fetch('http://localhost:8080/api/cars', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                },
                body: JSON.stringify(carData)
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            console.log('Car creation success:');
            navigate('/');
        } catch (error) {
            console.error('Car creation failed:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Add New Car</h2>
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
            <button type="submit">Add Car</button>
        </form>
    );
}

export default CreateCarForm;
