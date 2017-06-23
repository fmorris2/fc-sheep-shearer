package scripts.fc.missions.fcsheepshearer.tasks;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;

import scripts.fc.api.interaction.impl.npcs.dialogue.NpcDialogue;
import scripts.fc.api.travel.Travel;
import scripts.fc.framework.task.Task;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fcsheepshearer.data.QuestStage;

public class StartQuest extends Task
{
	private final int MAX_DIST_FROM_FARMER = 2;
	
	@Override
	public boolean execute()
	{
		if(Player.getPosition().distanceTo(FCSheepShearer.FARMER_TILE) > MAX_DIST_FROM_FARMER)
		{
			Camera.setCameraAngle(General.random(0, 30));
			Travel.webWalkTo(FCSheepShearer.FARMER_TILE);
		}
		else
			new NpcDialogue("Talk-to", "Fred the Farmer", 10, 0, 0, 0, 0).execute();
		
		return false;
	}

	@Override
	public boolean shouldExecute()
	{
		return Game.getSetting(FCSheepShearer.QUEST_SETTING_INDEX) == QuestStage.NOT_STARTED.getSetting()
				&& Inventory.getAll().length <= 7;
	}

	@Override
	public String getStatus()
	{
		return "Start Quest";
	}

}
