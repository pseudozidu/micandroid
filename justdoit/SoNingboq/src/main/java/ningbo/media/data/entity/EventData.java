package ningbo.media.data.entity;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ningbo.media.rest.WsConstants;
import ningbo.media.rest.dto.SystemUserData;

@XmlType(name = "Event", namespace = WsConstants.NS, propOrder = { "md5Value",
		"title", "subject", "address", "startDate", "startTime", "endDate",
		"endTime", "photo_path", "organizer","telephone", "location",
		"user" })
@XmlRootElement
public class EventData {

	private String md5Value;

	private String title;

	private String subject;

	private String address;

	private String startDate;

	private String startTime;

	private String endDate;

	private String endTime;

	private String photo_path;

	private String organizer;
	
	private String telephone ;

	private LocationDetail location;

	private SystemUserData user ;

	public String getMd5Value() {
		return md5Value;
	}

	public void setMd5Value(String md5Value) {
		this.md5Value = md5Value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPhoto_path() {
		return photo_path;
	}

	public void setPhoto_path(String photo_path) {
		this.photo_path = photo_path;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}


	public SystemUserData getUser() {
		return user;
	}

	public void setUser(SystemUserData user) {
		this.user = user;
	}

	public LocationDetail getLocation() {
		return location;
	}

	public void setLocation(LocationDetail location) {
		this.location = location;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	
}
