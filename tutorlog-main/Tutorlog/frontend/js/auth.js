const API_URL = 'http://localhost:8080/api';

function toggleForms() {
    document.getElementById('loginForm').classList.toggle('active');
    document.getElementById('registerForm').classList.toggle('active');
    document.getElementById('message').innerHTML = '';
}

function showMessage(message, type) {
    const msgElement = document.getElementById('message');
    msgElement.textContent = message;
    msgElement.className = `message ${type}`;
    msgElement.style.display = 'block';
}

async function handleRegister(event) {
    event.preventDefault();
    
    const fullName = document.getElementById('fullName').value;
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    if(password !== confirmPassword) {
        showMessage('❌ Passwords do not match', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                fullName,
                email,
                password
            })
        });
        
        const data = await response.json();
        
        if(data.success) {
            showMessage('✅ Registration successful! Please login.', 'success');
            setTimeout(() => {
                toggleForms();
                document.getElementById('registerForm').reset();
            }, 1500);
        } else {
            showMessage('❌ ' + (data.message || 'Registration failed'), 'error');
        }
    } catch(error) {
        showMessage('❌ An error occurred. Please try again.', 'error');
        console.error(error);
    }
}

async function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email,
                password
            })
        });
        
        const data = await response.json();
        
        if(data.success) {
            localStorage.setItem('user', JSON.stringify(data.user));
            localStorage.setItem('token', data.token);
            
            showMessage('✅ Login successful! Redirecting...', 'success');
            
            setTimeout(() => {
                if(data.user.role === 'ADMIN') {
                    window.location.href = 'admin.html';
                } else {
                    window.location.href = 'dashboard.html';
                }
            }, 1000);
        } else {
            showMessage('❌ ' + (data.message || 'Login failed'), 'error');
        }
    } catch(error) {
        showMessage('❌ An error occurred. Please try again.', 'error');
        console.error(error);
    }
}

function logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    window.location.href = 'index.html';
}