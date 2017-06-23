package scripts.fc.missions.fcsheepshearer.tasks;

import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;

import scripts.fc.api.generic.FCConditions;
import scripts.fc.api.travel.Travel;
import scripts.fc.framework.task.Task;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fcsheepshearer.data.QuestStage;

public class InventoryCheck extends Task
{
	private final int MAX_INVENTORY_SIZE = 7;
	
	@Override
	public boolean execute()
	{
		if(Banking.isInBank())
		{
			if(Banking.isBankScreenOpen())
				Banking.depositAll();
			else
				if(Banking.openBank())
					Timing.waitCondition(FCConditions.BANK_LOADED_CONDITION, 4000);
		}
		else
			Travel.walkToBank();
		
		return false;
	}

	@Override
	public boolean shouldExecute()
	{
		return Game.getSetting(FCSheepShearer.QUEST_SETTING_INDEX) == QuestStage.NOT_STARTED.getSetting()
				&& Inventory.getAll().length > MAX_INVENTORY_SIZE;
	}

	@Override
	public String getStatus()
	{
		return "Inventory check";
	}

}
