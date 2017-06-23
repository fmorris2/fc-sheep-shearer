package scripts.fc.missions.fcsheepshearer.tasks;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;

import scripts.fc.api.interaction.impl.npcs.dialogue.NpcDialogue;
import scripts.fc.api.travel.Travel;
import scripts.fc.framework.task.Task;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fcsheepshearer.data.QuestStage;

public class CompleteQuest extends Task
{
	private final int DISTANCE_THRESHOLD = 2;
	
	@Override
	public boolean execute()
	{
		if(Player.getPosition().distanceTo(FCSheepShearer.FARMER_TILE) > DISTANCE_THRESHOLD)
			Travel.webWalkTo(FCSheepShearer.FARMER_TILE);
		else
		{
			while(Inventory.getCount("Ball of wool") > 0 && Game.getSetting(FCSheepShearer.QUEST_SETTING_INDEX) != QuestStage.COMPLETED.getSetting())
			{
				General.println("Turning in quest...");
				NpcDialogue dialogue = new NpcDialogue("Talk-to", "Fred the Farmer", 10, 0);
				dialogue.setCheckPath(true);
				dialogue.execute();
				General.sleep(100);
			}
		}
		
		return false;	
	}

	@Override
	public boolean shouldExecute()
	{
		return Inventory.getCount("Ball of wool") >= 20;
	}

	@Override
	public String getStatus()
	{
		return "Complete quest";
	}

}
