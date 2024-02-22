<!--Category-->
var categories = ["Designing", "Development"];

// Populate categories dropdown
var categoriesDropdown = document.getElementById("project-category-input");
categories.forEach(function (category) {
    const option = document.createElement("option");
    option.value = category;
    option.text = category;
    categoriesDropdown.add(option);
});

// Create a new script element
var script = document.createElement('script');

// Set the src attribute to the jQuery CDN URL
script.src = 'https://code.jquery.com/jquery-3.6.4.min.js';

// Append the script element to the document body
document.body.appendChild(script);

<!--Project creation-->
<!--Getting Users-->
document.addEventListener('DOMContentLoaded', function() {
    // Call userGetter to fetch and display user list for the default department
    userGetter();
});

function userGetter() {
    const departmentId = document.getElementById('project-department-input').value;
    const url = '/member-selector';
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            // Filter users based on the selected department ID
            const filteredUsers = data.filter(user => user.department.id === parseInt(departmentId));
            // Display the filtered users in the member list
            populateMemberList(filteredUsers);
        })
        .catch(error => console.error('Error fetching user data:', error));
}

function populateMemberList(users) {
    const memberListContainer = document.getElementById('team-member-list');
    // Clear existing member list content
    memberListContainer.innerHTML = '';

    // Loop through the filtered users and create HTML elements for each user
    users.forEach(user => {
        console.log(user.id);
        console.log(user.name);

        const listItem = document.createElement('li');
        listItem.innerHTML = `
            <div class="form-check d-flex align-items-center justify-content-between">
                <div>
                    <input class="form-check-input  me-3" type="checkbox" id="user-${user.id}" name="users">
                    <label class="form-check-label d-flex align-items-center" for="user-${user.id}">
                        <span class="flex-shrink-0">
                            <img src="assets/images/${user.photo}" alt="" class="avatar-xxs rounded-circle" />
                        </span>
                        <span class="flex-grow-1 ms-2">${user.name}</span>
                        <span class="flex-grow-1 ms-2">${user.position}</span>
                        <div class="flex-shrink-0 ms-4 additional-content">
                            <ul class="list-inline tasks-list-menu mb-0">
                                <li class="list-inline-item">
                                    <a href="issue_member_details.html"><button class="btn btn-sm btn-light" id="view-btn">View</button></a>
                                </li>
                            </ul>
                        </div>
                    </label>
                </div>
<!--                <div class="hidden-input" id="hiddenInputContainer">-->
<!--                    <input type="text" class="form-control form-control-sm float-end" id="placeholderInput" placeholder="Placeholder">-->
<!--                </div>-->
            </div>
        `;
        memberListContainer.appendChild(listItem);
    });
}


<!--Getting Department-->
function departmentGetter() {
    const url = '/departments-selector';
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            const selectElement = document.getElementById('project-department-input');
            // Clear existing options
            selectElement.innerHTML = '';
            // Create and append options for each department
            data.forEach((department, index) => {
                const option = document.createElement('option');
                option.value = department.id;
                option.textContent = department.name;
                selectElement.appendChild(option);
                if (index === 0) {
                    option.selected = true;
                }
            });
            userGetter();
        })
        .catch(error => console.error("Error:", error));
}


function stripHtmlTags(html) {
    const doc = new DOMParser().parseFromString(html, 'text/html');
    return doc.body.textContent || "";
}
function selectMembers() {
    const url = '/members-selector';
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            const selectElement = document.getElementById('project-crab-input');
            // Clear existing options
            selectElement.innerHTML = '';
            // Create and append options for each department
            data.forEach(department => {
                const option = document.createElement('option');
                option.value = department.id;
                option.textContent = department.name;
                selectElement.appendChild(option);
            });
        })
        .catch(error => console.error("Error:", error));
}


function stripHtmlTags(html) {
    const doc = new DOMParser().parseFromString(html, 'text/html');
    return doc.body.textContent || "";
}

 function showData() {

    document.getElementById("formAuthor").addEventListener("submit", async function (event) {
        event.preventDefault();
        console.log("submitting form...");

        // Create an array to store the selected user IDs
        let selectedUserIds = [];
        //I get all the data from that way
        const checkboxes = document.querySelectorAll('.form-check-input');
        checkboxes.forEach(checkbox => {
            
            if (checkbox.checked) {
                console.log(checkbox.checked);
                console.log(checkbox.value);
                selectedUserIds.push(checkbox.value);
            }
        })

        let editorData = window.ckeditor.getData();
        let plainTextData = stripHtmlTags(editorData);
        console.log("here");

        let formData = {
            creator: document.getElementById('project-creator-name').value,
            objective: document.getElementById('project-objective-input').value,
            title: document.getElementById('project-title-input').value,
            category: document.getElementById('project-category-input').value,
            description: plainTextData,
            priority: document.getElementById('choices-priority-input').value,
            planStartDate: document.getElementById('datepicker-start-date-input').value,
            planEndDate: document.getElementById('datepicker-end-date-input').value,
            status: document.getElementById('project-status-input').value,
            department: {
                id: document.getElementById('project-department-input').value
            },
            users: selectedUserIds
        };

        console.log(formData);
        console.log("this");

        let url = "/add-project?users=" + selectedUserIds.join("&users=");
        fetch(url, {
            method: 'Post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                // location.reload();
            })
            .catch(error => console.log("Error" + error));
    });
}