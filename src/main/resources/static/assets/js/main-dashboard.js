 document.addEventListener('DOMContentLoaded', function() {
    // Call userGetter to fetch and display a user list for the default department
    sameDepartmentUsers();
    myTasks();
    projectsOfCurrentUser();
    getProjectCountsByStatus();
    getIssueCountsByStatus();
})

//Number Active Projects
fetch('/projects/count/active')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Update project count on the frontend
        document.getElementById('projectCount').textContent = data;
        document.getElementById('active-projects').textContent = data;
    })
    .catch(error => console.error('Error fetching project count:', error));

//All Projects
fetch('/project/count/all')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Update project count on the frontend
        document.getElementById('all-projects').textContent = data;
        document.getElementById('total-projects').textContent = data;
    })
    .catch(error => console.error('Error fetching project count:', error));

//Inactive projects
fetch('/projects/count/inactive')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Update project count on the frontend
        document.getElementById('inactive-projects').textContent = data;
    })
    .catch(error => console.error('Error fetching project count:', error));

<!--|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\-->
//All Issues
fetch('/issues/count/all')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Update project count on the frontend
        document.getElementById('all-issues').textContent = data;
        document.getElementById('total-issues').textContent = data;
    })
    .catch(error => console.error('Error fetching project count:', error));

//Active Issues
fetch('/issues/count/active')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Update project count on the frontend
        document.getElementById('issueCount').textContent = data;
        document.getElementById('active-issues').textContent = data;
    })
    .catch(error => console.error('Error fetching project count:', error));

//Inactive Issues
fetch('/issues/count/inactive')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Update project count on the frontend
        document.getElementById('inactive-issues').textContent = data;
    })
    .catch(error => console.error('Error fetching project count:', error));

<!--||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||-->
//All AssignIssues
fetch('/assignIssues/count/active')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Update project count on the frontend
        document.getElementById('assignIssueCount').textContent = data;
        //document.getElementById('active-projects').textContent = data;
    })
    .catch(error => console.error('Error fetching project count:', error));

// <!--||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||-->
function populateMemberWithDepartment(members){
    const tableBody = document.getElementById('project-table-body');
    tableBody.innerHTML = ''; // Clear existing table rows

    members.forEach(member => {
        const row = document.createElement('tr');

        const profileCell = document.createElement('td');
        // Assuming you have a URL to the profile picture
        profileCell.innerHTML = `<img src="/static/img/${member.profilePictureFileName}" alt="Profile Picture" class="rounded-circle avatar-sm"/>`;

        const nameCell = document.createElement('td');
        nameCell.textContent = member.name;

        const emailCell = document.createElement('td');
        emailCell.textContent = member.email;

        const roleCell = document.createElement('td');
        roleCell.textContent = member.role;

        row.appendChild(profileCell);
        row.appendChild(nameCell);
        row.appendChild(emailCell);
        row.appendChild(roleCell);

        tableBody.appendChild(row);
    });
}


function getDepartmentOfCurrentUser(id) {
    const departmentId = id;
    const url = `/members-selector/${departmentId}`;
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(r => r.json())
        .then(members => {
            console.log(members);
            populateMemberWithDepartment(members);
        });

}

//Members from his department
function sameDepartmentUsers(){
    const url = '/find-same-department-with-currentUser';
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(r => r.json())
        .then(user => {
            console.log(user.department.id);
            getDepartmentOfCurrentUser(user.department.id);
        });
}
<!--|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\\\\\|||||||||||||\||||-->


function populateSubIssuesWithUserId(subIssues) {
    const tableBody = document.getElementById('my-issue-table');
    tableBody.innerHTML = ''; // Clear existing table rows

    subIssues.forEach(subIssue => {
        const row = document.createElement('tr');

        const nameCell = document.createElement('td');
        nameCell.textContent = subIssue.name;

        const deadlineCell = document.createElement('td');
        deadlineCell.textContent = subIssue.planDueDate;

        const statusCell = document.createElement('td');
        statusCell.textContent = subIssue.status;

        const assigneeCell = document.createElement('td');
        const assigneeImg = document.createElement('img');
        assigneeImg.classList.add('rounded-circle', 'avatar-xxs', 'shadow');
        assigneeImg.alt = 'User Profile Picture';
        assigneeCell.appendChild(assigneeImg);

        const assignIssueId = subIssue.id;

        // Fetch the assignee's profile picture
        fetch(`/getUserPhotoById/${assignIssueId}`, {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Accept': 'application/json',
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.text(); // Assuming the response is a URL string
                } else {
                    throw new Error('Failed to fetch profile picture');
                }
            })
            .then(profilePictureUrl => {
                const profilePicture = profilePictureUrl;
                // Set the profile picture URL
                assigneeImg.src = `/static/img/${profilePictureUrl}`;
            })
            .catch(error => {
                console.error('Error fetching profile picture:', error);
            });

        row.appendChild(nameCell);
        row.appendChild(deadlineCell);
        row.appendChild(statusCell);
        row.appendChild(assigneeCell);

        tableBody.appendChild(row);
    });
}


function getSubIssuesByUserId(id) {
    const userId = id;
    const url = `/get-subIssues-by-userId/${userId}`;
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(r => r.json())
        .then(subIssues => {
            console.log(subIssues);
            populateSubIssuesWithUserId(subIssues);
        });
}
function myTasks(){
    const url = '/find-same-department-with-currentUser';
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(r => r.json())
        .then(user => {
            console.log(user.id);
            getSubIssuesByUserId(user.id);
        });
}

<!--|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||-->

function populateProjectByUserIds(data) {
    const projectsTable = document.getElementById('my-projects');
    projectsTable.innerHTML = ''; // Clear existing projects

    if (Array.isArray(data)) {
        data.forEach(project => {
            const row = document.createElement('tr');

            const nameCell = document.createElement('td');
            nameCell.textContent = project.title;

            const statusCell = document.createElement('td');
            statusCell.textContent = project.status;

            row.appendChild(nameCell);
            row.appendChild(statusCell);

            projectsTable.appendChild(row);
        });
    } else {
        console.error('Error: Data is not an array');
    }
}

function findProjectsWithUser(id) {
    const url = `/getProjectsByUserId/${id}`;
    return fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch project IDs');
            }
        })
        .catch(error => {
            console.error('Error fetching project IDs:', error);
        });
}

function projectsOfCurrentUser(){
    const url = '/find-same-department-with-currentUser';
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(r => r.json())
        .then(user => {
            console.log(user.id);
            return findProjectsWithUser(user.id);
        })
        .then(data => {
            populateProjectByUserIds(data);
        })
        .catch(error => {
            console.error('Error fetching user:', error);
        });
}
<!--||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||-->

function getProjectCountsByStatus() {
    const url = '/projects/count';
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch project counts by status');
            }
        })
        .then(data => {
            // Handle the response data
            const todoCount = data?.todo || 0;
            const inProgressCount = data?.inProgress ;
            const pendingCount = data?.pending ;
            const overdueCount = data?.overdue ;
            const completedCount = data?.completed ;

            // Handle the counts as needed
            console.log('TODO count:', todoCount);
            console.log('In Progress count:', inProgressCount);
            console.log('Pending count:', pendingCount);
            console.log('Overdue count:', overdueCount);
            console.log('Completed count:', completedCount);

            // Update the content of the spans with specific IDs
            document.getElementById('completedCount-projects').innerText = data.completed === 0 ? "No Projects" : `${data.completed} Projects`;
            document.getElementById('inProgressCount-projects').innerText = data.inProgress === 0 ? "No Projects" : `${data.inProgress} Projects`;
            document.getElementById('todoCount-projects').innerText = data.todo === 0 ? "No Projects" : `${data.todo} Projects`;
            document.getElementById('overdueCount-projects').innerText = data.overdue === 0 ? "No Projects" : `${data.overdue} Projects`;
            document.getElementById('pendingCount-projects').innerText = data.pending === 0 ? "No Projects" : `${data.pending} Projects`;
        })
        .catch(error => {
            console.error('Error fetching project counts by status:', error);
        });
}


function getIssueCountsByStatus() {
    const url = '/issues/count';
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch project counts by status');
            }
        })
        .then(data => {
            // Handle the response data
            const todoCount = data?.todo || 0;
            const inProgressCount = data?.inProgress ;
            const pendingCount = data?.pending ;
            const overdueCount = data?.overdue ;
            const completedCount = data?.completed ;

            // Handle the counts as needed
            console.log('TODO count:', todoCount);
            console.log('In Progress count:', inProgressCount);
            console.log('Pending count:', pendingCount);
            console.log('Overdue count:', overdueCount);
            console.log('Completed count:', completedCount);

            // Update the content of the spans with specific IDs
            document.getElementById('completedCount-issues').innerText = data.completed === 0 ? "No Issues" : `${data.completed} Issues`;
            document.getElementById('inProgressCount-issues').innerText = data.inProgress === 0 ? "No Issues" : `${data.inProgress} Issues`;
            document.getElementById('todoCount-issues').innerText = data.todo === 0 ? "No Issues" : `${data.todo} Issues`;
            document.getElementById('overdueCount-issues').innerText = data.overdue === 0 ? "No Issues" : `${data.overdue} Issues`;
            document.getElementById('pendingCount-issues').innerText = data.pending === 0 ? "No Issues" : `${data.pending} Issues`;
        })
        .catch(error => {
            console.error('Error fetching project counts by status:', error);
        });
}





























