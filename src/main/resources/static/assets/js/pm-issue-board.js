async function fetchUserDetails(teamLeaderId, teamLeaderElement) {
    try {
    const response = await fetch('/get-user/' + teamLeaderId);
        const user = await response.json();
        // Update HTML with user details, including the profile picture
        const profilePicture= user.profilePictureFileName;
        console.log("Profile Picture:",profilePicture);
        const imgElement = teamLeaderElement.querySelector('img');
        imgElement.src = '/assets/userPhoto/' + profilePicture
        ;
        imgElement.alt = '';  // Set alt text if needed
        teamLeaderElement.setAttribute('title', user.name);

    } catch (error) {
        console.error('Error fetching user details:', error);
        teamLeaderElement.textContent = 'Error fetching user details: ' + error.message;
    }
}

// Get all elements with the class "teamLeader"
var teamLeaderElements = document.getElementsByClassName("teamLeaderName");

// Iterate through each element
for (var i = 0; i < teamLeaderElements.length; i++) {
    var teamLeaderId = teamLeaderElements[i].getAttribute("data-team-leader-id");

    // Call the function to fetch user details and update HTML
    fetchUserDetails(teamLeaderId, teamLeaderElements[i]);
}