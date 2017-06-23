package scripts.fc.missions.fcsheepshearer.data;

public enum QuestStage
{
	NOT_STARTED(0),
	STARTED(1),
	WOOL_DELIVERED(20),
	COMPLETED(21);
	
	private int setting;
	
	QuestStage(int setting)
	{
		this.setting = setting;
	}
	
	public int getSetting()
	{
		return setting;
	}
}
