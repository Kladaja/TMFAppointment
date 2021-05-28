package com.example.tmfappointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Appointment {
    private int attachment;  // = banner image
    private Date calendarEvent;
    private AppointmentCategory category;
    private String contactMedium;
    private Date creationDate;
    private String description;
    private String externalId;  // = title
    private String href;
    private String id;
    private Date lastUpdate;
    private String note;
    private String relatedEntity;
    private String relatedParty;
    private String relatedPlace;
    private AppointmentStateType status;
    private Date validFor;  // = deadline

    public Appointment() {}

    public Appointment(int attachment,
                       String category,
                       String contactMedium,
                       String description,
                       String externalId,
                       String href,
                       String id,
                       String note,
                       String relatedEntity,
                       String relatedParty,
                       String relatedPlace,
                       String validFor) {
        this.attachment = attachment;
        this.calendarEvent = new Date();
        this.category = AppointmentCategory.valueOf(category);
        this.contactMedium = contactMedium;
        this.creationDate = Calendar.getInstance().getTime();
        this.description = description;
        this.externalId = externalId;
        this.href = href;
        this.id = id;
        this.lastUpdate = Calendar.getInstance().getTime();
        this.note = note;
        this.relatedEntity = relatedEntity;
        this.relatedParty = relatedParty;
        this.relatedPlace = relatedPlace;
        this.status = AppointmentStateType.initialized;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.validFor = format.parse(validFor);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getAttachment() {
        return attachment;
    }
    public Date getCalendarEvent() { return calendarEvent; }
    public AppointmentCategory getCategory() {
        return category;
    }
    public String getContactMedium() {
        return contactMedium;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public String getDescription() {
        return description;
    }
    public String getExternalId() {
        return externalId;
    }
    public String getHref() {
        return href;
    }
    public String getId() {
        return id;
    }
    public Date getLastUpdate() {
        return lastUpdate;
    }
    public String getNote() {
        return note;
    }
    public String getRelatedEntity() {
        return relatedEntity;
    }
    public String getRelatedParty() {
        return relatedParty;
    }
    public String getRelatedPlace() {
        return relatedPlace;
    }
    public AppointmentStateType getStatus() {
        return status;
    }
    public Date getValidFor() {
        return validFor;
    }

    private void setCalendarEvent(Date calendarEvent) { this.calendarEvent = calendarEvent; }
    private void setContactMedium(String contactMedium) { this.contactMedium = contactMedium; }
    private void setDescription(String description) { this.description = description; }
    private void setExternalId(String externalId) { this.externalId = externalId; }
    private void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }
    private void setNote(String note) { this.note = note; }
    private void setRelatedEntity(String relatedEntity) { this.relatedEntity = relatedEntity; }
    private void setRelatedParty(String relatedParty) { this.relatedParty = relatedParty; }
    private void setRelatedPlace(String relatedPlace) { this.relatedPlace = relatedPlace; }
    private void setStatus(AppointmentStateType status) { this.status = status; }
    private void setValidFor(Date validFor) { this.validFor = validFor; }
}
