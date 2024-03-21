document.getElementById("createIssueBtn").addEventListener('click', function (event) {

    event.preventDefault();
    // Get the form element
    const form = document.getElementById("createIssueForm");

    // Check if the form is valid
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return false;
    }

    const selectedTeamLeaderId = getSelectedTeamLeaderId();
    console.log('Selected Team Leader ID:', selectedTeamLeaderId);
    function stripHtmlTags(html) {
        const doc = new DOMParser().parseFromString(html, 'text/html');
        return doc.body.textContent || "";
    }
    let issueEditorData = window.ckeditor.getData();
    let plainTextData = stripHtmlTags(issueEditorData);

    const formData = new FormData();
    let issueData = {
        project: {
            id: document.getElementById('parent-project-input').value
        },
        title: document.getElementById('issue-title-input').value,
        issueType: document.getElementById('issue-type-input').value,
        issueCategory: {
            id: document.getElementById('issue-category-input').value
        },
        subject: document.getElementById('issue-subject-input').value,
        description: plainTextData,
        /*teamLeaderId: document.getElementById('issue-teamleader-input').value,*/
        teamLeaderId: getSelectedTeamLeaderId(),
        planStartDate: document.getElementById('issue-plan-start-date').value,
        planDueDate: document.getElementById('issue-plan-due-date').value,
        priority: document.getElementById('issue-priority-input').value,
    };
    //formData.append('teamLeaderId', selectedTeamLeaderId);
    formData.append("issue", JSON.stringify(issueData));
    const fileInput = document.getElementById('file-input1');
    for (let i = 0; i < fileInput.files.length; i++) {
        formData.append('files', fileInput.files[i]);
    }

    // let issue ;

    let url = "/create-issue";
    fetch(url, {
        method: 'Post',
        body: formData,

    })
        .then(response => response.json())
        .then(data => {
            console.log("data receive successfully")
            console.log(data);
            $('#createIssueModal').modal('hide');
            sendNotiIssueCreate(data);
            // Reload the page
        })
        .catch(error => console.log("Error" + error));
});

function sendNotiIssueCreate(issue) {

    fetch(`/get-user/${issue.teamLeaderId}`)
        .then(response => response.json())
        .then(user => {
            delete currentUser.authorities;
            delete user.authorities;

            let noti = {
                title: "New Issue Assigned",
                redirectURL: "/teamleader-issueboard",
                content: `You have been assigned to Issue of ${issue.title}`,
                sender: currentUser,
                sendTo: user
            }
            sendNotification(noti);
            window.location.reload();
        })

}


function getSelectedTeamLeaderId() {
    const radioButtons = document.querySelectorAll('input[name="user.id"]');

    for (const radioButton of radioButtons) {
        if (radioButton.checked) {
            return radioButton.value;
        }
    }
    // Return a default value or handle the case where no team leader is selected
    return null;
}