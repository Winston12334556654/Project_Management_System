let issueList = [];
document.addEventListener('DOMContentLoaded', () => {
    // Make an AJAX request to fetch data from the Rest API endpoint
    fetch('/get-issues')
        .then(response => response.json())
        .then(data => {
            console.log('All issue list: ', data);
            issueList = data;
            // Render the data into the DataTable
            refreshIssueListTable(issueList);
        })
        .catch(error => {
            console.log('Error: ', error);
        });

    // checkInputValidation('name', 'Department name', null, null);
    // checkInputValidation('editDepartmentName', 'Department name', null, null);
    // formValidate('addDepartmentForm', addDepartment);
    // formValidate('editDepartmentForm', editDepartment);
});

//Render Department list Table
function refreshIssueListTable(items) {
    const table = document.getElementById('department-table');

    // Check if DataTable is already initialized and destroy it
    if (table && table.DataTable) {
        table.DataTable.destroy();
    }

    return new DataTable(table, {
        data: items,
        responsive: true,
        columns: [
            {data: 'id'},
            {data: 'name'},
            {data: 'email'},
            {data: 'phone'},
            {
                data: 'status',
                render: (data, type, row) => `
                    <span class="badge rounded-pill text-bg-${data ? 'success' : 'danger'}">${data ? 'Active' : 'Inactive'}</span>
                `,
            },
            {
                data: 'action',
                render: (data, type, row) => `
                    <button class="btn btn-sm btn-primary edit-item-btn" onclick="displayEditDepartmentModal(${row.id})">Edit</button>
                    <button class="btn btn-sm btn-${data ? 'danger' : 'success'} mx-2" onclick="toggleDepartment(${row.id})">${data ? 'Close' : 'Open'}</button>
                    <button class="btn btn-sm btn-danger remove-item-btn" data-bs-toggle="modal" data-bs-target="#deleteRecordModal">Remove</button>
                `,
            }
        ],
    });
}
