import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../Components/AuthContext.jsx';
import {jwtDecode} from "jwt-decode";

// eslint-disable-next-line react/prop-types
function LoginForm({ onLoginSuccess }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleNavClick = () => {
        navigate('/register');
    };

    const handleLoginSuccess = (data) => {
        localStorage.setItem('jwtToken', data.jwt);
        localStorage.setItem('clientId', data.clientId);

        const decodedToken = jwtDecode(data.jwt);
        const userData = {
            username: decodedToken.sub,
            role: decodedToken.role,
            clientId: decodedToken.clientId
        };

        localStorage.setItem('user', JSON.stringify(userData));
        login(userData, data.jwt);

        if (onLoginSuccess) {
            onLoginSuccess();
        } else {
            navigate('/');
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const loginData = {
            username: username,
            password: password
        };

        try {
            const response = await fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginData)
            });

            if (!response.ok) {
                const errMsg = await response.text();
                throw new Error(errMsg || 'Network response was not ok');
            }

            const data = await response.json();
            console.log('Login success:', data);
            handleLoginSuccess(data);
        } catch (error) {
            console.error('Login failed:', error);
            setError(error.message || 'Failed to login');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Login</h2>
            {error && <p className="error">{error}</p>}
            <div>
                <label>Username:</label>
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
            </div>
            <div>
                <label>Password:</label>
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            </div>
            <button type="submit">Login</button>
            <button type="button" onClick={handleNavClick}>Register</button>
        </form>
    );
}

export default LoginForm;