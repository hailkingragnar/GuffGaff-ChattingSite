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

async function login(){
    console.log('Logging  in users...');
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    console.log(`The username is : ${username}+"The password is : ${password}`);
    try {
        const response = await fetch(`/api/v1/authenticate`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username: username, password: password})
        })
        console.log(`The response is ${await response.json()}`);
        console.log(`The response is ${response.status}`);
        // Step 2: Check if the response is OK
        if (!response.ok) {
            alert("Invalid Username or password");
            throw new Error('Login failed: Invalid credentials or server error');
        }

        // Step 3: Parse the response to JSON
        const data = await response.json();

        // Step 4: Save the token in localStorage
        localStorage.setItem('jwtToken', data.token);
        console.log(data.token);
        console.log('Login successful, token saved');
        window.location.href = '/guffgaff';
        connect();
    }catch (error) {
        if (error.name === 'AbortError') {
            console.error("Request timed out");
            alert("Request timed out. Please try again.");
        } else {
            // Log the error and show a generic error message
            console.error("Error logging in:", error);
            alert(error.message || "An error occurred while logging in.");
        }
    }
}



