package com.vee.healthplus.load;

import java.util.List;

public class UserLEntity {

		private int userid;
		private List<SportLEntity> sportlist = null;
		
		public Integer getUserId() {
			return userid;
		}

		public void setUserId(Integer userid) {
			this.userid = userid;
		}

		public void setSportList(List<SportLEntity> sportlist) {
			this.sportlist=sportlist;
		}

		public List<SportLEntity> getTrack() {
			return sportlist;

		}
	}


