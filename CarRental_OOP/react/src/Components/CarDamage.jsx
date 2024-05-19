import React, { useState, useEffect } from 'react';
import PropTypes from "prop-types";

function CarDamageComponent({ orderId, authToken }) {
    const [carDamage, setCarDamage] = useState([]);
    const role = localStorage.getItem('role');

    useEffect(() => {
        const fetchCarDamage = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/car-damage?orderId=${orderId}`, {
                    headers: { 'Authorization': `Bearer ${authToken}` }
                });
                const data = await response.json();
                console.log(data);
                setCarDamage(Array.isArray(data) ? data : []);
            } catch (error) {
                console.error('Failed to fetch car damages:', error);
                setCarDamage([]);
            }
        };

        fetchCarDamage();
    }, [orderId, authToken]);

    return (
        <div>
            {carDamage.length > 0 ? (
                carDamage.map((damage, index) => (
                    <div key={index}>
                        <h3>Car damages info</h3>
                        <p>Description: {damage.description}</p>
                        <p>Repair Cost: ${damage.repairCost ? damage.repairCost.toFixed(2) : 'N/A'}</p>
                    </div>
                ))
            ) : (
                <>
                    {role === "ADMIN" && (
                        <button type="button" onClick={() => window.open(`/add-damage/${orderId}`, '_blank')}>Add Damage</button>
                    )}
                </>
            )}
        </div>
    );
}

CarDamageComponent.propTypes = {
    orderId: PropTypes.number.isRequired,
    authToken: PropTypes.string.isRequired
};

export default CarDamageComponent;
