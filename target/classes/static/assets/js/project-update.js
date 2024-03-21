<!--These codes are for the project Update-->
function memberPlaces(uDepartment, uProject) {
    const memberListContainer = document.getElementById('team-member-list-update');
    // Clear existing member list content
    memberListContainer.innerHTML = '';

    if (!Array.isArray(uProject) || !Array.isArray(uDepartment)) {
        console.error('User data is not an array:', uProject, uDepartment);
        return;
    }

    uDepartment.forEach(user => {
        const isChecked = uProject.some(projUser  => projUser .id === user.id);
        appendUserToList(user, isChecked, memberListContainer);
    });
}

function appendUserToList(user, isChecked, container) {
    const listItem = document.createElement('li');
    listItem.innerHTML = `
        <div class="form-check d-flex align-items-center justify-content-between">
            <div>
                <label class="form-check-label d-flex align-items-center" for="user-${user.id}">
                 <input class="form-check-input-update me-3" type="checkbox" value="${user.id}" ${isChecked ? 'checked' : ''}>
                    <span class="flex-shrink-0">
                        <img src="/static/assets/userPhoto/${user.profilePictureFileName}" alt="" class="avatar-xxs rounded-circle" />
                    </span> 
                    <div>
                            <div class="fw-bold">${user.name}</div>
                            <div>${user.position.positionName}</div>
                        </div>
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
    container.appendChild(listItem);
}

function displayEditProject(id) {
    // Assign the project ID to a variable
    const projectId = id;
    console.log("Project Edit ", projectId);

    // Function to fetch project data and populate fields
    function fetchProjectByIdAndSetFields(projectId) {
        // Fetch project data based on its ID
        fetch(`/project-selector/${projectId}`, {
            method: 'GET'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(project => {
                // Check if a project is found
                if (project) {
                    // Populate modal fields with project data
                    document.getElementById('project-id').value = project.id;
                    document.getElementById('project-title-update').value = project.title;
                    document.getElementById('project-objective-update').value = project.objective;
                    //document.getElementById('project-creator-update').value = project.creator;
                    document.getElementById('project-category-update').value = project.category;
                    //document.getElementById('project-status-update').value = project.status;
                    document.getElementById('choices-priority-update').value = project.priority;
                    document.getElementById('datepicker-start-date-update').value = project.planStartDate;
                    document.getElementById('datepicker-end-date-update').value = project.planEndDate;

                    window.ckeditorUpdate.setData(project.description);

                    // Fetch user IDs based on project IDs
                    getUserIdsByProjectIds(project.id)
                        .then(userIds => {
                            console.log(userIds);
                            // Ensure userIds is always an array
                            if (!Array.isArray(userIds)) {
                                userIds = [userIds];
                            }
                            // Fetch user data with project IDs
                            return Promise.all([
                                getUserDataWithProject(userIds),
                                getTeamLeaderWithProject(userIds),
                            ]);
                        })
                        .then(([userDataFromProject, teamLeaderDataFromProject]) => {
                            // Fetch departments data
                            return fetch('/departments', {
                                method: 'GET'
                            })
                                .then(response => {
                                    if (!response.ok) {
                                        throw new Error('Network response was not ok');
                                    }
                                    return response.json();
                                })
                                .then(departmentsData => {
                                    // Check if departmentsData is defined and is an array
                                    if (Array.isArray(departmentsData)) {
                                        // Set the selected department based on project's department id
                                        const selectElement = document.getElementById('project-department-update');
                                        const relatedDepartment = departmentsData.find(department => department.id === project.department.id);

                                        if (relatedDepartment) {
                                            selectElement.value = relatedDepartment.name;

                                            // Fetch users based on department and project
                                            return Promise.all([
                                                getUserFromDepartment(relatedDepartment.id),
                                                getLeaderFromDepartment(relatedDepartment.id)
                                            ])
                                                .then(([userDataFromDepartment, leaderDataFromDepartment]) => {
                                                    console.log("userDataFromProject:", userDataFromProject);
                                                    console.log("userDataFromDepartment:", userDataFromDepartment);
                                                    // Call memberPlaces with the fetched user data
                                                    memberPlaces(userDataFromDepartment, userDataFromProject);
                                                    // Now you can use leaderDataFromDepartment and teamLeaderDataFromProject as needed
                                                    populateLeaderList(leaderDataFromDepartment, teamLeaderDataFromProject);
                                                });
                                        } else {
                                            console.log('Related department not found.');
                                        }
                                    } else {
                                        console.log('departmentsData is not an array or is undefined/null.');
                                    }
                                })
                                .catch(error => console.error("Error fetching departments:", error));
                        })
                        .catch(error => {
                            console.error("Error:", error);
                        });
                    // Show the modal
                    const projectEditModal = document.getElementById('updateModal');
                    if (projectEditModal) {
                        projectEditModal.classList.add('show');
                        projectEditModal.style.display = 'block';

                    } else {
                        console.log("Project Edit Modal not found");
                    }
                } else {
                    console.log("Project not found for ID:", projectId);
                }
            })
            .catch(error => console.error("Error fetching project:", error));
    }
    // Call the function to fetch department data and set selected department
    fetchProjectByIdAndSetFields(projectId);
}
function getUserFromDepartment(departmentId) {
    const url = `/members-selection/${departmentId}`;
    return fetch(url, {
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
            console.log(data);
            // Return the user data
            return data;
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            throw error; // Rethrow the error for the caller to handle
        });
}
function getLeaderFromDepartment(departmentId){
    const url = `/leader-selection/${departmentId}`;
    return fetch(url, {
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
            console.log(data);
            // Return the user data
            return data;
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            throw error; // Rethrow the error for the caller to handle
        });
}

// Function to fetch user IDs based on project IDs
function getUserIdsByProjectIds(projectId) {
    // Construct the URL with the projectId as a query parameter
    const url = `/member?projectId=${projectId}`;

    // Make a GET request using Fetch API
    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Return the response data, which will contain user IDs
            console.log(data);
            return data;
        })
        .catch(error => {
            // Handle errors
            console.error('There was a problem with the fetch operation:', error);
            throw error; // Rethrow the error for the caller to handle
        });
}

// Function to fetch user data based on user IDs
function getUserDataWithProject(userIds) {
    const url = `/users?userIds=${userIds.join(',')}`;
    return fetch(url, {
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
        .then(users => {
            // Process the user data and return it
            console.log("Hey Nigga : "+users);
            return users;
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            throw error; // rethrow the error to be caught elsewhere
        });
}
<!--The End-->

<!--Edit Project codes-->

function htmlTags(html) {
    const doc = new DOMParser().parseFromString(html, 'text/html');
    return doc.body.textContent || "";
}

function updateProject(){
    event.preventDefault();
    console.log("submitting add form .....");

    const choicedUsers = [];
    document.querySelectorAll('.form-check-input-update').forEach(checkbox => {

        if (checkbox.checked && checkbox.value !== "dark") {
            choicedUsers.push(checkbox.value);
        }
    });

    console.log(choicedUsers);

    let editor = window.ckeditorUpdate.getData();
    console.log(editor);
    let plainTextData = htmlTags(editor);
    console.log("here");

    let updateData = {
        //creator: document.getElementById('project-creator-update').value,
        objective: document.getElementById('project-objective-update').value,
        title: document.getElementById('project-title-update').value ,
        category: document.getElementById('project-category-update').value,
        description: plainTextData,
        priority: document.getElementById('choices-priority-update').value,
        planStartDate: document.getElementById('datepicker-start-date-update').value,
        planEndDate: document.getElementById('datepicker-end-date-update').value,
        //status: document.getElementById('project-status-update').value,
    };
    console.log("updateData", updateData);
    const id = document.getElementById('project-id').value;
    const response =  fetch(`/edit-project/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({...updateData, userIds: choicedUsers}),
    })
        .then(response => response.json())
        .then(data => {
            console.log('Data received:', data);
            displayProjects('All');
        })
        .catch(error => console.log("Error" + error));
}

function getTeamLeaderWithProject(userIds){
    const url = `/leaders?userIds=${userIds.join(',')}`;
    return fetch(url, {
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
        .then(users => {
            // Process the user data and return it
            console.log("Hey Nigga : "+users);
            return users;
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            throw error; // rethrow the error to be caught elsewhere
        });
}

function populateLeaderList(leaderFromDepartment, leaderFromProject) {
    const memberListContainer = document.getElementById('team-leader-list-update');
    // console.log("dto : " + dto);
    // Clear existing member list content
    memberListContainer.innerHTML = '';

    if (!Array.isArray(leaderFromProject) || !Array.isArray(leaderFromProject)) {
        console.error('User data is not an array:', leaderFromProject, leaderFromDepartment);
        return;
    }

    leaderFromDepartment.forEach(user => {
        const isChecked = leaderFromProject.some(projUser  => projUser .id === user.id);
        appendLeaderToList(user, isChecked, memberListContainer);
    });
}

function appendLeaderToList(user, isChecked, container) {
    const listItem = document.createElement('li');
    listItem.innerHTML = `
        <div class="form-check d-flex align-items-center justify-content-between">
            <div>
                <label class="form-check-label d-flex align-items-center" for="user-${user.id}">
                 <input class="form-check-input-update me-3" type="checkbox" value="${user.id}" ${isChecked ? 'checked' : ''}>
                    <span class="flex-shrink-0">
                        <img src="/static/assets/userPhoto/${user.profilePictureFileName}" alt="" class="avatar-xxs rounded-circle" />
                    </span> 
                    <div>
                            <div class="fw-bold">${user.name}</div>
                            <div>${user.position.positionName}</div>
                        </div>
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
    container.appendChild(listItem);
}










