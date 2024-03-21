document.addEventListener("DOMContentLoaded", function (){
    displayProfile();
    getDepartment();
    getPositions();
})

function displayProfile() {
    // Fetch data from the server
    fetch('/user/profile', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },
        credentials: 'include' // Include this option if your server uses session-based authentication
    })
        .then(response => {
            // Check if the response is successful
            if (!response.ok) {
                throw new Error('Failed to fetch user data');
            }
            // Parse the JSON response
            return response.json();
        })
        .then(data => {
            console.log('User data:', data);
            document.getElementById('current-id').value = data.id;
            document.getElementById('nameInput').value = data.name;
             document.getElementById('dobInput').value = data.dob;
            document.getElementById('phoneNumberInput').value = data.phone;
            document.getElementById('emailInput').value = data.email;
            // Auto-select department in dropdown
            const positionSelected = document.getElementById('editPosition');
            positionSelected.value = data.position.id; // Assuming data contains departmentId

            // Optionally, dispatch a change event on the select element to trigger any associated event listeners
            const event1 = new Event('change');
            positionSelected.dispatchEvent(event1);

            if (data.gender === 'male') {
                document.getElementById('maleRadio').checked = true;
            } else if (data.gender === 'female') {
                document.getElementById('femaleRadio').checked = true;
            }

            document.getElementById('editEducation').value = data.education;
             document.getElementById('addressInput').value = data.address;

            // Auto-select department in dropdown
            const departmentSelect = document.getElementById('editDepartment');
            departmentSelect.value = data.department.id; // Assuming data contains departmentId

            // Optionally, dispatch a change event on the select element to trigger any associated event listeners
            const event = new Event('change');
            departmentSelect.dispatchEvent(event);

            const userProfileImage = document.getElementById('userProfile');
            if (data.profilePictureFileName) {
                userProfileImage.src = `/static/assets/userPhoto/${data.profilePictureFileName}`;
                userProfileImage.alt = data.name;
            } else {
                // Fallback image or hide the image element
                userProfileImage.src = '/static/assets/userPhoto/1709489830348.jpg'; // Provide a default image
                userProfileImage.alt = 'Default Profile Image';
            }

            document.getElementById('username').textContent = data.name;
            document.getElementById('position').textContent = data.position.positionName;
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
        });
}

function getDepartment(){
    fetch('/departments', {
        method: 'GET',
    })
        .then(r => r.json())
        .then(data => {
            // Get a reference to the select element
            const departmentSelect = document.getElementById('editDepartment');

            // Clear any existing options
            departmentSelect.innerHTML = '';

            // Add options based on the data received
            data.forEach(department => {
                const option = document.createElement('option');
                option.value = department.id; // Assuming department object has an 'id' property
                option.textContent = department.name; // Assuming department object has a 'name' property
                departmentSelect.appendChild(option);
            });
        })
}
function getPositions(){
    fetch('/api/user/positions', {
        method: 'GET',
    })
        .then(r => r.json())
        .then(data => {
            // Get a reference to the select element
            const positionSelect = document.getElementById('editPosition');
            // Clear any existing options
            positionSelect.innerHTML = '';
            // Add options based on the data received
            data.forEach(position => {
                const option = document.createElement('option');
                option.value = position.id; // Assuming department object has an 'id' property
                option.textContent = position.positionName; // Assuming department object has a 'name' property
                positionSelect.appendChild(option);
            });
        })
}

function  updateProfile(){
    const userId = document.getElementById('current-id').value;
    const updatedData = {
        name: document.getElementById('nameInput').value,
        dob: document.getElementById('dobInput').value,
        phone: document.getElementById('phoneNumberInput').value,
        email: document.getElementById('emailInput').value,
        position: {
            id: document.getElementById('editPosition').value
        },
        gender: document.querySelector('input[name="gender"]:checked').value,
        education: document.getElementById('editEducation').value,
        address: document.getElementById('addressInput').value,
        department:{
            id: document.getElementById('editDepartment').value
        },
    }
    fetch(`/edit-user/${userId}`, {
        method: 'POST',
        headers:{
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(updatedData)
    })
        .then(response => {
            if(response.ok){
                if(document.getElementById('editProfilePicture-in-user').value != null){
                    updateImage();
                }
                // Reload the browser
                Swal.fire({
                    title: 'Success!',
                    text: 'Your profile has been updated successfully.',
                    icon: 'success',
                    timer: 2000, // 2 seconds
                    showConfirmButton: false // Hide the default "Ok" button
                });
            }
        })
        .then(data=>{
            console.log(data);
        })
        .catch(error=> {
            console.log('Error saving user profile:', error);
        })
}

function  updateImage(){

    // Create FormData object
    const formData = new FormData();
    // Check if a file is selected before appending
    const profilePictureInput = document.getElementById('editProfilePicture-in-user');
    const selectedFile = profilePictureInput.files[0];
    console.log("Selected File:", selectedFile);

    if (selectedFile) {
        formData.append('file', selectedFile);
        // Log the FormData before sending the request
        console.log("FormData before sending:", formData);
        const id = document.getElementById('current-id').value;
        fetch(`/edit-profilePhoto/${id}`,{
            method: 'POST',
            body: formData,
        })
            .then(response=>{
                console.log("Response received from the server:", response);
                if(response.ok){
               console.log("ok");
            }
            })
            .then(updateUserData =>{
                console.log("User updated successfully:"+ updateUserData);
            })
            .catch(error => {
                console.error("Error updating user:", error);
            });
    }
}

function validateChangePassword() {
    var oldPassword = document.getElementById('oldPasswordInput').value;
    fetch(`/user/password/${oldPassword}`)
            .then(response=>{
                console.log("Response received from the server:", response);
                if(response.ok){
                    console.log("ok");
                }
                return response.json();
            })
                .then(data =>{
                    console.log("User updated successfully:"+ data);
                    if(data == true){
                        var newPassword = document.getElementById('newPasswordInput').value;
                        var confirmPassword = document.getElementById('confirmPasswordInput').value;

                        if (newPassword !== confirmPassword) {
                            var confirmPasswordInput = document.getElementById('confirmPasswordInput');
                            confirmPasswordInput.classList.add('is-invalid');
                            Swal.fire({
                                title: 'Success!',
                                html: '<div>Update password failed</div><div style="font-size: 60%; color: red">Please, make sure that new password & confirm password are the same</div>',
                                icon: 'error',
                                timer: 4000, // 2 seconds
                                showConfirmButton: false // Hide the default "Ok" button
                            });
                            return false;
                        }else{
                            Swal.fire({
                                title: 'Success!',
                                text: 'Your password has been updated successfully.',
                                icon: 'success',
                                timer: 2000, // 2 seconds
                                showConfirmButton: false // Hide the default "Ok" button
                            });
                            changePassword();
                        }
                    }else{
                        Swal.fire({
                            title: 'Success!',
                            html: '<div>Update password failed</div><div style="font-size: 60%; color: red">Please, check back your old password</div>',
                            icon: 'error',
                            timer: 4000, // 2 seconds
                            showConfirmButton: false // Hide the default "Ok" button
                        });
                    }

                })
                .catch(error => {
                    console.error("Error updating user:", error);
                });
}

function changePassword(){
    const userId = document.getElementById('current-id').value;
    const updatePassword = {
        password: document.getElementById('newPasswordInput').value
    }
    console.log(updatePassword);
    fetch(`/update-password/${userId}`, {
        method: 'POST',
        headers:{
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(updatePassword)
    })
        .then(response=>{
            console.log("Response received from the server:", response);
            if(response.ok){
                console.log("ok");
            }
        })
        .then(updateUserData =>{
            console.log("User updated successfully:"+ updateUserData);
        })
        .catch(error => {
            console.error("Error updating user:", error);
        });
}



