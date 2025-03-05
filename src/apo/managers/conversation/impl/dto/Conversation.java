package apo.managers.conversation.impl.dto;

import java.util.HashSet;
import java.util.Set;

import apo.managers.conversation.IConversation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Conversation implements IConversation {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
	public int owner_user_id;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConversationMember> members = new HashSet<>();
    
    
    public Conversation() {}   
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwner_user_id() {
		return owner_user_id;
	}

	public void setOwner_user_id(int owner_user_id) {
		this.owner_user_id = owner_user_id;
	}

	public Set<ConversationMember> getMembers() {
		return members;
	}

	public void setMembers(Set<ConversationMember> members) {
		this.members = members;
	}

	@Override
	public Object getScopeData(int scope_id) {
		// TODO Auto-generated method stub
		return null;
	}

//	public void setMember(ConversationMember member) {
//		member.conversation = this;
//		members.add(member);
//	}
//    
// 
}


