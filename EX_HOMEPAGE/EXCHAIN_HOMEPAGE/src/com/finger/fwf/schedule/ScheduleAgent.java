package com.finger.fwf.schedule;

import java.util.LinkedHashMap;

import com.finger.fwf.schedule.task.FWFScheduler;

public class ScheduleAgent extends FWFScheduler {

    public ScheduleAgent() { }

    public boolean isPossibility() {
        return _isPossibility;
    }

    public int getScheduleCount() {
        return super.getScheduleCount();
    }

    public String[] getScheduleList() throws Exception {
        return super.getScheduleList();
    }

    public void startingSchedule(String scheduleId, LinkedHashMap<String, String> scheduleInfo) throws Exception {
        super.startingSchedule(scheduleId, scheduleInfo);
    }

    public void terminateSchedule(String scheduleId) throws Exception {
        super.terminateSchedule(scheduleId);
    }
    public void terminateScheduleSMN(String sysMgtNo) throws Exception {
        super.terminateScheduleSMN(sysMgtNo);
    }
    public void terminateScheduleAll() throws Exception {
        super.terminateScheduleAll();
    }

    public void immediatelyExecute(String scheduleId) throws Exception {
        super.immediatelyExecute(scheduleId);
    }

    public boolean isRunningSchedule(String scheduleId) throws Exception {
        return super.isRunningSchedule(scheduleId);
    }
    public void setStateRunning(String scheduleId) throws Exception {
        super.setStateRunning( scheduleId );
    }
    public void setStateWaiting(String scheduleId) throws Exception {
        super.setStateWaiting( scheduleId );
    }

    public void debugScheduleInfo() throws Exception {
        super.debugScheduleInfo();
    }
}