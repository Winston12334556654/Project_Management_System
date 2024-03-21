let stompNotiClient = null;
let notificationNumber = 0;
let notificationMessages = [];
let currentUser;
const notiNumberSpan = document.getElementById('notification-number');
const notiPopNumberSpan = document.getElementById('notifications-pop-number');
const notiListDiv = document.getElementById('notification-list');

document.addEventListener("DOMContentLoaded", function() {
    console.log("The document has finished loading.");
    notificationNumber =fetchNotificationNumberFromLocalStorage();
    notiNumberSpan.innerText= fetchNotificationNumberFromLocalStorage().toString();
    notiPopNumberSpan.innerText=fetchNotificationNumberFromLocalStorage().toString() + ' New';
    notificationMessages = fetchNotificationMessages();

    notificationMessages.forEach(notification => {
        appendNotiToNotiList(notification);
    });
    unReadNotiCount();
    fetchCurrentUser();
    webSocketConnect();
});
function  notiItemOnClick(notification){
    console.log(notification);
    updateReadNotificationMessage(notification);
    document.getElementById(notification.id).classList.add("bg-light","text-primary");
    unReadNotiCount();
    window.location.href = notification.redirectURL;
}
function appendNotiToNotiList(notification){
    // Create the HTML content as a string
    const htmlContent = `
  <div class="text-reset notification-item d-block dropdown-item  ${notification.isRead?"":"bg-light"}" id=${notification.id}>
    <div class="d-flex">
      <i class="bx bxs-bell fs-24 text-primary me-3"></i>
      <div class="flex-grow-1">
        <a>
          <h6 class="mt-0 mb-1 fs-13 fw-semibold ${notification.isRead?"":"text-primary font-weight-bold"}">${notification.title}</h6>
        </a>
        <div class="fs-13 text-muted">
          <p class="mb-1 ${notification.isRead?"":"text-primary font-weight-bold"}">${notification.content}</p>
        </div>
      </div>    
    </div>
  </div>`;

    const newDiv = document.createElement("div");
    newDiv.innerHTML = htmlContent;
    newDiv.addEventListener("click",()=>{
        notiItemOnClick(notification);
    })
    notiListDiv.prepend(newDiv);
}

function setNotificationMessages(notification){
    if(localStorage.getItem('notifications')!=null){
        notificationMessages = [...JSON.parse(localStorage.getItem('notifications'))];
    }
    notification["isRead"] = false;
    notificationMessages.push(notification);
    localStorage.setItem('notifications',JSON.stringify( notificationMessages));
}

function  updateReadNotificationMessage(notification){
    if(localStorage.getItem('notifications')!=null){
        notificationMessages = [...JSON.parse(localStorage.getItem('notifications'))];
    }
    notification["isRead"] = true;
    notificationMessages = [... notificationMessages.filter(n=> n.id!==notification.id)];
    notificationMessages.push(notification);
    localStorage.setItem('notifications',JSON.stringify( notificationMessages));
}

function fetchNotificationMessages(){
   return  localStorage.getItem('notifications')? JSON.parse(localStorage.getItem('notifications')):[];
}


function increaseNotificationNumberToLocalStorage(){
    if(localStorage.getItem('notification-number')!==null){
        notificationNumber = parseInt(localStorage.getItem('notification-number'));
    }
    notificationNumber++;
    localStorage.setItem('notification-number', notificationNumber);
}

function decreaseNotificationNumberToLocalStorage(){
    if(localStorage.getItem('notification-number')!==null){
        notificationNumber = parseInt(localStorage.getItem('notification-number'));
    }
    notificationNumber--;
    localStorage.setItem('notification-number', notificationNumber);
}

function  unReadNotiCount(){
    notificationNumber=0;
    notificationMessages.forEach(n=>{
        if(!n.isRead){
            notificationNumber++;
        }
    });
    localStorage.setItem('notification-number', notificationNumber);
}

function fetchNotificationNumberFromLocalStorage(){
    return  parseInt(localStorage.getItem('notification-number'))?parseInt(localStorage.getItem('notification-number')):0;
}

function webSocketConnect() {
    const socket = new SockJS('/ws');
    stompNotiClient = Stomp.over(socket);
stompNotiClient.connect({}, function (frame) {
    console.log('WebSocket for noti Connected: ' + frame);
    // Subscribe to receive notifications
    stompNotiClient.subscribe('/user/specific', function (notification) {
        // Handle received notification
        console.log('Received notification:', JSON.parse(notification.body));
        increaseNotificationNumberToLocalStorage();
        setNotificationMessages(JSON.parse(notification.body));
        appendNotiToNotiList(JSON.parse(notification.body))
        notiNumberSpan.innerText= fetchNotificationNumberFromLocalStorage().toString();
        notiPopNumberSpan.innerText=fetchNotificationNumberFromLocalStorage().toString() + ' New';
        playNotificationSound();
    });
});
}
function disconnect() {
    if (stompNotiClient !== null) {
        stompNotiClient.disconnect();
    }
    console.log("Disconnected");
}

function sendNotification(notification) {
    stompNotiClient.send("/app/sendNotification", {}, JSON.stringify(notification));
}
function  fetchCurrentUser() {
    fetch('/api/user/current-user')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch current user');
            }
            return response.json();
        })
        .then(user => {
            // Update UI with user data
            currentUser = user;
        })
        .catch(error => {
            console.error('Error fetching current user:', error);
            return Promise.reject(0);
        });
}

function playNotificationSound() {
    // Play the notification sound
    const audioElement = document.getElementById('notification-sound');
    audioElement.play();
}

function playSendSound() {
    // Play the notification sound
    const audioElement = document.getElementById('send-sound');
    audioElement.play();
}



