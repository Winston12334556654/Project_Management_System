document.getElementById("createIssueForm").addEventListener('submit', function (event) {
    event.preventDefault();
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
        teamLeaderId: document.getElementById('issue-teamleader-input').value,
        planStartDate: document.getElementById('issue-plan-start-date').value,
        planDueDate: document.getElementById('issue-plan-due-date').value,
        priority: document.getElementById('issue-priority-input').value,
    };

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
            //issue = data;

            // fetch(url, {
            //
            // }).then(files=>{
            //     issue = {...issue,...files};
            // })

        })
        .catch(error => console.log("Error" + error));
});