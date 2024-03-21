
async function assignIssueBox(id) {
    let assignIssue = {};
    console.log("Issue Id:" + id);

    try {
        const response = await fetch(`/teamleaderissues/${id}`);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();

        console.log('All Issue Details From Data: ', data);
        assignIssue = data;

        console.log("Assign Issues:", assignIssue);
       // console.log("Members List:",assignIssue.memberUsers);
        if (assignIssue ) {

            document.getElementById('issueId').value = assignIssue.issueId;

            let planStartDate = assignIssue.issuePlanStartDate;
            let planDueDate = assignIssue.issuePlanEndDate;
            //For Start Date && Due Date
            const startDateFlatpickr = initializeStartDatePicker(planStartDate, planDueDate);
            const dueDateFlatpickr = initializeDueDatePicker(planStartDate, planDueDate);
            //For Members
            populateMemberList(assignIssue.memberUsers);
            // Show the modal
            const assignIssueModal = document.getElementById('assignIssueModal');
            if (assignIssueModal) {
                assignIssueModal.classList.add('show');
                assignIssueModal.style.display = 'block';
            } else {
                console.log("Issue Assign Modal not found");
            }
        }
    } catch (error) {
        console.log('Error: ', error);
    }
}

function populateMemberList(memberUsers) {
    // Assuming assignIssue.memberUsers is an array of objects with member information
    const memberListContainer = document.getElementById('memberlist');

    // Clear existing content
    memberListContainer.innerHTML = '';

    // Iterate over the member list and create <li> elements
    memberUsers.forEach(member => {
        // Create <li> element
        const listItem = document.createElement('li');

        // Set inner HTML for the <li> based on member information
        listItem.innerHTML = `
            <div class="form-check d-flex align-items-center justify-content-between">
                <div>
                    <input class="form-check-input mt-3" type="radio" name="user.id" value="${member.id}" id="${member.id}" >
                    <label class="form-check-label d-flex align-items-center" for="${member.id}">
                        <span class="flex-shrink-0">
                                <img src="/assets/userPhoto/${member.profilePictureFileName}" alt="" class="avatar-xs rounded-circle " />
                            </span>
                        <div class="d-flex justify-content-between flex-grow-1 ms-2 user-label-container">
                            <div>
                                <span>${member.name}</span>
                                <div><h6>${member.position.positionName}</h6></div>
                            </div>
                            
                        </div>
                        
                        
                    </label>
                    
                </div>
                <!--<div class="ms-auto " >
                                <button class="btn btn-sm btn-light " id="view-btn" style="margin-right: 25px;">View</button>
                            </div>-->
            </div>
        `;

        // Append the <li> to the container
        memberListContainer.appendChild(listItem);
    });
}


function initializeStartDatePicker(planStartDate, planDueDate) {
    const startDateElement = document.getElementById('startDate');

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
    const dueDateElement = document.getElementById('dueDate');

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


// File Names List
function updateFileNamesList(fileNames) {
    const fileNamesList = document.getElementById('fileNamesList');

    // Clear existing list items
    fileNamesList.innerHTML = '';

    // Loop through the file names and create list items
    fileNames.forEach(fileName => {
        const listItem = document.createElement('li');
        listItem.classList.add('mt-2', 'fileItem', 'border', 'rounded');

        listItem.innerHTML = `
            <div class="d-flex p-2">
                <div class="flex-shrink-0 me-3">
                    <div class="avatar-sm bg-light rounded">
                        <img src="https://i.pinimg.com/236x/1d/1f/e9/1d1fe9745c6a9c509965ebbc07c40db1.jpg" alt="Project-Image" width="50" height="50" data-dz-thumbnail="true">
                    </div>
                </div>
                <div class="flex-grow-1">
                    <div class="pt-1">
                        <h5 class="fs-14 mt-2" data-dz-name="true">${fileName}</h5>
<!--                        <p class="fs-13 text-muted mb-0" data-dz-size="true">751 Bytes</p>-->
                        <strong class="error text-danger" data-dz-errormessage="true"></strong>
                    </div>
                </div>
                
            </div>
        `;

        // Append the dynamically created list item to the list
        fileNamesList.appendChild(listItem);
    });
}