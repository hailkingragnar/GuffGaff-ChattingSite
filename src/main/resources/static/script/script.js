let stompClient;
let currentReceiver = '';
    let username = '';

function connect() {
    console.log('Connecting to WebSocket...');
    const socket = new SockJS(`/chat?username=${encodeURIComponent(username)}`);
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

function login() {
    console.log('Inside login...');

     username = document.getElementById('username').value;
    console.log("Attempting to log in with username:", username);

    // Send login request
    fetch(`/login?username=${username}`, { method: 'POST' })
        .then(response => response.text())
        .then(data => {
            console.log("Login response data: " + data);
            document.getElementById('loginSection').style.display = 'none';
            document.getElementById('chatSection').style.display = 'block';
            connect(); // Establish WebSocket connection
        })
        .catch(error => {
            console.error("Error during login request: ", error);
        });
}

function fetchUsers() {
    console.log('Fetching online users...');
    fetch('/users')
        .then(response => response.json())
        .then(users => {
            console.log("Received users: ", users);
            const userList = document.getElementById('users');
            userList.innerHTML = '';
            users.forEach(user => {
                const li = document.createElement('li');
                li.textContent = user;
                li.onclick = function () {
                    selectUser(user);
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
}

function sendMessage() {
    const message = document.getElementById('message').value;
    console.log("Sending message:", message, "to", currentReceiver);

    if (stompClient && message) {
        const messageData = {
            'sender': username,
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


