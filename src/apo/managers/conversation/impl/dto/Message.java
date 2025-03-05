package apo.managers.conversation.impl.dto;

import apo.managers.conversation.IMessage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Message implements IMessage
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    private Conversation conversation;

    private int author_id;
    private String text;
    
    public Message() {}	
    
	@Override
	public Object getScopeData(int scope_id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	
	
}
