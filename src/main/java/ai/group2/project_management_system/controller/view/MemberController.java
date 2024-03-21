package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.dto.IssueDetailsDto;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.AssignIssue;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.AssignIssueRepository;
import ai.group2.project_management_system.service.AssignIssueService;
import ai.group2.project_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final UserService userService;
    private final AssignIssueService assignIssueService;
    private final AssignIssueRepository assignIssueRepository;
    @ModelAttribute("user")
    public User getUserFromSession(HttpSession session) {
        User user = userService.getCurrentUser();
        return user;
    }

    @GetMapping("member-issuelist")
    public String showMemberIssueList(Model model){
        var user = userService.getCurrentUser();
        List<AssignIssue> assignIssues=assignIssueService.getAssignIssuesByMemberId(user.getId());
        System.out.println("Assign Issues:"+assignIssues.size());
        model.addAttribute("assignIssues",assignIssues);
        return "member-issuelist";
    }

    @GetMapping("/member-issuedetails/{id}")
    public String ViewIssueDetails(@PathVariable Long id, Model model) {
        AssignIssue assignIssue=assignIssueService.getAssignIssueDetailsByAssignIssueId(id);
            model.addAttribute("assignIssue",assignIssue);
        return "member-issuedetails";
    }


    @GetMapping("/member-issueboard")
    public String MemberIssueBoard(Model model) {
        int todoCount = 0;
        int  inprogressCount=0;
        int completedCount=0;
        var user = userService.getCurrentUser();
        Long memberId=user.getId();
        List<AssignIssue> memberIssues=assignIssueService.getAssignIssuesByMemberId(memberId);
        System.out.println("MemberIssues:"+memberIssues.size());
        for(AssignIssue assignIssue:memberIssues){
            if(assignIssue != null && assignIssue.getStatus().equals(Status.COMPLETED)){
                if(assignIssue.getPlanDueDate().isBefore(assignIssue.getActualDueDate())){
                    assignIssue.setOverDue(true);
                    assignIssueRepository.save(assignIssue);
                }
            }else  {
                if( assignIssue.getPlanDueDate().isBefore(LocalDate.now())){
                    assignIssue.setOverDue(true);
                    assignIssueRepository.save(assignIssue);
                }

            }
            if(assignIssue.getStatus()==Status.TODO){
                todoCount+=1;
            } else if (assignIssue.getStatus()==Status.INPROGRESS) {
                inprogressCount+=1;
            }else if(assignIssue.getStatus()==Status.COMPLETED){
                completedCount+=1;
            }else {
                System.out.println("It is not ok for counting");
            }

        }
        List<AssignIssue> currentMemberIssues=new ArrayList<AssignIssue>();
        for (int i = 0; i <memberIssues.size(); i++) {
            if (!memberIssues.get(i).getIssue().getProject().getStatus().equals(Status.COMPLETED)
            && memberIssues.get(i).getIssue().getProject().isIsActive()
            && memberIssues.get(i).getIssue().isActive()
            && memberIssues.get(i).isActive()) {
                currentMemberIssues.add(memberIssues.get(i));
            }
        }

        //   log.info("currnenIssues list -> {}",currentMemberIssues.size());

        model.addAttribute("todoCount", todoCount);
        model.addAttribute("inprogressCount", inprogressCount);
        model.addAttribute("completedCount", completedCount);
            model.addAttribute("currentIssues",currentMemberIssues);
        return "member-issueboard";
    }
}
