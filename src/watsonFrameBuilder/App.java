package watsonFrameBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class App {

	public static void main(String[] args) {
		
		if(args.length != 1 )
		{
			System.out.println("You must state input file!");
			return;
		}
		System.out.println("Reading from file: " + args[0]);
		
		
		
		ArrayList<WatsonFrame> frames = new ArrayList<WatsonFrame>();
		
		File input = new File(args[0]);
		try(FileReader fileReader = new FileReader(input);)
		{

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			//Used for correcting mistakes in input file
			int currentLine = 1;

			String currDate;
			currDate = bufferedReader.readLine();
			
			do
			{
				String frameLine;
				frameLine = bufferedReader.readLine();
				currentLine++;
				while(!frameLine.matches("[0-9].*")) //when it comes to some number first it means it is came to new date block
				{
					//Creating frame
					String[] frameParts = frameLine.split(" ");
					
					String projecName = frameParts[0];
					
					ArrayList<String> tags = new ArrayList<String>();
				
					//Iterate through all parts searching for tags - they start with +
					int i;
					for(i = 1 ; i < frameParts.length;i++)
					{
						if(frameParts[i].charAt(0) == '+')
						{
							tags.add(frameParts[i]);
						}
						else
							break;
					}
					
					//After tags we continue with times 
					String startTime = frameParts[i];
					i += 2;
					String endTime = frameParts[i];
					
					WatsonFrame newFrame  = new WatsonFrame(currDate, projecName, tags, startTime, endTime);
					
					if(newFrame.exceedsToNextDay())
					{
						//If frame exceeds to next day  make two consecutive frames
						WatsonFrame frame1  = new WatsonFrame(currDate, projecName, tags, startTime, "23:59");
						
						//Create next day date by incrementing it by one day
						DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
						LocalDate date = LocalDate.parse(currDate,formater);
						
						
						
						date = date.plusDays(1);
						
						//generate new date
						//It is still in dd.mm.yyyy format because it is converted to proper format later
						
						
						String newDate = date.format(formater);

						
						
						WatsonFrame frame2  = new WatsonFrame(newDate, projecName, tags, "00:00", endTime);

						if(frame1.formatCheck()) {
							frames.add(frame1);
						}
						else
						{
							System.out.println("Wrong format on line: " + currentLine);
							return;
						}

						if(frame2.formatCheck()) {
							frames.add(frame2);
						}
						else
						{
							System.out.println("Wrong format on line: " + currentLine);
							return;
						}
					}
					else
					{
						if(newFrame.formatCheck()) {
							frames.add(newFrame);
						}
						else
						{
							System.out.println("Wrong format on line: " + currentLine);
							return;
						}
					}
					
					
					
					frameLine = bufferedReader.readLine();
					currentLine++;
					if(frameLine == null)
						break;
				}
				currDate = frameLine;
				
				if(frameLine == null)
				{
					//Reached end of file
					break;
				}
			}while(true);
			
			
			
			//When all frames are made, we write to file "watsonFormated.sh" 
			//It is .sh so it can be immediately executed by shell and frames will be added to watson
			
			File output = new File("watsonFormated.sh");
			
			FileWriter fileOut = new FileWriter(output);
			
			for(WatsonFrame frame : frames)
			{
				fileOut.write(frame.getWatsonFormat()+"\n");
			}
			
			fileOut.close();
			
			System.out.println("Output written to file: watsonFormated.sh");
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
