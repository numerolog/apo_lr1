package apo.managers.conversation.impl.dto;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;

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
	
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Expose(serialize=false, deserialize=false)
    @ManyToOne
    private Conversation conversation;

    @Expose(serialize=false, deserialize=false)
//TODO:    @Expose
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MessageFlag> flags = new HashSet<>();
    
    @Expose
    private int author_id;
    
    @Expose
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

	@Override
	public Object getScopeData(int scope_id) 
	{
		return this;
	}

	public Set<MessageFlag> getFlags() 
	{
		return flags;
	}
	
}
