const API_URL = 'http://localhost:8080/api';

let pdfLinks = [];

document.addEventListener('DOMContentLoaded', () => {
    checkAdminAccess();
    loadAllLessons();
    loadStats();
});

function checkAdminAccess() {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if(!user.role || user.role !== 'ADMIN') {
        alert('⛔ Access Denied! Admin only.');
        window.location.href = 'index.html';
    }
}

function showSection(section) {
    document.querySelectorAll('.admin-section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.sidebar-btn').forEach(b => b.classList.remove('active'));
    
    const sectionElement = document.getElementById(section + 'Section');
    if(sectionElement) {
        sectionElement.classList.add('active');
    }
    
    if(event && event.target) {
        event.target.classList.add('active');
    }
    
    if(section === 'analytics') {
        loadStats();
    }
}

function addPdfLink() {
    const link = document.getElementById('pdfLink').value.trim();
    if(link && link.startsWith('http')) {
        pdfLinks.push(link);
        updatePdfLinksList();
        document.getElementById('pdfLink').value = '';
    } else {
        alert('Please enter a valid URL');
    }
}

function updatePdfLinksList() {
    const list = document.getElementById('pdfLinksList');
    list.innerHTML = '<h4>📎 PDF Links:</h4>';
    pdfLinks.forEach((link, index) => {
        const item = document.createElement('div');
        item.style.marginBottom = '10px';
        item.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; background: white; padding: 10px; border-radius: 6px;">
                <a href="${link}" target="_blank" style="flex: 1; color: #667eea; text-decoration: none; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                    📄 ${link.substring(0, 50)}...
                </a>
                <button type="button" onclick="removePdfLink(${index})" style="padding: 5px 10px; background: #e74c3c; color: white; border: none; border-radius: 4px; cursor: pointer; margin-left: 10px;">✕ Remove</button>
            </div>
        `;
        list.appendChild(item);
    });
}

function removePdfLink(index) {
    pdfLinks.splice(index, 1);
    updatePdfLinksList();
}

async function handleCreateLesson(event) {
    event.preventDefault();
    
    const lessonData = {
        title: document.getElementById('lessonTitle').value,
        description: document.getElementById('lessonDescription').value,
        subject: document.getElementById('lessonSubject').value,
        youtubeLink: document.getElementById('youtubeLink').value,
        pdfLinks: pdfLinks,
        requiredSubscription: document.getElementById('requiredSubscription').value,
        duration: parseInt(document.getElementById('duration').value),
        difficulty: document.getElementById('difficulty').value
    };
    
    try {
        const response = await fetch(`${API_URL}/admin/lessons/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(lessonData)
        });
        
        const data = await response.json();
        
        if(data.success) {
            alert('✅ Lesson created successfully!');
            event.target.reset();
            pdfLinks = [];
            document.getElementById('pdfLinksList').innerHTML = '';
            loadAllLessons();
        } else {
            alert('❌ Error: ' + data.message);
        }
    } catch(error) {
        alert('❌ An error occurred while creating the lesson');
        console.error(error);
    }
}

async function loadAllLessons() {
    try {
        const response = await fetch(`${API_URL}/lessons/all`);
        const lessons = await response.json();
        displayLessonsInTable(lessons);
    } catch(error) {
        console.error('Error loading lessons:', error);
    }
}

function displayLessonsInTable(lessons) {
    const tbody = document.querySelector('#lessonsTable tbody');
    tbody.innerHTML = '';
    
    if(lessons.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: #999;">No lessons yet</td></tr>';
        return;
    }
    
    lessons.forEach(lesson => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td><strong>${lesson.title}</strong></td>
            <td>${lesson.subject}</td>
            <td>${lesson.duration} min</td>
            <td><span style="background: #667eea; color: white; padding: 4px 10px; border-radius: 4px;">${lesson.requiredSubscription}</span></td>
            <td>
                <button onclick="editLesson('${lesson.id}')">✏️ Edit</button>
                <button class="delete" onclick="deleteLesson('${lesson.id}')">🗑️ Delete</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function editLesson(lessonId) {
    alert('📝 Edit functionality coming soon!');
}

function deleteLesson(lessonId) {
    if(confirm('Are you sure you want to delete this lesson?')) {
        fetch(`${API_URL}/admin/lessons/${lessonId}/delete`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
        .then(response => response.json())
        .then(data => {
            if(data.success) {
                alert('✅ Lesson deleted successfully!');
                loadAllLessons();
            } else {
                alert('❌ Error: ' + data.message);
            }
        })
        .catch(error => {
            alert('❌ Error deleting lesson');
            console.error(error);
        });
    }
}

async function loadStats() {
    try {
        const response = await fetch(`${API_URL}/admin/stats`);
        const stats = await response.json();
        
        document.getElementById('totalUsers').textContent = stats.totalUsers || 0;
        document.getElementById('totalLessons').textContent = stats.totalLessons || 0;
        document.getElementById('premiumUsers').textContent = stats.premiumUsers || 0;
    } catch(error) {
        console.error('Error loading stats:', error);
    }
}

function logout() {
    if(confirm('Are you sure you want to logout?')) {
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        window.location.href = 'index.html';
    }
}