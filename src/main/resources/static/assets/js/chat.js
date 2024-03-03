let stompClient;
let issueId;
let currentUser;
document.addEventListener('DOMContentLoaded', () => {
    renderChatList();

    async function fetchCurrentUser() {
        return await fetch('/api/user/current-user')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch current user');
                }
                return response.json();
            })
            .then(user => {
                // Update UI with user data
                currentUser = user;
                return user;
            })
            .catch(error => {
                console.error('Error fetching current user:', error);
                return Promise.reject(0);
            });
    }

    function scrollToButton() {
        const kk = document.getElementById('chat-conversation').querySelector("#chat-conversation .simplebar-content-wrapper");
        kk.scrollTop = kk.scrollHeight;
    }

    function formatTimestamp(timestamp) {
        const date = new Date(timestamp); // Parse the timestamp string into a Date object
        // Extract individual date components
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        const seconds = date.getSeconds().toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month is zero-based
        const year = date.getFullYear().toString().slice(2); // Get last two digits of the year
        return `${hours}:${minutes}:${seconds} ${day}/${month}/${year}`;
    }

    async function fetchMessageList(issueId) {
        return await fetch(`/get-messages/${issueId}`)
            .then(response => response.json())
            .then(messageList => {
                return messageList;
            })
            .catch(error => {
                console.log('Error: ', error);
                return Promise.reject(0);
            });
    }

    function renderChatList() {
        fetchCurrentUser()
            .then(currentUser => {
                const userId = currentUser.id;
                fetch(`/get-issues/${userId}`)
                    .then(response => response.json())
                    .then(data => {
                        console.log('All issue list: ', data);
                        issueList = data;
                        let chatList = document.getElementById('issue-chat-list');
                        chatList.innerHTML = '';

                        // Generate new list items based on the data
                        issueList.forEach(function (issue) {
                            const outerDiv = document.createElement('div');
                            outerDiv.classList.add('d-flex', 'align-items-center', 'px-4', 'mb-3');
                            const innerDiv = document.createElement('div');
                            innerDiv.classList.add('flex-grow-1');
                            const btn = document.createElement("a");
                            btn.classList.add("issue-btn")
                            const heading = document.createElement('h4');
                            heading.classList.add('mb-0', 'fs-11', 'text-muted', 'text-uppercase');
                            heading.textContent = issue.title + " Group Chat";
                            btn.appendChild(heading);
                            innerDiv.appendChild(btn);
                            outerDiv.appendChild(innerDiv);
                            chatList.appendChild(outerDiv);

                            btn.addEventListener("click", function () {
                                issueGroupOnClick(issue)
                            });
                        });
                    })
                    .catch(error => {
                        console.log('Error: ', error);
                    });
            })
            .catch(err => {
                console.log(err);
            })
    }

    function displayGroupMembers(userList) {
        document.getElementById('group-members').innerHTML = '';
        userList.forEach(function (user) {
            const aElement = document.createElement('a');
            aElement.setAttribute('class', 'dropdown-item');
            aElement.innerHTML = '<i class="ri-user-fill align-bottom text-muted me-2"></i>' + user.name;
            document.getElementById('group-members').appendChild(aElement);
        });
    }

    function getGroupMember(issueId) {
        fetch(`/get-members/${issueId}`)
            .then(response => response.json())
            .then(userList => {
                console.log('Group Member list: ', userList);
                displayGroupMembers(userList);
            })
            .catch(error => {
                console.log('Error: ', error);
            });


    }

    function issueGroupOnClick(issue) {
        document.getElementById('issue-group-title').innerHTML = issue.title;
        document.getElementById('isActive').innerHTML = issue.active ? "Active" : "Inactive";
        renderMessage(issue.id);
        subscribeToIssue(issue.id);
        getGroupMember(issue.id);
        issueId = issue.id;
        console.log(issue);
    }

    function renderMessage(issueId) {
        fetchMessageList(issueId)
            .then(messages => {
                displayMessages(messages);
            })
            .catch(err =>
                console.log(err));
    }

    function commonAppendMessage(message) {
        const liElement = document.createElement('li');
        liElement.classList.add('chat-list');
        if (currentUser.id === message.senderId) {
            liElement.classList.add('right')
        } else {
            liElement.classList.add('left')
        }
        liElement.id = message.id;
        const conversationListDiv = document.createElement('div');
        conversationListDiv.classList.add('conversation-list');

        // user name
        const userNameWrap = document.createElement('div');
        userNameWrap.classList.add('conversation-name');

        const userName = document.createElement('small');
        userName.classList.add('text-muted', 'time');
        userName.textContent = message.user.name;

        userNameWrap.appendChild(userName);

        const userChatContentDiv = document.createElement('div');
        userChatContentDiv.classList.add('user-chat-content');

        const ctextWrapDiv = document.createElement('div');
        ctextWrapDiv.classList.add('ctext-wrap');

        const ctextWrapContentDiv = document.createElement('div');
        ctextWrapContentDiv.classList.add('ctext-wrap-content');

        const pElement = document.createElement('p');
        pElement.classList.add('mb-0', 'ctext-content');
        pElement.textContent = message.content;

        ctextWrapContentDiv.appendChild(pElement);
        ctextWrapDiv.appendChild(ctextWrapContentDiv);

        const conversationNameDiv = document.createElement('div');
        conversationNameDiv.classList.add('conversation-name');

        const smallElement = document.createElement('small');
        smallElement.classList.add('text-muted', 'time');
        smallElement.textContent = formatTimestamp(message.timeStamp);

        const spanElement = document.createElement('span');
        spanElement.classList.add('text-success', 'check-message-icon');

        const iElement = document.createElement('i');
        iElement.classList.add('bx', 'bx-check');

        spanElement.appendChild(iElement);
        conversationNameDiv.appendChild(smallElement);
        conversationNameDiv.appendChild(spanElement);
        userChatContentDiv.appendChild(userNameWrap);
        userChatContentDiv.appendChild(ctextWrapDiv);
        userChatContentDiv.appendChild(conversationNameDiv);
        conversationListDiv.appendChild(userChatContentDiv);
        liElement.appendChild(conversationListDiv);
        document.getElementById('user-conversation').appendChild(liElement);
    }


    function displayMessages(messages) {
        document.getElementById('user-conversation').innerHTML = '';
        messages.forEach(message => {
            commonAppendMessage(message)
        });
        scrollToButton();
    }

    function displayUpdateMessage(message) {
        commonAppendMessage(message);
        scrollToButton();
    }

    function subscribeToIssue(issueId) {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, () => {
            console.log('Connected to WebSocket');
            stompClient.subscribe(`/topic/messages/${issueId}`, function (data) {
                let message = JSON.parse(data.body).body; // Extract the message data from the payload
                console.log("msg:::", message.content);
                displayUpdateMessage(message);
            });
        });
    }

    //websocket connection
    document.getElementById('chat-input-form').addEventListener("submit", function (e) {
        e.preventDefault();
        sendMessage(document.getElementById('chat-input').value);
    })

    function sendMessage(content) {
        console.log(content)
        const senderId = currentUser.id;
        const message = {senderId, content};
        console.log(message);
        stompClient.send(`/app/chat/${issueId}`, {}, JSON.stringify(message));
        document.getElementById('chat-input').value = '';
    }
});


