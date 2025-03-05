package apo.managers.conversation.impl.dto;

import com.google.gson.annotations.Expose;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

// реакция или флаг о прочтении
@Entity
public class MessageFlag 
{

    @Expose
    @Id
    @GeneratedValue
    private int id;

    @Expose(serialize=false, deserialize=false)
    @OneToOne
    public Message message;

    @Expose
    @Column(nullable = false)
	public int user_id;

    // 0 - сообщение прочитано, все остальное - код емодзи
    public static final int READEN_FLAG = 0;
    
    @Expose
    @Column(nullable = false)
	public int flag = READEN_FLAG;
    
    @Column(nullable = false)
    public LocalDateTime timestamp = LocalDateTime.now();
    
    public MessageFlag() 
    {
    	;
    }
    
	public MessageFlag(int user_id) 
	{
		super();
		this.user_id = user_id;
	}

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
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


