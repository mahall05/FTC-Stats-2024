package Team;

import java.util.ArrayList;

public class Group{
    protected TeamMember[] members;
    
    public Group(Object[] members){
        this.members = new TeamMember[members.length];
        for(int i = 0; i < members.length; i++){
            this.members[i] = (TeamMember) members[i];
        }
    }

    public TeamMember[] getMembers(){
        return members;
    }
}
