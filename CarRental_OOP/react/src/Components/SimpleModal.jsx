import React from 'react';
import PropTypes from 'prop-types';

const SimpleModal = ({ isOpen, handleClose, children }) => {
    if (!isOpen) {
        return null;
    }

    return (
        <div className="modal-overlay">
            <div className="modal">
                {children}
                <button onClick={handleClose}>Close</button>
            </div>
        </div>
    );
};

SimpleModal.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    children: PropTypes.node
};

export default SimpleModal;
