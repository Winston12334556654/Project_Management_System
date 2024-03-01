async function fetchUserDetails(teamLeaderId, teamLeaderElement) {
    try {
        const response = await fetch('/get-user/' + teamLeaderId);
        const user = await response.json();

        if (user && user.name) {
            // Create the label container
            const labelContainer = document.createElement('label');
            labelContainer.classList.add('form-check-label', 'd-flex', 'align-items-center');

            // Create the user photo element
            const userPhoto = document.createElement('span');
            userPhoto.classList.add('flex-shrink-0');
            const imgElement = document.createElement('img');
            imgElement.src = "/assets/userPhoto/" + user.profilePictureFileName;// Use a default photo if none is available
            imgElement.alt = 'User Photo';
            imgElement.classList.add('avatar-xs', 'rounded-circle', 'shadow');
            userPhoto.appendChild(imgElement);

            // Create the user details container
            const userDetailsContainer = document.createElement('div');
            userDetailsContainer.classList.add('d-flex', 'justify-content-between', 'flex-grow-1', 'ms-2', 'user-label-container');

            // Create the user name element
            const userNameElement = document.createElement('div');
            userNameElement.innerHTML = '<span>' + user.name + '</span>';

            // Append everything together
            userDetailsContainer.appendChild(userNameElement);


            labelContainer.appendChild(userPhoto);
            labelContainer.appendChild(userDetailsContainer);

            // Clear any existing content in teamLeaderElement and append the new labelContainer
            teamLeaderElement.innerHTML = '';
            teamLeaderElement.appendChild(labelContainer);
        } else {
            teamLeaderElement.textContent = 'User not found';
        }
    } catch (error) {
        console.error('Error fetching user details:', error);
        teamLeaderElement.textContent = 'Error fetching user details: ' + error.message;
    }
}

// Get all elements with the class "teamLeader"
var teamLeaderElements = document.getElementsByClassName("teamLeader");

// Iterate through each element
for (var i = 0; i < teamLeaderElements.length; i++) {
    var teamLeaderId = teamLeaderElements[i].getAttribute("data-team-leader-id");

    // Call the function to fetch user details and update HTML
    fetchUserDetails(teamLeaderId, teamLeaderElements[i]);
}