let projectList=[];
let issueList = [];
let dataReady = false;
let loginUser;
document.addEventListener('DOMContentLoaded', function () {

    init_fetch_data();

    console.log("render pie chart",issueList);

    function init_fetch_data(){
        dataReady = false;
        fetchCurrentUser().then(user=>{
            getAllProjects(user).then(projects=>{
                getAllIssues().then(issues=>{
                    console.log(issues);
                    console.log(filterRelatedIssues(projects,issues));
                    let filterIssues = filterRelatedIssues(projects,issues);
                    renderPieChart(filterIssues,projects);
                    renderBarChart(filterIssues, projects);
                    showGanttChart(filterIssues);
                })
            })
        });
    }

    function filterRelatedIssues(projects,issues){
        let filterIssues = [];
        projects.map(project=>{
            filterIssues = [...filterIssues,...issues.filter(issue=>issue.project.id === project.id)];
        });
        return filterIssues;
    }

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
            creatorName = user.name;
        }
        return await fetch(`/get-all-projects/${creatorName}`, function (){})
            .then(response => response.json())
            .then(projects => {
                projectList = projects;
                return projects;

            })
            .catch(err => {
                return Promise.reject(0);
            });
    }


    async function getAllIssues(){
        return await fetch(`/issueList`, function (){})
            .then(response => response.json())
            .then(issues => {
                return issues;
            })
            .catch(err => {
                return Promise.reject(0);
            });
    }

    async function getAllIssuesByProjectId(projectId){
        return await fetch(`/${projectId}/issues`, function (){})
            .then(response => response.json())
            .then(issues => {
                return issues;
            })
            .catch(err => {
                return Promise.reject(0);
            });
    }

    function calculateIssuesPercentage(issues,project){
        const filterData = issues.filter(issue => issue.project.id === project.id);
        return 100*(filterData.length/issues.length);
    }


    function renderPieChart(issues,projects) {
        let data = [];
        projects.map(project=>{
            let tmp = {
                name:project.title,
                y:calculateIssuesPercentage(issues,project),
            }
            data.push(tmp);
        });
        Highcharts.chart('issue-pie-chat-container', {
            chart: {
                type: 'pie'
            },
            title: {
                text: 'Issues distribution across Individual Projects'
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

    function calculateProjectsDataArray(issues, projects, status){
        let data = [];
        projects.map(project=>{
            const issueByProject = issues.filter(issue => issue.project.id === project.id);
            const issueByStatus = issueByProject.filter(issue => issue.status === status );
            data.push(issueByStatus.length);
        });
        return data;
    }

    function getBarChartSeriesData(issues, projects, statusName) {
        let series = [];
        statusName.map(status => {
            let temp = {
                name: status,
                data: calculateProjectsDataArray(issues, projects, status)
            }
            series.push(temp);
        })
        return series;
    }

    function renderBarChart(issues, projects){
        let statusName = ['TODO', 'INPROGRESS', 'PENDING','COMPLETED']
        let categories = projects.map(project => (project.title));

        Highcharts.chart('issue-bar-chart-container', {
            chart: {
                type: 'bar'
            },
            title: {
                text: 'Issue Status across Each Project',
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
                    text: 'Number of Issues',
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
            series: getBarChartSeriesData(issues, projects, statusName)
        });
    }

    function showGanttChart(data){
        Highcharts.ganttChart('gantt-chat-container', {
            title: {
                text: 'Issues Timeline'
            },
            xAxis: {
                currentDateIndicator: true,
                min: Date.UTC(2024, 0, 1),
                max: Date.UTC(2024, 11, 31)
            },
            series: data.map(issue => ({
                name: issue.title,
                data: [{
                    name: `${issue.title}
                    (${issue.project.title})`,
                    start: Date.parse(issue.planStartDate), // Assuming start and end dates are provided in a valid format
                    end: Date.parse(issue.planDueDate),
                    color: 'rgba(124, 181, 236, 0.8)'
                }, {
                    //name: 'Actual',
                    start: Date.parse(issue.actualStartDate),
                    end: Date.parse(issue.actualDueDate),
                    color: 'rgba(255, 0, 0, 0.8)'
                }]
            })),
        });
    }

})