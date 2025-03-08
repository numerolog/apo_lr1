package apo.managers.conversation.impl.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import apo.managers.conversation.IConversation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Conversation implements IConversation 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
	public int owner_user_id;

    @Column(nullable = false)
	public String name;
    
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ConversationMember> members = new HashSet<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();
    
    public Conversation() 
    {
    	;
    }   
	
	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
//todo: убрать
	public int getOwner_user_id() 
	{
		return owner_user_id;
	}

	@Override
	public int getOwnerId() 
	{
		return getOwner_user_id();
	}

	public void setOwner_user_id(int owner_user_id) 
	{
		this.owner_user_id = owner_user_id;
	}

	public Set<ConversationMember> getMembers() 
	{
		return members;
	}

	public Set<Integer> getMembersId() 
	{
		return getMembers().stream().map(ConversationMember::getUser_id).collect(Collectors.toSet());
	}

	public void setMembers(Set<ConversationMember> members) 
	{
		this.members = members;
	}

	public Collection<Message> getMessages() 
	{
		return messages;
	}

}


