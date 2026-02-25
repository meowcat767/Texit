let posts = [];
let currentView = 'index'; // 'index', 'detail', 'compose-post', 'compose-comment', 'auth'
let selectedIndex = 0;
let currentPost = null;
let currentUser = null;
let authHeader = null;
let users = [];

const listView = document.getElementById('list-view');
const detailView = document.getElementById('detail-view');
const composeView = document.getElementById('compose-view');
const authView = document.getElementById('auth-view');
const adminView = document.getElementById('admin-view');
const postList = document.getElementById('post-list');
const userList = document.getElementById('user-list');
const postHeader = document.getElementById('post-header');
const postBody = document.getElementById('post-body');
const commentList = document.getElementById('comment-list');
const statusBar = document.getElementById('status-bar');
const topBar = document.getElementById('top-bar');

const authUsernameInput = document.getElementById('auth-username');
const authPasswordInput = document.getElementById('auth-password');
const subjectInput = document.getElementById('subject-input');
const bodyInput = document.getElementById('body-input');
const commentBodyInput = document.getElementById('comment-body-input');
const postInputs = document.getElementById('post-inputs');
const commentInputs = document.getElementById('comment-inputs');
const composeTitle = document.getElementById('compose-title');

async function checkAuth() {
    try {
        const response = await fetch('/api/users/me', { headers: authHeader ? { 'Authorization': authHeader } : {} });
        if (response.ok) {
            currentUser = await response.json();
            showView('index');
            fetchPosts();
        } else {
            showView('auth');
        }
    } catch (error) {
        showView('auth');
    }
}

async function login() {
    const username = authUsernameInput.value.trim();
    const password = authPasswordInput.value.trim();
    if (!username || !password) {
        alert('Please enter both username and password');
        return;
    }
    const header = 'Basic ' + btoa(username + ':' + password);
    try {
        const response = await fetch('/api/users/me', { headers: { 'Authorization': header } });
        if (response.ok) {
            currentUser = await response.json();
            authHeader = header;
            authUsernameInput.value = '';
            authPasswordInput.value = '';
            showView('index');
            fetchPosts();
        } else {
            alert('Login failed: Invalid username or password');
        }
    } catch (error) {
        alert('Login error: ' + error.message);
    }
}

async function logout() {
    currentUser = null;
    authHeader = null;
    authUsernameInput.value = '';
    authPasswordInput.value = '';
    showView('auth');
}

async function register() {
    const username = authUsernameInput.value.trim();
    const password = authPasswordInput.value.trim();
    if (!username || !password) {
        alert('Please enter both username and password');
        return;
    }
    try {
        const response = await fetch('/api/users/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        if (response.ok) {
            alert('Registration successful. You can now login.');
        } else {
            const error = await response.text();
            alert('Registration failed: ' + error);
        }
    } catch (error) {
        alert('Registration error: ' + error.message);
    }
}

async function fetchPosts() {
    try {
        const response = await fetch('/api/posts', { headers: { 'Authorization': authHeader } });
        if (response.status === 401) {
            showView('auth');
            return;
        }
        if (!response.ok) {
            alert('Failed to fetch posts: ' + response.statusText);
            return;
        }
        posts = await response.json();
        renderIndex();
    } catch (error) {
        alert('Error fetching posts: ' + error.message);
    }
}

async function fetchUsers() {
    try {
        const response = await fetch('/api/users', { headers: { 'Authorization': authHeader } });
        if (response.ok) {
            users = await response.json();
            renderAdmin();
        } else if (response.status === 403) {
            alert('Access denied: Admins only.');
            showView('index');
        } else {
            alert('Failed to fetch users: ' + response.statusText);
        }
    } catch (error) {
        alert('Error fetching users: ' + error.message);
    }
}

function renderAdmin() {
    userList.innerHTML = '';
    users.forEach((user, index) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="col-num">${index + 1}</td>
            <td class="col-username">${user.username}</td>
            <td class="col-role">${user.role}</td>
        `;
        userList.appendChild(tr);
    });
}

function renderIndex() {
    postList.innerHTML = '';
    posts.forEach((post, index) => {
        const tr = document.createElement('tr');
        if (index === selectedIndex) tr.className = 'selected';
        tr.innerHTML = `
            <td class="col-num">${index + 1}</td>
            <td class="col-votes">${post.votes}</td>
            <td class="col-author">${post.author.username}</td>
            <td class="col-title">${post.title}</td>
        `;
        postList.appendChild(tr);
    });
    updateStatus();
}

function updateStatus() {
    if (currentView === 'index') {
        const count = posts.length;
        const current = count > 0 ? selectedIndex + 1 : 0;
        const userStr = currentUser ? ` [User: ${currentUser.username}${currentUser.role === 'ADMIN' ? '*' : ''}]` : '';
        statusBar.textContent = `-- Texit: Index [${current}/${count}] (${count > 0 ? Math.round((current/count)*100) : 0}%) --${userStr}`;
        const adminKey = currentUser && currentUser.role === 'ADMIN' ? ' a:admin' : '';
        topBar.textContent = `Texit: [Index] - (j:down k:up Enter:view p:post r:refresh q:logout${adminKey})`;
    } else if (currentView === 'detail') {
        statusBar.textContent = `-- Texit: Post View [${currentPost.title}] --`;
        topBar.textContent = 'Texit: [Post] - (c:comment v:upvote b:downvote q:back)';
    } else if (currentView.startsWith('compose')) {
        statusBar.textContent = `-- Texit: Compose --`;
        topBar.textContent = 'Texit: [Compose] - (Ctrl+S: Submit, Esc: Cancel)';
    } else if (currentView === 'auth') {
        statusBar.textContent = '-- Texit: Login/Register Required --';
        topBar.textContent = 'Texit: [Login] - (l:login r:register)';
    } else if (currentView === 'admin') {
        statusBar.textContent = '-- Texit: Admin Dashboard --';
        topBar.textContent = 'Texit: [Admin] - (q:back)';
    }
}

async function viewPost(id) {
    try {
        const response = await fetch(`/api/posts/${id}`, { headers: { 'Authorization': authHeader } });
        if (!response.ok) {
            alert('Failed to fetch post details');
            showView('index');
            return;
        }
        currentPost = await response.json();
        
        postHeader.innerHTML = `
            Subject: ${currentPost.title}<br>
            From:    ${currentPost.author.username}<br>
            Date:    ${new Date(currentPost.createdAt).toLocaleString()}<br>
            Votes:   ${currentPost.votes}
        `;
        postBody.textContent = currentPost.body;
        
        const commentResponse = await fetch(`/api/comments/post/${id}`, { headers: { 'Authorization': authHeader } });
        const comments = await commentResponse.json();
        
        commentList.innerHTML = '';
        comments.forEach(comment => {
            const div = document.createElement('div');
            div.className = 'comment';
            div.innerHTML = `
                <div class="comment-meta">From: ${comment.author.username} at ${new Date(comment.createdAt).toLocaleString()}</div>
                <div class="comment-body">${comment.body}</div>
            `;
            commentList.appendChild(div);
        });
        
        showView('detail');
    } catch (error) {
        alert('Error loading post: ' + error.message);
    }
}

function showView(view) {
    currentView = view;
    listView.style.display = view === 'index' ? 'block' : 'none';
    detailView.style.display = view === 'detail' ? 'block' : 'none';
    composeView.style.display = view.startsWith('compose') ? 'block' : 'none';
    authView.style.display = view === 'auth' ? 'block' : 'none';
    adminView.style.display = view === 'admin' ? 'block' : 'none';
    updateStatus();
}

async function submitPost() {
    const title = subjectInput.value.trim();
    const body = bodyInput.value.trim();
    if (!title || !body) {
        alert('Post title and body cannot be empty.')
        return;
    }
    
    try {
        const response = await fetch('/api/posts', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': authHeader },
            body: JSON.stringify({ title, body })
        });
        if (response.ok) {
            subjectInput.value = '';
            bodyInput.value = '';
            showView('index');
            fetchPosts();
        } else {
            alert('Failed to submit post: ' + response.statusText);
        }
    } catch (error) {
        alert('Error submitting post: ' + error.message);
    }
}

async function submitComment() {
    const body = commentBodyInput.value.trim();
    if (!body){
        alert('Comment cannot be empty.')
        return;
    }
    
    try {
        const response = await fetch(`/api/comments/post/${currentPost.id}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': authHeader },
            body: JSON.stringify({ body })
        });
        if (response.ok) {
            commentBodyInput.value = '';
            viewPost(currentPost.id);
        } else {
            alert('Failed to submit comment: ' + response.statusText);
        }
    } catch (error) {
        alert('Error submitting comment: ' + error.message);
    }
}

async function vote(value) {
    try {
        const response = await fetch(`/api/votes/post/${currentPost.id}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': authHeader },
            body: JSON.stringify({ value })
        });
        if (response.ok) {
            viewPost(currentPost.id);
        } else {
            alert('Failed to vote: ' + response.statusText);
        }
    } catch (error) {
        alert('Error voting: ' + error.message);
    }
}

window.addEventListener('keydown', (e) => {
    if (document.activeElement.tagName === 'INPUT' || document.activeElement.tagName === 'TEXTAREA') {
        if (e.key === 'Escape') {
             document.activeElement.blur();
             return;
        }
        if (!(e.ctrlKey && e.key === 's')) return;
    }

    if (currentView === 'index') {
        if (e.key === 'j') {
            if (selectedIndex < posts.length - 1) {
                selectedIndex++;
                renderIndex();
            }
        } else if (e.key === 'k') {
            if (selectedIndex > 0) {
                selectedIndex--;
                renderIndex();
            }
        } else if (e.key === 'Enter') {
            if (posts[selectedIndex]) viewPost(posts[selectedIndex].id);
        } else if (e.key === 'p') {
            composeTitle.textContent = 'Compose New Post';
            postInputs.style.display = 'block';
            commentInputs.style.display = 'none';
            showView('compose-post');
        } else if (e.key === 'r') {
            fetchPosts();
        } else if (e.key === 'q') {
             logout();
        } else if (e.key === 'a' && currentUser && currentUser.role === 'ADMIN') {
             showView('admin');
             fetchUsers();
        }
    } else if (currentView === 'detail') {
        if (e.key === 'q') {
            showView('index');
        } else if (e.key === 'c') {
            composeTitle.textContent = 'Compose Comment';
            postInputs.style.display = 'none';
            commentInputs.style.display = 'block';
            showView('compose-comment');
        } else if (e.key === 'v') {
            vote(1);
        } else if (e.key === 'b') {
            vote(-1);
        }
    } else if (currentView.startsWith('compose')) {
        if (e.key === 'Escape') {
            showView(currentView === 'compose-post' ? 'index' : 'detail');
        } else if (e.key === 's' && e.ctrlKey) {
            e.preventDefault();
            if (currentView === 'compose-post') {
                submitPost();
            } else {
                submitComment();
            }
        }
    } else if (currentView === 'auth') {
        if (e.key === 'l') {
            login();
        } else if (e.key === 'r') {
            register();
        }
    } else if (currentView === 'admin') {
        if (e.key === 'q') {
            showView('index');
        }
    }
});

checkAuth();
