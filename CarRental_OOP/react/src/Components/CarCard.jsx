import React, { useState } from 'react';
import PropTypes from 'prop-types';
import CreateOrderForm from '../Pages/Order/CreateOrder.jsx';
import SimpleModal from './SimpleModal';

const CarCard = ({ car }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const role = localStorage.getItem('role');

    const handleOrderClick = () => {
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div className="card">
            <img src={car.imageURL} alt={`${car.brand} ${car.model}`}/>
            <h3>{car.brand} {car.model}</h3>
            <>
                {role === "ADMIN" && (
                    <p>Car id: {car.id}</p>
                )}
            </>
            <p>{car.availability ? 'Available' : 'Unavailable'}</p>
            <p>${car.priceForDay} per day</p>
            {car.availability && <button onClick={handleOrderClick}>Order now</button>}
            <SimpleModal isOpen={isModalOpen} handleClose={handleCloseModal}>
                <CreateOrderForm carId={car.id} pricePerDay={car.priceForDay} imageURL={car.imageURL} model={car.model} brand={car.brand} />
            </SimpleModal>
        </div>
    );
};

CarCard.propTypes = {
    car: PropTypes.shape({
        id: PropTypes.number.isRequired,
        imageURL: PropTypes.string.isRequired,
        brand: PropTypes.string.isRequired,
        model: PropTypes.string.isRequired,
        availability: PropTypes.bool.isRequired,
        priceForDay: PropTypes.number.isRequired,
    }).isRequired,
};

export default CarCard;
