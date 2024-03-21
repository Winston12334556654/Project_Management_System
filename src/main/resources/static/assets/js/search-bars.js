function redirectToIssueListToDo() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'TODO');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "/member-issuelist";
}

function redirectToIssueListToDoInProgress() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'INPROGRESS');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "/member-issuelist";
}

function redirectToIssueListCompleted() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'COMPLETED');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "/member-issuelist";
}

function redirectToIssueListOverDue() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'OVERDUE');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "/member-issuelist";
}

//<!------------------------------------------------------------------------------------------------------------------------------------------------------->
function teamLeaderRedirectToIssueListToDo() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'TODO');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "/teamleader-issuelist";
}

function teamLeaderRedirectToIssueListToDoInProgress() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'INPROGRESS');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "/teamleader-issuelist";
}

function teamLeaderRedirectToIssueListCompleted() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'COMPLETED');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "/teamleader-issuelist";
}

function teamLeaderRedirectToIssueListPending() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'PENDING');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "/teamleader-issuelist";
}
function teamLeaderRedirectToIssueListOverDue() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'OVERDUE');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "/teamleader-issuelist";
}
//<!------------------------------------------------------------------------------------------------------------------------------------------------------->
function projectManagerRedirectToIssueListToDo() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'TODO');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "project/my-projects";
}

function projectManagerRedirectToIssueListToDoInProgress() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'INPROGRESS');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "project/my-projects";
}

function projectManagerRedirectToIssueListCompleted() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'COMPLETED');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "project/my-projects";
}

function projectManagerRedirectToIssueListPending() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'PENDING');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "project/my-projects";
}
function projectManagerRedirectToIssueListOverDue() {
    // Set a flag in sessionStorage indicating that the "See More" button was clicked
    sessionStorage.setItem('seeMoreClicked', 'true');

    localStorage.setItem('selectedStatus', 'OVERDUE');

    console.log('selectedStatus:', localStorage.getItem('selectedStatus'));
    window.location.href = "project/my-projects";
}