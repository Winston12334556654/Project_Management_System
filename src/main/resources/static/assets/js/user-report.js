let teamLeaderList ;
let issueList ;
let memberList;
let subIssueList;
let filterMembers;

document.addEventListener('DOMContentLoaded', function () {



    function  dropDownChangeListenerForTeamLeader(event,containerId){
        const departmentId = event.target?event.target.value:event;

        // Fetch team leaders and render pie chart when department selection changes
        getTeamleader(departmentId).then(teamLeaders => {
            teamLeaderList = teamLeaders;
            // Clear issue list before fetching new data
            issueList = [];

            teamLeaders.forEach(teamLeader => {
                getIssueByTeamLeader(teamLeader.id).then(issues => {
                    // Merge issues from different team leaders
                    issueList = [...issues];

                    if(event.target){
                        switch (event.target.id){
                            case 'department-select-input1':renderPieChart(issueList, teamLeaderList,'teamleader-pie-chart-container');break;
                            case 'department-select-input2':renderBarChart(issueList,teamLeaderList,'teamleader-bar-chart-container');break;
                            default:
                        }
                    }else {
                        if(containerId==='department-select-input1'){
                            renderPieChart(issueList,teamLeaderList,'teamleader-pie-chart-container');
                        }else{
                            renderBarChart(issueList,teamLeaderList,'teamleader-bar-chart-container');
                        }
                    }
                });
            });
        });
    }

    // async function dropDownChangeListenerForTeamLeader(event, containerId) {
    //     const departmentId = event.target ? event.target.value : event;
    //
    //     try {
    //         const teamLeaders = await getTeamleader(departmentId);
    //         const teamLeaderList = teamLeaders;
    //
    //         // Clear issue list before fetching new data
    //         let issueList = [];
    //
    //         // Array to store promises for fetching issues
    //         const issuePromises = teamLeaders.map(teamLeader => {
    //             return getIssueByTeamLeader(teamLeader.id).then(issues => {
    //                 // Merge issues from different team leaders
    //                 issueList.push(...issues);
    //             });
    //         });
    //
    //         // Wait for all issue fetching promises to resolve
    //         await Promise.all(issuePromises);
    //
    //         if (event.target) {
    //             switch (event.target.id) {
    //                 case 'department-select-input1':
    //                     renderPieChart(issueList, teamLeaderList, 'teamleader-pie-chart-container');
    //                     break;
    //                 case 'department-select-input2':
    //                     renderBarChart(issueList, teamLeaderList, 'teamleader-bar-chart-container');
    //                     break;
    //                 default:
    //             }
    //         } else {
    //             if (containerId === 'department-select-input1') {
    //                 renderPieChart(issueList, teamLeaderList, 'teamleader-pie-chart-container');
    //             } else {
    //                 renderBarChart(issueList, teamLeaderList, 'teamleader-bar-chart-container');
    //             }
    //         }
    //     } catch (error) {
    //         console.error("Error:", error);
    //     }
    // }


    function  dropDownChangeListenerForMember(event,containerId){
        const departmentId = event.target?event.target.value:event;

        // Fetch team leaders and render pie chart when department selection changes
        getMembers(departmentId).then(members => {
            memberList = members;
            // Clear issue list before fetching new data
            subIssueList = [];
            members.forEach(member => {
                getSubIssueByMember(member.id).then(subIssues => {
                    // Merge issues from different team leaders
                    subIssueList = subIssues;

                    if(event.target){
                        switch (event.target.id){
                            case 'department-select-input3':renderPieChart(subIssueList, memberList,'member-pie-chart-container');break;
                            case 'department-select-input4':renderBarChart(subIssueList,memberList,'member-bar-chart-container');break;
                            default:
                        }
                    }else {
                        if(containerId === 'department-select-input3'){
                            renderPieChart(subIssueList,memberList,'member-pie-chart-container');
                        }else{
                            renderBarChart(subIssueList,memberList,'member-bar-chart-container');
                        }
                    }
                });
            });
        });
    }


    // Fetch department dropdown and set up event listener
    showDepartmentDropdown('department-select-input1').then(() => {
        document.getElementById('department-select-input1').addEventListener('change', (event) => {dropDownChangeListenerForTeamLeader(event,'department-select-input1')});
    });

    showDepartmentDropdown('department-select-input2').then(() => {
        document.getElementById('department-select-input2').addEventListener('change', (event) => {dropDownChangeListenerForTeamLeader(event,'department-select-input2')});
    });

    showDepartmentDropdown('department-select-input3').then(() => {
        document.getElementById('department-select-input3').addEventListener('change', (event) => {dropDownChangeListenerForMember(event,'department-select-input3')});
    });

    showDepartmentDropdown('department-select-input4').then(() => {
        document.getElementById('department-select-input4').addEventListener('change', (event) => {dropDownChangeListenerForMember(event,'department-select-input4')});
    });


    async function showDepartmentDropdown(inputElement) {
        return await fetch('/departments')
            .then(response => response.json())
            .then(data => {
                console.log('All department list: ', data);
                const selectElement = document.getElementById(inputElement);
                selectElement.innerHTML = '';
                let i = 1;
                data.forEach(department => {
                    const option = document.createElement('option');
                    option.value = department.id;
                    option.textContent = department.name;
                    if(i===1){
                        if(inputElement==='department-select-input1' || inputElement === 'department-select-input2'){
                            dropDownChangeListenerForTeamLeader(department.id,inputElement);
                        }else{
                            dropDownChangeListenerForMember(department.id,inputElement);
                        }
                    }
                    i++;
                    selectElement.appendChild(option);
                });

            })
            .catch(error => {
                console.log('Error: ', error);
            });
    }

    async function getTeamleader(departmentId){
        //const departmentId = document.getElementById('department-select-input1').value;
        return await fetch(`/teamLeader-selection/${departmentId}`)
            .then(response => response.json())
            .then(data => {
                console.log('Teamleader list: ', data);
                return data;
            })
            .catch(error => {
                console.log('Error: ', error);
                return Promise.reject(0);
            });
    }

    async function getIssueByTeamLeader(teamLeaderId){
        return await fetch(`/get-issues-by-teamLeaderId/${teamLeaderId}`)
            .then(response => response.json())
            .then(data => {
                console.log('Teamleader list: ', data);
                return data;
            })
            .catch(error => {
                console.log('Error: ', error);
                return Promise.reject(0);
            });
    }

    async function getMembers(departmentId){
        //const departmentId = document.getElementById('department-select-input1').value;
        return await fetch(`/members-selector/${departmentId}`)
            .then(response => response.json())
            .then(members => {
                console.log('Member list: ', members);
                filterMembers = members.filter(member => member.role ==='MEMBER')
                console.log('filter Member list: ', filterMembers);
                return filterMembers;
            })
            .catch(error => {
                console.log('Error: ', error);
                return Promise.reject(0);
            });
    }

    async function getSubIssueByMember(memberId){
        return await fetch(`/get-all-subIssues/${memberId}`)
            .then(response => response.json())
            .then(data => {
                console.log('SubIssue list: ', data);
                return data;
            })
            .catch(error => {
                console.log('Error: ', error);
                return Promise.reject(0);
            });
    }

    function calculateIssuePercentage(issues,teamLeader){
        const filterData = issues.filter(issue => issue.teamLeaderId === teamLeader.id);
        return 100*(filterData.length/issues.length);
    }

    function renderPieChart(issues,teamLeaders,containerId) {
        let data = [];
        teamLeaders.map(teamLeader=>{
            let tmp = {
                name:teamLeader.name,
                y:calculateIssuePercentage(issues,teamLeader),
            }
            data.push(tmp);
        });

        console.log("team pie chart:: data ",data);

        Highcharts.chart(containerId, {
            chart: {
                type: 'pie'
            },
            title: {
                text: ''
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

    function calculateTeamLeaderDataArray(issues, teamLeaders, status){
        let data = [];
        teamLeaders.map(teamLeader=>{
            const issuesByTeamLeader = issues.filter(issue => issue.teamLeaderId === teamLeader.id);
            const issuesByStatus = issuesByTeamLeader.filter(issue => issue.status === status );
            data.push(issuesByStatus.length);
        });
        return data;
    }

    function getBarChartSeriesData(issues, teamLeaders, statusName) {
        let series = [];
        statusName.map(status => {
            let temp = {
                name: status,
                data: calculateTeamLeaderDataArray(issues, teamLeaders, status)
            }
            series.push(temp);
        })
        return series;
    }

    function renderBarChart(issues, teamLeaders,containerId){
        let statusName = ['TODO', 'INPROGRESS', 'PENDING','COMPLETED']
        let categories = teamLeaders.map(teamLeader => (teamLeader.name));

        Highcharts.chart(containerId, {
            chart: {
                type: 'bar'
            },
            title: {
                text: ''
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
            series: getBarChartSeriesData(issues, teamLeaders, statusName)
        });
    }

});