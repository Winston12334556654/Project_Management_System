let stompClient;
document.addEventListener('DOMContentLoaded',()=>{

    const socket = new SockJS('/ws');

    stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
        console.log('Connected to WebSocket');
        stompClient.subscribe('/topic/messages', (message) => {
            showMessage(JSON.parse(message.body));
        });
    });

    function showMessage(message) {
        const messages = document.getElementById('messages');
        const li = document.createElement('li');

        const sender = message.sender || "Unknown";  // Check for undefined
        const content = message.content || "No content";  // Check for undefined

        console.log(sender);
        console.log(content);
        li.textContent = `${message.sender} : ${message.content}`;
        // const user = sessionStorage.getItem('user');
        // const currentUser = JSON.parse(user);
        if (sender === "Tin Htet") { // Replace "current_user" with the actual current user identifier
            li.classList.add("own-message");
        } else {
            li.classList.add("other-message");
        }
        messages.appendChild(li);
    }

});

function sendMessage() {

    const sender = document.getElementById('sender').value;
    const content = document.getElementById('content').value;
    const message = {sender, content};
    stompClient.send('/app/chat', {}, JSON.stringify(message));
}





