// EquipTrack - Main JavaScript File

// Utility Functions
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// API Helper Functions
async function fetchAPI(url, options = {}) {
    const token = localStorage.getItem('token');
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    try {
        const response = await fetch(url, {
            ...options,
            headers
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// Equipment Functions
async function loadEquipment(filters = {}) {
    let url = '/api/equipment/available';
    const params = new URLSearchParams(filters);
    if (params.toString()) {
        url += '?' + params.toString();
    }

    try {
        const equipment = await fetchAPI(url);
        return equipment;
    } catch (error) {
        console.error('Error loading equipment:', error);
        return [];
    }
}

async function searchEquipment(keyword) {
    try {
        const equipment = await fetchAPI(`/api/equipment/search?keyword=${encodeURIComponent(keyword)}`);
        return equipment;
    } catch (error) {
        console.error('Error searching equipment:', error);
        return [];
    }
}

// Booking Functions
async function createBooking(bookingData) {
    try {
        const booking = await fetchAPI('/api/bookings', {
            method: 'POST',
            body: JSON.stringify(bookingData)
        });
        return booking;
    } catch (error) {
        console.error('Error creating booking:', error);
        throw error;
    }
}

async function loadUserBookings(userId) {
    try {
        const bookings = await fetchAPI(`/api/bookings/customer/${userId}`);
        return bookings;
    } catch (error) {
        console.error('Error loading bookings:', error);
        return [];
    }
}

// User Functions
function getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

function isUserLoggedIn() {
    return getCurrentUser() !== null;
}

function logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    window.location.href = '/login';
}

// Form Validation
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validatePassword(password) {
    return password.length >= 6;
}

function validatePhoneNumber(phone) {
    const re = /^\+?[\d\s\-\(\)]+$/;
    return re.test(phone);
}

// Notification System
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 1rem 2rem;
        border-radius: 8px;
        background: ${type === 'success' ? '#10b981' : type === 'error' ? '#ef4444' : '#4A90E2'};
        color: white;
        font-weight: 600;
        box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        z-index: 10000;
        animation: slideIn 0.3s ease-out;
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// Date Utilities
function getMinDate() {
    const today = new Date();
    return today.toISOString().split('T')[0];
}

function calculateDaysBetween(startDate, endDate) {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffTime = Math.abs(end - start);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays + 1; // Include both start and end dates
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    console.log('EquipTrack initialized');
    
    // Check for user session
    const user = getCurrentUser();
    if (user) {
        console.log('User logged in:', user.email);
    }
    
    // Load equipment icons
    loadEquipmentIcons();
});

// Replace letter placeholders with the excavator icon
function loadEquipmentIcons() {
    const equipmentCards = document.querySelectorAll('.equipment-image');
    
    equipmentCards.forEach(card => {
        const initialLetter = card.querySelector('.equipment-initial');
        
        if (initialLetter) {
            // Create image element using the excavator icon
            const img = document.createElement('img');
            img.src = '/images/excavator-icon.svg';
            img.alt = 'Equipment Icon';
            img.style.cssText = 'width: 120px; height: 120px; object-fit: contain;';
            
            card.innerHTML = '';
            card.appendChild(img);
        }
    });
}

// Export for use in other scripts
window.EquipTrack = {
    formatCurrency,
    formatDate,
    fetchAPI,
    loadEquipment,
    searchEquipment,
    createBooking,
    loadUserBookings,
    getCurrentUser,
    isUserLoggedIn,
    logout,
    validateEmail,
    validatePassword,
    validatePhoneNumber,
    showNotification,
    getMinDate,
    calculateDaysBetween
};
