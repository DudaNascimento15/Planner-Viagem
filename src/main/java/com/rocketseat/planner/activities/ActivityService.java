package com.rocketseat.planner.activities;

import com.rocketseat.planner.participantes.ParticipantData;
import com.rocketseat.planner.viagem.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository repository;
    public  ActivityResponse saveActivity(ActivityRequestPayLoad payLoad, Trip trip){
        Activity newActivity = new Activity(payLoad.title(), payLoad.occours_at(), trip);

        this.repository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData>getAllActivitiesFromId(UUID tripId){
            return this.repository.findByTripId(tripId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(),activity.getOccursat())).toList();
        };
}
