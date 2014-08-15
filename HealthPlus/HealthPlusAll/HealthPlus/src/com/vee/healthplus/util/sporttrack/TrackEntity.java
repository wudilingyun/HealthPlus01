package com.vee.healthplus.util.sporttrack;

import android.os.Parcel;
import android.os.Parcelable;

import com.vee.healthplus.load.TrackLEntity;

public class TrackEntity extends TrackLEntity implements Parcelable {



	private int id;

	private int userid = 0;

	public TrackEntity(TrackLEntity te, int id, int userid) {
		this.id = id;
		this.userid = userid;
		setCalory(te.getCalory());
		setDistance(te.getDistance());
		setDuration(te.getDuration());
		setLatitude(te.getLatitude());
		setLongitude(te.getLongitude());
		setVelocity(te.getVelocity());
	}

	public TrackEntity() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userid;
	}

	public void setUserId(Integer userid) {
		this.userid = userid;
	}

	
	
	
	public static final Parcelable.Creator<TrackEntity> CREATOR = new Creator<TrackEntity>() {  
        public TrackEntity createFromParcel(Parcel source) {  
        	TrackEntity entity = new TrackEntity();  
            entity.id = source.readInt();  
        	entity.userid = source.readInt();  
        	entity.calory = source.readString();  
        	entity.distance = source.readString();  
        	entity.duration = source.readString();  
        	entity.latitude = source.readString();  
        	entity.longitude = source.readString();
        	entity.velocity = source.readString();
            return entity;  
        }  
  
        public TrackEntity[] newArray(int size) {  
            return new TrackEntity[size];  
        }  
    };  
  
    @Override  
    public int describeContents() {  
        return 0;  
    }  
  
    /** 
     * 将实体类数据写入Parcel 
     */  
    @Override  
    public void writeToParcel(Parcel parcel, int flags) {  
        parcel.writeInt(id);  
        parcel.writeInt(userid);
        
        parcel.writeString(calory);  
        parcel.writeString(distance);
        parcel.writeString(duration);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(velocity);
    }  
}
