package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.dto.ProjectDTO;
import ai.group2.project_management_system.dto.UserDTO;
import ai.group2.project_management_system.mapping.UserMapping;
import ai.group2.project_management_system.model.Enum.Role;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.*;
import ai.group2.project_management_system.repository.ProjectRepository;
import ai.group2.project_management_system.service.DepartmentService;
import ai.group2.project_management_system.service.Impl.ProjectServiceImpl;
import ai.group2.project_management_system.service.ProjectService;
import ai.group2.project_management_system.service.UserService;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectAPI {

    private final ProjectService projectService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final ProjectRepository projectRepository;

    private final UserMapping userMapping;

    @GetMapping("/departments-selector")
    public ResponseEntity<List<Department>> selectDepartment() {
        System.out.println("department is gone");
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/members-selector/{departmentId}")
    public ResponseEntity<List<UserDTO>> getUsers(@PathVariable Long departmentId) {
        System.out.println("Users are gone");
        User currentUser = userService.getCurrentUser();
        List<UserDTO> users = userService.getUsersByDepartmentId(departmentId); // Assuming this returns List<User>
        users.removeIf(user -> user.getId().equals(currentUser.getId()));
        System.out.println("member-selector " + users);
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/members-selection/{departmentId}")
    public ResponseEntity<List<User>> getMembers(@PathVariable Long departmentId){
        System.out.println("Users are gone : ");
        List<User> users = userService.getMembersByDepartmentId(departmentId);
        users = users.stream()
                .filter(user -> !user.getRole().equals(Role.PM) && !user.getRole().equals(Role.PMO) && !user.getRole().equals(Role.TEAMLEADER))
                .collect(Collectors.toList());
        System.out.println(users);
        users.forEach(user -> {
            String profile = userService.getUserPhotoById(user.getId());
            user.setProfilePictureFileName(profile);
            System.out.println("User isActive: " + user.isActive());
        });
        return ResponseEntity.ok(users);
    }

    @GetMapping("/teamLeader-selection/{departmentId}")
    public ResponseEntity<List<User>> getTeamLeaders(@PathVariable Long departmentId){
        System.out.println("Users are gone : ");
        List<User> teamLeaders = userService.getMembersByDepartmentId(departmentId);
        teamLeaders = teamLeaders.stream()
                .filter(user -> !user.getRole().equals(Role.PM) && !user.getRole().equals(Role.PMO) && !user.getRole().equals(Role.MEMBER))
                .collect(Collectors.toList());
        System.out.println(teamLeaders);
        teamLeaders.forEach(user -> {
            String profile = userService.getUserPhotoById(user.getId());
            user.setProfilePictureFileName(profile);
            System.out.println("User isActive: " + user.isActive());
        });
        return ResponseEntity.ok(teamLeaders);
    }


    // This method is for creating new projects
    @PostMapping("/add-project")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {

        System.out.println("Here we go");
        project.setIsActive(true);
        System.out.println(project);

        List<Long> userIds = project.getUserIds();
        User currentUser = userService.getCurrentUser();
        project.setCreator(currentUser.getName());
        userIds.add(currentUser.getId());

        List<User> users = userService.findUsersByIds(userIds);
        project.setUsers(new HashSet<>(users));
        project.setStatus(Status.TODO);
        Project newProject = projectService.save(project);
        return ResponseEntity.ok(newProject);
    }

    //These methods are for selecting back and display back the project list
    @GetMapping("/show-projects")
    public ResponseEntity<List<Project>> getActiveProjects() {
        List<Project> projects = projectService.getAllProjectsWithUsers();
        List<Project> activeProjectsByUser=new ArrayList<Project>();
        for(Project project:projects){
            if(project != null && project.getStatus().equals(Status.COMPLETED)){
                if(project.getPlanEndDate().isBefore(project.getActualEndDate())){
                    project.setOverDue(true);
                    projectRepository.save(project);
                }
            }else  {
                if( project.getPlanEndDate().isBefore(LocalDate.now())){
                    project.setOverDue(true);
                    projectRepository.save(project);
                }
            }
        }

        /*if(userService.getCurrentUser().getRole()==Role.PMO || userService.getCurrentUser().getRole()==Role.PM){
            return ResponseEntity.ok(activeProjects);
        }else {
            List<Project> userProjects=projectRepository.findProjectsByUserId(userService.getCurrentUser().getId());
            //System.out.println("Project Size:"+teamLeaderProjects.size());
            for(Project project:userProjects){
                if(project.isActive()){
                    activeProjectsByUser.add(project);
                }
            }
            return ResponseEntity.ok(activeProjectsByUser);
        }*/
        List<Project> projects1 = projects.stream()
                .sorted(Comparator.comparingLong(Project::getId).reversed())
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects1);
    }

//    @GetMapping("/show-inactive-projects")
//    public ResponseEntity<List<Project>> getInactiveProjects() {
//        List<Project> projects = projectService.getAllProjectsWithUsers();
//
//        List<Project> inactiveProjects = projects.stream()
//                .filter(project -> !project.isActive())
//                .collect(Collectors.toList());
//        System.out.println(inactiveProjects);
//        return ResponseEntity.ok(inactiveProjects);
//    }


    @PutMapping("/edit-project/{id}")
    public ResponseEntity<Project> editProject(@PathVariable("id") Long projectId, @RequestBody Project project){
        System.out.println("we reach edit mapping!");

       Project pj = projectService.getProjectBy_Id(projectId);
        if (pj != null) {

            pj.setTitle(project.getTitle());
            pj.setObjective(project.getObjective());
            pj.setCreator(project.getCreator());
            pj.setDescription(project.getDescription());
            pj.setCategory(project.getCategory());
            pj.setStatus(project.getStatus());
            pj.setPriority(project.getPriority());
            pj.setPlanStartDate(project.getPlanStartDate());
            pj.setPlanEndDate(project.getPlanEndDate());

            Project latestUser = projectService.save(pj);
            return ResponseEntity.ok(latestUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/member")
    public ResponseEntity<List<Long>> getUserIdsByProjectId(@RequestParam Long projectId) {
        System.out.print("Get Mapping"+ projectId);
        List<Long> userIds = projectService.getUserIdsByProjectId(projectId);
        System.out.println(userIds);
        return new ResponseEntity<>(userIds, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsersByIds(@RequestParam("userIds") List<Long> userIds) {
        List<User> users = userService.findUsersByIds(userIds);
        users = users.stream()
                .filter(user -> !user.getRole().equals(Role.PM) && !user.getRole().equals(Role.PMO))
                .collect(Collectors.toList());
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //These methods are for editing the data
    @GetMapping("/project-selector/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        System.out.println("Start Update"+ projectId);
        Project project = projectService.getProjectBy_Id(projectId);
        System.out.println(project);
        if (project != null) {
            return ResponseEntity.ok().body(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/project/{projectId}")
    public ResponseEntity<String> updateProjectStatus(@PathVariable("projectId") Long projectId,
                                                    @RequestBody Project requestProject ) {
        //   String newStatus= String.valueOf(requestAssignIssue.getStatus());
        Project project = projectRepository.findById(projectId).orElse(null);

        if (project != null) {
            project.setStatus(requestProject.getStatus());
            project.setActualEndDate(LocalDate.now());
            projectRepository.save(project);
            return ResponseEntity.ok(String.format("Issue %d status updated to %s", project, requestProject.getStatus()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user-for-project-update/{departmentId}")
    public ResponseEntity<List<UserDTO>> getUsersForProject(@PathVariable Long departmentId) {
        System.out.println("Updaters are gone");
        List<UserDTO> users = userService.getUsersByDepartmentId(departmentId);
        System.out.println(users);
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    // This is to delete projects
    @PutMapping("/updateStatusForProject/{id}")
    public ResponseEntity<Project> updateStatusForProject(@PathVariable("id") Long projectId){
        Project project = projectService.getProjectBy_Id(projectId);
        System.out.println("Project id : " + projectId);
        System.out.println("Project status : " + project.isActive());

        if(project != null){
            project.setActive(!project.isActive());
            Project updatedProject = projectService.save(project);
            System.out.println("Project later ststus : "+ project.isActive());
            return ResponseEntity.ok(updatedProject);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/all-project-ids")
    public ResponseEntity<List<Long>> getAllProjectIds() {
        List<Long> projectIds = projectService.getAllProjectIds();
        return ResponseEntity.ok(projectIds);
    }
    @GetMapping("/project-name/{projectId}")
    public ResponseEntity<String> getProjectNameById(@PathVariable Long projectId) {
        Project project = projectService.getProjectBy_Id(projectId);

        if (project != null) {
            return ResponseEntity.ok(project.getTitle()); // Assuming the project name is stored in the 'title' field
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
