
function handleEditButtonClick() {

    var issueId = this.getAttribute('data-issue-id');
    console.log('Clicked "Edit" button for issue id:', issueId);
}

var editButton = document.getElementById('issueUpdate');
editButton.addEventListener('click', handleEditButtonClick);