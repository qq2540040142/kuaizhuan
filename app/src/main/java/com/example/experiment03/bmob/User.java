package com.example.experiment03.bmob;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    //    身份
    private Integer Identity;
    //    权限
    private Integer Power;
    //    所属
    private String Suoshu;
    //    我的任务集合
    private List<Integer> MineTask;
    //    当前任务领取标识
    private Integer CurrentTask;
    //    当前任务状态标识
    private Integer TaskState;

    public Integer getIdentity(){
        return Identity;
    }
    public void setIdentity(Integer integer){
        this.Identity = integer;
    }
    public String getSuoshu(){return Suoshu;}
    public void setSuoshu(String s){
        this.Suoshu = s;
    }

    public Integer getPower(){
        return Power;
    }
    public void setPower(Integer power){
        this.Power = power;
    }

    public List<Integer> getMineTask() {
        return MineTask;
    }

    public void setMineTask(List<Integer> mineTask) {
        MineTask = mineTask;
    }

    public Integer getCurrentTask() {
        return CurrentTask;
    }

    public void setCurrentTask(Integer currentTask) {
        CurrentTask = currentTask;
    }

    public Integer getTaskState() {
        return TaskState;
    }

    public void setTaskState(Integer taskState) {
        TaskState = taskState;
    }
}
