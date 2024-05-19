import React from 'react';
import PropTypes from 'prop-types';
import CarDamage from "./CarDamage.jsx";
import CarReturn from "./CarReturn.jsx";

const OrderCard = ({ order }) => {
    const formattedStartDate = new Date(order.rentalStart).toLocaleDateString();
    const formattedEndDate = new Date(order.rentalEnd).toLocaleDateString();
    const formattedTotalPrice = order.totalPrice.toFixed(2);
    const role = localStorage.getItem('role');

    return (
        <div className="card">
            <h3>Order #{order.id}</h3>
            <>
                {role === "ADMIN" && (
                    <div>
                        <p>Client ID: {order.clientId}</p>
                        <p>Car ID: {order.carId}</p>
                    </div>
                )}
            </>
            <p>Rental Start: {formattedStartDate}</p>
            <p>Rental End: {formattedEndDate}</p>
            <p>Status: {order.status}</p>
            <p>Total Price: ${formattedTotalPrice}</p>
            <CarDamage orderId={order.id} authToken={localStorage.getItem('jwtToken')} />
            <CarReturn orderId={order.id} authToken={localStorage.getItem('jwtToken')} carId={order.carId} status={order.status} />
        </div>
    );
};

OrderCard.propTypes = {
    order: PropTypes.shape({
        id: PropTypes.number.isRequired,
        clientId: PropTypes.number.isRequired,
        carId: PropTypes.number.isRequired,
        rentalStart: PropTypes.string.isRequired,
        rentalEnd: PropTypes.string.isRequired,
        status: PropTypes.string.isRequired,
        totalPrice: PropTypes.number.isRequired,
    }).isRequired,
};

export default OrderCard;
