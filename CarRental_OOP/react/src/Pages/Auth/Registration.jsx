import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function RegistrationForm() {
    const [username, setUsername] = useState('test');
    const [password, setPassword] = useState('test');
    const [role, setRole] = useState('USER');
    const [fullName, setFullName] = useState('test');
    const [passportNumber, setPassportNumber] = useState('test');
    const [phoneNumber, setPhoneNumber] = useState('3203294910');
    const [confirmPassword, setConfirmPassword] = useState('test');
    const [email, setEmail] = useState('test@gmail.com');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (password !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }

        const registrationData = {
            fullName: fullName,
            passportNumber: passportNumber,
            phoneNumber: phoneNumber,
            email: email,
            username: username,
            password: password,
            role: role
        };

        try {
            const response = await fetch('http://localhost:8080/api/clients', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(registrationData)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Network response was not ok');
            }

            const data = await response.json();
            console.log('Registration success:', data);
            navigate('/login');
        } catch (error) {
            console.error('Registration failed:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Register</h2>
            <div>
                <label>Username:</label>
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required/>
            </div>
            <div>
                <label>Email:</label>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required/>
            </div>
            <div>
                <label>Full Name:</label>
                <input type="fullname" value={fullName} onChange={(e) => setFullName(e.target.value)} required/>
            </div>
            <div>
                <label>Phone Number:</label>
                <input type="phonenumber" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)}
                       required/>
            </div>
            <div>
                <label>Passport Number:</label>
                <input type="passportnumber" value={passportNumber} onChange={(e) => setPassportNumber(e.target.value)} required/>
            </div>
            <div>
                <label>Password:</label>
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required/>
            </div>
            <div>
                <label>Confirm Password:</label>
                <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)}
                       required/>
            </div>
            <button type="submit">Register</button>
        </form>
    );
}

export default RegistrationForm;
