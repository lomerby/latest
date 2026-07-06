const API_URL = 'http://localhost:8080/api';

let currentUser = null;
let allLessons = [];
let currentLesson = null;

document.addEventListener('DOMContentLoaded', () => {
    loadUserData();
    loadLessons();
});

function loadUserData() {
    const userData = localStorage.getItem('user');
    if(!userData) {
        window.location.href = 'index.html';
        return;
    }
    
    currentUser = JSON.parse(userData);
    document.getElementById('userEmail').textContent = currentUser.email;
    document.getElementById('userSubscription').textContent = currentUser.subscriptionLevel || 'FREE';
    document.getElementById('currentPlan').textContent = currentUser.subscriptionLevel || 'FREE';
    
    if(currentUser.subscriptionEndDate) {
        const endDate = new Date(currentUser.subscriptionEndDate);
        document.getElementById('expiryInfo').textContent = `Expires on: ${endDate.toLocaleDateString()}`;
    }
}

async function loadLessons() {
    try {
        const response = await fetch(`${API_URL}/lessons/all`);
        const lessons = await response.json();
        allLessons = lessons;
        displayLessons(lessons);
    } catch(error) {
        console.error('Error loading lessons:', error);
        document.getElementById('lessonsList').innerHTML = '<p style="color: red;">Error loading lessons. Please refresh the page.</p>';
    }
}

function displayLessons(lessons) {
    const container = document.getElementById('lessonsList');
    
    if(lessons.length === 0) {
        container.innerHTML = '<p style="grid-column: 1 / -1; text-align: center; color: #999;">No lessons available yet.</p>';
        return;
    }
    
    container.innerHTML = '';
    
    lessons.forEach(lesson => {
        const card = document.createElement('div');
        card.className = 'lesson-card';
        card.innerHTML = `
            <h3>${lesson.title}</h3>
            <p>${lesson.description.substring(0, 100)}...</p>
            <span class="level">📚 ${lesson.requiredSubscription}</span>
            <button onclick="openLessonModal('${lesson.id}')">View Lesson →</button>
        `;
        container.appendChild(card);
    });
}

function openLessonModal(lessonId) {
    const lesson = allLessons.find(l => l.id === lessonId);
    
    if(!canAccessLesson(lesson)) {
        alert('🔒 You need to upgrade your subscription to access this lesson!');
        showUpgradeModal();
        return;
    }
    
    currentLesson = lesson;
    
    document.getElementById('lessonTitle').textContent = lesson.title;
    document.getElementById('lessonDescription').textContent = lesson.description;
    
    // YouTube embed
    const videoContainer = document.getElementById('lessonVideo');
    const youtubeId = extractYoutubeId(lesson.youtubeLink);
    if(youtubeId) {
        videoContainer.innerHTML = `<iframe width="100%" height="400" src="https://www.youtube.com/embed/${youtubeId}" frameborder="0" allowfullscreen></iframe>`;
    } else {
        videoContainer.innerHTML = `<p style="color: red;">Invalid YouTube link</p>`;
    }
    
    // PDF links
    const pdfContainer = document.getElementById('pdfLinks');
    pdfContainer.innerHTML = '';
    if(lesson.pdfLinks && lesson.pdfLinks.length > 0) {
        lesson.pdfLinks.forEach((pdf, index) => {
            const link = document.createElement('a');
            link.href = pdf;
            link.textContent = `📄 Download Resource ${index + 1}`;
            link.target = '_blank';
            link.style.display = 'block';
            link.style.marginBottom = '10px';
            link.style.color = '#667eea';
            link.style.textDecoration = 'none';
            link.style.fontWeight = 'bold';
            link.onmouseover = () => link.textDecoration = 'underline';
            link.onmouseout = () => link.textDecoration = 'none';
            pdfContainer.appendChild(link);
        });
    } else {
        pdfContainer.innerHTML = '<p style="color: #999;">No additional resources available</p>';
    }
    
    document.getElementById('progressFill').style.width = '0%';
    document.getElementById('progressText').textContent = '0%';
    
    document.getElementById('lessonModal').classList.add('show');
}

function closeLessonModal() {
    document.getElementById('lessonModal').classList.remove('show');
    currentLesson = null;
}

function canAccessLesson(lesson) {
    const subscriptionLevels = { FREE: 0, BASIC: 1, PREMIUM: 2 };
    const userLevel = subscriptionLevels[currentUser.subscriptionLevel] || 0;
    const lessonLevel = subscriptionLevels[lesson.requiredSubscription] || 0;
    
    return userLevel >= lessonLevel;
}

function extractYoutubeId(url) {
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    const match = url.match(regExp);
    return match && match[2].length === 11 ? match[2] : '';
}

function filterLessons() {
    const search = document.getElementById('searchInput').value.toLowerCase();
    const subject = document.getElementById('subjectFilter').value;
    
    const filtered = allLessons.filter(lesson => {
        const matchesSearch = lesson.title.toLowerCase().includes(search) ||
                             lesson.description.toLowerCase().includes(search);
        const matchesSubject = !subject || lesson.subject === subject;
        
        return matchesSearch && matchesSubject;
    });
    
    displayLessons(filtered);
}

function showUpgradeModal() {
    document.getElementById('upgradeModal').classList.add('show');
}

function closeUpgradeModal() {
    document.getElementById('upgradeModal').classList.remove('show');
}

function selectPlan(plan) {
    if(plan === currentUser.subscriptionLevel) {
        alert('✅ You are already on this plan');
        return;
    }
    
    alert(`🎉 Thank you for upgrading to ${plan} plan!\n\nIn a production environment, you would be redirected to a payment gateway like Paystack or Stripe.`);
    
    currentUser.subscriptionLevel = plan;
    localStorage.setItem('user', JSON.stringify(currentUser));
    
    document.getElementById('userSubscription').textContent = plan;
    document.getElementById('currentPlan').textContent = plan;
    
    closeUpgradeModal();
    loadLessons();
}

function markAsComplete() {
    if(!currentLesson) return;
    
    document.getElementById('progressFill').style.width = '100%';
    document.getElementById('progressText').textContent = '100%';
    
    alert('✅ Lesson marked as complete! Great job! 🎉');
}

// Close modal when clicking outside
window.onclick = function(event) {
    const lessonModal = document.getElementById('lessonModal');
    const upgradeModal = document.getElementById('upgradeModal');
    
    if (event.target === lessonModal) {
        lessonModal.classList.remove('show');
    }
    if (event.target === upgradeModal) {
        upgradeModal.classList.remove('show');
    }
}