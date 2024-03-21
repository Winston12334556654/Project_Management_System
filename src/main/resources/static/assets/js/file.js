function initializeFileUpload(inputId, containerId){
    document.getElementById('file-input').addEventListener('change', function (e) {
        const files = e.target.files;
        const fileNamesContainer = document.getElementById('fileNames');

        for (let i = 0; i < files.length; i++) {
            // Check if the file with the same name already exists
            const existingFile = Array.from(fileNamesContainer.children).find(item => {
                const fileNameElement = item.querySelector('[data-dz-name]');
                return fileNameElement && fileNameElement.textContent === files[i].name;
            });

            if (!existingFile) {
                const li = document.createElement('li');
                li.className = 'mt-2 fileItem';

                const fileItemDiv = document.createElement('div');
                fileItemDiv.className = 'border rounded';

                const dFlexDiv = document.createElement('div');
                dFlexDiv.className = 'd-flex p-2';

                const thumbnailDiv = document.createElement('div');
                thumbnailDiv.className = 'flex-shrink-0 me-3';
                const thumbnail = document.createElement('div');
                thumbnail.className = 'avatar-sm bg-light rounded';
                const img = document.createElement('img');
                img.src = 'https://i.pinimg.com/236x/1d/1f/e9/1d1fe9745c6a9c509965ebbc07c40db1.jpg';
                img.alt = 'Project-Image';
                img.width = 50;
                img.height = 50;
                img.dataset.dzThumbnail = true;
                thumbnail.appendChild(img);
                thumbnailDiv.appendChild(thumbnail);

                const contentDiv = document.createElement('div');
                contentDiv.className = 'flex-grow-1';

                const ptDiv = document.createElement('div');
                ptDiv.className = 'pt-1';

                const fileNameHeading = document.createElement('h5');
                fileNameHeading.className = 'fs-14 mb-1';
                fileNameHeading.dataset.dzName = true;
                fileNameHeading.textContent = files[i].name;

                const fileSizeParagraph = document.createElement('p');
                fileSizeParagraph.className = 'fs-13 text-muted mb-0';
                fileSizeParagraph.dataset.dzSize = true;
                fileSizeParagraph.textContent = formatBytes(files[i].size);

                const errorStrong = document.createElement('strong');
                errorStrong.className = 'error text-danger';
                errorStrong.dataset.dzErrormessage = true;

                ptDiv.appendChild(fileNameHeading);
                ptDiv.appendChild(fileSizeParagraph);
                ptDiv.appendChild(errorStrong);

                contentDiv.appendChild(ptDiv);

                const deleteDiv = document.createElement('div');
                deleteDiv.className = 'flex-shrink-0 ms-3';
                const deleteButton = document.createElement('button');
                deleteButton.className = 'btn btn-sm btn-danger';
                deleteButton.textContent = 'Delete';
                deleteButton.dataset.dzRemove = true;

                // Add an event listener for the "Delete" button
                deleteButton.addEventListener('click', function () {
                    fileNamesContainer.removeChild(li);
                });

                deleteDiv.appendChild(deleteButton);

                dFlexDiv.appendChild(thumbnailDiv);
                dFlexDiv.appendChild(contentDiv);
                dFlexDiv.appendChild(deleteDiv);

                fileItemDiv.appendChild(dFlexDiv);

                li.appendChild(fileItemDiv);
                fileNamesContainer.appendChild(li);
            }
        }

        // Clear the input field value if you want to reset the selection
        // e.target.value = '';
    });
}

function initializeFileUpload1(inputId, containerId){
    document.getElementById('file-input1').addEventListener('change', function (e) {
        const files = e.target.files;
        const fileNamesContainer = document.getElementById('fileNames1');

        for (let i = 0; i < files.length; i++) {
            // Check if the file with the same name already exists
            const existingFile = Array.from(fileNamesContainer.children).find(item => {
                const fileNameElement = item.querySelector('[data-dz-name]');
                return fileNameElement && fileNameElement.textContent === files[i].name;
            });

            if (!existingFile) {
                const li = document.createElement('li');
                li.className = 'mt-2 fileItem';

                const fileItemDiv = document.createElement('div');
                fileItemDiv.className = 'border rounded';

                const dFlexDiv = document.createElement('div');
                dFlexDiv.className = 'd-flex p-2';

                const thumbnailDiv = document.createElement('div');
                thumbnailDiv.className = 'flex-shrink-0 me-3';
                const thumbnail = document.createElement('div');
                thumbnail.className = 'avatar-sm bg-light rounded';
                const img = document.createElement('img');
                img.src = 'https://i.pinimg.com/236x/1d/1f/e9/1d1fe9745c6a9c509965ebbc07c40db1.jpg';
                img.alt = 'Project-Image';
                img.width = 50;
                img.height = 50;
                img.dataset.dzThumbnail = true;
                thumbnail.appendChild(img);
                thumbnailDiv.appendChild(thumbnail);

                const contentDiv = document.createElement('div');
                contentDiv.className = 'flex-grow-1';

                const ptDiv = document.createElement('div');
                ptDiv.className = 'pt-1';

                const fileNameHeading = document.createElement('h5');
                fileNameHeading.className = 'fs-14 mb-1';
                fileNameHeading.dataset.dzName = true;
                fileNameHeading.textContent = files[i].name;

                const fileSizeParagraph = document.createElement('p');
                fileSizeParagraph.className = 'fs-13 text-muted mb-0';
                fileSizeParagraph.dataset.dzSize = true;
                fileSizeParagraph.textContent = formatBytes(files[i].size);

                const errorStrong = document.createElement('strong');
                errorStrong.className = 'error text-danger';
                errorStrong.dataset.dzErrormessage = true;

                ptDiv.appendChild(fileNameHeading);
                ptDiv.appendChild(fileSizeParagraph);
                ptDiv.appendChild(errorStrong);

                contentDiv.appendChild(ptDiv);

                const deleteDiv = document.createElement('div');
                deleteDiv.className = 'flex-shrink-0 ms-3';
                const deleteButton = document.createElement('button');
                deleteButton.className = 'btn btn-sm btn-danger';
                deleteButton.textContent = 'Delete';
                deleteButton.dataset.dzRemove = true;

                // Add an event listener for the "Delete" button
                deleteButton.addEventListener('click', function () {
                    fileNamesContainer.removeChild(li);
                });

                deleteDiv.appendChild(deleteButton);

                dFlexDiv.appendChild(thumbnailDiv);
                dFlexDiv.appendChild(contentDiv);
                dFlexDiv.appendChild(deleteDiv);

                fileItemDiv.appendChild(dFlexDiv);

                li.appendChild(fileItemDiv);
                fileNamesContainer.appendChild(li);
            }
        }
    });
}

function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}