import React, { useState, useEffect } from 'react';
import PropTypes from "prop-types";

function CarReturnComponent({ orderId, authToken, carId, status}) {
    const [carReturn, setCarReturn] = useState(null);
    const role = localStorage.getItem('role');

    useEffect(() => {
        const fetchCarReturn = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/car-return?orderId=${orderId}`, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                    },
                });
                const data = await response.json();
                if (data) {
                    setCarReturn(data);
                }
            } catch (error) {
                console.error('Failed to fetch car return:', error);
            }
        };

        fetchCarReturn();
    }, [orderId, authToken]);

    return (
        <div>
            {carReturn ? (
                <div>
                    <h3>Car return info</h3>
                    <p>Return Date: {new Date(carReturn.returnDate).toLocaleDateString()}</p>
                    {carReturn.carCondition && carReturn.carCondition.trim() !== "" && (
                        <p>Car Condition: {carReturn.carCondition}</p>
                    )}
                    <p>Notes: {carReturn.notes}</p>
                </div>
            ) : (
                <>
                    {role === "ADMIN" && (
                        <button type="button" onClick={() => window.open(`/add-return/${orderId}/${carId}/${status}`, '_blank')}>Add Return</button>
                    )}
                </>
            )}
        </div>
    );
}

CarReturnComponent.propTypes = {
    orderId: PropTypes.number.isRequired,
    authToken: PropTypes.string.isRequired,
    carId: PropTypes.number.isRequired,
    status: PropTypes.string.isRequired
};

export default CarReturnComponent;
