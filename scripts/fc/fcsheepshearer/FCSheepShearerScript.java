package scripts.fc.fcsheepshearer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.fc.framework.mission.Mission;
import scripts.fc.framework.paint.FCPaintable;
import scripts.fc.framework.script.FCMissionScript;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;

@ScriptManifest(
		authors     = { 
		    "Final Calibur",
		}, 
		category    = "Quests", 
		name        = "FC Sheep Shearer", 
		version     = 0.1, 
		description = "Completes Sheep Shearer for you. Start anywhere. Will collect shears for you if you don't have them.", 
		gameMode    = 1)

public class FCSheepShearerScript extends FCMissionScript implements FCPaintable, Painting, Starting, Ending
{	
	@Override
	protected Queue<Mission> getMissions()
	{
		return new LinkedList<Mission>(Arrays.asList(new FCSheepShearer(this)));
	}

	@Override
	protected String[] scriptSpecificPaint()
	{
		return new String[]{};
	}
}
