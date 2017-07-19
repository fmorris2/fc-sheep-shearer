package scripts.fc.missions.fcsheepshearer.tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSTile;

import scripts.fc.api.abc.PersistantABCUtil;
import scripts.fc.api.generic.FCConditions;
import scripts.fc.api.interaction.impl.objects.ClickObject;
import scripts.fc.api.travel.FCTeleporting;
import scripts.fc.api.travel.Travel;
import scripts.fc.framework.data.Vars;
import scripts.fc.framework.task.Task;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fcsheepshearer.data.QuestStage;

public class SpinWool extends Task
{
	private static final long serialVersionUID = 8650835682869360935L;
	
	private final RSArea KITCHEN_AREA = new RSArea(new RSTile(3205, 3217, 0), new RSTile(3212, 3211, 0));	
	private final Positionable WHEEL_TILE = new RSTile(3209, 3213, 1);
	private final boolean SHOULD_TELEPORT = General.random(0, 1) == 0;
	private final int INTERFACE_MASTER = 459;
	private final int INTERFACE_CHILD = 100;
	private final int ANIMATION_ID = 894;
	private final long ANIMATION_TIMEOUT = 2000;
	private final int ESTIMATED_SPIN_WAIT = 50000;
	
	private long lastAnimation;
	
	@Override
	public boolean execute()
	{
		if(KITCHEN_AREA.contains(Player.getPosition()))
		{
			General.println("Failsafe out of kitchen");
			if(new ClickObject("Climb-up", "Staircase", 15).execute())
				Timing.waitCondition(FCConditions.planeChanged(0), 3500);
		}
		else if(!Combat.isUnderAttack() && ShearSheep.SHEEP_PEN.contains(Player.getPosition()) 
				&& (!SHOULD_TELEPORT || !FCTeleporting.homeTeleport()))
			Travel.webWalkTo(WHEEL_TILE);
		else if(Player.getPosition().distanceTo(WHEEL_TILE) > 1)
		{
			GameTab.open(TABS.INVENTORY);
			Travel.webWalkTo(WHEEL_TILE);
		}
		else
			spinWool();
		
		return false;
	}

	@Override
	public boolean shouldExecute()
	{
		return Game.getSetting(FCSheepShearer.QUEST_SETTING_INDEX) == QuestStage.STARTED.getSetting()
				&& Inventory.getCount("Ball of wool") + Inventory.getCount("Wool") >= 20 &&
				Inventory.getCount("Ball of wool") < 20;
	}

	@Override
	public String getStatus()
	{
		return "Spin wool";
	}
	
	private void spinWool()
	{
		if(Timing.timeFromMark(lastAnimation) > ANIMATION_TIMEOUT) //not spinning
		{
			handleWheel();
		}
		else //spinning
		{
			((PersistantABCUtil)(Vars.get().get("abc2"))).performTimedActions();
			
			if(Player.getAnimation() == ANIMATION_ID)
				lastAnimation = Timing.currentTimeMillis();
		}
	}
	
	private void handleWheel()
	{
		RSInterface inter = Interfaces.get(INTERFACE_MASTER, INTERFACE_CHILD);
		
		if(inter == null || inter.isHidden())
		{
			if(new ClickObject("Spin", "Spinning wheel", 5).execute())
				Timing.waitCondition(FCConditions.interfaceUp(INTERFACE_MASTER), 3000);
		}
		else if(inter.click("Make X") && Timing.waitCondition(FCConditions.ENTER_AMOUNT_CONDITION, 2000))
		{
			Keyboard.typeSend(""+Inventory.getCount("Wool"));
			if(Timing.waitCondition(FCConditions.animationChanged(-1), 2400))
			{
				PersistantABCUtil abc2 = Vars.get().get("abc2");
				lastAnimation = Timing.currentTimeMillis();
				Vars.get().addOrUpdate("spinStartTime", Timing.currentTimeMillis());
				abc2.generateTrackers(abc2.generateBitFlags(ESTIMATED_SPIN_WAIT));
			}
		}
	}

}
