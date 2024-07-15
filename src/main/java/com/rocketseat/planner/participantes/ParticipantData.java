package com.rocketseat.planner.participantes;

import java.util.UUID;

public record ParticipantData(UUID id, String name, String email, Boolean isConfirmado) {
}
