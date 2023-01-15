package com.zanckor.questapi.createQuest;

import java.util.List;

public class ServerQuest {
    public int id;
    public String title;

    public String quest_type;
    public List<String> quest_target;
    public List<Integer> target_quantity;

    public boolean hasTimeLimit;
    public int timeLimitInSeconds;

    public String requirements_type;
    public int requirements_min;
    public int requirements_max;

    public String reward_type;
    public List<String> reward;
    public List<Integer> reward_quantity;


    public static ServerQuest createQuest(ServerQuest abstractQuest){
        ServerQuest playerQuest = new ServerQuest();

        playerQuest.setId(abstractQuest.getId());
        playerQuest.setTitle(abstractQuest.getTitle());
        playerQuest.setQuest_type(abstractQuest.getQuest_type());
        playerQuest.setQuest_target(abstractQuest.getQuest_target());
        playerQuest.setTarget_quantity(abstractQuest.getTarget_quantity());
        playerQuest.setHasTimeLimit(abstractQuest.isHasTimeLimit());
        playerQuest.setTimeLimitInSeconds(abstractQuest.getTimeLimitInSeconds());
        playerQuest.setRequirements_type(abstractQuest.getRequirements_type());
        playerQuest.setRequirements_min(abstractQuest.getRequirements_min());
        playerQuest.setRequirements_max(abstractQuest.getRequirements_max());
        playerQuest.setReward_type(abstractQuest.getReward_type());
        playerQuest.setReward(abstractQuest.getReward());
        playerQuest.setReward_quantity(abstractQuest.getReward_quantity());

        return playerQuest;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuest_type() {
        return quest_type;
    }

    public void setQuest_type(String quest_type) {
        this.quest_type = quest_type;
    }

    public List<String> getQuest_target() {
        return quest_target;
    }

    public void setQuest_target(List<String> quest_target) {
        this.quest_target = quest_target;
    }

    public List<Integer> getTarget_quantity() {
        return target_quantity;
    }

    public void setTarget_quantity(List<Integer> target_quantity) {
        this.target_quantity = target_quantity;
    }

    public String getRequirements_type() {
        return requirements_type;
    }

    public void setRequirements_type(String requirements_type) {
        this.requirements_type = requirements_type;
    }

    public int getRequirements_min() {
        return requirements_min;
    }

    public void setRequirements_min(int requirements_min) {
        this.requirements_min = requirements_min;
    }

    public int getRequirements_max() {
        return requirements_max;
    }

    public void setRequirements_max(int requirements_max) {
        this.requirements_max = requirements_max;
    }

    public String getReward_type() {
        return reward_type;
    }

    public void setReward_type(String reward_type) {
        this.reward_type = reward_type;
    }

    public List<String> getReward() {
        return reward;
    }

    public void setReward(List<String> reward) {
        this.reward = reward;
    }

    public List<Integer> getReward_quantity() {
        return reward_quantity;
    }

    public void setReward_quantity(List<Integer> reward_quantity) {
        this.reward_quantity = reward_quantity;
    }

    public boolean isHasTimeLimit() {
        return hasTimeLimit;
    }

    public void setHasTimeLimit(boolean hasTimeLimit) {
        this.hasTimeLimit = hasTimeLimit;
    }

    public int getTimeLimitInSeconds() {
        return timeLimitInSeconds;
    }

    public void setTimeLimitInSeconds(int timeLimitInSeconds) {
        this.timeLimitInSeconds = timeLimitInSeconds;
    }
}
