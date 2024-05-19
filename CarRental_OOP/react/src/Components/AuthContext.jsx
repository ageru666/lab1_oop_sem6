import React, { createContext, useContext, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import {jwtDecode} from "jwt-decode";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('jwtToken'));

    useEffect(() => {
        const storedToken = localStorage.getItem('jwtToken');

        if (storedToken) {
            const decoded = jwtDecode(storedToken);
            const userData = {
                username: decoded.sub,
                role: decoded.role,
                clientId: decoded.clientId
            };
            localStorage.setItem('role', userData.role);
            setUser(userData);
            setToken(storedToken);
        }
    }, []);

    const login = (userData, newToken) => {
        localStorage.setItem('jwtToken', newToken);
        localStorage.setItem('user', JSON.stringify(userData));
        localStorage.setItem('role', userData.role);
        setUser(userData);
        setToken(newToken);
    };

    const logout = () => {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('user');
        localStorage.removeItem('role');
        setUser(null);
        setToken(null);
    };

    return (
        <AuthContext.Provider value={{ user, token, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

AuthProvider.propTypes = {
    children: PropTypes.node.isRequired,
};

export const useAuth = () => useContext(AuthContext);
