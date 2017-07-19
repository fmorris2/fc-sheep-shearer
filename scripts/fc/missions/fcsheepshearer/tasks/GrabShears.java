package scripts.fc.missions.fcsheepshearer.tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;

import scripts.fc.api.abc.ABC2Reaction;
import scripts.fc.api.abc.PersistantABCUtil;
import scripts.fc.api.generic.FCConditions;
import scripts.fc.api.interaction.impl.grounditems.PickUpGroundItem;
import scripts.fc.api.travel.Travel;
import scripts.fc.framework.data.Vars;
import scripts.fc.framework.task.Task;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fcsheepshearer.data.QuestStage;

public class GrabShears extends Task
{
	private static final long serialVersionUID = -8819229787618161266L;
	
	private static final int EST_WAIT_TIME = 50000; //takes 50 secs to respawn
	private final int SHEARS_ID = 1735;
	private final int DISTANCE_THRESHOLD = 2;
	
	private ABC2Reaction reaction = new ABC2Reaction("grabShearsReaction", false, EST_WAIT_TIME);
	
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
		if(GroundItems.find("Shears").length == 0)
			reaction.start();
		else
		{
			reaction.react();
			if(new PickUpGroundItem("Shears").execute())
				Timing.waitCondition(FCConditions.inventoryChanged(Inventory.getAll().length), 3500);
		}
	}

}
