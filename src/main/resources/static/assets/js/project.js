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
    // Call userGetter to fetch and display a user list for the default department
    displayProjects();
    departmentGetter();
});

function userGetter() {
    const departmentId = document.getElementById('project-department-input').value;
    const url = `/members-selector/${departmentId}`;

    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Display the users returned by the server in the member list
            populateMemberList(data);
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
        });
}

function populateMemberList(dto) {
    const memberListContainer = document.getElementById('team-member-list');
    // Clear existing member list content
    memberListContainer.innerHTML = '';

    if (!Array.isArray(dto)) {
        console.error('DTO is not an array:', dto);
        return;
    }

    // Loop through the DTOs and create HTML elements for each DTO
    dto.forEach(user => {
        const listItem = document.createElement('li');
        listItem.innerHTML = `
            <div class="form-check d-flex align-items-center justify-content-between">
                <div>
                    <input class="form-check-input  me-3" type="checkbox" value="${user.id}">
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
                console.log(department.id);
                console.log(department.name)
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

 function showData() {

    document.getElementById("formAuthor").addEventListener("submit", async function (event) {
        event.preventDefault();
        console.log("submitting form...");

        const selectedUserIds = [];
        document.querySelectorAll('.form-check-input').forEach(checkbox => {

            if (checkbox.checked && checkbox.value !== "dark") {
                selectedUserIds.push(checkbox.value);
            }
        });
        console.log(selectedUserIds);

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
        };
        console.log(formData);
        console.log("this");

       let url = "/add-project";
        fetch(url, {
            method: 'Post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ ...formData, userIds: selectedUserIds}),
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                // location.reload();
            })
            .catch(error => console.log("Error" + error));
    });
}

<!--Displaying all the data-->
// Get the select box element
const projectFilterSelect = document.getElementById('projectFilter');

// Add an event listener to handle changes in the selected value
projectFilterSelect.addEventListener('change', function() {
    const selectedFilter = projectFilterSelect.value;
    displayProjects(selectedFilter);
});

function displayProjects(filter){
    const url = "/show-projects";
    fetch(url, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Filter projects based on the selected filter
            const filteredProjects = filterProjects(data, filter);
            // Display the users returned by the server in the member list
            populateProjectList(filteredProjects);
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
        });
}
function filterProjects(projects, filter) {
    switch (filter) {
        case 'Today':
            return projects.filter(project => isToday(new Date(project.planStartDate)));
        case 'Yesterday':
            return projects.filter(project => isYesterday(new Date(project.planStartDate)));
        // Add more cases for other filter options if needed
        case 'All':
        default:
            return projects;
    }
}

// Function to check if a date is today
function isToday(date) {
    const today = new Date();
    return date.toDateString() === today.toDateString();
}

// Function to check if a date is yesterday
function isYesterday(date) {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    return date.toDateString() === yesterday.toDateString();
}

const titles = [];
function populateProjectList(dto) {
    const projectListContainer = document.getElementById('projects');
    // Clear existing member list content
    projectListContainer.innerHTML = '';

    if (!Array.isArray(dto)) {
        console.error('DTO is not an array:', dto);
        return;
    }

    // Loop through the DTOs and create HTML elements for each DTO
    dto.forEach(pj => {
        const projectCard = document.createElement('div');
        projectCard.className = 'col-xxl-3 col-sm-6 project-card';
        projectCard.innerHTML = `
                    <div class="card card-height-100" id="project-card">
                        <div class="card-body">
                            <div class="d-flex flex-column h-100">
                                <div class="d-flex">
                                    <div class="flex-grow-1">
                                        <p class="text-muted mb-4">Updated 3hrs ago</p>
                                    </div>
                                    <div class="flex-shrink-0">
                                        <div class="d-flex gap-1 align-items-center">

                                            <div class="dropdown">
    <button class="btn btn-link text-muted p-1 mt-n2 py-0 text-decoration-none fs-15 shadow-none" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
            <path d="M5 8a1 1 0 1 1 0-2 1 1 0 0 1 0 2zm3 0a1 1 0 1 1 0-2 1 1 0 0 1 0 2zm3 0a1 1 0 1 1 0-2 1 1 0 0 1 0 2z"/>
        </svg>
    </button>

    <div class="dropdown-menu dropdown-menu-end">
        <form id="viewProjectForm" action="/project/project-detail" method="POST">
            <input type="hidden" name="id" value="${pj.id}">
            <button type="submit" class="dropdown-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" th:class="bi bi-eye-fill align-bottom me-2 text-muted" viewBox="0 0 16 16">
                <path d="M.5 3 .04.87a2 2 0 0 0-.342 1.311l.637 7A2 2 0 0 0 2.826 14H9v-1H2.826a1 1 0 0 1-.995-.91l-.637-7A1 1 0 0 1 2.19 4h11.62a1 1 0 0 1 .996 1.09L14.54 8h1.005l.256-2.819A2 2 0 0 0 13.81 3H9.828a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 6.172 1H2.5a2 2 0 0 0-2 2m5.672-1a1 1 0 0 1 .707.293L7.586 3H2.19q-.362.002-.683.12L1.5 2.98a1 1 0 0 1 1-.98z"/>
                <path d="M15.854 10.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.707 0l-1.5-1.5a.5.5 0 0 1 .707-.708l1.146 1.147 2.646-2.647a.5.5 0 0 1 .708 0"/>
                </svg>
                View
            </button>
        </form>
        
        <a class="dropdown-item" href="#" data-bs-toggle="modal" data-bs-target="#updateModal">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" th:class="bi bi-pencil-fill align-bottom me-2 text-muted" viewBox="0 0 16 16">
                <path d="M12.28 4.28l-.025-.025a.5.5 0 0 1-.144-.332L12.11 3H11V1H3v2H1v10h2v2h10v-2h2V5h-2.5a.5.5 0 0 1-.36.28zM4 2h6v2H4V2z"/>
            </svg>
            Edit
        </a>
        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="#" data-bs-toggle="modal" data-bs-target="#removeProjectModal">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" th:class="bi bi-trash-fill align-bottom me-2 text-muted" viewBox="0 0 16 16">
                <path d="M5.5 1a.5.5 0 0 1 .5.5V2h4v-.5a.5.5 0 0 1 1 0V2h1a.5.5 0 0 1 0 1H1a.5.5 0 0 1 0-1h1V1.5a.5.5 0 0 1 .5-.5zM1.18 5l.83 10.005a1 1 0 0 0 .996.995h10.004a1 1 0 0 0 .995-.996L14.82 5H1.18zm6.156 1.61a.5.5 0 0 1 .488.612l-.488 3.488a.5.5 0 0 1-.612.488l-1.488-.41a.5.5 0 0 1-.324-.787l.787-.786a.5.5 0 0 1 .787.324l.398 1.196 2.012-2.012-.398-1.196a.5.5 0 0 1 .612-.612l1.488.41a.5.5 0 0 1 .324.787l-.787.786a.5.5 0 0 1-.787-.324L8.036 6.61l-.398-1.196a.5.5 0 0 1 .324-.787l1.488-.41z"/>
            </svg>
            Remove
        </a>
    </div>
</div>
                                        </div>
                                    </div>


                                </div>
                                <div class="d-flex mb-2">
                                    <div class="flex-shrink-0 me-3">
                                        <div class="avatar-sm">
                                                    <span class="avatar-title bg-warning-subtle rounded p-2">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="#343a40" class="bi bi-folder-check" viewBox="0 0 16 16">
  <path fill="#343a40" d="m.5 3 .04.87a2 2 0 0 0-.342 1.311l.637 7A2 2 0 0 0 2.826 14H9v-1H2.826a1 1 0 0 1-.995-.91l-.637-7A1 1 0 0 1 2.19 4h11.62a1 1 0 0 1 .996 1.09L14.54 8h1.005l.256-2.819A2 2 0 0 0 13.81 3H9.828a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 6.172 1H2.5a2 2 0 0 0-2 2m5.672-1a1 1 0 0 1 .707.293L7.586 3H2.19q-.362.002-.683.12L1.5 2.98a1 1 0 0 1 1-.98z"/>
  <path fill="#343a40" d="M15.854 10.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.707 0l-1.5-1.5a.5.5 0 0 1 .707-.708l1.146 1.147 2.646-2.647a.5.5 0 0 1 .708 0"/>
</svg>

                                                    </span>
                                        </div>
                                    </div>
                                    <div class="flex-grow-1">
                                        <h5 class="mb-1 fs-15"><a href="project-detail" class="project-title">${pj.title}</a></h5>
                                        <p class="project-description text-truncate-two-lines mb-3" id="project-description">${pj.description}</p>
                                    </div>
                                </div>
                                <div class="mt-auto">
                                    <div class="d-flex mb-2">
                                        <div class="flex-grow-1">
                                            <div>Issues</div>
                                        </div>

                                    </div>
                                    <div class="progress progress-sm animated-progress">
                                        <div class="progress-bar bg-success" role="progressbar" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100" style="width: 34%;"></div><!-- /.progress-bar -->
                                    </div><!-- /.progress -->
                                </div>
                            </div>

                        </div>
                        <!-- end card body -->
                        <div class="card-footer bg-transparent border-top-dashed py-2">
                            <div class="d-flex align-items-center">
                                <div class="flex-grow-1">
                                    <div class="avatar-group">
                                        <a href="javascript: void(0);" class="avatar-group-item shadow" data-bs-toggle="tooltip" data-bs-trigger="hover" data-bs-placement="top" title="Darline Williams">
                                            <div class="avatar-xxs">
                                                <img src="assets/images/users/avatar-2.jpg" alt="" class="rounded-circle img-fluid">
                                            </div>
                                        </a>
                                        <a href="javascript: void(0);" class="avatar-group-item shadow" data-bs-toggle="tooltip" data-bs-trigger="hover" data-bs-placement="top" title="Darline Williams">
                                            <div class="avatar-xxs">
                                                <img src="assets/images/users/avatar-2.jpg" alt="" class="rounded-circle img-fluid">
                                            </div>
                                        </a>
                                    </div>
                                </div>
                                <div class="flex-shrink-0">
                                    <div class="text-muted">
                                        <i class="ri-calendar-event-fill me-1 align-bottom"></i> ${pj.planStartDate}
                                    </div>
                                </div>

                            </div>

                        </div>
                        <!-- end card footer -->
                    </div>
        `;
        projectListContainer.appendChild(projectCard);
        projectCard.addEventListener('click', function(event) {
            const dropdownToggle = event.target.closest('.dropdown-toggle');
            if (dropdownToggle) {
                // Toggle the dropdown menu
                const dropdownMenu = dropdownToggle.nextElementSibling;
                dropdownMenu.classList.toggle('show');
            }
        });
    });
    const titleElements = document.querySelectorAll('.project-title');
    titleElements.forEach(element => {
        titles.push(element.textContent);
    });
}

function searchProjects() {
    const searchInput = document.getElementById('search-input').value;
    console.log(searchInput);
    const projectCards = document.querySelectorAll('.project-card');

    console.log(titles);
    projectCards.forEach((card, index) => {
        const title = titles[index];
        if (title.includes(searchInput)) {
            card.style.display = 'block'; // Show the card if it matches the search
        } else {
            card.style.display = 'none'; // Hide the card if it doesn't match the search
        }
    });
}
document.getElementById('search-input').addEventListener('input', searchProjects);

//JS codes to update project data


