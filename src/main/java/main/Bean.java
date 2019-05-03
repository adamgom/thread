package main;

import java.util.Date;

public class Bean {
	private int consumerID;
	private Date consumerWorkDate;
	private String dataNo;
	
	public Bean(int consumerID, Date consumerWorkDate, String dataNo) {
		this.consumerID = consumerID;
		this.consumerWorkDate = consumerWorkDate;
		this.dataNo = dataNo;
	}

	public int 		getConsumerID() 		{return consumerID;}
	public Date 	getConsumerWorkDate() 	{return consumerWorkDate;}
	public String 	getDataNo() 			{return dataNo;}
}
