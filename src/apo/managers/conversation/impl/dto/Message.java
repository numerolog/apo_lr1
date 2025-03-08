package apo.managers.conversation.impl.dto;

import java.util.HashSet;
import java.util.Set;

import apo.managers.conversation.IMessage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Message implements IMessage
{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Conversation conversation;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<MessageFlag> flags = new HashSet<>();
    
    private int author_id;
    private String text;
    
    public Message() 
    {
    	;
    }	
    	
	public int getId() 
	{
		return id;
	}

	public int getAuthor_id() 
	{
		return author_id;
	}

	public void setAuthor_id(int author_id) 
	{
		this.author_id = author_id;
	}

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}

	public void setConversation(Conversation conversation) 
	{
		this.conversation = conversation;
	}

	public Set<MessageFlag> getFlags() 
	{
		return flags;
	}
	
}
