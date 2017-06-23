package scripts.fc.missions.fcsheepshearer.tasks;

import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;

import scripts.fc.api.generic.FCConditions;
import scripts.fc.api.interaction.impl.grounditems.PickUpGroundItem;
import scripts.fc.api.travel.Travel;
import scripts.fc.framework.task.Task;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fcsheepshearer.data.QuestStage;

public class GrabShears extends Task
{
	private final int SHEARS_ID = 1735;
	private final int DISTANCE_THRESHOLD = 2;
	
	@Override
	public boolean execute()
	{
		if(Player.getPosition().distanceTo(FCSheepShearer.FARMER_TILE) > DISTANCE_THRESHOLD)
			Travel.webWalkTo(FCSheepShearer.FARMER_TILE);
		else
			grabShears();
		
		return false;
	}

	@Override
	public boolean shouldExecute()
	{
		return Game.getSetting(FCSheepShearer.QUEST_SETTING_INDEX) > QuestStage.NOT_STARTED.getSetting()
				&& Inventory.getCount(SHEARS_ID) == 0;
	}

	@Override
	public String getStatus()
	{
		return "Grab Shears";
	}
	
	private void grabShears()
	{
		if(new PickUpGroundItem("Shears").execute())
			Timing.waitCondition(FCConditions.inventoryChanged(Inventory.getAll().length), 3500);
	}

}
