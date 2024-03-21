document.addEventListener('DOMContentLoaded', function () {
    let createIssueModal = new bootstrap.Modal(document.getElementById('createIssueModal'));
    let viewModal = new bootstrap.Modal(document.getElementById('viewModal'));

    document.getElementById('create-issue').addEventListener('click', function () {
        showCategoryDropdown();
        showProjectDropdown();
        initializeFileUpload1('file-input1', 'fileNames1');
        createIssueModal.show();
    });

    function showProjectDropdown(){
        var creatorValue = document.getElementById('creator').textContent;
        const url = '/get-projects/'+ creatorValue;
        fetch(url)
            .then(response => response.json())
            .then(data => {
                console.log('All Project list: ', data);
                const selectElement = document.getElementById('parent-project-input');
                selectElement.innerHTML = '';
                data.forEach(project => {
                    const option = document.createElement('option');
                    option.value = project.id;
                    option.textContent = project.title;
                    selectElement.appendChild(option);
                });
                selectElement.addEventListener('change', getTeamLeader);
                /*getTeamLeader();*/
                selectElement.addEventListener('change',getPlanDates);
                /*getPlanDates();*/
            })
            .catch(error => {
                console.log('Error: ', error);
            });
    }

    function getTeamLeader() {
        const projectId = document.getElementById('parent-project-input').value;
        const url = `/issue-members-selector/${projectId}`;
        console.log("ProjectId:",projectId);
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
                console.log("Data",data);
                showMemberList(data);
            })
            .catch(error => {
                console.error('Error fetching user data:', error);
            });
    }

    function showMemberList(dto) {
        const teamLeaderListContainer = document.getElementById('teamleader-list');
        // Clear existing member list content
        teamLeaderListContainer.innerHTML = '';

        if (!Array.isArray(dto)) {
            console.error('DTO is not an array:', dto);
            return;
        }

        // Loop through the DTOs and create HTML elements for each DTO
        dto.forEach(user => {
            console.log("user list: ", user);
            if(user.role.toString() === "TEAMLEADER") {
                console.log("Photo name",user.profilePictureName);
                const listItem = document.createElement('li');
                listItem.innerHTML = `
    <div class="form-check d-flex align-items-center justify-content-between">
        <div>
            <input class="form-check-input  mt-3" type="radio"  name="user.id" value="${user.id}" id="${user.id}">
            <label class="form-check-label  d-flex align-items-center" for="${user.id}">
                <span class="flex-shrink-0">

                    <img src="/assets/userPhoto/${user.profilePictureName}" alt="" class="avatar-xxs rounded-circle" />

                </span>
              
                <div class="d-flex justify-content-between flex-grow-1 ms-2 user-label-container">
                            <div>
                                <span >${user.name}</span>
                                <div ><h6>${user.position.positionName}</h6></div>
                            </div>
                            
                 </div>
</div>
       
            
            </label>
        </div>
    </div>
`;
                teamLeaderListContainer.appendChild(listItem);
            }
        });


    }

    let startDateFlatpickr;
    let dueDateFlatpickr;
    function getPlanDates(){
        const projectId = document.getElementById('parent-project-input').value;
        const url = `/get-project/${projectId}`;
        fetch(url)
            .then(response => response.json())
            .then(project => {
                console.log("Project:",project);
                // Display the users returned by the server in the member list
                let planStartDate = project.planStartDate;
                let planDueDate = project.planEndDate;
                console.log("Plan Start Date:",planStartDate);
                console.log("Plan Due Date:",planDueDate);
                //For Start Date && Due Date
                 startDateFlatpickr = initializeStartDatePicker(planStartDate, planDueDate);
                 dueDateFlatpickr = initializeDueDatePicker(planStartDate, planDueDate);


            })
            .catch(error => {
                console.error('Error fetching user data:', error);
            });


    }

    function initializeStartDatePicker(planStartDate, planDueDate) {
        const startDateElement = document.getElementById('issue-plan-start-date');

        return flatpickr(startDateElement, {
            enableTime: false,
            dateFormat: 'Y-m-d',
            minDate: planStartDate,
            maxDate: planDueDate,
            onChange: function (selectedDates) {
                dueDateFlatpickr.set('minDate', selectedDates[0]);
                startDateFlatpickr.set('maxDate', planDueDate);
            }
        });
    }

    function initializeDueDatePicker(planStartDate, planDueDate) {
        const dueDateElement = document.getElementById('issue-plan-due-date');

        return flatpickr(dueDateElement, {
            enableTime: false,
            dateFormat: 'Y-m-d',
            minDate: planStartDate,  // Set minimum date as planStartDate
            maxDate: planDueDate,    // Set maximum date as planDueDate
            onChange: function (selectedDates) {
                // Update maxDate of startDateFlatpickr to planDueDate
                startDateFlatpickr.set('maxDate', planDueDate);

                // Update minDate of dueDateFlatpickr to the selected due date
                dueDateFlatpickr.set('minDate', selectedDates[0]);
            }
        });
    }


    function showCategoryDropdown() {
        const url = '/get-categories';
        fetch(url)
            .then(response => response.json())
            .then(data => {
                console.log('All department list: ', data);
                const selectElement = document.getElementById('issue-category-input');
                selectElement.innerHTML = '';
                data.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.id;
                    option.textContent = category.name;
                    selectElement.appendChild(option);
                });

                // Create an option for adding a new category
                const addOption = document.createElement('option');
                addOption.value = 'add_new_category';
                addOption.textContent = '+ Add New Category';
                selectElement.appendChild(addOption);
            })
            .catch(error => {
                console.log('Error: ', error);
            });
    }


    // Event listener for select change
    document.getElementById('issue-category-input').addEventListener('change', function() {
        let categoryModal = new bootstrap.Modal(document.getElementById('addIssueCategoryModal'));
        const selectedValue = this.value;
        if (selectedValue === 'add_new_category') {
            categoryModal.show();
            this.value = '';
        }
    });

    //Event Listener for Add issue category
    document.getElementById('addIssueCategoryForm').addEventListener('submit', function (event){
        event.preventDefault();
        console.log("submitting add form .....");
        let formData = {
            name: document.getElementById('issueCategoryName').value,
            description: document.getElementById('issueCategoryDescription').value
        };

        console.log(formData);

        let url = '/add-category';
        fetch( url ,{
            method: 'POST',
            headers: {
                'Content-type' : 'application/json'
            },
            body: JSON.stringify(formData),
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                showCategoryDropdown();
                // Show success modal using SweetAlert
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Your issue category has been successfully added.',
                    showConfirmButton: false,
                    timer: 2000 // Close after 2 seconds
                })
            })
            .catch(error => {
                console.error('Error adding new category:', error);
            });
    });

})

