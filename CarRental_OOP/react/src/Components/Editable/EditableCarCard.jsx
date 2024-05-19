import React, { useState } from 'react';
import PropTypes from 'prop-types';
import EditCarForm from './EditCarForm.jsx';
import SimpleModal from '../SimpleModal.jsx';
import CarCard from "../CarCard.jsx";

const EditableCarCard = ({ cars }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentCar, setCurrentCar] = useState(null);


    const handleEditClick = (car) => {
        setCurrentCar(car);
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div className="card-container">
            {cars.map(car => (
                <div key={car.id}>
                    <CarCard car={car} />
                    <button onClick={() => handleEditClick(car)}>Edit</button>
                </div>
            ))}
            {currentCar && (
                <SimpleModal isOpen={isModalOpen} handleClose={handleCloseModal}>
                    <EditCarForm car={currentCar} closeModal={handleCloseModal}/>
                </SimpleModal>
            )}
        </div>
    );
};

EditableCarCard.propTypes = {
    cars: PropTypes.array.isRequired,
};

export default EditableCarCard;
