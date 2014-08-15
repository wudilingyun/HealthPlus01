package com.vee.healthplus.load;

import java.util.List;

public class SportLEntity {

	protected int sportid;
	protected Integer sporttypeid = 0;
	protected String time = "0";
	protected int mode = 0;
	protected int sensorid = 0;
	protected List<TrackLEntity> tracklist = null;

	public Integer getId() {
		return sportid;
	}

	public void setId(Integer id) {
		this.sportid = id;
	}

	public Integer getSportid() {
		return sporttypeid;
	}

	public void setSportid(Integer sporttypeid) {
		this.sporttypeid = sporttypeid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getSensorId() {
		return sensorid;
	}

	public void setSensorId(Integer sensorid) {
		this.sensorid = sensorid;
	}

	public void setTrackList(List<TrackLEntity> tracklist) {
//		 if (tracklist == null){
//		 tracklist = new ArrayList<TrackLEntity>();
//		 tracklist.add(new TrackLEntity()); //add one fake data
//		 }

		this.tracklist=tracklist;
	}

	public List<TrackLEntity> getTrack() {
		return tracklist;

	}
}
