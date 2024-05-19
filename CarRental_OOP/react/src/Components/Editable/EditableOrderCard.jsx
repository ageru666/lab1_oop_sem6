import React, { useState } from 'react';
import PropTypes from 'prop-types';
import EditOrderForm from './EditOrderForm.jsx';
import SimpleModal from '../SimpleModal.jsx';
import OrderCard from "../OrderCard.jsx";

const EditableOrderCard = ({ orders }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentOrder, setCurrentOrder] = useState(null);


    const handleEditClick = (order) => {
        setCurrentOrder(order);
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div className="card-container">
            {orders.map(order => (
                <div key={order.id}>
                    <OrderCard order={order} />
                    <button onClick={() => handleEditClick(order)}>Edit</button>
                </div>
            ))}
            {currentOrder && (
                <SimpleModal isOpen={isModalOpen} handleClose={handleCloseModal}>
                    <EditOrderForm order={currentOrder} closeModal={handleCloseModal}/>
                </SimpleModal>
            )}
        </div>
    );
};

EditableOrderCard.propTypes = {
    orders: PropTypes.array.isRequired,
};

export default EditableOrderCard;
