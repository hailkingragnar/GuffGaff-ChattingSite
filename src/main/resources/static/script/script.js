let stompClient;
let currentReceiver = '';
    let username = '';

/*window.onload = function() {
    // Get the session cookie automatically included in HTTP requests
    const sessionCookie = getSessionCookie();  // Custom function to retrieve session cookie (if needed)

    if (sessionCookie) {
        connect();  // Trigger WebSocket connection
    } else {
        console.log("User is not authenticated.");
    }
}*/
/* window.onload = function() {
    // Get the current URL path
    const currentPath = window.location.pathname;

    // Check if the path is '/home'
    if (currentPath === '/guffgaff') {
        connect();

    } else {
        console.log("Not on /home page, skipping...");
    }
}; */
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;

    if (currentPath === '/guffgaff') {
        if (typeof SockJS !== 'undefined' && typeof Stomp !== 'undefined') {
            connect();
        } else {
            console.log('SockJS or Stomp is not loaded yet.');
        }
    }
});


function connect() {
    console.log('Connecting to WebSocket...');
    const socket = new SockJS(`/chat`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected to WebSocket: ' + frame);

        // Subscribe to private messages
        stompClient.subscribe('/user/queue/messages', function(message) {
            console.log("Message received from server: ", message.body);  // Log the received message

            try {
                const parsedMessage = JSON.parse(message.body);
                console.log("Parsed message: ", parsedMessage);
                showMessage(parsedMessage);  // Call showMessage with parsed message
            } catch (error) {
                console.error("Error parsing message: ", error);
            }
        });

        // Fetch and display online users
        fetchUsers();
    }, function (error) {
        console.error("WebSocket connection error: ", error);  // Log WebSocket connection errors
    });
}


function fetchUsers() {
    console.log('Fetching online users...');
    fetch('/users')
        .then(response => response.json())
        .then(usernames => {
            console.log("Received users: ", usernames);
            const userList = document.getElementById('users');
            userList.innerHTML = '';
            usernames.forEach(username => {
                const li = document.createElement('li');
                li.textContent = username;
                li.onclick = function () {
                    selectUser(username);
                };
                userList.appendChild(li);
            });
        })
        .catch(error => {
            console.error("Error fetching users: ", error);
        });
}
function selectUser(user) {
    console.log("User selected: ", user);
    currentReceiver = user;
    document.getElementById('chatWith').textContent = user;
    document.getElementById('chatWindow').style.display = 'block';
    loadMessages();
}

function sendMessage() {
    const message = document.getElementById('message').value;
    console.log("Sending message:", message, "to", currentReceiver);

    if (stompClient && message) {
        const messageData = {
            'receiver': currentReceiver,
            'message': message
        };
        console.log("Message data to be sent:", JSON.stringify(messageData));

        stompClient.send("/app/chat.send", {}, JSON.stringify(messageData));
        messageData.sender = "you";
        showMessage(messageData);
        document.getElementById('message').value = '';
    } else {
        console.warn("Message or receiver is missing.");
    }
}

function showMessage(message) {
    console.log("Inside showMessage:", message);

    const chatBox = document.getElementById('chatBox');
    if (!chatBox) {
        console.error("ChatBox element not found!");
        return;
    }

    const alignmentClass = (message.sender === "you") ? "justify-end" : "justify-start";
    const styleClass = (message.sender === "you") ? "bg-blue-500 text-white" : "bg-gray-300 text-gray-900";

    const sanitizedMessage = message.message.replace(/</g, "&lt;").replace(/>/g, "&gt;");

    const messageHTML = `
        <div class="flex ${alignmentClass} space-x-2 my-2">
            <div class="${styleClass} p-3 rounded-lg shadow max-w-xs">
                ${sanitizedMessage}
            </div>
        </div>
    `;

    chatBox.innerHTML += messageHTML;
    chatBox.scrollTop = chatBox.scrollHeight;
}
function loadMessages(){
    document.getElementById('chatBox').innerHTML='';

    fetch(`/messages?sender=${encodeURIComponent(username)}&receiver=${encodeURIComponent(currentReceiver)}`)
        .then(response => response.json())
        .then(data => {
            // Sort messages by date (oldest first)
            data.sort((a, b) => new Date(a.dateAndTime) - new Date(b.dateAndTime));

            data.forEach((message) => {
                showMessage(message);
            });

        }).catch(error => {
            console.error("Error parsing message:", error);
    })
}

async function login(event) {
    event.preventDefault();
    console.log('Logging in user...');

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    console.log(`The username is: ${username}, The password is: ${password}`);

    try {
        const response = await fetch(`http://localhost:8080/api/v1/authenticate`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username, password})
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();  // Await the JSON response
        console.log('Response:', data);

        // Store token if it exists
        if (data.token) {

          window.location.href = "/guffgaff";

        }
    } catch (error) {
        console.error('Error authenticating:', error);
    }
}
document.getElementById('login-button').addEventListener('click', login);


    async function logout() {
        console.log('Logging out user...');

        try {
            // Call backend to logout and clear cookie
            const response = await fetch('http://localhost:8080/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                console.log('Logout successful!');
                // Redirect user to login page or any other page after logout
                window.location.href = '/login';
            } else {
                console.error('Logout failed');
            }
        } catch (error) {
            console.error('Error logging out:', error);
        }
}




