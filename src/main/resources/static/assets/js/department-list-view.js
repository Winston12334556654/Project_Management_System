let departmentList = [];

document.addEventListener('DOMContentLoaded', () => {
    // Make an AJAX request to fetch data from the Rest API endpoint
    fetch('/departments')
        .then(response => response.json())
        .then(data => {
            console.log('All department list: ', data);
            departmentList = data;
            // Render the data into the DataTable
            refreshDepartmentListTable(departmentList);
        })
        .catch(error => {
            console.log('Error: ', error);
        });

    // Render Department list Table
    function refreshDepartmentListTable(items) {
        const table = document.getElementById('department-table');
        // Check if DataTable is already initialized and destroy it
        if (table && table.DataTable) {
            table.DataTable.destroy();
        }

        return new DataTable(table, {
            data: items,
            responsive: true,
            columns: [
                { data: 'id' },
                { data: 'name' },
                { data: 'email' },
                { data: 'phone' },
                {
                    data: 'status',
                    render: (data, type, row) => `
                        <span class="badge rounded-pill text-bg-${data ? 'success' : 'danger'}">${data ? 'Active' : 'Inactive'}</span>
                    `,
                },
                {
                    data: 'active',
                    render: (data, type, row) => `
                        <button class="btn btn-sm btn-primary edit-item-btn" onclick="displayEditDepartmentModal(${row.id})">Edit</button>
                        
                        <button class="btn btn-sm btn-danger remove-item-btn" data-bs-toggle="modal" data-bs-target="#deleteRecordModal">Remove</button>
                    `,
                }
            ],
            dom: 'Bfrtip',
            buttons: ['copy', 'csv', 'excel', 'pdf', 'print'],
        });
        table.buttons().container().appendTo('#department-table .col-md-6:eq(0)');
    }

    // Add new Department
    document.getElementById("departmentAddForm").addEventListener('submit', function (event) {
        event.preventDefault();
        console.log("submitting add form .....");
        let formData = {
            name: document.getElementById('name').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value,
            status: document.getElementById('status').value
        };

        console.log(formData);

        let url = "/add-department";
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
                location.reload();
            })
            .catch(error => console.log("Error" + error));
    });

    // Display Department data in modal for edit
    function displayEditDepartmentModal(id) {
        const department = departmentList.find(department => department.id === id);
        console.log("Department Edit ", department);

        if (department) {
            document.getElementById('editId').value = department.id;
            document.getElementById('editName').value = department.name;
            document.getElementById('editEmail').value = department.email;
            document.getElementById('editPhone').value = department.phone;
            document.getElementById('editStatus').value = department.status;

            const departmentEditModal = document.getElementById('editDepModal');
            if (departmentEditModal) {
                departmentEditModal.classList.add('show');
                departmentEditModal.style.display = 'block';
            } else {
                console.log("Department Edit Modal not found");
            }
        } else {
            console.log("Department not found for ID:", id);
        }
    }

    // Edit Department data
    document.getElementById("departmentEditForm").addEventListener('submit', function (event) {
        event.preventDefault();
        console.log("submitting add form .....");
        const id = document.getElementById('editId').value;
        const name = document.getElementById('editName').value;
        const email = document.getElementById('editEmail').value;
        const phone = document.getElementById('editPhone').value;
        const status = document.getElementById('editStatus').value;

        const updatedDepartment = {
            id: id,
            name: name,
            email: email,
            phone: phone,
            status: status
        };

        console.log("updatedDepartment", updatedDepartment);
        fetch(`/edit-department/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedDepartment),
        })
            .then(response => response.json())
            .then(data => {
                console.log('Data received:', data);
                location.reload();
            })
            .catch(error => console.log("Error" + error));
    });

});
