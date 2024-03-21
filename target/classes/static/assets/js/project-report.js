let pjtList;
let loginUser;
document.addEventListener('DOMContentLoaded', function () {
    async function  fetchCurrentUser() {
        return await fetch('/api/user/current-user')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch current user');
                }
                return response.json();
            })
            .then(user => {
                return user;
            })
            .catch(error => {
                console.error('Error fetching current user:', error);
                return Promise.reject(0);
            });
    }

    async function getAllProjects(user){
        let creatorName = null;
        if(user.role === 'PM'){
            creatorName = currentUser.name;
        }
        return await fetch(`/get-all-projects/${creatorName}`, function (){})
            .then(response => response.json())
            .then(projects => {
                pjtList = projects;
                return projects;

            })
            .catch(err => {
                return Promise.reject(0);
            });
    }

    function showDepartmentCharts(projects){
        getAllDepartments()
            .then(departments =>{
                renderPieChart(projects,departments);
                renderBarChart(projects,departments);
            });
    }

    function calculateDepartmentPercentage(projects,department){
        const filterData = projects.filter(project => project.department.id === department.id);
        return 100*(filterData.length/projects.length);
    }

    function showGanttChart(data){
        Highcharts.ganttChart('gantt-chat-container', {
            title: {
                text: 'Project Timeline'
            },
            xAxis: {
                currentDateIndicator: true,
                min: Date.UTC(2024, 0, 1),
                max: Date.UTC(2024, 11, 31)
            },
            series: data.map(project => ({
                name: project.title,
                data: [{
                    name: project.title,
                    start: Date.parse(project.planStartDate), // Assuming start and end dates are provided in a valid format
                    end: Date.parse(project.planEndDate),
                    color: 'rgba(124, 181, 236, 0.8)'
                }, {
                    //name: 'Actual',
                    start: Date.parse(project.actualStartDate),
                    end: Date.parse(project.actualEndDate),
                    color: 'rgba(255, 0, 0, 0.8)'
                }]
            })),
        });
    }

    fetchCurrentUser().then(user=>{
    getAllProjects(user)
        .then( projects =>{
            showGanttChart(projects);
            showDepartmentCharts(projects);
        });

    })
    async function getAllDepartments(){
        return await fetch("/departments", function (){})
            .then(response => response.json())
            .then(departments => {
                return departments;
            })
            .catch(err => {
                return Promise.reject(0);
            });
    }


    function renderPieChart(projects,departments) {
        let data = [];
        departments.map(department=>{
            let tmp = {
                name:department.name,
                y:calculateDepartmentPercentage(projects,department),
            }
            data.push(tmp);
        });
        Highcharts.chart('pie-chat-container', {
            chart: {
                type: 'pie'
            },
            title: {
                text: 'Project distribution across departments'
            },
            tooltip: {
                valueSuffix: '%'
            },
            plotOptions: {
                series: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: [{
                        enabled: true,
                        distance: 20
                    }, {
                        enabled: true,
                        distance: -40,
                        format: '{point.percentage:.1f}%',
                        style: {
                            fontSize: '1.2em',
                            textOutline: 'none',
                            opacity: 0.7
                        },
                        filter: {
                            operator: '>',
                            property: 'percentage',
                            value: 10
                        }
                    }]
                }
            },
            series: [
                {
                    name: 'Percentage',
                    colorByPoint: true,
                    data,
                }
            ]
        });
    }

    function calculateDepartmentDataArray(projects, departments, status){
        let data = [];
        departments.map(department=>{
            const projectsByDepartment = projects.filter(project => project.department.id === department.id);
            const projectsByStatus = projectsByDepartment.filter(project => project.status === status );
            data.push(projectsByStatus.length);
        });
        return data;
    }

    function getBarChartSeriesData(projects, departments, statusName) {
        let series = [];
        statusName.map(status => {
            let temp = {
                name: status,
                data: calculateDepartmentDataArray(projects, departments, status)
            }
            series.push(temp);
        })
        return series;
    }


    function renderBarChart(projects, departments){
        let statusName = ['TODO', 'INPROGRESS', 'PENDING','COMPLETED']
        let categories = departments.map(department => (department.name));

        Highcharts.chart('bar-chart-container', {
            chart: {
                type: 'bar'
            },
            title: {
                text: 'Project Status By Each Department',
            },
            xAxis: {
                categories,
                title: {
                    text: null
                },
                gridLineWidth: 1,
                lineWidth: 0
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Number of Projects',
                    align: 'high'
                },
                labels: {
                    overflow: 'justify'
                },
                gridLineWidth: 0
            },

            plotOptions: {
                bar: {
                    borderRadius: '50%',
                    dataLabels: {
                        enabled: true
                    },
                    groupPadding: 0.1
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'top',
                x: -40,
                y: 80,
                floating: true,
                borderWidth: 1,
                backgroundColor:
                    Highcharts.defaultOptions.legend.backgroundColor || '#FFFFFF',
                shadow: true
            },
            credits: {
                enabled: false
            },
            series: getBarChartSeriesData(projects, departments, statusName)
        });
    }

});


