package watsonFrameBuilder;

import java.util.ArrayList;

public class WatsonFrame {
	private String date;
	private String projectName;
	private ArrayList<String> tags;
	private String startTime;
	private String endTime;
	
	
	public WatsonFrame(String date, String projectName, ArrayList<String> tags, String startTime, String endTime) {
		super();
		this.date = date;
		this.projectName = projectName;
		this.tags = tags;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	
	public StringBuilder getWatsonFormat()
	{
		//Formating date
		String[] dateParts = date.split("\\.");
		StringBuilder formatedDate = new StringBuilder();
		formatedDate.append(dateParts[2]);
		formatedDate.append("-");
		formatedDate.append(dateParts[1]);
		formatedDate.append("-");
		formatedDate.append(dateParts[0]);

		
		StringBuilder line = new StringBuilder();
		line.append("watson add --from \"");
		line.append(formatedDate);
		line.append(" ");
		line.append(startTime);
		line.append("\" --to \"");
		line.append(formatedDate);
		line.append(" ");
		line.append(endTime);
		line.append("\" ");
		line.append(projectName);
		line.append(" ");
		
		for(String tag : tags)
		{
			line.append(tag + " ");
		}
		
		return line;
	}

	//Checks date and time formats
	public boolean formatCheck()
	{
		if(!date.matches("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}\\."))
		{
			System.out.println("Date is not in proper format");
			return false;
		}
		
		
		if(!startTime.matches("[0-9]{2}:[0-9]{2}"))
		{
			System.out.println("Start time is not in proper format");
			return false;
		}
		
		if(!endTime.matches("[0-9]{2}:[0-9]{2}"))
		{
			System.out.println("End time is not in proper format");
			return false;
		}
		
		
		return true;
	}
	
	public boolean exceedsToNextDay()
	{
		//Parse start and end time
		String[] startParts = startTime.split(":");
		String[] endParts = endTime.split(":");
		
		int startH = Integer.parseInt(startParts[0]);
		int startMin = Integer.parseInt(startParts[1]);
		
		
		int endH = Integer.parseInt(endParts[0]);
		int endMin = Integer.parseInt(endParts[1]);
		
		if(startH < endH)
		{
			return false;
		}
		else if(startH == endH)
		{
			if(startMin < endMin)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return true;
	}
		
		
	}
	
	
}












