package apo.managers.conversation.impl.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

// реакция или флаг о прочтении
@Entity
public class MessageFlag 
{

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    public Message message;

    @Column(nullable = false)
	public int user_id;

    // 0 - сообщение прочитано, 1 - отредактировано(unused), числа после первого кода емодзи - коды емодзи, остальное - резерв
    public static final int READEN_FLAG = 0;
    
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


