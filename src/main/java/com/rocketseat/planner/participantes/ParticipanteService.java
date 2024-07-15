package com.rocketseat.planner.participantes;

import com.rocketseat.planner.viagem.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipanteService {
    @Autowired
    private ParticipantRepository repository;

    public void registerParticipatsToEvent(List<String> participantsToInvite, Trip trips){
        if (participantsToInvite == null || participantsToInvite.isEmpty()) {
            throw new IllegalArgumentException("A lista de participantes a convidar não pode ser nula ou vazia.");
        }
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email,trips)).toList();

        this.repository.saveAll(participants);
        System.out.println(participants.get(0).getId());
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip){
        Participant participant = new Participant(email,trip);
        this.repository.save(participant);

        return new ParticipantCreateResponse(participant.getId());
    }
    public void triggerConfirmationEmailToParticipants(UUID tripId){};

    public void triggerConfirmationEmailToParticipant(String email){};

    public List<ParticipantData> getAllParticipantsFromEvent(UUID tripId){
        return this.repository.findByTripId(tripId).stream().map(participant -> new ParticipantData(participant.getId(), participant.getName(), participant.getEmail(), participant.isConfirmado())).toList();
    }
}
