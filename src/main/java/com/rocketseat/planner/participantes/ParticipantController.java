package com.rocketseat.planner.participantes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")



public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestePayLoad payLoad) {
        Optional<Participant> participant = this.participantRepository.findById(id);

        if(participant.isPresent()){
           Participant rawParticipant = participant.get();
           rawParticipant.setConfirmado(true);
           rawParticipant.setName(payLoad.name());

           this.participantRepository.save(rawParticipant);
           return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();

    }
}
