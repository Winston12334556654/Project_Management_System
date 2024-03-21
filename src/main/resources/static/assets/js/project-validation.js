// Function to validate form fields
function validateForm() {
    const form = document.getElementById('formAuthor');
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return false;
    }
    return true;
}
// Function to validate form and display confirmation modal
function validateAndConfirm() {
    // Perform your validation here
    if (validateForm()) {
        // Show success message
        Swal.fire({
            title: 'Success!',
            text: 'Your project has been created successfully.',
            icon: 'success',
            timer: 2000 // 2 seconds
        });
        showData();
    }else{
        Swal.fire({
            title: 'Error!',
            text: 'Please check back your project data',
            icon: 'error',
            timer: 2000 // 2 seconds
        });
    }
}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------//
document.getElementById('updateSubmit').addEventListener('click', function(event) {
    validateForUpdateProject(event);
});

function validateUpdateForm() {
    const form = document.getElementById('updateProject');
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return false;
    }
    return true;
}
function validateForUpdateProject(event){
    event.preventDefault();
    // Perform your validation here
    if (validateUpdateForm()) {
        // Show a success message
        Swal.fire({
            title: 'Success!',
            text: 'Your project has been updated successfully.',
            icon: 'success',
            timer: 2000 // 2 seconds
        });
        updateProject();
    }
}




