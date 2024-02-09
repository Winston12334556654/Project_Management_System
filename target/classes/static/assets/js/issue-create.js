function getCategory() {
    const url = '/get-category';
    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log('All department list: ', data);
            const selectElement = document.getElementById('issue-category-input');
            // Clear existing options
            selectElement.innerHTML = '';
            // Create and append options for each department
            data.forEach(category => {
                const option = document.createElement('option');
                option.value = category.id;
                option.textContent = category.name;
                selectElement.appendChild(option);
            })
                .catch(error => {
                    console.log('Error: ', error);
                });
        });
}


