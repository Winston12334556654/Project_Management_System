let projectList;
let months = ['Jan', 'Feb', 'Mar', 'April', 'May', 'June','July','August','Sept','Oct','Nov','Dec'];
document.addEventListener('DOMContentLoaded', function () {

    async function getAllProjects(){
        return await fetch("/all-project", function (){})
            .then(response => response.json())
            .then(projects => {
                console.log("All Projects: "+ projects);
                projectList = projects;
                return projects;
            })
            .catch(err => {
                return Promise.reject(0);
            });
    }

    function getMonthNumber(dateString) {
        let date = new Date(dateString);
        return date.getMonth();
    }

    getAllProjects().then(projects =>{
        console.log("All Projects: "+ projects);
        let monthsByProjects=[];

        for(let i=0;i<months.length;i++){
            let tCount = 0;
            projects.map(project=>{
                if(getMonthNumber(project.planStartDate)===i){
                    tCount++;
                }
            });
            monthsByProjects.push(tCount);
        }

        renderProjectDateBarChart(monthsByProjects);
    })



    function  renderProjectDateBarChart(data) {
        // let projArr = [12, 10, 25, 30, 50, 45, 35, 20, 60, 70, 80, 90];
        Highcharts.chart('projectChart', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Annual Project Amount',
                align: 'center'
            },

            xAxis: {
                categories:months,
                crosshair: true,
                accessibility: {
                    description: 'Months'
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Project Count'
                }
            },
            tooltip: {
                valueSuffix: ' (1000 MT)'
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [
                {
                    name: 'Months',
                    data,
                },

            ]
        });
    }



});