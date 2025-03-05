package apo.managers.conversation.impl.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.annotations.Expose;

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

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Expose
    @Column(nullable = false)
	public int owner_user_id;

    @Expose(serialize=false, deserialize=false)
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ConversationMember> members = new HashSet<>();

    @Expose(serialize=false, deserialize=false)
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

	public int getOwner_user_id() 
	{
		return owner_user_id;
	}

	public void setOwner_user_id(int owner_user_id) 
	{
		this.owner_user_id = owner_user_id;
	}

	public Set<ConversationMember> getMembers() 
	{
		return members;
	}

	public void setMembers(Set<ConversationMember> members) 
	{
		this.members = members;
	}

	public Collection<Message> getMessages() 
	{
		return messages;
	}

	@Override
	public Object getScopeData(int scope_id) 
	{
		return this;
	}

}


