package scripts.fc.missions.fcsheepshearer.tasks;

import org.tribot.api.Timing;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Filter;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;

import scripts.fc.api.abc.ABC2Helper;
import scripts.fc.api.generic.FCConditions;
import scripts.fc.api.interaction.EntityInteraction;
import scripts.fc.api.interaction.impl.npcs.ClickNpc;
import scripts.fc.api.interaction.impl.objects.ClickObject;
import scripts.fc.api.travel.Travel;
import scripts.fc.api.wrappers.FCTiming;
import scripts.fc.framework.data.Vars;
import scripts.fc.framework.task.AnticipativeTask;
import scripts.fc.framework.task.PredictableInteraction;
import scripts.fc.framework.task.Task;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fcsheepshearer.data.QuestStage;

public class ShearSheep extends AnticipativeTask implements PredictableInteraction
{
	private static final long serialVersionUID = 5739321063964049443L;

	public static final RSArea SHEEP_PEN = new RSArea(new RSTile[]{
			new RSTile(3193, 3277, 0), new RSTile(3211, 3277, 0),
			new RSTile(3212, 3257, 0), new RSTile(3193, 3257, 0)});
	
	private final Filter<RSNPC> CURRENT_SHEEP_FILTER = currentSheepFilter();
	private final Positionable STILE_TILE = new RSTile(3197, 3278, 0);
	
	private RSNPC currentSheep;
	
	@Override
	public boolean execute()
	{
		if(!SHEEP_PEN.contains(Player.getPosition()))
			enterPen();
		else
			return shearSheep();
		
		return false;
	}

	@Override
	public boolean shouldExecute()
	{
		return Game.getSetting(FCSheepShearer.QUEST_SETTING_INDEX) == QuestStage.STARTED.getSetting()
				&& Inventory.getCount("Wool") < 20 && Inventory.getCount("Ball of wool") == 0
				&& Inventory.getCount("Shears") > 0;
	}

	@Override
	public String getStatus()
	{
		return "Shear sheep";
	}
	
	private boolean shearSheep()
	{
		RSPlayer player = Player.getRSPlayer();
		EntityInteraction inter = getInteractable();
		if(player == null || inter == null) return false;
		
		return inter.execute() && FCTiming.waitCondition(() -> player.getInteractingCharacter() != null, 1200);
	}
	
	private void enterPen()
	{
		if(Player.getPosition().distanceTo(STILE_TILE) > 3)
			Travel.webWalkTo(STILE_TILE);
		else
			if(new ClickObject("Climb-over", "Stile", 5).execute())
				Timing.waitCondition(FCConditions.inAreaCondition(SHEEP_PEN), 4000);
	}
	
	@Override
	public EntityInteraction getInteractable()
	{
		RSNPC[] npcs = NPCs.findNearest(Filters.NPCs.nameEquals("Sheep")
				.combine(Filters.NPCs.actionsContains("Shear"), false)
				.combine(Filters.NPCs.actionsNotContains("Talk-to"), false)
				.combine(Filters.NPCs.inArea(SHEEP_PEN), false)
				.combine(CURRENT_SHEEP_FILTER, true));
		
		if(npcs.length > 0)
		{
			ABCUtil abc = Vars.get().get("abc");
			currentSheep = ABC2Helper.shouldUseClosest(abc, npcs) ? npcs[0] : npcs[1];
			return new ClickNpc("Shear", currentSheep);
		}
		
		return null;
	}
	
	private Filter<RSNPC> currentSheepFilter()
	{
		return new Filter<RSNPC>()
		{
			@Override
			public boolean accept(RSNPC n)
			{
				return currentSheep == null || !n.equals(currentSheep);
			}
		};
	}
	
	@Override
	public Task getNext()
	{
		return shouldExecute() ? this : null;
	}
	
	@Override
	public void waitForTaskComplete()
	{
		int invCount = Inventory.getAll().length;
		FCTiming.waitCondition(() -> Inventory.getAll().length > invCount 
				|| (Player.getRSPlayer().getInteractingCharacter() == null && Player.getAnimation() == -1), 8000);
	}

}
