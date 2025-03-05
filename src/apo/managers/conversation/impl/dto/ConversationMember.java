package apo.managers.conversation.impl.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
public class ConversationMember 
{

    @Id
    @GeneratedValue
    private int id;
    
    @OneToOne
    public Conversation conversation;
    
    @Column(nullable = false)
	public int user_id;
    
    public ConversationMember() 
    {
    	
    }
    
	public ConversationMember(int user_id) 
	{
		super();
		this.user_id = user_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() 
	{
		return user_id;
	}
	
	public void setUser_id(int user_id) 
	{
		this.user_id = user_id;
	}   
	 
}


