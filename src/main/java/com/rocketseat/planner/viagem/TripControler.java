package com.rocketseat.planner.viagem;

import com.rocketseat.planner.activities.ActivityData;
import com.rocketseat.planner.activities.ActivityRequestPayLoad;
import com.rocketseat.planner.activities.ActivityResponse;
import com.rocketseat.planner.activities.ActivityService;
import com.rocketseat.planner.links.LinkData;
import com.rocketseat.planner.links.LinkRequestPayLoad;
import com.rocketseat.planner.links.LinkResponse;
import com.rocketseat.planner.links.LinkService;
import com.rocketseat.planner.participantes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripControler {


    @Autowired
    private ParticipanteService participanteService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;



    @Autowired
    private TripRepository repository;
    @Autowired
    private TripRepository tripRepository;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripResquestPayLoad payLoad){
      Trip newTrip = new Trip(payLoad);

      this.repository.save(newTrip);
      this.participanteService.registerParticipatsToEvent(payLoad.email_to_invite(), newTrip);

      return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));

    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
        Optional<Trip> trip  = this.repository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripResquestPayLoad payLoad){
        Optional<Trip> trip  = this.repository.findById(id);
        if(trip.isPresent()){
            Trip roalTrip = trip.get();
            roalTrip.setEndsAt(LocalDateTime.parse(payLoad.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            roalTrip.setStartsAt(LocalDateTime.parse(payLoad.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            roalTrip.setDestination(payLoad.destination());

            this.repository.save(roalTrip);

            return ResponseEntity.ok(roalTrip);

        }
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id){
        Optional<Trip> trip  = this.repository.findById(id);
        if(trip.isPresent()){
            Trip roalTrip = trip.get();
            roalTrip.setIsconfirmed(true);

            this.repository.save(roalTrip);
            this.participanteService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(roalTrip);

        }
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayLoad payLoad){
        Optional<Trip> trip  = this.repository.findById(id);
        if(trip.isPresent()){
            Trip roalTrip = trip.get();
            ActivityResponse activityResponse = this.activityService.saveActivity(payLoad, roalTrip);

            return ResponseEntity.ok(activityResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok(activityDataList);
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticpant(@PathVariable UUID id, @RequestBody ParticipantRequestePayLoad payLoad){
        Optional<Trip> trip  = this.repository.findById(id);
        if(trip.isPresent()){
            Trip roalTrip = trip.get();

            ParticipantCreateResponse participantCreateResponse = this.participanteService.registerParticipantToEvent(payLoad.email(), roalTrip);
            if(roalTrip.getIsconfirmed()) this.participanteService.triggerConfirmationEmailToParticipant(payLoad.email());

            return ResponseEntity.ok(participantCreateResponse);

        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){
        List<ParticipantData> participantList = this.participanteService.getAllParticipantsFromEvent(id);

        return ResponseEntity.ok(participantList);
    }


    //LINKS

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayLoad payLoad){
        Optional<Trip> trip  = this.repository.findById(id);
        if(trip.isPresent()){
            Trip roalTrip = trip.get();
            LinkResponse linkResponse = this.linkService.saveLink(payLoad, roalTrip);

            return ResponseEntity.ok(linkResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id){
        List<LinkData> linkDataList = this.linkService.getAllLinksFromTrip(id);

        return ResponseEntity.ok(linkDataList);
    }



}
