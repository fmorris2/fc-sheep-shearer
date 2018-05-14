package scripts.fc.missions.fcsheepshearer;

import java.util.Arrays;
import java.util.LinkedList;

import org.tribot.api.General;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.types.RSTile;

import scripts.fc.framework.mission.MissionManager;
import scripts.fc.framework.quest.QuestMission;
import scripts.fc.framework.script.FCMissionScript;
import scripts.fc.framework.task.Task;
import scripts.fc.missions.fcsheepshearer.data.QuestStage;
import scripts.fc.missions.fcsheepshearer.tasks.CompleteQuest;
import scripts.fc.missions.fcsheepshearer.tasks.GrabShears;
import scripts.fc.missions.fcsheepshearer.tasks.InventoryCheck;
import scripts.fc.missions.fcsheepshearer.tasks.ShearSheep;
import scripts.fc.missions.fcsheepshearer.tasks.SpinWool;
import scripts.fc.missions.fcsheepshearer.tasks.StartQuest;

public class FCSheepShearer extends MissionManager implements QuestMission
{
	public static final int QUEST_SETTING_INDEX = 179;
	public static final Positionable FARMER_TILE = new RSTile(3190, 3273, 0);
	
	public FCSheepShearer(FCMissionScript script)
	{
		super(script);
	}
	
	@Override
	public boolean hasReachedEndingCondition()
	{
		return Game.getSetting(QUEST_SETTING_INDEX) == QuestStage.COMPLETED.getSetting();
	}

	@Override
	public String getMissionName()
	{
		return "Sheep Shearer";
	}

	@Override
	public String getCurrentTaskName()
	{
		return currentTask == null ? "null" : currentTask.getStatus();
	}

	@Override
	public void execute()
	{	
		if(Login.getLoginState() != STATE.INGAME)
		{
			General.println("Waiting for login...");
			return;
		}
		
		executeTasks();
	}

	@Override
	public LinkedList<Task> getTaskList()
	{
		return new LinkedList<Task>(Arrays.asList(
				new InventoryCheck(), new StartQuest(), new GrabShears(), 
				new ShearSheep(), new SpinWool(), new CompleteQuest()));
	}

	@Override
	public String getEndingMessage()
	{
		return "Sheep Shearer has been completed.";
	}
	
	public String toString()
	{
		return getMissionName();
	}

	@Override
	public String[] getMissionSpecificPaint()
	{
		return new String[0];
	}

	@Override
	public void resetStatistics()
	{
	}

	@Override
	public boolean canStart() {
		return true;
	}

	@Override
	public int getQuestPointReward() {
		return 1;
	}

}
