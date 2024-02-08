<!--Category-->
var categories = ["Designing", "Development"];

// Populate categories dropdown
var categoriesDropdown = document.getElementById("project-category-input");
categories.forEach(function (category) {
    const option = document.createElement("option");
    option.value = category;
    option.text = category;
    categoriesDropdown.add(option);
});

// Create a new script element
var script = document.createElement('script');

// Set the src attribute to the jQuery CDN URL
script.src = 'https://code.jquery.com/jquery-3.6.4.min.js';

// Append the script element to the document body
document.body.appendChild(script);

<!--Project creation-->
function departmentGetter(){
    fetch('/departments-selector')
        .then(response => response.json())
        .then(data => {
            data.forEach(department=>{
                const option = document.createElement('option');
                option.value = department.id;
                option.text = department.name;
                document.getElementById('project-department-input').createElement(option);
            })
        });
}

function stripHtmlTags(html) {
    const doc = new DOMParser().parseFromString(html, 'text/html');
    return doc.body.textContent || "";
}

function showData() {

    document.getElementById("formAuthor").addEventListener("submit", async function (event) {
        event.preventDefault();
        console.log("submitting form...");



        let editorData = window.ckeditor.getData();
        let plainTextData = stripHtmlTags(editorData);
        console.log("here");
        let formData ={
            creator: document.getElementById('project-creator-name').value,
            objective: document.getElementById('project-objective-input').value,
            title: document.getElementById('project-title-input').value,
            category: document.getElementById('project-category-input').value,
            description: plainTextData,
            priority: document.getElementById('choices-priority-input').value,
            planStartDate: document.getElementById('datepicker-start-date-input').value,
            planEndDate: document.getElementById('datepicker-end-date-input').value,
            status: document.getElementById('project-status-input').value
        };

        console.log("there");
        console.log(formData.title);
        console.log("this");

        let url = "/add-project";
        fetch(url, {
            method: 'Post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),

        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                // location.reload();
            })
            .catch(error => console.log("Error" + error));
    });
}