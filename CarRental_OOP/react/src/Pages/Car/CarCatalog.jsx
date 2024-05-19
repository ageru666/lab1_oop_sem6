import React, { useState, useEffect } from 'react';
import CarCard from '../../Components/CarCard.jsx';

function CarCatalog() {
    const [cars, setCars] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
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

        fetchCars();
    }, []);

    if (isLoading) {
        return <div>Loading cars...</div>;
    }

    if (cars.length === 0) {
        return <div>No cars available.</div>;
    }

    return (
        <div className="card-container">
            {cars.map(car => <CarCard key={car.id} car={car} />)}
        </div>
    );
}

export default CarCatalog;
