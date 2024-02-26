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

        //const sender = message.sender || "Unknown";  // Check for undefined
        const content = message.content || "No content";  // Check for undefined

        //console.log(sender);
        console.log(content);
        //${message.sender}
        li.textContent = `${message.content}`;
        messages.appendChild(li);
    }




});

function sendMessage() {
    const sender = "Mg Mg";//document.getElementById('sender').value;
    const content = document.getElementById('content').value;
    const message = {sender, content};
    stompClient.send('/app/chat', {}, JSON.stringify(message));
}





